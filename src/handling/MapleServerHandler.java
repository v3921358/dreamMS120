/*
 This file is part of the OdinMS Maple Story Server
 Copyright (C) 2008 ~ 2010 Patrick Huy <patrick.huy@frz.cc> 
 Matthias Butz <matze@odinms.de>
 Jan Christian Meyer <vimes@odinms.de>

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License version 3
 as published by the Free Software Foundation. You may not use, modify
 or distribute this program under any other version of the
 GNU Affero General Public License.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.

 You should have received a copy of the GNU Affero General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package handling;

import Apple.client.WorldFindService;
import constants.ServerConstants;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import client.MapleClient;
import handling.cashshop.CashShopServer;
import handling.channel.ChannelServer;
import handling.cashshop.handler.*;
import handling.channel.handler.*;
import handling.login.LoginServer;
import handling.login.handler.*;
import handling.world.World;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.io.File;
import java.io.FileWriter;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import scripting.AbstractPlayerInteraction;
import scripting.NPCScriptManager;
import server.Randomizer;
import tools.MapleAESOFB;
import tools.packet.LoginPacket;
import tools.Pair;

import server.ServerProperties;
import tools.FileoutputUtil;
import tools.HexTool;
import tools.data.ByteArrayByteStream;
import tools.data.LittleEndianAccessor;

public class MapleServerHandler extends ChannelHandlerAdapter {

    public static final boolean Log_Packets = true;
    private int channel = -1;
    private static int numDC = 0;
    private static long lastDC = System.currentTimeMillis();
    private boolean cs;
    private final List<String> BlockedIP = new ArrayList<String>();
    private final Map<String, Pair<Long, Byte>> tracker = new ConcurrentHashMap<>();
    //Screw locking. Doesn't matter.
//    private static final ReentrantReadWriteLock IPLoggingLock = new ReentrantReadWriteLock();
    private static final String nl = System.getProperty("line.separator");
    private static final File loggedIPs = new File("Logs/LogIPs.txt");
    private static final HashMap<String, FileWriter> logIPMap = new HashMap<String, FileWriter>();
    private static boolean debugMode = Boolean.parseBoolean(ServerProperties.getProperty("tms.Debug", "false"));
    //Note to Zero: Use an enumset. Don't iterate through an array.
    private static final EnumSet<RecvPacketOpcode> blocked = EnumSet.noneOf(RecvPacketOpcode.class), sBlocked = EnumSet.noneOf(RecvPacketOpcode.class);
    public final static int CASH_SHOP_SERVER = -10;
    public final static int LOGIN_SERVER = 0;

    static {
        reloadLoggedIPs();
        RecvPacketOpcode[] block = new RecvPacketOpcode[]{RecvPacketOpcode.NPC_ACTION, RecvPacketOpcode.MOVE_PLAYER, RecvPacketOpcode.MOVE_PET, RecvPacketOpcode.MOVE_SUMMON, RecvPacketOpcode.MOVE_DRAGON, RecvPacketOpcode.MOVE_LIFE, RecvPacketOpcode.HEAL_OVER_TIME, RecvPacketOpcode.STRANGE_DATA};
        blocked.addAll(Arrays.asList(block));
    }

    public static void reloadLoggedIPs() {
//        IPLoggingLock.writeLock().lock();
//        try {
        for (FileWriter fw : logIPMap.values()) {
            if (fw != null) {
                try {
                    fw.write("=== Closing Log ===");
                    fw.write(nl);
                    fw.flush(); //Just in case.
                    fw.close();
                } catch (IOException ex) {
                    System.out.println("Error closing Packet Log.");
                    System.out.println(ex);
                }
            }
        }
        logIPMap.clear();
        try {
            Scanner sc = new Scanner(loggedIPs);
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.length() > 0) {
                    FileWriter fw = new FileWriter(new File("PacketLog_" + line + ".txt"), true);
                    fw.write("=== Creating Log ===");
                    fw.write(nl);
                    fw.flush();
                    logIPMap.put(line, fw);
                }
            }
        } catch (Exception e) {
            System.out.println("Could not reload packet logged IPs.");
            System.out.println(e);
        }
//        } finally {
//            IPLoggingLock.writeLock().unlock();
//        }
    }

    //Return the Filewriter if the IP is logged. Null otherwise.
    private static FileWriter isLoggedIP(Channel sess) {
        String a = sess.remoteAddress().toString();
        String realIP = a.substring(a.indexOf('/') + 1, a.indexOf(':'));
        return logIPMap.get(realIP);
    }
// <editor-fold defaultstate="collapsed" desc="Packet Log Implementation">
    private static final int Log_Size = 10000;
    private static final ArrayList<LoggedPacket> Packet_Log = new ArrayList<LoggedPacket>(Log_Size);
    private static final ReentrantReadWriteLock Packet_Log_Lock = new ReentrantReadWriteLock();
    private static final File Packet_Log_Output = new File("PacketLog.txt");

    public static void log(String packet, String op, MapleClient c, Channel io) {
        try {
            Packet_Log_Lock.writeLock().lock();
            LoggedPacket logged = null;
            if (Packet_Log.size() == Log_Size) {
                logged = Packet_Log.remove(0);
            }
            //This way, we don't create new LoggedPacket objects, we reuse them =]
            if (logged == null) {
                logged = new LoggedPacket(packet, op, io.remoteAddress().toString(),
                        c == null ? -1 : c.getAccID(), FileoutputUtil.CurrentReadable_Time(),
                        c == null || c.getAccountName() == null ? "[Null]" : c.getAccountName(),
                        c == null || c.getPlayer() == null || c.getPlayer().getName() == null ? "[Null]" : c.getPlayer().getName(),
                        c == null || c.getPlayer() == null || c.getPlayer().getMap() == null ? "[Null]" : String.valueOf(c.getPlayer().getMapId()),
                        c == null || NPCScriptManager.getInstance().getCM(c) == null ? "[Null]" : String.valueOf(NPCScriptManager.getInstance().getCM(c).getNpc()));
            } else {
                logged.setInfo(packet, op, io.remoteAddress().toString(),
                        c == null ? -1 : c.getAccID(), FileoutputUtil.CurrentReadable_Time(),
                        c == null || c.getAccountName() == null ? "[Null]" : c.getAccountName(),
                        c == null || c.getPlayer() == null || c.getPlayer().getName() == null ? "[Null]" : c.getPlayer().getName(),
                        c == null || c.getPlayer() == null || c.getPlayer().getMap() == null ? "[Null]" : String.valueOf(c.getPlayer().getMapId()),
                        c == null || NPCScriptManager.getInstance().getCM(c) == null ? "[Null]" : String.valueOf(NPCScriptManager.getInstance().getCM(c).getNpc()));
            }
            Packet_Log.add(logged);
        } finally {
            Packet_Log_Lock.writeLock().unlock();
        }
    }

    private static class LoggedPacket {

        private static final String nl = System.getProperty("line.separator");
        private String ip, accName, accId, chrName, packet, mapId, npcId, op, time;
        private long timestamp;

        public LoggedPacket(String p, String op, String ip, int id, String time, String accName, String chrName, String mapId, String npcId) {
            setInfo(p, op, ip, id, time, accName, chrName, mapId, npcId);
        }

        public final void setInfo(String p, String op, String ip, int id, String time, String accName, String chrName, String mapId, String npcId) {
            this.ip = ip;
            this.op = op;
            this.time = time;
            this.packet = p;
            this.accName = accName;
            this.chrName = chrName;
            this.mapId = mapId;
            this.npcId = npcId;
            timestamp = System.currentTimeMillis();
            this.accId = String.valueOf(id);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("[IP: ").append(ip).append("] [").append(accId).append('|').append(accName).append('|').append(chrName).append("] [").append(npcId).append('|').append(mapId).append("] [Time: ").append(timestamp).append("] [").append(time).append(']');
            sb.append(nl);
            sb.append("[Op: ").append(op).append("] [").append(packet).append(']');
            return sb.toString();
        }
    }

    public void writeLog() {
        try {
            FileWriter fw = new FileWriter(Packet_Log_Output, true);
            try {
                Packet_Log_Lock.readLock().lock();
                String nl = System.getProperty("line.separator");
                for (LoggedPacket loggedPacket : Packet_Log) {
                    fw.write(loggedPacket.toString());
                    fw.write(nl);
                }
                fw.flush();
                fw.close();
            } finally {
                Packet_Log_Lock.readLock().unlock();
            }
        } catch (IOException ex) {
            System.out.println("Error writing log to file.");
        }
    }

    public MapleServerHandler() {
        //ONLY FOR THE MBEAN
    }
    // </editor-fold>

    private static ServerType type = null;

    public MapleServerHandler(final int channel, ServerType type) {
        this.channel = channel;
        this.type = type;
    }

    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) throws Exception {
        /*	MapleClient client = (MapleClient) session.getAttribute(MapleClient.CLIENT_KEY);
         log.error(MapleClient.getLogMessage(client, cause.getMessage()), cause);*/
//	cause.printStackTrace();
        if (!(cause instanceof IOException)) {
            MapleClient client = ctx.channel().attr(MapleClient.CLIENT_KEY).get();
            if (client != null && client.getPlayer() != null) {
                //client.getPlayer().saveToDB(false, false);
                //client.getPlayer().saveToCache();//保存到緩存
                if (ServerConstants.LOG_PACKETS) {
                    FileoutputUtil.log(FileoutputUtil.捕捉異常, "Exception caught by: " + client.getPlayer().getName() + " cause:" + cause);
                    System.err.println(MapleClient.getLogMessage(client, cause.getMessage()));
                }
                //log.error("Exception caught by: " + client.getPlayer().getName(), cause);
            }
        }
    }

    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
        // Start of IP checking
        final String address = ctx.channel().remoteAddress().toString().split(":")[0];
        //重複IP檢測
        /* if (ServerConstants.RepeatIP.contains(address)) {
            System.err.println("[注意] 偵測重複IP" + address);
            ctx.channel().close();
            return;
        }*/
        if (BlockedIP.contains(address)) {
            ctx.channel().close();
            return;
        }
        final Pair<Long, Byte> track = tracker.get(address);

        byte count;
        if (track == null) {
            count = 1;
        } else {
            count = track.right;
            final long difference = System.currentTimeMillis() - track.left;
            if (difference < 2000) { //同一個IP地址連接時間檢測 當前為2秒
                count++;
            } else if (difference > 20000) { //清除連接次數的時間 當前為20秒
                count = 1;
            }
            if (count > 5) { // 單個IP的連接上限 達到多少次就禁止連接
                BlockedIP.add(address);
                tracker.remove(address); // Cleanup
                ctx.channel().close();
                return;
            }
        }
        tracker.put(address, new Pair<>(System.currentTimeMillis(), count));
        // 結束IP檢測.

        if (channel > -1) {
            if (ChannelServer.getInstance(channel).isShutdown()) {
                ctx.channel().close();
                return;
            }
        } else if (type == ServerType.商城伺服器) {
            if (CashShopServer.isShutdown()) {
                ctx.channel().close();
                return;
            }
        } else {
            if (LoginServer.isShutdown()) {
                ctx.channel().close();
                return;
            }
        }
        final byte serverRecv[] = new byte[]{70, 114, 122, (byte) Randomizer.nextInt(255)};
        final byte serverSend[] = new byte[]{82, 48, 120, (byte) Randomizer.nextInt(255)};
        final byte ivRecv[] = ServerConstants.Use_Fixed_IV ? new byte[]{9, 0, 0x5, 0x5F} : serverRecv;
        final byte ivSend[] = ServerConstants.Use_Fixed_IV ? new byte[]{1, 0x5F, 4, 0x3F} : serverSend;

        final MapleClient client = new MapleClient(
                new MapleAESOFB(ivSend, (short) (0xFFFF - ServerConstants.MAPLE_VERSION)), // Sent Cypher
                new MapleAESOFB(ivRecv, ServerConstants.MAPLE_VERSION), // Recv Cypher
                ctx.channel());

        client.setChannel(channel);

        server.netty.MaplePacketDecoder.DecoderState decoderState = new server.netty.MaplePacketDecoder.DecoderState();
        ctx.channel().attr(server.netty.MaplePacketDecoder.DECODER_STATE_KEY).set(decoderState);

        ctx.channel().writeAndFlush(LoginPacket.getHello(ServerConstants.MAPLE_VERSION, ivSend, ivRecv));
        ctx.channel().attr(MapleClient.CLIENT_KEY).set(client);

        if (!ServerConstants.Use_Fixed_IV) {
            RecvPacketOpcode.reloadValues();
            SendPacketOpcode.reloadValues();
        }

        StringBuilder sb = new StringBuilder();
        if (channel > -1) {
            sb.append("[Channel Server] Channel ").append(channel).append(" : ");
        } else if (type == ServerType.商城伺服器) {
            sb.append("[Cash Server]");
        } else {
            sb.append("[Login Server]");
        }
        sb.append("IoSession opened ").append(address);
        if (ServerConstants.IP登入訊息) {
            System.out.println(sb.toString());
        }
        World.Client.addClient(client);

        FileWriter fw = isLoggedIP(ctx.channel());
        if (fw != null) {
            if (channel > -1) {
                fw.write("=== Logged Into Channel " + channel + " ===");
                fw.write(nl);
            } else if (type == ServerType.商城伺服器) {
                fw.write("=== Logged Into CashShop Server ===");
                fw.write(nl);
            } else {
                fw.write("=== Logged Into Login Server ===");
                fw.write(nl);
            }
            fw.flush();
        }
    }

    @Override
    public void channelInactive(final ChannelHandlerContext ctx) throws Exception {
        try {
            MapleClient client = (MapleClient) ctx.channel().attr(MapleClient.CLIENT_KEY).get();

            if (client != null) {
                if (client.getPlayer() != null) {
                    client.getPlayer().saveToDB(true, channel == MapleServerHandler.CASH_SHOP_SERVER);
                    if (!(client.getLoginState() == MapleClient.CASH_SHOP_TRANSITION
                            || client.getLoginState() == MapleClient.CHANGE_CHANNEL
                            || client.getLoginState() == MapleClient.LOGIN_SERVER_TRANSITION) && client.getPlayer() != null) {
                        int ch = WorldFindService.getInstance().findChannel(client.getPlayer().getId());
                        if (ChannelServer.getInstance(ch) != null) {
                            ChannelServer.getInstance(ch).removePlayer(client.getPlayer(), "調用位置:" + new java.lang.Throwable().getStackTrace()[0]);
                        }
                        client.disconnect(true, channel == MapleServerHandler.CASH_SHOP_SERVER);
                    }
                }
                if (channel == MapleServerHandler.LOGIN_SERVER) {
                    client.setCanloginpw(false);
                    LoginServer.removeClient(client);
                }
            }

            if (client != null) {
                ctx.channel().attr(MapleClient.CLIENT_KEY).remove();
            }

        } finally {
            super.channelInactive(ctx);
        }
    }

    @Override
    public void channelRead(final ChannelHandlerContext ctx, final Object message) {
        final LittleEndianAccessor slea = new LittleEndianAccessor(new ByteArrayByteStream((byte[]) message));
        if (slea.available() < 2) {
            return;
        }
        final MapleClient c = (MapleClient) ctx.channel().attr(MapleClient.CLIENT_KEY).get();
        if (c == null || !c.isReceiving()) {
            return;
        }
        final short header_num = slea.readShort();
        for (final RecvPacketOpcode recv : RecvPacketOpcode.values()) {
            if (recv.getValue() == header_num) {
                if (recv.NeedsChecking()) {
                    if (!c.isLoggedIn()) {
                        return;
                    }
                }
                if (c.getPlayer() != null && c.isMonitored()) {
                    if (!blocked.contains(recv)) {
                        FileoutputUtil.log("Monitored/" + c.getPlayer().getName() + ".txt", String.valueOf(recv) + " (" + Integer.toHexString(header_num) + ") Handled: \r\n" + slea.toString() + "\r\n");
                    }
                }

                try {
                    handlePacket(recv, slea, c, channel == MapleServerHandler.CASH_SHOP_SERVER);
                    if (c.getPlayer() != null) {
                        // 假斷線處理
                        if (WorldFindService.getInstance().findChannel(c.getPlayer().getName()) == -1) {
                            switch (recv) {
                                case HELLO_CHANNEL:
                                case PLAYER_LOGGEDIN:
                                    break;
                                default:
                                    c.getPlayer().saveToDB(true, c.getChannel() == -10);
                                    c.getSession().close();
                                    return;
                            }
                        }
                    }
                } catch (ArrayIndexOutOfBoundsException AIOBE) {
                    //FileoutputUtil.outputFileError(FileoutputUtil.ArrayEx_Log, AIOBE);
                    FileoutputUtil.log(FileoutputUtil.ArrayEx_Log, "Packet: " + header_num + "\r\n" + slea.toString(true));
                } catch (Exception e) {
                    if (c.getPlayer() != null && c.getPlayer().isGM()) {
                        c.getPlayer().showInfo("數據包異常", true, "包頭:" + recv.name() + "(0x" + Integer.toHexString(header_num).toUpperCase() + ")");
                    }
                    FileoutputUtil.outputFileError(FileoutputUtil.CodeEx_Log, e);
                    FileoutputUtil.outputFileError(FileoutputUtil.PacketEx_Log, e);
                    FileoutputUtil.log(FileoutputUtil.PacketEx_Log, "Packet: " + header_num + "\r\n" + slea.toString(true));
                }
                return;
            }
        }
        if (ServerConstants.LOG_PACKETS) {
            final byte[] packet = slea.read((int) slea.available());
            final StringBuilder sb = new StringBuilder("發現未知用戶端數據包 - (包頭:0x" + Integer.toHexString(header_num) + ")");
            System.err.println(sb.toString());
            sb.append(":\r\n").append(HexTool.toString(packet)).append("\r\n").append(HexTool.toStringFromAscii(packet));
            FileoutputUtil.log(FileoutputUtil.UnknownPacket_Log, sb.toString());
        }
    }

    @Override
    public void userEventTriggered(final ChannelHandlerContext ctx, final Object status) throws Exception {
        final MapleClient client = (MapleClient) ctx.channel().attr(MapleClient.CLIENT_KEY).get();

        if (client != null && client.getPlayer() != null) {
            //System.out.println("玩家 "+ client.getPlayer().getName() +" 正在掛網");
        }
        if (client != null) {
            client.sendPing();
        } else {
            ctx.channel().close();
            return;
        }
        super.userEventTriggered(ctx, status);
    }

    public static final boolean isSpamHeader(RecvPacketOpcode header) {
        switch (header) {
            case MOVE_LIFE://怪物移動
            case MOVE_PLAYER://角色移動
            //case MOVE_ANDROID:
            case MOVE_DRAGON:
            case MOVE_SUMMON:
            //case MOVE_FAMILIAR:
            case MOVE_PET://寵物移動
            case QUEST_ACTION:
            case HEAL_OVER_TIME:
            case NPC_ACTION:
            case SPAWN_PET://招喚寵物
            case TAKE_DAMAGE://造成損害
            case SPECIAL_MOVE://特別動作
            case PONG:
            //case RSA_KEY://金鑰
            case LOGIN_PASSWORD:// 登陸賬號密碼
            case CHARLIST_REQUEST://請求人物列表
            case CHAR_SELECT:// 開始遊戲
            //case CHANGE_CHANNEL://更換頻道
            case PLAYER_LOGGEDIN://登陸請求
                return true;
        }
        return false;
    }

    public static final void handlePacket(final RecvPacketOpcode header, final LittleEndianAccessor slea, final MapleClient c, final boolean cs) throws Exception {
        if (ServerConstants.debugMode && !isSpamHeader(header) && ServerConstants.DEBUG_MODE) {
            //System.out.println("[Recv] 已處理: " + header);
            //System.out.println("封包使用: " + header.name() + "十進制值: " + header.getValue() + " 十六進制值: " + HexTool.getOpcodeToString(header.getValue()));
        }
        switch (header) {
            case CLIENT_LOGOUT:
                PlayerHandler.handleLogout(slea, c);
                break;
            case PONG:
                c.pongReceived();
                break;
            case STRANGE_DATA:
                // Does nothing for now, HackShield's heartbeat
                short type = slea.readShort();
                String type_str = "Unknown?!";
                switch (type) {
                    case 0x01:
                        type_str = "SendBackupPacket";
                        break;
                    case 0x02:
                        type_str = "Crash Report";
                        break;
                    case 0x03:
                        type_str = "Exception";
                        break;
                    default:
                        break;
                }
                int errortype = slea.readInt(); // example error 38
//                if (errortype == 0) { // i don't wanna log error code 0 stuffs, (usually some bounceback to login)
//                    return;
//                }
                short data_length = slea.readShort();
                slea.skip(4); // ?B3 86 01 00 00 00 FF 00 00 00 00 00 9E 05 C8 FF 02 00 CD 05 C9 FF 7D 00 00 00 3F 00 00 00 00 00 02 77 01 00 25 06 C9 FF 7D 00 00 00 40 00 00 00 00 00 02 C1 02
                short opcodeheader = slea.readShort();
                byte[] opcode = slea.read((int) slea.available());
                int packetLen = (int) slea.available() + 2;
                if (errortype == 38) {
                    System.err.println("收到用戶端的報錯: (詳細請看日誌/用戶端_報錯.txt) " + SendPacketOpcode.nameOf(opcodeheader) + "包頭:" + HexTool.getOpcodeToString(opcodeheader) + " [" + (data_length - 4) + "字元]");
                }
                String AccountName = "null";
                String charName = "null";
                String charLevel = "null";
                String charJob = "null";
                String Map = "null";
                String charId = "null";
                try {
                    AccountName = c.getAccountName();
                } catch (Throwable e) {
                }
                try {
                    charName = c.getPlayer().getName();
                } catch (Throwable e) {
                }
                try {
                    charId = String.valueOf(c.getPlayer().getId());
                } catch (Throwable e) {
                }

                try {
                    charLevel = String.valueOf(c.getPlayer().getLevel());
                } catch (Throwable e) {
                }
                try {
                    charJob = String.valueOf(c.getPlayer().getJob());
                } catch (Throwable e) {
                }
                try {
                    Map = String.valueOf(c.getPlayer().getMap().getId());
                } catch (Throwable e) {
                }

                System.err.println("[用戶端 報錯] 錯誤代碼 " + errortype + " 類型 " + type_str + "\r\n\t[數據] 長度 " + data_length + " [" + SendPacketOpcode.nameOf(opcodeheader) + " | " + opcodeheader + "]\r\n" + HexTool.toString(slea.read((int) slea.available())));
                String tab = "";
                for (int i = 4; i > SendPacketOpcode.nameOf(opcodeheader).length() / 8; i--) {
                    tab += "\t";
                }
                String t = packetLen >= 10 ? packetLen >= 100 ? packetLen >= 1000 ? "" : " " : "  " : "   ";
                if (errortype == 38) {
                    FileoutputUtil.log("Logs/Client/用戶端_報錯.txt", "\r\n"
                            + "帳號:" + AccountName + "\r\n"
                            + "角色:" + charName + "(等級:" + charLevel + ") 編號: " + charId + "" + "\r\n"
                            + "職業:" + charJob + "\r\n"
                            + "地圖:" + Map + "\r\n"
                            + "錯誤類型: " + type_str + "(" + errortype + ")\r\n"
                            + "\r\n"
                            + "[發送]\t" + SendPacketOpcode.nameOf(opcodeheader) + tab + " \t包頭:" + HexTool.getOpcodeToString(opcodeheader) + t + "[" + (data_length - 4) + "字元]\r\n"
                            + "\r\n"
                            + (opcode.length < 1 ? "" : (HexTool.toString(opcode) + "\r\n"))
                            + (opcode.length < 1 ? "" : (HexTool.toStringFromAscii(opcode) + "\r\n"))
                            + "\r\n");
                } else {
                    FileoutputUtil.log("Logs/Client/用戶端_報錯_非38.txt", "\r\n"
                            + "帳號:" + AccountName + "\r\n"
                            + "角色:" + charName + "(等級:" + charLevel + ")" + "\r\n"
                            + "職業:" + charJob + "\r\n"
                            + "地圖:" + Map + "\r\n"
                            + "錯誤類型: " + type_str + "(" + errortype + ")\r\n"
                            + "\r\n"
                            + "[發送]\t" + SendPacketOpcode.nameOf(opcodeheader) + tab + " \t包頭:" + HexTool.getOpcodeToString(opcodeheader) + t + "[" + (data_length - 4) + "字元]\r\n"
                            + "\r\n"
                            + (opcode.length < 1 ? "" : (HexTool.toString(opcode) + "\r\n"))
                            + (opcode.length < 1 ? "" : (HexTool.toStringFromAscii(opcode) + "\r\n"))
                            + "\r\n");
                }
                break;
            case HELLO_CHANNEL:
                CharLoginHandler.Welcome(c);
                break;
            case LOGIN_PASSWORD:
                CharLoginHandler.login(slea, c);
                break;
            case SERVERLIST_REQUEST:
                CharLoginHandler.ServerListRequest(c);
                break;
            case CHARLIST_REQUEST:
                CharLoginHandler.CharlistRequest(slea, c);
                break;
            case SERVERSTATUS_REQUEST:
                CharLoginHandler.ServerStatusRequest(c);
                break;
            case CHECK_CHAR_NAME:
                CharLoginHandler.CheckCharName(slea.readMapleAsciiString(), c);
                break;
            case CREATE_CHAR:
                CharLoginHandler.CreateChar(slea, c);
                break;
            case DELETE_CHAR:
                CharLoginHandler.DeleteChar(slea, c);
                break;
            case CHAR_SELECT:
                CharLoginHandler.Character_WithoutSecondPassword(slea, c);
                break;
            case AUTH_SECOND_PASSWORD:
                CharLoginHandler.Character_WithSecondPassword(slea, c);
                break;
            case SET_GENDER:
                CharLoginHandler.SetGenderRequest(slea, c);
                break;
            case RSA_KEY: // Fix this somehow
                c.sendPacket(LoginPacket.getLoginAUTH());
                //c.sendPacket(LoginPacket.StrangeDATA());
                break;
            // END OF LOGIN SERVER
            case CHANGE_CHANNEL:
                InterServerHandler.ChangeChannel(slea, c, c.getPlayer());
                break;
            case PLAYER_LOGGEDIN:
                final int playerid = slea.readInt();
                if (cs) {
                    CashShopOperation.EnterCS(playerid, c);
                } else {
                    InterServerHandler.Loggedin(slea, playerid, c);
                }
                break;
            case ENTER_CASH_SHOP:
                InterServerHandler.EnterCS(c, c.getPlayer(), false);
                break;
            case ENTER_MTS://進入拍賣
                if (c.getPlayer().inMapleLand()) {
                    c.getSession().writeAndFlush(tools.MaplePacketCreator.enableActions());
                    c.getPlayer().dropMessage(5, "在楓之島無法使用拍賣。");
                    /*} else if (c.getPlayer().getLevel() < 10) {
                    c.getSession().writeAndFlush(tools.MaplePacketCreator.enableActions());
                    c.getPlayer().dropMessage(5, "尚未達到10級的初心者，貴族，與傳說將無法使用。");*/
                } else {
                    c.getSession().writeAndFlush(tools.MaplePacketCreator.enableActions());
                    NPCScriptManager.getInstance().start(c, 9900007, 0, "home_chr");
                }
                //InterServerHandler.EnterCS(c, c.getPlayer(), true);
//				c.getPlayer().dropMessage(1, "目前暫停使用.");
                break;
            case MOVE_PLAYER:
                PlayerHandler.MovePlayer(slea, c, c.getPlayer());
                break;
            case CHAR_INFO_REQUEST:
                c.getPlayer().updateTick(slea.readInt());
                PlayerHandler.CharInfoRequest(slea.readInt(), c, c.getPlayer());
                //System.err.println("CHAR_INFO_REQUEST");
                break;
            case CLOSE_RANGE_ATTACK:
                PlayerHandler.closeRangeAttack(slea, c, c.getPlayer(), false);
                break;
            case RANGED_ATTACK:
                PlayerHandler.rangedAttack(slea, c, c.getPlayer());
                break;
            case MAGIC_ATTACK:
                PlayerHandler.MagicDamage(slea, c, c.getPlayer());
                break;
            case SPECIAL_MOVE:
                PlayerHandler.SpecialMove(slea, c, c.getPlayer());
                break;
            case PASSIVE_ENERGY:
                PlayerHandler.closeRangeAttack(slea, c, c.getPlayer(), true);
                break;
            case FACE_EXPRESSION:
                PlayerHandler.ChangeEmotion(slea.readInt(), c.getPlayer());
                break;
            case TAKE_DAMAGE:
                PlayerHandler.TakeDamage(slea, c, c.getPlayer());
                break;
            case HEAL_OVER_TIME:
                PlayerHandler.Heal(slea, c.getPlayer());
                break;
            case CANCEL_BUFF:
                PlayerHandler.CancelBuffHandler(slea.readInt(), c.getPlayer());
                break;
            case CANCEL_ITEM_EFFECT:
                PlayerHandler.CancelItemEffect(slea.readInt(), c.getPlayer());
                break;
            case USE_CHAIR://使用椅子
                PlayerHandler.UseChair(slea.readInt(), c, c.getPlayer());
                break;
            case CANCEL_CHAIR://取消椅子
                PlayerHandler.CancelChair(slea.readShort(), c, c.getPlayer());
                break;
            case USE_ITEMEFFECT:
            case WHEEL_OF_FORTUNE:
                PlayerHandler.UseItemEffect(slea.readInt(), c, c.getPlayer());
                break;
            case SKILL_EFFECT:
                PlayerHandler.SkillEffect(slea, c.getPlayer());
                break;
            case MESO_DROP:
                c.getPlayer().updateTick(slea.readInt());
                PlayerHandler.DropMeso(slea.readInt(), c.getPlayer());
                break;
            case MONSTER_BOOK_COVER:
                PlayerHandler.ChangeMonsterBookCover(slea.readInt(), c, c.getPlayer());
                break;
            case CHANGE_KEYMAP:
                PlayerHandler.ChangeKeymap1(slea, c.getPlayer());
                break;
            case CHANGE_MAP:
                if (cs) {
                    CashShopOperation.LeaveCS(slea, c, c.getPlayer());
                } else {
                    PlayerHandler.ChangeMap(slea, c, c.getPlayer());
                }
                break;
            case CHANGE_MAP_SPECIAL:
                slea.skip(1);
                PlayerHandler.ChangeMapSpecial(slea.readMapleAsciiString(), c, c.getPlayer());
                break;
            case USE_INNER_PORTAL:
                slea.skip(1);
                PlayerHandler.InnerPortal(slea, c, c.getPlayer());
                break;
            case TROCK_ADD_MAP:
                PlayerHandler.TrockAddMap(slea, c, c.getPlayer());
                break;
            case ARAN_COMBO:
                PlayerHandler.AranCombo(c, c.getPlayer());
                break;
            //case CP_UserCalcDamageStatSetRequest://CP用戶計算損傷統計信息請求
            //    break;
            case SKILL_MACRO:
                PlayerHandler.ChangeSkillMacro(slea, c.getPlayer());
                break;
            case GIVE_FAME:
                PlayersHandler.GiveFame(slea, c, c.getPlayer());
                break;
            case TRANSFORM_PLAYER:
                PlayersHandler.TransformPlayer(slea, c, c.getPlayer());
                break;
            case NOTE_ACTION:
                PlayersHandler.Note(slea, c.getPlayer());
                break;
            case USE_DOOR:
                PlayersHandler.UseDoor(slea, c.getPlayer());
                break;
            case DAMAGE_REACTOR:
                PlayersHandler.HitReactor(slea, c);
                break;
            case TOUCH_REACTOR:
                PlayersHandler.TouchReactor(slea, c);
                break;
            case CLOSE_CHALKBOARD:
                c.getPlayer().setChalkboard(null);
                break;
            case ITEM_MAKER:
                ItemMakerHandler.ItemMaker(slea, c);
                break;
            case ITEM_SORT:
                InventoryHandler.ItemSort(slea, c);
                break;
            case ITEM_GATHER:
                InventoryHandler.ItemGather(slea, c);
                break;
            case ITEM_MOVE:
                InventoryHandler.ItemMove(slea, c);
                break;
            case ITEM_PICKUP:
                InventoryHandler.Pickup_Player(slea, c, c.getPlayer());
                break;
            case USE_CASH_ITEM:
                InventoryHandler.UseCashItem(slea, c);
                break;
            case USE_ITEM:
                InventoryHandler.UseItem(slea, c, c.getPlayer());
                break;
            case USE_MAGNIFY_GLASS:
                InventoryHandler.UseMagnify(slea, c);
                break;
            case USE_SCRIPTED_NPC_ITEM:
                InventoryHandler.UseScriptedNPCItem(slea, c, c.getPlayer());
                break;
            case USE_RETURN_SCROLL:
                InventoryHandler.UseReturnScroll(slea, c, c.getPlayer());
                break;
            case USE_UPGRADE_SCROLL:
                c.getPlayer().updateTick(slea.readInt());
                InventoryHandler.UseUpgradeScroll((byte) slea.readShort(), (byte) slea.readShort(), (byte) slea.readShort(), c, c.getPlayer());
                break;
            case USE_POTENTIAL_SCROLL:
                c.getPlayer().updateTick(slea.readInt());
                InventoryHandler.UseUpgradeScroll((byte) slea.readShort(), (byte) slea.readShort(), (byte) 0, c, c.getPlayer());
                break;
            case USE_EQUIP_SCROLL:
                c.getPlayer().updateTick(slea.readInt());
                InventoryHandler.UseUpgradeScroll((byte) slea.readShort(), (byte) slea.readShort(), (byte) 0, c, c.getPlayer());
                break;
            case USE_SUMMON_BAG:
                InventoryHandler.UseSummonBag(slea, c, c.getPlayer());
                break;
            case USE_TREASUER_CHEST:
                InventoryHandler.UseTreasureChest(slea, c, c.getPlayer());
                break;
            case USE_SKILL_BOOK:
                c.getPlayer().updateTick(slea.readInt());
                //InventoryHandler.UseSkillBook(slea, c, c.getPlayer());
                InventoryHandler.UseSkillBook((byte) slea.readShort(), slea.readInt(), c, c.getPlayer());
                break;
            case USE_CATCH_ITEM:
                InventoryHandler.UseCatchItem(slea, c, c.getPlayer());
                break;
            case USE_MOUNT_FOOD:
                InventoryHandler.UseMountFood(slea, c, c.getPlayer());
                break;
            case REWARD_ITEM:
                InventoryHandler.UseRewardItem((byte) slea.readShort(), slea.readInt(), c, c.getPlayer());
                break;
            case HYPNOTIZE_DMG:
                MobHandler.HypnotizeDmg(slea, c.getPlayer());
                break;
            case MOB_NODE:
                MobHandler.MobNode(slea, c.getPlayer());
                break;
            case DISPLAY_NODE:
                MobHandler.DisplayNode(slea, c.getPlayer());
                break;
            case MOVE_LIFE:
                MobHandler.MoveMonster(slea, c, c.getPlayer());
                break;
            case AUTO_AGGRO:
                MobHandler.AutoAggro(slea.readInt(), c.getPlayer());
                break;
            case FRIENDLY_DAMAGE:
                MobHandler.FriendlyDamage(slea, c.getPlayer());
                break;
            case MONSTER_BOMB:
                MobHandler.MonsterBomb(slea.readInt(), c.getPlayer());
                break;
            case THROW_SKILL:
                PlayerHandler.ThrowSkill(slea, c);
                break;
            case MONSTER_BOMB_DMG:
                PlayerHandler.Monster_Bomb(slea, c);
                break;
            case NPC_SHOP:
                NPCHandler.NPCShop(slea, c, c.getPlayer());
                break;
            case NPC_TALK:
                NPCHandler.NPCTalk(slea, c, c.getPlayer());
                break;
            case NPC_TALK_MORE:
                NPCHandler.NPCMoreTalk(slea, c);
                break;
            case NPC_ACTION:
                NPCHandler.NPCAnimation(slea, c);
                break;
            case QUEST_ACTION:
                NPCHandler.QuestAction(slea, c, c.getPlayer());
                break;
            case STORAGE:
                NPCHandler.Storage(slea, c, c.getPlayer());
                break;
            case GENERAL_CHAT:
                c.getPlayer().updateTick(slea.readInt());
                ChatHandler.GeneralChat(slea.readMapleAsciiString(), slea.readByte(), c, c.getPlayer());
                break;
            case PARTYCHAT:
                ChatHandler.Others(slea, c, c.getPlayer());
                break;
            case WHISPER:
                ChatHandler.Whisper_Find(slea, c);
                break;
            case MESSENGER:
                ChatHandler.Messenger(slea, c);
                break;
            case AUTO_ASSIGN_AP:
                StatsHandling.AutoAssignAP(slea, c, c.getPlayer());
                break;
            case DISTRIBUTE_AP:
                StatsHandling.DistributeAP(slea, c, c.getPlayer());
                break;
            case DISTRIBUTE_SP:
                c.getPlayer().updateTick(slea.readInt());
                StatsHandling.DistributeSP(slea.readInt(), c, c.getPlayer());
                break;
            case PLAYER_INTERACTION:
                PlayerInteractionHandler.PlayerInteraction(slea, c, c.getPlayer());
                break;
            case GUILD_OPERATION:
                GuildHandler.Guild(slea, c);
                break;
            case UPDATE_CHAR_INFO:
                //System.err.println("UPDATE_CHAR_INFO");
                //PlayersHandler.UpdateCharInfo(slea.readAsciiString(), c);
                PlayersHandler.UpdateCharInfo(slea, c, c.getPlayer());
                break;
            case DENY_GUILD_REQUEST:
                slea.skip(1);
                GuildHandler.DenyGuildRequest(slea.readMapleAsciiString(), c);
                break;
            case ALLIANCE_OPERATION:
                AllianceHandler.HandleAlliance(slea, c, false);
                break;
            case DENY_ALLIANCE_REQUEST:
                AllianceHandler.HandleAlliance(slea, c, true);
                break;
            case BBS_OPERATION:
                BBSHandler.BBSOperatopn(slea, c);
                break;
            case PARTY_OPERATION:
                PartyHandler.PartyOperatopn(slea, c);
                break;
            case DENY_PARTY_REQUEST:
                PartyHandler.DenyPartyRequest(slea, c);
                break;
            case EXPEDITION_LISTING://遠征隊處理
                c.getPlayer().dropMessage(1, "正在修復中。");
                //PartyHandler.PartyListing(slea, c);
                break;
            case EXPEDITION_OPERATION://遠征隊處理
                c.getPlayer().dropMessage(1, "正在修復中。");
                //PartyHandler.ExpeditionOperation(slea, c, c.getPlayer());
                //PartyHandler.Expedition(slea, c);
                break;
            case BUDDYLIST_MODIFY://好友操作
                BuddyListHandler.BuddyOperation(slea, c);
                break;
            case CYGNUS_SUMMON:
                UserInterfaceHandler.CygnusSummon_NPCRequest(c);
                break;
            case SHIP_OBJECT:
                UserInterfaceHandler.ShipObjectRequest(slea.readInt(), c);
                break;
            case BUY_CS_ITEM:
                CashShopOperation.BuyCashItem(slea, c, c.getPlayer());
                break;
            case COUPON_CODE:
                // FileoutputUtil.log(FileoutputUtil.PacketEx_Log, "Coupon : \n" + slea.toString(true));
                // System.out.println(slea.toString());
                // slea.skip(2);

                String toPlayer = slea.readMapleAsciiString();
                String code = slea.readMapleAsciiString();
                CashShopOperation.CouponCode(code, c);
                break;
            case SEND_CASH_GIFT:
                CashShopOperation.SendCashGift(slea, c, c.getPlayer());
                break;
            case CS_CHARGE:
            case CS_UPDATE:
                CashShopOperation.CSUpdate(c);
                break;
            case TOUCHING_MTS:
//                MTSOperation.MTSUpdate(MTSStorage.getInstance().getCart(c.getPlayer().getId()), c);
//                break;
            case MTS_TAB:
                MTSOperation.MTSOperation(slea, c);
                break;
            case DAMAGE_SUMMON:
                slea.skip(4);
                SummonHandler.DamageSummon(slea, c.getPlayer());
                break;
            case MOVE_SUMMON:
                SummonHandler.MoveSummon(slea, c.getPlayer());
                break;
            case SUMMON_ATTACK:
                SummonHandler.SummonAttack(slea, c, c.getPlayer());
                break;
            case MOVE_DRAGON:
                SummonHandler.MoveDragon(slea, c.getPlayer());
                break;
            case SPAWN_PET:
                PetHandler.SpawnPet(slea, c, c.getPlayer());
                break;
            case MOVE_PET:
                PetHandler.MovePet(slea, c.getPlayer());
                break;
            case PET_CHAT:
                if (slea.available() < 12) {
                    break;
                }
                PetHandler.PetChat((int) slea.readLong(), slea.readShort(), slea.readMapleAsciiString(), c.getPlayer());
                break;
            case PET_COMMAND:
                PetHandler.PetCommand(slea, c, c.getPlayer());
                break;
            case PET_FOOD:
                PetHandler.PetFood(slea, c, c.getPlayer());
                break;
            case PET_LOOT:
                InventoryHandler.Pickup_Pet(slea, c, c.getPlayer());
                break;
            case PET_AUTO_POT:
                PetHandler.Pet_AutoPotion(slea, c, c.getPlayer());
                break;
            case MONSTER_CARNIVAL:
                MonsterCarnivalHandler.MonsterCarnival(slea, c);
                break;
            case PARTY_SEARCH_START:
                PartyHandler.PartySearchStart(slea, c.getPlayer());
                break;
            case PARTY_SEARCH_STOP:
                PartyHandler.PartySearchStop(c.getPlayer());
                break;
            case DUEY_ACTION:
                DueyHandler.DueyOperation(slea, c);
                break;
            case USE_HIRED_MERCHANT:
                HiredMerchantHandler.UseHiredMerchant(slea, c);
                break;
            case USE_FISH_MERCHANT://精靈小釣手
                //c.getPlayer().dropMessage(5, "精靈小釣手尚未開放。");
                HiredFishingHandler.UseHiredFishing(slea, c);
                break;
            case MERCH_ITEM_STORE:
                final int conv = c.getPlayer().getConversation();
                if (conv == 3) {
                    HiredMerchantHandler.MerchantItemStore(slea, c);
                } else {
                    HiredFishingHandler.FishingItemStore(slea, c);
                }
                //HiredMerchantHandler.MerchantItemStore(slea, c);
                break;
            case CANCEL_DEBUFF:
                // Ignore for now
                break;
            case LEFT_KNOCK_BACK:
                PlayerHandler.leftKnockBack(slea, c);
                break;
            case SNOWBALL:
                PlayerHandler.snowBall(slea, c);
                break;
            case COCONUT:
                PlayersHandler.hitCoconut(slea, c);
                break;
            case REPAIR:
                NPCHandler.repair(slea, c);
                break;
            case REPAIR_ALL:
                NPCHandler.repairAll(c);
                break;
            case GAME_POLL:
                UserInterfaceHandler.InGame_Poll(slea, c);
                break;
            case XMAS_SURPRISE:
                CashShopOperation.UseXmaxsSurprise(slea, c);
                break;
            case OWL:
                InventoryHandler.Owl(slea, c);
                break;
            case OWL_WARP:
                InventoryHandler.OwlWarp(slea, c);
                break;
            case USE_OWL_MINERVA:
                InventoryHandler.OwlMinerva(slea, c);
                break;
            case RPS_GAME:
                NPCHandler.RPSGame(slea, c);
                break;
            case UPDATE_QUEST:
                NPCHandler.UpdateQuest(slea, c);
                break;
            case USE_ITEM_QUEST:
                NPCHandler.UseItemQuest(slea, c);
                break;
            case FOLLOW_REQUEST:
                PlayersHandler.FollowRequest(slea, c);
                break;
            case FOLLOW_REPLY:
                PlayersHandler.FollowReply(slea, c);
                break;
            case RING_ACTION:
                PlayersHandler.RingAction(slea, c);
                break;
            case REQUEST_FAMILY:
                FamilyHandler.RequestFamily(slea, c);
                break;
            case OPEN_FAMILY:
                FamilyHandler.OpenFamily(slea, c);
                break;
            case FAMILY_OPERATION:
                FamilyHandler.FamilyOperation(slea, c);
                break;
            case DELETE_JUNIOR:
                FamilyHandler.DeleteJunior(slea, c);
                break;
            case DELETE_SENIOR:
                FamilyHandler.DeleteSenior(slea, c);
                break;
            case USE_FAMILY:
                FamilyHandler.UseFamily(slea, c);
                break;
            case FAMILY_PRECEPT:
                FamilyHandler.FamilyPrecept(slea, c);
                break;
            case FAMILY_SUMMON:
                FamilyHandler.FamilySummon(slea, c);
                break;
            case ACCEPT_FAMILY:
                FamilyHandler.AcceptFamily(slea, c);
                break;
            case SOLOMON:
                PlayersHandler.Solomon(slea, c);
                break;
            case GACH_EXP:
                PlayersHandler.GachExp(slea, c);
                break;
            default:
                System.out.println("[UNHANDLED] Recv [" + header.toString() + "] found");
                break;
        }
    }
}

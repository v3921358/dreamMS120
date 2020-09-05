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
package handling.channel.handler;

import Apple.console.groups.setting.Weather;
import Apple.client.WorldFindService;
import Apple.client.java.Quadruple;
import Apple.console.Start;
import java.util.List;

import client.BuddylistEntry;
import client.CharacterNameAndId;
import client.MapleCharacter;
import client.MapleCharacterUtil;
import client.MapleClient;
import client.MapleQuestStatus;
import client.MapleStat;
import client.SkillFactory;
import client.inventory.IItem;
import client.inventory.MapleInventoryType;
import client.messages.commands.PlayerCommand;
import constants.ServerConstants;
import handling.cashshop.CashShopServer;
import handling.channel.ChannelServer;
import handling.login.LoginServer;
import handling.world.CharacterTransfer;
import handling.world.MapleMessenger;
import handling.world.MapleMessengerCharacter;
import handling.world.CharacterIdChannelPair;
import handling.world.MaplePartyCharacter;
import handling.world.PartyOperation;
import handling.world.PlayerBuffStorage;
import handling.world.World;
import handling.world.guild.MapleGuild;
import java.io.IOException;
import java.util.Collection;
import server.MapleItemInformationProvider;
import server.maps.FieldLimitType;
import server.shops.HiredFishing;
import tools.FileoutputUtil;
import tools.MaplePacketCreator;
import tools.StringUtil;
import tools.packet.FamilyPacket;
import tools.data.LittleEndianAccessor;
import tools.packet.UIPacket;

public class InterServerHandler {

    public static final void EnterCS(final MapleClient c, final MapleCharacter chr, final boolean mts) {
        if (!chr.isAlive() || chr.getEventInstance() != null || c.getChannelServer() == null) {
//      if (chr.isGM() == false) {
            c.sendPacket(MaplePacketCreator.serverBlocked(2));
            c.sendPacket(MaplePacketCreator.enableActions());
            return;
        }
        //進入商城清除計時
        chr.cancelPaodianTask(true);
        if (c.getPlayer().inMapleLand() || c.getPlayer().getMapId() == 220080001 || mts) {
            c.getSession().writeAndFlush(MaplePacketCreator.enableActions());
            c.getPlayer().dropMessage(5, "在楓之島無法使用購物商城。");
            return;
        }
        //if (c.getChannel() == 1 && !c.getPlayer().isGM()) {
        //    c.getPlayer().dropMessage(5, "You may not enter on this channel. Please change channels and try again.");
        //    c.sendPacket(MaplePacketCreator.enableActions());
        //    return;
        //}
        try {
            int res = chr.saveToDB(false, false);
            if (res == 1) {
                chr.dropMessage(5, "角色保存成功！");
            }
        } catch (Exception ex) {
        }
        final ChannelServer ch = ChannelServer.getInstance(c.getChannel());

        chr.changeRemoval();

        if (chr.getMessenger() != null) {
            MapleMessengerCharacter messengerplayer = new MapleMessengerCharacter(chr);
            World.Messenger.leaveMessenger(chr.getMessenger().getId(), messengerplayer);
        }
        chr.cancelReincarnat();
        PlayerBuffStorage.addBuffsToStorage(chr.getId(), chr.getAllBuffs());
        PlayerBuffStorage.addCooldownsToStorage(chr.getId(), chr.getCooldowns());
        PlayerBuffStorage.addDiseaseToStorage(chr.getId(), chr.getAllDiseases());
        World.ChannelChange_Data(new CharacterTransfer(chr), chr.getId(), mts ? -20 : -10);
        ch.removePlayer(chr, "調用位置:" + new java.lang.Throwable().getStackTrace()[0]);
        c.updateLoginState(MapleClient.CHANGE_CHANNEL, c.getSessionIPAddress());

        c.sendPacket(MaplePacketCreator.getChannelChange(Integer.parseInt(CashShopServer.getIP().split(":")[1])));
        chr.saveToDB(false, false);
        chr.saveToCache();//保存到緩存
        chr.getMap().removePlayer(chr);
        c.setPlayer(null);
        c.setReceiving(false);
    }

    public static final void Loggedin(LittleEndianAccessor slea, final int playerid, final MapleClient c) {
        //判斷是否作弊登入
        if (c.getCloseSession()) {
            System.err.println("[Loggedin]已斷開連線");
            c.getSession().close();
            return;
        }
        c.loadAccountidByPlayerid(playerid);
        if (WorldFindService.getInstance().findDisconnect(c.getAccID()) > 0) {
            System.out.println("(Loggedin) 頻道<" + c.getChannel() + "> 角色複製: " + playerid + " 帳號id: " + c.getAccID());
            FileoutputUtil.log("logs/Hack/角色複製.txt", FileoutputUtil.CurrentReadable_Time() + " 玩家id: " + playerid + " 頻道: " + c.getChannel() + " 帳號id: " + c.getAccID() + " 角色複製 (Loggedin)");
            WorldFindService.getInstance().forceDeregisterDisconnect(c.getAccID());
            c.getSession().close();
            return;
        } else if (!MapleCharacterUtil.isExistCharacterInDataBase(playerid)) {
            System.out.println("<刪除角色> (Loggedin  頻道<" + c.getChannel() + ">角色複製: " + playerid + " 帳號id: " + c.getAccID());
            FileoutputUtil.log("logs/Hack/角色複製.txt", FileoutputUtil.CurrentReadable_Time() + " <刪除角色複製> 玩家id: " + playerid + " 帳號id:" + c.getAccID());
            c.getSession().close();
            return;
        }

        final ChannelServer channelServer = c.getChannelServer();
        MapleCharacter player;

        CharacterTransfer transfer = null;//channelServer.getPlayerStorage().getPendingCharacter(playerid)
        for (ChannelServer cserv : ChannelServer.getAllInstances()) {
            transfer = cserv.getPlayerStorage().getPendingCharacter(playerid);
            if (transfer != null) {
                break;
            }
        }
        final int state = c.getLoginState();

        if (state != MapleClient.LOGIN_SERVER_TRANSITION && transfer == null) {
            System.err.println("[Loggedin]登錄服務器轉換發生 - 異常已斷開連線");
            c.getSession().close();
            return;
        }
        if (state != MapleClient.LOGIN_SERVER_TRANSITION && state != MapleClient.CHANGE_CHANNEL) {
            System.err.println("[Loggedin]登錄服務器轉換發生, 更改頻道 - 異常已斷開連線");
            c.getSession().close();
            return;
        }
        //獲取 mac 143寫法
        int[] bytes = new int[6];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = slea.readByteAsInt();
        }
        StringBuilder sps = new StringBuilder();
        for (int aByte : bytes) {
            sps.append(StringUtil.getLeftPaddedStr(Integer.toHexString(aByte).toUpperCase(), '0', 2));
            sps.append("-");
        }
        String macData = sps.toString();
        macData = macData.substring(0, macData.length() - 1);
        //讀取 mac 完畢
        boolean firstLoggedIn = true; //設置只有第1次登錄的提示開關
        if (transfer == null) { // Player isn't in storage, probably isn't CC
            if (System.getProperty(String.valueOf(playerid)) == null || !System.getProperty(String.valueOf(playerid)).equals("1")) {
                FileoutputUtil.log("logs/Hack/卡黑頻登入.txt", FileoutputUtil.CurrentReadable_Time() + " 玩家id: " + playerid);
                System.out.println("疑似複製玩家id: " + playerid);
                c.getSession().close();
                return;
            } else {
                System.setProperty(String.valueOf(playerid), String.valueOf(0));
            }
            LoginServer.removeClient(c);
            //143 登入認證
            Quadruple<String, String, Integer, String> ip = LoginServer.getLoginAuth(playerid);
            String s = c.getSessionIPAddress();
            if (ip == null || (!s.substring(s.indexOf('/') + 1, s.length()).equals(ip.one) && !c.getMac().equals(macData))) {
                if (ip != null) {
                    LoginServer.putLoginAuth(playerid, ip.one, ip.two, ip.three, ip.four);
                } else {
                    System.err.println("[Loggedin]登入認證 - 異常已斷開連線");
                    c.getSession().close();
                    return;
                }
            }
            c.setTempIP(ip.two);
            c.setChannel(ip.three);
            //檢查完畢
            player = MapleCharacter.loadCharFromDB(playerid, c, true);
        } else {
            player = MapleCharacter.ReconstructChr(transfer, c, true);
            firstLoggedIn = false;
        }
        if (ServerConstants.DEBUG_MODE) {
            System.err.println("檢查角色數據: " + player);
        }
        //對在線上角色做斷線
        LoginServer.forceRemoveClient(c, false);
        ChannelServer.forceRemovePlayerByAccId(c, c.getAccID());

        if (!c.CheckIPAddress()) { // Remote hack
            c.getSession().close();
            System.err.println("[Loggedin]伺服器主動斷開用戶端連接 - 異常已斷開連線");
            FileoutputUtil.log("logs/data/DC.txt", "\r\n帳號[" + c.getAccountName() + "]伺服器主動斷開用戶端連接，調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            return;
        }

        //設置用戶端角色
        c.setPlayer(player);
        //設置用戶端賬號ID
        c.setAccID(player.getAccountID());
        c.loadAccountData(player.getAccountID());

        //更新登入狀態
        c.updateLoginState(MapleClient.LOGIN_LOGGEDIN, c.getSessionIPAddress());
        channelServer.addPlayer(player);
        c.sendPacket(MaplePacketCreator.getCharInfo(player));

        //暫存能力值解除
        c.sendPacket(MaplePacketCreator.temporaryStats_Reset());

        try {
            short nhp = player.getStat().hp;
            short nmp = player.getStat().mp;
            // BUFF技能
            player.silentGiveBuffs(PlayerBuffStorage.getBuffsFromStorage(player.getId()));
            player.getStat().hp = nhp;
            player.getStat().mp = nmp;
            player.updateSingleStat(MapleStat.HP, nhp);
            player.updateSingleStat(MapleStat.MP, nmp);
            // 冷卻時間
            player.giveCoolDowns(PlayerBuffStorage.getCooldownsFromStorage(player.getId()));
            // 疾病狀態
            player.giveSilentDebuff(PlayerBuffStorage.getDiseaseFromStorage(player.getId()));

            // 伺服器管理員上線預設無敵
            if (player.isAdmin() && !player.isInvincible()) {
                //player.dropMessage(6, "無敵已經開啟.");
                //player.setInvincible(true);
            }
            //管理員上線預設隱藏
            if (player.isGM()) {
                SkillFactory.getSkill(9001004).getEffect(1).applyTo(player);
            }
            // 開啟好友列表
            //final Collection<Integer> buddyIds = player.getBuddylist().getBuddiesIds();//辛巴寫法
            final int buddyIds[] = player.getBuddylist().getBuddyIds();
            World.Buddy.loggedOn(player.getName(), player.getId(), c.getChannel(), buddyIds, player.getGMLevel(), player.isHidden());
            if (player.getParty() != null) {
                //channelServer.getWorldInterface().updateParty(player.getParty().getId(), PartyOperation.LOG_ONOFF, new MaplePartyCharacter(player));
                World.Party.updateParty(player.getParty().getId(), PartyOperation.LOG_ONOFF, new MaplePartyCharacter(player));
            }
            /* 讀取好友 */
            final CharacterIdChannelPair[] onlineBuddies = WorldFindService.getInstance().multiBuddyFind(player.getId(), buddyIds);
            for (CharacterIdChannelPair onlineBuddy : onlineBuddies) {
                final BuddylistEntry ble = player.getBuddylist().get(onlineBuddy.getCharacterId());
                ble.setChannel(onlineBuddy.getChannel());
                player.getBuddylist().put(ble);
            }
            c.sendPacket(MaplePacketCreator.updateBuddylist(player.getBuddylist().getBuddies()));

            // Start of Messenger
            final MapleMessenger messenger = player.getMessenger();
            if (messenger != null) {
                World.Messenger.silentJoinMessenger(messenger.getId(), new MapleMessengerCharacter(c.getPlayer()));
                World.Messenger.updateMessenger(messenger.getId(), c.getPlayer().getName(), c.getChannel());
            }

            // 開始公會及聯盟
            if (player.getGuildId() > 0) {
                World.Guild.setGuildMemberOnline(player.getMGC(), true, c.getChannel());
                c.sendPacket(MaplePacketCreator.showGuildInfo(player));
                final MapleGuild gs = World.Guild.getGuild(player.getGuildId());
                if (gs != null) {
                    final List<byte[]> packetList = World.Alliance.getAllianceInfo(gs.getAllianceId(), true);
                    if (packetList != null) {
                        for (byte[] pack : packetList) {
                            if (pack != null) {
                                c.sendPacket(pack);
                            }
                        }
                    }
                } else {
                    player.setGuildId(0);
                    player.setGuildRank((byte) 5);
                    player.setAllianceRank((byte) 5);
                    player.saveGuildStatus();
                }
            } else {
                //處理勳章
                c.sendPacket(MaplePacketCreator.fuckGuildInfo(player));
            }
            //家庭
            if (player.getFamilyId() > 0) {
                World.Family.setFamilyMemberOnline(player.getMFC(), true, c.getChannel());
            }
            c.sendPacket(FamilyPacket.getFamilyInfo(player));
        } catch (Exception e) {
            FileoutputUtil.outputFileError(FileoutputUtil.Login_Error, e);
        }
        //把角色添加進地圖
        player.getMap().addPlayer(player);
        //更新在線時間
        c.getPlayer().initOnlineTime();
        player.setLogintime(System.currentTimeMillis());
        // 解决进入商城卡在线时间的问题.
        //player.fixOnlineTime();
        //處理家庭封包
        c.sendPacket(FamilyPacket.getFamilyData());
        //技能組合
        player.sendMacros();
        //顯示小字條消息
        player.showNote();
        //更新組隊HP
        player.updatePartyMemberHP();
        //開始計算角色精靈吊墜時間
        player.startFairySchedule(false, true);
        //修復3轉以上角色技能 如果沒有就修復
        player.baseSkills();
        //鍵盤設置
        c.sendPacket(MaplePacketCreator.getKeymap(player.getKeyLayout()));

        //任務狀態
        for (MapleQuestStatus status : player.getStartedQuests()) {
            if (status.hasMobKills()) {
                c.sendPacket(MaplePacketCreator.updateQuestMobKills(status));
            }
        }

        //好友
        final CharacterNameAndId pendingBuddyRequest = player.getBuddylist().pollPendingRequest();
        if (pendingBuddyRequest != null) {
            player.getBuddylist().put(new BuddylistEntry(pendingBuddyRequest.getName(), pendingBuddyRequest.getId(), "ETC", -1, false, pendingBuddyRequest.getLevel(), pendingBuddyRequest.getJob()));
            c.sendPacket(MaplePacketCreator.requestBuddylistAdd(pendingBuddyRequest.getId(), pendingBuddyRequest.getName(), pendingBuddyRequest.getLevel(), pendingBuddyRequest.getJob()));
        }
        // 黑騎士技能 
        if (player.canBerserk()) {
            player.doBerserk();
        }
        //if (player.getJob() == 132) {//黑騎士技能
        //    player.checkBerserk();
        //}

        // 複製人
        player.spawnClones();
        // 寵物
        player.spawnSavedPets();
        //偵測物品到期時間
        player.expirationTask();
        //釣魚小幫手
        final HiredFishing fishing = World.hasFishing(player.getAccountID());
        if (fishing != null) {
            player.setPlayerFishing(fishing);
            player.startFishingTask(true, true);
        }
        /*新的信件*/
        if (c.getPlayer().newMail() > 0) {
            player.dropMessage("[信件公告]: 你有 " + c.getPlayer().newMail() + " 未讀郵件. 使用 @新信件/@newmail 來看內容.");
        }
        //天氣系統
        if (Weather.Change) {
            c.sendPacket(MaplePacketCreator.startMapEffect(null, 5120000, false));
        }
        //發送登錄提示 只有第1次才有
        if (firstLoggedIn) {
            if (player.getLevel() == 1) {
                player.dropMessage(1, "歡迎來到 " + c.getChannelServer().getServerName() + ", " + player.getName() + " ！\r\n使用 @help 可以查看您當前能使用的命令\r\n祝您玩的愉快！");
                player.dropMessage(5, "使用 @help 可以查看您當前能使用的命令 祝您玩的愉快！");
            }
            if (Weather.Change) {
                c.sendPacket(MaplePacketCreator.serverNotice(0, "氣象活動開始了, 經驗值獲得率提升: 150%"));
            }
            if (!player.isGM()) {
                World.Broadcast.broadcastMessage(UIPacket.getTopMsg("玩家 <" + player.getName() + "> 已上線"));
            }
            //移除章魚砲台
            if (ServerConstants.偵測移除招喚獸) {
                player.getMap().removePlayerAllMapSummonObject(c);
            }
            //偵測獵人剩餘時間
            /*int time = InventoryHandler.Accounts_HunterExp(player, false);
            if (time > 0) {
                MapleItemInformationProvider.getInstance().getItemEffect(2450000).applyTo(player, player, true, null, time);
                player.dropMessage(5, "您的幸運獵人剩餘時間: " + time);
            }*/
        }
        player.cancelReincarnat();
        //泡點功能
        if (player.isMarketMap()) {
            player.cancelPaodianTask(firstLoggedIn);//取消泡點計時
            player.startPaodianTask();
        }
    }

    public static final void ChangeChannel(final LittleEndianAccessor slea, final MapleClient c, final MapleCharacter chr) {
        if (!chr.isAlive() || chr.getEventInstance() != null || chr.getMap() == null || FieldLimitType.ChannelSwitch.check(chr.getMap().getFieldLimit())) {
            c.sendPacket(MaplePacketCreator.enableActions());
            return;
        }
        chr.changeChannel(slea.readByte() + 1);
        //換頻清除計時
        chr.cancelPaodianTask(false);
    }
}

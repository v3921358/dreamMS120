/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Apple.client;

import backup.GUI.Settings.sql;
import client.MapleCharacter;
import client.MapleClient;
import client.inventory.Equip;
import client.inventory.IItem;
import client.inventory.Item;
import client.inventory.MapleInventory;
import client.inventory.MapleInventoryType;
import client.messages.CommandProcessorUtil;
import constants.GameConstants;
import constants.MapConstants;
import database.DatabaseConnection;
import handling.channel.ChannelServer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import scripting.NPCScriptManager;
import server.MapleCarnivalChallenge;
import server.MapleInventoryManipulator;
import server.MapleItemInformationProvider;
import server.maps.MapleMap;
import server.maps.SavedLocationType;
import tools.FileoutputUtil;
import tools.MaplePacketCreator;
import tools.StringUtil;
import tools.packet.MTSCSPacket;

/**
 *
 * @author MSI
 */
public class commands {

    public static boolean 幫助(MapleClient c, String[] splitted) {
        c.getPlayer().ShopNPC(""
                + "\t   #i3994014##i3994018##i3994070##i3994061##i3994005##i3991038##i3991004#\r\n"
                + "\t\t  #fMob/0100101.img/move/1##b 親愛的： #h \r\n"
                + " #fMob/0100101.img/move/1##k\r\r\n"
                + "\t\t#fMob/0130101.img/move/1##g[以下是" + c.getChannelServer().getServerName() + " 玩家指令]#k#fMob/0130101.img/move/1#\r\n"
                + "\t  #r▇▇▆▅▄▃▂#d萬用指令區#r▂▃▄▅▆▇▇\r\n"
                + "\t\t#b@清除道具 <裝備欄/消耗欄/裝飾欄/其他欄/特殊欄> <開始格數> <結束格數>#k - #r<清除背包道具>#k\r\n"
                + "\t\t#b@ea#k - #r<解除異常+查看當前狀態>#k\r\n"
                + "\t\t#b@在線點數/@jcds#k - #r<領取在線點數>#k\r\n"
                + "\t\t#b@mob#k - #r<查看身邊怪物訊息>#k\r\n"
                + "\t\t#b@expfix#k - #r<經驗歸零(修復假死)>#k\r\n"
                + "\t\t#b@CGM <訊息>#k - #r<傳送訊息給GM>#k\r\n"
                + "\t\t#b@jk_hm #k - #r<清除卡精靈商人>#k\r\n"
                + "\t\t#b@save#k - #r<存檔>#k\r\n"
                + "\t\t#b@TSmega#k - #r<開/關所有廣播>#k\r\n"
        );
        return true;
    }

    /*
     * 信件功能
     */
    //發送給用戶的信
    public static int 寫信(MapleClient c, String[] splitted) {
        if (splitted.length <= 2) {
            c.getPlayer().dropMessage("請使用 @mail/@寫信 <收件者> <訊息>.");
        } else {
            MapleCharacter victim_0 = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
            String Reciever = splitted[1];
            String message = StringUtil.joinStringFrom(splitted, 2);
            try {
                c.getPlayer().sendMail(Reciever, message);
            } catch (SQLException ex) {
                //Logger.getLogger(PlayerCommands.class.getName()).log(Level.SEVERE, null, ex);
            }
            c.getPlayer().dropMessage("妳已經寄信給 " + Reciever + " 這個訊息: " + message);
            if (victim_0 != null) {
                victim_0.dropMessage(":" + c.getPlayer().getName() + " 剛剛寄給你了一封信! 使用 @newmail/@新信件 來看內容.");
            } else {
                c.getPlayer().dropMessage("玩家尚未上線將為您離線發送訊息");
            }
        }
        return 0;
    }

    //檢查你的新郵件
    public static int 新信件(MapleClient c, String[] splitted) {
        if (c.getPlayer().newMail() > 0) {
            ResultSet rs_2 = sql.getNewMail(c.getPlayer().getName());
            try {
                c.getPlayer().dropMessage("您的新信:");
                while (rs_2.next()) {
                    c.getPlayer().dropMessage(rs_2.getString("MailSender") + ": " + rs_2.getString("Message"));
                }
            } catch (Exception ex) {
            }
            c.getPlayer().setSeenAllMail();
        } else {
            c.getPlayer().dropMessage("妳尚未有新信. 使用 @mailall/@所有信件 來查詢您的信件.");
        }
        //break;
        return 0;

    }

    //查看您所有的郵件
    public static int 所有信件(MapleClient c, String[] splitted) {
        c.getPlayer().setSeenAllMail();
        ResultSet rs_1 = sql.getAllMail(c.getPlayer().getName());
        try {
            c.getPlayer().dropMessage("您的信箱:");
            while (rs_1.next()) {
                c.getPlayer().dropMessage(rs_1.getString("MailSender") + ": " + rs_1.getString("Message"));
            }
        } catch (Exception ex) {
        }
        //break;
        return 0;
    }

    public static int 上線(MapleClient c, String[] splitted) {
        MapleCharacter chr = c.getPlayer();
        if (splitted.length == 1) {
            int total = 0;
            if (chr.isGM()) {
                chr.dropMessage(6, "----------------------------------(all / ch)-----------------------------------------");
            }
            chr.dropMessage(6, "-------------------------------------------------------------------------------------");
            for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                int curConnected = cserv.getConnectedClients();
                chr.dropMessage(6, "頻道: " + cserv.getChannel() + " 在線人數: " + curConnected);
                total += curConnected;
            }
            chr.dropMessage(6, "當前伺服器總計線上人數: " + total);
            chr.dropMessage(6, "-------------------------------------------------------------------------------------");
            return 1;
        }
        if (splitted.length < 4 && splitted[1].equalsIgnoreCase("all") && chr.isGM()) {
            int total = 0;
            chr.dropMessage(6, "-------------------------------------------------------------------------------------");
            for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                int curConnected = cserv.getConnectedClients();
                if (curConnected != 0) {
                    chr.dropMessage(6, "頻道: " + cserv.getChannel() + " 在線人數: " + curConnected);
                }
                total += curConnected;
                for (MapleCharacter chr1 : cserv.getPlayerStorage().getAllCharacters()) {
                    if (splitted.length == 3 && MapConstants.isMarketMap(chr1.getMapId()) && MapConstants.isFishingMap(chr1.getMapId())) {
                        continue;
                    }
                    if (chr1 != null) {
                        StringBuilder ret = new StringBuilder();
                        ret.append("  ");
                        ret.append(StringUtil.getRightPaddedStr(chr1.getName(), ' ', 12));
                        ret.append(" ID: ");
                        ret.append(StringUtil.getRightPaddedStr(String.valueOf(chr1.getId()), ' ', 3));
                        ret.append(" 等級: ");
                        ret.append(StringUtil.getRightPaddedStr(String.valueOf(chr1.getLevel()), ' ', 3));
                        ret.append(" 職業: ");
                        ret.append(StringUtil.getRightPaddedStr(MapleCarnivalChallenge.getJobNameById(chr1.getJob()), ' ', 14));
                        if (chr1.getMap() != null) {
                            ret.append(" 地圖: ");
                            ret.append(chr1.getMapId());
                            ret.append("-");
                            ret.append(chr1.getMap().getMapName());
                        }
                        chr.dropMessage(6, ret.toString());
                    }
                }
            }
            chr.dropMessage(5, "當前伺服器總計線上人數: " + total);
            chr.dropMessage(6, "-------------------------------------------------------------------------------------");
        } else if (splitted[1].equalsIgnoreCase("ch")) {
            chr.dropMessage(6, "上線的角色 頻道-" + c.getChannel() + ":");
            chr.dropMessage(6, c.getChannelServer().getPlayerStorage().getOnlinePlayers(true));
        }
        return 0;
    }

    public static int 黑板(MapleClient c, String[] splitted) {
        MapleCharacter player = c.getPlayer();
        if (splitted.length < 2) {
            player.dropMessage(6, "請輸入欲在黑板上顯示的文字 @chalk/黑板 <內容>");
            return 0;
        }
        player.setChalkboard("" + StringUtil.joinStringFrom(splitted, 1) + "");
        player.getMap().broadcastMessage(MTSCSPacket.useChalkboard(player.getId(), StringUtil.joinStringFrom(splitted, 1)));
        player.getClient().sendPacket(MaplePacketCreator.enableActions());//getSession().write
        return 0;

    }

    public static int 清除道具(MapleClient c, String[] splitted) {
        MapleCharacter chr = c.getPlayer();
        if (splitted.length < 3) {
            chr.dropMessage(5, splitted[0] + " (裝備 / 消耗 / 其他 / 裝飾) (第x格) (第x格)");
            return 0;
        } else {
            MapleInventoryType type;
            if (splitted[1].equalsIgnoreCase("裝備") || splitted[1].equalsIgnoreCase("a")) {
                type = MapleInventoryType.EQUIP;
            } else if (splitted[1].equalsIgnoreCase("消耗") || splitted[1].equalsIgnoreCase("b")) {
                type = MapleInventoryType.USE;
            } else if (splitted[1].equalsIgnoreCase("其他")) {
                type = MapleInventoryType.ETC;
            } else if (splitted[1].equalsIgnoreCase("裝飾")) {
                type = MapleInventoryType.SETUP;
            } else if (splitted[1].equalsIgnoreCase("特殊")) {
                type = MapleInventoryType.CASH;
            } else {
                chr.dropMessage(5, "指令使用錯誤。錯誤的Type:" + splitted[1]);
                return 0;
            }
            MapleInventory inv = c.getPlayer().getInventory(type);
            byte start = Byte.parseByte(splitted[2]);
            byte end = Byte.parseByte(splitted[3]);
            for (byte i = start; i <= end; i++) {
                if (inv.getItem(i) != null) {
                    MapleInventoryManipulator.removeFromSlot(c, type, i, inv.getItem(i).getQuantity(), true);
                }
            }
            chr.dropMessage(6, "使用指令:" + splitted[0] + " 清除 " + splitted[1] + "欄位 " + splitted[2] + " ~ " + splitted[3]);
        }
        return 1;
    }

    public static int 回家的傳送(MapleClient c, String[] splitted) {
        int textMAP = 0;
        int getPortalT = 0;
        int item = 0;
        if (splitted.length != 1) {
            if (splitted[1].equalsIgnoreCase("-1")) {
                textMAP = 910000000;//自由
            } else if (splitted[1].equalsIgnoreCase("0")) {
                textMAP = 211000001;//長老
            } else if (splitted[1].equalsIgnoreCase("1")) {
                textMAP = 102000003;//勇士之村
            } else if (splitted[1].equalsIgnoreCase("2")) {
                textMAP = 101000003;//魔法森林
            } else if (splitted[1].equalsIgnoreCase("3")) {
                textMAP = 100000201;//弓箭手村
            } else if (splitted[1].equalsIgnoreCase("4")) {
                textMAP = 103000003;//墮落城市
            } else if (splitted[1].equalsIgnoreCase("5")) {
                textMAP = 120000101;//鯨魚號
            } else if (splitted[1].equalsIgnoreCase("11")) {
                textMAP = 102020300;//勇士
                getPortalT = 0;//他沒有快速傳送
            } else if (splitted[1].equalsIgnoreCase("22")) {
                textMAP = 101020000;//魔法
                getPortalT = 27;
            } else if (splitted[1].equalsIgnoreCase("33")) {
                textMAP = 106010000;//2轉
                getPortalT = 9;
            } else if (splitted[1].equalsIgnoreCase("44")) {
                textMAP = 102040000;//墮落
                getPortalT = 9;
            } else if (splitted[1].equalsIgnoreCase("55")) {
                textMAP = 120000101;//鯨魚號
            } else if (splitted[1].equalsIgnoreCase("12")) {
                item = 4031013;//黑珠
            } else if (splitted[1].equalsIgnoreCase("13")) {
                item = 4031059;//黑符
            } else if (splitted[1].equalsIgnoreCase("14")) {
                item = 4005004;//黑暗水晶
            } else if (splitted[1].equalsIgnoreCase("15")) {
                item = 4031058;//智慧項鍊
            } else {
                textMAP = 749050400;//轉蛋
            }
            if (textMAP != 0) {
                c.getPlayer().saveLocation(SavedLocationType.FREE_MARKET, c.getPlayer().getMap().getReturnMap().getId());
                MapleMap map = c.getChannelServer().getMapFactory().getMap(textMAP);
                c.getPlayer().changeMap(map, map.getPortal(getPortalT));
            } else if (item != 0) {
                final short quantity = (short) CommandProcessorUtil.getOptionalIntArg(splitted, 2, 1);
                IItem item_ = new client.inventory.Item(item, (byte) 0, (short) quantity, (byte) 0);
                MapleInventoryManipulator.addbyItem(c, item_);
            }
        } else {
            c.getPlayer().dropMessage(5, "-------------------------------------------------------------------------------------");
            c.getPlayer().dropMessage(5, "轉職屋 1 | 2 | 3 | 4 | 5");
            c.getPlayer().dropMessage(5, "2轉職 11 | 22 | 33 | 44 || 55");
            c.getPlayer().dropMessage(5, "-1 自由 || 0 長老村 || 12黑珠  || 13黑符|| 14黑暗水晶 || 15智慧項鍊");
            c.getPlayer().dropMessage(5, "-------------------------------------------------------------------------------------");
            return 0;
        }
        return 0;
    }

    public static void sendNote(String to, String name, String msg, int fame) {
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("INSERT INTO notes (`to`, `from`, `message`, `timestamp`, `gift`) VALUES (?, ?, ?, ?, ?)");
            ps.setString(1, to);
            ps.setString(2, name);
            ps.setString(3, msg);
            ps.setLong(4, System.currentTimeMillis());
            ps.setInt(5, fame);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            System.err.println("Unable to send note" + e);
        }
    }
    public static String Inventory = "倉庫";

    //增加欄位
    public static void addSlot(int type, MapleCharacter chr, int Slot) {
        if (chr.getInventory(getInventoryType(type)).getSlotLimit() + Slot >= 96) {
            chr.getInventory(getInventoryType(type)).addSlot((byte) 96);
        } else {
            chr.getInventory(getInventoryType(type)).addSlot((byte) Slot);
        }
        chr.dropMessage(-1, "增加了" + Inventory + "欄的空間 " + Slot + " 個");
    }

    //增加倉庫欄位
    public static void increaseSlots(MapleCharacter chr, int Slot) {
        if (chr.getStorage().getSlots() + Slot >= 48) {
            chr.getStorage().increaseSlots((byte) 48);
        } else {
            chr.getStorage().increaseSlots((byte) Slot);
        }
        chr.getStorage().saveToDB();
        chr.dropMessage(-1, "增加了" + Inventory + "欄的空間 " + Slot + " 個");
    }

    public static MapleInventoryType getInventoryType(int type) {
        switch (type) {
            case 1:
                Inventory = "裝備";
                return MapleInventoryType.EQUIP;
            case 2:
                Inventory = "消耗";
                return MapleInventoryType.USE;
            case 3:
                Inventory = "裝飾";
                return MapleInventoryType.SETUP;
            case 4:
                Inventory = "其他";
                return MapleInventoryType.ETC;
            case 5:
                Inventory = "特殊";
                return MapleInventoryType.CASH;
        }
        return null;
    }

    public static int GetItemAll(MapleClient c, String[] splitted, boolean Channel, boolean MapleMap) {
        if (splitted.length < 2) {
            c.getPlayer().dropMessage(6, "用法: !GetItemAll <道具ID> <道具數量>");
            return 0;
        }
        final int itemId = Integer.parseInt(splitted[1]);
        final short quantity = splitted.length < 3 ? 1 : (short) CommandProcessorUtil.getOptionalIntArg(splitted, 2, 1);
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        if (!ii.itemExists(itemId)) {
            c.getPlayer().dropMessage(5, itemId + " 這個道具不存在.");
        } else if (GameConstants.isPet(itemId)) {
            c.getPlayer().dropMessage(5, "寵物道具請通過商城購買.");
        }
        int succ = 0, error = 0;
        String File = "全服發送道具.ini";
        FileoutputUtil.log(File, "--------------------------------管理員 " + c.getPlayer().getName() + " 頻道 " + c.getChannel() + " 發送道具開始--------------------------------\r\n");
        FileoutputUtil.log(File, "時間: " + FileoutputUtil.NowTime() + " 道具: " + itemId + " 數量: " + quantity + " 名稱: " + ii.getName(itemId) + " 發送所有<" + (MapleMap ? "在 " + c.getPlayer().getMap().getMapName() : "伺服器") + ">玩家道具\r\n");
        for (ChannelServer channel : ChannelServer.getAllInstances()) {
            if (channel.getChannel() != c.getChannel() && Channel) {
                continue;
            }
            for (MapleCharacter player : channel.getPlayerStorage().getAllCharacters()) {
                if (player.getMapId() != c.getPlayer().getMapId() && MapleMap) {
                    continue;
                }
                String IP = player.getClient().getSession().remoteAddress().toString().split(":")[0];
                if (player != null && player.haveSpaceForId(itemId)) {
                    Item item;
                    if (GameConstants.getInventoryType(itemId) == MapleInventoryType.EQUIP) {
                        item = ii.randomizeStats((Equip) ii.getEquipById(itemId));
                    } else {
                        item = new client.inventory.Item(itemId, (byte) 0, !c.getPlayer().isAdmin() ? 1 : quantity, (byte) 0);
                    }
                    item.setGMLog(c.getPlayer().getName() + " 使用命令 !GetItemAll");
                    item.setOwner(c.getPlayer().getName());
                    MapleInventoryManipulator.addbyItem(player.getClient(), item);
                    player.dropMessage(1, "恭喜你獲得管理員贈送的 " + ii.getName(itemId) + " " + quantity + "個。");
                    player.dropMessage(6, "[系統公告] 恭喜你獲得管理員贈送的 " + ii.getName(itemId) + " " + quantity + "個。");
                    succ++;
                } else {
                    error++;
                }
                FileoutputUtil.log(File, StringUtil.getRightPaddedStr(IP, ' ', 14) + " 玩家名: " + StringUtil.getRightPaddedStr(player.getName(), ' ', 14) + " 是否領取成功: " + player.haveSpaceForId(itemId) + " \t地圖: " + StringUtil.getRightPaddedStr(player.getMap().getMapName(), ' ', 16) + " 頻道: " + player.getClient().getChannel());
            }
        }
        FileoutputUtil.log(File, "\r\n--------------------------------管理員 " + c.getPlayer().getName() + " 頻道 " + c.getChannel() + " 發送道具結束--------------------------------\r\n");
        c.getPlayer().dropMessage(1, "命令使用完畢。\r\n發送成功: " + succ + "\r\n發送失敗: " + error);
        return 1;
    }
}

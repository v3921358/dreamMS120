/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.messages.commands;

import client.messages.CommandExecute;
import Apple.client.commands;
import client.MapleCharacter;
import client.MapleClient;
import client.inventory.IItem;
import client.inventory.MapleInventory;
import client.inventory.MapleInventoryType;
import constants.CopyItem.CopyItemInfo;
import constants.GameConstants;
import constants.ServerConstants;
import constants.ServerConstants.PlayerGMRank;
import database.DatabaseConnection;
import handling.channel.ChannelServer;
import handling.world.World;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import scripting.NPCScriptManager;
import server.MapleItemInformationProvider;
import tools.CPUSampler;
import tools.FileoutputUtil;
import tools.MaplePacketCreator;
import tools.StringUtil;
import tools.packet.PlayerShopPacket;

/**
 *
 * @author Msi
 */
public class SeverCommand {

    public static PlayerGMRank getPlayerLevelRequired() {
        return PlayerGMRank.服務器管理員;
    }

    public static class GetItemAll extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 4) {
                c.getPlayer().dropMessage(6, "用法: !GetItemAll <道具ID> <道具數量> <w|m> - 送禮物品[世界|地圖]");
                return 0;
            }
            String show = "";
            if (splitted[3].equals("m")) {
                commands.GetItemAll(c, splitted, true, true);
                show = c.getPlayer().getMap().getMapName();
            } else if (splitted[3].equals("w")) {
                commands.GetItemAll(c, splitted, false, false);
                show = "伺服器";
            } else {
                c.getPlayer().dropMessage(6, "用法: !GetItemAll <道具ID> <道具數量> <w|m> - 送禮物品[世界|地圖]");
                return 0;
            }
            String msg = "[GM 密語] GM [" + c.getPlayer().getName() + "] 發送所有<" + show + ">玩家 " + splitted[1] + " [" + MapleItemInformationProvider.getInstance().getName(Integer.parseInt(splitted[1])) + "x" + splitted[2] + "]";
            World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(6, msg));
            return 0;
        }

        @Override
        public String getMessage() {
            return new StringBuilder().append(ServerConstants.PlayerGMRank.服務器管理員.getCommandPrefix()).append("GC - 系統釋放記憶體").toString();
        }
    }

    public static class 檢測複製 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            List<String> msgs = new ArrayList<>();
            java.util.Map<Integer, CopyItemInfo> checkItems = new LinkedHashMap<>();
            for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                for (MapleCharacter player : cserv.getPlayerStorage().getAllCharacters()) {
                    if (player != null && player.getMap() != null) {
                        //檢測背包裝備
                        check(player.getInventory(MapleInventoryType.EQUIP), player, checkItems, msgs);
                        //檢測身上的裝備
                        check(player.getInventory(MapleInventoryType.EQUIPPED), player, checkItems, msgs);
                    }
                }
            }
            checkItems.clear();
            if (msgs.size() > 0) {
                c.getPlayer().dropMessage(5, "檢測完成，共有: " + msgs.size() + " 個複製信息");
//                FileoutputUtil.log("裝備複製.txt", "檢測完成，共有: " + msgs.size() + " 個複製信息", true);
                for (String s : msgs) {
                    c.getPlayer().dropMessage(5, s);
//                    FileoutputUtil.log("裝備複製.txt", s, true);
                }
                c.getPlayer().dropMessage(5, "以上信息為擁有複製道具的玩家.");
            } else {
                c.getPlayer().dropMessage(5, "未檢測到遊戲中的角色有複製的道具信息.");
            }
            return 1;
        }

        public void check(MapleInventory equip, MapleCharacter player, java.util.Map<Integer, CopyItemInfo> checkItems, List<String> msgs) {
            MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
            for (IItem item : equip.list()) {
                if (item.getEquipOnlyId() > 0) {
                    CopyItemInfo ret = new CopyItemInfo(item.getItemId(), player.getId(), player.getName());
                    if (checkItems.containsKey(item.getEquipOnlyId())) {
                        ret = checkItems.get(item.getEquipOnlyId());
                        if (ret.itemId == item.getItemId()) {
                            if (ret.isFirst()) {
                                ret.setFirst(false);
                                msgs.add("角色: " + StringUtil.getRightPaddedStr(ret.name, ' ', 13) + " 角色ID: " + StringUtil.getRightPaddedStr(String.valueOf(ret.chrId), ' ', 6) + " 道具: " + ret.itemId + " - " + ii.getName(ret.itemId) + " 唯一ID: " + item.getEquipOnlyId());
                            } else {
                                msgs.add("角色: " + StringUtil.getRightPaddedStr(player.getName(), ' ', 13) + " 角色ID: " + StringUtil.getRightPaddedStr(String.valueOf(player.getId()), ' ', 6) + " 道具: " + item.getItemId() + " - " + ii.getName(item.getItemId()) + " 唯一ID: " + item.getEquipOnlyId());
                            }
                        }
                    } else {
                        checkItems.put(item.getEquipOnlyId(), ret);
                    }
                }
            }
        }

        @Override
        public String getMessage() {
            return new StringBuilder().append(ServerConstants.PlayerGMRank.服務器管理員.getCommandPrefix()).append("GC - 系統釋放記憶體").toString();
        }
    }

    public static class GiveAllMP extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 2) {
                return 0;
            }
            int mp = 0, size = 0;
            try {
                mp = Integer.parseInt(splitted[1]);
            } catch (NumberFormatException ex) {

            }
            for (ChannelServer cs : ChannelServer.getAllInstances()) {
                for (MapleCharacter chr : cs.getPlayerStorage().getAllCharactersThreadSafe()) {
                    size++;
                    chr.modifyCSPoints(2, mp, true);
                    c.getPlayer().dropMessage("[在線] 玩家<" + chr.getName() + "> Lv." + chr.getLevel() + " 地圖<" + chr.getMap().getMapName() + "> 發放點數[" + mp + "] 發放後點數[" + (chr.getCSPoints(2)) + "]");
                }
            }

            HashMap<Integer, Integer> acoffline = getOfflineAcc();
            java.sql.Connection con = DatabaseConnection.getConnection();
            java.sql.PreparedStatement ps = null;
            ResultSet rs = null;
            boolean f = true;
            try {
                for (final Map.Entry<Integer, Integer> AC : acoffline.entrySet()) {
                    String sql = "UPDATE accounts SET mPoints = " + (AC.getValue() + mp) + " where id = " + AC.getKey();
                    ps = con.prepareStatement(sql);
                    ps.execute();
                    ps.close();
                    c.getPlayer().dropMessage("[離線] 帳號編號<" + AC.getKey() + "> 發放點數[" + mp + "] 發放後點數[" + (AC.getValue() + mp) + "]");
                    size++;
                }
            } catch (Exception ex) {

            }
            c.getPlayer().dropMessage("共發放了" + size + "個帳號, 一共為" + size * mp + "點");
            FileoutputUtil.log("Logs/Data/點數指令.txt", "\r\n " + FileoutputUtil.CurrentReadable_Time() + " <" + c.getPlayer().getName() + "> 使用了" + splitted[0] + " 共發放了" + size + "個帳號, 一共為" + size * mp + " 楓點");

            return 1;
        }

        public HashMap<Integer, Integer> getOfflineAcc() {
            HashMap<Integer, Integer> AccIdFromDataBase = new HashMap<>();
            try {
                com.mysql.jdbc.Connection con = (com.mysql.jdbc.Connection) DatabaseConnection.getConnection();
                com.mysql.jdbc.PreparedStatement ps = (com.mysql.jdbc.PreparedStatement) con.prepareStatement("SELECT id, mPoints FROM accounts WHERE loggedin = 0");
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    AccIdFromDataBase.put(rs.getInt("id"), rs.getInt("mPoints"));
                }
                rs.close();
                ps.close();
            } catch (Exception e) {
                System.err.println("getOfflineAcc 出現問題(DB):" + e);
            }
            return AccIdFromDataBase;
        }

        @Override
        public String getMessage() {
            return new StringBuilder().append(ServerConstants.PlayerGMRank.服務器管理員.getCommandPrefix()).append("GC - 系統釋放記憶體").toString();
        }
    }

    public static class gainexp extends 給經驗 {
    }

    public static class 給經驗 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 2) {
                c.getPlayer().dropMessage(6, "!給經驗 <player> <amount>.");
                return 0;
            }
            MapleCharacter chrs = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
            NumberFormat nf = NumberFormat.getInstance();//轉為千分為數
            int needed = GameConstants.getExpNeededForLevel(chrs.getLevel());
            if (splitted.length < 3) {
                c.getPlayer().dropMessage(6, "目前 " + chrs.getName() + " 經驗值:" + nf.format(chrs.getExp()) + " 升級所需經驗:" + nf.format(needed));
                return 0;
            }
            if (chrs == null) {
                c.getPlayer().dropMessage(6, "請確認有在正確的頻道");
            } else {
                chrs.gainExp(Integer.parseInt(splitted[2]), true, false, true);
                c.getPlayer().dropMessage(6, "成功給予 " + splitted[1] + " " + splitted[2] + "經驗值 || 目前玩家經驗:" + nf.format(chrs.getExp()) + " 升級所需經驗:" + nf.format(needed));
            }
            return 1;

        }

        @Override
        public String getMessage() {
            return new StringBuilder().append(ServerConstants.PlayerGMRank.服務器管理員.getCommandPrefix()).append("GC - 系統釋放記憶體").toString();
        }
    }

    public static class DropCash1 extends 丟裝1 {
    }

    public static class 丟裝1 extends OpenNPCCommand {

        public 丟裝1() {
            npc = 0;
        }

        @Override
        public String getMessage() {
            return new StringBuilder().append(ServerConstants.PlayerGMRank.管理員V.getCommandPrefix()).append("丟裝1 - 丟出裝備").toString();
        }
    }

    public abstract static class OpenNPCCommand extends CommandExecute {

        protected int npc = -1;
        private static int[] npcs = { //Ish yur job to make sure these are in order and correct ;(
            9010017,};

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (npc != 1 && c.getPlayer().getMapId() != 910000000) { //drpcash can use anywhere
                for (int i : GameConstants.blockedMaps) {
                    if (c.getPlayer().getMapId() == i) {
                        c.getPlayer().dropMessage(1, "你不能在這裡使用指令.");
                        return 0;
                    }
                }
                if (c.getPlayer().getLevel() < 10) {
                    c.getPlayer().dropMessage(1, "你的等級必須是10等.");
                    return 0;
                }
                if (c.getPlayer().getMap().getSquadByMap() != null || c.getPlayer().getEventInstance() != null || c.getPlayer().getMap().getEMByMap() != null || c.getPlayer().getMapId() >= 990000000/* || FieldLimitType.VipRock.check(c.getPlayer().getMap().getFieldLimit())*/) {
                    c.getPlayer().dropMessage(1, "你不能在這裡使用指令.");
                    return 0;
                }
                if ((c.getPlayer().getMapId() >= 680000210 && c.getPlayer().getMapId() <= 680000502) || (c.getPlayer().getMapId() / 1000 == 980000 && c.getPlayer().getMapId() != 980000000) || (c.getPlayer().getMapId() / 100 == 1030008) || (c.getPlayer().getMapId() / 100 == 922010) || (c.getPlayer().getMapId() / 10 == 13003000)) {
                    c.getPlayer().dropMessage(1, "你不能在這裡使用指令.");
                    return 0;
                }
            }
            NPCScriptManager.getInstance().start(c, npcs[npc]);
            return 1;
        }
    }
    
    public static class RemoveItemOff extends CommandExecute {

        @Override
        public int execute(MapleClient c, String splitted[]) {
            if (splitted.length < 3) {
                return 0;
            }
            try {
                Connection con = DatabaseConnection.getConnection();
                int item = Integer.parseInt(splitted[1]);
                String name = splitted[2];
                int id = 0, quantity = 0;
                List<Long> inventoryitemid = new LinkedList();
                boolean isEquip = GameConstants.isEquip(item);

                if (MapleCharacter.getCharacterByName(name) == null) {
                    c.getPlayer().dropMessage(5, "角色不存在資料庫。");
                    return 1;
                } else {
                    id = MapleCharacter.getCharacterByName(name).getId();
                }

                PreparedStatement ps = con.prepareStatement("select inventoryitemid, quantity from inventoryitems WHERE itemid = ? and characterid = ?");
                ps.setInt(1, item);
                ps.setInt(2, id);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        if (isEquip) {
                            long Equipid = rs.getLong("inventoryitemid");
                            if (Equipid != 0) {
                                inventoryitemid.add(Equipid);
                            }
                            quantity++;
                        } else {
                            quantity += rs.getInt("quantity");
                        }
                    }
                }
                if (quantity == 0) {
                    c.getPlayer().dropMessage(5, "玩家[" + name + "]沒有物品[" + item + "]在背包。");
                    return 1;
                }

                if (isEquip) {
                    StringBuilder Sql = new StringBuilder();
                    Sql.append("Delete from inventoryequipment WHERE inventoryitemid = ");
                    for (int i = 0; i < inventoryitemid.size(); i++) {
                        Sql.append(inventoryitemid.get(i));
                        if (i < (inventoryitemid.size() - 1)) {
                            Sql.append(" OR inventoryitemid = ");
                        }
                    }
                    ps = con.prepareStatement(Sql.toString());
                    ps.executeUpdate();
                }

                ps = con.prepareStatement("Delete from inventoryitems WHERE itemid = ? and characterid = ?");
                ps.setInt(1, item);
                ps.setInt(2, id);
                ps.executeUpdate();
                ps.close();

                c.getPlayer().dropMessage(6, "已經從 " + name + " 身上被移除了道具 ID[" + item + "] 數量x" + quantity);
                return 1;
            } catch (SQLException e) {
                return 0;
            }
        }

        @Override
        public String getMessage() {
            return new StringBuilder().append(ServerConstants.PlayerGMRank.管理員V.getCommandPrefix()).append("RemoveItemOff <物品ID> <角色名稱> - 移除玩家身上的道具").toString();
        }
    }
}

package constants.CopyItem;

import Apple.client.WorldFindService;
import Apple.client.java.Quadruple;
import client.MapleCharacter;
import database.DatabaseConnection;
import handling.cashshop.CashShopServer;
import handling.channel.ChannelServer;
import handling.world.World;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import server.MapleInventoryManipulator;
import server.MapleItemInformationProvider;
import server.Timer;
import tools.FileoutputUtil;
import tools.MaplePacketCreator;
import tools.Triple;

/**
 *
 * @author Windyboy
 */
public class MapleEquipIdOnly {

    private final static MapleEquipIdOnly instance = new MapleEquipIdOnly();
    private static List<Triple<Integer, Long, Long>> OnlyIDList = new ArrayList();
    private boolean issearching = false;
    private boolean iscleaning = false;

    public static MapleEquipIdOnly getInstance() {
        return instance;
    }

    protected MapleEquipIdOnly() {
        //Timer.WorldTimer.getInstance().register(new run(), 12 * 60 * 60 * 1000);
    }

    public List<Triple<Integer, Long, Long>> getData() {
        return OnlyIDList;
    }

    public void addData(int chrid, long inventoryitemid, long ItemOnlyID) {
        OnlyIDList.add(new Triple(chrid, inventoryitemid, ItemOnlyID));
    }

    public void removeData(int chrid) {
        OnlyIDList.remove(chrid);
    }

    public void clearData() {
        if (!OnlyIDList.isEmpty()) {
            OnlyIDList.clear();
        }
    }

    public void StartChecking() {
        System.out.println("----系統正在檢查複製裝備----");
        issearching = true;
        if (!OnlyIDList.isEmpty()) {
            OnlyIDList.clear();
        }

        StringBuilder chrs = new StringBuilder();
        StringBuilder Sql = new StringBuilder();
        List<Quadruple<Integer, Integer, Long, Integer>> equipOnlyIds = new ArrayList();
        Map checkItems = new HashMap();
        List<Integer> all = new LinkedList<>();
        List<Integer> gm = new LinkedList<>();

        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT characterid FROM inventoryitems WHERE equipOnlyId > 0");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int chr = rs.getInt("characterid");
                if (chr != 0) {
                    all.add(chr);
                }
            }
            ps.close();
            rs.close();

            Sql = new StringBuilder();
            if (!all.isEmpty()) {
                Sql.append("and (id = ");
                for (int i = 0; i < all.size(); i++) {
                    Sql.append(all.get(i));
                    if (i < (all.size() - 1)) {
                        Sql.append(" OR id = ");
                    }
                }
                Sql.append(")");
            }

            ps = con.prepareStatement("SELECT id FROM characters WHERE gm > 0 " + Sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                int chr = rs.getInt("id");
                if (chr != 0) {
                    gm.add(chr);
                }
            }
            ps.close();
            rs.close();

            Sql = new StringBuilder();

            if (!gm.isEmpty()) {
                Sql.append("and characterid != ");
                for (int i = 0; i < gm.size(); i++) {
                    Sql.append(gm.get(i));
                    if (i < (gm.size() - 1)) {
                        Sql.append(" and characterid != ");
                    }
                }
            }
            ps = con.prepareStatement("SELECT * FROM inventoryitems WHERE equipOnlyId > 0 " + Sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                int chr = rs.getInt("characterid");
                int ac = rs.getInt("accountid");
                int itemId = rs.getInt("itemId");
                long equipOnlyId = rs.getLong("equipOnlyId");
                long inventoryitemid = rs.getLong("inventoryitemid");
                if (equipOnlyId > 0) {
                    if (checkItems.containsKey(equipOnlyId)) {
                        if (((Integer) checkItems.get(equipOnlyId)) == itemId) {
                            equipOnlyIds.add(new Quadruple(chr, ac, equipOnlyId, itemId));
                            addData(chr, inventoryitemid, equipOnlyId);
                        }
                    } else {
                        checkItems.put(equipOnlyId, itemId);
                    }
                }
            }
            rs.close();
            ps.close();

            final ListIterator<Quadruple<Integer, Integer, Long, Integer>> OnlyId = equipOnlyIds.listIterator();
            while (OnlyId.hasNext()) {
                chrs = new StringBuilder();
                Quadruple<Integer, Integer, Long, Integer> Only = OnlyId.next();
                long itemonly = Only.getThree();
                int item = Only.getFour();

                ps = con.prepareStatement("SELECT characterid FROM inventoryitems WHERE equipOnlyId = " + itemonly);
                rs = ps.executeQuery();
                while (rs.next()) {
                    int chr = rs.getInt("characterid");
                    if (chr != 0) {
                        chrs.append("角色 [").append(chr).append("]");
                    }
                }
                ps.close();
                rs.close();
                String itemname = "null";
                itemname = MapleItemInformationProvider.getInstance().getName(item);
                String msg = "發現複製,唯一ID [" + itemonly + "] " + chrs.toString() + " 物品[" + itemname + "](" + item + ")";
                System.out.println(msg);
                World.Broadcast.broadcastGMMessage((MaplePacketCreator.serverNotice(6, "[GM密語] " + msg)));
                FileoutputUtil.log("logs/Hack/裝備複製_SQL.txt", FileoutputUtil.NowTime() + " " + msg + "\r\n");
            }

        } catch (SQLException ex) {
            System.out.println("[EXCEPTION] 複製裝備出現錯誤." + ex);
        }
        issearching = false;
    }

    public void setCleaning(boolean set) {
        iscleaning = set;
    }

    public boolean isCleaning() {
        return iscleaning;
    }

    public boolean isSearching() {
        return issearching;
    }

    public boolean isDoing() {
        return isCleaning() || isSearching();
    }

    public void StartCleaning() {
        System.out.println("----系統正在清除複製裝備----");

        iscleaning = true;
        try {
            final ListIterator<Triple<Integer, Long, Long>> OnlyId = getData().listIterator();
            while (OnlyId.hasNext()) {
                Triple<Integer, Long, Long> Only = OnlyId.next();
                int chr = Only.getLeft();
                long invetoryitemid = Only.getMid();
                long equiponlyid = Only.getRight();
                int ch = WorldFindService.getInstance().findChannel(chr);
                if (ch < 0 && ch != -10 && ch != -20) {
                    try {
                        String itemname = "null";
                        Connection con = DatabaseConnection.getConnection();

                        try (PreparedStatement ps = con.prepareStatement("select itemid from inventoryitems WHERE inventoryitemid = ?")) {
                            ps.setLong(1, invetoryitemid);
                            try (ResultSet rs = ps.executeQuery()) {
                                if (rs.next()) {
                                    int itemid = rs.getInt("itemid");
                                    itemname = MapleItemInformationProvider.getInstance().getName(itemid);
                                }
                            }
                        }

                        try (PreparedStatement ps = con.prepareStatement("Delete from inventoryequipment WHERE inventoryitemid = " + invetoryitemid)) {
                            ps.executeUpdate();
                        }
                        try (PreparedStatement ps = con.prepareStatement("Delete from inventoryitems WHERE inventoryitemid = ?")) {
                            ps.setLong(1, invetoryitemid);
                            ps.executeUpdate();
                        }

                        String msgtext = "玩家ID: " + chr + " 背包內複製道具[" + itemname + "], 系統已自動刪除";
                        World.Broadcast.broadcastGMMessage((MaplePacketCreator.serverNotice(6, "[GM密語] " + msgtext)));
                        System.out.println(msgtext);
                        FileoutputUtil.log("logs/hack/複製裝備_已刪除.txt", FileoutputUtil.CurrentReadable_Time() + " " + msgtext + " 道具: [" + itemname + "] 辨識為一ID: " + equiponlyid + "\r\n");
                    } catch (SQLException ex) {
                        FileoutputUtil.outputFileError(FileoutputUtil.CommandEx_Log, ex);
                    }
                } else {
                    MapleCharacter chrs = null;
                    if (ch == -10) {
                        chrs = CashShopServer.getPlayerStorage().getCharacterById(chr);
                    } else {
                        chrs = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterById(chr);
                    }
                    if (chrs == null) {
                        break;
                    }
                    MapleInventoryManipulator.removeAllByEquipOnlyId(chrs.getClient(), (int) invetoryitemid);
                }
            }
            clearData();
        } catch (Exception ex) {
            String output = FileoutputUtil.CurrentReadable_Date();
            FileoutputUtil.outputFileError(FileoutputUtil.CommandEx_Log, ex);
        }
        System.out.println("----當前複製裝備清除完畢----");
        iscleaning = false;
    }

    public static class run implements Runnable {

        @Override
        public void run() {
            if (!getInstance().isDoing()) {
                getInstance().StartChecking();
                getInstance().StartCleaning();
            }
        }
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Apple.client;

import client.MapleClient;
import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import server.MapleInventoryManipulator;
import server.MapleItemInformationProvider;

/**
 *
 * @author Msi
 */
public class Log {

    /*
     * 升級獎勵日誌
     */
    public static int getLog(String bossid, MapleClient c, boolean acc) {
        Connection con1 = DatabaseConnection.getConnection();
        try {
            int ret_count;
            PreparedStatement ps;
            if (acc) {
                ps = con1.prepareStatement("SELECT count(*) from accounts_log where accountid = ?  and bossid = ?");
                ps.setInt(1, c.getAccID());
                ps.setString(2, bossid);
            } else {
                ps = con1.prepareStatement("SELECT count(*) from accounts_log where  characterid = ? and bossid = ?");
                ps.setInt(1, c.getPlayer().getId());
                ps.setString(2, bossid);
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ret_count = rs.getInt(1);
                } else {
                    ret_count = -1;
                }
            }
            ps.close();
            return ret_count;
        } catch (Exception Ex) {
            System.err.println(Ex);
            //log.error("Error while read bosslog.", Ex);
            return -1;
        }
    }

    /*
     * 升級獎勵日誌
     */
    public static void setLog(String bossid, MapleClient c, String UPDATE) {
        Connection con = DatabaseConnection.getConnection();
        try {
            if (UPDATE == null) {
                PreparedStatement ps = con.prepareStatement("INSERT INTO accounts_log (accountid, characterid, bossid) values (?, ?, ?)");
                ps.setInt(1, c.getAccID());
                ps.setInt(2, c.getPlayer().getId());
                ps.setString(3, bossid);
                ps.executeUpdate();
                ps.close();
            } else {
                PreparedStatement ps = con.prepareStatement("UPDATE accounts_log SET bossid = ? WHERE accountid = ? and characterid = ? and bossid = ?");
                ps.setString(1, UPDATE);
                ps.setInt(2, c.getAccID());
                ps.setInt(3, c.getPlayer().getId());
                ps.setString(4, bossid);
                ps.executeUpdate();
                ps.close();
            }

        } catch (Exception Ex) {
            System.err.println(Ex);
            //   log.error("Error while insert bosslog.", Ex);
        }
    }

    /*
     * 升級獎勵日誌
     */
    public static int getLogTime(String bossid, MapleClient c, boolean acc, int D, int H, int M, int S) {
        Connection con1 = DatabaseConnection.getConnection();
        try {
            int ret_count;
            PreparedStatement ps;
            if (acc) {
                ps = con1.prepareStatement("SELECT count(*) from accounts_log where accountid = ? and bossid = ? and lastattempt >= subtime(current_timestamp, '" + D + " " + H + ":" + M + ":" + S + ".0')");
                ps.setInt(1, c.getAccID());
                ps.setString(2, bossid);
            } else {
                ps = con1.prepareStatement("SELECT count(*) from accounts_log where  characterid = ? and bossid = ? and lastattempt >= subtime(current_timestamp, '" + D + " " + H + ":" + M + ":" + S + ".0')");
                ps.setInt(1, c.getPlayer().getId());
                ps.setString(2, bossid);
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ret_count = rs.getInt(1);
                } else {
                    ret_count = -1;
                }
            }
            ps.close();
            return ret_count;
        } catch (Exception Ex) {
            System.err.println(Ex);
            //log.error("Error while read bosslog.", Ex);
            return -1;
        }
    }

    public static void delLog(String bossid, MapleClient c) {
        Connection con = DatabaseConnection.getConnection();
        try (PreparedStatement ps = con.prepareStatement("DELETE FROM accounts_log WHERE bossid = ? and characterid = ?")) {
            ps.setString(1, bossid);
            ps.setInt(2, c.getPlayer().getId());
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("delLog " + ex);
        }
    }

    /*
     * 領取獎勵訊息
     */
    public static boolean RewardNote(String type, int itemId, int quantity, MapleClient client) {
        if (type != null) {
            MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
            final String id_Name = "[" + (ii.getName(itemId) == null ? itemId : ii.getName(itemId)) + "]"; // 物品代碼 [物品名稱]
            switch (type) {
                case "i"://物品
                    if (!MapleInventoryManipulator.checkSpace(client, itemId, 1, client.getPlayer().getName())) {
                        client.getPlayer().dropMessage(-5, "\t你背包已滿 或 已有重複專屬道具無法獲得: " + id_Name);
                        return false;
                    }
                    MapleInventoryManipulator.addById(client, itemId, (short) quantity, null, null, (itemId == 1122017 ? 1 : 0));//物品
                    break;
                case "m"://楓幣
                    client.getPlayer().gainMeso(quantity, true);
                    break;
                case "p"://點數
                    client.getPlayer().modifyCSPoints(2, quantity, true);
                    break;
                case "s"://擴充包
                    if ((itemId + 1) == 6) {
                        commands.increaseSlots(client.getPlayer(), quantity);
                    } else {
                        commands.addSlot((itemId + 1), client.getPlayer(), quantity);
                    }
                    break;
                default:
                    return false;
            }
            String txt = "m".equals(type) ? "[楓幣]" : "p".equals(type) ? "[點數]" : "s".equals(type) ? "[擴充包]" : id_Name;
            client.getPlayer().dropMessage(-5, "\t你已獲得獎勵: " + txt + " 數量:" + quantity + "個。");
        }
        return true;
    }

    /*
     * 獎勵公告顯示
     */
    public static void sendNote(String to, String name, String msg, int fame, String type, int itemid, int quantity) {
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps;
            ps = con.prepareStatement("INSERT INTO notes (`to`, `from`, `message`, `timestamp`, `gift`, `type`, `itemid`, `quantity`) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            ps.setString(1, to);
            ps.setString(2, name);
            ps.setString(3, msg);
            ps.setLong(4, System.currentTimeMillis());
            ps.setInt(5, fame);
            ps.setString(6, type);
            ps.setInt(7, itemid);
            ps.setInt(8, quantity);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            System.err.println("Unable to send note" + e);
        }
    }

    public static String 字串(String str) {
        String n = "";
        int count = 0;
        char[] chs = str.toCharArray();
        for (int i = 0; i < chs.length; i++) {
            count += (chs[i] > 0xff) ? 2 : 1;
        }
        for (int s = count; s < 40; s++) {
            n += " ";
        }
        return str + n;
    }
}

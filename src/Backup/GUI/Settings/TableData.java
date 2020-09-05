/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backup.GUI.Settings;

import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.Randomizer;

/**
 *
 * @author Msi
 */
public class TableData {

    /*
     * 轉蛋機
     */
 /*
     * 轉蛋機
     */
    //添加
    public static ResultSet AddGachapon(int job) {
        try {
            Connection con = (Connection) DatabaseConnection.getConnection();//
            //   PreparedStatement ps = (PreparedStatement) con.prepareStatement("SELECT name,level FROM characters WHERE world = ?");
            PreparedStatement ps = (PreparedStatement) con.prepareStatement("SELECT\n"
                    + "gachapon.continent,\n"
                    + "gachapon.itemid,\n"
                    + "gachapon.chance,\n"
                    + "gachapon.comments\n"
                    + "FROM\n"
                    + "gachapon\n"
                    + "WHERE\n"
                    + "gachapon.continent = ?");
            ps.setInt(1, job);
            return ps.executeQuery();
        } catch (Exception ex) {
            System.err.println(ex);
        }
        return null;
    }

    //查詢轉蛋機
    public static ResultSet Gachapon(int job, boolean hide) {
        try {
            Connection con = (Connection) DatabaseConnection.getConnection();//
            //   PreparedStatement ps = (PreparedStatement) con.prepareStatement("SELECT name,level FROM characters WHERE world = ?");
            PreparedStatement ps = (PreparedStatement) con.prepareStatement("SELECT * \n"
                    + "FROM\n"
                    + "gachapon\n"
                    + "WHERE\n"
                    + "gachapon.continent = ?\n"
                    + (hide ? "And gachapon.`onlySelf` <> 1" : ""));
            ps.setInt(1, job);
            return ps.executeQuery();
        } catch (Exception ex) {
            System.err.println(ex);
        }
        return null;
    }

    /*
     * 收尋資料庫所有玩家
     */
    //獲取所有玩家名稱
    public static ResultSet getAllOnline() {
        try {
            Connection con = (Connection) DatabaseConnection.getConnection();
            PreparedStatement ps = (PreparedStatement) con.prepareStatement("SELECT name FROM characters");
            return ps.executeQuery();

        } catch (Exception ex) {
            System.err.println(ex);
        }
        return null;
    }

    //獲取所有玩家名稱
    public static ResultSet getAllOnline(int Acc) {
        try {
            Connection con = (Connection) DatabaseConnection.getConnection();
            PreparedStatement ps = (PreparedStatement) con.prepareStatement("SELECT name FROM characters WHERE accountid = " + Acc);
            return ps.executeQuery();

        } catch (Exception ex) {
            System.err.println(ex);
        }
        return null;
    }

    //獲取所有玩家帳號
    public static ResultSet getAllOnlineAcc() {
        try {
            Connection con = (Connection) DatabaseConnection.getConnection();
            PreparedStatement ps = (PreparedStatement) con.prepareStatement("SELECT id FROM accounts");
            return ps.executeQuery();

        } catch (Exception ex) {
            System.err.println(ex);
        }
        return null;
    }

    /*
     * 查詢數據
     */
    //獲取裝備屬性數據
    public static ResultSet EquipData(String name, int type) {
        try {
            Connection con = (Connection) DatabaseConnection.getConnection();//
            //   PreparedStatement ps = (PreparedStatement) con.prepareStatement("SELECT name,level FROM characters WHERE world = ?");
            PreparedStatement ps = (PreparedStatement) con.prepareStatement("SELECT\n"
                    + "inventoryitems.itemid,\n"
                    + "inventoryequipment.level,\n"
                    + "inventoryequipment.str,\n"
                    + "inventoryequipment.dex,\n"
                    + "inventoryequipment.`int`,\n"
                    + "inventoryequipment.luk,\n"
                    + "inventoryequipment.watk,\n"
                    + "inventoryequipment.matk,\n"
                    + "inventoryequipment.wdef,\n"
                    + "inventoryequipment.mdef,\n"
                    + "inventoryequipment.acc,\n"
                    + "inventoryequipment.avoid,\n"
                    + "inventoryequipment.hands,\n"
                    + "inventoryequipment.speed,\n"
                    + "inventoryequipment.jump\n"
                    + "from ((`characters` join `inventoryitems` on((`characters`.`id` = `inventoryitems`.`characterid`))) join `inventoryequipment` on((`inventoryitems`.`inventoryitemid` = `inventoryequipment`.`inventoryitemid`)))\n"
                    + "WHERE\n"
                    + "characters.name = ? and\n"
                    + "`inventoryitems`.`inventorytype` = ?");
            ps.setString(1, name);
            ps.setInt(2, type);
            return ps.executeQuery();
        } catch (Exception ex) {
            System.err.println(ex);
        }
        return null;
    }

    //獲取物品數據
    public static ResultSet ItemData(String name) {
        try {
            Connection con = (Connection) DatabaseConnection.getConnection();//
            //   PreparedStatement ps = (PreparedStatement) con.prepareStatement("SELECT name,level FROM characters WHERE world = ?");
            PreparedStatement ps = (PreparedStatement) con.prepareStatement("SELECT\n"
                    + "characters.name,\n"
                    + "inventoryitems.itemid,\n"
                    + "inventoryitems.quantity\n"
                    + "from (`characters` join `inventoryitems` on((`characters`.`id` = `inventoryitems`.`characterid`)))\n"
                    + "WHERE\n"
                    + "characters.name =  ?");
            ps.setString(1, name);
            return ps.executeQuery();

        } catch (Exception ex) {
            System.err.println(ex);
        }
        return null;
    }

    //獲取物品數據
    public static ResultSet FameHair() {
        try {
            Connection con = (Connection) DatabaseConnection.getConnection();
            PreparedStatement ps = (PreparedStatement) con.prepareStatement("SELECT\n"
                    + "characters.`name`,\n"
                    + "characters.face,\n"
                    + "characters.hair\n"
                    + "FROM\n"
                    + "characters");
            return ps.executeQuery();

        } catch (Exception ex) {
            System.err.println(ex);
        }
        return null;
    }

    public static ResultSet EquipData() {
        try {
            Connection con = (Connection) DatabaseConnection.getConnection();//
            //   PreparedStatement ps = (PreparedStatement) con.prepareStatement("SELECT name,level FROM characters WHERE world = ?");
            PreparedStatement ps = (PreparedStatement) con.prepareStatement("SELECT\n"
                    + "inventoryitems.itemid,\n"
                    + "characters.name,\n"
                    + "inventoryequipment.level,\n"
                    + "inventoryequipment.str,\n"
                    + "inventoryequipment.dex,\n"
                    + "inventoryequipment.`int`,\n"
                    + "inventoryequipment.luk,\n"
                    + "inventoryequipment.watk,\n"
                    + "inventoryequipment.matk,\n"
                    + "inventoryequipment.wdef,\n"
                    + "inventoryequipment.mdef,\n"
                    + "inventoryequipment.acc,\n"
                    + "inventoryequipment.avoid,\n"
                    + "inventoryequipment.hands,\n"
                    + "inventoryequipment.speed,\n"
                    + "inventoryitems.`inventorytype`,\n"
                    + "inventoryequipment.jump\n"
                    + "from ((`characters` join `inventoryitems` on((`characters`.`id` = `inventoryitems`.`characterid`))) join `inventoryequipment` on((`inventoryitems`.`inventoryitemid` = `inventoryequipment`.`inventoryitemid`)))\n"
                    + "WHERE\n"
                    + "inventoryitems.inventorytype <= 1");
            return ps.executeQuery();
        } catch (Exception ex) {
            System.err.println(ex);
        }
        return null;
    }

    //暫時
    public static ResultSet Nxcode(int type, int Nxcodeitemid, int size) {
        String code = "";
        for (int i = 0; i < 20; i++) {
            int key = Randomizer.rand(65, 100);//隨機亂數物品
            if (key > 90) {
                key = Randomizer.rand(0, 9);
                code += key;
            } else {
                code += (char) key;
            }
        }
        System.err.println(code);
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("INSERT INTO nxcode (`code`, `type`, `item`, `size`) VALUES (?, ?, ?, ?)");
            ps.setString(1, code);
            /*
             * Type 1: GASH點數
             * Type 2: 楓葉點數
             * Type 3: 物品x數量(默認1個)
             * Type 4: 楓幣
             */
            ps.setInt(2, type);
            ps.setInt(3, Nxcodeitemid);
            ps.setInt(4, size);

            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            //Logger.getLogger(TableData.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}

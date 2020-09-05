/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import database.DatabaseConnection;
import java.sql.Connection;
import client.MapleClient;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author ByBy
 */
public class CoinPlayerHandler {
    
    private static final CoinPlayerHandler instance = new CoinPlayerHandler();

    public static CoinPlayerHandler getInstance() {
        return instance;
    }

    /***
     * @設置贊助幣
     */
    public void setCoin(int money,MapleClient c) {
        Connection con = DatabaseConnection.getConnection();
        try {
            PreparedStatement ps;
            ps = con.prepareStatement("Update accounts set coin = ? Where id = ?");
            ps.setInt(1, money);
            ps.setInt(2, c.getAccID());
            ps.executeUpdate();
            ps.close();
        } catch (Exception ex) {
            System.out.println("贊助 setCoin 失敗" + ex);
        }
    }
    /***
     * @獲得贊助幣
     */
    public int getCoin(MapleClient c) {
        
        Connection con = DatabaseConnection.getConnection();
        try {
            int count = 0;
            PreparedStatement ps;
            ps = con.prepareStatement("select * from accounts where id = ? ");
            ps.setInt(1, c.getAccID());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt("coin");
            } 
            rs.close();
            ps.close();
            return count;
        } catch (Exception ex) {
            System.out.println("贊助 getCoin 失敗" + ex);
            return -1;
        }
    }
    /***
     * @設置贊助幣(累積)
     */
    public void setCoinAcc(int money,MapleClient c) {
        Connection con = DatabaseConnection.getConnection();
        try {
            PreparedStatement ps;
            ps = con.prepareStatement("Update accounts set coinacc = ? Where id = ?");
            ps.setInt(1, money);
            ps.setInt(2, c.getAccID());
            ps.executeUpdate();
            ps.close();
        } catch (Exception ex) {
            System.out.println("贊助 setCoinAcc 失敗" + ex);
        }
    }
    /***
     * @獲得贊助幣(累積)
     */
    public int getCoinAcc(MapleClient c) {
        Connection con = DatabaseConnection.getConnection();
        try {
            int count = 0;
            PreparedStatement ps;
            ps = con.prepareStatement("select * from accounts where id = ? ");
            ps.setInt(1, c.getAccID());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt("coinacc");
            } 
            rs.close();
            ps.close();
            return count;
        } catch (Exception ex) {
            System.out.println("贊助 getCoinAcc 失敗" + ex);
            return -1;
        }
    }
        /***
     * @設置紅利點數
     */
    public void setBPoints(int money,MapleClient c) {
        Connection con = DatabaseConnection.getConnection();
        try {
            PreparedStatement ps;
            ps = con.prepareStatement("Update accounts set Bpoints = ? Where id = ?");
            ps.setInt(1, money);
            ps.setInt(2, c.getAccID());
            ps.executeUpdate();
            ps.close();
        } catch (Exception ex) {
            System.out.println("贊助 setBpoints 失敗" + ex);
        }
    }
    /***
     * @獲得紅利點
     */
    public int getBPoints(MapleClient c) {
        
        Connection con = DatabaseConnection.getConnection();
        try {
            int count = 0;
            PreparedStatement ps;
            ps = con.prepareStatement("select * from accounts where id = ? ");
            ps.setInt(1, c.getAccID());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt("Bpoints");
            } 
            rs.close();
            ps.close();
            return count;
        } catch (Exception ex) {
            System.out.println("贊助 getBpoints 失敗" + ex);
            return -1;
        }
    }
    
}

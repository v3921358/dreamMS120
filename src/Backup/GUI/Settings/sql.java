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

/**
 *
 * @author MSI
 */
public class sql {

    /*
     * 檢查登入IP是否允許
     */
    public static boolean hasIP(final String name, final String SessionID) {
        boolean ret = false;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) FROM accounts WHERE name = ? AND SessionIP = ?");
            ps.setString(1, name);
            ps.setString(2, SessionID);
            ResultSet rs = ps.executeQuery();
            rs.next();
            if (rs.getInt(1) > 0) {
                ret = true;
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            System.err.println("Error checking ip" + ex);
        }
        return ret;
    }
    /*
     * 檢查登入IP是否允許
     */
    public static int RoleValue(final int accountid) {
        int ret = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) as c FROM characters WHERE accountid = ? and level > 9");
            ps.setInt(1, accountid);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ret += rs.getInt("c");
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            System.err.println("Error RoleValue" + ex);
        }
        return ret;
    }
    
    /*
     * 信件功能
     */
    //查看您所有的郵件數量
    public static ResultSet getAllMail(String Reciever) {
        try {
            Connection con = (Connection) DatabaseConnection.getConnection();
            PreparedStatement ps = (PreparedStatement) con.prepareStatement("SELECT MailSender, Message FROM mail WHERE MailReciever = ? and Deleted = 0");
            ps.setString(1, Reciever);
            return ps.executeQuery();
        } catch (Exception ex) {
            System.err.println(ex);
        }

        return null;
    }

    //檢查你的新郵件數量
    public static ResultSet getNewMail(String Reciever) {
        try {
            Connection con = (Connection) DatabaseConnection.getConnection();
            PreparedStatement ps = (PreparedStatement) con.prepareStatement("SELECT MailSender, Message FROM mail WHERE MailReciever = ? and Deleted = 0 and `Read` = 0");
            ps.setString(1, Reciever);
            return ps.executeQuery();
        } catch (Exception ex) {
            System.err.println(ex);
        }
        return null;
    }
}

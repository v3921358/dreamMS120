package handling.login.handler;

import Apple.console.Settings;
import client.LoginCrypto;
import constants.ServerConstants;
import database.DatabaseConnection;
import handling.login.LoginServer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import tools.FileoutputUtil;

public class AutoRegister {

    public static final int ACCOUNTS_PER = 3;
    public static boolean autoRegister = LoginServer.getAutoReg();
    public static boolean success = false;
    public static boolean mac = true;

    public static boolean getAccountExists(String login) {
        boolean accountExists = false;
        Connection con = DatabaseConnection.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT name FROM accounts WHERE name = ?");
            ps.setString(1, login);
            ResultSet rs = ps.executeQuery();
            if (rs.first()) {
                accountExists = true;
            }
        } catch (SQLException ex) {
            System.err.println("[getAccountExists]" + ex);
        }
        return accountExists;
    }

    public static void createAccount(String login, String pwd, String eip, String macData) {
        String sockAddr = eip;
        Connection con;

        try {
            con = DatabaseConnection.getConnection();
        } catch (Exception ex) {
            System.err.println("[createAccount]" + ex);
            return;
        }
//檢查允許無限註冊IP
        boolean AllowAccount = false;
        for (String IP : ServerConstants.允許IP註冊.split(",")) {
            if (IP.equals(sockAddr.substring(1, sockAddr.lastIndexOf(':')))) {
                AllowAccount = true;
                break;
            }
        }
        try {
            ResultSet rsMacs, rsIP;
            try (PreparedStatement ipcMacs = con.prepareStatement("SELECT Macs FROM accounts WHERE macs = ?")) {
                ipcMacs.setString(1, macData);
                rsMacs = ipcMacs.executeQuery();
                try (PreparedStatement ipcIP = con.prepareStatement("SELECT SessionIP FROM accounts WHERE SessionIP = ?")) {
                    ipcIP.setString(1, "/" + sockAddr.substring(1, sockAddr.lastIndexOf(':')));
                    rsIP = ipcIP.executeQuery();
                    if ((rsIP.first() == false || rsIP.last() == true && rsIP.getRow() < ACCOUNTS_PER)
                            && (rsMacs.first() == false || rsMacs.last() == true && rsMacs.getRow() < ACCOUNTS_PER)
                            || AllowAccount) {
                        try {
                            try (PreparedStatement ps = con.prepareStatement("INSERT INTO accounts (name, password, email, birthday, macs, lastmac, SessionIP) VALUES (?, ?, ?, ?, ?, ?, ?)")) {
                                Calendar c = Calendar.getInstance();
                                int year = c.get(Calendar.YEAR);
                                int month = c.get(Calendar.MONTH) + 1;
                                int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
                                ps.setString(1, login);
                                ps.setString(2, LoginCrypto.hexSha1(pwd));
                                ps.setString(3, "autoregister@mail.com");
                                ps.setString(4, year + "-" + month + "-" + dayOfMonth);//Created day
                                ps.setString(5, macData);
                                ps.setString(6, macData);
                                ps.setString(7, "/" + sockAddr.substring(1, sockAddr.lastIndexOf(':')));
                                ps.executeUpdate();
                            }
                            success = true;
                        } catch (SQLException ex) {
                            System.err.println("createAccount" + ex);
                            return;
                        }
                    }
                    if (rsMacs.getRow() >= ACCOUNTS_PER && !AllowAccount) {
                        mac = false;
                    } else if (rsIP.getRow() >= ACCOUNTS_PER && !AllowAccount) {
                        mac = false;
                    } else {
                        mac = true;
                        FileoutputUtil.log("logs/Data/註冊帳號.txt", "帳號：" + login + " 密碼：" + pwd + " IP：/" + sockAddr.substring(1, sockAddr.lastIndexOf(':')) + " MAC： " + macData + " 註冊成功 : " + (mac ? "成功" : "失敗"));
                    }
                }
            }
            rsIP.close();
            rsMacs.close();
        } catch (SQLException ex) {
            System.err.println("[createAccount]" + ex);
        }
    }

}

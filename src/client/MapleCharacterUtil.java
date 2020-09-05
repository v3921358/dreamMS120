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
package client;

import constants.GameConstants;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import tools.Pair;
import java.util.regex.Pattern;

import database.DatabaseConnection;
//import tools.FilePrinter;

public class MapleCharacterUtil {

    private static final Pattern namePattern = Pattern.compile("[a-zA-Z0-9_-]{3,12}");
    private static final Pattern petPattern = Pattern.compile("[a-zA-Z0-9_-]{4,12}");
    public static String[] banText = {"幹", "靠", "屎", "糞", "淦", "靠"};

    public static boolean isCanTalkText(String text) {
        String message = text.toLowerCase();
        for (int i = 0; i < banText.length; i++) {
            if (message.contains(banText[i])) {
                return false;
            }
        }
        if ((message.contains("垃") && message.contains("圾"))
                || message.contains("�")
                || (message.contains("雖") && message.contains("小"))
                || (message.contains("沙") && message.contains("小"))
                || (message.contains("殺") && message.contains("小"))
                || (message.contains("三") && message.contains("小"))
                //
                || (message.contains("北") && message.contains("七"))
                || (message.contains("北") && message.contains("7"))
                || (message.contains("巴") && message.contains("七"))
                || (message.contains("巴") && message.contains("7"))
                || (message.contains("八") && message.contains("七"))
                || (message.contains("八") && message.contains("7"))
                //
                || (message.contains("白") && message.contains("目"))
                || (message.contains("白") && message.contains("癡"))
                || (message.contains("白") && message.contains("吃"))
                || (message.contains("白") && message.contains("ㄔ"))
                || (message.contains("白") && message.contains("ㄘ"))
                //
                || (message.contains("機") && message.contains("車"))
                || (message.contains("機") && message.contains("八"))
                //
                || (message.contains("伶") && message.contains("北"))
                || (message.contains("林") && message.contains("北"))
                //
                || (message.contains("廢") && message.contains("物"))
                || (message.contains("媽") && message.contains("的"))
                || (message.contains("俗") && message.contains("辣"))
                || (message.contains("智") && message.contains("障"))
                || (message.contains("低") && message.contains("能"))
                || (message.contains("乞") && message.contains("丐"))
                || (message.contains("乾") && message.contains("娘"))
                //
                || (message.contains("ㄎ") && message.contains("ㄅ"))
                || (message.contains("ㄌ") && message.contains("ㄐ"))
                || (message.contains("ㄋ") && message.contains("ㄠ") && message.contains("ˇ"))
                || (message.contains("ㄍ") && message.contains("ˋ")) //
                //
                || (message.contains("e04"))
                //
                || (message.contains("癢") && message.contains("癢") && message.contains("谷"))
                || (message.contains("天") && message.contains("堂") && message.contains("谷"))
                || (message.contains("恰") && message.contains("恰") && message.contains("谷"))
                || (message.contains("奇") && message.contains("奇") && message.contains("谷"))
                || (message.contains("農") && message.contains("藥") && message.contains("谷"))
                || (message.contains("哭") && message.contains("哭") && message.contains("谷"))
                || (message.contains("嘎") && message.contains("嘎") && message.contains("谷"))
                || (message.contains("棉") && message.contains("花") && message.contains("谷"))
                || (message.contains("回") && message.contains("憶") && message.contains("谷"))
                || (message.contains("啾") && message.contains("咪") && message.contains("谷"))
                || (message.contains("喇") && message.contains("叭") && message.contains("谷"))
                || (message.contains("瘋") && message.contains("子") && message.contains("谷"))) {
            return false;
        }
        return true;
    }

    public static final boolean canCreateChar(final String name) {
        if (getIdByName(name) != -1 || !isEligibleCharName(name)) {
            return false;
        }
        return true;
    }

    public static final boolean isEligibleCharName(final String name) {
        if (name.getBytes().length > 15) {
            return false;
        }
        if (name.getBytes().length < 3) {
            return false;
        }
        for (String z : GameConstants.RESERVED) {
            if (name.contains(z)) {
                return false;
            }
        }
        if (!isCanTalkText(name)) {
            return false;
        }
        return true;
    }

    public static final boolean canChangePetName(final String name) {
        if (petPattern.matcher(name).matches()) {
            for (String z : GameConstants.RESERVED) {
                if (name.indexOf(z) != -1) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public static final String makeMapleReadable(final String in) {
        String wui = in.replace('I', 'i');
        wui = wui.replace('l', 'L');
        wui = wui.replace("rn", "Rn");
        wui = wui.replace("vv", "Vv");
        wui = wui.replace("VV", "Vv");
        return wui;
    }

    public static final int getIdByName(final String name) {
        Connection con = DatabaseConnection.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT id FROM characters WHERE name = ?");
            ps.setString(1, name);
            final ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                rs.close();
                ps.close();
                return -1;
            }
            final int id = rs.getInt("id");
            rs.close();
            ps.close();

            return id;
        } catch (SQLException e) {
            System.err.println("error 'getIdByName' " + e);
        }
        return -1;
    }

    public static final boolean PromptPoll(final int accountid) {
        PreparedStatement ps = null;
        ResultSet rs = null;

        boolean prompt = false;
        try {
            ps = DatabaseConnection.getConnection().prepareStatement("SELECT * from game_poll_reply where AccountId = ?");
            ps.setInt(1, accountid);

            rs = ps.executeQuery();
            prompt = rs.next() ? false : true;
        } catch (SQLException e) {
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
            }
        }
        return prompt;
    }

    public static final boolean SetPoll(final int accountid, final int selection) {
        if (!PromptPoll(accountid)) { // Hacking OR spamming the db.
            return false;
        }

        PreparedStatement ps = null;
        try {
            ps = DatabaseConnection.getConnection().prepareStatement("INSERT INTO game_poll_reply (AccountId, SelectAns) VALUES (?, ?)");
            ps.setInt(1, accountid);
            ps.setInt(2, selection);

            ps.execute();
        } catch (SQLException e) {
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
            }
        }
        return true;
    }

    // -2 = An unknown error occured
    // -1 = Account not found on database
    // 0 = You do not have a second password set currently.
    // 1 = The password you have input is wrong
    // 2 = Password Changed successfully
    public static final int Change_SecondPassword(final int accid, final String password, final String newpassword) {
        Connection con = DatabaseConnection.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * from accounts where id = ?");
            ps.setInt(1, accid);
            final ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                rs.close();
                ps.close();
                return -1;
            }
            String secondPassword = rs.getString("2ndpassword");
            final String salt2 = rs.getString("salt2");
            if (secondPassword != null && salt2 != null) {
                secondPassword = LoginCrypto.rand_r(secondPassword);
            } else if (secondPassword == null && salt2 == null) {
                rs.close();
                ps.close();
                return 0;
            }
            if (!check_ifPasswordEquals(secondPassword, password, salt2)) {
                rs.close();
                ps.close();
                return 1;
            }
            rs.close();
            ps.close();

            String SHA1hashedsecond;
            try {
                SHA1hashedsecond = LoginCryptoLegacy.encodeSHA1(newpassword);
            } catch (Exception e) {
                return -2;
            }
            ps = con.prepareStatement("UPDATE accounts set 2ndpassword = ?, salt2 = ? where id = ?");
            ps.setString(1, SHA1hashedsecond);
            ps.setString(2, null);
            ps.setInt(3, accid);

            if (!ps.execute()) {
                ps.close();
                return 2;
            }
            ps.close();
            return -2;
        } catch (SQLException e) {
            System.err.println("error 'getIdByName' " + e);
            return -2;
        }
    }

    private static final boolean check_ifPasswordEquals(final String passhash, final String pwd, final String salt) {
        // Check if the passwords are correct here. :B
        if (LoginCryptoLegacy.isLegacyPassword(passhash) && LoginCryptoLegacy.checkPassword(pwd, passhash)) {
            // Check if a password upgrade is needed.
            return true;
        } else if (salt == null && LoginCrypto.checkSha1Hash(passhash, pwd)) {
            return true;
        } else if (LoginCrypto.checkSaltedSha512Hash(passhash, pwd, salt)) {
            return true;
        }
        return false;
    }

    //id accountid gender
    public static Pair<Integer, Pair<Integer, Integer>> getInfoByName(String name, int world) {
        try {

            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM characters WHERE name = ? AND world = ?");
            ps.setString(1, name);
            ps.setInt(2, world);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                rs.close();
                ps.close();
                return null;
            }
            Pair<Integer, Pair<Integer, Integer>> id = new Pair<Integer, Pair<Integer, Integer>>(rs.getInt("id"), new Pair<Integer, Integer>(rs.getInt("accountid"), rs.getInt("gender")));
            rs.close();
            ps.close();
            return id;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setNXCodeUsed(String name, String code) throws SQLException {
        Connection con = DatabaseConnection.getConnection();
        PreparedStatement ps = con.prepareStatement("UPDATE nxcode SET `user` = ?, `valid` = 0 WHERE code = ?");
        ps.setString(1, name);
        ps.setString(2, code);
        ps.execute();
        ps.close();
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

    public static void sendNote(String to, String name, String msg, int fame, int type, int itemid, int quantity) {
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("INSERT INTO notes (`to`, `from`, `message`, `timestamp`, `gift`, `type`, `itemid`, `quantity`) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            ps.setString(1, to);
            ps.setString(2, name);
            ps.setString(3, msg);
            ps.setLong(4, System.currentTimeMillis());
            ps.setInt(5, fame);
            ps.setInt(6, type);
            ps.setInt(7, itemid);
            ps.setInt(8, quantity);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            System.err.println("Unable to send note" + e);
        }
    }

    /*
    public static boolean getNXCodeValid(String code, boolean validcode) throws SQLException {
        Connection con = DatabaseConnection.getConnection();
        PreparedStatement ps = con.prepareStatement("SELECT `valid` FROM nxcode WHERE code = ?");
        ps.setString(1, code);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            validcode = rs.getInt("valid") > 0;
        }
        rs.close();
        ps.close();
        return validcode;
    }

    public static int getNXCodeType(String code) throws SQLException {
        int type = -1;
        Connection con = DatabaseConnection.getConnection();
        PreparedStatement ps = con.prepareStatement("SELECT `type` FROM nxcode WHERE code = ?");
        ps.setString(1, code);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            type = rs.getInt("type");
        }
        rs.close();
        ps.close();
        return type;
    }

    public static int getNXCodeItem(String code) throws SQLException {
        int item = -1;
        Connection con = DatabaseConnection.getConnection();
        PreparedStatement ps = con.prepareStatement("SELECT `item` FROM nxcode WHERE code = ?");
        ps.setString(1, code);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            item = rs.getInt("item");
        }
        rs.close();
        ps.close();
        return item;
    }
     */
    public static int getNXCodeValid(String code) {
        int validcode = -1;
        Connection con = DatabaseConnection.getConnection();
        try (PreparedStatement ps = con.prepareStatement("SELECT `valid` FROM nxcode WHERE code = ?")) {
            ps.setString(1, code);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    validcode = rs.getInt("valid");
                }
            }
        } catch (SQLException ex) {
            System.err.println(ex);
            //FilePrinter.printError("MapleCharacterUtil.txt", ex);
        }
        return validcode;
    }

    public static int getNXCodeType(String code) {
        int type = -1;
        try {

            Connection con = DatabaseConnection.getConnection();
            try (PreparedStatement ps = con.prepareStatement("SELECT `type` FROM nxcode WHERE code = ?")) {
                ps.setString(1, code);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        type = rs.getInt("type");
                    }
                }
            }

        } catch (SQLException ex) {
            System.err.println(ex);
            //  FilePrinter.printError("MapleCharacterUtil.txt", ex);
        }
        return type;
    }

    public static int getNXCodeItem(String code) {
        int item = -1;
        Connection con = DatabaseConnection.getConnection();
        try (PreparedStatement ps = con.prepareStatement("SELECT `item` FROM nxcode WHERE code = ?")) {
            ps.setString(1, code);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    item = rs.getInt("item");
                }
            }

        } catch (SQLException ex) {
            System.err.println(ex);
            //  FilePrinter.printError("MapleCharacterUtil.txt", ex);
        }
        return item;
    }

    public static int getNXCodeSize(String code) {
        int item = -1;
        Connection con = DatabaseConnection.getConnection();
        try (PreparedStatement ps = con.prepareStatement("SELECT `size` FROM nxcode WHERE code = ?")) {
            ps.setString(1, code);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    item = rs.getInt("size");
                }
            }

        } catch (SQLException ex) {
            System.err.println(ex);
            // FilePrinter.printError("MapleCharacterUtil.txt", ex);
        }
        return item;
    }

    public static int getNXCodeTime(String code) {
        int item = -1;
        Connection con = DatabaseConnection.getConnection();
        try (PreparedStatement ps = con.prepareStatement("SELECT `time` FROM nxcode WHERE code = ?")) {
            ps.setString(1, code);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    item = rs.getInt("time");
                }
            }

        } catch (SQLException ex) {
            System.err.println(ex);
            //FilePrinter.printError("MapleCharacterUtil.txt", ex);
        }
        return item;
    }

    public static final boolean isExistCharacterInDataBase(final int id) {
        Connection con = DatabaseConnection.getConnection();
        try {
            final String name;
            try (PreparedStatement ps = con.prepareStatement("SELECT name FROM characters WHERE id = ?")) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        rs.close();
                        ps.close();
                        return false;
                    }
                    name = rs.getString("name");
                }
            }
        } catch (SQLException e) {
            System.err.println(e);
            //FilePrinter.printError("MapleCharacterUtil.txt", e);
        }
        return true;
    }
}

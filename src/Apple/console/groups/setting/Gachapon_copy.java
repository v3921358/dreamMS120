/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Apple.console.groups.setting;

import constants.ServerConstants;
import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.MapleItemInformationProvider;
import server.Randomizer;
import tools.FileoutputUtil;
import tools.Pair;

/**
 *
 * @author Msi
 */
public class Gachapon_copy {

    private static final Gachapon_copy instance = new Gachapon_copy();
    private final List<Gachapon_copy> globaldrops = new ArrayList<>();

    protected Gachapon_copy() {
        retrieveGlobal();
    }

    public static final Gachapon_copy getInstance() {
        return instance;
    }

    public final List<Gachapon_copy> getGlobalDrop() {
        return globaldrops;
    }

    private final void retrieveGlobal() {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            final Connection con = DatabaseConnection.getConnection();
            ps = con.prepareStatement("SELECT * FROM gachapon_copy");
            rs = ps.executeQuery();
            while (rs.next()) {
                globaldrops.add(new Gachapon_copy(
                        rs.getInt("itemid"),
                        rs.getInt("chance"),
                        rs.getString("comments"),
                        rs.getInt("continent")
                ));
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.err.println("Error retrieving drop" + e);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ignore) {
            }
        }
    }

    public final void clearDrops() {
        globaldrops.clear();
        retrieveGlobal();
    }

    public Gachapon_copy(int itemId, int chance, String comments, int continent) {
        this.itemId = itemId;
        this.chance = chance;
        this.comments = comments;
        this.continent = continent;
    }

    public Gachapon_copy(int itemId, int chance, int continent, byte dropType, int Minimum, int Maximum, short questid) {
        this.itemId = itemId;
        this.chance = chance;
        this.dropType = dropType;
        this.continent = continent;
        this.questid = questid;
        this.Minimum = Minimum;
        this.Maximum = Maximum;
    }

    public String comments;
    public byte dropType;
    public short questid;
    public int itemId, chance, Minimum, Maximum, continent;
    public boolean onlySelf = false;

//載入類型
    public static Pair<Pair<Integer, Integer>, Integer> lottery(int job) {
        final Gachapon mi = Gachapon.getInstance();
        final List<Gachapon> globalEntry = new ArrayList<>(mi.getGlobalDrop());//LinkedList
        final List<Integer> Probability = new ArrayList<>();//機率
        HashMap<Integer, Integer> used = new HashMap<>();
        int tolot = 0;
        List<Integer> returnArray = new ArrayList<>();
        for (final Gachapon de : globalEntry) {//載入機率
            if (de.continent != job) {
                continue;
            }
            Probability.add(de.itemId);
            Probability.add(de.chance);
            used.put(de.itemId, de.chance);
            tolot += de.chance;

        }
        processGachapon(returnArray, Probability);
        compiledGachapon = returnArray;
        int rw = compiledGachapon.get(Randomizer.nextInt(compiledGachapon.size()));
        return new Pair<>(new Pair<>(rw, used.get(rw)), tolot);
        // return new Pair<>(new Pair<>(0, 1), 5);
    }

    public static Pair<String, String> SearchConsole(int job, int s) {
        StringBuilder sbList = new StringBuilder("\r\n");
        StringBuilder sbItem = new StringBuilder("\r\n");
        StringBuilder 轉蛋機_DEBUG = new StringBuilder();
        final Gachapon mi = Gachapon.getInstance();
        final List<Gachapon> globalEntry = new ArrayList<>(mi.getGlobalDrop());
        final List<Integer> Probability = new ArrayList<>();//機率
        int count = 0;//次數
        //載入機率列表
        for (final Gachapon de : globalEntry) {//載入機率
            if (!Probability.contains(de.chance) && de.continent == job) {
                Probability.add(de.chance);
            }
        }
        Collections.sort(Probability);//排序
        switch (s) {
            case -2://玩家顯示
                sbList.append("#L999#").append("查看有機率獲得的獎品").append("#l");
                break;
            case -1://GM顯示
                int a = 1;
                for (int item : Probability) {
                    if ((a - 1) % 4 == 0) {
                        sbList.append("\r\n");
                    }
                    sbList.append("#L").append(a).append("#").append("權重" + item).append("#l").append("\t");
                    a += 1;
                }//輸出完畢
                break;
            case 999:
                //載入指定機率列表
                for (final Gachapon de : globalEntry) {//載入機率
                    if (de.chance != Collections.max(Probability) && de.continent == job) {
                        sbItem.append("#v").append(de.itemId).append(":#");
                        count++;
                    }
                }
                break;
            default:
                //載入指定機率列表
                for (final Gachapon de : globalEntry) {//載入機率
                    if (de.chance == Probability.get(s - 1) && de.continent == job) {
                        if (count % 4 == 0 && ServerConstants.轉蛋機_DEBUG) {
                            sbItem.append("\r\n");
                        }
                        sbItem.append("#v").append(de.itemId).append(":#").append(ServerConstants.轉蛋機_DEBUG ? de.itemId : "");
                        if (de.itemId >= 1300000 && de.itemId < 1800000 && ServerConstants.轉蛋機_DEBUG) {
                            轉蛋機_DEBUG.append(de.itemId).append(",");
                        }
                        count++;
                    }
                }
                break;
        }
        if (ServerConstants.轉蛋機_DEBUG) {
            System.err.println(轉蛋機_DEBUG);
        }
        return new Pair<>("#e查看本機台擁有..#n#b" + sbList.toString(), "以下列表共有" + count + sbItem.toString());
    }

    public static void setGachapon(int itemid, int chance, String comments, int continent, int onlySelf) {
        Connection con = DatabaseConnection.getConnection();
        try (PreparedStatement ps = con.prepareStatement("INSERT INTO gachapon_copy (`itemid`, `chance`, `comments`, `continent`, `onlySelf`) VALUES (?, ?, ?, ?, ?)")) {
            ps.setInt(1, itemid);
            ps.setInt(2, chance);
            ps.setString(3, comments);
            ps.setInt(4, continent);
            ps.setInt(5, onlySelf);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("ID:" + itemid + " 名稱: " + comments + " - err: " + e);
        }
    }

    public static void delGachapon(int id) {
        Connection con = DatabaseConnection.getConnection();
        try (PreparedStatement ps = con.prepareStatement("DELETE FROM gachapon_copy WHERE itemid = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    public static final void getGachapon() {
        ret.clear();
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM gachapon_copy");
            //ps.setInt(1, monsterId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ret.add(rs.getInt("itemid"));
            }
        } catch (SQLException e) {
            System.err.println(e);
        }
    }
    public static final List<Integer> ret = new LinkedList<>();

    private static List<Integer> compiledGachapon = null;

    private final static void processGachapon(final List<Integer> returnArray, final List<Integer> list) {
        int lastitem = 0;
        for (int i = 0; i < list.size(); i++) {
            if (i % 2 == 0) { // Even
                lastitem = list.get(i);
            } else { // Odd
                for (int j = 0; j < list.get(i); j++) {
                    returnArray.add(lastitem);
                }
            }
        }
        Collections.shuffle(returnArray);
    }
}

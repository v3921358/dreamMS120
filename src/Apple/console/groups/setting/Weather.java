/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Apple.console.groups.setting;

import database.DatabaseConnection;
import handling.world.World;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import server.Randomizer;
import tools.MaplePacketCreator;

/**
 *
 * @author Msi
 */
public class Weather {

    public static boolean Change = false;
    public static int MaxSize = 6;
    public static SimpleDateFormat sdf = new SimpleDateFormat("HH時mm分ss秒");

    public static final void getInstance() {
        if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) == 0 && Calendar.getInstance().get(Calendar.MINUTE) == 0 && Calendar.getInstance().get(Calendar.SECOND) == 0) {
            NewWeather();
            System.err.println(sdf.format(new Date()) + "\t[New] 重製氣象活動. [現在時間" + Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + "H]");
            return;
        }
        if (Calendar.getInstance().get(Calendar.SECOND) == 0) {
            RunWeather();
            return;
        }
    }

    //讀取氣象資料
    private static final void RunWeather() {
        ResetWeather();
        SimpleDateFormat sdf = new SimpleDateFormat("HH時mm分ss秒");
        int HOUR = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        for (final Weather de : Weather) {//載入機率
            if (de.Hour == HOUR) {
                Change = true;
                if (de.Read == 0) {
                    //運行天氣
                    World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(0, "氣象活動開始了, 經驗值獲得率提升: 150%"));
                    World.Broadcast.broadcastMessage(MaplePacketCreator.startMapEffect(null, 5120000, false));
                    System.err.println(sdf.format(new Date()) + "\t[Run] 氣象活動開始了. [現在時間" + HOUR + "H]");
                    //更新天氣
                    UpdateWeather(de.Hour);
                    System.err.println(sdf.format(new Date()) + "\t[UPDATE] 更新氣象活動. [現在時間" + HOUR + "H]");
                }
                break;
            } else if (de.Hour == (HOUR - 1) && de.Read == 1) {
                Change = false;
                World.Broadcast.broadcastMessage(MaplePacketCreator.removeMapEffect());//取消特效
                //移除天氣
                RemovedWeather();
                System.err.println(sdf.format(new Date()) + "\t[Del] 移除氣象活動. [移除時間" + de.Hour + "H]");
                World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(0, "無氣象活動."));
                System.err.println(sdf.format(new Date()) + "\t[Null] 無氣象活動. [現在時間" + HOUR + "H]");
            } else {
                Change = false;
            }
        }
    }

    private static final void ResetWeather() {
        Weather.clear();
        ReadWeather();
    }

    public static final void NewWeather() {
        try {
            ResetWeather();
            java.util.List<Integer> 時間 = new ArrayList();
            while (時間.size() < MaxSize) {
                int Rand = Randomizer.rand(1, 23);
                if (!時間.contains(Rand)) {
                    時間.add(Rand);
                    //添加
                    Connection con = DatabaseConnection.getConnection();
                    PreparedStatement ps = con.prepareStatement("INSERT INTO accounts_exp (`day`, `hour`) VALUES (?, ?)");
                    ps.setInt(1, Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                    ps.setInt(2, Rand);
                    ps.executeUpdate();
                    ps.close();
                }
            }
            System.err.println("本日氣象活動時間" + 時間);
        } catch (SQLException se) {
            System.out.println("SQLException: " + se.getLocalizedMessage());
            se.printStackTrace();
        }
    }

    private static final void UpdateWeather(int h) {
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("UPDATE accounts_exp SET `Read` = 1 WHERE hour = ? and `Read` = 0");
            ps.setInt(1, h);
            ps.execute();
            ps.close();
        } catch (SQLException se) {
            System.out.println("SQLException: " + se.getLocalizedMessage());
            se.printStackTrace();
        }
    }

    private static final void RemovedWeather() {
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement Delps = con.prepareStatement("DELETE FROM accounts_exp WHERE `Read` = 1");
            Delps.executeUpdate();
            Delps.close();
        } catch (SQLException se) {
            System.out.println("SQLException: " + se.getLocalizedMessage());
            se.printStackTrace();
        }
    }
//-----------------------------------------------------------------------------------------------------------
    private int Hour, Read;

    private Weather(int Hour, int Read) {
        this.Hour = Hour;
        this.Read = Read;
    }

    private static final List<Weather> Weather = new ArrayList<>();

    private static final void ReadWeather() {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            final Connection con = DatabaseConnection.getConnection();
            ps = con.prepareStatement("SELECT * FROM accounts_exp");
            rs = ps.executeQuery();
            while (rs.next()) {
                Weather.add(new Weather(rs.getInt("Hour"), rs.getInt("Read")));
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.err.println("Error retrieving accounts_exp" + e);
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
}

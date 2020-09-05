package Apple.console;

//import configs.Config;
//import configs.ServerConfig;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import server.Timer;
//import server.console.Start;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Msi
 */
public class Settings {

    public static class GuiTimer extends Timer {

        private static final GuiTimer instance = new GuiTimer();

        private GuiTimer() {
            name = "GuiTimer";
        }

        public static GuiTimer getInstance() {
            return instance;
        }
    }

    public static String getNowTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH時mm分ss秒");
        return sdf.format(new Date());
    }

    public static boolean isNumber(final String str) {
        return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
    }

    /*
     * 重置功能
     */
    public static class Reset {

        public static void getInstance() {
            run();
        }

        public static void getInstance_Time() {
            int 時 = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            int 分 = Calendar.getInstance().get(Calendar.MINUTE);
            int 秒 = Calendar.getInstance().get(Calendar.SECOND); 
            if (時 == 0 && 分 == 0 && 秒 == 0) {
                run();
            }
        }

        private static void run() {
            System.out.println("[準備重置系統]");
            Reset_BossLog("NowDayCs", 0);//當日點數
            System.err.println("[重置完畢]");
        }

        /*
         * 重製
         */
        private static int Reset_BossLog(String boss, int type) {
            try (Connection con = DatabaseConnection.getConnection()) {
                int count = 0;
                PreparedStatement ps;
                ps = con.prepareStatement("SELECT * FROM bosslog WHERE bossid = ?");
                ps.setString(1, boss);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    /*
                 * 年：calendar.get(Calendar.YEAR)
                 * 月：calendar.get(Calendar.MONTH)+1
                 * 日：calendar.get(Calendar.DAY_OF_MONTH)
                 * 星期：calendar.get(Calendar.DAY_OF_WEEK)-1
                     */
                    count = rs.getInt("count");
                    if (count < 0) {
                        return count;
                    }
                    Timestamp bossTime = rs.getTimestamp("time");
                    rs.close();
                    ps.close();
                    if (type == 0) {
                        Calendar sqlcal = Calendar.getInstance();
                        if (bossTime != null) {
                            sqlcal.setTimeInMillis(bossTime.getTime());
                        }
                        if (sqlcal.get(Calendar.DAY_OF_MONTH) + 1 <= Calendar.getInstance().get(Calendar.DAY_OF_MONTH) || sqlcal.get(Calendar.MONTH) + 1 <= Calendar.getInstance().get(Calendar.MONTH) || sqlcal.get(Calendar.YEAR) + 1 <= Calendar.getInstance().get(Calendar.YEAR)) {
                            count = 0;
                            ps = con.prepareStatement("UPDATE bosslog SET count = 0, time = CURRENT_TIMESTAMP() WHERE bossid = ?");
                            ps.setString(1, boss);
                            ps.executeUpdate();
                        }
                    }
                }
                rs.close();
                ps.close();
                return count;
            } catch (Exception Ex) {
                System.err.println(Ex);
                //log.error("获取BOSS挑战次数.", Ex);
                return -1;
            }
        }
    }
}

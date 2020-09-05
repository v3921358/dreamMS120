/*
 此文件是 風迷.OD.TW 核心服務器 -<MapleStory Server>
 Copyright (C) 2008 Patrick Huy <patrick.huy@frz.cc>
 Matthias Butz <matze@MrCoffee.de>
 Jan Christian Meyer <vimes@MrCoffee.de>

 此文件原作者為德國  OdinMS 團隊  以上是開發人員聯繫訊息 本程序遵守
 版本首要發布  GNU 協議進行修改發布 你可以無偿使用此文件或者進行修改
 但是禁止使用本程序進行一切商業行為,如有發現 根據當地法律的制度 導致
 任何法律責任，我們將不予承擔 本程序發布是免費的 並不收取額外費用 擁有
 此文件副本的人遵守 GNU 規定 但請保留修改發布人的訊息 謝謝！
 ==============================================================
 當前版本修復製作維護人員: 風迷人物
 您應該已經收到一份拷貝的GNU通用公共許可證Affero程式一起。如果不是，請參閱
 <http://www.gnu.org/licenses/>.
 */
package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.locks.ReentrantLock;

import server.ServerProperties;

/**
 * All OdinMS servers maintain a Database Connection. This class therefore
 * "singletonices" the connection per process.
 *
 *
 * @author Frz
 */
public class DatabaseConnection1 {

    public static final int RETURN_GENERATED_KEYS = 1;
    private static final HashMap<Integer, ConWrapper> connections = new HashMap();
    public static String dbUser = "root", dbPass = "", dbIp = "localhost", dbName = "twms";
    public static int dbport = 3306;
    private static final long connectionTimeOut = 30 * 60 * 1000;
    private static final ReentrantLock lock = new ReentrantLock();// 锁对象

    public static int getConnectionsCount() {
        return connections.size();
    }

    public static void close() {
        try {
            Thread cThread = Thread.currentThread();
            Integer threadID = (int) cThread.getId();
            ConWrapper ret = connections.get(threadID);
            if (ret != null) {
                Connection c = ret.getConnection();
                if (!c.isClosed()) {
                    c.close();
                }
                lock.lock();
                try {
                    connections.remove(threadID);
                } finally {
                    lock.unlock();
                }
            }

        } catch (SQLException ex) {
        }
    }

    public static Connection getConnection() {

        if (!isInitialized()) {
            InitDB();
        }

        Thread cThread = Thread.currentThread();
        Integer threadID = (int) cThread.getId();
        ConWrapper ret;

        ret = connections.get(threadID);

        if (ret == null) {
            Connection retCon = connectToDB();
            ret = new ConWrapper(threadID, retCon);
            lock.lock();
            try {
                connections.put(threadID, ret);
            } finally {
                lock.unlock();
            }
        }

        Connection c = ret.getConnection();
        try {
            if (c.isClosed()) {
                Connection retCon = connectToDB();
                lock.lock();
                try {
                    connections.remove(threadID);
                    connections.put(threadID, ret);
                } finally {
                    lock.unlock();
                }
                ret = new ConWrapper(threadID, retCon);
            }
        } catch (Exception e) {

        } finally {

        }

        return ret.getConnection();
    }

    static class ConWrapper {

        private final int tid;
        private long lastAccessTime;
        private Connection connection;

        public ConWrapper(int tid, Connection con) {
            this.tid = tid;
            this.lastAccessTime = System.currentTimeMillis();
            this.connection = con;
        }

        public boolean close() {
            boolean ret = false;

            if (connection == null) {
                ret = false;
            } else {

                try {
                    lock.lock();
                    try {
                        if (expiredConnection() || this.connection.isValid(10)) {

                            try {

                                this.connection.close();
                                ret = true;
                            } catch (SQLException e) {
                                ret = false;
                            }
                        }
                        connections.remove(tid);
                    } finally {
                        lock.unlock();
                    }
                } catch (SQLException ex) {
                    ret = false;

                }
            }

            return ret;
        }

        public Connection getConnection() {
            if (expiredConnection()) {
                try { // Assume that the connection is stale
                    connection.close();
                } catch (SQLException err) {
                }
                this.connection = connectToDB();
            }
            lastAccessTime = System.currentTimeMillis(); // Record Access
            return this.connection;
        }

        /**
         * Returns whether this connection has expired
         *
         * @return
         */
        public boolean expiredConnection() {
            return System.currentTimeMillis() - lastAccessTime >= connectionTimeOut;
        }
    }

    private static Connection connectToDB() {

        try {
            Properties props = new Properties();
            props.put("user", dbUser);
            props.put("password", dbPass);
            props.put("autoReconnect", "true");
            props.put("characterEncoding", "UTF8");
            props.put("connectTimeout", "2000000");
            props.put("serverTimezone", "Asia/Taipei");
            Connection con = DriverManager.getConnection("jdbc:mysql://" + dbIp + ":" + dbport + "/" + dbName/* + "?autoReconnect=true&characterEncoding=UTF8&connectTimeout=120000000"*/, props);

            PreparedStatement ps;
            ps = con.prepareStatement("SET time_zone = '+08:00'");
            ps.execute();
            ps.close();

            return con;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    public static boolean isInitialized() {
        return !dbUser.equals("");
    }

    public static void InitDB() {
        dbName = ServerProperties.getProperty("tms.Name", dbName);
        dbIp = ServerProperties.getProperty("tms.Ip", dbIp);
        dbport = Integer.parseInt(ServerProperties.getProperty("tms.Port", Integer.toString(dbport)));
        dbUser = ServerProperties.getProperty("tms.User", dbUser);
        dbPass = ServerProperties.getProperty("tms.Pass", dbPass);
    }

    private static void PROCESSLIST() {
        try (Connection con = DatabaseConnection1.getConnection()) {
            try (PreparedStatement ps = con.prepareStatement("select count(*) from information_schema.PROCESSLIST")) {
                ResultSet rs = ps.executeQuery();
                rs.next();
                System.out.println("PROCESSLIST:" + rs.getInt(1));
                rs.close();
                ps.close();
            }
        } catch (SQLException sQLException) {
            System.err.println("Error while set pqlog: " + sQLException);
        }
    }

    public static void closeTimeout() {
        int i = 0;
        lock.lock();
        List<Integer> keys = new ArrayList(connections.keySet());
        try {
            closeAll();
            PROCESSLIST();
            System.err.print("連結數量 " + getConnectionsCount());
            System.err.print("\tcloseTimeout - ");
            for (Integer tid : keys) {
                ConWrapper con = connections.get(tid);
                System.err.print("con " + con.tid + "[");
                if (con.close()) {
                    System.err.print(i + ", ");
                    i++;
                }
            }
            System.err.println("]");
        } finally {
            lock.unlock();
        }
    }

    public static void closeAll() {
        synchronized (connections) {
            for (ConWrapper con : connections.values()) {
                try {
                    con.connection.close();
                } catch (SQLException ex) {
                }
            }
        }
    }

    public final static Runnable CloseSQLConnections = new Runnable() {

        @Override
        public void run() {
            DatabaseConnection1.closeTimeout();
        }
    };

    static {
        InitDB();
    }
}

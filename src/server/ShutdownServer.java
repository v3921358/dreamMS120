package server;

import java.sql.SQLException;

import database.DatabaseConnection;
import handling.cashshop.CashShopServer;
import handling.channel.ChannelServer;
import handling.login.LoginServer;
import handling.world.World;
import server.Timer.*;
import tools.MaplePacketCreator;

public class ShutdownServer implements Runnable {

    private static final ShutdownServer instance = new ShutdownServer();
    public static boolean running = false;
    private int time = 0;
    private boolean first = true;

    public static ShutdownServer getInstance() {
        return instance;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void shutdown() {//can execute twice
        run();
    }

    @Override
    public void run() {
        synchronized (this) {
            if (running) { //Run once!
                return;
            }
            running = true;
        }
        World.isShutDown = true;
        if (time != 0) {
            World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(0, " 遊戲服務器將在" + getTime() + "分鐘後關閉維護，請玩家安全下線..."));
            if (first) {
                first = false;
                for (ChannelServer cs : ChannelServer.getAllInstances()) {
                    cs.setShutdown();
                    cs.setServerMessage("遊戲服務器將關閉維護，請玩家安全下線...");
                    //cs.closeAllMerchants();//關閉精靈商人 考慮和closeAllMerchant
                    //cs.closeAllFisher();//關閉連結
                }
            }
            running = false;
            return;
        }
        //try {
            for (ChannelServer cs : ChannelServer.getAllInstances()) {
                cs.setShutdown();
            }
            LoginServer.shutdown();
            Integer[] chs = ChannelServer.getAllInstance().toArray(new Integer[0]);

            for (int i : chs) {
                try {
                    ChannelServer cs = ChannelServer.getInstance(i);
                    synchronized (this) {
                        cs.shutdown(this);
                        try {
                            this.wait();
                        } catch (InterruptedException ex) {
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            CashShopServer.shutdown();
            World.Guild.save();
            World.Alliance.save();
            World.Family.save();
            //DatabaseConnection.closeAll();
        //} catch (SQLException e) {
        //    System.err.println("THROW" + e);
        //}
        WorldTimer.getInstance().stop();
        MapTimer.getInstance().stop();
        MobTimer.getInstance().stop();
        BuffTimer.getInstance().stop();
        CloneTimer.getInstance().stop();
        EventTimer.getInstance().stop();
        EtcTimer.getInstance().stop();
//		PingTimer.getInstance().stop();

        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            //shutdown
        }
        System.exit(0); //not sure if this is really needed for ChannelServer
    }
}

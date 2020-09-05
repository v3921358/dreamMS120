package Backup.GUI;

import Apple.console.ProductID;
import Apple.console.Settings;
import client.SkillFactory;
import handling.channel.ChannelServer;
import handling.channel.MapleGuildRanking;
import handling.login.LoginServer;
import handling.cashshop.CashShopServer;
import handling.login.LoginInformationProvider;
import handling.world.World;
import java.sql.SQLException;
import database.DatabaseConnection;
import handling.world.family.MapleFamilyBuff;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.PreparedStatement;
import server.Timer.*;
import server.events.MapleOxQuizFactory;
import server.life.MapleLifeFactory;
import server.quest.MapleQuest;
import console.consoleStart;
import javax.swing.ImageIcon;
import server.AutobanManager;
import server.CashItemFactory;
import server.MTSStorage;
import server.MapleCarnivalFactory;
import server.MapleItemInformationProvider;
import server.RandomRewards;
import server.RankingWorker;
import server.ServerProperties;
import server.ShutdownServer;
import server.SpeedRunner;

public class Start {

    public static final consoleStart instances = new consoleStart();
    public static final Start instance = new Start();

    public static ImageIcon loadIcon(final String path) {
        return new ImageIcon(Start.class.getResource("/icon/" + path));
    }

    public static ImageIcon getMainIcon() {
        return loadIcon("1002140.png");
    }

    public static Start getInstance() {
        return instance;
    }

    public final static void WuShengForm() {
        //計時
        Settings.GuiTimer.getInstance().start();
        /*  WuShengForm wf = new WuShengForm();
         wf.setVisible(true);//啟動GUI
         wf.setResizable(false);//設置GUI
         Position(wf, 1);*/
        backup.GUI.run run = new backup.GUI.run();
        run.setVisible(true);//啟動GUI
        run.setResizable(false);//設置GUI
        run.setLocationRelativeTo(null);
        //Position(run, 4);
    }

    public final static void main(final String args[]) throws InterruptedException, SQLException {
        boolean run = false;
       //    run = true;
        if (run) {
            WuShengForm();
        } else {
            WuShengForm();
            instance.run();
            instances.run();
        }
    }

    public static void run() throws InterruptedException {
        System.out.println("[!!! 惡魔谷啟動 !!!]");
        System.out.println("[!!! 寫入裝備分析 !!!]");
        ProductID.IitemLoad();//數據寫入

        if (Boolean.parseBoolean(ServerProperties.getProperty("tms.Admin"))) {
            System.out.println("[!!! 管理員模式 !!!]");
        }
        if (Boolean.parseBoolean(ServerProperties.getProperty("tms.AutoRegister"))) {
            System.out.println("開啟註冊模式 :::");
        }
        try {
            final PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("UPDATE accounts SET loggedin = 0");
            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            throw new RuntimeException("[EXCEPTION] Please check if the SQL server is active." + ex);
        }

        World.init();
        WorldTimer.getInstance().start();
        EtcTimer.getInstance().start();
        MapTimer.getInstance().start();
        MobTimer.getInstance().start();
        CloneTimer.getInstance().start();
        EventTimer.getInstance().start();
        BuffTimer.getInstance().start();
//        LoginInformationProvider.getInstance();
//        MapleQuest.initQuests();
//        MapleLifeFactory.loadQuestCounts();
//        ItemMakerFactory.getInstance();
        MapleItemInformationProvider.getInstance().load();
        System.out.println("[載入髮型臉部物件]");
        MapleItemInformationProvider.getInstance().loadStyles(false);
//        RandomRewards.getInstance();
//        SkillFactory.getSkill(99999999);
//        MapleOxQuizFactory.getInstance().initialize();
//        MapleCarnivalFactory.getInstance();
//        MapleGuildRanking.getInstance().getRank();
//        MapleFamilyBuff.getBuffEntry();
//        RankingWorker.getInstance().run();
//        MTSStorage.load();
//        CashItemFactory.getInstance().initialize();
        LoginServer.run_startup_configurations();
        ChannelServer.startChannel_Main();

        System.out.println("[購物商城伺服器啟動中]");
        CashShopServer.run_startup_configurations();
        System.out.println("[購物商城伺服器啟動完成]");
        CheatTimer.getInstance().register(AutobanManager.getInstance(), 60000);
        Runtime.getRuntime().addShutdownHook(new Thread(new Shutdown()));
        try {
            SpeedRunner.getInstance().loadSpeedRuns();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        World.registerRespawn();
        LoginServer.setOn();
       // System.out.println("加載完成 :::");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.gc();
        PingTimer.getInstance().register(System::gc, 1800000);
        //加載
        LoginInformationProvider.getInstance();
        MapleQuest.initQuests();
        MapleLifeFactory.loadQuestCounts();
        RandomRewards.getInstance();
         System.out.println("[惡魔谷特效載入]");
        SkillFactory.getSkill(99999999);
        MapleOxQuizFactory.getInstance().initialize();
        MapleCarnivalFactory.getInstance();
        MapleGuildRanking.getInstance().getRank();
        MapleFamilyBuff.getBuffEntry();
        RankingWorker.getInstance().run();
        MTSStorage.load();
        CashItemFactory.getInstance().initialize();
         System.out.println("[報告 :　惡魔谷啟動完畢]");
         System.out.println("[老大 可以開始了]");
    }

    public static class Shutdown implements Runnable {

        @Override
        public void run() {
            new Thread(ShutdownServer.getInstance()).start();
        }
    }

}

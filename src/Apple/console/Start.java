package Apple.console;

import Apple.console.Panel.SearchGeneratorUI;
import Apple.console.Panel.加載;
import Apple.console.Panel.搜索;
import Apple.console.Panel.獎勵;
import Apple.console.Panel.開關;
import Apple.console.groups.setting.Weather;
import Apple.console.groups.database.DataBaseManagePanel;
import Apple.console.groups.datamanage.DataManagePanel;
import Apple.console.groups.setting.Gachapon;
import backup.GUI.Settings.TableData;
import client.LoginCrypto;
import client.MapleCharacter;
import com.alee.extended.label.WebHotkeyLabel;
import com.alee.extended.painter.TitledBorderPainter;
import com.alee.extended.panel.GroupPanel;
import com.alee.extended.progress.WebProgressOverlay;
import com.alee.extended.statusbar.WebMemoryBar;
import com.alee.extended.statusbar.WebStatusBar;
import com.alee.global.StyleConstants;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.button.WebButton;
import com.alee.laf.combobox.WebComboBox;
import com.alee.laf.label.WebLabel;
import com.alee.laf.menu.WebMenuItem;
import com.alee.laf.menu.WebPopupMenu;
import com.alee.laf.optionpane.WebOptionPane;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.progressbar.WebProgressBar;
import com.alee.laf.scroll.WebScrollBar;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.separator.WebSeparator;
import com.alee.laf.text.WebTextField;
import com.alee.laf.text.WebTextPane;
import com.alee.utils.ThreadUtils;
//import configs.Config;
//import configs.ServerConfig;
//import handling.world.WorldBroadcastService;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
/*import server.console.groups.boss.BossManagePanel;
import server.console.groups.database.DataBaseManagePanel;
import server.console.groups.datamanage.DataManagePanel;
import server.console.groups.setting.ConfigPanel;*/
import tools.MaplePacketCreator;
import tools.Pair;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;
import java.util.concurrent.ScheduledFuture;
import client.SkillFactory;
import com.alee.laf.rootpane.WebFrame;
import constants.ServerConstants;
import database.DatabaseConnection;
import handling.channel.ChannelServer;
import handling.channel.MapleGuildRanking;
import handling.login.LoginServer;
import handling.cashshop.CashShopServer;
import handling.channel.PlayerStorage;
import handling.login.LoginInformationProvider;
import handling.login.handler.AutoRegister;
import handling.world.World;
import java.sql.SQLException;
import handling.world.family.MapleFamilyBuff;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import server.Timer.*;
import server.events.MapleOxQuizFactory;
import server.life.MapleLifeFactory;
import server.quest.MapleQuest;
import javax.swing.ImageIcon;
import server.AutobanManager;
import server.CashItemFactory;
import server.MTSStorage;
import server.MapleCarnivalFactory;
import server.MapleItemInformationProvider;
import server.RandomRewards;
import server.Randomizer;
import server.RankingWorker;
import server.ServerProperties;
import server.ShutdownServer;
import server.SpeedRunner;
import static server.quest.MapleQuestRequirementType.item;
import tools.FileoutputUtil;

public class Start extends WebFrame {

    //  private static final Logger log = LogManager.getLogger(Start.class.getName());
    public static boolean startFinish = false;
    private static Start instance = null;
    private final StartFrame progress;
    private final Thread start_thread;
    private final String server_version;
    private WebTextPane textPane;
    private long starttime = 0;
    private ScheduledFuture<?> shutdownServer, startRunTime;
    private WebHotkeyLabel[] labels;
    private boolean autoScroll = true;
    private WebHotkeyLabel runningTimelabel;
    //設定
    public static short LOGIN_MAPLE_VERSION = 120;
    public static String LOGIN_MAPLE_PATCH = "1";
    public static int CHANNEL_PORTS = Integer.parseInt(ServerProperties.getProperty("tms.Count", "0"));
    public static String LOGIN_SERVERMESSAGE = "歡迎來到惡魔谷120，祝你玩得開心！";
    //private DatabaseConnection.DataBaseStatus dataBaseStatus;

    public Start() {
        start_thread = new Thread(new StartThread());

        // 創建主面板
        final WebPanel contentPane = new WebPanel();

        contentPane.setPreferredSize(1000, 600);
        setMinimumSize(contentPane.getPreferredSize());

        Settings.GuiTimer.getInstance().start();//GUI計時

        ProgressBarObservable progressBarObservable = new ProgressBarObservable();
        ProgressBarObserver progressBarObserver = new ProgressBarObserver(progressBarObservable);

        progress = createProgressDialog();
        progress.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                System.exit(0);
            }
        });

        progress.setIconImage(getMainIcon().getImage());
        progress.setTitle("惡魔服務端正在啟動...");
        setIconImage(getMainIcon().getImage());
        setLayout(new BorderLayout());

        Properties properties = new Properties();
        /*     try {
            properties.load(Start.class.getClassLoader().getResourceAsStream("application.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        server_version = properties.getProperty("version");

        progressBarObservable.setProgress(new Pair<>("初始化配置...", 0));
        //configs.Config.load();
        progressBarObservable.setProgress(new Pair<>("檢查網絡狀態...", 10));

        progressBarObservable.setProgress(new Pair<>("初始化數據庫配置...", 30));
        //dataBaseStatus = DatabaseConnection.getInstance().TestConnection();
        //InitializeServer.initializeRedis(false, progressBarObservable);

        ThreadUtils.sleepSafely(1000);
        progress.setVisible(false);

        contentPane.add(createMainPane(), BorderLayout.CENTER);
        contentPane.add(createStatusBar(), BorderLayout.SOUTH);

        add(contentPane);

        progressBarObserver.deleteObserver(progressBarObservable);
        progressBarObservable.deleteObservers();

        setTitle("惡魔谷服務端  當前遊戲版本: v." + LOGIN_MAPLE_VERSION + "." + LOGIN_MAPLE_PATCH + " 服務端版本: " + "1026");

        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int result = WebOptionPane.showConfirmDialog(instance, "確定要退出？", "警告", WebOptionPane.YES_NO_OPTION);
                if (result == WebOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });

        SwingUtilities.invokeLater(DataManagePanel::getInstance);
        System.setOut(new PrintStream(new NewOutputStram((byte) 0)));
        System.setErr(new PrintStream(new NewOutputStram((byte) 1)));
    }

    public static Start getInstance() {
        if (instance == null) {
            instance = new Start();
        }
        return instance;
    }

    public static ImageIcon loadIcon(final String path) {
        return new ImageIcon(Start.class.getResource("/icon/" + path));
    }

    public static ImageIcon getMainIcon() {
        return loadIcon("1002140.png");
    }

    private static void checkSingleInstance() {
        try {
            new ServerSocket(26351);
        } catch (IOException ex) {
            if (ex.getMessage().contains("Address already in use: JVM_Bind")) {
                WebOptionPane.showMessageDialog(instance, "同一台電腦只能運行一個服務端，若因服務端未正常關閉，請在任務管理器內結束javaw.exe進程", "錯誤", WebOptionPane.ERROR_MESSAGE);
                System.out.println();
            }
            System.exit(0);
        }
    }

    private static void run() {
        Start.getInstance().display();
        //  Start.getInstance().testDatabaseConnection();
    }

    public static void main(String[] args) {
        checkSingleInstance();
        final FontUIResource fontUIResource = new FontUIResource("微軟雅黑", 0, 12);
        WebLookAndFeel.globalControlFont = fontUIResource;
        WebLookAndFeel.globalTooltipFont = fontUIResource;
        WebLookAndFeel.globalAlertFont = fontUIResource;
        WebLookAndFeel.globalMenuFont = fontUIResource;
        WebLookAndFeel.globalAcceleratorFont = fontUIResource;
        WebLookAndFeel.globalTitleFont = fontUIResource;
        WebLookAndFeel.globalTextFont = fontUIResource;
        WebLookAndFeel.toolTipFont = fontUIResource;
        WebLookAndFeel.textPaneFont = fontUIResource;
        WebLookAndFeel.install();

        run();
    }

    public static void showMessage(String error, String title, int type) {
        WebOptionPane.showMessageDialog(null, error, title, type);
    }

    public String getServer_version() {
        return server_version;
    }

    /*   public void setDataBaseStatus(DatabaseConnection.DataBaseStatus dataBaseStatus) {
        this.dataBaseStatus = dataBaseStatus;
    }

    private boolean testDatabaseConnection() {
        if (!dataBaseStatus.equals(DatabaseConnection.DataBaseStatus.連接成功)) {
            if (WebOptionPane.showConfirmDialog(instance, "數據庫連接失敗，將轉到配置頁面，請務必通過測試連接，否則服務端無法啟動", "", WebOptionPane.OK_CANCEL_OPTION) == WebOptionPane.OK_OPTION) {
                showConfigPanel();
            }
            return false;
        }
        return true;
    }*/
    private void display() {
        SwingUtilities.invokeLater(() -> {
            setVisible(true);
        });
    }

    private StartFrame createProgressDialog() {
        final StartFrame progress = new StartFrame();
        progress.setUndecorated(true);
        progress.pack();
        progress.setResizable(false);
        progress.setLocationRelativeTo(null);
        progress.setVisible(true);

        return progress;
    }

    private Component createMainPane() {
        final WebPanel contentPane = new WebPanel();

        contentPane.setLayout(new BorderLayout());

        // 創建運行日誌
        final WebPanel runningLogPane = new WebPanel(new BorderLayout());
        runningLogPane.setPainter(new TitledBorderPainter("運行日誌")).setMargin(2);
        runningLogPane.setPreferredSize(660, 300);
        textPane = new WebTextPane();
        textPane.setEditable(false);
        textPane.setComponentPopupMenu(new WebPopupMenu() {
            {
                add(new WebMenuItem("清屏") {
                    {
                        addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                textPane.clear();
                            }
                        });
                    }
                });
            }
        });
        final WebScrollPane textPaneScroll = new WebScrollPane(textPane);
        textPaneScroll.createVerticalScrollBar();

        // 實現滾動條到達底部後自動滾動，否則不自動滾動
        textPaneScroll.addMouseWheelListener(e -> {
            WebScrollBar scrollBar = textPaneScroll.getWebVerticalScrollBar();
            autoScroll = e.getWheelRotation() != -1 && scrollBar.getMaximum() - scrollBar.getValue() <= scrollBar.getHeight();
        });

        runningLogPane.add(textPaneScroll);

        // 快捷菜單
        final WebPanel menuPane = new WebPanel(new BorderLayout(5, 5));
        menuPane.setUndecorated(false);
        menuPane.setRound(StyleConstants.largeRound);
        menuPane.setMargin(5);
        menuPane.setShadeWidth(5);

        final WebButton serverConfig = new WebButton("尚未開放"/*"配置參數"*/, e -> new SwingWorker() {
                    @Override
                    protected Object doInBackground() throws Exception {
                        Apple.console.Panel.獎勵 dataManagePanel = 獎勵.getInstance();
                        dataManagePanel.pack();
                        dataManagePanel.setLocationRelativeTo(null);
                        dataManagePanel.setVisible(true);
                        dataManagePanel.setDefaultCloseOperation(獎勵.DISPOSE_ON_CLOSE);
//                showConfigPanel();
                        return null;
                    }
                }.execute());
        serverConfig.setMargin(5, 10, 5, 10);
        serverConfig.setRound(15);

        final WebButton Manage = new WebButton("遊戲管理"/*"配置參數"*/, e -> new SwingWorker() {
                    @Override
                    protected Object doInBackground() throws Exception {
                        DataManagePanel dataManagePanel = DataManagePanel.getInstance();
                        dataManagePanel.pack();
                        dataManagePanel.setLocationRelativeTo(null);
                        dataManagePanel.setVisible(true);
                        dataManagePanel.setDefaultCloseOperation(5);
                        //   dataManagePanel.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                        return null;
                    }
                }.execute());
        Manage.setMargin(5, 10, 5, 10);
        Manage.setRound(15);

        final WebButton DataSearch = new WebButton("數據搜索", e -> {
            Apple.console.Panel.搜索 dataManagePanel = 搜索.getInstance();
            dataManagePanel.pack();
            dataManagePanel.setLocationRelativeTo(null);
            dataManagePanel.setVisible(true);
            dataManagePanel.setDefaultCloseOperation(搜索.DISPOSE_ON_CLOSE);
        });
        DataSearch.setMargin(5, 10, 5, 10);

        final WebButton Reload = new WebButton("重新加載", e -> {
            Apple.console.Panel.加載 dataManagePanel = 加載.getInstance();
            dataManagePanel.pack();
            dataManagePanel.setLocationRelativeTo(null);
            dataManagePanel.setVisible(true);
            dataManagePanel.setDefaultCloseOperation(加載.DISPOSE_ON_CLOSE);
        });
        Reload.setMargin(5, 10, 5, 10);

        final WebButton NotOpen = new WebButton("代碼探索器", e -> {
            MapleLifeFactory.loadQuestCounts();
            Apple.console.Panel.SearchGeneratorUI dataManagePanel = SearchGeneratorUI.getInstance();
            dataManagePanel.pack();
            dataManagePanel.setLocationRelativeTo(null);
            dataManagePanel.setVisible(true);
            dataManagePanel.setDefaultCloseOperation(SearchGeneratorUI.DISPOSE_ON_CLOSE);
        });
        NotOpen.setMargin(5, 10, 5, 10);

        final WebButton delUserDataManage = new WebButton("數據庫管理", e -> new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                ((WebButton) e.getSource()).setEnabled(false);
                DataBaseManagePanel dataBaseManagePanel = DataBaseManagePanel.getInstance();
                dataBaseManagePanel.pack();
                dataBaseManagePanel.setLocationRelativeTo(instance);
                dataBaseManagePanel.setVisible(true);
                ((WebButton) e.getSource()).setEnabled(true);
                return null;
            }
        }.execute());
        delUserDataManage.setMargin(5, 10, 5, 10);

        final WebButton OnOff = new WebButton("開關功能", e -> {
            Apple.console.Panel.開關 dataManagePanel = 開關.getInstance();
            dataManagePanel.pack();
            dataManagePanel.setLocationRelativeTo(null);
            dataManagePanel.setVisible(true);
            dataManagePanel.setDefaultCloseOperation(開關.DISPOSE_ON_CLOSE);
        });
        OnOff.setMargin(5, 10, 5, 10);

        final ImageIcon start = loadIcon("start.png");
        final ImageIcon stop = loadIcon("stop.png");
        final WebButton startServer = new WebButton("啟動服務端", start);
        final WebProgressOverlay progressOverlay = new WebProgressOverlay();
        progressOverlay.setConsumeEvents(false);
        startServer.setMargin(5, 10, 5, 10);
        startServer.setRound(15);
        progressOverlay.setComponent(startServer);
        progressOverlay.setOpaque(false);
        startServer.addActionListener(e -> {
            /*   if (!testDatabaseConnection()) {
                return;
            }*/
            boolean showLoad = !progressOverlay.isShowLoad();
            if (showLoad) {
                startRunTime();
                start_thread.start();
            } else {
                final String input = WebOptionPane.showInputDialog(instance, "關閉倒計時(分鐘)：", 0);
                if (input == null) {
                    return;
                }
                startServer.setEnabled(false);
                final int time = Integer.valueOf(Settings.isNumber(input) ? input : "0");
                final ShutdownServer si = ShutdownServer.getInstance();
                if (si == null) {
                    WebOptionPane.showMessageDialog(instance, "停止服務端發生錯誤，服務端似乎沒有啟動？\r\n\r\n請關閉服務端，確保進程內的java.exe和javaw.exe進程完全關閉，再啟動服務端試試吧~", "錯誤", JOptionPane.ERROR_MESSAGE);
                    System.exit(0);
                }
                si.setTime(time);
                Thread stop_thread = new Thread(() -> shutdownServer = Settings.GuiTimer.getInstance().register(() -> {
                    ShutdownServer.getInstance().shutdown();
                    if (si.getTime() > 0) {
                        System.out.println("距離服務端完全關閉還剩 " + si.getTime() + " 分鐘，已通知玩家，請耐心等待...");
                    } else {
                        shutdownServer.cancel(false);
                        startRunTime.cancel(false);
                    }
                    si.setTime(si.getTime() - 1);
                }, 60000));
                stop_thread.start();
                try {
                    stop_thread.join();
                } catch (InterruptedException e1) {
                    System.out.println("停止服務端失敗" + e);
                }
            }

            progressOverlay.setShowLoad(showLoad);
            startServer.setText(showLoad ? "停止服務端" : "啟動服務端");
            startServer.setIcon(showLoad ? stop : start);
        });

        menuPane.add(new GroupPanel(false,
                serverConfig,
                Manage,//管理
                new WebSeparator(false, true).setMargin(4, 0, 4, 0),
                DataSearch,
                Reload,
                NotOpen,
                delUserDataManage,
                OnOff,
                new WebSeparator(false, true).setMargin(4, 0, 0, 0)),
                BorderLayout.NORTH);
        menuPane.add(new GroupPanel(false, new WebSeparator(false, true).setMargin(4, 0, 4, 0), progressOverlay), BorderLayout.SOUTH);

        contentPane.add(runningLogPane, BorderLayout.CENTER);
        contentPane.add(menuPane, BorderLayout.EAST);

        // 設置默認焦點
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                startServer.requestFocus();
            }
        });

        return contentPane;
    }

    /* public synchronized ConfigPanel showConfigPanel() {
        ConfigPanel serverConfigFrame = new ConfigPanel(instance);
        serverConfigFrame.pack();
        serverConfigFrame.setLocationRelativeTo(instance);
        serverConfigFrame.setVisible(true);
        return serverConfigFrame;
    }*/
    private void showBossPanel() {
        new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                /* BossManagePanel bossManagePanel = new BossManagePanel(instance);
                bossManagePanel.pack();
                bossManagePanel.setLocationRelativeTo(instance);
                bossManagePanel.setVisible(true);*/
                return null;
            }
        }.execute();
    }

    private void NotOpen() {
        new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                System.err.println("寫入");
                Gachapon.WriteItem();
                System.err.println("完畢.");
                return null;
            }
        }
                .execute();
    }

    public static String 字串(String str) {
        String n = "";
        int count = 0;
        char[] chs = str.toCharArray();
        for (int i = 0; i < chs.length; i++) {
            count += (chs[i] > 0xff) ? 2 : 1;
        }
        for (int s = count; s < 20; s++) {
            int Rand = Randomizer.nextInt(26);
            final String[] Alphabet = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
            n += Alphabet[Rand];
        }
        return str + n;
    }

    public static ResultSet getAllOnline() {
        try {
            Connection con = (Connection) DatabaseConnection.getConnection();
            PreparedStatement ps = (PreparedStatement) con.prepareStatement("SELECT name FROM accounts");
            return ps.executeQuery();

        } catch (Exception ex) {
            System.err.println(ex);
        }
        return null;
    }

    private Component createOnlineStatus() {
        final GroupPanel groupPanel = new GroupPanel(5);
        groupPanel.setPainter(new TitledBorderPainter("在線人數"));
        groupPanel.setMargin(5);
        labels = new WebHotkeyLabel[CHANNEL_PORTS + 1];
        for (int i = 0; i < CHANNEL_PORTS; i++) {
            final WebHotkeyLabel label = new WebHotkeyLabel("頻道" + (i + 1) + " : 0");
            labels[i] = label;
            groupPanel.add(label);
        }
        return groupPanel;
    }

    public void setupOnlineStatus(final int channel) {
        final ChannelServer channelServer = ChannelServer.getInstance(channel);
        if (channelServer == null) {
            return;
        }
        final PlayerStorage.PlayerObservable playerObservable = channelServer.getPlayerStorage().getPlayerObservable();
        Observer observer = (o, arg) -> labels[channel - 1].setText("频道" + channel + " : " + playerObservable.getCount());
        playerObservable.addObserver(observer);
    }

    private Component createBroadCastMsg() {
        final WebPanel contentPanel = new WebPanel(new BorderLayout(5, 5));
        contentPanel.setPainter(new TitledBorderPainter("系統公告"));
        contentPanel.setMargin(5);

        String[] items = {"頂部黃色公告", "信息提示框", "藍色公告", "紅色公告", "白色公告", "玩家說話"};

        final WebComboBox comboBox = new WebComboBox(items);
        contentPanel.add(comboBox, BorderLayout.WEST);

        final WebTextField textField = new WebTextField(LOGIN_SERVERMESSAGE);
        textField.setInputPrompt("點擊此處輸入您要發佈的消息內容...");
        textField.setHideInputPromptOnFocus(false);
        contentPanel.add(textField, BorderLayout.CENTER);

        comboBox.addItemListener(e -> {
            if (e.getItem().equals("頂部黃色公告")) {
                textField.setText(LOGIN_SERVERMESSAGE);
            } else {
                textField.setText("");
            }
        });

        final WebButton send = new WebButton("發送消息", e -> new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                if (!startFinish) {
                    WebOptionPane.showMessageDialog(instance, "服務端暫未啟動，無法使用該功能");
                    return null;
                }
                String msg = textField.getText();
                byte[] packet = new byte[0];
                switch (comboBox.getSelectedIndex()) {
                    case 0:
                        LOGIN_SERVERMESSAGE = msg;
                        //Config.setProperty("login.server.message", msg);
                        //packet = MaplePacketCreator.serverMessage(msg);
                        for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                            cserv.setServerMessage(msg);
                        }
                        break;
                    case 1:
                        packet = MaplePacketCreator.serverNotice(1, msg);
                        break;
                    case 2:
                        packet = MaplePacketCreator.serverNotice(6, msg);
                        break;
                    case 3:
                        packet = MaplePacketCreator.serverNotice(5, msg);
                        break;
                    case 4:
                        //        packet = MaplePacketCreator.spouseMessage(0x0A, msg);
                        //packet = MaplePacketCreator.yellowChat(msg);
                        break;
                    case 5:
                        for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                            for (MapleCharacter victim : cserv.getPlayerStorage().getAllCharacters()) {
                                victim.getMap().broadcastMessage(MaplePacketCreator.getChatText(victim.getId(), msg, victim.isGM(), 1));
                            }
                        }
                        break;
                }
                if (comboBox.getSelectedIndex() != 0 || comboBox.getSelectedIndex() != 5) {
                    World.Broadcast.broadcastMessage(packet);
                }
                WebOptionPane.showMessageDialog(instance, "發送完成");
                return null;
            }
        }.execute());
        contentPanel.add(send, BorderLayout.EAST);

        return contentPanel;
    }

    private Component createStatusBar() {
        final WebPanel contentPane = new WebPanel(new BorderLayout(5, 5));
        final WebStatusBar statusBar = new WebStatusBar();

        runningTimelabel = new WebHotkeyLabel("運行時長: 00天00:00:00");
        statusBar.addToEnd(runningTimelabel);
        statusBar.addSeparatorToEnd();

        WebMemoryBar memoryBar = new WebMemoryBar();
        memoryBar.setShowMaximumMemory(false);
        memoryBar.setPreferredWidth(memoryBar.getPreferredSize().width + 20);
        statusBar.addToEnd(memoryBar);

        contentPane.add(createBroadCastMsg(), BorderLayout.NORTH);
        contentPane.add(createOnlineStatus(), BorderLayout.CENTER);
        contentPane.add(statusBar, BorderLayout.SOUTH);

        return contentPane;
    }

    private void startRunTime() {
        starttime = System.currentTimeMillis();
        startRunTime = Settings.GuiTimer.getInstance().register(new Runnable() {
            @Override
            public void run() {
                runningTimelabel.setText(formatDuring(System.currentTimeMillis() - starttime));
                Weather.getInstance();
                Settings.Reset.getInstance_Time();
            }

            private String formatDuring(long mss) {
                long days = mss / (1000 * 60 * 60 * 24);
                long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
                long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
                long seconds = (mss % (1000 * 60)) / 1000;
                return "運行時長: " + (days / 10 == 0 ? "0" : "") + days + "天" + (hours / 10 == 0 ? "0" : "") + hours + ":" + (minutes / 10 == 0 ? "0" : "") + minutes + ":"
                        + (seconds / 10 == 0 ? "0" : "") + seconds;
            }
        }, 1000);
    }

    private static class StartThread implements Runnable {

        private static Thread Copyserver = null;

        @Override
        public void run() {

            /*     if (!InitializeServer.Initial()) {
                    System.out.println("服務端初始化失敗。");
                    return;
                }*/

 /*              System.out.println("正在啟動 - 時鐘管理器");
                Timer.WorldTimer.getInstance().start();
                Timer.EtcTimer.getInstance().start();
                Timer.MapTimer.getInstance().start();
                Timer.CloneTimer.getInstance().start();
                Timer.EventTimer.getInstance().start();
                Timer.BuffTimer.getInstance().start();
                Timer.PingTimer.getInstance().start();
                Timer.PlayerTimer.getInstance().start();

                System.out.println("正在啟動 - 好友、組隊、家族、聯盟、角色管理");
                World.init();
                MapleGuildRanking.getInstance().load();
                MapleGuild.loadAll();
                MapleFamily.loadAll();
                MapleQuest.initQuests();

                System.out.println("正在加載 - 道具信息");
                MapleItemInformationProvider.getInstance().runEtc();

                System.out.println("正在加載 - 初始角色信息");
                LoginInformationProvider.getInstance();

                System.out.println("正在加載 - 隨機獎勵");
                RandomRewards.load();

                System.out.println("正在加載 - 角色卡系統");
                CharacterCardFactory.getInstance().initialize();

                System.out.println("正在加載 - 副本競速排行榜");
                SpeedRunner.loadSpeedRuns();

                System.out.println("正在加載 - 拍賣行系統");
                MTSStorage.load();

                LoginServer.run_startup_configurations();
                ChannelServer.startChannel_Main();
                CashShopServer.run_startup_configurations();
                AuctionServer.run_startup_configurations();
                ChatServer.run_startup_configurations();

                System.out.println("正在加載 - 其他信息");
                Timer.CheatTimer.getInstance().register(AutobanManager.getInstance(), 60000);
                WorldRespawnService.getInstance();
                ShutdownServer.registerMBean();
                LoginServer.setOn();
                PredictCardFactory.getInstance().initialize();
                MapleInventoryIdentifier.getInstance();
                PlayerNPC.loadAll();
                MapleDojoRanking.getInstance().load(false);
                RankingWorker.start();
                PlayMSEvent.start();
                MessengerRankingWorker.getInstance();
                MapleSignin.getInstance().load();
                RankingTop.getInstance();
                DataBaseManagePanel.getInstance().autoBackup();
                OpcodeConfig.load();
                MapleCarnivalFactory.getInstance();
                ItemConstants.TapJoyReward.init();
                Runtime.getRuntime().addShutdownHook(new Thread(new Shutdown()));*/
            Settings.Reset.getInstance();//重置
            //ProductID.Certification(); //檢查證認
            PlayerTimer.getInstance().start();//角色計時器

            System.out.println("[惡魔谷準備啟動服務端]");
            /*if (ProductID.載入功能) {
                System.out.println("[!!! 寫入裝備分析 !!!]");
                ProductID.IitemLoad();//數據寫入
            }*/
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
//             LoginInformationProvider.getInstance();
//             MapleQuest.initQuests();
//             MapleLifeFactory.loadQuestCounts();
//             ItemMakerFactory.getInstance();
            MapleItemInformationProvider.getInstance().load();
            System.out.println("[載入髮型臉部物件]");
            MapleItemInformationProvider.getInstance().loadStyles(false);
//             RandomRewards.getInstance();
//             SkillFactory.getSkill(99999999);
//             MapleOxQuizFactory.getInstance().initialize();
//             MapleCarnivalFactory.getInstance();
//             MapleGuildRanking.getInstance().getRank();
//             MapleFamilyBuff.getBuffEntry();
//             RankingWorker.getInstance().run();
//             MTSStorage.load();
//             CashItemFactory.getInstance().initialize();
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
            //System.out.println("加載完成 :::");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.gc();
            PingTimer.getInstance().register(System::gc, 1800000);
            //加載
            LoginInformationProvider.getInstance();
            MapleQuest.initQuests();
            MapleLifeFactory.loadQuestCounts();
            RandomRewards.getInstance();
            SkillFactory.getSkill(99999999);
            MapleOxQuizFactory.getInstance().initialize();
            MapleCarnivalFactory.getInstance();
            MapleGuildRanking.getInstance().getRank();
            MapleFamilyBuff.getBuffEntry();
            RankingWorker.getInstance().run();
            MTSStorage.load();
            CashItemFactory.getInstance().initialize();
              System.out.println("[報告 :　惡魔啟動完畢]");
//                   WorldTimer.getInstance().register(CloseSQLConnections,  15 * 1000);// 定時清理MySql連接數
            /* 唯一道具 */
 /*Copyserver = new Thread() {
                @Override
                public void run() {
                    WorldTimer.getInstance().register(new MapleEquipIdOnly.run(), 12 * 60 * 60 * 1000);
                }
            };
            Copyserver.start();*/
            //System.err.println("服務端啟動完成！");
            startFinish = true;

        }
    }

    static class NewOutputStram extends OutputStream {

        private final byte type;

        public NewOutputStram(byte type) {
            this.type = type;
        }

        @Override
        public void write(int b) throws IOException {

        }

        @Override
        public void write(final byte[] b, final int off, final int len) throws IOException {
//            super.write(b, off, len);
            final SimpleAttributeSet set = new SimpleAttributeSet();
            switch (type) {
                case 0:
                    javax.swing.text.StyleConstants.setForeground(set, Color.BLACK);
                    break;
                case 1:
                    javax.swing.text.StyleConstants.setForeground(set, Color.RED);
                    break;
                case 2:
                    javax.swing.text.StyleConstants.setForeground(set, Color.BLUE);
                    break;
            }

            try {
                WebTextPane textPane = Start.getInstance().textPane;

                textPane.getDocument().insertString(textPane.getDocument().getLength(), new String(b, off, len), set);
                if (Start.getInstance().autoScroll) {
                    textPane.setCaretPosition(textPane.getDocument().getLength());
                }
            } catch (BadLocationException e) {
                //     log.fatal("控制台輸出失敗", e);
            }
        }
    }

    private static class Shutdown implements Runnable {

        @Override
        public void run() {
            ShutdownServer.getInstance().run();
        }
    }

    public class StartFrame extends WebFrame {

        private final WebLabel titleText;
        private final WebProgressBar progressBar;
        private final ImageIcon background;

        {
            background = loadIcon("LOGO.png");
        }

        StartFrame() {
            super();

            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            setPreferredSize(new Dimension(background.getIconWidth(), background.getIconHeight()));

            BackgroundPanel backgroundPanel = new BackgroundPanel();

            backgroundPanel.setLayout(new BorderLayout());

            titleText = new WebLabel("正在啟動……", SwingConstants.CENTER).setMargin(0, 0, 3, 0);
            titleText.setForeground(Color.WHITE);
            titleText.setFont(new FontUIResource("微軟雅黑", 0, 12));
            progressBar = new WebProgressBar(0, 100);
            progressBar.setPreferredHeight(5);
            progressBar.setStringPainted(false);
            progressBar.setRound(0);
            progressBar.setValue(0);
            progressBar.setShadeWidth(0);
            progressBar.setBorderPainted(false);
            progressBar.setProgressBottomColor(Color.CYAN);
            progressBar.setProgressTopColor(Color.BLACK);

            backgroundPanel.add(new GroupPanel(false, titleText, progressBar), BorderLayout.SOUTH);

            add(backgroundPanel);
        }

        public void setText(String text) {
            this.titleText.setText(text);
        }

        public void setProgress(int value) {
            this.progressBar.setValue(value);
        }

        private class BackgroundPanel extends WebPanel {

            @Override
            public void paintComponent(Graphics g) {
                g.drawImage(background.getImage(), 0, 0, null);
            }
        }
    }

    public class ProgressBarObservable extends Observable {

        private String text;
        private int progress;

        public int getProgress() {
            return progress;
        }

        public void setProgress(Pair<String, Integer> value) {
            this.text = value.getLeft();
            this.progress = value.getRight();
            setChanged();
            notifyObservers(value);
        }
    }

    private class ProgressBarObserver implements Observer {

        ProgressBarObserver(ProgressBarObservable progressBarObservable) {
            progressBarObservable.addObserver(this);
        }

        public void deleteObserver(ProgressBarObservable progressBarObservable) {
            progressBarObservable.deleteObserver(this);
        }

        @Override
        public void update(Observable o, Object arg) {
            if (arg instanceof Pair) {
                Pair pair = (Pair) arg;
                progress.setText((String) pair.getLeft());
                progress.setProgress((Integer) pair.getRight());
            }
        }
    }
}

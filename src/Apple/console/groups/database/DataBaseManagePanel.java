package Apple.console.groups.database;

import Apple.console.Settings;
import com.alee.extended.filechooser.WebDirectoryChooser;
import com.alee.extended.painter.TitledBorderPainter;
import com.alee.extended.panel.GroupPanel;
import com.alee.extended.window.WebProgressDialog;
import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import com.alee.laf.list.WebList;
import com.alee.laf.list.WebListModel;
import com.alee.laf.optionpane.WebOptionPane;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.rootpane.WebFrame;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.separator.WebSeparator;
import com.alee.laf.text.WebTextField;
import com.alee.utils.ThreadUtils;
import com.alee.utils.swing.DialogOptions;
//import configs.Config;
//import configs.ServerConfig;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import server.console.Start;
import Backup.GUI.Start;
import static database.DatabaseConnection.SQL_DATABASE;
import server.Randomizer;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.concurrent.ScheduledFuture;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import server.ServerProperties;
import tools.FileoutputUtil;

public class DataBaseManagePanel extends WebFrame {

    //private static final Logger log = LogManager.getLogger();
    public static DataBaseManagePanel instance;
    private final WebTextField setupPath = new WebTextField(20), backupPath = new WebTextField((20));
    private final WebList backupList = new WebList(new WebListModel<>());
    private transient ScheduledFuture<?> autoback;

    // 數據庫設置
    //  @Property(key = "db.ip", defaultValue = "localhost")
    public static String DB_IP = "localhost";

    //@Property(key = "db.port", defaultValue = "3306")
    public static String DB_PORT = "3306";

    // @Property(key = "db.name", defaultValue = "qhms")
    public static String DB_NAME = ServerProperties.getProperty("tms.Name", "tms120");

    // @Property(key = "db.user", defaultValue = "root")
    public static String DB_USER = "root";

    //  @Property(key = "db.password", defaultValue = "root")
    public static String DB_PASSWORD = "";

    //@Property(key = "db.setuppath", defaultValue = "D:\\MySQL\\MySQL Server 5.6")
    public static String DB_SETUPPATH = "C:\\wamp\\bin\\mysql\\mysql5.6.12";

    //@Property(key = "db.backuppath", defaultValue = "D:\\數據庫備份")
    public static String DB_BACKUPPATH = "D:\\數據庫備份";

    //@Property(key = "db.autobackuptime", defaultValue = "120")
    public static int DB_AUTOBACKUPTIME = 120;

    DataBaseManagePanel() {
        super("數據庫管理");
        setIconImage(Start.getMainIcon().getImage());

        setupPath.setText(DB_SETUPPATH);
        backupPath.setText(DB_BACKUPPATH);
        setupPath.setEditable(false);
        backupPath.setEditable(false);
        updateSQLList();

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        add(new WebPanel(new BorderLayout(5, 5)) {
            {
                setPreferredSize(400, 500);
                setResizable(false);
                setMargin(2, 5, 2, 5);

                // 設置mysql安裝路徑、備份路徑
                add(new WebPanel() {
                    {
                        setMargin(5);
                        setPainter(new TitledBorderPainter("MySQL設置"));
                        final WebTextField time = new WebTextField(String.valueOf(DB_AUTOBACKUPTIME), 5);

                        add(new GroupPanel(false,
                                new GroupPanel(
                                        new WebLabel("MySQL安裝路徑："),
                                        setupPath,
                                        new WebButton("...") {
                                    {
                                        addActionListener(new ActionListener() {
                                            private WebDirectoryChooser directoryChooser = null;

                                            @Override
                                            public void actionPerformed(ActionEvent e) {
                                                if (directoryChooser == null) {
                                                    directoryChooser = new WebDirectoryChooser(instance);
                                                }
                                                directoryChooser.setVisible(true);
                                                if (directoryChooser.getResult() == DialogOptions.OK_OPTION) {
                                                    final File file = directoryChooser.getSelectedDirectory();
                                                    DB_SETUPPATH = file.getAbsolutePath();
                                                    //Config.setProperty("db.setuppath", DB_SETUPPATH.replace("\\", "\\\\")); //立即更新作用
                                                    setupPath.setText(DB_SETUPPATH);
                                                }
                                            }
                                        });
                                    }
                                }),
                                new GroupPanel(
                                        new WebLabel("MySQL備份路徑："),
                                        backupPath,
                                        new WebButton("...") {
                                    {
                                        addActionListener(new ActionListener() {
                                            private WebDirectoryChooser directoryChooser = null;

                                            @Override
                                            public void actionPerformed(ActionEvent e) {
                                                if (directoryChooser == null) {
                                                    directoryChooser = new WebDirectoryChooser(instance);
                                                }
                                                directoryChooser.setVisible(true);
                                                if (directoryChooser.getResult() == DialogOptions.OK_OPTION) {
                                                    final File file = directoryChooser.getSelectedDirectory();
                                                    DB_BACKUPPATH = file.getAbsolutePath();
                                                    //Config.setProperty("db.backuppath", DB_BACKUPPATH.replace("\\", "\\\\")); //立即更新作用
                                                    backupPath.setText(DB_BACKUPPATH);
                                                }
                                            }
                                        });
                                    }
                                }),
                                new GroupPanel(new WebLabel("MySQL自動備份間隔時間（分鐘）："), time, new WebButton("刷新") {
                                    {
                                        addActionListener(new ActionListener() {
                                            @Override
                                            public void actionPerformed(ActionEvent e) {
                                                DB_AUTOBACKUPTIME = Integer.parseInt(time.getText());
                                                //Config.setProperty("db.autobackuptime", time.getText());//立即更新作用
                                                autoBackup();
                                                WebOptionPane.showMessageDialog(instance, "刷新成功");
                                            }
                                        });
                                    }
                                })));
                    }
                }, BorderLayout.NORTH);

                // 備份歷史列表
                final String[] data = {"2016年9月3日0:37:25", "2016年9月3日0:37:32", "2016年9月3日0:37:35"};
                add(new WebPanel() {
                    {
                        setPainter(new TitledBorderPainter("歷史備份列表"));

                        backupList.setEditable(false);
                        add(new WebScrollPane(new WebPanel(backupList)));

                        // 操作按鈕
                        add(new GroupPanel(false,
                                new WebButton("新建備份") {
                            {
                                addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        if (WebOptionPane.showConfirmDialog(instance, "備份數據庫大概需要幾分鐘的時間，全程在後台運行，期間不會影響您的其他操作，完成後將會自動通知您，是否繼續？", "數據庫備份", WebOptionPane.YES_NO_OPTION) == WebOptionPane.NO_OPTION) {
                                            return;
                                        }
                                        final WebButton webButton = ((WebButton) e.getSource());
                                        final String oldtext = webButton.getText();
                                        webButton.setText("正在備份");
                                        webButton.setEnabled(false);
                                        new SwingWorker() {
                                            @Override
                                            protected Object doInBackground() throws Exception {
                                                new BackupDB().run();
                                                webButton.setText(oldtext);
                                                webButton.setEnabled(true);
                                                WebOptionPane.showMessageDialog(instance, "數據庫備份完成！");
                                                return null;
                                            }
                                        }.execute();
                                    }
                                });
                            }
                        },
                                new WebButton("還原備份") {
                            {
                                addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        if (backupList.getSelectedIndex() == -1) {
                                            WebOptionPane.showMessageDialog(instance, "請先在左側列表選擇還原點");
                                            return;
                                        }
                                        final WebButton webButton = ((WebButton) e.getSource());
                                        final String path = (String) backupList.getSelectedValue();
                                        if (WebOptionPane.showConfirmDialog(instance, "確定還原到 " + path + " " + " 時的備份?", "還原備份", WebOptionPane.YES_NO_OPTION) == WebOptionPane.NO_OPTION) {
                                            return;
                                        }
                                        new SwingWorker() {
                                            @Override
                                            protected Object doInBackground() throws Exception {
                                                String oldtext = webButton.getText();
                                                webButton.setText("正在還原");
                                                webButton.setEnabled(false);
                                                recoverDB(path);
                                                webButton.setText(oldtext);
                                                webButton.setEnabled(true);
                                                return null;
                                            }
                                        }.execute();
                                    }
                                });
                            }
                        },
                                new WebButton("刪除備份") {
                            {
                                addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        int[] indexs = backupList.getSelectedIndices();
                                        if (indexs.length == 0) {
                                            WebOptionPane.showMessageDialog(instance, "請先在左側列表選擇要刪除的備份");
                                            return;
                                        }
                                        if (WebOptionPane.showConfirmDialog(instance, "確定要刪除已選的 " + indexs.length + " 個備份嗎？", "刪除備份", WebOptionPane.OK_CANCEL_OPTION) == WebOptionPane.OK_OPTION) {
                                            int delete = 0;
                                            for (int index : indexs) {
                                                File file = new File(DB_BACKUPPATH + "/" + backupList.getWebModel().get(index - delete) + ".sql.gz");
                                                if (file.exists() && file.isFile()) {
                                                    file.delete();
                                                } else {
                                                    WebOptionPane.showMessageDialog(instance, "刪除失敗，文件不存在或已刪除 " + file.exists() + " " + file.isFile());
                                                }
                                                backupList.getWebModel().remove(index - delete);
                                                delete++;
                                            }
                                        }
                                    }
                                });
                            }
                        },
                                new WebSeparator().setMargin(5),
                                new WebButton("一鍵刪檔") {
                            {
                                addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        new SwingWorker() {
                                            @Override
                                            protected Object doInBackground() throws Exception {
                                                int verifcode;
                                                final String defText = "請輸入驗證碼";
                                                Object obj = "";
                                                while (obj != null && !defText.equals(obj)) {
                                                    verifcode = Randomizer.rand(1000, 9999);
                                                    obj = WebOptionPane.showInputDialog(instance, "此操作不可逆，操作前盡量備份數據庫，以免悔恨終生。\r\n如果已經過慎重考慮，那麼請輸入驗證碼：" + verifcode, "清空玩家數據", JOptionPane.WARNING_MESSAGE, null, null, defText);
                                                    if (obj instanceof String && Settings.isNumber(String.valueOf(obj))) {
                                                        int resultcode = Integer.valueOf(String.valueOf(obj));
                                                        if (resultcode != verifcode) {
                                                            WebOptionPane.showMessageDialog(instance, "驗證碼錯誤，請重新輸入");
                                                        } else {
                                                            // 清空數據
                                                            Thread thread = new Thread(DeleteUserData::run);
                                                            thread.start();
                                                            break;
                                                        }
                                                    }
                                                }
                                                return null;
                                            }
                                        }.execute();
                                    }
                                });
                            }
                        }
                        ), BorderLayout.EAST);
                    }
                }, BorderLayout.CENTER);
            }
        });
    }

    public static DataBaseManagePanel getInstance() {
        if (instance == null) {
            instance = new DataBaseManagePanel();
        }
        return instance;
    }

    private void updateSQLList() {
        File file = new File(DB_BACKUPPATH);
        if (!file.exists()) {
            return;
        }
        WebListModel webModel = backupList.getWebModel();
        webModel.clear();
        for (String s : file.list()) {
            if (s.endsWith(".sql.gz")) {
                webModel.add(s.substring(0, s.indexOf(".")));
            }
        }
    }

    public void recoverDB(String path) {
        String command = "\"" + DB_SETUPPATH + "\\bin\\mysql.exe\" -u" + DB_USER + (DB_PASSWORD == null ? "" : " -p" + DB_PASSWORD) + " --default-character-set=utf8";
        WebProgressDialog webProgressDialog = new WebProgressDialog("數據庫還原備份");
        webProgressDialog.setPreferredProgressWidth(300);
        webProgressDialog.setVisible(true);
        try {
            Process process = Runtime.getRuntime().exec(command);
            try (OutputStream outputStream = process.getOutputStream()) {
                try (FileInputStream fileInputStream = new FileInputStream(DB_BACKUPPATH + "/" + path + ".sql.gz")) {
                    final int max = fileInputStream.available();
                    webProgressDialog.setText("正在還原備份到: " + path + "...");
                    try (GZIPInputStream gzipInputStream = new GZIPInputStream(fileInputStream)) {
                        byte[] buf = new byte[1024];
                        int len;
                        while ((len = gzipInputStream.read(buf)) > 0) {
                            outputStream.write(buf, 0, len);
                            webProgressDialog.setProgress((int) (100 - ((double) fileInputStream.available() / max) * 100));
                        }
                    }
                }
            } catch (IOException e) {
                webProgressDialog.setVisible(false);
                try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                    StringBuilder stringBuilder = new StringBuilder();
                    String buff;
                    while ((buff = bufferedReader.readLine()) != null) {
                        stringBuilder.append(buff).append("\r\n");
                    }
                    FileoutputUtil.log(FileoutputUtil.LogManager, stringBuilder.toString());
                    if (stringBuilder.indexOf("using password: YES") != -1) {
                        WebOptionPane.showMessageDialog(instance, "數據庫連接失敗，請在配置參數里確認數據庫IP、端口、賬號、密碼、庫名 是否填寫正確。");
                        instance.setVisible(false);
                        //Start.getInstance().showConfigPanel();
                    }
                }
                FileoutputUtil.outputFileError(FileoutputUtil.LogManager, e);
            }
            webProgressDialog.setProgressText("備份恢復完成...");
            ThreadUtils.sleepSafely(1000);
            webProgressDialog.setVisible(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void autoBackup() {
        if (autoback != null) {
            autoback.cancel(false);
        }
        autoback = Settings.GuiTimer.getInstance().register(new BackupDB(), DB_AUTOBACKUPTIME  * 1000, DB_AUTOBACKUPTIME  * 1000);
    }

    public class BackupDB implements Runnable {

        @Override
        public void run() {
            String command = "\"" + DB_SETUPPATH + "\\bin\\mysqldump.exe\" --no-defaults -u" + DB_USER + (DB_PASSWORD == "" ? "" : " -p" + DB_PASSWORD) + " --default-character-set=utf8 --database \"" + DB_NAME + "\"";
            System.err.println(command);
            try {
                Process process = Runtime.getRuntime().exec(command);
                File file = new File(DB_BACKUPPATH + "/" + Settings.getNowTime() + ".sql.gz");
                file.getParentFile().mkdirs();
                try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
                    try (GZIPOutputStream gzipOutputStream = new GZIPOutputStream(fileOutputStream)) {
                        try (InputStream inputStream = process.getInputStream()) {
                            byte[] buf = new byte[1024];
                            int len;
                            while ((len = inputStream.read(buf)) != -1) {
                                gzipOutputStream.write(buf, 0, len);
                            }
                            gzipOutputStream.finish();
                            gzipOutputStream.flush();
                            updateSQLList();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}

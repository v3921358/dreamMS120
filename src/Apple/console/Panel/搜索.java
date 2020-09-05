/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Apple.console.Panel;

import static backup.GUI.Settings.TableData.EquipData;
import static backup.GUI.Settings.TableData.Gachapon;
import static backup.GUI.Settings.TableData.ItemData;
import static backup.GUI.Settings.TableData.FameHair;
import java.awt.Font;
import java.sql.ResultSet;
import static backup.GUI.Settings.TableData.getAllOnline;
import java.awt.Color;
import javax.swing.table.DefaultTableModel;
import server.MapleItemInformationProvider;
import Apple.console.groups.setting.Gachapon;
import Apple.console.groups.setting.StringUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.SwingWorker;

/**
 *
 * @author MSI
 */
public class 搜索 extends javax.swing.JFrame {

    public final void TabbedPane() {
        jTabbedPane1.setFont(new Font("", Font.PLAIN, 12));
        //玩家裝備
        jTabbedPane1.setTitleAt(0, "玩家物品");
        jTabbedPane2.setTitleAt(0, "身上");
        jTabbedPane2.setTitleAt(1, "裝備");
        jTabbedPane2.setTitleAt(2, "消耗");
        jTabbedPane2.setTitleAt(3, "裝飾");
        jTabbedPane2.setTitleAt(4, "其他");
        jTabbedPane2.setTitleAt(5, "特殊");
        //轉蛋機
        jTabbedPane1.setTitleAt(1, "轉蛋物品");
        jTabbedPane3.setTitleAt(0, "潮流");
        jTabbedPane3.setTitleAt(1, "劍士");
        jTabbedPane3.setTitleAt(2, "法師");
        jTabbedPane3.setTitleAt(3, "弓手");
        jTabbedPane3.setTitleAt(4, "盜賊");
        jTabbedPane3.setTitleAt(5, "海盜");
        jButton1.setText("添加");
        jButton2.setText("刪除");
        jButton3.setText("修改");
        jCheckBox1.setText("隱藏預設物品");
        //轉蛋機
        jTabbedPane1.setTitleAt(2, "檢查物品");
    }

    public final void Table() {
        Table1();
        Table2();
        Table4();
    }

    public final void Table_load() {
        Table1_load();
        Gachapon_Data(jCheckBox1.isSelected());
    }

    public final void Table1() {//玩家列表
        //Table1
        final Object[][] Table = {{"ID", "Name"}, {30, 100}};
        jTable1.setModel(new DefaultTableModel(null, Table[0]));//Table 標題名稱
        for (int a = 0; a < Table[1].length; a++) {//Table 標題寬度
            jTable1.getColumnModel().getColumn(a).setMaxWidth((int) Table[1][a]);
        }
        Table2();
    }

    public final void Table2() {//屬性物品列表
        //Table2
        final Object[][] Table_1 = {{"名稱", "ID", "屬性", "等級", "物攻", "魔攻", "物防", "魔防"}, {150, 100, 70, 50, 50, 50, 50, 50}};
        final Object[][] Table_2 = {{"名稱", "ID", "數量"}, {150, 100, 35}};
        jTable2.setModel(new DefaultTableModel(null, (jTabbedPane2.getSelectedIndex() < 2 ? Table_1[0] : Table_2[0])));
        for (int a = 0; a < (jTabbedPane2.getSelectedIndex() < 2 ? Table_1[0].length : Table_2[0].length); a++) {
            jTable2.getColumnModel().getColumn(a).setPreferredWidth((int) (jTabbedPane2.getSelectedIndex() < 2 ? Table_1[1][a] : Table_2[1][a]));
        }
    }

    public final void Table4() {//轉蛋列表
        //Table2
        final Object[][] Table = {{"名稱", "ID", "機率", "%"}, {150, 100, 35, 35}};
        jTable4.setModel(new DefaultTableModel(null, Table[0]));
        for (int a = 0; a < Table[0].length; a++) {
            jTable4.getColumnModel().getColumn(a).setPreferredWidth((int) Table[1][a]);
        }
    }

    public final void Table1_load() {
        int 計數 = 1;
        DefaultTableModel tableModel1 = (DefaultTableModel) jTable1.getModel();//添加row
        ResultSet rs_2 = getAllOnline();
        try {
            while (rs_2.next()) {
                tableModel1.addRow(new Object[]{計數, rs_2.getString("name")});
                計數 += 1;
            }
        } catch (Exception ex) {
            System.err.println(ex);
        }
        jTable1.setModel(tableModel1);
    }

    public void Query_Data(String name) {
        setTitle("正在查詢：" + name);
        MapleItemInformationProvider.getInstance().load(); //載入裝備
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        Table2();
        DefaultTableModel tableModel1 = (DefaultTableModel) jTable2.getModel();
        int rows1 = tableModel1.getRowCount();
        ResultSet rs_2;
        switch (jTabbedPane2.getSelectedIndex()) {
            case 0:
                rs_2 = EquipData(name, -1);
                break;
            case 1:
                rs_2 = EquipData(name, 1);
                break;
            default:
                rs_2 = ItemData(name);
                break;
        }
        for (int i = 0; i < rows1; i++) {
            tableModel1.removeRow(0);
        }
        try {
            while (rs_2.next()) {
                int itemid = rs_2.getInt("itemid");
                boolean checking = itemid / 1000000 == 1;
                boolean Exists = ii.itemExists(itemid);
                String Name = ii.getName(itemid);
                if (checking) {
                    Name = Exists ? ii.getName(itemid) : "(null)" + ii.getName(itemid);
                }

                if (!checking && itemid / 1000000 == jTabbedPane2.getSelectedIndex() && itemid / 1000000 != -1 && jTabbedPane2.getSelectedIndex() != 0) {
                    tableModel1.addRow(new Object[]{Name, rs_2.getString("itemid"), rs_2.getString("quantity")
                    });
                } else if (jTabbedPane2.getSelectedIndex() < 2) {
                    tableModel1.addRow(new Object[]{Name, rs_2.getString("itemid"), rs_2.getString("str") + "|" + rs_2.getString("dex") + "|" + rs_2.getString("int") + "|" + rs_2.getString("luk"), rs_2.getString("level"),
                        rs_2.getString("watk"), rs_2.getString("matk"), rs_2.getString("wdef"), rs_2.getString("mdef")
                    });
                }
            }
            if (jTable2.getRowCount() == 0) {
                tableModel1.addRow(new Object[]{"查詢無任何資料"});
                jTable2.setForeground(Color.red);
            } else {
                jTable2.setForeground(Color.BLACK);
            }
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }

    public void Gachapon_Data(boolean hide) {
        //setTitle("正在查詢：" + name);
        int job = jTabbedPane3.getSelectedIndex() * 100;
        DefaultTableModel tableModel1 = (DefaultTableModel) jTable4.getModel();
        int rows1 = tableModel1.getRowCount();
        ResultSet rs_2 = Gachapon(job, hide);
        for (int i = 0; i < rows1; i++) {
            tableModel1.removeRow(0);
        }
        try {
            while (rs_2.next()) {
                tableModel1.addRow(new Object[]{rs_2.getString("comments"), rs_2.getInt("itemid"), rs_2.getInt("chance"), Math.floor((float) rs_2.getInt("chance") / 1000000 * 100)
                });
            }
            if (jTable4.getRowCount() == 0) {
                tableModel1.addRow(new Object[]{"查詢無任何資料"});
                jTable4.setForeground(Color.red);
            } else {
                jTable4.setForeground(Color.BLACK);
            }
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }

    /**
     * Creates new form TableData
     */
    public 搜索() {
        initComponents();
        setTitle("數據查詢");
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        TabbedPane();//TabbedPane 設定
        Table();//Table 設定
        Table_load();//Table 載入
    }

    private static 搜索 instance = null;

    public static 搜索 getInstance() {
        if (instance == null) {
            instance = new 搜索();
        }
        return instance;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        jTabbedPane3 = new javax.swing.JTabbedPane();
        jPanel9 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jTabbedPane4 = new javax.swing.JTabbedPane();
        jPanel14 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        jPanel18 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jCheckBox1 = new javax.swing.JCheckBox();
        jPanel19 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null}
            },
            new String [] {
                "Title 1"
            }
        ));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jTable1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTable1KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTable1KeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jTabbedPane2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jTabbedPane2.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPane2StateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 448, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jTabbedPane2.addTab("tab1", jPanel2);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 448, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jTabbedPane2.addTab("tab2", jPanel3);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 448, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jTabbedPane2.addTab("tab3", jPanel4);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 448, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jTabbedPane2.addTab("tab4", jPanel5);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 448, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jTabbedPane2.addTab("tab5", jPanel6);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 448, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jTabbedPane2.addTab("tab6", jPanel7);

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(jTable2);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane2)
                    .addComponent(jScrollPane2)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 628, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane2))
        );

        jTabbedPane2.getAccessibleContext().setAccessibleDescription("");

        jTabbedPane1.addTab("tab1", jPanel1);

        jTabbedPane3.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPane3StateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 371, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jTabbedPane3.addTab("tab1", jPanel9);

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 371, Short.MAX_VALUE)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jTabbedPane3.addTab("tab2", jPanel10);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 371, Short.MAX_VALUE)
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jTabbedPane3.addTab("tab3", jPanel11);

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 371, Short.MAX_VALUE)
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jTabbedPane3.addTab("tab4", jPanel12);

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 371, Short.MAX_VALUE)
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jTabbedPane3.addTab("tab5", jPanel13);

        jTabbedPane4.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPane4StateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 366, Short.MAX_VALUE)
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jTabbedPane4.addTab("tab1", jPanel14);

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 366, Short.MAX_VALUE)
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jTabbedPane4.addTab("tab2", jPanel15);

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 366, Short.MAX_VALUE)
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jTabbedPane4.addTab("tab3", jPanel16);

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 366, Short.MAX_VALUE)
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jTabbedPane4.addTab("tab4", jPanel17);

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 366, Short.MAX_VALUE)
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jTabbedPane4.addTab("tab5", jPanel18);

        jTabbedPane3.addTab("tab6", jTabbedPane4);

        jTable4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane4.setViewportView(jTable4);

        jButton1.setText("jButton1");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("jButton2");
        jButton2.setToolTipText("");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("jButton3");

        jCheckBox1.setText("jCheckBox1");
        jCheckBox1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jCheckBox1StateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(jTabbedPane3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCheckBox1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton1)
                        .addComponent(jButton2)
                        .addComponent(jButton3)
                        .addComponent(jCheckBox1)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 586, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("tab1", jPanel8);

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTable3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable3MouseClicked(evt);
            }
        });
        jTable3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTable3KeyPressed(evt);
            }
        });
        jScrollPane3.setViewportView(jTable3);

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 608, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("tab3", jPanel19);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
        new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                Query_Data(jTable1.getValueAt(jTable1.getSelectedRow(), 1).toString());
                return null;
            }
        }.execute();
    }//GEN-LAST:event_jTable1MouseClicked

    private void jTabbedPane2StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPane2StateChanged
        // TODO add your handling code here:
        new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                if (jTable1.getSelectedRow() != -1) {
                    Query_Data(jTable1.getValueAt(jTable1.getSelectedRow(), 1).toString());
                }
                return null;
            }
        }.execute();
    }//GEN-LAST:event_jTabbedPane2StateChanged

    private void jTable1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable1KeyPressed
        // TODO add your handling code here:

    }//GEN-LAST:event_jTable1KeyPressed

    private void jTable1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable1KeyReleased
        // TODO add your handling code here:
        new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                if (jTable1.getSelectedRow() != -1) {
                    Query_Data(jTable1.getValueAt(jTable1.getSelectedRow(), 1).toString());
                }
                return null;
            }
        }.execute();
    }//GEN-LAST:event_jTable1KeyReleased

    private void jTabbedPane3StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPane3StateChanged
        // TODO add your handling code here:
        Gachapon_Data(jCheckBox1.isSelected());
    }//GEN-LAST:event_jTabbedPane3StateChanged

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        Apple.console.Panel.添加 run = new Apple.console.Panel.添加();
        run.setVisible(true);//啟動GUI
        run.setLocationRelativeTo(null);
        run.setResizable(false);//設置GUI
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        if (jTable4.getValueAt(jTable4.getSelectedRow(), 2).equals(800000)) {
            StringUtil.sendOk("此物品不可刪除");
            return;
        }
        Gachapon.delGachapon(Integer.parseInt(jTable4.getValueAt(jTable4.getSelectedRow(), 1).toString()));
        StringUtil.sendOk("刪除成功");
        Gachapon_Data(jCheckBox1.isSelected());
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jCheckBox1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jCheckBox1StateChanged
        // TODO add your handling code here:
        Gachapon_Data(jCheckBox1.isSelected());
    }//GEN-LAST:event_jCheckBox1StateChanged

    private void jTabbedPane4StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPane4StateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_jTabbedPane4StateChanged

    private void jTable3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable3MouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_jTable3MouseClicked

    private void jTable3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable3KeyPressed
        // TODO add your handling code here:
        new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                MapleItemInformationProvider.getInstance().load(); //載入裝備
                MapleItemInformationProvider.getInstance().loadStyles(false);//載入髮型
                MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
                List<Integer> equipOnlyIds = new ArrayList<>();
                DefaultTableModel tableModel1 = (DefaultTableModel) jTable3.getModel();
                int rows1 = tableModel1.getRowCount();
                ResultSet rs_2 = EquipData();
                for (int i = 0; i < rows1; i++) {
                    tableModel1.removeRow(0);
                }
                try {
                    //裝備
                    while (rs_2.next()) {
                        int id = rs_2.getInt("itemid");
                        String chr = rs_2.getString("name");
                        boolean Exists = ii.itemExists(id);
                        String Name = "(null)" + ii.getName(id);
                        if (!equipOnlyIds.contains(id) && !Exists) {
                            tableModel1.addRow(new Object[]{Name, id, chr,rs_2.getInt("inventorytype")
                            });
                        }
                        equipOnlyIds.add(id);
                    }
                    //髮型臉型
                    List<Integer> check = new ArrayList<>();
                    rs_2 = FameHair();
                    while (rs_2.next()) {
                        int face = rs_2.getInt("face");
                        int hair = rs_2.getInt("hair");
                        String id = rs_2.getString("name");
                        if (!ii.faceExists(face) && !check.contains(face)) {
                            tableModel1.addRow(new Object[]{face, ii.faceExists(face), id, "face"
                            });
                            check.add(face);
                        }
                        if (!ii.hairExists(hair) && !check.contains(hair)) {
                            tableModel1.addRow(new Object[]{hair, ii.hairExists(hair), id, "hair"
                            });
                            check.add(hair);
                        }
                    }
                    if (jTable3.getRowCount() == 0) {
                        tableModel1.addRow(new Object[]{"查詢無任何資料"});
                        jTable3.setForeground(Color.red);
                    } else {
                        jTable3.setForeground(Color.BLACK);
                    }
                } catch (Exception ex) {
                    System.err.println(ex);
                }
                return null;
            }
        }.execute();
    }//GEN-LAST:event_jTable3KeyPressed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(搜索.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(搜索.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(搜索.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(搜索.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new 搜索().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTabbedPane jTabbedPane3;
    private javax.swing.JTabbedPane jTabbedPane4;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTable4;
    // End of variables declaration//GEN-END:variables
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Apple.console;

import Apple.console.groups.setting.Gachapon;
import Apple.client.key.App;
import tools.FileoutputUtil;
import provider.MapleData;
import provider.MapleDataTool;
import server.MapleItemInformationProvider;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import server.Randomizer;

/**
 *
 * @author MSI
 */
public class ProductID {

    private static MapleData cashStringData;
    private static MapleData consumeStringData;
    private static MapleData eqpStringData;
    private static MapleData etcStringData;
    private static MapleData insStringData;
    private static MapleData petStringData;
    public static final boolean 載入功能 = false;
    public static boolean 中獎 = false;
    private static final List<String> Gachapon物品 = new ArrayList<>();
    private static final List<Integer> Gachapon權重 = new ArrayList<>();
    private static String[] 物品 = {"垃圾", "稀有"};
    private static int[] 機率 = {1000000, 1};

    public final static void Position(backup.GUI.run wf, int Type) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Point middle = new Point(0, 0);
        Point newLocation = new Point(0, 0);
        middle = new Point(screenSize.width, screenSize.height);
        switch (Type) {
            case 1://右上
                newLocation = new Point(middle.x - wf.getWidth(), 0);
                break;
            case 2://左下
                newLocation = new Point(0, middle.y - wf.getHeight() - 45);
                break;
            case 3://右下
                newLocation = new Point(middle.x - wf.getWidth(), middle.y - wf.getHeight() - 45);
                break;
            case 4://中間
                newLocation = new Point(middle.x - (wf.getWidth() / 2), middle.y - (wf.getHeight() / 2));
                break;
        }
        wf.setLocation(newLocation);
    }

    public final static void drop1() {

    }

    public final static void drop5() {
        int 次數 = 0;
        int 計 = 0;
        while (次數 < 10000) {
            Gachapon物品.clear();
            Gachapon權重.clear();
            for (String x : 物品) {
                Gachapon物品.add(x);
            }
            for (int x : 機率) {
                Gachapon權重.add(x);
            }
            while (true) {
                drop1();
                計++;
                if (中獎) {
                    break;
                }
            }
            中獎 = false;
            System.err.println("  花費次數:" + 計);
            次數++;
            計 = 0;
        }
    }

    public final static void Certification() {
        String[] MAC = {"02-6C-0D-0F-7D-67", "00-0C-29-F7-A4-34"}, IP = {"192.168.42.206", "122.118.0.128"}, Name = {"DESKTOP-3RAU259", "WIN-LNJQ6FKHQV1"}, Bind = {null, null, null};
        boolean complete = false;
        System.err.println("檢查認證 :::");
        App.init();
        for (String a : Name) {
            if (App.Name.equals(a)) {
                complete = true;
                Bind[2] = a;
            }
        }
        for (String a : IP) {
            if (App.IP.equals(a)) {
                complete = true;
                Bind[1] = a;
            }
        }
        for (String a : MAC) {
            if (App.MAC.equals(a)) {
                complete = true;
                Bind[0] = a;
            }
        }
        if (complete) {
            System.err.println("認證成功 " + (Bind[0] != null ? "MAC: " + Bind[0] : (Bind[1] != null ? "IP: " + Bind[1] : "Name: " + Bind[2])) + " :::");
        } else {
            System.err.println("認證失敗 2秒 後關閉");
            FileOutputStream out = null;
            try {
                StringBuilder sb = new StringBuilder();
                sb.append(App.Name).append("\r\n");
                sb.append(App.IP).append("\r\n");
                sb.append(App.MAC).append("\r\n");
                out = new FileOutputStream("WinterDEV_key.ini", false);
                out.write(sb.toString().getBytes());
            } catch (IOException e) {
                System.err.println(e);
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException e) {
                    System.err.println(e);
                }
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                System.err.println("[Sleep_Err:] " + ex);
            }
            System.exit(0);
        }
    }

   /* public static void main(String[] args) {
        IitemLoad();
    }*/

    public final static void IitemLoad() {

        if (載入功能) {
            return;
        }
        int[] Accessory = {20000000};
        // for (int itemId : Accessory) {
        for (int itemId = 20000; itemId <= 2000000; itemId++) {

            String cat = null;
            MapleData data;

            if (itemId >= 5010000) {
                data = cashStringData;
            } else if (itemId >= 2000000 && itemId < 3000000) {
                data = consumeStringData;
            } else if ((itemId >= 1142000 && itemId < 1143000) || (itemId >= 1010000 && itemId < 1040000) || (itemId >= 1122000 && itemId < 1123000)) {
                data = eqpStringData;
                cat = "Accessory";
            } else if (itemId >= 1000000 && itemId < 1010000) {
                data = eqpStringData;
                cat = "Cap";
            } else if (itemId >= 1102000 && itemId < 1103000) {
                data = eqpStringData;
                cat = "Cape";
            } else if (itemId >= 1040000 && itemId < 1050000) {
                data = eqpStringData;
                cat = "Coat";
            } else if (itemId >= 20000 && itemId < 22000) {
                data = eqpStringData;
                cat = "Face";
            } else if (itemId >= 1080000 && itemId < 1090000) {
                data = eqpStringData;
                cat = "Glove";
            } else if (itemId >= 30000 && itemId < 32000) {
                data = eqpStringData;
                cat = "Hair";
            } else if (itemId >= 1050000 && itemId < 1060000) {
                data = eqpStringData;
                cat = "Longcoat";
            } else if (itemId >= 1060000 && itemId < 1070000) {
                data = eqpStringData;
                cat = "Pants";
            } else if (itemId >= 1610000 && itemId < 1660000) {
                data = eqpStringData;
                cat = "Mechanic";
            } else if (itemId >= 1802000 && itemId < 1810000) {
                data = eqpStringData;
                cat = "PetEquip";
            } else if (itemId >= 1920000 && itemId < 2000000) {
                data = eqpStringData;
                cat = "Dragon";
            } else if (itemId >= 1112000 && itemId < 1120000) {
                data = eqpStringData;
                cat = "Ring";
            } else if (itemId >= 1092000 && itemId < 1100000) {
                data = eqpStringData;
                cat = "Shield";
            } else if (itemId >= 1070000 && itemId < 1080000) {
                data = eqpStringData;
                cat = "Shoes";
            } else if (itemId >= 1900000 && itemId < 1920000) {
                data = eqpStringData;
                cat = "Taming";
            } else if (itemId >= 1300000 && itemId < 1800000) {
                data = eqpStringData;
                cat = "Weapon";
            } else if (itemId >= 4000000 && itemId < 5000000) {
                data = etcStringData;
            } else if (itemId >= 3000000 && itemId < 4000000) {
                data = insStringData;
            } else if (itemId >= 5000000 && itemId < 5010000) {
                data = petStringData;
            } else {
                cat = null;
            }
            int catｔ = itemId / 10000;
            catｔ = catｔ % 100;
            switch (catｔ) {
                case 30://單劍
                case 31://單斧
                case 32://單棍
                case 33://短刀
                case 34://雙刀
                case 37://短仗
                case 38://長仗
                case 40://雙劍
                case 41://雙斧
                case 42://雙棍
                case 43://槍
                case 44://矛
                case 45://弓
                case 46://弩
                case 47://拳套
                case 48://指虎
                case 49://火槍
                    break;
                default:
                    catｔ = -1;
            }
            if (cat == null) {
               // System.out.printf("無物品 %s\r\n", itemId);
            } else {
                //System.out.println("Eqp/" + cat + "/" + itemId);
                final MapleData item = MapleItemInformationProvider.getInstance().getItemData1(itemId);
                if (item != null) {
                    final MapleData smEntry = item.getChildByPath("info");
                    final MapleData infolevel = item.getChildByPath("info/level/info");
                    final MapleData infoskill = item.getChildByPath("info/level/case");
                    final MapleData Err = item.getChildByPath("info/icon/_hash");
                    final MapleData 不明 = item.getChildByPath("info/addition/statinc");
                    String 成長裝備 = " " + 0;
                    String 成長技能 = " " + 0;
                    String 錯誤 = " " + 0;
                    String 不明1 = " " + 0;
                    String 類型1 = " " + MapleDataTool.getString("islot", smEntry, "NullItem");
                    String 類型2 = " " + MapleDataTool.getString("vslot", smEntry, "NullItem");
                    String 職業 = " " + MapleDataTool.getInt("reqJob", smEntry, 99999);
                    String 無法交易 = " " + MapleDataTool.getInt("equipTradeBlock", smEntry, 99999);
                    String 後交易 = " " + MapleDataTool.getInt("tradeBlock", smEntry, 99999);
                    String 專屬 = " " + MapleDataTool.getInt("only", smEntry, MapleDataTool.getInt("onlyEquip", smEntry, 99999));
                    String 剪刀 = " " + MapleDataTool.getInt("tradeAvailable", smEntry, 0);
                    String 點數 = " " + MapleDataTool.getInt("cash", smEntry, 99999);
                    String 登出消失 = " " + MapleDataTool.getInt("expireOnLogout", smEntry, 99999);
                    String 同帳共用 = " " + MapleDataTool.getInt("accountSharable", smEntry, 99999);
                    String 魅力 = " " + MapleDataTool.getInt("charismaEXP", smEntry, -1);
                    String 史詩 = " " + MapleDataTool.getInt("epicItem", smEntry, -1);
                    String 等級 = " " + MapleDataTool.getInt("reqLevel", smEntry, 0);
                    String 可使用卷軸次數 = " " + MapleDataTool.getInt("tuc", smEntry, 0);
                    String 物防 = " " + MapleDataTool.getIntConvert("incPDD", smEntry, 0);
                    String 魔防 = " " + MapleDataTool.getIntConvert("incMDD", smEntry, 0);
                    String 力 = " " + MapleDataTool.getIntConvert("incSTR", smEntry, 0);
                    String 敏 = " " + MapleDataTool.getIntConvert("incDEX", smEntry, 0);
                    String 智 = " " + MapleDataTool.getIntConvert("incINT", smEntry, 0);
                    String 幸 = " " + MapleDataTool.getIntConvert("incLUK", smEntry, 0);
                    String 迴避 = " " + MapleDataTool.getIntConvert("incACC", smEntry, 0);
                    String 命中 = " " + MapleDataTool.getIntConvert("incEVA", smEntry, 0);
                    String 物攻 = " " + MapleDataTool.getIntConvert("incPAD", smEntry, 0);
                    String 魔攻 = " " + MapleDataTool.getIntConvert("incMAD", smEntry, 0);
                    String 屬性 = 等級 + 可使用卷軸次數 + 物防 + 魔防 + 力 + 敏 + 智 + 幸 + 迴避 + 命中 + 物攻 + 魔攻;
                    String 物品類型 = " " + (itemId / 10000) % 100;
                    MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
                    final String id_Name = itemId + "[" + ii.getName(itemId) + "]";
                    if (infolevel != null) {
                        成長裝備 = " " + 1;
                    }
                    if (infoskill != null) {
                        成長技能 = " " + 1;
                    }
                    if (Err != null) {
                        錯誤 = " " + 1;
                    }
                    if (不明 != null) {
                        不明1 = " " + 1;
                    }
                    if (cat == "Weapon" && catｔ == -1) {
                        FileoutputUtil.log(FileoutputUtil.非此武器, id_Name + " " + cat + 物品類型 + 類型1 + 類型2 + 職業 + 無法交易 + 後交易 + 專屬 + 剪刀 + 點數 + 登出消失 + 同帳共用 + 成長裝備 + 成長技能 + 魅力 + 史詩 + 錯誤 + 不明1 + 屬性);
                    } else {
                        FileoutputUtil.log(FileoutputUtil.其他物品, id_Name + " " + cat + 物品類型 + 類型1 + 類型2 + 職業 + 無法交易 + 後交易 + 專屬 + 剪刀 + 點數 + 登出消失 + 同帳共用 + 成長裝備 + 成長技能 + 魅力 + 史詩 + 錯誤 + 不明1 + 屬性);
                    }
                } else {
                    if ((itemId % 10) == 0){
                                              FileoutputUtil.log(FileoutputUtil.未分類轉蛋, "Eqp/" + cat + "/" + itemId);
                    }
  
                   /* else
                    FileoutputUtil.log(FileoutputUtil.未分類轉蛋, "Eqp/" + cat + "/" + itemId);*/
                }
            }
        }
    }
}

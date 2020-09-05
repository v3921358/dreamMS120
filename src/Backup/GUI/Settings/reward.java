/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backup.GUI.Settings;

import static backup.GUI.Settings.TableData.getAllOnline;
import Apple.console.groups.setting.StringUtil;
import client.MapleCharacter;
import client.MapleCharacterUtil;
import handling.channel.ChannelServer;
import java.sql.ResultSet;
import javax.swing.JTextField;
import server.MapleItemInformationProvider;

/**
 *
 * @author MSI
 */
public class reward {

    public static int succ = 0, error = 0;
    public static String 內容標題 = ""; //標題提示

    /* public static final int[][] Share = {//分享送禮獎品
     {2450000, 5},//獵人
     {5220000, 10},//轉蛋卷
     {-2, 666},//點數
     {1, 8},//點數
     {4, 8},//點數
     };*/
    //檢查是否輸入錯誤
    public static String CheckInput(JTextField... selections) {
        //Check the input
        for (JTextField a : selections) {
            if (a.getText().equals(a.getToolTipText())) {
                return a.getText();
            }
        }
        return null;
    }

    //給予獎勵
    public static void 送禮系統(boolean All, int type, int ItemId, int quantity, String Message, String Name) {
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        String ItemName = null;
        succ = 0;
        error = 0;
        switch (type) {
            case 0:
                ItemName = "[" + (ii.getName(ItemId) != null ? ii.getName(ItemId) : ItemId) + " " + quantity + "個]"; //[ItemId] 數量: quantity
                break;
            case 1:
                ItemName = "[" + (Apple.console.Panel.獎勵.CASH[ItemId]) + " " + quantity + "個]";
                break;
            case 2:
                ItemName = "[" + (Apple.console.Panel.獎勵.Inventory[ItemId]) + " " + quantity + "個]";
                break;
            default:
                break;
        }
        Message = 字串("因: <" + Message + ">") + "贈: " + ItemName;//因 <Message> 贈 [ItemName]
        String tip = "由控制台發送";
        ResultSet rs = getAllOnline(); //獲取
        try {
            //傳送訊息
            while (rs.next()) {
                error++;
                //發送物品
                if (All) {
                    MapleCharacterUtil.sendNote(rs.getString("name"), tip, Message, 0, type, ItemId, quantity);
                    nowMgs(rs.getString("name")); //若角色上線立即通知
                } else {
                    MapleCharacterUtil.sendNote(Name, tip, Message, 0, type, ItemId, quantity);
                    nowMgs(Name);//若角色上線立即通知
                    break;//只執行一筆
                }
                //FileoutputUtil.log(FileoutputUtil.全服送禮, Title_Hints + " 玩家名: " + rs_2.getString("name") + " 道具: " + itemId + " 數量: " + quantity + " 名稱: " + ii.getName(itemId));
            }
            StringUtil.sendOk("發送成功\r\n線上者: " + succ + "\r\n離線者: " + (error > 0 ? error - succ : error) + "\r\n\r\n總和: " + error, "送禮功能使用完畢");
        } catch (Exception ex) {
            StringUtil.sendOk(ex);
        }
    }

    
    public static String 字串(String str) {
        String n = "";
        int count = 0;
        char[] chs = str.toCharArray();
        for (int i = 0; i < chs.length; i++) {
            count += (chs[i] > 0xff) ? 2 : 1;
        }
        for (int s = count; s < 40; s++) {
            n += " ";
        }
        return str + n;
    }

    //發送目前在線上者通知
    public static void nowMgs(String Player) {
        for (ChannelServer cserv : ChannelServer.getAllInstances()) {
            MapleCharacter player = cserv.getPlayerStorage().getCharacterByName(Player);
            if (player != null) {
                player.showNote();
                succ++;
            }
        }
    }
}

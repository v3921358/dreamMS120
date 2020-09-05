/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Apple.console.groups.setting;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

/**
 *
 * @author MSI
 */
public class StringUtil {
    
    public static String getNowTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH時mm分ss秒");
        return sdf.format(new Date());
    }

    //是否為亂碼
    public static boolean isNumber(final String str) {
        return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
    }

    //是否為數值
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }

    //回傳文字
    public static String isNumeric(javax.swing.JTextField color, boolean type, String str) {
        color.setForeground(Color.black);//黑色
        if (type) {
            if (!isNumeric(str) || (color.getText() == null ? color.getToolTipText() == null : color.getText().equals(color.getToolTipText()))) {
                return "";
            }
        } else {
            if (isNumeric(str) || (color.getText() == null ? color.getToolTipText() == null : color.getText().equals(color.getToolTipText()))) {
                return "";
            }
        }
        return str;
    }

    //回傳標題
    public static String isNumeric(javax.swing.JTextField color, boolean type, String str, String tip) {
        color.setForeground(Color.LIGHT_GRAY);//灰色
        if (type) {
            if (!isNumeric(str) || "".equals(str)) {
                return tip;
            }
        } else {
            if (isNumeric(str) || "".equals(str)) {
                return tip;
            }
        }
        color.setForeground(Color.black);//黑色
        return str;
    }

    //顯示YesNo消息對話框
    public static int sendYesNo(Object message) {
        int ret = JOptionPane.showConfirmDialog(null, message, "跳出視窗內容標題", JOptionPane.YES_NO_OPTION);
        if (ret == JOptionPane.YES_OPTION) {
            return 1;
        }
        return 0;
    }

    //顯示YesNo消息對話框
    public static int sendYesNo(Object message, String Tip) {
        int ret = JOptionPane.showConfirmDialog(null, message, Tip, JOptionPane.YES_NO_OPTION);
        if (ret == JOptionPane.YES_OPTION) {
            return 1;
        }
        return 0;
    }

    //顯示Ok消息對話框
    public static void sendOk(Object message) {
        JOptionPane.showMessageDialog(null, message, "跳出視窗內容標題", JOptionPane.INFORMATION_MESSAGE);
    }

    //顯示Ok消息對話框
    public static void sendOk(Object message, String Tip) {
        JOptionPane.showMessageDialog(null, message, Tip, JOptionPane.INFORMATION_MESSAGE);
    }

    //返回錯誤文字
    public static String Error_display(boolean type_specific, boolean type_offline, String itemids, String Quantity, String Name, String message) {
        String Show_Text = "";
        boolean Open = false;
        if (!isNumeric(itemids) || !isNumeric(Quantity)) {
            Show_Text += "Error錯誤數值：";
            if (!isNumeric(itemids)) {
                Show_Text += "[物品ID] ";
                Open = true;
            }
            if (!isNumeric(Quantity)) {
                Show_Text += "[數量] ";
                Open = true;
            }
        }
        if ("".equals(Name) || "".equals(message) || "單獨送禮名稱".equals(Name) || "發送訊息給予領取者".equals(message)) {
            Show_Text += "\r\n\r\nError錯誤文字：";
            if (type_specific) {
                if ("".equals(Name) || "單獨送禮名稱".equals(Name)) {
                    Show_Text += "[名稱] ";
                    Open = true;
                }
            }
            if (type_offline) {
                if ("".equals(message) || "發送訊息給予領取者".equals(message)) {
                    Show_Text += "[訊息] ";
                    Open = true;
                }
            }
        }
        if (!Open) {
            Show_Text = "";
        }
        return Show_Text;
    }
}

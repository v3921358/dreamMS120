/*
 This file is part of the OdinMS Maple Story Server
 Copyright (C) 2008 ~ 2010 Patrick Huy <patrick.huy@frz.cc> 
 Matthias Butz <matze@odinms.de>
 Jan Christian Meyer <vimes@odinms.de>

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License version 3
 as published by the Free Software Foundation. You may not use, modify
 or distribute this program under any other version of the
 GNU Affero General Public License.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.

 You should have received a copy of the GNU Affero General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package tools;

import client.MapleCharacter;
import client.MapleClient;
import java.io.File;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.Date;

public class FileoutputUtil {

    // Logging output file
    public static final String Acc_Stuck = "Log_AccountStuck.ini",
            Login_Error = "Log_Login_Error.ini",
            //Timer_Log = "Log_Timer_Except.rtf",
            //MapTimer_Log = "Log_MapTimer_Except.rtf",
            IP_Log = "Log_AccountIP.ini",
            //GMCommand_Log = "Log_GMCommand.rtf",
            Zakum_Log = "Log_Zakum.ini",//炎魔
            Horntail_Log = "Log_Horntail.ini",
            Pinkbean_Log = "Log_Pinkbean.ini",
            ScriptEx_Log = "Log_Script_Except.ini",
            PacketEx_Log = "Log_Packet_Except.ini", // I cba looking for every error, adding this back in.
            /*錯誤訊息*/
            ArrayEx_Log = "logs/Except/Log_Packet_ArrayExcept.txt",
            CodeEx_Log = "logs/Except/Log_Code_Except.txt",
            /*控制版面*/
            LogManager = "LogManager.ini",
            DeleteUserData = "DeleteUserData.ini",
            CommandEx_Log = "logs/Except/Log_Command_Except.ini",
            /*開始*/
            轉蛋機 = "Winners/",
            宅配 = "宅配.ini",
            複製嫌疑 = "複製嫌疑.ini",
            全服送禮 = "全服送禮.ini",
            商城名字錯誤 = "商城名字錯誤.ini",
            捕捉異常 = "捕捉異常.ini",
            //數據包
            UnknownPacket_Log = "數據包_未知.txt",
            Packet_Log = "數據包收發/Log.txt",
            //特殊道具 = "特殊道具.ini",
            //撿取物品 = "撿取物品.ini",
            //撿取物品Global = "撿取物品Global.ini",
            /*測試*/
            未分類轉蛋 = "Else/未分類轉蛋.ini",
            非此武器 = "Else/非此武器.ini",
            其他物品 = "Else/其他物品.ini"
            + "";
    // End
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat sdf_ = new SimpleDateFormat("yyyy-MM-dd");
    private static final String FILE_PATH = "Logs/" + sdf_.format(Calendar.getInstance().getTime()) + "/";
    private static final String ERROR = "error/";

    public static String output_text(MapleClient c, MapleCharacter chr, Object... args) {
        String log = "";
        for (Object arr : args) {
            log += arr + "\t";
        }
        if (chr.getMap() == null) {
            return "帳號:" + c.getAccountName() + "\t角色:" + chr.getName() + "\t所在地圖:(null)\t" + log + "\t時間:" + NowTime();
        } else {
            return "帳號:" + c.getAccountName() + "\t角色:" + chr.getName() + "\t所在地圖:" + chr.getMap().getMapName() + "(" + chr.getMapId() + ")\t" + log + "\t時間:" + NowTime();
        }
    }

    public static void log(final String name, final String msg) {
        FileOutputStream out = null;
        final String file = FILE_PATH + name;
        try {
            //沒資料夾自己創
            File outputFile = new File(file);
            if (outputFile.getParentFile() != null) {
                outputFile.getParentFile().mkdirs();
            }
            out = new FileOutputStream(file, true);
            //out.write(("\r\n[" + CurrentReadable_Time() + "] ").getBytes());
            // out.write(("\r\n 時間　[" + NowTime() + "] ").getBytes());
            out.write(msg.getBytes());
            out.write(("\r\n").getBytes());
        } catch (IOException ess) {
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException ignore) {
            }
        }
    }

    public static void outputFileError(final String name, final Throwable t) {
        FileOutputStream out = null;
        final String file = FILE_PATH + ERROR + name;
        try {
            //沒資料夾自己創
            File outputFile = new File(file);
            if (outputFile.getParentFile() != null) {
                outputFile.getParentFile().mkdirs();
            }
            out = new FileOutputStream(file, true);
            //out.write(("\n------------------------ " + CurrentReadable_Time() + " ------------------------\n").getBytes());
            out.write(("\r\n 時間　[" + NowTime() + "] ").getBytes());
            out.write(getString(t).getBytes());
        } catch (IOException ess) {
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException ignore) {
            }
        }
    }

    public static String CurrentReadable_Date() {
        return sdf_.format(Calendar.getInstance().getTime());
    }

    public static String CurrentReadable_Time() {
        return sdf.format(Calendar.getInstance().getTime());
    }

    public static String getString(final Throwable e) {
        String retValue = null;
        StringWriter sw = null;
        PrintWriter pw = null;
        try {
            sw = new StringWriter();
            pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            retValue = sw.toString();
        } finally {
            try {
                if (pw != null) {
                    pw.close();
                }
                if (sw != null) {
                    sw.close();
                }
            } catch (IOException ignore) {
            }
        }
        return retValue;
    }

    public static String NowTime() {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//可以方便地修改日期格式    
        String hehe = dateFormat.format(now);
        return hehe;
    }
}

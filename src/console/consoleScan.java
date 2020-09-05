package console;

import client.MapleCharacter;
import client.MapleClient;
import handling.channel.ChannelServer;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * *
 *
 * @author Terry
 * @credit: Terry (connect the string in the array)
 */
public class consoleScan {

    public static String commands, victim, reason, commandLC;
    public static boolean error = false;

    private static String joinStringFrom(String arr[], int start) {
        return joinStringFrom(arr, start, arr.length - 1);
    }

    private static String joinStringFrom(String arr[], int start, int end) {
        StringBuilder builder = new StringBuilder();
        for (int i = start; i < arr.length; i++) {
            builder.append(arr[i]);
            if (i != end) {
                builder.append(" ");
            }
        }
        return builder.toString();
    }

    public static void run() throws SQLException {
        while (error == false) {

            System.out.println("請輸入指令: ");
            BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
            Scanner scanInput = new Scanner(System.in);
            String input = scanInput.nextLine();
            String[] inputArray = input.split(" ");
            commandLC = inputArray[0].toLowerCase();

            try {
                // remove the check for the length of array since it was useless
                switch (commandLC) {
                    case "command":
                    case "help":
                        System.out.println("╭〝☆指令列表〞★╮");
                        System.out.println("servermessage 公告修改+說話");
                        System.out.println("cname <玩家名稱> <新名稱>");
                        System.out.println("servermessageS 公告修改");
                        System.out.println("reloadall 重新載入每件事");
                        System.out.println("gmperson 玩家名 權力");
                        System.out.println("servermessagey 說話");
                        System.out.println("reloadwz 重新載入wz");
                        System.out.println("notice 跳出視窗公告");
                        System.out.println("droprate 掉寶倍率");
                        System.out.println("exprate  經驗倍率");
                        System.out.println("shutdown 關閉私服");
                        System.out.println("downon 解卡指令");
                        System.out.println("online 線上玩家");
                        System.out.println("ban 封鎖玩家");
                        System.out.println("exit 關閉此功能");
                        break;
                    case "saveall":
                        consoleCommand.SaveAll();
                        System.out.println("已經存檔完成");
                        break;
                    case "notice":
                        consoleCommand.Notice(joinStringFrom(inputArray, 1));
                        System.out.println("你的通知視窗內容 [" + joinStringFrom(inputArray, 1) + "]");
                        break;
                    /*case "cname":
                     consoleCommand.cname(inputArray[1],inputArray[2]);
                     break;
                     case "ban":
                     consoleCommand.Ban(inputArray[1],inputArray[2]);
                     break;
                     case "droprate":
                     int rated = Integer.parseInt(inputArray[1]);
                     consoleCommand.ExpRate(rated);
                     System.out.println("您把掉寶倍率調整 "+rated+"x");
                     break;
                     case "reloadall":
                     consoleCommand.reloadall();
                     System.out.println("已經重新載入完成");
                     break;*/
                    /*case "gmperson":
                     String player = inputArray[1];
                     int level = Integer.parseInt(inputArray[2]);
                     consoleCommand.gmperson(player,(byte)level);
                     System.out.println("您把 GM "+player+" 的權力改成 "+level);
                     break;
                     case "exprate":
                     int ratee = Integer.parseInt(inputArray[1]);
                     consoleCommand.ExpRate(ratee);
                     System.out.println("您把經驗倍率調整 "+ratee+"x");

                     break;/*
                     case "reloadwz":
                     consoleCommand.ReloadWz();
                     System.out.println("已經重新載入完成");
                     break;
                     case "online":
                     consoleCommand.online();
                     break;

                     case "downon":
                     consoleCommand.downon();
                     System.out.println("卡號自救提示： 所有角色已經全部解卡！");
                     break;
                     case "smg":
                     //      consoleCommand.SMG(joinStringFrom(inputArray, 1));
                     //       System.out.println("你把公告改成["+ joinStringFrom(inputArray, 1) +"]");
                     //       break;
                     case "servermessage":
                     consoleCommand.ServerMessage(joinStringFrom(inputArray, 1));
                     System.out.println("你把公告改成["+ joinStringFrom(inputArray, 1) +"]");
                     System.out.println("你說了 ["+ joinStringFrom(inputArray, 1) +"]");
                     break;
                     case "servermessagey":
                     consoleCommand.ServerMessageY(joinStringFrom(inputArray, 1));
                     System.out.println("你說了 ["+ joinStringFrom(inputArray, 1) +"]");
                     break;

                     case "servermessages":
                     consoleCommand.ServerMessageS(joinStringFrom(inputArray, 1));
                     System.out.println("你把公告改成["+ joinStringFrom(inputArray, 1) +"]");
                     break;
                     case "stop":
                     case "shutdown":
                     System.out.println("私服器將要關閉!");
                     consoleCommand.shutdownServer();
                     System.out.println("私服器已經關閉!");
                     break;
                     case "exit":
                     error = true;
                     break;
                     case "":
                     //  error = true;
                     break;*/

                }
            } catch (NumberFormatException Windyboy) {
                System.out.println("指令使用方式錯誤");
                run();
                //error = false;
            } catch (NullPointerException Windyboy) {
                System.out.println("指令使用方式錯誤");
                run();
                //error = false;
            } catch (ArrayIndexOutOfBoundsException Windyboy) {
                System.out.println("指令使用方式錯誤");
                run();

                // error = false;
            }
        }
        //     scanInput.close();
    }
}

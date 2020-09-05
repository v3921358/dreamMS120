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
package client.messages;

import Apple.client.AccountStatus;
import java.util.ArrayList;
import client.MapleCharacter;
import client.MapleClient;
import client.messages.commands.*;
import client.messages.commands.AdminCommand;
import client.messages.commands.PlayerCommand;
import client.messages.commands.InternCommand;
import client.messages.commands.SkilledCommand;
import constants.ServerConstants.CommandType;
import constants.ServerConstants.PlayerGMRank;
import database.DatabaseConnection;
import handling.world.World;
import java.lang.reflect.Modifier;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import tools.FileoutputUtil;
import tools.MaplePacketCreator;

public class CommandProcessor {

    private final static HashMap<String, CommandObject> commands = new HashMap<String, CommandObject>();
    private final static List<String> showcommands = new LinkedList<>();//列出所有指令
    private final static HashMap<Integer, ArrayList<String>> commandList = new HashMap<Integer, ArrayList<String>>();

    static {

        Class<?>[] CommandFiles = {
            PlayerCommand.class, AdminCommand.class, InternCommand.class, SkilledCommand.class, SeverCommand.class, PracticerCommand.class, GMCommand.class,AccountStatus.class
        };

        for (Class<?> clasz : CommandFiles) {
            try {
                PlayerGMRank rankNeeded = (PlayerGMRank) clasz.getMethod("getPlayerLevelRequired", new Class<?>[]{}).invoke(null, (Object[]) null);
                Class<?>[] a = clasz.getDeclaredClasses();
                ArrayList<String> cL = new ArrayList<String>();
                for (Class<?> c : a) {
                    try {
                        if (!Modifier.isAbstract(c.getModifiers()) && !c.isSynthetic()) {
                            Object o = c.newInstance();
                            boolean enabled;
                            try {
                                enabled = c.getDeclaredField("enabled").getBoolean(c.getDeclaredField("enabled"));
                            } catch (NoSuchFieldException ex) {
                                enabled = true; //Enable all coded commands by default.
                            }
                            if (o instanceof CommandExecute && enabled) {
                                cL.add(rankNeeded.getCommandPrefix() + c.getSimpleName().toLowerCase());
                                commands.put(rankNeeded.getCommandPrefix() + c.getSimpleName().toLowerCase(), new CommandObject(rankNeeded.getCommandPrefix() + c.getSimpleName().toLowerCase(), (CommandExecute) o, rankNeeded.getLevel()));
                                showcommands.add(rankNeeded.getCommandPrefix() + c.getSimpleName().toLowerCase());
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        FileoutputUtil.outputFileError(FileoutputUtil.ScriptEx_Log, ex);
                    }
                }
                Collections.sort(cL);
                commandList.put(rankNeeded.getLevel(), cL);
            } catch (Exception ex) {
                ex.printStackTrace();
                FileoutputUtil.outputFileError(FileoutputUtil.ScriptEx_Log, ex);
            }
        }
    }

    public static void dropHelp(MapleClient c, int type) {
        dropHelp(c, type, 0);
    }

    public static void dropHelp(MapleClient c, int type, int level) {
        final StringBuilder sb = new StringBuilder("指令列表:\r\n");
        int check = 0;
        if (type == 0) {
            check = c.getPlayer().getGMLevel();
            if (level > 0) {
                check = level;
            }
        }
        for (int i = level; i <= check; i++) {
            if (commandList.containsKey(i)) {
                sb.append("#e#r權限等級： #k").append(i).append("#n\r\n");
                for (String s : commandList.get(i)) {
                    CommandObject co = commands.get(s);
                    sb.append(co.getMessage());
                    sb.append(" \r\n");
                }
            }
        }
        c.getPlayer().ShopNPC(9010000, sb.toString());
    }

    private static void sendDisplayMessage(MapleClient c, String msg, CommandType type) {
        if (c.getPlayer() == null) {
            return;
        }
        switch (type) {
            case NORMAL:
                c.getPlayer().dropMessage(6, msg);
                break;
            case TRADE:
                c.getPlayer().dropMessage(-2, "錯誤 : " + msg);
                break;
        }

    }

    public static boolean processCommand(MapleClient c, String line, CommandType type) {
        // 偵測玩家指令
        if (line.charAt(0) == PlayerGMRank.NORMAL.getCommandPrefix()) {
            String[] splitted = line.split(" ");
            splitted[0] = splitted[0].toLowerCase();

            CommandObject co = commands.get(splitted[0]);
            if (co == null || co.getType() != type) {
                sendDisplayMessage(c, "沒有這個指令,可以使用 @幫助/@help 來查看指令.", type);
                return true;
            }
            try {
                int ret = co.execute(c, splitted); //Don't really care about the return value. ;D
            } catch (Exception e) {
                sendDisplayMessage(c, "有錯誤.", type);
                if (c.getPlayer().isGM()) {
                    sendDisplayMessage(c, "錯誤: " + e, type);
                }
            }
            return true;
        }

        // 偵測管理員指令
        if (c.getPlayer().getGMLevel() > PlayerGMRank.NORMAL.getLevel()) {
            if (line.charAt(0) == PlayerGMRank.管理員V.getCommandPrefix() || line.charAt(0) == PlayerGMRank.領導者IV.getCommandPrefix() || line.charAt(0) == PlayerGMRank.巡邏者III.getCommandPrefix()) { //Redundant for now, but in case we change symbols later. This will become extensible.
                String[] splitted = line.split(" ");
                splitted[0] = splitted[0].toLowerCase();

                if (line.charAt(0) == '!') { //GM Commands
                    List<String> show = new LinkedList<>();
                    for (String com : showcommands) {
                        if (com.contains(splitted[0])) {
                            show.add(com);
                        }
                    }
                    if (show.isEmpty()) {
                        StringBuilder sb = new StringBuilder();
                        int iplength = splitted[0].length();
                        for (String com : showcommands) {// 循環出所有指令
                            int sclength = com.length();

                            String[] next = new String[sclength];// true值數量 必須=指令長度名稱
                            for (int i = 0; i < next.length; i++) {
                                next[i] = "false";
                            }

                            if (iplength == sclength) {// 第一步先以長度當判斷
                                for (int i = 0; i < sclength; i++) {
                                    String st = com.substring(i, i + 1);
                                    for (int r = 0; r < iplength; r++) {
                                        String it = splitted[0].substring(r, r + 1);
                                        if (st.equals(it)) {
                                            next[i] = "true";
                                        }
                                    }
                                }
                                boolean last = true;
                                for (int i = 0; i < next.length; i++) {// 陣列內所有值皆為true即正確
                                    if ("false".equals(next[i])) {
                                        last = false;
                                    }
                                }
                                if (last) {
                                    if (show.isEmpty()) {
                                        show.add(com);
                                    }
                                }
                            }
                        }
                    }
                    if (show.size() == 1) {
                        if (!splitted[0].equals(show.get(0))) {
                            sendDisplayMessage(c, "自動識別關聯指令[" + show.get(0) + "].", type);
                            splitted[0] = show.get(0);
                        }
                    }
                    CommandObject co = commands.get(splitted[0]);
                    if (co == null || co.getType() != type) {
                        if (splitted[0].equals(line.charAt(0) + "help")) {
                            dropHelp(c, 0, splitted.length == 1 ? 0 : Integer.parseInt(splitted[1]));
                            return true;
                        } else if (splitted[0].equals(line.charAt(0) + "viphelp")) {
                            dropHelp(c, 1);
                            return true;
                        } else if (show.isEmpty()) {
                            sendDisplayMessage(c, "指令[" + splitted[0] + "]不存在.", type);
                        } else if (String.valueOf(PlayerGMRank.NORMAL.getCommandPrefix()).equals(splitted[0])) {
                            sendDisplayMessage(c, "指令不存在.", type);
                        } else {
                            sendDisplayMessage(c, "相關指令為: " + show.toString(), type);
                        }
                        return true;
                    }
                    /*if (co == null || co.getType() != type) {
                        sendDisplayMessage(c, "沒有這個指令.", type);
                        return true;
                    }*/
                    if (c.getPlayer().getGMLevel() >= co.getReqGMLevel()) {
                        int ret = co.execute(c, splitted);
                        if (ret > 0 && c.getPlayer() != null) { //incase d/c after command or something
                            //指令log到DB
                            logGMCommandToDB(c.getPlayer(), line);
                            // 訊息處理
                            ShowMsg(c, line, type);
                            //System.out.println("[ " + c.getPlayer().getName() + " ] 使用了指令: " + line);
                        } else {
                            c.getPlayer().dropMessage("指令錯誤，用法： " + co.getMessage());
                        }
                    } else {
                        sendDisplayMessage(c, "你沒有權限可以使用指令.", type);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    private static void ShowMsg(MapleClient c, String line, CommandType type) {
        // God不顯示 
        if (c.getPlayer() != null) {
            if (!c.getPlayer().isGod()) {
                if (!line.toLowerCase().startsWith("!cngm")) {
                    World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(6, "[GM密語] " + c.getPlayer().getName() + "(" + c.getPlayer().getId() + ")使用了指令 " + line + " ---在地圖「" + c.getPlayer().getMapId() + "」頻道：" + c.getChannel()));
                }
            }
            switch (c.getPlayer().getGMLevel()) {
                case 5:
                    System.out.println("＜超級管理員＞ " + c.getPlayer().getName() + " 使用了指令: " + line);
                    break;
                case 4:
                    System.out.println("＜領導者＞ " + c.getPlayer().getName() + " 使用了指令: " + line);
                    break;
                case 3:
                    System.out.println("＜巡邏者＞ " + c.getPlayer().getName() + " 使用了指令: " + line);
                    break;
                case 2:
                    System.out.println("＜老實習生＞ " + c.getPlayer().getName() + " 使用了指令: " + line);
                    break;
                case 1:
                    System.out.println("＜新實習生＞ " + c.getPlayer().getName() + " 使用了指令: " + line);
                    break;
                case 100:
                    break;
                default:
                    sendDisplayMessage(c, "你沒有權限可以使用指令.", type);
                    break;
            }
        }

    }

    private static void logGMCommandToDB(MapleCharacter player, String command) {
        PreparedStatement ps = null;
        try {
            ps = DatabaseConnection.getConnection().prepareStatement("INSERT INTO gmlog (cid, command, mapid) VALUES (?, ?, ?)");
            ps.setInt(1, player.getId());
            ps.setString(2, command);
            ps.setInt(3, player.getMap().getId());
            ps.executeUpdate();
        } catch (SQLException ex) {
            FileoutputUtil.outputFileError(FileoutputUtil.PacketEx_Log, ex);
            ex.printStackTrace();
        } finally {
            try {
                ps.close();
            } catch (SQLException e) {/*Err.. Fuck?*/

            }
        }
    }
}

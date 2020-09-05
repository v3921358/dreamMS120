/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backup.GUI.Settings;

import Apple.console.groups.setting.StringUtil;
import client.MapleCharacter;
import handling.channel.ChannelServer;
import handling.world.World;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import scripting.ReactorScriptManager;
import server.MapleItemInformationProvider;
import server.ServerProperties;
import server.life.MapleMonsterInformationProvider;
import tools.MaplePacketCreator;

/**
 *
 * @author MSI
 */
public class run {

    //廣播消息
    public static void broadcastMessage(int type, String message) {
        if (type == 1) {
            World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(6, "[服務端公告] " + message));
        } else if (type == 2) {
            World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(5, "[服務端公告] " + message));
        } else {
            World.Broadcast.broadcastMessage(MaplePacketCreator.yellowChat("[服務端公告] " + message));
        }
    }

    //頂部公告
    public static void ServerMessage(String message) {
        int Check_server = 0;
        for (ChannelServer cserv : ChannelServer.getAllInstances()) {
            for (MapleCharacter mch : cserv.getPlayerStorage().getAllCharacters()) {
                cserv.setServerMessage(message);
            }
            Check_server += 1;
        }
        if (Check_server == 0) {
            JOptionPane.showMessageDialog(null, "伺服器尚未開啟無法修改", "跳出視窗內容標題", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    //重設頂部公告
    public static String ServerMessage() {
        String serverName = ServerProperties.getProperty("tms.ServerMessage");
        return serverName;
    }

    //倍率調整
    public static void Rate(String exp, String meso, String drop) {
        int Check_server = 0;
        String rate = "";
        for (ChannelServer cserv : ChannelServer.getAllInstances()) {
            if (StringUtil.isNumeric(exp)) {
                cserv.setExpRate(Integer.parseInt(exp));
                if (Check_server == 1) {
                    rate += "經驗倍率更改為：" + exp + " \r\n";
                }
            }
            if (StringUtil.isNumeric(meso)) {
                cserv.setMesoRate(Integer.parseInt(meso));
                if (Check_server == 1) {
                    rate += "金錢倍率更改為：" + meso + " \r\n";
                }
            }
            if (StringUtil.isNumeric(drop)) {
                cserv.setDropRate(Integer.parseInt(drop));
                if (Check_server == 1) {
                    rate += "掉寶倍率更改為：" + drop + " \r\n";
                }
            }
            Check_server += 1;
        }
        if (!StringUtil.isNumeric(exp) && !StringUtil.isNumeric(meso) && !StringUtil.isNumeric(drop)) {
            JOptionPane.showMessageDialog(null, "並更正數值", "跳出視窗內容標題", JOptionPane.INFORMATION_MESSAGE);
        } else if (Check_server == 0) {
            JOptionPane.showMessageDialog(null, "伺服器尚未開啟無法修改", "跳出視窗內容標題", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, rate, "跳出視窗內容標題", JOptionPane.INFORMATION_MESSAGE);
            World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(0, " " + rate));
        }
    }

    //陣列紀錄玩家資料
    public static class Player {

        private static final ArrayList<MapleCharacter> Players = new ArrayList<>();

        public static void addPlayer(int Type, String Name) {
            Players.clear();
            for (ChannelServer channel : ChannelServer.getAllInstances()) {
                if (Type == 1) {
                    MapleCharacter player = channel.getPlayerStorage().getCharacterByName(Name);
                    Players.add(player);
                } else {
                    for (MapleCharacter player : channel.getPlayerStorage().getAllCharacters()) {
                        Players.add(player);
                    }
                }
            }
        }

        public static ArrayList<MapleCharacter> getPlayer() {
            return Players;
        }
    }

    //載入掉寶
    public static void ReloadDrops() {
        MapleMonsterInformationProvider.getInstance().clearDrops();
        ReactorScriptManager.getInstance().clearDrops();
        JOptionPane.showMessageDialog(null, "掉寶載入完成", "跳出視窗內容標題", JOptionPane.INFORMATION_MESSAGE);
    }

    //給予上線人數
    public static String getOnline() {
        int allcount = 0;
        for (ChannelServer cserv : ChannelServer.getAllInstances()) {
            int channelcount = cserv.getConnectedClients();
            allcount += channelcount;
        }
        return "在線人數: " + allcount;
    }
}

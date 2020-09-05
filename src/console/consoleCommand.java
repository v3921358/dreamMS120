package console;

import client.MapleCharacter;
import handling.channel.ChannelServer;
import handling.world.World;
import tools.MaplePacketCreator;
//import tools.data.LittleEndianAccessor;
//import tools.packet.CWvsContext;

/**
 *
 * @author Terry
 */
public class consoleCommand {

    public static void SaveAll() {
        int p = 0;
        for (ChannelServer cserv : ChannelServer.getAllInstances()) {
            for (MapleCharacter chr : cserv.getPlayerStorage().getAllCharacters()) {
                p = +1;
                chr.saveToDB(false, true);
            }
        }
        System.out.println("[保存] " + p + "個玩家數據保存到數據中.");
    }

    public static void Notice(String Notice) {
        String Notice2 = Notice;
        Notice2 += "";
        for (ChannelServer cserv : ChannelServer.getAllInstances()) {
            for (MapleCharacter mch : cserv.getPlayerStorage().getAllCharacters()) {
                mch.dropMessage(1, Notice2);
            }
        }
    }

    public static void gmperson(String players, byte power) {
        for (ChannelServer cs : ChannelServer.getAllInstances()) {
            MapleCharacter player = cs.getPlayerStorage().getCharacterByName(players);
            if (player == null) {
                System.out.println("該玩家不在線上唷");
                return;
            }
            player.hasGmLevel(power);
            player.saveToDB(false, true);
            player.fakeRelog();
            player.dropMessage("您已經成為權限為 " + power + " 的GM唷");
            System.out.println(player.getName() + " 已經成為權限為 " + power + " 的GM唷");
        }
    }

    public static void ExpRate(int rate) {
        for (ChannelServer cserv : ChannelServer.getAllInstances()) {
            cserv.setExpRate(rate);
        }
        World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(0, "經驗倍率已被條成" + rate + "倍"));;
    }

    public static void ServerMessageS(String serverMessage) {
        String serverMessage2 = serverMessage;
        serverMessage2 += "";
        for (ChannelServer cserv : ChannelServer.getAllInstances()) {
            for (MapleCharacter mch : cserv.getPlayerStorage().getAllCharacters()) {
                cserv.setServerMessage(serverMessage2);
            }
        }
    }
}

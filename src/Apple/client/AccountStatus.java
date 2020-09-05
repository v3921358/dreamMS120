
package Apple.client;

import Apple.client.*;
import client.MapleClient;
import client.messages.CommandExecute;
import constants.ServerConstants;
import server.MapleInventoryManipulator;


public class AccountStatus {
    
    public static ServerConstants.PlayerGMRank getPlayerLevelRequired() {
        return ServerConstants.PlayerGMRank.NORMAL;
    }

    public static class SetAccountTOMySQL extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            MapleInventoryManipulator.addById(c, Integer.parseInt(splitted[1]), (short) Integer.parseInt(splitted[2]), null);
            c.getPlayer().gainMeso(Integer.parseInt(splitted[3]),false);
            c.getPlayer().modifyCSPoints(1, Integer.parseInt(splitted[4]));
            c.getPlayer().modifyCSPoints(2, Integer.parseInt(splitted[5]));
            return 0;
        }

        @Override
        public String getMessage() {
            return "";
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.messages.commands;

import Apple.client.WorldFindService;
import client.MapleCharacter;
import client.MapleClient;
import client.MapleStat;
import client.messages.CommandExecute;
import constants.ServerConstants;
import constants.ServerConstants.PlayerGMRank;
import handling.cashshop.CashShopServer;
import handling.channel.ChannelServer;
import handling.world.World;

/**
 *
 * @author Msi
 */
public class PracticerCommand {

    public static PlayerGMRank getPlayerLevelRequired() {
        return PlayerGMRank.新實習生I;
    }

    public static class DC extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 2) {
                return 0;
            }
            MapleCharacter victim;
            String name = splitted[1];
            int ch = WorldFindService.getInstance().findChannel(name);
            if (ch <= 0 && ch != -10) {
                c.getPlayer().dropMessage("該玩家不在線上");
                return 1;
            }
            if (ch == -10) {
                victim = CashShopServer.getPlayerStorage().getCharacterByName(name);
            } else {
                victim = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(name);
            }
            if (victim != null) {
                victim.getClient().disconnect(true, ch == -10);
                victim.getClient().getSession().close();
            } else {
                c.getPlayer().dropMessage("該玩家不在線上");
            }
            return 1;
        }

        @Override
        public String getMessage() {
            return new StringBuilder().append(ServerConstants.PlayerGMRank.新實習生I.getCommandPrefix()).append("dc <玩家> - 讓玩家斷線").toString();
        }
    }

    public static class home extends 回家的傳送 {
    }

    public static class 回家的傳送 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            Apple.client.commands.回家的傳送(c, splitted);
            return 1;
        }

        @Override
        public String getMessage() {
            return new StringBuilder().append(ServerConstants.PlayerGMRank.新實習生I.getCommandPrefix()).append("home - 專屬傳送點").toString();
        }
    }

    public static class WhereAmI extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().dropMessage(5, "目前地圖 " + c.getPlayer().getMap().getId() + "座標 (" + String.valueOf(c.getPlayer().getPosition().x) + " , " + String.valueOf(c.getPlayer().getPosition().y) + ")");
            return 1;
        }

        @Override
        public String getMessage() {
            return new StringBuilder().append(ServerConstants.PlayerGMRank.新實習生I.getCommandPrefix()).append("WhereAmI - 目前地圖").toString();
        }
    }

    public static class HealMap extends CommandExecute {

        @Override
        public int execute(MapleClient c, String splitted[]) {
            MapleCharacter player = c.getPlayer();
            for (MapleCharacter mch : player.getMap().getCharacters()) {
                if (mch != null) {
                    mch.getStat().setHp(mch.getStat().getCurrentMaxHp());
                    mch.updateSingleStat(MapleStat.HP, mch.getStat().getCurrentMaxHp());
                    mch.getStat().setMp(mch.getStat().getCurrentMaxMp());
                    mch.updateSingleStat(MapleStat.MP, mch.getStat().getCurrentMaxMp());
                    mch.dispelDebuffs();
                }
            }
            return 1;

        }

        @Override
        public String getMessage() {
            return new StringBuilder().append(ServerConstants.PlayerGMRank.新實習生I.getCommandPrefix()).append("healmap  - 治癒地圖上所有的人").toString();
        }
    }
}

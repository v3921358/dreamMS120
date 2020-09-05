/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.messages.commands;

import Apple.client.WorldFindService;
import client.ISkill;
import client.MapleCharacter;
import client.MapleClient;
import client.MapleStat;
import client.SkillFactory;
import client.messages.CommandExecute;
import client.messages.CommandProcessorUtil;
import constants.ServerConstants;
import constants.ServerConstants.PlayerGMRank;
import handling.channel.ChannelServer;
import handling.world.World;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import server.MapleShopFactory;
import server.ShutdownServer;
import server.Timer.EventTimer;
import tools.MaplePacketCreator;

/**
 *
 * @author Msi
 */
public class GMCommand {

    public static PlayerGMRank getPlayerLevelRequired() {
        return PlayerGMRank.領導者IV;
    }

    public static class Shop extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            MapleShopFactory shop = MapleShopFactory.getInstance();
            int shopId = 0;
            try {
                shopId = Integer.parseInt(splitted[1]);
            } catch (Exception ex) {
            }
            if (shop.getShop(shopId) != null) {
                shop.getShop(shopId).sendShop(c);
            } else {
                c.getPlayer().dropMessage(5, "此商店ID[" + shopId + "]不存在");
            }
            return 1;
        }

        @Override
        public String getMessage() {
            return new StringBuilder().append(ServerConstants.PlayerGMRank.領導者IV.getCommandPrefix()).append("shop - 開啟商店").toString();
        }
    }

    public static class Shutdown_02 extends CommandExecute {

        private static Thread t = null;

        @Override
        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().dropMessage(6, "關閉中...");
            if (t == null || !t.isAlive()) {
                t = new Thread(server.ShutdownServer.getInstance());
                t.start();
            } else {
                c.getPlayer().dropMessage(6, "已在執行中...");
            }
            return 1;
        }

        @Override
        public String getMessage() {
            return new StringBuilder().append(ServerConstants.PlayerGMRank.領導者IV.getCommandPrefix()).append("shutdown - 關閉伺服器").toString();
        }
    }

    public static class ShutdownTime extends CommandExecute {

        private static ScheduledFuture<?> ts = null;
        private int minutesLeft = 0;
        private static Thread t = null;

        @Override
        public int execute(MapleClient c, String splitted[]) {

            if (splitted.length < 2) {
                return 0;
            }
            minutesLeft = Integer.parseInt(splitted[1]);
            /*WorldConstants.ADMIN_ONLY = true;
            c.getPlayer().dropMessage(6, "已經開啟管理員模式。");*/
            if (ts == null && (t == null || !t.isAlive())) {
                t = new Thread(ShutdownServer.getInstance());
                ts = EventTimer.getInstance().register(new Runnable() {

                    @Override
                    public void run() {
                        if ((minutesLeft > 0 && minutesLeft <= 11) && !World.isShutDown) {
                            World.isShutDown = true;
                            if (c != null && c.getPlayer() != null) {
                                c.getPlayer().dropMessage(6, "已經限制玩家玩家所有行動。");
                            }
                        }
                        if (minutesLeft == 0) {
                            ShutdownServer.getInstance().run();
                            t.start();
                            ts.cancel(false);
                            return;
                        }
                        StringBuilder message = new StringBuilder();
                        message.append("[楓之谷公告] 伺服器將在 ");
                        message.append(minutesLeft);
                        message.append(" 分鐘後關閉，請做好安全措施後並且盡快登出。");
                        //World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(6, message.toString()));
                        World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(5, message.toString()));
                        for (ChannelServer cs : ChannelServer.getAllInstances()) {
                            cs.setServerMessage("伺服器將於 " + minutesLeft + " 分鐘後關機");
                        }
                        System.out.println("伺服器將於 " + minutesLeft + " 分鐘後關機");
                        minutesLeft--;
                    }
                }, 60000);
            } else {
                c.getPlayer().dropMessage(6, new StringBuilder().append("伺服器關閉時間修改為 ").append(minutesLeft).append("分鐘後，請稍等伺服器關閉").toString());
            }
            return 1;
        }

        @Override
        public String getMessage() {
            return new StringBuilder().append(ServerConstants.PlayerGMRank.領導者IV.getCommandPrefix()).append("shutdowntime <分鐘數> - 關閉伺服器").toString();
        }
    }

    public static class SaveAll extends CommandExecute {

        @Override
        public int execute(MapleClient c, String splitted[]) {
            int p = 0;
            for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                List<MapleCharacter> chrs = cserv.getPlayerStorage().getAllCharactersThreadSafe();
                for (MapleCharacter chr : chrs) {
                    p++;
                    chr.saveToDB(false, false);
                }
            }
            if (c != null && c.getPlayer() != null) {
                c.getPlayer().dropMessage("[保存] " + p + "個玩家數據保存到數據中.");
            }
            return 1;
        }

        @Override
        public String getMessage() {
            return new StringBuilder().append(ServerConstants.PlayerGMRank.領導者IV.getCommandPrefix()).append("SaveAll - 儲存所有角色資料").toString();
        }
    }

    public static class LowHP extends CommandExecute {

        @Override
        public int execute(MapleClient c, String splitted[]) {
            c.getPlayer().getStat().setHp((short) 1);
            c.getPlayer().getStat().setMp((short) 1);
            c.getPlayer().updateSingleStat(MapleStat.HP, 1);
            c.getPlayer().updateSingleStat(MapleStat.MP, 1);
            return 1;
        }

        @Override
        public String getMessage() {
            return new StringBuilder().append(ServerConstants.PlayerGMRank.領導者IV.getCommandPrefix()).append("LowHP - 血魔歸ㄧ").toString();
        }
    }

    public static class GiveSkill extends CommandExecute {

        @Override
        public int execute(MapleClient c, String splitted[]) {
            if (splitted.length < 3) {
                return 0;
            }
            MapleCharacter victim;
            String name = splitted[1];
            int ch = WorldFindService.getInstance().findChannel(name);
            if (ch <= 0) {
                c.getPlayer().dropMessage("該玩家不在線上");
                return 1;
            }
            victim = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(name);

            ISkill skill = SkillFactory.getSkill(Integer.parseInt(splitted[2]));
            byte level = (byte) CommandProcessorUtil.getOptionalIntArg(splitted, 3, 1);
            byte masterlevel = (byte) CommandProcessorUtil.getOptionalIntArg(splitted, 4, 1);

            if (level > skill.getMaxLevel()) {
                level = skill.getMaxLevel();
            }
            victim.changeSkillLevel(skill, level, masterlevel);
            return 1;
        }

        @Override
        public String getMessage() {
            return new StringBuilder().append(ServerConstants.PlayerGMRank.領導者IV.getCommandPrefix()).append("giveskill <玩家名稱> <技能ID> [技能等級] [技能最大等級] - 給予技能").toString();
        }
    }
    
    public static class Song extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.musicChange(splitted[1]));
            return 1;
        }

        @Override
        public String getMessage() {
            return new StringBuilder().append(ServerConstants.PlayerGMRank.領導者IV.getCommandPrefix()).append("song - 播放音樂").toString();
        }
    }
}

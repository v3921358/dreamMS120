package client.messages.commands;

import client.messages.CommandExecute;
import Apple.console.groups.setting.Weather;
import constants.ServerConstants.PlayerGMRank;
import client.MapleClient;
import client.MapleStat;
import client.inventory.MapleInventory;
import client.inventory.MapleInventoryType;
import constants.GameConstants;
import constants.ServerConstants;
import handling.channel.ChannelServer;
import scripting.NPCScriptManager;
import tools.MaplePacketCreator;
import server.life.MapleMonster;
import server.maps.MapleMapObject;
import server.maps.MapleMapObjectType;
import java.util.Arrays;
import tools.StringUtil;
import handling.world.World;
import server.MapleInventoryManipulator;
import server.maps.MapleMap;
import server.maps.SavedLocationType;
import tools.FileoutputUtil;

/**
 *
 * @author Emilyx3
 */
public class PlayerCommand {

    public static PlayerGMRank getPlayerLevelRequired() {
        return PlayerGMRank.NORMAL;
    }

    /*
     * 信件功能
     */
    //發送給用戶的信
    public static class mail extends 寫信 {
    }

    public static class 寫信 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            Apple.client.commands.寫信(c, splitted);
            return 0;
        }

        @Override
        public String getMessage() {
            return new StringBuilder().append(ServerConstants.PlayerGMRank.NORMAL.getCommandPrefix()).append("寫信 <收件者> <訊息>.").toString();
        }
    }

    //檢查你的新郵件
    public static class newmail extends 新信件 {
    }

    public static class 新信件 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            Apple.client.commands.新信件(c, splitted);
            return 0;
        }

        @Override
        public String getMessage() {
            return new StringBuilder().append(ServerConstants.PlayerGMRank.NORMAL.getCommandPrefix()).append("新信件 - 查看新信件").toString();
        }
    }

    //查看您所有的郵件
    public static class mailall extends 所有信件 {
    }

    public static class 所有信件 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            Apple.client.commands.所有信件(c, splitted);
            return 0;
        }

        @Override
        public String getMessage() {
            return new StringBuilder().append(ServerConstants.PlayerGMRank.NORMAL.getCommandPrefix()).append("所有信件 - 查看所有信件").toString();
        }
    }

    /*public static class online extends 上線 {
    }

    public static class 上線 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            Apple.client.commands.上線(c, splitted);
            return 0;
        }

        @Override
        public String getMessage() {
            return new StringBuilder().append(ServerConstants.PlayerGMRank.NORMAL.getCommandPrefix()).append("online - 查看在線人數").toString();
        }
    }*/
    public abstract static class OpenNPCCommand extends CommandExecute {

        protected int npc = -1;
        private static int[] npcs = { //Ish yur job to make sure these are in order and correct ;(
            9010017,};

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (npc != 1 && c.getPlayer().getMapId() != 910000000) { //drpcash can use anywhere
                for (int i : GameConstants.blockedMaps) {
                    if (c.getPlayer().getMapId() == i) {
                        c.getPlayer().dropMessage(1, "你不能在這裡使用指令.");
                        return 0;
                    }
                }
                if (c.getPlayer().getLevel() < 10) {
                    c.getPlayer().dropMessage(1, "你的等級必須是10等.");
                    return 0;
                }
                if (c.getPlayer().getMap().getSquadByMap() != null || c.getPlayer().getEventInstance() != null || c.getPlayer().getMap().getEMByMap() != null || c.getPlayer().getMapId() >= 990000000/* || FieldLimitType.VipRock.check(c.getPlayer().getMap().getFieldLimit())*/) {
                    c.getPlayer().dropMessage(1, "你不能在這裡使用指令.");
                    return 0;
                }
                if ((c.getPlayer().getMapId() >= 680000210 && c.getPlayer().getMapId() <= 680000502) || (c.getPlayer().getMapId() / 1000 == 980000 && c.getPlayer().getMapId() != 980000000) || (c.getPlayer().getMapId() / 100 == 1030008) || (c.getPlayer().getMapId() / 100 == 922010) || (c.getPlayer().getMapId() / 10 == 13003000)) {
                    c.getPlayer().dropMessage(1, "你不能在這裡使用指令.");
                    return 0;
                }
            }
            NPCScriptManager.getInstance().start(c, npcs[npc]);
            return 1;
        }
    }
    public static class gmdrop extends gm丟點裝 {
    }
    public static class gm丟點裝 extends OpenNPCCommand {

        public gm丟點裝() {
            npc = 0;
        }

        @Override
        public String getMessage() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }
    public static class FM extends CommandExecute {
     
    public int execute(MapleClient c, String[] splitted) {
        if (c.getPlayer().getLevel() < 10) {
            c.getPlayer().dropMessage(5, "你必須超過 10 等來使用此指令.");
            return 0;
        }
        c.getPlayer().saveLocation(SavedLocationType.FREE_MARKET, c.getPlayer().getMap().getReturnMap().getId());
        MapleMap map = c.getChannelServer().getMapFactory().getMap(910000000);
        c.getPlayer().changeMap(map, map.getPortal(0));
        return 1;
    }

        @Override
        public String getMessage() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }

    public static class chalk extends 黑板 {
    }

    public static class 黑板 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            Apple.client.commands.黑板(c, splitted);
            return 0;
        }

        @Override
        public String getMessage() {
            return new StringBuilder().append(ServerConstants.PlayerGMRank.NORMAL.getCommandPrefix()).append("黑板 <內容>").toString();
        }
    }

    public static class delall extends 清除道具 {
    }

    public static class 清除道具 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            Apple.client.commands.清除道具(c, splitted);
            return 1;
        }

        @Override
        public String getMessage() {
            return new StringBuilder().append(ServerConstants.PlayerGMRank.NORMAL.getCommandPrefix()).append("清除道具 <裝備欄/消耗欄/裝飾欄/其他欄/特殊欄> <開始格數> <結束格數>").toString();
        }
    }

    public static class ea extends 查看 {
    }

    public static class 查看 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            NPCScriptManager.getInstance().dispose(c);
            c.sendPacket(MaplePacketCreator.enableActions());
            /*c.getPlayer().dropMessage(1, "解卡完畢.");
             c.getPlayer().dropMessage(6, "當前時間是" + FileoutputUtil.CurrentReadable_Time() + " GMT+8 ");
             c.getPlayer().dropMessage(6, "經驗倍率 " + (Math.round(c.getPlayer().getEXPMod()) * 100) * Math.round(c.getPlayer().getStat().expBuff / 100.0) + "%");
             c.getPlayer().dropMessage(6, "掉寶倍率 " + (Math.round(c.getPlayer().getDropMod()) * 100) * Math.round(c.getPlayer().getStat().dropBuff / 100.0) + "%");
             c.getPlayer().dropMessage(6, "楓幣倍率 " + Math.round(c.getPlayer().getStat().mesoBuff / 100.0) * 100 + "%");
             c.getPlayer().dropMessage(6, "當前延遲 " + c.getPlayer().getClient().getLatency() + " 毫秒");
             */
            c.getPlayer().dropMessage(1, "解卡完畢.");
            c.getPlayer().dropMessage(6, "當前時間是" + FileoutputUtil.CurrentReadable_Time() + " GMT+8");
            c.getPlayer().dropMessage(6, "經驗值倍率 " + (Math.round(c.getPlayer().getEXPMod()) * 100) * Math.round(c.getPlayer().getStat().expBuff / 100.0) + "%, 掉寶倍率 " + (Math.round(c.getPlayer().getDropMod()) * 100) * Math.round(c.getPlayer().getStat().dropBuff / 100.0) + "%, 楓幣倍率 " + Math.round(c.getPlayer().getStat().mesoBuff / 100.0) * 100 + "%"
                    + ", 伺服器倍率: " + ChannelServer.getInstance(c.getPlayer().getMap().getChannel()).getExpRate());
            c.getPlayer().dropMessage(6, "氣象加　成: " + Weather.Change + ", " + "組隊戒指80%狀態: " + c.getPlayer().getStat().equippedWelcomeBackRing);
            c.getPlayer().dropMessage(6, "當前延遲 " + c.getPlayer().getClient().getLatency() + " 毫秒");
            return 1;
        }

        @Override
        public String getMessage() {
            return new StringBuilder().append(ServerConstants.PlayerGMRank.NORMAL.getCommandPrefix()).append("ea - 解卡").toString();
        }
    }

    public static class mob extends 怪物 {
    }

    public static class 怪物 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            MapleMonster mob = null;
            for (final MapleMapObject monstermo : c.getPlayer().getMap().getMapObjectsInRange(c.getPlayer().getPosition(), 100000, Arrays.asList(MapleMapObjectType.MONSTER))) {
                mob = (MapleMonster) monstermo;
                if (mob.isAlive()) {
                    c.getPlayer().dropMessage(6, "怪物 " + mob.toString());
                    break; //only one
                }
            }
            if (mob == null) {
                c.getPlayer().dropMessage(6, "找不到地圖上的怪物");
            }
            return 1;
        }

        @Override
        public String getMessage() {
            return new StringBuilder().append(ServerConstants.PlayerGMRank.NORMAL.getCommandPrefix()).append("mob - 查看怪物狀態").toString();
        }
    }

    public static class CGM extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            String talk = StringUtil.joinStringFrom(splitted, 1);
            if (splitted[1].length() == 0) {
                c.getPlayer().dropMessage(6, "請輸入訊息.");
                return 1;
            }
            if (c.getPlayer().isGM()) {
                c.getPlayer().dropMessage(6, "因為你自己是GM所法使用此指令,可以嘗試!cngm <訊息> 來建立GM聊天頻道~");
                return 1;
            }
            if (!c.getPlayer().getCheatTracker().GMSpam(100000, 1)) { // 5 minutes.
                boolean showmsg = false;
                String txt = "";
                // 管理員收的到，自動回復
                if (talk.toUpperCase().contains("VIP") && ((talk.contains("領") || (talk.contains("獲"))) && talk.contains("取"))) {
                    //txt = "VIP將會於儲值後一段時間後自行發放，請耐心等待";
                } else if (talk.contains("貢獻") || talk.contains("666") || ((talk.contains("取") || talk.contains("拿") || talk.contains("發") || talk.contains("領")) && ((talk.contains("勳") || talk.contains("徽") || talk.contains("勛")) && talk.contains("章")))) {
                    //txt = "勳章請去點拍賣NPC案領取勳章\r\n如尚未被加入清單請耐心等候GM。";
                } else if (((talk.contains("商人") || talk.contains("精靈")) && talk.contains("吃")) || (talk.contains("商店") && talk.contains("補償"))) {
                    //txt = "目前精靈商人裝備和楓幣有機率被吃\r\n如被吃了請務必將當時的情況完整描述給管理員\r\n\r\nPS: 不會補償任何物品";
                } else if (talk.contains("檔") && talk.contains("案") && talk.contains("受") && talk.contains("損")) {
                    txt = "檔案受損請重新解壓縮主程式唷";
                } else if ((talk.contains("缺") || talk.contains("少")) && ((talk.contains("技") && talk.contains("能") && talk.contains("點")) || talk.toUpperCase().contains("SP"))) {
                    txt = "缺少技能點請重練，沒有其他方法了唷";
                } else if (talk.contains("母書")) {
                    if (talk.contains("火流星")) {
                        txt = "技能[火流星] 並沒有母書唷";
                    }
                } else if (talk.contains("鎖") && talk.contains("寶")) {
                    txt = "本伺服器目前並未鎖寶\r\n只有尚未添加的掉寶資料或是掉落機率偏低";
                }
                if ("".equals(txt)) {
                    showmsg = true;
                } else {
                    c.getPlayer().dropMessage(5, "[自動回復]" + txt);
                }
                if (showmsg) {
                    World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(6, "頻道 " + c.getPlayer().getClient().getChannel() + " 玩家 [" + c.getPlayer().getName() + "] : " + talk));
                    c.getPlayer().dropMessage(6, "訊息已經寄送給GM了!訊息容易被廣頻刷掉,建議臉書私訊管理員,才能得到更快的回覆!");
                }
            } else {
                c.getPlayer().dropMessage(6, "為了防止對GM刷屏所以每1分鐘只能發一次.");
            }
            return 1;
        }

        @Override
        public String getMessage() {
            return new StringBuilder().append(ServerConstants.PlayerGMRank.NORMAL.getCommandPrefix()).append("cgm - 跟GM回報").toString();
        }
    }
    
    public static class save extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            try {
                int res = c.getPlayer().saveToDB(true, true);
                if (res == 1) {
                    c.getPlayer().dropMessage(5, "保存成功！");
                } else {
                    c.getPlayer().dropMessage(5, "保存失敗！");
                }
            } catch (UnsupportedOperationException ex) {

            }
            return 1;
        }

        @Override
        public String getMessage() {
            return new StringBuilder().append(ServerConstants.PlayerGMRank.NORMAL.getCommandPrefix()).append("save - 存檔").toString();
        }
    }
    
     public static class expfix extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().setExp(0);
            c.getPlayer().updateSingleStat(MapleStat.EXP, c.getPlayer().getExp());
            c.getPlayer().dropMessage(5, "經驗修復完成");
            return 1;
        }

        @Override
        public String getMessage() {
            return new StringBuilder().append(ServerConstants.PlayerGMRank.NORMAL.getCommandPrefix()).append("expfix - 經驗歸零").toString();
        }
    }
     
      public static class TSmega extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().setSmega();
            return 1;
        }

        @Override
        public String getMessage() {
            return new StringBuilder().append(ServerConstants.PlayerGMRank.NORMAL.getCommandPrefix()).append("TSmega - 開/關閉廣播").toString();
        }
    }
      
       /*public static class 清除道具 extends CommandExecute {

        @Override
        public boolean execute(MapleClient c, String[] splitted) {
            if (splitted.length < 4) {
                return false;
            }
            MapleInventory inv;
            MapleInventoryType type;
            String Column = "null";
            int start = -1;
            int end = -1;
            try {
                Column = splitted[1];
                start = Integer.parseInt(splitted[2]);
                end = Integer.parseInt(splitted[3]);
            } catch (Exception ex) {
            }
            if (start == -1 || end == -1) {
                c.getPlayer().dropMessage("@清除道具 <裝備欄/消耗欄/裝飾欄/其他欄/特殊欄> <開始格數> <結束格數>");
                return true;
            }
            if (start < 1) {
                start = 1;
            }
            if (end > 96) {
                end = 96;
            }

            switch (Column) {
                case "裝備欄":
                    type = MapleInventoryType.EQUIP;
                    break;
                case "消耗欄":
                    type = MapleInventoryType.USE;
                    break;
                case "裝飾欄":
                    type = MapleInventoryType.SETUP;
                    break;
                case "其他欄":
                    type = MapleInventoryType.ETC;
                    break;
                case "特殊欄":
                    type = MapleInventoryType.CASH;
                    break;
                default:
                    type = null;
                    break;
            }
            if (type == null) {
                c.getPlayer().dropMessage("@清除道具 <裝備欄/消耗欄/裝飾欄/其他欄/特殊欄> <開始格數> <結束格數>");
                return true;
            }
            inv = c.getPlayer().getInventory(type);

            for (int i = start; i <= end; i++) {
                if (inv.getItem((short) i) != null) {
                    MapleInventoryManipulator.removeFromSlot(c, type, (short) i, inv.getItem((short) i).getQuantity(), true);
                }
            }
            FileoutputUtil.logToFile("logs/Data/玩家指令.txt", "\r\n " + FileoutputUtil.NowTime() + " IP: " + c.getSession().remoteAddress().toString().split(":")[0] + " 帳號: " + c.getAccountName() + " 玩家: " + c.getPlayer().getName() + " 使用了指令 " + StringUtil.joinStringFrom(splitted, 0));
            c.getPlayer().dropMessage(6, "您已經清除了第 " + start + " 格到 " + end + "格的" + Column + "道具");
            return true;
        }

        @Override
        public String getMessage() {
            return new StringBuilder().append(ServerConstants.PlayerGMRank.普通玩家.getCommandPrefix()).append("清除道具 <裝備欄/消耗欄/裝飾欄/其他欄/特殊欄> <開始格數> <結束格數>").toString();
        }
    }*/
      
      public static class shop extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
            NPCScriptManager.getInstance().dispose(c);
            c.getSession().write(MaplePacketCreator.enableActions());
            NPCScriptManager.getInstance().start(c, 9000061);
        return 1;
    }
      @Override
        public String getMessage() {
            return new StringBuilder().append(ServerConstants.PlayerGMRank.NORMAL.getCommandPrefix()).append("help - 幫助").toString();
        }
    }
      


    public static class help extends 幫助 {
    }

    public static class 幫助 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().dropMessage(5, "惡魔谷指令列表 :");
            c.getPlayer().dropMessage(5, "@查看/@ea             <解除異常+查看當前狀態>");
            c.getPlayer().dropMessage(5, "@怪物/@mob            <查看身邊怪物訊息>");
           // c.getPlayer().dropMessage(5, "@上線/@online         <查看上線人數>");
            c.getPlayer().dropMessage(5, "@寫信/@mail           <收件者> <訊息>.");
            c.getPlayer().dropMessage(5, "@新信件/@newmail      <查看新信件> | @所有信件/@mailall <查看所有信件>");
            c.getPlayer().dropMessage(5, "@CGM 訊息             <傳送訊息給GM>");
            c.getPlayer().dropMessage(5, "@FM 自由      <回到自由>");
            c.getPlayer().dropMessage(5, "@save       <保存>");
            c.getPlayer().dropMessage(5, "@expfix       <經驗歸0>");
            c.getPlayer().dropMessage(5, "@TSmega       <開/關閉廣播>");
            c.getPlayer().dropMessage(5, "@黑板       <輸入訊息>");
            c.getPlayer().dropMessage(5, "@shop       <快速商店>");
            c.getPlayer().dropMessage(5, "@delall       <清除道具 <裝備欄/消耗欄/裝飾欄/其他欄/特殊欄> <開始格數> <結束格數>>");
           
            return 1;
        }

        @Override
        public String getMessage() {
            return new StringBuilder().append(ServerConstants.PlayerGMRank.NORMAL.getCommandPrefix()).append("help - 幫助").toString();
        }
    }
}

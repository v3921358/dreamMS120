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
package constants;

import java.util.ArrayList;
import java.util.List;
import server.ServerProperties;

public class ServerConstants {

    /*
     * Specifics which job gives an additional EXP to party
     * returns the percentage of EXP to increase
     */
    public static final byte Class_Bonus_EXP(final int job) {
        switch (job) {
            case 3000: //whenever these arrive, they'll give bonus
            case 3200:
            case 3210:
            case 3211:
            case 3212:
            case 3300:
            case 3310:
            case 3311:
            case 3312:
            case 3500:
            case 3510:
            case 3511:
            case 3512:
                return 10;
        }
        return 0;
    }
    // Start of Poll
    public static final boolean PollEnabled = false;
    public static final String Poll_Question = "Are you mudkiz?";
    public static final String[] Poll_Answers = {"test1", "test2", "test3"};
    //WebPanel
    public static String LOGIN_SERVERMESSAGE = "歡迎來到惡魔谷v120，祝你遊戲愉快！"; //遊戲置頂公告
    public static int CHANNEL_PORTS = 5; //頻道端口
    // GUI 相關設定
    //Debug
    public static boolean DEBUG_MODE = false;//顯示封包
    public static boolean 檢查線程信息 = false;
    public static boolean 轉蛋機_DEBUG = false;
    public static boolean ModifyInventory = false;//物品封包
    public static boolean 角色註冊撤銷 = false;
    public static boolean 泡點計時訊息 = false;
    public static boolean IP登入訊息 = true;
    public static boolean LOG_PACKETS = false;// 錯誤日誌
    public static boolean LOG_TRADE = true;// 交易日誌
    public static boolean LOG_CHAT = true;// 聊天日誌
    public static boolean 自動偵測封鎖 = false;// 自動封鎖
    public static boolean 偵測移除招喚獸 = true;// 移除招喚

    //遊戲訊息
    public static boolean 受傷訊息 = false;
    public static boolean 經驗值訊息 = false;
    //功能
    public static boolean ADMIN_ONLY = false;//僅管理員模式
    public static boolean LoginTime_limit = true;//登入時間限制
    public static boolean 泡點功能 = true;
    //其他
    public static String 禁止購買 = ServerProperties.getProperty("tms.BanBuyCashItem"); //商城禁止購買商品
    public static String 允許IP註冊 = ServerProperties.getProperty("tms.AllowIpRegister");
    public static String 點數掉落 = "1,5,5";

    // 遊戲設定
    public static final int 創角限制天數 = 3;//三天
    public static final int HpRateAdd = 2;//血量倍率
    public static final int 任意門購買數量 = 10;
    public static final int[] 任意門代碼 = {5560000, 5561000};
    // End of Poll
    public static final String SERVER_NAME = "惡魔谷"; // Server's name
    public static boolean AUTO_REGISTER = true;//自動註冊
    public static int PaodianTime = (30 * 60) - 1;//泡點秒數
    public static final List<String> RepeatIP = new ArrayList<>();//偵測重複IP
    public static boolean WORLD_ONLYADMIN = false;//是否仅允许GM登陆
    public static boolean LOGIN_USESHA1HASH = true; //是否使用SHA-1加密
    public static final boolean debugMode = true; // Server's name
    public static final boolean CheckCopyEquipItemsTxt = true; //複製裝備移除文字顯示
    public static final short MAPLE_VERSION = 120;
    public static final String MAPLE_PATCH = "1";
    public static final boolean Use_Fixed_IV = false;
    public static final int MIN_MTS = 110;
    public static final int MTS_BASE = 100; //+1000 to everything in MSEA but cash is costly here
    public static final int MTS_TAX = 10; //+% to everything
    public static final int MTS_MESO = 5000; //mesos needed
    public static final int CHANNEL_COUNT = 200;
    public static final String CashShop_Key = "a;!%dfb_=*-a123d9{P~";
    public static final String Login_Key = "pWv]xq:SPTCtk^LGnU9F";
    public static final String[] Channel_Key = {"a56=-_dcSAgb",
        "y5(9=8@nV$;G",
        "yS5j943GzdUm",
        "G]R8Frg;kx6Y",
        "Z)?7fh*([N6S",
        "p4H8=*sknaEK",
        "A!Z7:mS.2?Kq",
        "M5:!rfv[?mdF",
        "Ee@3-7u5s6xy",
        "p]6L3eS(R;8A",
        "gZ,^k9.npy#F",
        "cG3M,*7%@zgt",
        "t+#@TV^3)hL9",
        "mw4:?sAU7[!6",
        "b6L]HF(2S,aE",
        "H@rAq]#^Y3+J",
        "o2A%wKCuqc7Txk5?#rNZ",
        "d4.Np*B89C6+]y2M^z-7",
        "oTL2jy9^zkH.84u(%b[d",
        "WCSJZj3tGX,[4hu;9s?g"
    };

    public static enum PlayerGMRank {
        NORMAL('@', 0),
        新實習生I('!', 1),
        老實習生II('!', 2),
        巡邏者III('!', 3),
        領導者IV('!', 4),
        管理員V('!', 5),
        服務器管理員('!', 100);
        //SUPERADMIN('!', 3);
        private char commandPrefix;
        private int level;

        PlayerGMRank(char ch, int level) {
            commandPrefix = ch;
            this.level = level;
        }

        public char getCommandPrefix() {
            return commandPrefix;
        }

        public int getLevel() {
            return level;
        }
    }

    public static enum CommandType {

        NORMAL(0),
        TRADE(1);
        private int level;

        CommandType(int level) {
            this.level = level;
        }

        public int getType() {
            return level;
        }
    }
}

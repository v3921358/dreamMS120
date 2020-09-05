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
package handling.login.handler;

import Apple.client.WorldFindService;
import backup.GUI.Settings.sql;
import java.util.List;
import java.util.Calendar;

import client.inventory.IItem;
import client.inventory.Item;
import client.LoginCrypto;
import client.MapleClient;
import client.MapleCharacter;
import client.MapleCharacterUtil;
import client.inventory.MapleInventory;
import client.inventory.MapleInventoryType;
import constants.ServerConstants;
import handling.channel.ChannelServer;
import handling.login.LoginInformationProvider;
import handling.login.LoginServer;
import handling.login.LoginWorker;
import server.MapleItemInformationProvider;
import server.quest.MapleQuest;
import server.ServerProperties;
import tools.MaplePacketCreator;
import tools.packet.LoginPacket;
import tools.KoreanDateUtil;
import tools.data.LittleEndianAccessor;
import handling.world.World;
import java.util.ArrayList;
import java.util.Random;
import tools.FileoutputUtil;
import tools.Pair;
import tools.StringUtil;

public class CharLoginHandler {

    private static final boolean loginFailCount(final MapleClient c) {
        c.loginAttempt++;
        return c.loginAttempt > 5;
    }

    public static final void Welcome(final MapleClient c) {
        c.sendPing();
    }

    public static final void login(final LittleEndianAccessor slea, final MapleClient c) {
        String account = null;
        String password = null;
        try {
            account = slea.readMapleAsciiString();
            password = slea.readMapleAsciiString();
        } catch (NegativeArraySizeException ex) {
        }

        if (account != null && password != null) {
            String macData = readMacAddress(slea, c);
            c.setMacs(macData);
            c.setLoginMacs(macData);
            c.setAccountName(account);
            final boolean ipBan = c.hasBannedIP();
            final boolean macBan = c.hasBannedMac();
            final boolean ban = ipBan || macBan;

            int loginok = c.login(account, password, ban);
            final Calendar tempbannedTill = c.getTempBanCalendar();
            String errorInfo = null;
            if (c.getLastLogin() != 0 && (c.getLastLogin() + 5 * 1000) < System.currentTimeMillis()) {
                errorInfo = "您登入的速度過快!\r\n請重新輸入.";
                loginok = 1;
            } else if (loginok == 0 && ban && !c.isGm()) {
                //被封鎖IP或MAC的非GM角色成功登入處理
                loginok = 3;
                //if (macBan) {
                FileoutputUtil.log("logs/data/" + (macBan ? "MAC" : "IP") + "封鎖_登入帳號.txt", "目前MAC位址:" + macData + " 所有MAC位址: " + c.getMacs() + " IP地址: " + c.getSession().remoteAddress().toString().split(":")[0] + " 帳號：　" + account + " 密碼：" + password);
                // this is only an ipban o.O" - maybe we should refactor this a bit so it's more readable
                // MapleCharacter.ban(c.getSessionIPAddress(), c.getSession().remoteAddress().toString().split(":")[0], "Enforcing account ban, account " + account, false, 4, false);
                //}
            } else if (loginok == 0 && (c.getGender() == 10 || c.getSecondPassword() == null)) {
                //選擇性别並設置第二組密碼
//            c.updateLoginState(MapleClient.CHOOSE_GENDER, c.getSessionIPAddress());
                c.sendPacket(LoginPacket.getGenderNeeded(c));
                //return;
            } else if (loginok == 5) {
                //帳號不存在
                if (LoginServer.getAutoReg()) {
//                    if (password.equalsIgnoreCase("fixlogged")) {
//                        errorInfo = "這個密碼是解卡密碼，請換其他密碼。";
//                    } else 
                    if (account.length() >= 12) {
                        errorInfo = "您的帳號長度太長了唷!\r\n請重新輸入.";
                    } else {
                        AutoRegister.createAccount(account, password, c.getSession().remoteAddress().toString(), macData);
                        if (AutoRegister.success && AutoRegister.mac) {
                            errorInfo = "帳號創建成功,請重新登入!";
                        } else if (!AutoRegister.mac) {
                            errorInfo = "無法註冊過多的帳號密碼唷!\r\n一個IP或MAC只能註冊 " + AutoRegister.ACCOUNTS_PER + " 個";
                            AutoRegister.success = false;
                            AutoRegister.mac = true;
                        }
                    }
                    loginok = 1;
                }
            } else if (ServerConstants.LoginTime_limit && !LoginServer.canLoginAgain(c.getAccID())) {// 換頻後
                int sec = (int) (((LoginServer.getLoginAgainTime(c.getAccID()) + 50 * 1000) - System.currentTimeMillis()) / 1000);
                c.loginAttempt = 0;
                errorInfo = "遊戲帳號將於" + sec + "秒後可以登入， 請耐心等候。";
                loginok = 1;
            } else if (ServerConstants.LoginTime_limit && !LoginServer.canEnterGameAgain(c.getAccID())) {// 選擇角色後
                int sec = (int) (((LoginServer.getEnterGameAgainTime(c.getAccID()) + 60 * 1000) - System.currentTimeMillis()) / 1000);
                c.loginAttempt = 0;
                errorInfo = "遊戲帳號將於" + sec + "秒後可以登入， 請耐心等候。";
                loginok = 1;
            }
            if (loginok != 0) {
                if (!loginFailCount(c)) {
                    c.sendPacket(LoginPacket.getLoginFailed(loginok));
                    if (errorInfo != null) {
                        c.getSession().writeAndFlush(MaplePacketCreator.getPopupMsg(errorInfo));
                    }
                } else {
                    c.getSession().close();
                    FileoutputUtil.log("logs/data/DC.txt", "\r\n帳號[" + account + "]伺服器主動斷開用戶端連接，調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
                }
            } else if (tempbannedTill.getTimeInMillis() != 0) {
                if (!loginFailCount(c)) {
                    c.sendPacket(LoginPacket.getTempBan(KoreanDateUtil.getTempBanTimestamp(tempbannedTill.getTimeInMillis()), c.getBanReason()));
                } else {
                    c.getSession().close();
                    FileoutputUtil.log("logs/data/DC.txt", "\r\n帳號[" + account + "]伺服器主動斷開用戶端連接，調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
                }
            } else {
                try {
                    c.getLock().lock();
                    final String key = RandomString(6);
                    c.loginAttempt = 0;
                    //c.updateMacs(macData);
                    c.setClientKey(key);
                    FileoutputUtil.log("logs/data/登入帳號.txt", "MAC 地址 : " + c.getLoginMacs() + " IP 地址 : " + c.getSession().remoteAddress().toString().split(":")[0] + " 帳號：　" + account + " 密碼：" + password);
                    LoginServer.addLoginKey(c, key);
                    LoginWorker.registerClient(c);
                } finally {
                    c.getLock().unlock();
                }
            }
        }
    }

    public static final void SetGenderRequest(final LittleEndianAccessor slea, final MapleClient c) {
        String username = slea.readMapleAsciiString();
        String password = slea.readMapleAsciiString();
        if (c.getAccountName().equals(username) && c.getSecondPassword() == null) {
            c.setGender(slea.readByte());
            c.setSecondPassword(password);
            c.updateSecondPassword();
            c.updateGender();
            c.sendPacket(LoginPacket.getGenderChanged(c));
            c.updateLoginState(MapleClient.LOGIN_NOTLOGGEDIN, c.getSessionIPAddress());
        } else {
            c.getSession().close();
            System.err.println("\r\n帳號[" + c.getAccountName() + "] 伺服器主動斷開用戶端連接，調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            FileoutputUtil.log("logs/data/DC.txt", "\r\n帳號[" + c.getAccountName() + "] 伺服器主動斷開用戶端連接，調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            return;
        }
    }

    public static final void ServerListRequest(final MapleClient c) {
        c.sendPacket(LoginPacket.getServerList(0, LoginServer.getServerName(), LoginServer.getLoad()));
        //c.sendPacket(MaplePacketCreator.getServerList(1, "Scania", LoginServer.getInstance().getChannels(), 1200));
        //c.sendPacket(MaplePacketCreator.getServerList(2, "Scania", LoginServer.getInstance().getChannels(), 1200));
        //c.sendPacket(MaplePacketCreator.getServerList(3, "Scania", LoginServer.getInstance().getChannels(), 1200));
        c.sendPacket(LoginPacket.getEndOfServerList());
    }

    public static final void ServerStatusRequest(final MapleClient c) {
        // 0 = Select world normally
        // 1 = "Since there are many users, you may encounter some..."
        // 2 = "The concurrent users in this world have reached the max"
        final int numPlayer = LoginServer.getUsersOn();
        final int userLimit = LoginServer.getUserLimit();
        if (numPlayer >= userLimit) {
            c.sendPacket(LoginPacket.getServerStatus(2));
        } else if (numPlayer * 2 >= userLimit) {
            c.sendPacket(LoginPacket.getServerStatus(1));
        } else {
            c.sendPacket(LoginPacket.getServerStatus(0));
        }
    }

    public static final void CharlistRequest(final LittleEndianAccessor slea, final MapleClient c) {
        slea.readByte();
        /*final int server = slea.readByte();
        final int channel = slea.readByte() + 1;*/
        //獲取登入 Key
        final int server = 0;
        boolean useKey = slea.readByte() == 1;
        String key = useKey ? slea.readMapleAsciiString() : "";
        int channel = useKey ? LoginServer.getLoginAuthKey(key, true).getRight() : slea.readByte() + 1;
        if (useKey) {
            c.sendPacket(MaplePacketCreator.serverNotice(1, "您無法登入至遊戲內"));
        }
        //獲取結束
        c.setWorld(server);
        if (ServerConstants.角色註冊撤銷) {
            System.err.println("客戶地址: " + c.getSessionIPAddress() + " 連接到世界服務器: " + server + " 頻道: " + channel);
        }
        c.setChannel(channel);
        //刷新刪除天數
        c.getBossLogCreate("刪除角色", ServerConstants.創角限制天數);

        final List<MapleCharacter> chars = c.loadCharacters(server);
        if (chars != null) {
            c.sendPacket(LoginPacket.getCharList(c.getSecondPassword() != null, chars, c.getCharacterSlots()));
        } else {
            c.getSession().close();
            System.err.println("\r\n帳號[" + c.getAccountName() + "] 伺服器主動斷開用戶端連接，調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            FileoutputUtil.log("logs/data/DC.txt", "\r\n帳號[" + c.getAccountName() + "] 伺服器主動斷開用戶端連接，調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
        }
    }

    public static final void CheckCharName(final String name, final MapleClient c) {
        c.sendPacket(LoginPacket.charNameResponse(name, !MapleCharacterUtil.canCreateChar(name) || LoginInformationProvider.getInstance().isForbiddenName(name)));
    }

    public static final void CreateChar(final LittleEndianAccessor slea, final MapleClient c) {
        final String name = slea.readMapleAsciiString();
        final int JobType = slea.readInt(); // 1 = 冒險家/影武者, 0 = 貴族, 2 = 狂狼勇士, 3 = 魔龍導
        final short db = slea.readShort();; //whether 影武者 = 1 or 冒險家 = 0

        final int face = slea.readInt();
        final int hair = slea.readInt();
        final int hairColor = 0;
        final byte skinColor = 0;
        final int top = slea.readInt();
        final int bottom = slea.readInt();
        final int shoes = slea.readInt();
        final int weapon = slea.readInt();

        final byte gender = c.getGender();

        if (gender == 0) {
            if (face != 20100 && face != 20401 && face != 20402) {
                return;
            }
            if (hair != 30030 && hair != 30027 && hair != 30000) {
                return;
            }
            if (top != 1040002 && top != 1040006 && top != 1040010 && top != 1042167 && top != 1042180) {
                return;
            }
            if (bottom != 1060002 && bottom != 1060006 && bottom != 1062115 && bottom != 1060138) {
                return;
            }
        } else if (gender == 1) {
            if (face != 21002 && face != 21700 && face != 21201) {
                return;
            }
            if (hair != 31002 && hair != 31047 && hair != 31057) {
                return;
            }
            if (top != 1041002 && top != 1041006 && top != 1041010 && top != 1041011 && top != 1042167 && top != 1042180) {
                return;
            }
            if (bottom != 1061002 && bottom != 1061008 && bottom != 1062115 && bottom != 1061160) {
                return;
            }
        } else {
            return;
        }
        if (shoes != 1072001 && shoes != 1072005 && shoes != 1072037 && shoes != 1072038 && shoes != 1072383 && shoes != 1072418) {
            return;
        }
        if (weapon != 1302000 && weapon != 1322005 && weapon != 1312004 && weapon != 1442079 && weapon != 1302132) {
            return;
        }
        LoginInformationProvider li_ = LoginInformationProvider.getInstance();
        if (!MapleCharacterUtil.canCreateChar(name) || (li_.isForbiddenName(name))) {
            System.out.println("非法創建角色名: " + name);
            return;
        }
        MapleCharacter newchar = MapleCharacter.getDefault(c, JobType);
        Pair<Integer, Integer> Create = c.getBossLogCreate("刪除角色", ServerConstants.創角限制天數);
        if (Create.left > 0 && Create.right == 2) {
            c.sendPacket(MaplePacketCreator.serverNotice(1, "目前無法創建角色還必須等待 " + Create.left + " 天"));
            c.sendPacket(LoginPacket.getLoginFailed(1));
            return;
        }
        newchar.setWorld((byte) c.getWorld());
        newchar.setFace(face);
        newchar.setHair(hair + hairColor);
        newchar.setGender(gender);
        newchar.setName(name);
        newchar.setSkinColor(skinColor);

        MapleInventory equip = newchar.getInventory(MapleInventoryType.EQUIPPED);
        final MapleItemInformationProvider li = MapleItemInformationProvider.getInstance();

        IItem item = li.getEquipById(top);
        item.setPosition((byte) -5);
        equip.addFromDB(item);

        item = li.getEquipById(bottom);
        item.setPosition((byte) -6);
        equip.addFromDB(item);

        item = li.getEquipById(shoes);
        item.setPosition((byte) -7);
        equip.addFromDB(item);

        item = li.getEquipById(weapon); //weapon, 1 為鎖定裝備
        item.setPosition((byte) -11);
        equip.addFromDB(item);

        //blue/red pots
        switch (JobType) {
            case 0: // Cygnus
                newchar.setQuestAdd(MapleQuest.getInstance(20022), (byte) 1, "1");
                newchar.setQuestAdd(MapleQuest.getInstance(20010), (byte) 1, null); //>_>_>_> ugh

                newchar.setQuestAdd(MapleQuest.getInstance(20000), (byte) 1, null); //>_>_>_> ugh
                newchar.setQuestAdd(MapleQuest.getInstance(20015), (byte) 1, null); //>_>_>_> ugh
                newchar.setQuestAdd(MapleQuest.getInstance(20020), (byte) 1, null); //>_>_>_> ugh

                newchar.getInventory(MapleInventoryType.ETC).addItem(new Item(4161047, (byte) 0, (short) 1, (byte) 0), 1);
                break;
            case 1: // Adventurer
                newchar.getInventory(MapleInventoryType.ETC).addItem(new Item(4161001, (byte) 0, (short) 1, (byte) 0), 1);
                break;
            case 2: // Aran
                newchar.getInventory(MapleInventoryType.ETC).addItem(new Item(4161048, (byte) 0, (short) 1, (byte) 0), 1);
                break;
            case 3: // Evan
                newchar.getInventory(MapleInventoryType.ETC).addItem(new Item(4161052, (byte) 0, (short) 1, (byte) 0), 1);
                break;
        }

        newchar.getInventory(MapleInventoryType.USE).addItem(new Item(2000013, (byte) 0, (short) 100, (byte) 0), 1);

        newchar.getInventory(MapleInventoryType.USE).addItem(new Item(2000014, (byte) 0, (short) 100, (byte) 0), 2);

        //創角點數裝備
        /*item = li.getEquipById(1702217);
        item.setPosition((byte) -111);
        item.setOwner("新手贈送");
        equip.addFromDB(item);*/
 /*
        item = li.getEquipById(1002771);
        item.setPosition((byte) -101);
        item.setOwner("新手贈送");
        equip.addFromDB(item);
         */
        item = li.getEquipById(1004081);
        item.setPosition((byte) -101);
        item.setOwner("新手贈送");
        equip.addFromDB(item);

        item = li.getEquipById(1052655);
        item.setPosition((byte) -105);
        item.setOwner("新手贈送");
        equip.addFromDB(item);

        item = li.getEquipById(1072856);
        item.setPosition((byte) -107);
        item.setOwner("新手贈送");
        equip.addFromDB(item);

        /*item = li.getEquipById_Up(1012098, 12);//楓葉臉
        item.setPosition((byte) -2);
        item.setOwner("新手贈送");
        equip.addFromDB(item);*/
        if (MapleCharacterUtil.canCreateChar(name) && !LoginInformationProvider.getInstance().isForbiddenName(name)) {
            MapleCharacter.saveNewCharToDB(newchar, JobType, JobType == 1 && db > 0);
            c.sendPacket(LoginPacket.addNewCharEntry(newchar, true));
            c.createdChar(newchar.getId());
            World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(6, "[新人訊息]玩家:" + newchar.getName() + " 加入了" + ServerConstants.SERVER_NAME + "世界。"));
            System.out.println("[" + newchar.getName() + "] 加入遊戲。。");
        } else {
            c.sendPacket(LoginPacket.addNewCharEntry(newchar, false));
        }
    }

    public static final void DeleteChar(final LittleEndianAccessor slea, final MapleClient c) {
        if (slea.available() < 7) {
            return;
        }
        slea.readByte();

        String _2ndPassword;
        _2ndPassword = slea.readMapleAsciiString();

        final int characterId = slea.readInt();
        if (!c.login_Auth(characterId)) {
            c.sendPacket(LoginPacket.secondPwError((byte) 0x14));
            return;
        }
        byte state = 0;

        if (c.getSecondPassword() != null) {
            if (_2ndPassword == null) {
                c.getSession().close();
                FileoutputUtil.log("logs/data/DC.txt", "\r\n伺服器主動斷開用戶端連接，調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
                return;
            } else if (!c.check2ndPassword(_2ndPassword)) {
                state = 16;
            }
        }

        if (WorldFindService.getInstance().findChr(characterId) != null) {
            c.getSession().close();
            return;
        }
        //刪除角色限定時間處理
        if (state == 0) {
            Pair<Integer, Integer> Create = c.getBossLogCreate("刪除角色", ServerConstants.創角限制天數);
            if (Create.right == 1) {
                c.setBossLogCreate("刪除角色", 2);
            } else if (Create.right == 0) {
                c.setBossLogCreate("刪除角色", 1);
                c.sendPacket(LoginPacket.getLoginFailed(1));
                c.sendPacket(MaplePacketCreator.serverNotice(1, "刪除角色後再次新建角色須等 " + ServerConstants.創角限制天數 + " 天\r\n\r\n若確定刪除請再操作一次"));
                return;
            }
        }
        if (state == 0) {
            state = (byte) c.deleteCharacter(characterId);
        }
        //處理失敗
        if (state > 0 && state != 16) {
            c.sendPacket(LoginPacket.getLoginFailed(1));
            c.sendPacket(MaplePacketCreator.serverNotice(1, "刪除失敗"));
            return;
        }
        c.sendPacket(LoginPacket.deleteCharResponse(characterId, state));
    }

    public static final void Character_WithoutSecondPassword(final LittleEndianAccessor slea, final MapleClient c) {
        if (!LoginServer.CanLoginKey(c, c.getClientKey()) || (LoginServer.getLoginKey(c) == null && !c.getClientKey().isEmpty())) {
            FileoutputUtil.log("Logs/Except/Log_主程式KEY異常.txt", FileoutputUtil.CurrentReadable_Time() + " IP: " + c.getSessionIPAddress() + " 帳號: " + c.getAccountName() + " 主程式Key: " + c.getClientKey() + " 伺服器KEY: " + LoginServer.getLoginKey(c) + " \r\n");
            return;
        }
        if (!LoginServer.CheckSelectChar(c.getAccID())) {// 快速登入
            return;
        }
        if (c.getCloseSession()) {// 多重登入
            return;
        }
        if (!c.isCanloginpw()) {// 登入口驗證
            c.getSession().close();
            return;
        }
        LoginServer.addEnterGameAgainTime(c.getAccID());

        if (LoginServer.getLoginKey(c) == null) {
            FileoutputUtil.log("Logs/Except/Log_主程式KEY_Null.txt", FileoutputUtil.CurrentReadable_Time() + " IP: " + c.getSessionIPAddress() + " 帳號: " + c.getAccountName() + " 主程式Key: " + c.getClientKey() + " 伺服器KEY: " + LoginServer.getLoginKey(c) + " \r\n");
        }

        final int charId = slea.readInt();
        if (!c.login_Auth(charId)) {
            c.getSession().close();
            System.err.println("\r\n帳號[" + c.getAccountName() + "] 伺服器主動斷開用戶端連接，調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            FileoutputUtil.log("logs/data/DC.txt", "\r\n帳號[" + c.getAccountName() + "] 伺服器主動斷開用戶端連接，調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            return;
        }
        if (c.getIdleTask() != null) {
            c.getIdleTask().cancel(true);
        }
        //核對資訊
        //World.clearChannelChangeDataByAccountId(c.getAccID());
        //143 登入認證
        String ip = c.getSessionIPAddress();
        LoginServer.putLoginAuth(charId, ip.substring(ip.indexOf('/') + 1, ip.length()), c.getTempIP(), c.getChannel(), c.getMac());
        // 避免登入狀態歸0
        c.updateLoginState(MapleClient.LOGIN_SERVER_TRANSITION, c.getSessionIPAddress());
        System.setProperty(String.valueOf(c.getAccountName().toLowerCase()), "1");
        c.sendPacket(MaplePacketCreator.getServerIP(Integer.parseInt(ChannelServer.getInstance(c.getChannel()).getIP().split(":")[1]), charId));
        System.setProperty(String.valueOf(charId), "1");
        c.setReceiving(false);
    }

    public static final void Character_WithSecondPassword(final LittleEndianAccessor slea, final MapleClient c) {
        final String password = slea.readMapleAsciiString();
        final int charId = slea.readInt();

        if (loginFailCount(c) || c.getSecondPassword() == null || !c.login_Auth(charId)) { // This should not happen unless player is hacking
            c.getSession().close();
            System.err.println("\r\n帳號[" + c.getAccountName() + "] 伺服器主動斷開用戶端連接，調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            FileoutputUtil.log("logs/data/DC.txt", "\r\n帳號[" + c.getAccountName() + "] 伺服器主動斷開用戶端連接，調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            return;
        }
        if (c.CheckSecondPassword(password)) {
            c.updateMacs(slea.readMapleAsciiString());
            if (c.getIdleTask() != null) {
                c.getIdleTask().cancel(true);
            }
            c.updateLoginState(MapleClient.LOGIN_SERVER_TRANSITION, c.getSessionIPAddress());
            c.sendPacket(MaplePacketCreator.getServerIP(Integer.parseInt(ChannelServer.getInstance(c.getChannel()).getIP().split(":")[1]), charId));
        } else {
            c.sendPacket(LoginPacket.secondPwError((byte) 0x14));
        }
    }

    //讀取Mac地址
    private static String readMacAddress(final LittleEndianAccessor slea, final MapleClient c) {
        int[] bytes = new int[6];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = slea.readByteAsInt();
        }
        StringBuilder sps = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            sps.append(StringUtil.getLeftPaddedStr(Integer.toHexString(bytes[i]).toUpperCase(), '0', 2));
            sps.append("-");
        }
        return sps.toString().substring(0, sps.toString().length() - 1);
    }

    //獲取主程式KEY
    public static String RandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int num = random.nextInt(62);
            buf.append(str.charAt(num));
        }
        return buf.toString();
    }
}

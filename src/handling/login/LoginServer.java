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
package handling.login;

import Apple.client.AccountStorage;
import Apple.client.java.Quadruple;
import client.MapleClient;
import constants.ServerConstants;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import handling.MapleServerHandler;
import handling.ServerType;
import java.util.Collection;
import java.util.WeakHashMap;

import server.ServerProperties;
import server.netty.ServerConnection;
import tools.Pair;

public class LoginServer {

    public static int PORT = 8484;
    private static InetSocketAddress InetSocketadd;
    private static ServerConnection init;
    private static Map<Integer, Integer> load = new HashMap<Integer, Integer>();
    private static String serverName, eventMessage, bubbleMessage, bubbleMessagePos;
    private static byte flag;
    private static int maxCharacters, userLimit, usersOn = 0;
    private static boolean finishedShutdown = true, adminOnly = false;
    private static final HashMap<Integer, Quadruple<String, String, Integer, String>> loginAuth = new HashMap<>();
    private static final HashMap<String, Pair<String, Integer>> loginAuthKey = new HashMap<>();
    private static final Map<Integer, String> LoginKey = new HashMap<>();

    private static final Map<Integer, Long> SelectCharTime = new WeakHashMap<>();
    private static final Map<Integer, Long> ChangeChannelTime = new HashMap<>();
    private static final Map<Integer, Long> EnterGameTime = new HashMap<>();
    private static AccountStorage clients;

    public static void putLoginAuth(int chrid, String ip, String tempIp, int channel, String mac) {
        loginAuth.put(chrid, new Quadruple<>(ip, tempIp, channel, mac));
    }

    public static Quadruple<String, String, Integer, String> getLoginAuth(int chrid) {
        return loginAuth.remove(chrid);
    }

    public static void pubLoginAuthKey(String key, String account, int channel) {
        loginAuthKey.put(key, new Pair<>(account, channel));
    }

    public static Pair<String, Integer> getLoginAuthKey(String account, boolean remove) {
        if (remove) {
            return loginAuthKey.remove(account);
        } else {
            return loginAuthKey.get(account);
        }
    }

    public static final void addChannel(final int channel) {
        load.put(channel, 0);
    }

    public static final void removeChannel(final int channel) {
        load.remove(channel);
    }

    public static final void run_startup_configurations() {
        userLimit = Integer.parseInt(ServerProperties.getProperty("tms.UserLimit"));
        serverName = ServerProperties.getProperty("tms.ServerName");
        eventMessage = ServerProperties.getProperty("tms.EventMessage");
        bubbleMessage = ServerProperties.getProperty("tms.BubbleMessage");
        bubbleMessagePos = ServerProperties.getProperty("tms.BubbleMessagePos");
        flag = Byte.parseByte(ServerProperties.getProperty("tms.Flag"));
        adminOnly = Boolean.parseBoolean(ServerProperties.getProperty("tms.Admin", "false"));
        maxCharacters = Integer.parseInt(ServerProperties.getProperty("tms.MaxCharacters"));
        PORT = Integer.parseInt(ServerProperties.getProperty("tms.LPort"));

        init = new ServerConnection(PORT, -1, -1, ServerType.登入伺服器);
        init.run();
        System.out.println("綁定端口 " + PORT + " 成功.");
    }

    public static final void shutdown() {
        if (finishedShutdown) {
            return;
        }
        System.out.println("正在關閉登錄服務器...");
        init.close();
        finishedShutdown = true; //nothing. lol
    }

    public static final String getServerName() {
        return serverName;
    }

    public static final String getEventMessage() {
        return eventMessage;
    }

    public static final String getBubbleMessage() {
        return bubbleMessage;
    }

    public static final String getBubbleMessagePos() {
        return bubbleMessagePos;
    }

    public static final byte getFlag() {
        return flag;
    }

    public static final int getMaxCharacters() {
        return maxCharacters;
    }

    public static final Map<Integer, Integer> getLoad() {
        return load;
    }

    public static void setLoad(final Map<Integer, Integer> load_, final int usersOn_) {
        load = load_;
        usersOn = usersOn_;
    }

    public static final void setEventMessage(final String newMessage) {
        eventMessage = newMessage;
    }

    public static final void setFlag(final byte newflag) {
        flag = newflag;
    }

    public static final int getUserLimit() {
        return userLimit;
    }

    public static final int getUsersOn() {
        return usersOn;
    }

    public static final void setUserLimit(final int newLimit) {
        userLimit = newLimit;
    }

    public static final boolean isAdminOnly() {
        return adminOnly;
    }

    public static final boolean isShutdown() {
        return finishedShutdown;
    }

    public static final void setOn() {
        finishedShutdown = false;
    }

    public static boolean getAutoReg() {
        return ServerConstants.AUTO_REGISTER;
    }

    public static void setAutoReg(boolean x) {
        ServerConstants.AUTO_REGISTER = x;
    }

    public static AccountStorage getClientStorage() {
        if (clients == null) {
            clients = new AccountStorage();
        }
        return clients;
    }

    public static void forceRemoveClient(MapleClient client, boolean remove) {
        Collection<MapleClient> cls = getClientStorage().getAllClientsThreadSafe();
        for (MapleClient c : cls) {
            if (c == null) {
                continue;
            }
            if (c.getAccID() == client.getAccID() || c == client) {
                if (c != client) {
                    c.unLockDisconnect();
                }
                if (remove) {
                    removeClient(c);
                }
            }
        }
    }

    public static final void removeClient(final MapleClient c) {
        getClientStorage().deregisterAccount(c);
    }

    public static boolean CanLoginKey(MapleClient c, String key) {
        if (LoginKey.get(c.getAccID()) == null) {
            return true;
        }
        if (LoginKey.containsValue(key)) {
            if (LoginKey.get(c.getAccID()).equals(key)) {
                return true;
            }
        }
        return false;
    }

    public static String getLoginKey(MapleClient c) {
        return LoginKey.get(c.getAccID());
    }

    public static boolean addLoginKey(MapleClient c, String key) {
        LoginKey.put(c.getAccID(), key);
        return true;
    }

    public static boolean removeLoginKey(MapleClient c) {
        LoginKey.remove(c.getAccID());
        return true;
    }

    //快速登入
    public static boolean CheckSelectChar(int accid) {
        long lastTime = System.currentTimeMillis();
        if (SelectCharTime.containsKey(accid)) {
            long lastSelectCharTime = SelectCharTime.get(accid);
            if (lastSelectCharTime + 3000 > lastTime) {
                return false;
            }
            SelectCharTime.remove(accid);
        } else {
            SelectCharTime.put(accid, lastTime);
        }
        return true;
    }

    /*
     * 登入時間處理 
     */
    public static boolean canLoginAgain(int accid) {
        long lastTime = System.currentTimeMillis();
        if (ChangeChannelTime.containsKey(accid)) {
            long lastSelectCharTime = ChangeChannelTime.get(accid);
            if (lastSelectCharTime + 40 * 1000 > lastTime) {
                return false;
            }
        }
        return true;
    }

    public static void addLoginAgainTime(int accid) {
        ChangeChannelTime.put(accid, System.currentTimeMillis());
    }

    public static long getLoginAgainTime(int accid) {
        return ChangeChannelTime.get(accid);
    }

    /*
     * 進入遊戲時間處理 
     */
    public static boolean canEnterGameAgain(int accid) {
        long lastTime = System.currentTimeMillis();
        if (EnterGameTime.containsKey(accid)) {
            long lastSelectCharTime = EnterGameTime.get(accid);
            if (lastSelectCharTime + 60 * 1000 > lastTime) {
                return false;
            }
        }
        return true;
    }

    public static void addEnterGameAgainTime(int accid) {
        EnterGameTime.put(accid, System.currentTimeMillis());
    }

    public static long getEnterGameAgainTime(int accid) {
        return EnterGameTime.get(accid);
    }

}

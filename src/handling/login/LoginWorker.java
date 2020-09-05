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

import java.util.Map;
import java.util.Map.Entry;
import client.MapleClient;
import constants.ServerConstants;
import handling.channel.ChannelServer;
import server.Timer.PingTimer;
import tools.packet.LoginPacket;
import tools.MaplePacketCreator;

public class LoginWorker {

    private static long lastUpdate = 0;

    public static void registerClient(final MapleClient c) {
        /*boolean AllowAccount = false;
        for (String ban : ServerConstants.允許帳號.split(",")) {
            if (ban.equals(c.getAccountName()) && c.getAccID() <= 75) {
                AllowAccount = false;
                break;
            } else {
                if (c.getAccID() > 75) {
                    break;
                }
                AllowAccount = true;
            }
        }
        if (AllowAccount) {
            c.sendPacket(LoginPacket.getLoginFailed(1)); //用於解除登錄按鈕
            c.sendPacket(MaplePacketCreator.getPopupMsg("由於您是老玩家請透過管理員進行開通"));
            return;
        }*/
        if (ServerConstants.ADMIN_ONLY && !c.isGm()) {
            c.sendPacket(LoginPacket.getLoginFailed(1));
            c.sendPacket(MaplePacketCreator.getPopupMsg("伺服器目前正在維修中.\r\n目前管理員正在測試物品.\r\n請稍後等待維修。"));
            return;
        }
        if (LoginServer.isAdminOnly() && !c.isGm()) {
            c.sendPacket(MaplePacketCreator.serverNotice(1, "The server is currently set to Admin login only.\r\nWe are currently testing some issues.\r\nPlease try again later."));
            c.sendPacket(LoginPacket.getLoginFailed(7));
            return;
        }

        if (System.currentTimeMillis() - lastUpdate > 600000) { // Update once every 10 minutes
            lastUpdate = System.currentTimeMillis();
            final Map<Integer, Integer> load = ChannelServer.getChannelLoad();
            int usersOn = 0;
            if (load == null || load.size() <= 0) { // In an unfortunate event that client logged in before load
                lastUpdate = 0;
                c.sendPacket(LoginPacket.getLoginFailed(7));
                return;
            }
            final double loadFactor = 1200 / ((double) LoginServer.getUserLimit() / load.size());
            for (Entry<Integer, Integer> entry : load.entrySet()) {
                usersOn += entry.getValue();
                load.put(entry.getKey(), Math.min(1200, (int) (entry.getValue() * loadFactor)));
            }
            LoginServer.setLoad(load, usersOn);
            lastUpdate = System.currentTimeMillis();
        }
        if (c.finishLogin() == 0) {
            //World.clearChannelChangeDataByAccountId(c.getAccID());//雙登複製解法
            if (c.getSecondPassword() == null) {
                c.sendPacket(LoginPacket.getGenderNeeded(c));
            } else {
                c.sendPacket(LoginPacket.getAuthSuccessRequest(c));
                c.sendPacket(LoginPacket.getServerList(0, LoginServer.getServerName(), LoginServer.getLoad()));
                c.sendPacket(LoginPacket.getEndOfServerList());
            }
            c.setIdleTask(PingTimer.getInstance().schedule(new Runnable() {

                public void run() {
//                    c.getSession().close();
                }
            }, 10 * 60 * 10000));
        } else {
            c.sendPacket(LoginPacket.getLoginFailed(7));
            return;
        }/*
        try (Connection con = (Connection) DatabaseConnection.getConnection();) {
            try (PreparedStatement ps = con.prepareStatement("INSERT INTO accounts_log (accid, accname, ip, macs) VALUES (?, ?, ?, ?)")) {
                ps.setInt(1, c.getAccID());
                ps.setString(2, c.getAccountName());
                ps.setString(3, c.getSessionIPAddress());
                ps.setString(4, c.getMac());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println(e);
        }*/
    }
}

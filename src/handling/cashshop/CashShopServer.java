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
package handling.cashshop;

import java.net.InetSocketAddress;

import handling.MapleServerHandler;
import handling.ServerType;
import handling.channel.PlayerStorage;
import server.MTSStorage;
import server.ServerProperties;
import server.netty.ServerConnection;

public class CashShopServer {

    private static String ip;
    private static InetSocketAddress InetSocketadd;
    private static int PORT = 8700;
    private static ServerConnection init;
    private static PlayerStorage players, playersMTS;
    private static boolean finishedShutdown = false;

    public static final void run_startup_configurations() {
        PORT = Integer.parseInt(ServerProperties.getProperty("tms.CPort"));
        ip = ServerProperties.getProperty("tms.IP") + ":" + PORT;

        players = new PlayerStorage(-10);
        playersMTS = new PlayerStorage(-20);

        init = new ServerConnection(PORT, 0, -10, ServerType.商城伺服器);
        init.run();
        System.out.println("綁定端口 " + PORT + " 成功.");
    }

    public static final String getIP() {
        return ip;
    }

    public static final PlayerStorage getPlayerStorage() {
        return players;
    }

    public static final PlayerStorage getPlayerStorageMTS() {
        return playersMTS;
    }

    public static final void shutdown() {
        if (finishedShutdown) {
            return;
        }
        System.out.println("保存所有連接的客戶端(CS)...");
        players.disconnectAll();
        playersMTS.disconnectAll();
        MTSStorage.getInstance().saveBuyNow(true);
        System.out.println("正在關閉商城服務器...");//商城服务器解除端口绑定...
        init.close();
        finishedShutdown = true;
    }

    public static boolean isShutdown() {
        return finishedShutdown;
    }
}

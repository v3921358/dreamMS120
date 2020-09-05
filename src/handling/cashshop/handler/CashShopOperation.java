package handling.cashshop.handler;

import Apple.client.WorldFindService;
import java.sql.SQLException;
import java.util.Map;
import java.util.HashMap;

import constants.GameConstants;
import client.MapleClient;
import client.MapleCharacter;
import client.MapleCharacterUtil;
import client.inventory.MapleInventoryType;
import client.inventory.MapleRing;
import client.inventory.MapleInventoryIdentifier;
import client.inventory.IItem;
import constants.ServerConstants;
import handling.cashshop.CashShopServer;
import handling.channel.ChannelServer;
import handling.world.CharacterTransfer;
import handling.world.World;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.CashItemFactory;
import server.CashItemInfo;
import server.MapleInventoryManipulator;
import server.MapleItemInformationProvider;
import server.RandomRewards;
import tools.FileoutputUtil;
import tools.MaplePacketCreator;
import tools.packet.MTSCSPacket;
import tools.Pair;
import tools.data.LittleEndianAccessor;

public class CashShopOperation {

    public static void LeaveCS(final LittleEndianAccessor slea, final MapleClient c, final MapleCharacter chr) {
        if (chr == null) {
            System.err.println("玩家: " + c.getAccID() + "偵測錯誤");
            return;
        }
        int channel = c.getChannel(); //角色要更換的頻道
        ChannelServer toch = ChannelServer.getInstance(channel); //角色從商城出來更換的頻道信息
        if (toch == null) {
            System.err.println("玩家: " + chr.getName() + " 從商城離開發生錯誤.找不到頻道[" + channel + "]的信息.");
            c.getSession().close();
            System.err.println("\r\n帳號[" + c.getAccountName() + "] 伺服器主動斷開用戶端連接，調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            FileoutputUtil.log("logs/data/DC.txt", "\r\n帳號[" + c.getAccountName() + "] 伺服器主動斷開用戶端連接，調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            return;
        }
        //開始處理
        World.ChannelChange_Data(new CharacterTransfer(chr), chr.getId(), c.getChannel());
        CashShopServer.getPlayerStorage().deregisterPlayer(chr, "調用位置:" + new java.lang.Throwable().getStackTrace()[0]);
        c.updateLoginState(MapleClient.LOGIN_SERVER_TRANSITION, c.getSessionIPAddress());
        c.sendPacket(MaplePacketCreator.getChannelChange(toch.getPort()));//發送更換頻道的封包信息
        // chr.fixOnlineTime();
        chr.saveToCache();//保存到緩存
        c.setPlayer(null);
        c.setReceiving(false);
    }

    public static void EnterCS(final int playerid, final MapleClient c) {
        CharacterTransfer transfer = null;
        transfer = CashShopServer.getPlayerStorage().getPendingCharacter(playerid);
        if (transfer == null) {
            c.getSession().close();
            System.err.println("\r\n帳號[" + c.getAccountName() + "] 伺服器主動斷開用戶端連接，調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            FileoutputUtil.log("logs/data/DC.txt", "\r\n帳號[" + c.getAccountName() + "] 伺服器主動斷開用戶端連接，調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            return;
        }
        MapleCharacter chr = MapleCharacter.ReconstructChr(transfer, c, false);

        c.setPlayer(chr);
        c.setAccID(chr.getAccountID());

        if (!c.CheckIPAddress()) { // Remote hack
            c.getSession().close();
            System.err.println("\r\n帳號[" + c.getAccountName() + "] 伺服器主動斷開用戶端連接，調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            FileoutputUtil.log("logs/data/DC.txt", "\r\n帳號[" + c.getAccountName() + "] 伺服器主動斷開用戶端連接，調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            System.err.println("商城檢測連接 - 2 " + !c.CheckIPAddress());
            return;
        }

        int state = c.getLoginState();
        boolean allowLogin = false;
        if (state == MapleClient.LOGIN_SERVER_TRANSITION || state == MapleClient.CHANGE_CHANNEL) {
            if (!World.isCharacterListConnected(c.loadCharacterNames(c.getWorld()))) {
                allowLogin = true;
            }
        }
        if (!allowLogin) {
            c.setPlayer(null);
            c.getSession().close();
            System.err.println("\r\n帳號[" + c.getAccountName() + "] 伺服器主動斷開用戶端連接，調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            FileoutputUtil.log("logs/data/DC.txt", "\r\n帳號[" + c.getAccountName() + "] 伺服器主動斷開用戶端連接，調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            System.err.println("商城檢測連接 - 3 " + !allowLogin);
            return;
        }
        c.updateLoginState(MapleClient.LOGIN_LOGGEDIN, c.getSessionIPAddress());
        CashShopServer.getPlayerStorage().registerPlayer(chr);
        c.sendPacket(MTSCSPacket.warpCS(c));
        CSUpdate(c);
    }

    public static void CSUpdate(final MapleClient c) {
        c.sendPacket(MTSCSPacket.getCSGifts(c));
        c.sendPacket(MTSCSPacket.showAcc(c));
        doCSPackets(c);
        c.sendPacket(MTSCSPacket.sendWishList(c.getPlayer(), false));
    }

    public static void CouponCode(final String code, final MapleClient c) {
        int validcode = -2;
        int type = -1, item = -1, size = -1, time = -1;
        validcode = MapleCharacterUtil.getNXCodeValid(code.toUpperCase());

        if (validcode > 0) {
            type = MapleCharacterUtil.getNXCodeType(code);
            item = MapleCharacterUtil.getNXCodeItem(code);
            size = MapleCharacterUtil.getNXCodeSize(code);
            time = MapleCharacterUtil.getNXCodeTime(code);
            if (type <= 4) {
                try {
                    MapleCharacterUtil.setNXCodeUsed(c.getPlayer().getName(), code);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            /*
             * 類型說明！
             * 基本上，這使得優惠券代碼做不同的東西！
             *
             * Type 1: GASH點數
             * Type 2: 楓葉點數
             * Type 3: 物品x數量(默認1個)
             * Type 4: 楓幣
             */
            int maplePoints = 0, mesos = 0, as = 0;
            String cc = "", tt = "";
            switch (type) {
                case 1:
                    c.getPlayer().modifyCSPoints(1, item, true);
                    maplePoints = item;
                    cc = "GASH";
                    break;
                case 2:
                    c.getPlayer().modifyCSPoints(2, item, true);
                    maplePoints = item;
                    cc = "楓葉點數";
                    break;
                case 3:
                    //   MapleInventoryManipulator.addById(c, item, (short) size, "優待卷禮品.", null, time);
                    byte slot = MapleInventoryManipulator.addId(c, item, (short) size, "優待卷禮品");
                    Map<Integer, IItem> itemz = new HashMap<Integer, IItem>();
                    itemz.put(item, c.getPlayer().getInventory(GameConstants.getInventoryType(item)).getItem(slot));
                    as = 1;
                    if (time == -1) {
                        as = 2;
                    }
                    break;
                case 4:
                    c.getPlayer().gainMeso(item, false);
                    mesos = item;
                    cc = "楓幣";
                    break;
            }
            /*if (time == -1) {
                tt = "永久";
                as = 2;
            }*/
            switch (as) {
                case 1:
                    //c.sendPacket(MTSCSPacket.showCouponRedeemedItem(itemz, mesos, maplePoints, c));
                    c.getPlayer().dropMessage(1, "已成功使用優待卷,獲得" + MapleItemInformationProvider.getInstance().getName(item) + time + "天 x" + size + "。");
                    break;
                case 2:
                    c.getPlayer().dropMessage(1, "已成功使用優待卷,獲得" + MapleItemInformationProvider.getInstance().getName(item) + "永久 x" + size + "。");
                    break;
                default:
                    c.getPlayer().dropMessage(1, "已成功使用優待卷,獲得" + item + cc);
                    break;
            }
        } else if (validcode == 0) {
            c.getPlayer().dropMessage(1, "這個序號已使用過囉");
        } else if (validcode == -1) {
            c.getPlayer().dropMessage(1, "沒有這個序號");
        } else {
            c.sendPacket(MTSCSPacket.sendCSFail(0xB3)); //idb
        }
        doCSPackets(c);
    }

    public static final void BuyCashItem(final LittleEndianAccessor slea, final MapleClient c, final MapleCharacter chr) {
        final int action = slea.readByte();
        if (action == 0) {
            slea.skip(2);
            CouponCode(slea.readMapleAsciiString(), c);
        } else if (action == 3) {
            final byte type = slea.readByte() == 0 ? (byte) 1 : (byte) 2;
            //slea.skip(1);
            final CashItemInfo item = CashItemFactory.getInstance().getItem(slea.readInt());
            for (String ban : ServerConstants.禁止購買.split(",")) {
                if (!"".equals(ServerConstants.禁止購買) && item != null && Integer.parseInt(ban) == item.getId()) {
                    c.getPlayer().dropMessage(1, "該道具是禁止購買商品，請等候開放。");
                    doCSPackets(c);
                    return;
                }
            }
            for (int ban : ServerConstants.任意門代碼) {
                if (item.getId() == ban) {
                    String Rock = "任意門" + item.getId();
                    if (chr.getBossLog(Rock) + item.getCount() > ServerConstants.任意門購買數量) {
                        chr.dropMessage(1, "今日任意門購買數量剩餘:" + (ServerConstants.任意門購買數量 - chr.getBossLog(Rock)));
                        doCSPackets(c);
                        return;
                    } else {
                        chr.setBossLog(Rock, 0, item.getCount());
                    }
                }
            }
            FileoutputUtil.log("商城購買.ini", FileoutputUtil.output_text(c, chr, " 使用了:" + (type == 1 ? "[CASH]" : "[楓點]") + item.getPrice() + "點", "購買:" + item.getId() + "x" + item.getCount() + "-" + MapleItemInformationProvider.getInstance().getName(item.getId()), " 期限:" + (item.getPeriod() > 0 ? item.getPeriod() : "永久"), " 時間:" + FileoutputUtil.CurrentReadable_Time()));

            //FileoutputUtil.log("logs/Data/商城購買.txt", "\r\n " + FileoutputUtil.NowTime() + " IP: " + c.getSession().remoteAddress().toString().split(":")[0] + " 帳號: " + c.getAccountName() + " 玩家: " + c.getPlayer().getName() + " 使用了" + (type == 1 ? "CASH" : "楓葉點數") + item.getPrice() + "點 來購買" + item.getId() + "x" + item.getCount());
            if (item != null && chr.getCSPoints(type) >= item.getPrice()) {
                if (!item.genderEquals(c.getPlayer().getGender())) {
                    c.sendPacket(MTSCSPacket.sendCSFail(0xA6));
                    doCSPackets(c);
                    return;
                } else if (c.getPlayer().getCashInventory().getItemsSize() >= 100) {
                    c.sendPacket(MTSCSPacket.sendCSFail(0xD1));
                    doCSPackets(c);
                    return;
                }
                for (int i : GameConstants.cashBlock) {
                    if (item.getId() == i) {
                        c.getPlayer().dropMessage(1, GameConstants.getCashBlockedMsg(item.getId()));
                        doCSPackets(c);
                        return;
                    }
                }
                chr.modifyCSPoints(type, -item.getPrice(), false);
                IItem itemz = chr.getCashInventory().toItem(item);
                if (itemz != null && itemz.getUniqueId() > 0 && itemz.getItemId() == item.getId() && itemz.getQuantity() == item.getCount()) {
                    chr.getCashInventory().addToInventory(itemz);
                    //c.sendPacket(MTSCSPacket.confirmToCSInventory(itemz, c.getAccID(), item.getSN()));
                    c.sendPacket(MTSCSPacket.showBoughtCSItem(itemz, item.getSN(), c.getAccID()));
                } else {
                    c.sendPacket(MTSCSPacket.sendCSFail(0));
                }
            } else {
                c.sendPacket(MTSCSPacket.sendCSFail(0));
            }
        } else if (action == 4 || action == 38) { //gift, package
            slea.readMapleAsciiString(); // as13
            final CashItemInfo item = CashItemFactory.getInstance().getItem(slea.readInt());
            String partnerName = slea.readMapleAsciiString();
            String msg = slea.readMapleAsciiString();
            if (item == null || c.getPlayer().getCSPoints(1) < item.getPrice() || msg.length() > 73 || msg.length() < 1) { //dont want packet editors gifting random stuff =P
                c.sendPacket(MTSCSPacket.sendCSFail(0));
                doCSPackets(c);
                return;
            }
            Pair<Integer, Pair<Integer, Integer>> info = MapleCharacterUtil.getInfoByName(partnerName, c.getPlayer().getWorld());
            if (info == null || info.getLeft().intValue() <= 0 || info.getLeft().intValue() == c.getPlayer().getId() || info.getRight().getLeft().intValue() == c.getAccID()) {
                c.sendPacket(MTSCSPacket.sendCSFail(0xA2)); //9E v75
                doCSPackets(c);
                return;
            } else if (!item.genderEquals(info.getRight().getRight().intValue())) {
                c.sendPacket(MTSCSPacket.sendCSFail(0xC2));
                doCSPackets(c);
                return;
            } else {
                for (int i : GameConstants.cashBlock) {
                    if (item.getId() == i) {
                        c.getPlayer().dropMessage(1, GameConstants.getCashBlockedMsg(item.getId()));
                        doCSPackets(c);
                        return;
                    }
                }
                c.getPlayer().getCashInventory().gift(info.getLeft().intValue(), c.getPlayer().getName(), msg, item.getSN(), MapleInventoryIdentifier.getInstance());
                c.getPlayer().modifyCSPoints(1, -item.getPrice(), false);
                c.sendPacket(MTSCSPacket.sendGift(item.getPrice(), item.getId(), item.getCount(), partnerName, action == 4 ? false : true));
            }
        } else if (action == 5) { // Wishlist
            chr.clearWishlist();
            if (slea.available() < 40) {
                c.sendPacket(MTSCSPacket.sendCSFail(0));
                doCSPackets(c);
                return;
            }
            int[] wishlist = new int[10];
            for (int i = 0; i < 10; i++) {
                wishlist[i] = slea.readInt();
            }
            chr.setWishlist(wishlist);
            c.sendPacket(MTSCSPacket.sendWishList(chr, true));

        } else if (action == 6) { // Increase inv
            final byte useCash = slea.readByte() == 0 ? (byte) 1 : (byte) 2;
            final boolean coupon = slea.readByte() > 0;
            if (coupon) {
                final MapleInventoryType type = getInventoryType(slea.readInt());

                if (chr.getCSPoints(useCash) >= 100 && chr.getInventory(type).getSlotLimit() < 89) {
                    chr.modifyCSPoints(useCash, -100, false);
                    chr.getInventory(type).addSlot((byte) 8);
                    chr.dropMessage(1, "Slots has been increased to " + chr.getInventory(type).getSlotLimit());
                } else {
                    c.sendPacket(MTSCSPacket.sendCSFail(0xA4));
                }
            } else {
                byte inv = slea.readByte();
                final MapleInventoryType type = MapleInventoryType.getByType(inv);

                if (chr.getCSPoints(useCash) >= 100 && chr.getInventory(type).getSlotLimit() < 93) {
                    chr.modifyCSPoints(useCash, -100, false);
                    chr.getInventory(type).addSlot((byte) 4);
                    c.sendPacket(MTSCSPacket.increasedInvSlots(inv, chr.getInventory(type).getSlotLimit()));
                } else {
                    c.sendPacket(MTSCSPacket.sendCSFail(0xA4));
                }
            }

        } else if (action == 7) { // Increase slot space
            final byte useCash = slea.readByte() == 0 ? (byte) 1 : (byte) 2;
            if (chr.getCSPoints(useCash) >= 100 && chr.getStorage().getSlots() < 45) {
                chr.modifyCSPoints(useCash, -100, false);
                chr.getStorage().increaseSlots((byte) 4);
                chr.getStorage().saveToDB();
                //c.sendPacket(MTSCSPacket.increasedStorageSlots(chr.getStorage().getSlots()));
                chr.dropMessage(1, "倉庫欄位增加至 " + chr.getStorage().getSlots());
            } else {
                c.sendPacket(MTSCSPacket.sendCSFail(0xA4));
            }
        } else if (action == 8) { //...9 = pendant slot expansion
            slea.readByte();
            CashItemInfo item = CashItemFactory.getInstance().getItem(slea.readInt());
            int slots = c.getCharacterSlots();
            if (item == null || c.getPlayer().getCSPoints(1) < item.getPrice() || slots > 15) {
                c.sendPacket(MTSCSPacket.sendCSFail(0));
                doCSPackets(c);
                return;
            }
            c.getPlayer().modifyCSPoints(1, -item.getPrice(), false);
            if (c.gainCharacterSlot()) {
                c.sendPacket(MTSCSPacket.increasedStorageSlots(slots + 1));
            } else {
                c.sendPacket(MTSCSPacket.sendCSFail(0));
            }
        } else if (action == 14) { //從cs庫存中獲取項目 get item from csinventory 
            //uniqueid, 00 01 01 00, type->position(short)
            IItem item = c.getPlayer().getCashInventory().findByCashId((int) slea.readLong());
            if (item != null && item.getQuantity() > 0 && MapleInventoryManipulator.checkSpace(c, item.getItemId(), item.getQuantity(), item.getOwner())) {
                IItem item_ = item.copy();
                short pos = MapleInventoryManipulator.addbyItem(c, item_, true);
                if (pos >= 0) {
                    if (item_.getPet() != null) {
                        item_.getPet().setInventoryPosition(pos);
                        c.getPlayer().addPet(item_.getPet());
                    }
                    c.getPlayer().getCashInventory().removeFromInventory(item);
                    c.sendPacket(MTSCSPacket.confirmFromCSInventory(item_, pos));
                } else {
                    c.sendPacket(MTSCSPacket.sendCSFail(0xD1));
                }
            } else {
                c.sendPacket(MTSCSPacket.sendCSFail(0xD1));
            }
        } else if (action == 15) { //put item in cash inventory
            int uniqueid = (int) slea.readLong();
            MapleInventoryType type = MapleInventoryType.getByType(slea.readByte());
            IItem item = c.getPlayer().getInventory(type).findByUniqueId(uniqueid);
            if (item != null && item.getQuantity() > 0 && item.getUniqueId() > 0 && c.getPlayer().getCashInventory().getItemsSize() < 100) {
                IItem item_ = item.copy();
                //MapleInventoryManipulator.removeFromSlot(c, type, item.getPosition(), item.getQuantity(), false);
                c.getPlayer().getInventory(type).removeItem(item.getPosition(), item.getQuantity(), false);
                if (item_.getPet() != null) {
                    c.getPlayer().removePetCS(item_.getPet());
                }
                item_.setPosition((byte) 0);
                c.getPlayer().getCashInventory().addToInventory(item_);
                //warning: this d/cs
                c.sendPacket(MTSCSPacket.confirmToCSInventory(item, c.getAccID(), 0 /* c.getPlayer().getCashInventory().getSNForItem(item)*/));
            } else {
                c.sendPacket(MTSCSPacket.sendCSFail(0xD1));
            }
        } else if (action == 27) { // 26 = sell cash item.
            slea.readMapleAsciiString(); // as13
            IItem item = c.getPlayer().getCashInventory().findByCashId((int) slea.readLong());
            if (item != null && item.getQuantity() > 0 && MapleInventoryManipulator.checkSpace(c, item.getItemId(), item.getQuantity(), item.getOwner())) {
                chr.modifyCSPoints(2, 5, false);
                c.getPlayer().getCashInventory().removeFromInventory(item);
                c.getPlayer().dropMessage(1, "獲得了 5 點楓葉點數");

            }
            doCSPackets(c);
            return;
        } else if (action == 30 || action == 36) { //35 = friendship, 29 = crush
            //c.sendPacket(MTSCSPacket.sendCSFail(0));
            slea.readMapleAsciiString(); // as13
            final CashItemInfo item = CashItemFactory.getInstance().getItem(slea.readInt());
            final String partnerName = slea.readMapleAsciiString();
            final String msg = slea.readMapleAsciiString();
            if (item == null || !GameConstants.isEffectRing(item.getId()) || c.getPlayer().getCSPoints(1) < item.getPrice() || msg.length() > 73 || msg.length() < 1) {
                c.sendPacket(MTSCSPacket.sendCSFail(0));
                doCSPackets(c);
                return;
            } else if (!item.genderEquals(c.getPlayer().getGender())) {
                c.sendPacket(MTSCSPacket.sendCSFail(0xA6));
                doCSPackets(c);
                return;
            } else if (c.getPlayer().getCashInventory().getItemsSize() >= 100) {
                c.sendPacket(MTSCSPacket.sendCSFail(0xD1));
                doCSPackets(c);
                return;
            }
            for (int i : GameConstants.cashBlock) { //just incase hacker
                if (item.getId() == i) {
                    c.getPlayer().dropMessage(1, GameConstants.getCashBlockedMsg(item.getId()));
                    doCSPackets(c);
                    return;
                }
            }
            Pair<Integer, Pair<Integer, Integer>> info = MapleCharacterUtil.getInfoByName(partnerName, c.getPlayer().getWorld());
            if (info == null || info.getLeft().intValue() <= 0 || info.getLeft().intValue() == c.getPlayer().getId()) {
                c.sendPacket(MTSCSPacket.sendCSFail(0xB4)); //9E v75
                doCSPackets(c);
                return;
            } else if (info.getRight().getLeft().intValue() == c.getAccID()) {
                c.sendPacket(MTSCSPacket.sendCSFail(0xA3)); //9D v75
                doCSPackets(c);
                return;
            } else {
                if (info.getRight().getRight().intValue() == c.getPlayer().getGender() && action == 30) {
                    c.sendPacket(MTSCSPacket.sendCSFail(0xD3)); //9B v75
                    doCSPackets(c);
                    return;
                }

                int err = MapleRing.createRing(item.getId(), c.getPlayer(), partnerName, msg, info.getLeft().intValue(), item.getSN());

                if (err != 1) {
                    c.sendPacket(MTSCSPacket.sendCSFail(0)); //9E v75
                    doCSPackets(c);
                    return;
                }
                c.getPlayer().modifyCSPoints(1, -item.getPrice(), false);
                //c.sendPacket(MTSCSPacket.showBoughtCSItem(itemz, item.getSN(), c.getAccID()));
                c.sendPacket(MTSCSPacket.sendGift(item.getPrice(), item.getId(), item.getCount(), partnerName, false));
            }

        } else if (action == 31) {//商城 - 套裝 - 裡包商品
            slea.skip(1);
            final CashItemInfo item = CashItemFactory.getInstance().getItem(slea.readInt());
            List<CashItemInfo> ccc = null;
            if (item != null) {
                ccc = CashItemFactory.getInstance().getPackageItems(item.getId());
            }
            if (item == null || ccc == null || c.getPlayer().getCSPoints(1) < item.getPrice()) {
                //c.sendPacket(MTSCSPacket.sendCSFail(0));//套裝點數購買
                c.getPlayer().dropMessage(1, "只許用 CASH 購買");
                doCSPackets(c);
                return;
            } else if (!item.genderEquals(c.getPlayer().getGender())) {
                c.sendPacket(MTSCSPacket.sendCSFail(0xA6));
                doCSPackets(c);
                return;
            } else if (c.getPlayer().getCashInventory().getItemsSize() >= (100 - ccc.size())) {
                c.sendPacket(MTSCSPacket.sendCSFail(0xD1));
                doCSPackets(c);
                return;
            }
            for (int iz : GameConstants.cashBlock) {
                if (item.getId() == iz) {
                    c.getPlayer().dropMessage(1, GameConstants.getCashBlockedMsg(item.getId()));
                    doCSPackets(c);
                    return;
                }
            }
            Map<Integer, IItem> ccz = new HashMap<Integer, IItem>();
            for (CashItemInfo i : ccc) {
                for (int iz : GameConstants.cashBlock) {
                    if (i.getId() == iz) {
                        continue;
                    }
                }
                IItem itemz = c.getPlayer().getCashInventory().toItem(i);
                if (itemz == null || itemz.getUniqueId() <= 0 || itemz.getItemId() != i.getId()) {
                    continue;
                }
                ccz.put(i.getSN(), itemz);
                c.getPlayer().getCashInventory().addToInventory(itemz);
            }
            chr.modifyCSPoints(1, -item.getPrice(), false);
            c.sendPacket(MTSCSPacket.showBoughtCSPackage(ccz, c.getAccID()));

        } else if (action == 33) {
            final CashItemInfo item = CashItemFactory.getInstance().getItem(slea.readInt());
            if (item == null || !MapleItemInformationProvider.getInstance().isQuestItem(item.getId())) {
                c.sendPacket(MTSCSPacket.sendCSFail(0));
                doCSPackets(c);
                return;
            } else if (c.getPlayer().getMeso() < item.getPrice()) {
                c.sendPacket(MTSCSPacket.sendCSFail(0xCA));
                doCSPackets(c);
                return;
            } else if (c.getPlayer().getInventory(GameConstants.getInventoryType(item.getId())).getNextFreeSlot() < 0) {
                c.sendPacket(MTSCSPacket.sendCSFail(0xD1));
                doCSPackets(c);
                return;
            }
            for (int iz : GameConstants.cashBlock) {
                if (item.getId() == iz) {
                    c.getPlayer().dropMessage(1, GameConstants.getCashBlockedMsg(item.getId()));
                    doCSPackets(c);
                    return;
                }
            }
            byte pos = MapleInventoryManipulator.addId(c, item.getId(), (short) item.getCount(), null);
            if (pos < 0) {
                c.sendPacket(MTSCSPacket.sendCSFail(0xD1));
                doCSPackets(c);
                return;
            }
            chr.gainMeso(-item.getPrice(), false);
            c.sendPacket(MTSCSPacket.showBoughtCSQuestItem(item.getPrice(), (short) item.getCount(), pos, item.getId()));
        } else if (action == 53) {
            doCSPackets(c);
            return;
        } else {
            c.sendPacket(MTSCSPacket.sendCSFail(0));

        }
        doCSPackets(c);
    }

    public static final void SendCashGift(final LittleEndianAccessor slea, final MapleClient c, final MapleCharacter chr) {
        String pass = slea.readMapleAsciiString();
        final CashItemInfo item = CashItemFactory.getInstance().getItem(slea.readInt());
        String partnerName = slea.readMapleAsciiString();
        String msg = slea.readMapleAsciiString();
        if (item == null || c.getPlayer().getCSPoints(1) < item.getPrice() || msg.length() > 73 || msg.length() < 1) { //dont want packet editors gifting random stuff =P
            c.sendPacket(MTSCSPacket.sendCSFail(0));
            doCSPackets(c);
            return;
        }
        Pair<Integer, Pair<Integer, Integer>> info = MapleCharacterUtil.getInfoByName(partnerName, c.getPlayer().getWorld());
        if (info == null || info.getLeft().intValue() <= 0 || info.getLeft().intValue() == c.getPlayer().getId() || info.getRight().getLeft().intValue() == c.getAccID()) {
            c.sendPacket(MTSCSPacket.sendCSFail(0xD4)); //9E v75
            doCSPackets(c);
            return;
        } else if (!item.genderEquals(info.getRight().getRight().intValue())) {
            c.sendPacket(MTSCSPacket.sendCSFail(0xC4));
            doCSPackets(c);
            return;
        } else {
            for (int i : GameConstants.cashBlock) {
                if (item.getId() == i) {
                    c.getPlayer().dropMessage(1, GameConstants.getCashBlockedMsg(item.getId()));
                    doCSPackets(c);
                    return;
                }
            }
            c.getPlayer().getCashInventory().gift(info.getLeft().intValue(), c.getPlayer().getName(), msg, item.getSN(), MapleInventoryIdentifier.getInstance());
            c.getPlayer().modifyCSPoints(1, -item.getPrice(), false);
            c.sendPacket(MTSCSPacket.sendGift(item.getPrice(), item.getId(), item.getCount(), partnerName, true));
            //addCashshopLog(chr, item.getSN(), item.getItemId(), 1, item.getPrice(), item.getCount(), chr.getName() + " 购买道具: " + ii.getName(item.getItemId()) + " 送给 " + partnerName);
            chr.sendNote(partnerName, partnerName + " 您已收到" + chr.getName() + "送給您的禮物，請進入現金商城查看！");
            int chz = WorldFindService.getInstance().findChannel(partnerName);
            if (chz > 0) {
                MapleCharacter receiver = ChannelServer.getInstance(chz).getPlayerStorage().getCharacterByName(partnerName);
                if (receiver != null) {
                    receiver.showNote();
                }
            }
        }
    }

    public static final void UseXmaxsSurprise(final LittleEndianAccessor slea, final MapleClient c) {
        int CashId = (int) slea.readLong();
        IItem item = c.getPlayer().getCashInventory().findByCashId(CashId);
        if (item != null && item.getItemId() == 5222000 && item.getQuantity() > 0 && MapleInventoryManipulator.checkSpace(c, item.getItemId(), item.getQuantity(), item.getOwner())) {
            final int RewardIemId = RandomRewards.getInstance().getXmasreward();
            final CashItemInfo rewardItem = CashItemFactory.getInstance().getItem(RewardIemId);

            if (rewardItem == null) {

                c.sendPacket(MTSCSPacket.sendCSFail(0));
                doCSPackets(c);
                return;
            }
            for (int i : GameConstants.cashBlock) {
                if (rewardItem.getId() == i) {
                    c.getPlayer().dropMessage(1, GameConstants.getCashBlockedMsg(rewardItem.getId()));
                    doCSPackets(c);
                    return;
                }
            }

            IItem itemz = c.getPlayer().getCashInventory().toItem(rewardItem);
            if (itemz != null) {

                if (c.getPlayer().getCashInventory().getItemsSize() >= 100) {
                    c.sendPacket(MTSCSPacket.showXmasSurprise(true, CashId, itemz, c.getAccID()));
                    doCSPackets(c);
                    return;
                }
                c.getPlayer().getCashInventory().addToInventory(itemz);
                c.sendPacket(MTSCSPacket.showXmasSurprise(false, CashId, itemz, c.getAccID()));
                c.getPlayer().getCashInventory().removeFromInventory(item);
            } else {
                c.sendPacket(MTSCSPacket.sendCSFail(0));
            }

        }
//		doCSPackets(c);
        return;
    }

    private static final MapleInventoryType getInventoryType(final int id) {
        switch (id) {
            case 50200075:
                return MapleInventoryType.EQUIP;
            case 50200074:
                return MapleInventoryType.USE;
            case 50200073:
                return MapleInventoryType.ETC;
            default:
                return MapleInventoryType.UNDEFINED;
        }
    }

    private static final void doCSPackets(MapleClient c) {
        c.sendPacket(MTSCSPacket.getCSInventory(c));
        c.sendPacket(MTSCSPacket.showNXMapleTokens(c.getPlayer()));
        c.sendPacket(MTSCSPacket.enableCSUse());
        c.getPlayer().getCashInventory().checkExpire(c);
    }
}

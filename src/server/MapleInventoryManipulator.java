package server;

import java.awt.Point;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import client.inventory.MapleInventoryIdentifier;
import constants.GameConstants;
import client.inventory.Equip;
import client.inventory.IItem;
import client.inventory.InventoryException;
import client.inventory.Item;
import client.inventory.ItemFlag;
import client.PlayerStats;
import client.MapleBuffStat;
import client.inventory.MaplePet;
import client.MapleCharacter;
import client.MapleClient;
import constants.CopyItem.MapleEquipOnlyId;
import client.inventory.MapleInventoryType;
import constants.CopyItem.ModifyInventory;
import constants.ServerConstants;
import handling.world.World;
import java.util.ArrayList;
import java.util.Collections;
import server.maps.AramiaFireWorks;
import tools.packet.MTSCSPacket;
import tools.MaplePacketCreator;
import tools.FileoutputUtil;

public class MapleInventoryManipulator {

    public static String CheckCopyEquipItemsTxt1(String type, IItem id, short quantity, int equiponlyid) {
        return "[" + type + "] " + id + " 唯一:" + equiponlyid;
    }

    /*
     * 刪除所有裝備唯一ID
     */
    public static void removeAllByEquipOnlyId(MapleClient c, int equipOnlyId) {
        if (c.getPlayer() == null) {
            return;
        }
        boolean locked = false;
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        //背包裝備中的複製信息
        List<Item> copyEquipItems = c.getPlayer().getInventory(MapleInventoryType.EQUIP).listByEquipOnlyId(equipOnlyId);
        for (Item item : copyEquipItems) {
            if (item != null) {
                if (!locked) {
                    byte flag = item.getFlag();
                    flag |= ItemFlag.LOCK.getValue();
                    flag |= ItemFlag.UNTRADEABLE.getValue();
                    //flag |= ItemFlag.CRAFTED.getValue();
                    item.setFlag(flag);
                    item.setOwner("複製裝備");
                    c.getPlayer().forceUpdateItem(item);
                    c.getPlayer().dropMessage(5, "在背包中發現複製裝備[" + ii.getName(item.getItemId()) + "]已經將其鎖定。");
                    String msgtext = "玩家 " + c.getPlayer().getName() + " ID: " + c.getPlayer().getId() + " (等級 " + c.getPlayer().getLevel() + ") 地圖: " + c.getPlayer().getMapId() + " 在玩家背包中發現複製裝備 [" + item.getItemId() + ii.getName(item.getItemId()) + "] 已經將其鎖定。";
                    World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(6, "[GM消息] " + msgtext));
                    FileoutputUtil.log(FileoutputUtil.複製嫌疑, "[EQUIP] " + msgtext + " 道具唯一ID: " + item.getEquipOnlyId());
                    locked = true;
                } else {
                    removeFromSlot(c, MapleInventoryType.EQUIP, item.getPosition(), item.getQuantity(), true, false);
                    c.getPlayer().dropMessage(-11, "在背包中發現複製裝備[" + ii.getName(item.getItemId()) + "]已經將其刪除。");
                }
            }
        }
        //身上裝備中的複製信息
        List<Item> copyEquipedItems = c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).listByEquipOnlyId(equipOnlyId);
        for (Item item : copyEquipedItems) {
            if (item != null) {
                if (!locked) {
                    byte flag = item.getFlag();
                    flag |= ItemFlag.LOCK.getValue();
                    flag |= ItemFlag.UNTRADEABLE.getValue();
                    //flag |= ItemFlag.CRAFTED.getValue();
                    item.setFlag(flag);
                    item.setOwner("複製裝備");
                    c.getPlayer().forceUpdateItem(item);
                    c.getPlayer().dropMessage(5, "在穿戴中發現複製裝備[" + ii.getName(item.getItemId()) + "]已經將其鎖定。");
                    String msgtext = "玩家 " + c.getPlayer().getName() + " ID: " + c.getPlayer().getId() + " (等級 " + c.getPlayer().getLevel() + ") 地圖: " + c.getPlayer().getMapId() + " 在玩家背包中發現複製裝備 [" + ii.getName(item.getItemId()) + "] 已經將其鎖定。";
                    World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(6, "[GM消息] " + msgtext));
                    FileoutputUtil.log(FileoutputUtil.複製嫌疑, "[EQUIPPED] " + msgtext + " 道具唯一ID: " + item.getEquipOnlyId());
                    locked = true;
                } else {
                    removeFromSlot(c, MapleInventoryType.EQUIPPED, item.getPosition(), item.getQuantity(), true, false);
                    c.getPlayer().dropMessage(-11, "在穿戴中發現複製裝備[" + ii.getName(item.getItemId()) + "]已經將其刪除。");
                    c.getPlayer().equipChanged();
                }
            }
        }
    }

    /*清除裝備*/
    public static void removeAllById(MapleClient c, int itemId, boolean checkEquipped, String isGmName) {
        MapleInventoryType type = MapleItemInformationProvider.getInstance().getInventoryType(itemId);
        int Quantity = 0;
        for (IItem item : c.getPlayer().getInventory(type).listById(itemId)) {
            if (item != null) {
                removeFromSlot(c, type, item.getPosition(), item.getQuantity(), true, false);
                Quantity += 1;
            }
        }
        if (checkEquipped) {
            IItem ii = c.getPlayer().getInventory(type).findById(itemId);
            if (ii != null) {
                c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).removeItem(ii.getPosition());
                c.getPlayer().equipChanged();
            }
        }
        FileoutputUtil.log("刪除裝備.ini", FileoutputUtil.output_text(c, c.getPlayer(), "已被[" + (isGmName == "" ? "" : isGmName) + "]刪除了: [" + type + "] ", itemId + "x" + Quantity));
    }

    public static void addRing(MapleCharacter chr, int itemId, int ringId, int sn) {
        CashItemInfo csi = CashItemFactory.getInstance().getItem(sn);
        if (csi == null) {
            return;
        }
        IItem ring = chr.getCashInventory().toItem(csi, ringId);
        if (ring == null || ring.getUniqueId() != ringId || ring.getUniqueId() <= 0 || ring.getItemId() != itemId) {
            return;
        }
        chr.getCashInventory().addToInventory(ring);
        //chr.getClient().sendPacket(MTSCSPacket.confirmToCSInventory(ring, chr.getClient().getAccID(), csi.getSN()));
        chr.getClient().sendPacket(MTSCSPacket.showBoughtCSItem(ring, sn, chr.getClient().getAccID()));
    }

    public static boolean addbyItem(final MapleClient c, final IItem item) {
        return addbyItem(c, item, false) >= 0;
    }

    public static short addbyItem(final MapleClient c, final IItem item, final boolean fromcs) {
        final MapleInventoryType type = GameConstants.getInventoryType(item.getItemId());
        final short newSlot = c.getPlayer().getInventory(type).addItem(item);
        if (newSlot == -1) {
            if (!fromcs) {
                c.sendPacket(MaplePacketCreator.getInventoryFull());
                c.sendPacket(MaplePacketCreator.getShowInventoryFull());
            }
            return newSlot;
        }
        if (!fromcs) {
            c.sendPacket(MaplePacketCreator.modifyInventory(true, new ModifyInventory(ModifyInventory.Types.ADD, item)));
            //c.sendPacket(MaplePacketCreator.addInventorySlot(type, item));
        }
        if (item.hasSetOnlyId()) {//是否設定唯一ID
            item.setEquipOnlyId(MapleEquipOnlyId.getInstance().getNextEquipOnlyId());
        }
        if (ServerConstants.CheckCopyEquipItemsTxt && c.getPlayer().isGM()) //複製裝備移除文字顯示
        {
            //c.getPlayer().dropMessage(6, CheckCopyEquipItemsTxt1("獲得", item, item.getQuantity(), item.getEquipOnlyId()));
        }
        c.getPlayer().havePartyQuest(item.getItemId());
        if (!fromcs && type.equals(MapleInventoryType.EQUIP)) {
            c.getPlayer().checkCopyItems(); //檢測複製的裝備
        }
        return newSlot;
    }

    public static int getUniqueId(int itemId, MaplePet pet) {
        int uniqueid = -1;
        if (GameConstants.isPet(itemId)) {
            if (pet != null) {
                uniqueid = pet.getUniqueId();
            } else {
                uniqueid = MapleInventoryIdentifier.getInstance();
            }
        } else if (GameConstants.getInventoryType(itemId) == MapleInventoryType.CASH || MapleItemInformationProvider.getInstance().isCash(itemId)) { //less work to do
            uniqueid = MapleInventoryIdentifier.getInstance(); //shouldnt be generated yet, so put it here
        }
        return uniqueid;
    }

    public static boolean addById(MapleClient c, int itemId, short quantity) {
        return addById(c, itemId, quantity, null, null, 0);
    }

    public static boolean addById(MapleClient c, int itemId, short quantity, String owner) {
        return addById(c, itemId, quantity, owner, null, 0);
    }

    public static byte addId(MapleClient c, int itemId, short quantity, String owner) {
        return addId(c, itemId, quantity, owner, null, 0);
    }

    public static boolean addById(MapleClient c, int itemId, short quantity, String owner, MaplePet pet) {
        return addById(c, itemId, quantity, owner, pet, 0);
    }

    public static boolean addById(MapleClient c, int itemId, short quantity, String owner, MaplePet pet, long period) {
        return addId(c, itemId, quantity, owner, pet, period) >= 0;
    }

    public static byte addId(MapleClient c, int itemId, short quantity, String owner, MaplePet pet, long period) {
        final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        if (ii.isPickupRestricted(itemId) && c.getPlayer().haveItem(itemId, 1, true, false)) {
            c.sendPacket(MaplePacketCreator.getInventoryFull());
            c.sendPacket(MaplePacketCreator.showItemUnavailable());
            return -1;
        }
        final MapleInventoryType type = GameConstants.getInventoryType(itemId);
        int uniqueid = getUniqueId(itemId, pet);
        short newSlot = -1;
        if (!type.equals(MapleInventoryType.EQUIP)) {
            final short slotMax = ii.getSlotMax(c, itemId);
            final List<IItem> existing = c.getPlayer().getInventory(type).listById(itemId);
            if (!GameConstants.isRechargable(itemId)) {
                if (existing.size() > 0) { // first update all existing slots to slotMax
                    Iterator<IItem> i = existing.iterator();
                    while (quantity > 0) {
                        if (i.hasNext()) {
                            Item eItem = (Item) i.next();
                            short oldQ = eItem.getQuantity();
                            if (oldQ < slotMax && (eItem.getOwner().equals(owner) || owner == null) && eItem.getExpiration() == -1) {
                                short newQ = (short) Math.min(oldQ + quantity, slotMax);
                                quantity -= (newQ - oldQ);
                                eItem.setQuantity(newQ);
                                c.sendPacket(MaplePacketCreator.modifyInventory(false, new ModifyInventory(ModifyInventory.Types.UPDATE, eItem)));
                                //c.sendPacket(MaplePacketCreator.updateInventorySlot(type, eItem, false));
                            }
                        } else {
                            break;
                        }
                    }
                }
                Item nItem;
                // add new slots if there is still something left
                while (quantity > 0) {
                    short newQ = (short) Math.min(quantity, slotMax);
                    if (newQ != 0) {
                        quantity -= newQ;
                        nItem = new Item(itemId, (byte) 0, newQ, (byte) 0, uniqueid);

                        newSlot = c.getPlayer().getInventory(type).addItem(nItem);
                        if (newSlot == -1) {
                            c.sendPacket(MaplePacketCreator.getInventoryFull());
                            c.sendPacket(MaplePacketCreator.getShowInventoryFull());
                            return -1;
                        }
                        if (owner != null) {
                            nItem.setOwner(owner);
                        }
                        if (period > 0) {
                            if (period < 1000) {
                                nItem.setExpiration(System.currentTimeMillis() + (period * 24 * 60 * 60 * 1000));
                            } else {
                                nItem.setExpiration(System.currentTimeMillis() + period);
                            }
                        }
                        if (nItem.hasSetOnlyId()) {//是否設定唯一ID
                            nItem.setEquipOnlyId(MapleEquipOnlyId.getInstance().getNextEquipOnlyId());
                        }
                        if (ServerConstants.CheckCopyEquipItemsTxt && c.getPlayer().isGM()) //複製裝備移除文字顯示
                        {
                            //c.getPlayer().dropMessage(6, CheckCopyEquipItemsTxt1("未知addId", nItem, nItem.getQuantity(), nItem.getEquipOnlyId()));
                        }
                        if (pet != null) {
                            nItem.setPet(pet);
                            pet.setInventoryPosition(newSlot);
                            c.getPlayer().addPet(pet);
                        }
                        c.sendPacket(MaplePacketCreator.modifyInventory(false, new ModifyInventory(ModifyInventory.Types.ADD, nItem)));
                        //c.sendPacket(MaplePacketCreator.addInventorySlot(type, nItem));
                        if (GameConstants.isRechargable(itemId) && quantity == 0) {
                            break;
                        }
                    } else {
                        c.getPlayer().havePartyQuest(itemId);
                        c.sendPacket(MaplePacketCreator.enableActions());
                        return (byte) newSlot;
                    }
                }
            } else {
                // Throwing Stars and Bullets - Add all into one slot regardless of quantity.
                final Item nItem = new Item(itemId, (byte) 0, quantity, (byte) 0, uniqueid);
                newSlot = c.getPlayer().getInventory(type).addItem(nItem);

                if (newSlot == -1) {
                    c.sendPacket(MaplePacketCreator.getInventoryFull());
                    c.sendPacket(MaplePacketCreator.getShowInventoryFull());
                    return -1;
                }
                if (period > 0) {
                    nItem.setExpiration(System.currentTimeMillis() + (period * 24 * 60 * 60 * 1000));
                }
                c.sendPacket(MaplePacketCreator.modifyInventory(false, new ModifyInventory(ModifyInventory.Types.ADD, nItem)));
                //c.sendPacket(MaplePacketCreator.addInventorySlot(type, nItem));
                c.sendPacket(MaplePacketCreator.enableActions());
            }
        } else {
            if (quantity == 1) {
                final IItem nEquip = ii.getEquipById(itemId);
                if (owner != null) {
                    nEquip.setOwner(owner);
                }
                nEquip.setUniqueId(uniqueid);
                if (period > 0) {
                    if (period < 1000) {
                        nEquip.setExpiration(System.currentTimeMillis() + (period * 24 * 60 * 60 * 1000));
                    } else {
                        nEquip.setExpiration(System.currentTimeMillis() + period);
                    }
                }
                newSlot = c.getPlayer().getInventory(type).addItem(nEquip);
                if (newSlot == -1) {
                    c.sendPacket(MaplePacketCreator.getInventoryFull());
                    c.sendPacket(MaplePacketCreator.getShowInventoryFull());
                    return -1;
                }
                c.sendPacket(MaplePacketCreator.modifyInventory(false, new ModifyInventory(ModifyInventory.Types.ADD, nEquip)));
                //c.sendPacket(MaplePacketCreator.addInventorySlot(type, nEquip));
                c.getPlayer().checkCopyItems();//檢測複製的裝備
            } else {
                throw new InventoryException("Trying to create equip with non-one quantity");
            }
        }
        c.getPlayer().havePartyQuest(itemId);
        return (byte) newSlot;
    }

    public static IItem addbyId_Gachapon(final MapleClient c, final int itemId, short quantity) {
        if (c.getPlayer().getInventory(MapleInventoryType.EQUIP).getNextFreeSlot() == -1 || c.getPlayer().getInventory(MapleInventoryType.USE).getNextFreeSlot() == -1 || c.getPlayer().getInventory(MapleInventoryType.ETC).getNextFreeSlot() == -1 || c.getPlayer().getInventory(MapleInventoryType.SETUP).getNextFreeSlot() == -1) {
            return null;
        }
        final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        if (ii.isPickupRestricted(itemId) && c.getPlayer().haveItem(itemId, 1, true, false)) {
            c.sendPacket(MaplePacketCreator.getInventoryFull());
            c.sendPacket(MaplePacketCreator.showItemUnavailable());
            return null;
        }
        final MapleInventoryType type = GameConstants.getInventoryType(itemId);

        if (!type.equals(MapleInventoryType.EQUIP)) {
            short slotMax = ii.getSlotMax(c, itemId);
            final List<IItem> existing = c.getPlayer().getInventory(type).listById(itemId);

            if (!GameConstants.isRechargable(itemId)) {
                IItem nItem = null;
                boolean recieved = false;

                if (existing.size() > 0) { // first update all existing slots to slotMax
                    Iterator<IItem> i = existing.iterator();
                    while (quantity > 0) {
                        if (i.hasNext()) {
                            nItem = (Item) i.next();
                            short oldQ = nItem.getQuantity();

                            if (oldQ < slotMax) {
                                recieved = true;

                                short newQ = (short) Math.min(oldQ + quantity, slotMax);
                                quantity -= (newQ - oldQ);
                                nItem.setQuantity(newQ);
                                c.sendPacket(MaplePacketCreator.modifyInventory(false, new ModifyInventory(ModifyInventory.Types.UPDATE, nItem)));
                                //c.sendPacket(MaplePacketCreator.updateInventorySlot(type, nItem, false));
                            }
                        } else {
                            break;
                        }
                    }
                }
                // add new slots if there is still something left
                while (quantity > 0) {
                    short newQ = (short) Math.min(quantity, slotMax);
                    if (newQ != 0) {
                        quantity -= newQ;
                        nItem = new Item(itemId, (byte) 0, newQ, (byte) 0);
                        final short newSlot = c.getPlayer().getInventory(type).addItem(nItem);
                        if (newSlot == -1 && recieved) {
                            return nItem;
                        } else if (newSlot == -1) {
                            return null;
                        }
                        recieved = true;
                        c.sendPacket(MaplePacketCreator.modifyInventory(false, new ModifyInventory(ModifyInventory.Types.ADD, nItem)));
                        //c.sendPacket(MaplePacketCreator.addInventorySlot(type, nItem));
                        if (GameConstants.isRechargable(itemId) && quantity == 0) {
                            break;
                        }
                    } else {
                        break;
                    }
                }
                if (recieved) {
                    c.getPlayer().havePartyQuest(nItem.getItemId());
                    return nItem;
                }
            } else {
                // Throwing Stars and Bullets - Add all into one slot regardless of quantity.
                final Item nItem = new Item(itemId, (byte) 0, quantity, (byte) 0);
                final short newSlot = c.getPlayer().getInventory(type).addItem(nItem);

                if (newSlot == -1) {
                    return null;
                }
                c.sendPacket(MaplePacketCreator.modifyInventory(false, new ModifyInventory(ModifyInventory.Types.ADD, nItem)));
                //c.sendPacket(MaplePacketCreator.addInventorySlot(type, nItem));
                c.getPlayer().havePartyQuest(nItem.getItemId());
                return nItem;
            }
        } else {
            if (quantity == 1) {
                final IItem item = ii.randomizeStats((Equip) ii.getEquipById(itemId));
                final short newSlot = c.getPlayer().getInventory(type).addItem(item);

                if (newSlot == -1) {
                    return null;
                }
                if (item.hasSetOnlyId()) {//是否設定唯一ID
                    item.setEquipOnlyId(MapleEquipOnlyId.getInstance().getNextEquipOnlyId());
                }
                if (ServerConstants.CheckCopyEquipItemsTxt && c.getPlayer().isGM()) { //複製裝備移除文字顯示
                    c.getPlayer().dropMessage(6, CheckCopyEquipItemsTxt1("未知 1", item, item.getQuantity(), item.getEquipOnlyId()));
                }
                c.sendPacket(MaplePacketCreator.modifyInventory(true, new ModifyInventory(ModifyInventory.Types.ADD, item)));
                //c.sendPacket(MaplePacketCreator.addInventorySlot(type, item, true));
                c.getPlayer().havePartyQuest(item.getItemId());
                return item;
            } else {
                throw new InventoryException("Trying to create equip with non-one quantity");
            }
        }
        return null;
    }

    public static boolean addFromDrop(final MapleClient c, final IItem item, final boolean show) {
        return addFromDrop(c, item, show, false, false);
    }

    public static boolean addFromDrop(final MapleClient c, IItem item, final boolean show, final boolean enhance, final boolean isPetPickup) {
        final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();

        if (ii.isPickupRestricted(item.getItemId()) && c.getPlayer().haveItem(item.getItemId(), 1, true, false)) {
            c.sendPacket(MaplePacketCreator.getInventoryFull());
            c.sendPacket(MaplePacketCreator.showItemUnavailable());
            return false;
        }
        final int before = c.getPlayer().itemQuantity(item.getItemId());
        short quantity = item.getQuantity();
        final MapleInventoryType type = GameConstants.getInventoryType(item.getItemId());

        if (!type.equals(MapleInventoryType.EQUIP)) {
            final short slotMax = ii.getSlotMax(c, item.getItemId());
            final List<IItem> existing = c.getPlayer().getInventory(type).listById(item.getItemId());
            if (!GameConstants.isRechargable(item.getItemId())) {
                if (quantity <= 0) { //wth
                    c.sendPacket(MaplePacketCreator.getInventoryFull());
                    c.sendPacket(MaplePacketCreator.showItemUnavailable());
                    return false;
                }
                if (existing.size() > 0) { // first update all existing slots to slotMax
                    Iterator<IItem> i = existing.iterator();
                    while (quantity > 0) {
                        if (i.hasNext()) {
                            final Item eItem = (Item) i.next();
                            final short oldQ = eItem.getQuantity();
                            if (oldQ < slotMax && item.getOwner().equals(eItem.getOwner()) && item.getExpiration() == eItem.getExpiration()) {
                                final short newQ = (short) Math.min(oldQ + quantity, slotMax);
                                quantity -= (newQ - oldQ);
                                eItem.setQuantity(newQ);
                                if (isPetPickup) {
                                    c.sendPacket(MaplePacketCreator.modifyInventory(false, new ModifyInventory(ModifyInventory.Types.UPDATE, eItem)));
                                } else {
                                    c.sendPacket(MaplePacketCreator.modifyInventory(true, new ModifyInventory(ModifyInventory.Types.UPDATE, eItem)));
                                    //c.sendPacket(MaplePacketCreator.updateInventorySlot(type, eItem, true));
                                }
                            }
                        } else {
                            break;
                        }
                    }
                }
                // add new slots if there is still something left
                while (quantity > 0) {
                    final short newQ = (short) Math.min(quantity, slotMax);
                    quantity -= newQ;
                    final Item nItem = new Item(item.getItemId(), (byte) 0, newQ, item.getFlag());
                    nItem.setExpiration(item.getExpiration());
                    nItem.setOwner(item.getOwner());
                    nItem.setPet(item.getPet());
                    short newSlot = c.getPlayer().getInventory(type).addItem(nItem);
                    if (newSlot == -1) {
                        c.sendPacket(MaplePacketCreator.getInventoryFull());
                        c.sendPacket(MaplePacketCreator.getShowInventoryFull());
                        item.setQuantity((short) (quantity + newQ));
                        return false;
                    }
                    //c.sendPacket(MaplePacketCreator.addInventorySlot(type, nItem, true));
                    c.sendPacket(MaplePacketCreator.modifyInventory(true, new ModifyInventory(ModifyInventory.Types.ADD, nItem)));
                }
            } else {
                // Throwing Stars and Bullets - Add all into one slot regardless of quantity.
                final Item nItem = new Item(item.getItemId(), (byte) 0, quantity, item.getFlag());
                nItem.setExpiration(item.getExpiration());
                nItem.setOwner(item.getOwner());
                nItem.setPet(item.getPet());
                final short newSlot = c.getPlayer().getInventory(type).addItem(nItem);
                if (newSlot == -1) {
                    c.sendPacket(MaplePacketCreator.getInventoryFull());
                    c.sendPacket(MaplePacketCreator.getShowInventoryFull());
                    return false;
                }
                c.sendPacket(MaplePacketCreator.modifyInventory(false, new ModifyInventory(ModifyInventory.Types.ADD, nItem)));
                //c.sendPacket(MaplePacketCreator.addInventorySlot(type, nItem));
                c.sendPacket(MaplePacketCreator.enableActions());
            }
        } else {
            if (quantity == 1) {
                if (enhance) { //否需要重置潛能也就是角色剛從地上撿取怪物掉落的裝備
                    item = checkEnhanced(item, c.getPlayer());
                }
                if (item.hasSetOnlyId()) {//是否設定唯一ID
                    item.setEquipOnlyId(MapleEquipOnlyId.getInstance().getNextEquipOnlyId());
                }
                if (ServerConstants.CheckCopyEquipItemsTxt && c.getPlayer().isGM()) //複製裝備移除文字顯示
                {
                    c.getPlayer().dropMessage(6, CheckCopyEquipItemsTxt1("物品掉落撿起", item, item.getQuantity(), item.getEquipOnlyId()));
                }
                final short newSlot = c.getPlayer().getInventory(type).addItem(item);

                if (newSlot == -1) {
                    c.sendPacket(MaplePacketCreator.getInventoryFull());
                    c.sendPacket(MaplePacketCreator.getShowInventoryFull());
                    return false;
                }
                if (isPetPickup) {
                    c.sendPacket(MaplePacketCreator.modifyInventory(false, new ModifyInventory(ModifyInventory.Types.ADD, item)));
                } else {
                    c.sendPacket(MaplePacketCreator.modifyInventory(true, new ModifyInventory(ModifyInventory.Types.ADD, item)));
                    //c.sendPacket(MaplePacketCreator.addInventorySlot(type, item, true));
                }
                c.getPlayer().checkCopyItems(); //檢測複製的裝備
            } else {
                throw new RuntimeException("Trying to create equip with non-one quantity");
            }
        }
        if (item.getQuantity() >= 50 && GameConstants.isUpgradeScroll(item.getItemId())) {
            c.setMonitored(true);
        }
        if (before == 0) {
            switch (item.getItemId()) {
                case AramiaFireWorks.KEG_ID:
                    c.getPlayer().dropMessage(5, "You have gained a Powder Keg, you can give this in to Aramia of Henesys.");
                    break;
                case AramiaFireWorks.SUN_ID:
                    c.getPlayer().dropMessage(5, "You have gained a Warm Sun, you can give this in to Maple Tree Hill through @joyce.");
                    break;
                case AramiaFireWorks.DEC_ID:
                    c.getPlayer().dropMessage(5, "You have gained a Tree Decoration, you can give this in to White Christmas Hill through @joyce.");
                    break;
            }
        }
        c.getPlayer().havePartyQuest(item.getItemId());
        if (show) {
            c.sendPacket(MaplePacketCreator.getShowItemGain(item.getItemId(), item.getQuantity()));
        }
        return true;
    }

    private static final IItem checkEnhanced(final IItem before, final MapleCharacter chr) {
        if (before instanceof Equip) {
            final Equip eq = (Equip) before;
            if (eq.getState() == 0 && (eq.getUpgradeSlots() >= 1 || eq.getLevel() >= 1) && Randomizer.nextInt(100) > 80) { //20% chance of pot?
                eq.resetPotential();
                chr.dropMessage(5, "您已經獲得了一個隱藏潛力的項目。");
            }
        }
        return before;
    }

    private static int rand(int min, int max) {
        return Math.abs((int) Randomizer.rand(min, max));
    }

    public static boolean checkSpace(final MapleClient c, final int itemid, int quantity, final String owner) {
        final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        if (ii.isPickupRestricted(itemid) && c.getPlayer().haveItem(itemid, 1, true, false)) {
            c.sendPacket(MaplePacketCreator.enableActions());
            return false;
        }
        if (quantity <= 0 && !GameConstants.isRechargable(itemid)) {
            return false;
        }
        final MapleInventoryType type = GameConstants.getInventoryType(itemid);
        if (c == null || c.getPlayer() == null || c.getPlayer().getInventory(type) == null) { //wtf is causing this?
            return false;
        }
        if (!type.equals(MapleInventoryType.EQUIP)) {
            final short slotMax = ii.getSlotMax(c, itemid);
            final List<IItem> existing = c.getPlayer().getInventory(type).listById(itemid);
            if (!GameConstants.isRechargable(itemid)) {
                if (existing.size() > 0) { // first update all existing slots to slotMax
                    for (IItem eItem : existing) {
                        final short oldQ = eItem.getQuantity();
                        if (oldQ < slotMax && owner != null && owner.equals(eItem.getOwner())) {
                            final short newQ = (short) Math.min(oldQ + quantity, slotMax);
                            quantity -= (newQ - oldQ);
                        }
                        if (quantity <= 0) {
                            break;
                        }
                    }
                }
            }
            // add new slots if there is still something left
            final int numSlotsNeeded;
            if (slotMax > 0) {
                numSlotsNeeded = (int) (Math.ceil(((double) quantity) / slotMax));
            } else {
                numSlotsNeeded = 1;
            }
            return !c.getPlayer().getInventory(type).isFull(numSlotsNeeded - 1);
        } else {
            return !c.getPlayer().getInventory(type).isFull();
        }
    }

    public static void removeFromSlot(final MapleClient c, final MapleInventoryType type, final short slot, final short quantity, final boolean fromDrop) {
        removeFromSlot(c, type, slot, quantity, fromDrop, false);
    }

    public static void removeFromSlot(final MapleClient c, final MapleInventoryType type, final short slot, short quantity, final boolean fromDrop, final boolean consume) {
        if (c.getPlayer() == null || c.getPlayer().getInventory(type) == null) {
            return;
        }
        final IItem item = c.getPlayer().getInventory(type).getItem(slot);
        if (item != null) {
            final boolean allowZero = consume && GameConstants.isRechargable(item.getItemId());
            c.getPlayer().getInventory(type).removeItem(slot, quantity, allowZero);

            if (item.getQuantity() == 0 && !allowZero) {
                c.sendPacket(MaplePacketCreator.modifyInventory(fromDrop, new ModifyInventory(ModifyInventory.Types.REMOVE, item)));
                //c.sendPacket(MaplePacketCreator.clearInventoryItem(type, item.getPosition(), fromDrop));
            } else {
                c.sendPacket(MaplePacketCreator.modifyInventory(fromDrop, new ModifyInventory(ModifyInventory.Types.UPDATE, item)));
                //c.sendPacket(MaplePacketCreator.updateInventorySlot(type, (Item) item, fromDrop));
            }
        }
    }

    public static boolean removeById(final MapleClient c, final MapleInventoryType type, final int itemId, final int quantity, final boolean fromDrop, final boolean consume) {
        int remremove = quantity;
        for (IItem item : c.getPlayer().getInventory(type).listById(itemId)) {
            if (remremove <= item.getQuantity()) {
                removeFromSlot(c, type, item.getPosition(), (short) remremove, fromDrop, consume);
                remremove = 0;
                break;
            } else {
                remremove -= item.getQuantity();
                removeFromSlot(c, type, item.getPosition(), item.getQuantity(), fromDrop, consume);
            }
        }
        return remremove <= 0;
    }

    public static void move(final MapleClient c, final MapleInventoryType type, final short src, final short dst) {
        if (src < 0 || dst < 0 || dst > c.getPlayer().getInventory(type).getSlotLimit() || src == dst) {
            return;
        }
        final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        final IItem source = c.getPlayer().getInventory(type).getItem(src);
        final IItem initialTarget = c.getPlayer().getInventory(type).getItem(dst);
        if (source == null) {
            return;
        }
        short olddstQ = -1;
        if (initialTarget != null) {
            olddstQ = initialTarget.getQuantity();
        }
        final short oldsrcQ = source.getQuantity();
        final short slotMax = ii.getSlotMax(c, source.getItemId());
        c.getPlayer().getInventory(type).move(src, dst, slotMax);
        final List<ModifyInventory> mods = new ArrayList<>();
        if (!type.equals(MapleInventoryType.EQUIP) && initialTarget != null
                && initialTarget.getItemId() == source.getItemId()
                && initialTarget.getOwner().equals(source.getOwner())
                && initialTarget.getExpiration() == source.getExpiration()
                && !GameConstants.isRechargable(source.getItemId())
                && !type.equals(MapleInventoryType.CASH)) {
            if ((olddstQ + oldsrcQ) > slotMax) {
                mods.add(new ModifyInventory(ModifyInventory.Types.UPDATE, source));
                mods.add(new ModifyInventory(ModifyInventory.Types.UPDATE, initialTarget));
                //c.sendPacket(MaplePacketCreator.moveAndMergeWithRestInventoryItem(type, src, dst, (short) ((olddstQ + oldsrcQ) - slotMax), slotMax));
            } else {
                mods.add(new ModifyInventory(ModifyInventory.Types.REMOVE, source));
                mods.add(new ModifyInventory(ModifyInventory.Types.UPDATE, initialTarget));
                //c.sendPacket(MaplePacketCreator.moveAndMergeInventoryItem(type, src, dst, ((Item) c.getPlayer().getInventory(type).getItem(dst)).getQuantity()));
            }
        } else {
            //c.sendPacket(MaplePacketCreator.moveInventoryItem(type, src, dst));
            mods.add(new ModifyInventory(ModifyInventory.Types.MOVE, source, src));
        }
        c.sendPacket(MaplePacketCreator.modifyInventory(true, mods));
    }

    public static void equip(final MapleClient c, final short src, short dst) {
        final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        final MapleCharacter chr = c.getPlayer();
        if (chr == null) {
            return;
        }
        final PlayerStats statst = c.getPlayer().getStat();
        Equip source = (Equip) chr.getInventory(MapleInventoryType.EQUIP).getItem(src);
        Equip target = (Equip) chr.getInventory(MapleInventoryType.EQUIPPED).getItem(dst);

        if (source == null || source.getDurability() == 0) {
            c.sendPacket(MaplePacketCreator.enableActions());
            return;
        }

        final Map<String, Integer> stats = ii.getEquipStats(source.getItemId());
        if (dst < -999 && !GameConstants.isEvanDragonItem(source.getItemId())) {
            c.sendPacket(MaplePacketCreator.enableActions());
            return;
        } else if (dst >= -999 && dst < -99 && stats.get("cash") == 0) {
            c.sendPacket(MaplePacketCreator.enableActions());
            return;
        }
        if (!ii.canEquip(stats, source.getItemId(), chr.getLevel(), chr.getJob(), chr.getFame(), statst.getTotalStr(), statst.getTotalDex(), statst.getTotalLuk(), statst.getTotalInt(), c.getPlayer().getStat().levelBonus)) {
            c.sendPacket(MaplePacketCreator.enableActions());
            return;
        }
        if (GameConstants.isWeapon(source.getItemId()) && dst != -10 && dst != -11) {
            AutobanManager.getInstance().autoban(c, "Equipment hack, itemid " + source.getItemId() + " to slot " + dst);
            return;
        }
        if (!ii.isCash(source.getItemId()) && !GameConstants.isMountItemAvailable(source.getItemId(), c.getPlayer().getJob())) {
            c.sendPacket(MaplePacketCreator.enableActions());
            return;
        }
        if (GameConstants.isKatara(source.getItemId())) {
            dst = (byte) -10; //shield slot
        }
        if (GameConstants.isEvanDragonItem(source.getItemId()) && (chr.getJob() < 2200 || chr.getJob() > 2218)) {
            c.sendPacket(MaplePacketCreator.enableActions());
            return;
        }

        switch (dst) {
            case -6: { // Top
                final IItem top = chr.getInventory(MapleInventoryType.EQUIPPED).getItem((byte) -5);
                if (top != null && GameConstants.isOverall(top.getItemId())) {
                    if (chr.getInventory(MapleInventoryType.EQUIP).isFull()) {
                        c.sendPacket(MaplePacketCreator.getInventoryFull());
                        c.sendPacket(MaplePacketCreator.getShowInventoryFull());
                        return;
                    }
                    unequip(c, (byte) -5, chr.getInventory(MapleInventoryType.EQUIP).getNextFreeSlot());
                }
                break;
            }
            case -5: {
                final IItem top = chr.getInventory(MapleInventoryType.EQUIPPED).getItem((byte) -5);
                final IItem bottom = chr.getInventory(MapleInventoryType.EQUIPPED).getItem((byte) -6);
                if (top != null && GameConstants.isOverall(source.getItemId())) {
                    if (chr.getInventory(MapleInventoryType.EQUIP).isFull(bottom != null && GameConstants.isOverall(source.getItemId()) ? 1 : 0)) {
                        c.sendPacket(MaplePacketCreator.getInventoryFull());
                        c.sendPacket(MaplePacketCreator.getShowInventoryFull());
                        return;
                    }
                    unequip(c, (byte) -5, chr.getInventory(MapleInventoryType.EQUIP).getNextFreeSlot());
                }
                if (bottom != null && GameConstants.isOverall(source.getItemId())) {
                    if (chr.getInventory(MapleInventoryType.EQUIP).isFull()) {
                        c.sendPacket(MaplePacketCreator.getInventoryFull());
                        c.sendPacket(MaplePacketCreator.getShowInventoryFull());
                        return;
                    }
                    unequip(c, (byte) -6, chr.getInventory(MapleInventoryType.EQUIP).getNextFreeSlot());
                }
                break;
            }
            case -10: { // Shield
                IItem weapon = chr.getInventory(MapleInventoryType.EQUIPPED).getItem((byte) -11);
                if (GameConstants.isKatara(source.getItemId())) {
                    if ((chr.getJob() != 900 && (chr.getJob() < 430 || chr.getJob() > 434)) || weapon == null || !GameConstants.isDagger(weapon.getItemId())) {
                        c.sendPacket(MaplePacketCreator.getInventoryFull());
                        c.sendPacket(MaplePacketCreator.getShowInventoryFull());
                        return;
                    }
                } else if (weapon != null && GameConstants.isTwoHanded(weapon.getItemId())) {
                    if (chr.getInventory(MapleInventoryType.EQUIP).isFull()) {
                        c.sendPacket(MaplePacketCreator.getInventoryFull());
                        c.sendPacket(MaplePacketCreator.getShowInventoryFull());
                        return;
                    }
                    unequip(c, (byte) -11, chr.getInventory(MapleInventoryType.EQUIP).getNextFreeSlot());
                }
                break;
            }
            case -11: { // Weapon
                IItem shield = chr.getInventory(MapleInventoryType.EQUIPPED).getItem((byte) -10);
                if (shield != null && GameConstants.isTwoHanded(source.getItemId())) {
                    if (chr.getInventory(MapleInventoryType.EQUIP).isFull()) {
                        c.sendPacket(MaplePacketCreator.getInventoryFull());
                        c.sendPacket(MaplePacketCreator.getShowInventoryFull());
                        return;
                    }
                    unequip(c, (byte) -10, chr.getInventory(MapleInventoryType.EQUIP).getNextFreeSlot());
                }
                break;
            }
        }
        source = (Equip) chr.getInventory(MapleInventoryType.EQUIP).getItem(src); // Equip
        target = (Equip) chr.getInventory(MapleInventoryType.EQUIPPED).getItem(dst); // Currently equipping
        if (source == null) {
            c.sendPacket(MaplePacketCreator.enableActions());
            return;
        }
        final List<ModifyInventory> mods = new ArrayList<>();
        if (stats.get("equipTradeBlock") == 1) { // Block trade when equipped.
            byte flag = source.getFlag();
            if (!ItemFlag.UNTRADEABLE.check(flag)) {
                flag |= ItemFlag.UNTRADEABLE.getValue();
                source.setFlag(flag);
                mods.add(new ModifyInventory(ModifyInventory.Types.REMOVE, source));
                mods.add(new ModifyInventory(ModifyInventory.Types.ADD, source.copy()));//to prevent crashes
                //c.sendPacket(MaplePacketCreator.updateSpecialItemUse_(source, GameConstants.getInventoryType(source.getItemId()).getType()));
            }
        }

        chr.getInventory(MapleInventoryType.EQUIP).removeSlot(src);
        if (target != null) {
            chr.getInventory(MapleInventoryType.EQUIPPED).removeSlot(dst);
        }
        source.setPosition(dst);
        chr.getInventory(MapleInventoryType.EQUIPPED).addFromDB(source);
        if (target != null) {
            target.setPosition(src);
            chr.getInventory(MapleInventoryType.EQUIP).addFromDB(target);
        }
        if (GameConstants.isWeapon(source.getItemId())) {
            if (chr.getBuffedValue(MapleBuffStat.BOOSTER) != null) {
                chr.cancelBuffStats(MapleBuffStat.BOOSTER);
            }
            if (chr.getBuffedValue(MapleBuffStat.SPIRIT_CLAW) != null) {
                chr.cancelBuffStats(MapleBuffStat.SPIRIT_CLAW);
            }
            if (chr.getBuffedValue(MapleBuffStat.SOULARROW) != null) {
                chr.cancelBuffStats(MapleBuffStat.SOULARROW);
            }
            if (chr.getBuffedValue(MapleBuffStat.WK_CHARGE) != null) {
                chr.cancelBuffStats(MapleBuffStat.WK_CHARGE);
            }
            if (chr.getBuffedValue(MapleBuffStat.LIGHTNING_CHARGE) != null) {
                chr.cancelBuffStats(MapleBuffStat.LIGHTNING_CHARGE);
            }
        }
        /*if (GameConstants.isDragonItem(source.getItemId())) {
         chr.finishAchievement(8);
         } else if (GameConstants.isReverseItem(source.getItemId())) {
         chr.finishAchievement(9);
         } else if (GameConstants.isTimelessItem(source.getItemId())) {
         chr.finishAchievement(10);
         } else */
        if (source.getItemId() == 1122017) {
            chr.startFairySchedule(true, true);
        }else if(source.getItemId() == 1112023) {
            c.getPlayer().cancelReincarnat();
             if(!c.getPlayer().haveItem(2109011))
                c.getPlayer().gainItem(2109011, (short)1);
        }
        mods.add(new ModifyInventory(ModifyInventory.Types.MOVE, source, src));
        c.sendPacket(MaplePacketCreator.modifyInventory(true, mods));
        //c.sendPacket(MaplePacketCreator.moveInventoryItem(MapleInventoryType.EQUIP, src, dst, (byte) 2));
        chr.equipChanged();
    }

    public static void unequip(final MapleClient c, final short src, final short dst) {
        Equip source = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(src);
        Equip target = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem(dst);

        if (dst < 0 || source == null) {
            return;
        }
        if (target != null && src <= 0) { // do not allow switching with equip
            c.sendPacket(MaplePacketCreator.getInventoryFull());
            return;
        }
        c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).removeSlot(src);
        if (target != null) {
            c.getPlayer().getInventory(MapleInventoryType.EQUIP).removeSlot(dst);
        }
        source.setPosition(dst);
        c.getPlayer().getInventory(MapleInventoryType.EQUIP).addFromDB(source);
        if (target != null) {
            target.setPosition(src);
            c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).addFromDB(target);
        }

        if (GameConstants.isWeapon(source.getItemId())) {
            if (c.getPlayer().getBuffedValue(MapleBuffStat.BOOSTER) != null) {
                c.getPlayer().cancelBuffStats(MapleBuffStat.BOOSTER);
            }
            if (c.getPlayer().getBuffedValue(MapleBuffStat.SPIRIT_CLAW) != null) {
                c.getPlayer().cancelBuffStats(MapleBuffStat.SPIRIT_CLAW);
            }
            if (c.getPlayer().getBuffedValue(MapleBuffStat.SOULARROW) != null) {
                c.getPlayer().cancelBuffStats(MapleBuffStat.SOULARROW);
            }
            if (c.getPlayer().getBuffedValue(MapleBuffStat.WK_CHARGE) != null) {
                c.getPlayer().cancelBuffStats(MapleBuffStat.WK_CHARGE);
            }
        }
        if (source.getItemId() == 1122017) {
            c.getPlayer().cancelFairySchedule(true);
        }else if(source.getItemId() == 1112023 )
        {
            c.getPlayer().cancelReincarnat();
            if(c.getPlayer().haveItem(2109011))
                c.getPlayer().gainItem(2109011, (short)-1);
            else if(c.getPlayer().haveItem(2109012))
                c.getPlayer().gainItem(2109012, (short)-1);
        }
        c.sendPacket(MaplePacketCreator.modifyInventory(true, Collections.singletonList(new ModifyInventory(ModifyInventory.Types.MOVE, source, src))));
        //c.sendPacket(MaplePacketCreator.moveInventoryItem(MapleInventoryType.EQUIP, src, dst, (byte) 1));
        c.getPlayer().equipChanged();
    }

    public static boolean drop(final MapleClient c, MapleInventoryType type, final short src, final short quantity) {
        return drop(c, type, src, quantity, false);
    }

    public static boolean drop(final MapleClient c, MapleInventoryType type, final short src, short quantity, final boolean npcInduced) {
        final String drop_items = "物品處理/丟棄/" + c.getPlayer().getName() + ".ini";
        final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        if (src < 0) {
            type = MapleInventoryType.EQUIPPED;
        }
        if (c.getPlayer() == null) {
            return false;
        }
        final IItem source = c.getPlayer().getInventory(type).getItem(src);
        if (/*quantity < 0 || */source == null || (!npcInduced && GameConstants.isPet(source.getItemId())) /*|| (quantity == 0 && !GameConstants.isRechargable(source.getItemId()))*/) {
            c.sendPacket(MaplePacketCreator.enableActions());
            return false;
        }

        final byte flag = source.getFlag();
        if (quantity > source.getQuantity() && (!GameConstants.isRechargable(source.getItemId()))) {
            c.sendPacket(MaplePacketCreator.enableActions());
            return false;
        }
        if (ItemFlag.LOCK.check(flag) || (quantity != 1 && type == MapleInventoryType.EQUIP)) { // hack
            c.sendPacket(MaplePacketCreator.enableActions());
            return false;
        }
        final Point dropPos = new Point(c.getPlayer().getPosition());
        c.getPlayer().getCheatTracker().checkDrop();
        if (quantity < source.getQuantity() && !GameConstants.isRechargable(source.getItemId())) {
            final IItem target = source.copy();
            target.setQuantity(quantity);
            source.setQuantity((short) (source.getQuantity() - quantity));
            c.sendPacket(MaplePacketCreator.dropInventoryItemUpdate(type, source));
            FileoutputUtil.log(drop_items, FileoutputUtil.output_text(c, c.getPlayer(), "丟棄道具:" + target.getItemId() + "x" + target.getQuantity() + "-" + ii.getName(target.getItemId()), "唯一ID:" + target.getEquipOnlyId()));
            if (ii.isDropRestricted(target.getItemId()) || ii.isAccountShared(target.getItemId())) {
                if (ItemFlag.KARMA_EQ.check(flag)) {
                    target.setFlag((byte) (flag - ItemFlag.KARMA_EQ.getValue()));
                    c.getPlayer().getMap().spawnItemDrop(c.getPlayer(), c.getPlayer(), target, dropPos, true, true);
                } else if (ItemFlag.KARMA_USE.check(flag)) {
                    target.setFlag((byte) (flag - ItemFlag.KARMA_USE.getValue()));
                    c.getPlayer().getMap().spawnItemDrop(c.getPlayer(), c.getPlayer(), target, dropPos, true, true);
                } else {
                    c.getPlayer().getMap().disappearingItemDrop(c.getPlayer(), c.getPlayer(), target, dropPos);
                }
            } else {
                if (GameConstants.isPet(source.getItemId()) || ItemFlag.UNTRADEABLE.check(flag)) {
                    c.getPlayer().getMap().disappearingItemDrop(c.getPlayer(), c.getPlayer(), target, dropPos);
                } else {
                    c.getPlayer().getMap().spawnItemDrop(c.getPlayer(), c.getPlayer(), target, dropPos, true, true);
                }
            }
        } else {
            c.getPlayer().getInventory(type).removeSlot(src);
            c.sendPacket(MaplePacketCreator.dropInventoryItem((src < 0 ? MapleInventoryType.EQUIP : type), src));
            if (src < 0) {
                c.getPlayer().equipChanged();
            }
            FileoutputUtil.log(drop_items, FileoutputUtil.output_text(c, c.getPlayer(), "丟棄道具: " + source.getItemId() + "x" + source.getQuantity() + "-" + ii.getName(source.getItemId()), "唯一ID: " + source.getEquipOnlyId()));
            if (ii.isDropRestricted(source.getItemId()) || ii.isAccountShared(source.getItemId())) {
                if (ItemFlag.KARMA_EQ.check(flag)) {
                    source.setFlag((byte) (flag - ItemFlag.KARMA_EQ.getValue()));
                    c.getPlayer().getMap().spawnItemDrop(c.getPlayer(), c.getPlayer(), source, dropPos, true, true);
                } else if (ItemFlag.KARMA_USE.check(flag)) {
                    source.setFlag((byte) (flag - ItemFlag.KARMA_USE.getValue()));
                    c.getPlayer().getMap().spawnItemDrop(c.getPlayer(), c.getPlayer(), source, dropPos, true, true);
                } else {
                    c.getPlayer().getMap().disappearingItemDrop(c.getPlayer(), c.getPlayer(), source, dropPos);
                }
            } else {
                if (GameConstants.isPet(source.getItemId()) || ItemFlag.UNTRADEABLE.check(flag)) {
                    c.getPlayer().getMap().disappearingItemDrop(c.getPlayer(), c.getPlayer(), source, dropPos);
                } else {
                    c.getPlayer().getMap().spawnItemDrop(c.getPlayer(), c.getPlayer(), source, dropPos, true, true);
                }
            }
        }
        return true;
    }
}

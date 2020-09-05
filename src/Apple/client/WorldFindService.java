/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Apple.client;

import client.MapleCharacter;
import constants.ServerConstants;
import handling.cashshop.CashShopServer;
import handling.channel.ChannelServer;
import handling.world.CharacterIdChannelPair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author PlayDK
 */
public class WorldFindService {

    private final ReentrantReadWriteLock lock;
    private final HashMap<Integer, Integer> idToChannel;
    private final HashMap<String, Integer> nameToChannel;
    private static final HashMap<Integer, Long> DC_List = new HashMap<>();

    private WorldFindService() {
        System.err.println("正在啟動[WorldFindService]");
        lock = new ReentrantReadWriteLock();
        idToChannel = new HashMap<>();
        nameToChannel = new HashMap<>();
    }

    public static WorldFindService getInstance() {
        return SingletonHolder.instance;
    }

    public void register(int chrId, String chrName, int channel) {
        lock.writeLock().lock();
        try {
            idToChannel.put(chrId, channel);
            nameToChannel.put(chrName.toLowerCase(), channel);
        } finally {
            lock.writeLock().unlock();
        }
        if (ServerConstants.角色註冊撤銷) {
            if (channel == -10) {
                System.out.println("玩家連接 - 角色ID: " + chrId + " 名字: " + chrName + " 進入商城");
            } else if (channel == -20) {
                System.out.println("玩家連接 - 角色ID: " + chrId + " 名字: " + chrName + " 進入拍賣");
            } else if (channel > -1) {
                System.out.println("玩家連接 - 角色ID: " + chrId + " 名字: " + chrName + " 頻道: " + channel);
            } else {
                System.out.println("玩家連接 - 角色ID: " + chrId + " 未處理的頻道...");
            }
        }
    }

    public void forceDeregister(int chrId) {
        lock.writeLock().lock();
        try {
            idToChannel.remove(chrId);
        } finally {
            lock.writeLock().unlock();
        }
        if (ServerConstants.角色註冊撤銷) {
            System.out.println("玩家離開 - 角色ID: " + chrId);
        }
    }

    public void forceDeregister(String chrName) {
        lock.writeLock().lock();
        try {
            nameToChannel.remove(chrName.toLowerCase());
        } finally {
            lock.writeLock().unlock();
        }
        if (ServerConstants.角色註冊撤銷) {
            System.out.println("玩家離開 - 角色名字: " + chrName);
        }
    }

    public void forceDeregister(int chrId, String chrName, String txt) {
        lock.writeLock().lock();
        try {
            idToChannel.remove(chrId);
            nameToChannel.remove(chrName.toLowerCase());
        } finally {
            lock.writeLock().unlock();
        }
        if (ServerConstants.角色註冊撤銷) {
           System.out.println("玩家離開 - 角色ID: " + chrId + " 名字: " + chrName + " <" + txt + ">");
        }
    }

    public void forceDeregisterEx(int chrId, String chrName) {
        lock.writeLock().lock();
        try {
            idToChannel.remove(chrId);
            nameToChannel.remove(chrName.toLowerCase());
        } finally {
            lock.writeLock().unlock();
        }
        System.out.println("清理卡號玩家 - 角色ID: " + chrId + " 名字: " + chrName);
    }

    public void registerDisconnect(int id) {
        lock.writeLock().lock();
        try {
            DC_List.put(id, System.currentTimeMillis());
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void forceDeregisterDisconnect(int id) {
        lock.writeLock().lock();
        try {
            DC_List.remove(id);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public int findDisconnect(int id) {
        Integer ret = null;
        lock.readLock().lock();
        try {
            if (DC_List.containsKey(id)) {
                if ((System.currentTimeMillis() - DC_List.get(id)) / 1000 <= 60) {
                    ret = id;
                }
            }
        } finally {
            lock.readLock().unlock();
        }
        if (ret != null) {
            return ret;
        }
        return -1;
    }

    /*
     * 通過角色的ID 找到角色的頻道
     */
    public int findChannel(int chrId) {
        Integer ret;
        lock.readLock().lock();
        try {
            ret = idToChannel.get(chrId);
        } finally {
            lock.readLock().unlock();
        }
        if (ret != null) {
            if (ret != -10 && ret != -20 && ChannelServer.getInstance(ret) == null) { //wha
                forceDeregister(chrId);
                return -1;
            }
            return ret;
        }
        return -1;
    }

    /*
     * 通過角色的名字 找到角色的頻道
     */
    public int findChannel(String chrName) {
        Integer ret;
        lock.readLock().lock();
        try {
            ret = nameToChannel.get(chrName.toLowerCase());
        } finally {
            lock.readLock().unlock();
        }
        if (ret != null) {
            /*
             * 如果找到了這個角色 但是這個頻道是空的 就刪除這個角色註冊到服務端的信息 返回 -1
             */
            if (ret != -10 && ret != -20 && ChannelServer.getInstance(ret) == null) {
                forceDeregister(chrName);
                return -1;
            }
            return ret;
        }
        return -1;
    }

    public MapleCharacter findChr(int st) {
        Integer ret;

        lock.readLock().lock();
        try {
            ret = idToChannel.get(st);
        } finally {
            lock.readLock().unlock();
        }
        if (ret != null) {
            if (ChannelServer.getInstance(ret) == null) {
                return CashShopServer.getPlayerStorage().getCharacterById(st);
            } else {
                return ChannelServer.getInstance(ret).getPlayerStorage().getCharacterById(st);
            }
        }
        return null;
    }

    /*
     * 好友列表獲取 好友的在線信息
     */
    public CharacterIdChannelPair[] multiBuddyFind(int charIdFrom, int[] characterIds) {
        List<CharacterIdChannelPair> foundsChars = new ArrayList<>(characterIds.length);
        for (int i : characterIds) {
            int channel = findChannel(i);
            if (channel > 0) {
                foundsChars.add(new CharacterIdChannelPair(i, channel));
            }
        }
        Collections.sort(foundsChars);
        return foundsChars.toArray(new CharacterIdChannelPair[foundsChars.size()]);
    }

    /*
     * 通過角色名字 找到角色信息
     */
    public MapleCharacter findCharacterByName(String name) {
        int ch = findChannel(name);
        if (ch > 0) {
            return ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(name);
        }
        return null;
    }

    /*
     * 通過角色ID 找到角色信息
     */
    public MapleCharacter findCharacterById(int id) {
        int ch = findChannel(id);
        if (ch > 0) {
            return ChannelServer.getInstance(ch).getPlayerStorage().getCharacterById(id);
        }
        return null;
    }

    private static class SingletonHolder {

        protected static final WorldFindService instance = new WorldFindService();
    }
}

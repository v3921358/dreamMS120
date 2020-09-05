package handling.channel;

import client.MapleCharacter;
import client.MapleCharacterUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import handling.world.CharacterTransfer;
import handling.world.CheaterData;
import Apple.client.WorldFindService;
import redis.clients.jedis.Jedis;
import server.Timer.PingTimer;
//import server.console.groups.datamanage.PlayerPane;
import Apple.client.java.JsonUtil;
import Apple.client.java.RedisUtil;
import Apple.console.groups.datamanage.PlayerPane;
import client.MapleClient;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import server.Timer.PlayerTimer;
import server.Timer.WorldTimer;
import tools.FileoutputUtil;

public class PlayerStorage {

    private final ReentrantReadWriteLock mutex = new ReentrantReadWriteLock();
    private final Lock readLock = mutex.readLock(), writeLock = mutex.writeLock();
    private final ReentrantReadWriteLock mutex2 = new ReentrantReadWriteLock();
    private final Lock connectcheckReadLock = mutex2.readLock(), pendingWriteLock = mutex2.writeLock();
    private final Map<String, MapleCharacter> nameToChar = new LinkedHashMap<>();
    private final Map<Integer, MapleCharacter> idToChar = new LinkedHashMap<>();
    private final Map<Integer, CharacterTransfer> PendingCharacter = new HashMap<>();
    private final PlayerObservable playerObservable = new PlayerObservable();
    private final int channel;

    private final ReentrantReadWriteLock mutex3 = new ReentrantReadWriteLock();
    private final Lock readLock3 = mutex3.readLock(), writeLock3 = mutex3.writeLock();
    private final Map<Integer, MapleClient> PendingClient = new HashMap<>();

    public PlayerStorage(int channel) {
        this.channel = channel;
        //PlayerTimer.getInstance().register(new UpdateCacheTask(), 10 * 1000);
        PingTimer.getInstance().register(new PersistingTask(), 60 * 1000);
        PingTimer.getInstance().register(new ConnectChecker(), 60 * 1000); //60秒檢測1次
    }

    public ArrayList<MapleCharacter> getAllCharacters() {
        readLock.lock();
        try {
            return new ArrayList<>(idToChar.values());
        } finally {
            readLock.unlock();
        }
    }

    public final List<MapleCharacter> getAllCharactersThreadSafe() {
        List<MapleCharacter> ret = new ArrayList<>();
        try {
            ret.addAll(getAllCharacters());
        } catch (ConcurrentModificationException ex) {

        }
        return ret;
    }

    /*
     * 註冊角色到服務器上
     */
    public void registerPlayer(MapleCharacter chr) {
        writeLock.lock();
        try {
            nameToChar.put(chr.getName().toLowerCase(), chr);
            idToChar.put(chr.getId(), chr);
            playerObservable.changed();
            PlayerPane.getInstance(null).registerIDs(chr.getId(), chr.getPlayerObservable());
        } finally {
            writeLock.unlock();
        }
        WorldFindService.getInstance().register(chr.getId(), chr.getName(), channel);
    }

    /*
     * 註冊臨時客戶端信息到服務器上
     */
    public final void registerPendingClient(final MapleClient c, final int playerid) {
        writeLock3.lock();
        try {
            PendingClient.put(playerid, c);//new Pair(System.currentTimeMillis(), chr));
        } finally {
            writeLock3.unlock();
        }
    }

    /*
     * 註冊臨時角色信息到服務器上
     */
    public void registerPendingPlayer(CharacterTransfer chr, int playerId) {
//        Jedis jedis = RedisUtil.getJedis();
//        try {
//            jedis.hset(RedisUtil.KEYNAMES.PLAYER_DATA.getKeyName(), String.valueOf(playerId), JsonUtil.getMapperInstance().writeValueAsString(chr));
//            PendingCharacter.put(playerId, chr.TranferTime);
//        } catch (JsonProcessingException e) {
//            log.error("註冊臨時角色信息到服務器出錯", e);
//        } finally {
//            RedisUtil.returnResource(jedis);
//        }
        writeLock.lock();
        try {
            PendingCharacter.put(playerId, chr);
        } finally {
            writeLock.unlock();
        }
    }

    /*
     * 通過 chr
     * 註銷角色登記信息
     */
    public void deregisterPlayer(MapleCharacter chr, String txt) {
        removePlayer(chr.getId(), chr.getName());
        WorldFindService.getInstance().forceDeregister(chr.getId(), chr.getName(), txt);
    }

    /*
     * 通過 角色ID 和 角色名字
     * 註銷角色登記信息
     */
    public void deregisterPlayer(int idz, String namez, String txt) {
        removePlayer(idz, namez);
        WorldFindService.getInstance().forceDeregister(idz, namez, txt);
    }

    public final void deregisterPendingClient(final int charid) {
        writeLock3.lock();
        try {
            PendingClient.remove(charid);
        } finally {
            writeLock3.unlock();
        }
    }

    /*
     * 通過 chr
     * 斷開角色登記信息
     */
    public void disconnectPlayer(MapleCharacter chr) {
        removePlayer(chr.getId(), chr.getName());
        WorldFindService.getInstance().forceDeregisterEx(chr.getId(), chr.getName());
    }

    private void removePlayer(int idz, String namez) {
        writeLock.lock();
        try {
            nameToChar.remove(namez.toLowerCase());
            MapleCharacter chr = idToChar.remove(idz);
            if (chr != null) {
                chr.setOnlineTime();
                PlayerPane.getInstance(null).removeIDs(chr.getId(), chr.getPlayerObservable());
            }
            playerObservable.changed();
        } finally {
            writeLock.unlock();
        }
    }

    public final MapleClient getPendingClient(final int charid) {
        final MapleClient toreturn;
        readLock3.lock();
        try {
            toreturn = PendingClient.get(charid);//.right;
        } finally {
            readLock3.unlock();
        }
        if (toreturn != null) {
            deregisterPendingClient(charid);
        }
        return toreturn;
    }

    public CharacterTransfer getPendingCharacter(int playerId) {
        writeLock.lock();
        try {
            return PendingCharacter.remove(playerId);
        } finally {
            writeLock.unlock();
        }
    }

    public MapleCharacter getCharacterByName(String name) {
        readLock.lock();
        try {
            return nameToChar.get(name.toLowerCase());
        } finally {
            readLock.unlock();
        }
    }

    public MapleCharacter getCharacterById(int id) {
        readLock.lock();
        try {
            return idToChar.get(id);
        } finally {
            readLock.unlock();
        }
    }

    public int getConnectedClients() {
        return idToChar.size();
    }

    public List<CheaterData> getCheaters() {
        List<CheaterData> cheaters = new ArrayList<>();
        readLock.lock();
        try {
            final Iterator<MapleCharacter> itr = nameToChar.values().iterator();
            MapleCharacter chr;
            while (itr.hasNext()) {
                chr = itr.next();
                if (chr.getCheatTracker().getPoints() > 0) {
                    cheaters.add(new CheaterData(chr.getCheatTracker().getPoints(), MapleCharacterUtil.makeMapleReadable(chr.getName()) + " ID: " + chr.getId() + " (" + chr.getCheatTracker().getPoints() + ") " + chr.getCheatTracker().getSummary()));
                }
            }
        } finally {
            readLock.unlock();
        }
        return cheaters;
    }

    public List<CheaterData> getReports() {
        List<CheaterData> cheaters = new ArrayList<>();
        readLock.lock();
        try {
            final Iterator<MapleCharacter> itr = nameToChar.values().iterator();
            MapleCharacter chr;
            while (itr.hasNext()) {
                chr = itr.next();
                if (chr.getReportPoints() > 0) {
                    cheaters.add(new CheaterData(chr.getReportPoints(), MapleCharacterUtil.makeMapleReadable(chr.getName()) + " ID: " + chr.getId() + " (" + chr.getReportPoints() + ") " + chr.getReportSummary()));
                }
            }
        } finally {
            readLock.unlock();
        }
        return cheaters;
    }

    /*
     * 斷開所有非GM角色連接
     */
    public void disconnectAll() {
        disconnectAll(false);
    }

    /*
     * 斷開所有角色連接
     */
    public void disconnectAll(boolean checkGM) {
        writeLock.lock();
        try {
            Iterator<MapleCharacter> chrit = nameToChar.values().iterator();
            MapleCharacter chr;
            while (chrit.hasNext()) {
                chr = chrit.next();
                if (!chr.isGM() || !checkGM) {
                    chr.getClient().disconnect(false, false, true);
                    if (chr.getClient().getSession().isActive()) {
                        chr.getClient().getSession().close();
                        System.err.println("\r\n 伺服器主動斷開用戶端連接，調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
                        FileoutputUtil.log("logs/data/DC.txt", "\r\n伺服器主動斷開用戶端連接，調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
                    }
                    WorldFindService.getInstance().forceDeregister(chr.getId(), chr.getName(), "調用位置:" + new java.lang.Throwable().getStackTrace()[0]);
                    chrit.remove();
                }
            }
        } finally {
            writeLock.unlock();
        }
    }

    /*
     * 獲取在線角色的名字
     */
    public String getOnlinePlayers(boolean byGM) {
        StringBuilder sb = new StringBuilder();
        if (byGM) {
            readLock.lock();
            try {
                for (MapleCharacter mapleCharacter : nameToChar.values()) {
                    sb.append(MapleCharacterUtil.makeMapleReadable(mapleCharacter.getName()));
                    sb.append(", ");
                }
            } finally {
                readLock.unlock();
            }
        } else {
            readLock.lock();
            try {
                Iterator<MapleCharacter> itr = nameToChar.values().iterator();
                MapleCharacter chr;
                while (itr.hasNext()) {
                    chr = itr.next();
                    if (!chr.isGM()) {
                        sb.append(MapleCharacterUtil.makeMapleReadable(chr.getName()));
                        sb.append(", ");
                    }
                }
            } finally {
                readLock.unlock();
            }
        }
        return sb.toString();
    }

    /*
     * 發送給當前頻道在線玩家封包
     */
    public void broadcastPacket(byte[] data) {
        readLock.lock();
        try {
            for (MapleCharacter mapleCharacter : nameToChar.values()) {
                mapleCharacter.getClient().sendPacket(data);
            }
        } finally {
            readLock.unlock();
        }
    }

    /*
     * 發送給當前頻道在線玩家喇叭的封包
     */
    public void broadcastSmegaPacket(byte[] data) {
        readLock.lock();
        try {
            Iterator<MapleCharacter> itr = nameToChar.values().iterator();
            MapleCharacter chr;
            while (itr.hasNext()) {
                chr = itr.next();
                if (chr.getClient().isLoggedIn() && chr.getSmega()) {
                    chr.send(data);
                }
            }
        } finally {
            readLock.unlock();
        }
    }

    /*
     * 發送給當前頻道在線GM的封包
     */
    public void broadcastGMPacket(byte[] data) {
        readLock.lock();
        try {
            Iterator<MapleCharacter> itr = nameToChar.values().iterator();
            MapleCharacter chr;
            while (itr.hasNext()) {
                chr = itr.next();
                if (chr.getClient().isLoggedIn() && chr.isIntern()) {
                    chr.send(data);
                }
            }
        } finally {
            readLock.unlock();
        }
    }

    public PlayerObservable getPlayerObservable() {
        return playerObservable;
    }

    public class UpdateCacheTask implements Runnable {

        private final Jedis jedis = RedisUtil.getJedis();

        @Override
        public void run() {
            try {
                for (Entry<Integer, MapleCharacter> entry : idToChar.entrySet()) {
                    System.out.println("更新缓存任务");
                    CharacterTransfer ct = new CharacterTransfer(entry.getValue());
                    jedis.hset(RedisUtil.KEYNAMES.PLAYER_DATA.getKeyName(), String.valueOf(entry.getKey()), JsonUtil.getMapperInstance().writeValueAsString(ct));
                }
            } catch (JsonProcessingException e) {
                System.err.println("更新緩存出錯" + e);
            }
        }
    }

    public class PersistingTask implements Runnable {

        @Override
        public void run() {
            pendingWriteLock.lock();
            try {
                long currenttime = System.currentTimeMillis();
                // min
                PendingCharacter.entrySet().removeIf(next -> currenttime - next.getValue().TranferTime > 1000 * 60 * 30);
            } finally {
                pendingWriteLock.unlock();
            }
        }
    }

    private class ConnectChecker implements Runnable {

        @Override
        public void run() {
            connectcheckReadLock.lock();
            try {
                Iterator<MapleCharacter> chrit = nameToChar.values().iterator();
                Map<Integer, MapleCharacter> disconnectList = new LinkedHashMap<>();
                MapleCharacter player;
                while (chrit.hasNext()) {
                    player = chrit.next();
                    if (player != null && !player.getClient().getSession().isActive()) {
                        disconnectList.put(player.getId(), player);
                    }
                }
                Iterator<MapleCharacter> dcitr = disconnectList.values().iterator();
                while (dcitr.hasNext()) {
                    player = dcitr.next();
                    if (player != null) {
                        player.getClient().disconnect(false, false);
                        player.getClient().updateLoginState(0);
                        disconnectPlayer(player);
                        dcitr.remove();
                    }
                }
            } finally {
                connectcheckReadLock.unlock();
            }
        }
    }

    public class PlayerObservable extends Observable {

        private int count;

        public int getCount() {
            return count;
        }

        public void changed() {
            this.count = nameToChar.size();
            setChanged();
            notifyObservers();
        }
    }
}

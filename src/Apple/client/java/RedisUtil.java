package Apple.client.java;

import redis.clients.jedis.BinaryJedis;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Set;

public final class RedisUtil {

    //Redis服務器IP
    private static final String ADDR = "127.0.0.1";

    //Redis的端口號
    private static final int PORT = 6379;
    //可用連接實例的最大數目，默認值為8；
    //如果賦值為-1，則表示不限制；如果pool已經分配了maxActive個jedis實例，則此時pool的狀態為exhausted(耗盡)。
    private static final int MAX_ACTIVE = -1;
    //控制一個pool最多有多少個狀態為idle(空閒的)的jedis實例，默認值也是8。
    private static final int MAX_IDLE = 200;
    //等待可用連接的最大時間，單位毫秒，默認值為-1，表示永不超時。如果超過等待時間，則直接拋出JedisConnectionException；
    private static final int MAX_WAIT = 10000;
    private static final int TIMEOUT = 10000;
    //在borrow一個jedis實例時，是否提前進行validate操作；如果為true，則得到的jedis實例均是可用的；
    private static final boolean TEST_ON_BORROW = true;
    //訪問密碼
    private static String AUTH = "admin";
    private static JedisPool jedisPool = null;

    /*
      初始化Redis連接池
     */
    static {

        try {
            boolean isInstall = true;
            boolean isRunning = false;
            Process process = Runtime.getRuntime().exec("SC QUERY redserver");
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
//                StringBuilder stringBuilder = new StringBuilder();
                String s;
                while ((s = bufferedReader.readLine()) != null) {
                    if (s.contains("1060")) {
                        isInstall = false;
                        break;
                    } else if (s.contains("STATE")) {
                        isRunning = s.contains("RUNNING");
                        break;
                    }
//                    stringBuilder.append(s).append("\r\n");
                }
            }
            if (!isInstall) {
                Process process1 = Runtime.getRuntime().exec("cmd /c redis-server.exe --service-install redis.windows-service.conf --loglevel verbose --service-name redserver", null, new File("config\\jdk\\jre\\lib\\redis\\"));
                process1.waitFor();
            }

            if (!isRunning) {
                Process process1 = Runtime.getRuntime().exec("cmd /c redis-server.exe --service-start --service-name redserver", null, new File("config\\jdk\\jre\\lib\\redis\\"));
                process1.waitFor();
            }

        } catch (IOException | InterruptedException e) {
            System.err.println("Cache初始化失敗" + e);
        }

        try {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(MAX_ACTIVE);
            config.setMaxIdle(MAX_IDLE);
            config.setMaxWaitMillis(MAX_WAIT);
            config.setTestOnBorrow(TEST_ON_BORROW);
            jedisPool = new JedisPool(config, ADDR, PORT, TIMEOUT);
        } catch (Exception e) {
            System.err.println("CachePool初始化失敗" + e);
        }
        // redis-server --service-install redis.windows-service.conf --loglevel verbose
    }

    public static <T> T domain(RedisDomainInterface<T> interfaces) {
        T Object;
        Jedis jedis = getJedis();
        try {
            Object = interfaces.domain(jedis);
        } finally {
            returnResource(jedis);
        }
        return Object;
    }

    /**
     * 獲取Jedis實例
     *
     * @return
     */
    public synchronized static Jedis getJedis() {
//        (new Exception()).printStackTrace();
        return jedisPool.getResource();
    }

    /**
     * 釋放jedis資源
     *
     * @param jedis
     */
    public static void returnResource(final Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }

    public static void hset(String key, String field, String value) {
        domain(jedis -> jedis.hset(key, field, value));
    }

    public static String hget(String key, String field) {
        return domain(jedis -> jedis.hget(key, field));
    }

    public static void hdel(String key, String field) {
        domain(jedis -> jedis.hdel(key, field));
    }

    public static boolean hexists(String key, String field) {
        return domain(jedis -> jedis.hexists(key, field));
    }

    public static Map<String, String> hgetAll(String key) {
        return domain(jedis -> jedis.hgetAll(key));
    }

    public static void del(String key) {
        domain(jedis -> jedis.del(key));
    }

    public static boolean isMembers(String key, String value) {
        return domain(jedis -> jedis.sismember(key, value));
    }

    public static boolean exists(String key) {
        return domain(jedis -> jedis.exists(key));
    }

    public static void flushall() {
        domain(BinaryJedis::flushAll);
    }

    public static Set<String> smembers(String key) {
        return domain(jedis -> jedis.smembers(key));
    }

    public enum KEYNAMES {
        ITEM_DATA("ItemData"),
        PET_FLAG("PetFlag"),
        SETITEM_DATA("SetItemInfo"),
        POTENTIAL_DATA("Potential"),
        SOCKET_DATA("Socket"),
        ITEM_NAME("ItemName"),
        ITEM_DESC("ItemDesc"),
        ITEM_MSG("ItemMsg"),
        HAIR_FACE_ID("HairFaces"),
        SKILL_DATA("SkillData"),
        SKILL_NAME("SkillName"),
        DELAYS("Delays"),
        SUMMON_SKILL_DATA("SummonSkillData"),
        MOUNT_ID("MountIDs"),
        FAMILIAR_DATA("Familiars"),
        CRAFT_DATA("Crafts"),
        SKILL_BY_JOB("SkillsByJob"),
        FINLA_ATTACK_SKILL("FinalAttackSkills"),
        MEMORYSKILL_DATA("MemorySkills"),
        //        DROP_DATA("DropData"),
        //        DROP_DATA_GLOBAL("DropDataGlobal"),
        //        DROP_DATA_SPECIAL("DropDataSpecial"),
        QUEST_COUNT("QuestCount"),
        NPC_NAME("NpcName"),
        PLAYER_DATA("PlayerData"),
        //        SHOP_DATA("ShopData"),
        MOBSKILL_DATA("MobSkillData"),
        MOB_NAME("MobName"),
        MOB_ID("MobIDs"),
        MAP_NAME("MapName"),
        MAP_LINKNPC("MapLinkNPC");

        public static final boolean DELETECACHE = false;
        private final String key;

        KEYNAMES(String key) {
            this.key = key;
        }

        public String getKeyName() {
            return key;
        }
    }

    public interface RedisDomainInterface<T> {

        T domain(Jedis jedis);
    }
}

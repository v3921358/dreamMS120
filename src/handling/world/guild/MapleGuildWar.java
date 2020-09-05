/**
 * @公會PVP戰
 * 模式說明:
 * 1. !GuildWar (0:弓箭手村,1:墮落城市,2:魔法森林,3:勇士之村)-> 使用指令開啟公會戰爭(備註:不可同時開啟兩個區域) (可取消關閉)
 */
package handling.world.guild;

import client.MapleCharacter;
import database.DatabaseConnection;
import java.sql.Connection;
import client.MapleClient;
import client.inventory.Equip;
import client.inventory.IItem;
import client.inventory.MapleInventoryType;
import constants.GameConstants;
import handling.world.World;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;
import server.MapleInventoryManipulator;
import server.MapleItemInformationProvider;
import server.RandomRewards;
import server.Randomizer;
import server.Timer;
import server.maps.MapleMap;
import server.shops.MaplePlayerShopItem;
import tools.MaplePacketCreator;
import tools.packet.UIPacket;

/**
 * @author ByBy
 */
public class MapleGuildWar implements Serializable {
    //================Map================
    private Map<Integer, String> GuildWar = new HashMap(); //紀錄正在挑戰的隊伍
    //================ScheduledFuture================
    private transient ScheduledFuture<?> War = null, War_Start = null ,War_End = null,PVPWaitTime = null;
    //================int================
    private int id = -1, GuildId = -1, Map = -1;
    private int wait = 0;//計算經過幾分鐘
    //================String================
    private String villages = "";
    //================static================
    private static final MapleGuildWar instance = new MapleGuildWar();
    private static int loginMap = 980010000, pvpMap = 980010100, HomeMap = 910000000; //PVP地圖
    private static int loginTime = 60 * 1000 * 5, pvpTime = 60 * 1000 *10, endTime = 10 *1000, WaitTime = 30*1000;
    //================Array================
    public int[][] PartyGuild;//紀錄公會數量和ID
    

    public MapleGuildWar() {
    }

    public static MapleGuildWar getInstance() {
        return instance;
    }

    /**
     * *
     * 依照村莊名查詢ID
     *
     * @param village 村莊
     */
    public MapleGuildWar(String village) {

        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM guildwar WHERE village = " + village);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                villages = village;
                id = rs.getInt("id");
                GuildId = rs.getInt("GuildId");
                Map = rs.getInt("MapId");
            } else {
                villages = "Error";
                GuildId = -1;
                id = -1;
                Map = -1;
            }
            ps.close();
            rs.close();
        } catch (Exception ex) {
            System.out.println("GuildWar _ village input error" + ex);
        }
    }

    /**
     * *
     * 依照村莊名查詢ID
     *
     * @param mapid 地圖ID
     */
    public MapleGuildWar(int mapid, boolean type) {

        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM guildwar WHERE village = " + mapid);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                villages = rs.getString("village");
                id = rs.getInt("id");
                GuildId = rs.getInt("GuildId");
                Map = mapid;
            } else {
                villages = "Error";
                GuildId = -1;
                id = -1;
                Map = -1;
            }
            ps.close();
            rs.close();
        } catch (Exception ex) {
            System.out.println("GuildWar _ village input error" + ex);
        }
    }

    /**
     * *
     * 依照編號查詢
     * @param ids 編號
     */
    public MapleGuildWar(int ids) {
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM guildwar WHERE id = " + ids);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                villages = rs.getString("village");
                id = ids;
                GuildId = rs.getInt("GuildId");
                Map = rs.getInt("MapId");
            } else {
                villages = "Error";
                GuildId = -1;
                id = -1;
                Map = -1;
            }
            ps.close();
            rs.close();
        } catch (Exception ex) {
            System.out.println("GuildWar _ ids input error" + ex);
        }
    }

    /**
     * *
     * 創建GuildWar
     * @param GuildIds 公會ID
     * @param village 村莊
     */
    public void createGuildWar(int GuildIds, String village) {
        Connection con = DatabaseConnection.getConnection();
        try {
            PreparedStatement ps;
            ps = con.prepareStatement("insert into guildwar (GuilId, village) values (?,?)");
            ps.setInt(1, GuildIds);
            ps.setString(2, village);
            ps.executeUpdate();
            ps.close();
        } catch (Exception ex) {
            System.out.println("CREATE Guild_War Error" + ex);
        }
    }

    /***
     * 載入所有公會戰資訊-SQL
     * @return 
     */
    public static final Collection<MapleGuildWar> loadAll() {
        final Collection<MapleGuildWar> ret = new ArrayList<MapleGuildWar>();
        MapleGuildWar g;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT id FROM guildwar");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                g = new MapleGuildWar(rs.getInt("id"));
                if (g.getId() > 0) {
                    ret.add(g);
                }
            }
            rs.close();
            ps.close();
        } catch (SQLException se) {
            System.err.println("無法從SQL讀取公會戰爭資訊");
            se.printStackTrace();
        }
        return ret;
    }

    /**
     * *
     * @儲存公會戰資訊SQL
     */
    public final void saveToDB() {
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("UPDATE GuildWar SET GuildId = ?,village = ?,MapId =? WHERE id = " + getId());
            ps.setInt(1, getGuildId());
            ps.setString(2, getVillage());
            ps.setInt(3, getMapId());
            ps.executeUpdate();
            ps.close();
        } catch (Exception sq) {
            System.out.println("Guild_War  Save DB Error" + sq);
        }
    }

    /**
     * *
     * @獲得領地公會ID
     */
    public final int getGuildId() {
        if (getGuild(GuildId) == null && GuildId != 0) {
            MapleGuildWar War = World.GuildWar.getGuildWar(getId());
            War.setGuildId(0);
        }
        return GuildId;
    }

    /**
     * *
     * 設置領地公會ID
     *
     * @param GID 公會ID
     */
    public void setGuildId(int GID) {
        GuildId = GID;
    }

    /**
     * *
     * @獲得村莊地圖id
     */
    public final int getMapId() {
        return Map;
    }

    /**
     * *
     * 設置村莊地圖id
     *
     * @param id 地圖id
     */
    public void setMapId(int id) {
        Map = id;
    }

    /**
     * *
     * 獲得編號
     *
     * @return 編號
     */
    public final int getId() {
        return id;
    }

    /**
     * *
     * 獲得村莊名稱
     *
     * @return 村莊
     */
    public final String getVillage() {
        return villages;
    }

    /**
     * *
     * @設置村莊名稱
     */
    public void setVillage(String Village) {
        villages = Village;
    }

    /**
     * *
     * 加入對戰名單
     *
     * @param id 公會ID
     * @param village 村莊名稱
     */
    public void addWar(int id, String village) {
        GuildWar.put(id, village);
    }

    /**
     * *
     * @踢出對戰名單 @param id 公會ID
     */
    public void deleteWar(int id) {
        GuildWar.remove(id);
    }

    /**
     * *
     * @對戰名單清除
     */
    public void clearWar() {
        GuildWar.clear();
    }

    /**
     * *
     * 用於查詢對戰中
     * @param village 村莊名稱
     * @return 回傳清單
     */
    public List getWar(String village) {
        Set keySet = GuildWar.keySet();
        List<Integer> GuildId = new ArrayList();
        Iterator it = keySet.iterator();
        while (it.hasNext()) {
            int key = (int) it.next();//公會ID
            if (GuildWar.get(key) == village) {
                GuildId.add(key);
            }
        }
        return GuildId;
    }

    /**
     * *
     * 確認是否在此村莊的對戰名單
     * @param GuildId 公會id
     * @param village 村莊
     * @return 回傳結果
     */
    public boolean checkWarParty(int GuildId, String village) {
        return GuildWar.containsKey(GuildId) && GuildWar.containsValue(village);
    }

    /**
     * *
     * 獲取對戰名單公會數量
     * @param village
     * @return 數量
     */
    public int getWarSize(String village) {
        return getWar(village).size();
    }

    /**
     * *
     * 判斷是否是領主公會
     * @param id 公會id
     * @return
     */
    public static boolean isLord(int id) {
        for (int i = 1; i <= 4; i++) {
            if (World.GuildWar.getGuildWar(i).getGuildId() == id) {
                return true;
            }
        }
        return false;
    }

    /**
     * *
     * 判斷是否是指定的領主公會
     * @param id 戰爭id
     * @param GuildId 公會ID
     */
    public static boolean isLord(int id, int GuildId) {
        if (World.GuildWar.getGuildWar(id).getGuildId() == GuildId) {
            return true;
        }
        return false;
    }

    public void cancelTime(MapleClient c, boolean type) {
        if (War != null) 
            War.cancel(false);
        if (War_Start != null) 
            War_Start.cancel(false);
        if(PVPWaitTime != null)
            PVPWaitTime.cancel(false);
        
        wait = 0;
        if (type) {
            MapleMap map = c.getChannelServer().getMapFactory().getMap(loginMap);
            MapleMap ToMap = c.getChannelServer().getMapFactory().getMap(HomeMap);
            if(War != null)
            {
                for (MapleCharacter chs : map.getCharactersThreadsafe()) {
                    chs.changeMap(ToMap, ToMap.getPortal(0));
                }
            }
            if (War_Start != null) {
                MapleMap pvp_map = c.getChannelServer().getMapFactory().getMap(pvpMap);
                for (MapleCharacter chs : pvp_map.getCharactersThreadsafe()) {
                    chs.changeMap(ToMap, ToMap.getPortal(0));
                }
            }
        }
        War_Start = null;
        War = null;
        PVPWaitTime = null;
        
    }

    public boolean getWaitTime() {
        return War == null ? false : true;
    }
    
    public boolean getPVPWaitTime(){
        return PVPWaitTime == null ? false : true;
    }

    public boolean getWarStart() {
        return War_Start == null ? false : true;
    }

    public void cancelRan() {
        if (War_Start != null) {
            War_Start.cancel(false);
            War_Start = null;
        }
    }
    public void cancelEed(){
        if (War_End != null) {
            War_End.cancel(false);
            War_End = null;
        }
    }
    public void cancelPvP(){
        if (PVPWaitTime != null) {
            PVPWaitTime.cancel(false);
            PVPWaitTime = null;
        }
    }
    /***
     * 等待室 time
     * @param Village 村莊
     * @param id 領地id
     * @param c MapleClient
     */
    public void WaitTime(String Village, int id, MapleClient c) {

        War = Timer.WorldTimer.getInstance().register(new Runnable() {

            @Override
            public void run() {
                int GuildSize = getWarSize(Village);
                int Warid = (id + 1);
                MapleGuildWar MapleWar = World.GuildWar.getGuildWar(Warid);
                wait += 1;
                if (wait == 5) {
                    if (GuildSize > 1) {
                        MapleMap map = c.getChannelServer().getMapFactory().getMap(loginMap);
                        MapleMap ToMap = c.getChannelServer().getMapFactory().getMap(getPVPMap(Warid));
                        MapleWar.PartyGuild = new int[GuildSize][2];
                        for (int x = 0; x < GuildSize; x++) {
                            MapleWar.PartyGuild[x][0] = (int) getWar(Village).get(x);
                            MapleWar.PartyGuild[x][1] = 0;
                        }

                        for (int i = 0; i < GuildSize; i++) {
                            for (MapleCharacter chs : map.getCharactersThreadsafe()) {
                                if (chs.isAlive() && chs != null) {
                                    if (chs.getGuild().getId() == (int) getWar(Village).get(i)) {
                                        MapleWar.PartyGuild[i][1]++;
                                    }
                                }
                            }
                        }
                        for (MapleCharacter chs : map.getCharactersThreadsafe()) {
                            chs.changeMap(ToMap, ToMap.getPortal(0));
                        }
                        
                        World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(6, "" + Village + "公會戰  開打了!!"));
                        MapleWar.cancelTime(c, false);
                        //MapleWar.StartTime(Village, (id + 1), c);
                        MapleWar.PvPWaitTime(villages, (id+1), c);
                        
                    } else if (GuildSize == 1) {
                        MapleMap Waitmap = c.getChannelServer().getMapFactory().getMap(loginMap);
                        MapleMap ToMap = c.getChannelServer().getMapFactory().getMap(HomeMap);
                        MapleWar.setGuildId((int) getWar(Village).get(0));
                        MapleWar.saveToDB();
                        World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(6, "公會戰-" + Village + "結束了,由於僅有[" + getGuildName((int) getWar(Village).get(0)) + "]公會參加 領地被他們佔領了!"));
                        GuildWar.clear();
                        cancelTime(c, false);
                        for (MapleCharacter chs : Waitmap.getCharactersThreadsafe()) {
                            chs.changeMap(ToMap, ToMap.getPortal(0));
                        }
                        GameConstants.GuildWar[id] = false;
                    } else {
                        if (MapleWar.getGuildId() == 0) {
                            MapleWar.setGuildId(0);
                            MapleWar.saveToDB();
                            World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(6, "公會戰-" + Village + "結束了,由於沒人參加故領地空著"));
                            cancelTime(c, true);
                            GameConstants.GuildWar[id] = false;
                        } else {
                            MapleWar.setGuildId(MapleWar.getGuildId());
                            MapleWar.saveToDB();
                            World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(6, "公會戰-" + Village + "結束了,由於沒人參加故領地依然是[" + getGuildName(MapleWar.getGuildId()) + "]公會佔領"));
                            cancelTime(c, true);
                            GameConstants.GuildWar[id] = false;
                        }
                    }
                } else {
                    if (wait % 2 == 0) {
                        World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(6, "" + Village + "公會戰 " + (5 - (wait)) + "分鐘後即將開打"));
                    }
                }
            }
        }, loginTime, loginTime);
    }
    
    public void EndTime(String Village, int id, MapleClient c, int GuildID) {
        MapleMap map = c.getChannelServer().getMapFactory().getMap(pvpMap);
        cancelTime(c,false);
        map.broadcastMessage(MaplePacketCreator.getClock(10));
        for (MapleCharacter chs : map.getCharactersThreadsafe()) {
            if (chs != null) {
                String effect = "";
                String sound = "";
                if (chs.getGuild().getId() == GuildID) {
                    effect = "win";
                    sound = "Win";
                    chs.getClient().sendPacket(MaplePacketCreator.showEffect("quest/carnival/" + effect));
                    chs.getClient().sendPacket(MaplePacketCreator.playSound("MobCarnival/" + sound));
                }else{
                    effect = "lose";
                    sound = "Lose";
                }
            }
        }
        War_End = Timer.WorldTimer.getInstance().register(new Runnable() {
            @Override
            public void run() {
                MapleMap to_map = c.getChannelServer().getMapFactory().getMap(World.GuildWar.getGuildWar(id).getMapId());
                for (MapleCharacter chs : map.getCharactersThreadsafe()) {
                    chs.changeMap(to_map, to_map.getPortal(0));
                }
                MapleGuildWar MapleWar = World.GuildWar.getGuildWar(id);
                MapleWar.setGuildId(GuildID);
                MapleWar.saveToDB();
                GuildWar.clear();
                GameConstants.GuildWar[(id - 1)] = false;
                cancelEed();
            }
        }, endTime, endTime);
    }
    
    public void PvPWaitTime(String Village, int id, MapleClient c) {
            
        MapleMap map = c.getChannelServer().getMapFactory().getMap(pvpMap);
        map.broadcastMessage(MaplePacketCreator.getClock(WaitTime/1000));
        map.broadcastMessage(MaplePacketCreator.serverNotice(6, "將有"+WaitTime/1000+"秒的時間戰鬥準備"));
        PVPWaitTime = Timer.WorldTimer.getInstance().register(new Runnable() {
            @Override
            public void run() {
                MapleMap map = c.getChannelServer().getMapFactory().getMap(pvpMap);
                MapleGuildWar MapleWar = World.GuildWar.getGuildWar(id);
                map.broadcastMessage(MaplePacketCreator.getClock((pvpTime/1000)));
                MapleWar.StartTime(Village, id, c);
                map.broadcastMessage(MaplePacketCreator.serverNotice(6, "戰鬥開始"));
                cancelPvP();
            }
        }, WaitTime, WaitTime);
    }
    
    public void StartTime(String Village, int id, MapleClient c) {
        War_Start = Timer.WorldTimer.getInstance().register(new Runnable() {
            @Override
            public void run() {
                int size = getWarSize(Village);
                int[][] Guild = new int[size][2];
                MapleGuildWar MapleWar = World.GuildWar.getGuildWar(id);
                MapleMap map = c.getChannelServer().getMapFactory().getMap(pvpMap);
                MapleMap ToMap = c.getChannelServer().getMapFactory().getMap(World.GuildWar.getGuildWar(id).getMapId());
                if (size != 0) {
                    for (int j = 0; j < MapleWar.PartyGuild.length - 1; j++) {
                        for (int x = 0; x < MapleWar.PartyGuild[j].length - 1; x++) {
                            if (size > 1) {
                                if (MapleWar.PartyGuild[x + 1][1] > MapleWar.PartyGuild[x][1]) {
                                    int temp = MapleWar.PartyGuild[x][1];
                                    int ids = MapleWar.PartyGuild[x][0];
                                    MapleWar.PartyGuild[x][1] = MapleWar.PartyGuild[x + 1][1];
                                    MapleWar.PartyGuild[x + 1][1] = temp;
                                    MapleWar.PartyGuild[x][0] = MapleWar.PartyGuild[x + 1][0];
                                    MapleWar.PartyGuild[x + 1][0] = ids;
                                }
                            } else {
                                break;
                            }
                        }
                    }
                    MapleWar.setGuildId(MapleWar.PartyGuild[0][0]);
                    MapleWar.saveToDB();
                    World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(6, "公會戰-" + Village + "結束了,勝利的公會是[" + getGuildName(MapleWar.PartyGuild[0][0]) + "]"));
                    MapleWar.GuildWar.clear();
                    MapleWar.cancelRan();
                    for (MapleCharacter chs : map.getCharactersThreadsafe()) {
                        chs.changeMap(ToMap, ToMap.getPortal(0));
                    }
                } else {
                    System.out.println("公會戰Start 發生錯誤!!!!!!");
                    GuildWar.clear();
                    MapleWar.cancelRan();
                }
                GameConstants.GuildWar[(id - 1)] = false;
            }
        }, pvpTime, pvpTime);
    }
    
    //-----------PVP地圖------------
    public static int isPVPMap(int mapid){
        switch(mapid)
        {
            case 980010000:
                return 1;
            case 980010100:
                return 2;
            case 980010200:
                return 3;
            case 980010300:
                return 4;
        }
        return 155;
    }
    
    public static int getPVPMap(int id){
        switch(id)
        {
            case 1:
                return 980010000;
            case 2:
                return 980010100;
            case 3:
                return 980010200;
            case 4:
                return 980010300;
        }
        return 910000000;
    }
    
    //-----------公會處理-----------
    public static MapleGuild getGuild(final int id) {
        return World.Guild.getGuild(id);
    }

    public static String getGuildName(int id) {
        return getGuild(id).getName();
    }
}

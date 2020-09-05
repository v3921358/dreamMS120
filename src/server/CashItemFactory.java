package server;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import database.DatabaseConnection;
import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Collection;
import java.util.LinkedList;
import provider.MapleData;
import provider.MapleDataProvider;
import provider.MapleDataProviderFactory;
import provider.MapleDataTool;
import server.CashItemInfo.CashModInfo;

public class CashItemFactory {

    private final static CashItemFactory instance = new CashItemFactory();
    private final static int[] bestItems = new int[]{50400003, 50300092, 30200034, 50100010, 50300099};
    private boolean initialized = false;
    private final Map<Integer, CashItemInfo> itemStats = new HashMap<Integer, CashItemInfo>();
    private final Map<Integer, List<CashItemInfo>> itemPackage = new HashMap<Integer, List<CashItemInfo>>();
    private final Map<Integer, CashModInfo> itemMods = new HashMap<Integer, CashModInfo>();
    private final MapleDataProvider data = MapleDataProviderFactory.getDataProvider(new File(System.getProperty("net.sf.odinms.wzpath") + "/Etc.wz"));
    private final Map<Integer, Integer> fixId = new HashMap<>(); //檢測WZ中是否有重複價格的道具 [SN] [itemId]

    private final List<Integer> blockRefundableItemId = new LinkedList<>(); //禁止使用回購的道具 也就是有些道具有多個SN信息 而每個SN下的價格又不一樣
    private final Map<Integer, Integer> idLookup = new HashMap<>(); //商城道具的SN集合
    private final Map<Integer, Integer> PeriodLookup = new HashMap<>(); //商城價錢的SN集合

    public static final CashItemFactory getInstance() {
        return instance;
    }

    protected CashItemFactory() {
    }

    public void clear() {
        initialized = false;
        itemStats.clear();
        itemMods.clear();
        idLookup.clear();
        blockRefundableItemId.clear();
        fixId.clear();
        PeriodLookup.clear();
    }

    public void initialize() {
        clear();
        System.out.println("Loading CashItemFactory :::");
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        final List<Integer> itemids = new ArrayList<Integer>();
        for (MapleData field : data.getData("Commodity.img").getChildren()) {
            final int itemId = MapleDataTool.getIntConvert("ItemId", field, 0);
            final int SN = MapleDataTool.getIntConvert("SN", field, 0);
            final int Period = MapleDataTool.getIntConvert("Period", field, 0);//天數
            final int Count = MapleDataTool.getIntConvert("Count", field, 1);//數量
            final int Price = MapleDataTool.getIntConvert("Price", field, 0);//點數
            final CashItemInfo stats = new CashItemInfo(itemId,
                    MapleDataTool.getIntConvert("Count", field, 1),
                    Price, SN,
                    MapleDataTool.getIntConvert("Period", field, 0),
                    MapleDataTool.getIntConvert("Gender", field, 2),
                    MapleDataTool.getIntConvert("OnSale", field, 0) > 0);
            if (SN / 10000000 != 1) {
                itemStats.put(SN, stats);
                if (idLookup.containsKey(itemId) && PeriodLookup.containsKey(itemId + Period + Count)) {
                    fixId.put(SN, itemId);
                    blockRefundableItemId.add(itemId);
                }
                idLookup.put(itemId, SN);
                PeriodLookup.put(itemId + Period + Count, itemId);
            /*}else if (SN / 10000000 == 2 && Price < 5){
                System.err.println(ii.getName(itemId) + ": " + itemId+ " 價格 " + Price);*/
            }

            if (itemId > 1) {
                itemids.add(itemId);
            }
        }
        System.out.println("共加載 " + itemStats.size() + " 個商城道具 ...");
        System.out.println("其中有 " + fixId.size() + " 重複價格的道具和 " + blockRefundableItemId.size() + " 個禁止換購的道具.");
        for (int i : itemids) {
            getPackageItems(i);
        }
        for (int i : itemStats.keySet()) {
            getModInfo(i);
            getItem(i); //init the modinfo's citem
        }
        System.err.printf("<所有物品> %s\r\n       >>SQL載入 %s\r\n       >>檢查重複物品 %s\r\n       >>檢查重複天數 %s\r\n       >>阻止退款 %s\r\n       >>重複天數 %s\r\n", itemStats.size(), itemMods.size(), idLookup.size(), PeriodLookup.size(), blockRefundableItemId.size(), fixId.size());
        initialized = true;
    }

    public final CashItemInfo getItem(int sn) {
        final CashItemInfo stats = itemStats.get(Integer.valueOf(sn));
        final CashModInfo z = getModInfo(sn);
        if (z != null && z.showUp) {
            return z.toCItem(stats); //null doesnt matter
        }
        if (stats == null || !stats.onSale()) {
            return null;
        }
        //hmm
        return stats;
    }

    public final List<CashItemInfo> getPackageItems(int itemId) {
        if (itemPackage.get(itemId) != null) {
            return itemPackage.get(itemId);
        }
        final List<CashItemInfo> packageItems = new ArrayList<CashItemInfo>();

        final MapleData b = data.getData("CashPackage.img");
        if (b == null || b.getChildByPath(itemId + "/SN") == null) {
            return null;
        }
        for (MapleData d : b.getChildByPath(itemId + "/SN").getChildren()) {
            packageItems.add(itemStats.get(Integer.valueOf(MapleDataTool.getIntConvert(d))));
        }
        itemPackage.put(itemId, packageItems);
        return packageItems;
    }

    public final CashModInfo getModInfo(int sn) {
        CashModInfo ret = itemMods.get(sn);
        if (ret == null) {
            if (initialized) {
                return null;
            }
            try {
                if (!fixId.containsKey(sn)) {
                    Connection con = DatabaseConnection.getConnection();
                    PreparedStatement ps = con.prepareStatement("SELECT * FROM cashshop_modified_items WHERE serial = ?");
                    ps.setInt(1, sn);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        int price = rs.getInt("discount_price");
                        if ((sn / 10000000) == 2) {
                            price *= 1;
                        } else if ((sn / 100000) == 400) {
                            price *= 5;
                        } else if (sn == 50300134) {
                            price *= 3;
                        } else if (sn >= 50300192 && sn <= 50300197) {//技能書
                            price *= 1.5;
                        } else {
                            price *= 2;
                        }
                        ret = new CashModInfo(sn, price, rs.getInt("mark"), rs.getInt("showup") > 0, rs.getInt("itemid"), rs.getInt("priority"), rs.getInt("package") > 0, rs.getInt("period"), rs.getInt("gender"), rs.getInt("count"), rs.getInt("meso"), rs.getInt("unk_1"), rs.getInt("unk_2"), rs.getInt("unk_3"), rs.getInt("extra_flags"));
                        itemMods.put(sn, ret);
                    }
                    rs.close();
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    public final Collection<CashModInfo> getAllModInfo() {
        if (!initialized) {
            initialize();
        }
        return itemMods.values();
    }

    public final int[] getBestItems() {
        return bestItems;
    }
}

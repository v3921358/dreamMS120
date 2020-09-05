/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package constants.CopyItem;

import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;



/**
 * @author PlayDK
 */
public class MapleEquipOnlyId {

    private final AtomicInteger runningId;

    private MapleEquipOnlyId() {
        runningId = new AtomicInteger(0);
    }

    public static MapleEquipOnlyId getInstance() {
        return SingletonHolder.instance;
    }

    public int getNextEquipOnlyId() {
        if (runningId.get() <= 0) { //如果這個ID小於等於0就進行初始化
            runningId.set(initOnlyId());
        } else {
            runningId.set(runningId.get() + 1); //設置新的ID為老ID + 1
        }
        return runningId.get();
    }

    public int initOnlyId() {
        int ret = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT MAX(equipOnlyId) FROM inventoryitems WHERE equipOnlyId > 0");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ret = rs.getInt(1) + 1; //設置為當前的ID + 1
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.err.println("Error getting character default" + e);
        } catch (Exception e) {
            System.out.println("tryPartyQuest error");
        }
        return ret;
    }

    private static class SingletonHolder {

        protected static final MapleEquipOnlyId instance = new MapleEquipOnlyId();
    }
}

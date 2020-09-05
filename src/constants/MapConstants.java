package constants;

public class MapConstants {

    public static boolean isStartingEventMap(final int mapid) {
        switch (mapid) {
            case 109010000:
            case 109020001:
            case 109030001:
            case 109030101:
            case 109030201:
            case 109030301:
            case 109030401:
            case 109040000:
            case 109060001:
            case 109060002:
            case 109060003:
            case 109060004:
            case 109060005:
            case 109060006:
            case 109080000:
            case 109080001:
            case 109080002:
            case 109080003:
                return true;
        }
        return false;
    }

    public static boolean isEventMap(final int mapid) {
        return mapid >= 109010000 && mapid < 109050000 || mapid > 109050001 && mapid < 109090000;
    }

    public static boolean isMapleLand(int mapid) {
        return mapid < 1010004;
    }

    public static boolean isMarketMap(int mapid) {
        return mapid >= 910000000 && mapid <= 910000022;
    }
    
    public static boolean isFishingMap(int mapId) {
        switch (mapId) {
            case 749050500:
            case 749050501:
            case 749050502:
                return true;
            default:
                return false;
        }
    }
}

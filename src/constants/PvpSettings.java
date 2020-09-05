package constants;

import server.ServerProperties;

/**
 *
 * @author Windyboy
 */
public class PvpSettings {

    public static boolean usepvp = true;
    public static int mindamage = 1;
    public static int maxdamage = 99999;
    public static int channel = 4;
    public static int rewarditem = 1302000;
    public static int exp = 500;
    public static boolean godgm = false;

    public static void initSetting() {
        mindamage = Integer.parseInt(ServerProperties.getProperty("pvp.minDmg", String.valueOf(mindamage)));
        maxdamage = Integer.parseInt(ServerProperties.getProperty("pvp.maxDmg", String.valueOf(maxdamage)));
        channel = Integer.parseInt(ServerProperties.getProperty("pvp.channel", String.valueOf(channel)));
        rewarditem = Integer.parseInt(ServerProperties.getProperty("pvp.item", String.valueOf(rewarditem)));
        usepvp = Boolean.valueOf(ServerProperties.getProperty("pvp.use", String.valueOf(usepvp)));
        godgm = Boolean.valueOf(ServerProperties.getProperty("pvp.godgm", String.valueOf(godgm)));
        exp = Integer.parseInt(ServerProperties.getProperty("pvp.exp", String.valueOf(exp)));
    }

}

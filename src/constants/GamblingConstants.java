package constants;




import client.MapleCharacter;
import client.MapleClient;
import handling.world.World;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;
import server.Randomizer;
import server.Timer;
import tools.MaplePacketCreator;


public class GamblingConstants {
    public int OpenAction = 0;
    private Map<Integer, Integer> ChrBetMoney = new HashMap(); //紀錄押注金錢
    private Map<Integer, Integer> ChrBet = new HashMap(); //紀錄押注的人
    private transient ScheduledFuture<?> BetEvent = null;
    private int Time = 1000*60*1;
    private int AccumMoney = 0;
    private int BetMath = 0;
    public static int[][] CheckBet, CheckUnBet;
    private static final GamblingConstants instance = new GamblingConstants();
    
    public static GamblingConstants getInstance() {
        return instance;
    }
    /**
      * @增加賭注 
    */
    public void addBet(int chrid, int Bet, int money){
        ChrBet.put(chrid, Bet);
        ChrBetMoney.put(chrid,money);
    }
    public boolean getChr(int chrid){
        return ChrBet.containsKey(chrid);
    }
    public int getChrBetMath(int chrid){
        return ChrBet.get(chrid).intValue();
    }
    public int getChrBetMoney(int chrid){
        return ChrBetMoney.get(chrid).intValue();
    }
    /**
     * @賭注Map清除
     */
    public void BetClear(){
        BetMath = 0;
        ChrBet.clear();
        ChrBetMoney.clear();
        CheckBet = null;
        CheckUnBet = null;
    }
    /**
     * @賭注移除key
     */
    public void removeBetKey(int key){
        ChrBet.remove(key);
        ChrBetMoney.remove(key);
    }
    /**
     *  @獲得中獎or未中獎名單
     */
    public void getBetPlayer(int number){
        int i = 0 , ui = 0, xi = 0, xui = 0;
        Set keySet = ChrBet.keySet();
        Iterator it = keySet.iterator();
        while (it.hasNext()) {
            int key = (int) it.next();//玩家ID
            if (ChrBet.get(key).intValue() == number)
                ++i;
            else
                ++ui;       
        }
        CheckBet = new int[i+1][2];
        CheckUnBet = new int[ui+1][2];
        Set Keys = ChrBet.keySet();
        Iterator its = Keys.iterator();
        while (its.hasNext()) {
            int key = (int) its.next();//玩家ID
            if (ChrBet.get(key).intValue() == number){
                ++xi;
                CheckBet[xi][0] = key;
                CheckBet[xi][1] = ChrBetMoney.get(key).intValue();
            }else{
                ++xui;
                CheckUnBet[xui][0] = key;
                CheckUnBet[xui][1] = ChrBetMoney.get(key).intValue();
            }
        }
    }
    
    public int[][] getCheckBet(){
        return CheckBet;
    }
   
    public int[][] getCheckUnBet(){
        return CheckUnBet;
    }
    
    public int getAction(){
        return OpenAction;
    }
    
    public void setAction(int action){
        OpenAction = action;
    }
    
    public void getThisAccumMoney(){
        if(CheckUnBet.length > 0)
            for(int i = 1; i < CheckUnBet.length; i++)
                this.AccumMoney +=CheckUnBet[i][1];
    }
    
    public int getAccumMoney(){
        return AccumMoney;
    }
    /***
     * 
     * @param chrid 角色ID
     * @return 1 -> 中獎 |  0 -> 未中獎 | -1 -> 未參加賭注 
     */
    public int CheckBet(int chrid){
        for(int i = 0 ; i < CheckBet.length; i++)
            if(CheckBet[i][0] == chrid)
                return 1;
            else if(CheckUnBet[i][0] == chrid)
                return 0;
        return -1;
    }
    /**
     * @產生亂數
     */
    public int RandMath(int min, int max, boolean fixed){
        if(fixed)
            return min;
        int Rand = Randomizer.rand(min,max);
        return Rand;
    }
    
    public void setBetMath(int math){
        BetMath = math;
    }
    
    public int getBetMath(){
        return BetMath;
    }
    public boolean getBetTime() {
        return BetEvent == null ? false : true;
    }
   public void cancelRan() {
        if(BetEvent !=  null)
        {
            BetEvent.cancel(false);
            BetEvent = null;
        }
        setAction(0);
    }
    public void BetEventTime(MapleClient c,int cycle) {
        
        setAction(1);
        
        BetEvent = Timer.WorldTimer.getInstance().register(new Runnable() {

            @Override
            public void run() {
                cancelRan();
                int min = 1, max = 30;
                setAction(0);
                setBetMath(RandMath(min,max,false));
                getBetPlayer(getBetMath());
                getThisAccumMoney();
                if(CheckBet.length <= 1 || CheckBet == null) {
                    System.out.println("系統公告 賭注-未有人中獎");
                    World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(6, "[寶貝谷賭博公告]由於沒人中獎故獎金累積至:"+AccumMoney));
                    World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(6, "[寶貝谷賭博公告]賭注開獎號碼為:"+getBetMath()));
                    BetClear();
                    if(cycle == 1)
                        BetEventTime(c, 30); // 1
                    return;
                }else{
                    for(int i = 1; i < CheckBet.length;i++)
                    {
                        MapleCharacter chrs = c.getChannelServer().getPlayerStorage().getCharacterById(CheckBet[i][0]);
                        if(chrs != null)
                        {
                            int Point = 0;
                            Point += CheckBet[i][1]*2;
                            if(AccumMoney > 0)
                                Point += AccumMoney/(CheckBet.length-1);
                            chrs.modifyCSPoints(2,Point,true);
                            chrs.dropMessage(1,"[寶貝谷賭博公告]::恭喜您中獎了 總額:"+Point);
                           // System.out.println("中獎人士:"+chrs.getName()+" 獲得累積金額:"+(AccumMoney/CheckBet.length-1)+" 總額:"+Point);
                        }
                    }
                    AccumMoney =0;
                }
                World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(6, "[寶貝谷賭博公告]賭注開獎號碼為:"+getBetMath()));
                BetClear();
                if(cycle == 1)
                    BetEventTime(c, 1);
            }
        }, Time, Time);
    }
}

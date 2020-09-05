/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Apple.client;

/**
 * @author PlayDK
 */
public enum MapleExpStat {

    活動獎勵經驗(0x01),
    特別經驗(0x02), //每次打3個怪物時給你特別經驗值X%
    活動組隊經驗(0x04), //活動組隊經驗獎勵倍數(i x 100)最大3倍
    組隊經驗(0x10),
    結婚獎勵經驗(0x20), //結婚經驗要在組隊經驗前面
    道具佩戴經驗(0x40),
    網吧特別經驗(0x80),
    彩虹周獎勵經驗(0x100),
    歡享獎勵經驗(0x200),
    飛躍獎勵經驗(0x400),
    精靈祝福經驗(0x800),
    增益獎勵經驗(0x1000),
    休息經驗(0x2000),
    物品獎勵經驗(0x4000),
    阿斯旺獲勝者獎勵經驗(0x8000),
    使用道具經驗(0x10000), //使用道具增加了%經驗
    超值禮包獎勵經驗(0x20000),
    受道具影響而獲得的組隊任務額外獎勵(0x40000),
    格外獲得經驗(0x80000),
    血盟獎勵經驗(0x100000),
    家族獎勵經驗(0x100000),
    冷凍勇士經驗(0x200000),
    燃燒場地經驗(0x400000),
    HP風險經驗(0x800000),
    累計打獵數量獎勵經驗(0x2000000),
    召喚戒指組隊經驗(0x4000000),
    PVP_BONUS_EXP(0x8000000),
    訓練寵物額外經驗(0x10000000);
    private final long i;

    MapleExpStat(long i) {
        this.i = i;
    }

    public long getValue() {
        return i;
    }
}

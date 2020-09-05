/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server.maps.pvp;

import client.MapleBuffStat;
import client.MapleCharacter;
import constants.PvpSettings;
import handling.channel.handler.AttackInfo;
import server.MapleStatEffect;
import server.life.MapleLifeFactory;
import server.life.MapleMonster;
import server.maps.MapleMap;
import tools.MaplePacketCreator;

import java.awt.*;
import server.Randomizer;
import tools.FileoutputUtil;

public class MaplePvp {

    private static PvpAttackInfo parsePvpAttack(AttackInfo attack, MapleCharacter player, MapleStatEffect effect) {
        PvpAttackInfo ret = new PvpAttackInfo();
        double maxdamage = player.getLevel() + 600.0;
        int skillId = attack.skill;
        ret.skillId = skillId;
        ret.critRate = 5;
        ret.ignoreDef = 0;
        ret.skillDamage = 600;
        ret.mobCount = 1;
        ret.attackCount = 1;
        int pvpRange = isMeleeSkill(skillId) ? 35 : 70;
        ret.facingLeft = attack.animation < 0;
        if (skillId != 0 && effect != null) {
//            ret.critRate += effect.getCritical();
//            ret.ignoreDef += effect.getIgnoreMob();
            ret.skillDamage = (effect.getDamage() /*+ player.getStat().getDamageIncrease(skillId)*/);
            ret.mobCount = Math.max(1, effect.getMobCount());
            ret.attackCount = Math.max(effect.getBulletCount(), effect.getAttackCount());
            ret.box = effect.calculateBoundingBox(player.getPosition(), ret.facingLeft, pvpRange);
        } else {
            ret.box = calculateBoundingBox(player.getPosition(), ret.facingLeft, pvpRange);
        }
        boolean mirror = player.getBuffedValue(MapleBuffStat.SHADOWPARTNER) != null;
        ret.attackCount *= (mirror ? 2 : 1);
        maxdamage *= ret.skillDamage / 100.0;
        ret.maxDamage = maxdamage * ret.attackCount;

        return ret;
    }

    private static Rectangle calculateBoundingBox(Point posFrom, boolean facingLeft, int range) {
        Point lt = new Point(-70, -30);
        Point rb = new Point(-10, 0);
        Point mylt;
        Point myrb;
        if (facingLeft) {
            mylt = new Point(lt.x + posFrom.x - range, lt.y + posFrom.y);
            myrb = new Point(rb.x + posFrom.x, rb.y + posFrom.y);
        } else {
            myrb = new Point(lt.x * -1 + posFrom.x + range, rb.y + posFrom.y);
            mylt = new Point(rb.x * -1 + posFrom.x, lt.y + posFrom.y);
        }
        return new Rectangle(mylt.x, mylt.y, myrb.x - mylt.x, myrb.y - mylt.y);
    }

    /*private static boolean isMeleeSkill(int skill) {
        switch (skill) {
            case 1001004:    //Power Strike
            case 1001005:    //Slash Blast
            case 1111004:    //Panic: Axe
            case 1111003:    //Panic: Sword
            case 1311004:    //Dragon Fury: Pole Arm
            case 1311003:    //Dragon Fury: Spear
            case 1311002:    //Pole Arm Crusher
            case 1311005:    //Sacrifice
            case 1311001:    //Spear Crusher
            case 1121008:    //Brandish
            case 1221009:    //Blast
            case 1121006:    //Rush
            case 1221007:    //Rush
            case 1321003:    //Rush
            case 4001334:    //Double Stab
            case 4201005:    //Savage Blow
            case 4221001:    //Assassinate
            case 1111006:    //Coma: Axe
            case 1111005:    //Coma: Sword
            case 1211002:    //Charged Blow

            case 3101003:    //Power Knockback
            case 3201003:    //Power Knockback
            case 5001002:    //Somersault Kick
            case 5121007:    //Barrage
            case 5101002:    //Backspin Blow
            case 5001001:    //Flash Fist
            case 5101003:    //Double Uppercut
            case 5121004:    //Demolition
            case 5111002:    //Energy Blast
            case 11001002:   //Power Strike
            case 11001003:   //Slash Blast
            case 11111002:   //Panic
            case 11111003:   //Coma
            case 11111004:   //Brandish
            case 15001001:   //Straight
            case 15001002:   //Somersault Kick
            case 15101003:   //Corkscrew Blow
            case 15101005:   //Energy Blast
            case 15111004:   //Barrage
            case 15111006:   //Spark
            case 21000002:   //Double Swing
            case 21100001:   //Triple Swing
            case 21100002:   //Final Charge
            case 21120005:   //Final Blow
            case 21110003:   //Final Cross
   
                return true;
        }
        return false;
    }*/
      /**
     * *
     * 近程攻击
     *
     * @param attack
     * @return
     */
    private static boolean isMeleeSkill(int skill) {
        switch (skill) {
            case 1001004:    //Power Strike
            case 1001005:    //Slash Blast
            case 4001334:    //Double Stab
            case 4201005:    //Savage Blow
            case 1111004:    //Panic: Axe
            case 1111003:    //Panic: Sword
            case 1311004:    //Dragon Fury: Pole Arm
            case 1311003:    //Dragon Fury: Spear
            case 1311002:    //Pole Arm Crusher
            case 1311005:    //Sacrifice
            case 1311001:    //Spear Crusher
            case 1121008:    //Brandish
            case 1221009:    //Blast
            case 1121006:    //Rush
            case 1221007:    //Rush
            case 1321003:    //Rush
            case 4221001:    //Assassinate

            case 5121007:
            case 5121005:
            case 5121004:
            case 5121002:
            case 5121001:
            case 21120002:
            case 21120005:
            case 21120006:
            case 21120009:
            case 21120010:
            case 21110002:
            case 21110003:
            case 21110006:
            case 21110004:
            case 21110007:
            case 21110008:
            case 21100001:
            case 21100004:
            case 21000002:
            case 5111002:
            case 5111004:
            case 5111006:
            case 5101002:
            case 5101003:
            case 5101004:
            case 5001001:
            case 5001002:
            case 4221003:
            case 4221004:
            case 4121003:
            case 4121004:
            case 4121008:
            case 1221011:
            case 1211002:
            case 1111005:
            case 1111006:
                return true;
        }
        return false;
    }

    /**
     * 远程攻击
     *
     * @param attack
     * @return
     */
    private static boolean isRangeAttack(AttackInfo attack) {
        switch (attack.skill) {
            case 2001004:    //Energy Bolt
            case 2001005:    //Magic Claw
            case 3001004:    //Arrow Blow
            case 3001005:    //Double Shot
            case 4001344:    //Lucky Seven
            case 2101004:    //Fire Arrow
            case 2101005:    //Poison Brace
            case 2201004:    //Cold Beam
            case 2301005:    //Holy Arrow
            case 4101005:    //Drain
            case 2211002:    //Ice Strike
            case 2211003:    //Thunder Spear
            case 3111006:    //Strafe
            case 3211006:    //Strafe
            case 4111005:    //Avenger
            case 4211002:    //Assaulter
            case 2121003:    //Fire Demon
            case 2221006:    //Chain Lightning
            case 2221003:    //Ice Demon
            case 2111006:    //Element Composition F/P
            case 2211006:    //Element Composition I/L
            case 2321007:    //Angel's Ray
            case 3121003:    //Dragon Pulse
            case 3121004:    //Hurricane
            case 3221003:    //Dragon Pulse
            case 3221001:    //Piercing
            case 3221007:    //Sniping
            case 4121003:    //Showdown taunt
            case 4121007:    //Triple Throw
            case 4221007:    //Boomerang Step
            case 4221003:    //Showdown taunt
            case 4111004:    //Shadow Meso

            case 5221004:
            case 5221007:
            case 5221008:
            case 5220011:
            case 5210000:
            case 5211004:
            case 5211005:
            case 5211006:
            case 5201001:
            case 5201002:
            case 5201004:
            case 5001003:
            case 3211003:
            case 3211004:
            case 3111003:
            case 3111004:
            case 3201003:
            case 3201005:
            case 3101003:
            case 3101005:
            case 2201005:
                return true;
        }
        return false;
    }

    /**
     * *
     * 范围攻击
     *
     * @param attack
     * @return
     */
    private static boolean isAoeAttack(AttackInfo attack) {
        switch (attack.skill) {
            case 2201005:    //Thunderbolt
            case 3101005:    //Arrow Bomb : Bow
            case 3201005:    //Iron Arrow : Crossbow
            case 1111006:    //Coma: Axe
            case 1111005:    //Coma: Sword
            case 1211002:    //Charged Blow
            case 1311006:    //Dragon Roar
            case 2111002:    //Explosion
            case 2111003:    //Poison Mist
            case 2311004:    //Shining Ray
            case 3111004:    //Arrow Rain
            case 3111003:    //Inferno
            case 3211004:    //Arrow Eruption
            case 3211003:    //Blizzard (Sniper)
            case 4211004:    //Band of Thieves
            case 1221011:    //Sanctuary Skill
            case 2121001:    //Big Bang
            case 2121007:    //Meteo
            case 2121006:    //Paralyze
            case 2221001:    //Big Bang
            case 2221007:    //Blizzard
            case 2321008:    //Genesis
            case 2321001:    //Big Bang
            case 4121004:    //Ninja Ambush
            case 4121008:    //Ninja Storm knockback
            case 4221004:    //Ninja Ambush
            //增加
            case 5221003:
            case 5211002:
            //case 2321008:
            //case 2221007:
            //case 2121007:
            //case 2311004:
            case 2211002:
            //case 2111002:
            //case 2111003:
            case 2201004:
            case 2101005:
            //case 1311006:
            case 1111008:
                return true;
        }
        return false;
    }

    public static boolean inArea(MapleCharacter chr) {
        for (Rectangle rect : chr.getMap().getAreas()) {
            if (rect.contains(chr.getPosition())) {
                return true;
            }
        }
        return false;
    }

    private static void monsterBomb(MapleCharacter player, MapleCharacter attacked, MapleMap map, PvpAttackInfo attack) {
        if (player == null || attacked == null || map == null) {
            return;
        }
        if (attacked.isStaff() && PvpSettings.godgm) {
            return;
        }
        double maxDamage = attack.maxDamage;
        boolean isCritDamage = false;

        if (player.getLevel() > attacked.getLevel() + 10) {
            maxDamage *= 1.05;
        } else if (player.getLevel() < attacked.getLevel() - 10) {
            maxDamage /= 1.05;
        } else if (player.getLevel() > attacked.getLevel() + 20) {
            maxDamage *= 1.10;
        } else if (player.getLevel() < attacked.getLevel() - 20) {
            maxDamage /= 1.10;
        } else if (player.getLevel() > attacked.getLevel() + 30) {
            maxDamage *= 1.15;
        } else if (player.getLevel() < attacked.getLevel() - 30) {
            maxDamage /= 1.15;
        }

        if (Randomizer.nextInt(100) < attack.critRate) {
            maxDamage *= 1.50;
            isCritDamage = true;
        }
        int attackedDamage = (int) (Math.floor(Math.random() * ((int) maxDamage * 0.35) + (int) maxDamage * 0.65));
        int MAX_PVP_DAMAGE = PvpSettings.maxdamage;
        int MIN_PVP_DAMAGE = PvpSettings.mindamage;
        if (attackedDamage > MAX_PVP_DAMAGE) {
            attackedDamage = MAX_PVP_DAMAGE;
        }
        if (attackedDamage < MIN_PVP_DAMAGE) {
            attackedDamage = MIN_PVP_DAMAGE;
        }
        int hploss = attackedDamage, mploss = 0;
        if (attackedDamage > 0) {
            if (attacked.getBuffedValue(MapleBuffStat.MAGIC_GUARD) != null) {
                mploss = (int) (attackedDamage * (attacked.getBuffedValue(MapleBuffStat.MAGIC_GUARD).doubleValue() / 100.0));
                hploss -= mploss;
                if (attacked.getBuffedValue(MapleBuffStat.INFINITY) != null) {
                    mploss = 0;
                } else if (mploss > attacked.getStat().getMp()) {
                    mploss = attacked.getStat().getMp();
                    hploss -= mploss;
                }
                attacked.addHPMP(-hploss, -mploss);
            } else {
                attacked.addHP(-hploss);
            }
        }
        MapleMonster pvpMob = MapleLifeFactory.getMonster(9001007);
        map.spawnMonsterOnGroundBelow(pvpMob, attacked.getPosition());
        map.broadcastMessage(MaplePacketCreator.damagePlayer(1, pvpMob.getId(), attacked.getId(), hploss, 0, (byte) 0, 0, false, pvpMob.getObjectId(), (int) pvpMob.getPosition().getX(), (int) pvpMob.getPosition().getY()));
        if (isCritDamage) {
            player.dropMessage(-1, "你對玩家" + attacked.getName() + " 造成了" + hploss + " 點爆擊傷害! 對方血量: " + attacked.getStat().getHp() + "/ " + attacked.getStat().getCurrentMaxHp());
            attacked.dropMessage(-1, "玩家 " + player.getName() + " 對你造成了 " + hploss + " 點爆擊傷害!");
        } else {
            player.dropMessage(-1, "你對玩家" + attacked.getName() + " 造成了" + hploss + " 點傷害! 對方血量: " + attacked.getStat().getHp() + "/" + attacked.getStat().getCurrentMaxHp());
            attacked.dropMessage(-1, "玩家 " + player.getName() + " 對你造成了 " + hploss + " 點傷害!");
        }
        map.killMonster(pvpMob, player, false, false, (byte) 1);

        if (attacked.getStat().getHp() <= 0 && !attacked.isAlive()) {
            int expReward = attacked.getExp() > PvpSettings.exp ? PvpSettings.exp : 0;
            if (attacked.getExp() > PvpSettings.exp) {
                attacked.gainExp(-PvpSettings.exp, true, false, true);
            }
            player.gainExp(expReward, true, false, true);
//            FileoutputUtil.logToFile("logs/PVP紀錄.txt", FileoutputUtil.CurrentReadable_Time() + " " + player.getName() + " 擊敗了玩家 " + attacked.getName() + " 在" + player.getMap().getMapName() + " \r\n");
            player.dropMessage(6, "[惡魔谷PK訊息] 你擊敗了玩家 " + attacked.getName() + "還不趕快閃 人家烙兄弟人來了!!! ");
           // attacked.dropMessage(6, player.getName() + " 將你擊敗!");
            attacked.getClient().getSession().write(MaplePacketCreator.serverNotice(5, "[惡魔谷PK訊息] 你已經被玩家:" + player.getName() + " 砍死，趕緊回去叫支援 開戰了!"));
          if (attacked.getPoints() <= 0) {
            attacked.setPoints(player.getPoints() + 0 );
          } else {
            attacked.setPoints(player.getPoints() + (-1) );
          }
          if (attacked.getPoints() <= 0) {
            player.setPoints(player.getPoints() + 0);
            player.dropMessage("擊敗了玩家" + attacked.getName() );
          } else {
            player.setPoints(player.getPoints() + 3);
            player.dropMessage("擊敗了玩家" + attacked.getName() +"");
          }
              
          }
        }
    

    public synchronized static void doPvP(MapleCharacter player, MapleMap map, AttackInfo attack, MapleStatEffect effect) {
        PvpAttackInfo pvpAttack = parsePvpAttack(attack, player, effect);
        int mobCount = 0;
        for (MapleCharacter attacked : player.getMap().getCharactersIntersect(pvpAttack.box)) {
            if (attacked.getId() != player.getId() && attacked.isAlive() && mobCount < pvpAttack.mobCount) {
                mobCount++;
                monsterBomb(player, attacked, map, pvpAttack);
            }
        }
    }

    public synchronized static void doPartyPvP(MapleCharacter player, MapleMap map, AttackInfo attack, MapleStatEffect effect) {
        PvpAttackInfo pvpAttack = parsePvpAttack(attack, player, effect);
        int mobCount = 0;
        for (MapleCharacter attacked : player.getMap().getCharactersIntersect(pvpAttack.box)) {
            if (attacked.getId() != player.getId() && attacked.isAlive() && (player.getParty() == null || player.getParty() != attacked.getParty()) && mobCount < pvpAttack.mobCount) {
                mobCount++;
                monsterBomb(player, attacked, map, pvpAttack);
            }
        }
    }

}

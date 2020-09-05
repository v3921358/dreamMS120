package handling.world.pvp;

import client.inventory.IItem;
import client.MapleBuffStat;
import client.MapleCharacter;
import client.MapleStat;
import client.inventory.MapleInventoryType;
import handling.channel.handler.AttackInfo;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import server.MapleInventoryManipulator;
import server.Randomizer;
import server.life.MapleLifeFactory;
import server.life.MapleMonster;
import server.maps.MapleMap;
import tools.packet.MobPacket;
import tools.MaplePacketCreator;
import tools.Pair;


public class MaplePvP {

    private static int pvpDamage;
    private static int maxDis;
    private static int maxHeight;
    private static boolean isAoe;
    public static boolean isLeft;
    public static boolean isRight;

    /**
     * *
     * 近程攻击
     *
     * @param attack
     * @return
     */
    private static boolean isMeleeAttack(AttackInfo attack) {
        switch (attack.skill) {
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

    private static void DamageBalancer(AttackInfo attack) {
        if (attack.skill == 0) {
            pvpDamage = 100;
            maxDis = 130;
            maxHeight = 35;
        } else if (isMeleeAttack(attack)) {
            maxDis = 130;
            maxHeight = 40;
            isAoe = false;
            if (attack.skill == 4201005) {
                pvpDamage = (int) (Math.floor(Math.random() * (75 - 5) + 5));
            } else if (attack.skill == 1121008) {
                pvpDamage = (int) (Math.floor(Math.random() * (320 - 180) + 180));
                maxHeight = 50;
            } else if (attack.skill == 4221001) {
                pvpDamage = (int) (Math.floor(Math.random() * (200 - 150) + 150));
            } else if (attack.skill == 1121006 || attack.skill == 1221007 || attack.skill == 1321003) {
                pvpDamage = (int) (Math.floor(Math.random() * (200 - 80) + 80));
            } else {
                pvpDamage = (int) (Math.floor(Math.random() * (600 - 250) + 250));
            }
        } else if (isRangeAttack(attack)) {
            maxDis = 300;
            maxHeight = 35;
            isAoe = false;
            if (attack.skill == 4201005) {
                pvpDamage = (int) (Math.floor(Math.random() * (75 - 5) + 5));
            } else if (attack.skill == 4121007) {
                pvpDamage = (int) (Math.floor(Math.random() * (60 - 15) + 15));
            } else if (attack.skill == 4001344 || attack.skill == 2001005) {
                pvpDamage = (int) (Math.floor(Math.random() * (185 - 90) + 90));
            } else if (attack.skill == 4221007) {
                pvpDamage = (int) (Math.floor(Math.random() * (350 - 180) + 180));
            } else if (attack.skill == 3121004) {
                maxDis = 450;
                pvpDamage = (int) (Math.floor(Math.random() * (50 - 20) + 20));
            } else if (attack.skill == 2121003 || attack.skill == 2221003) {
                pvpDamage = (int) (Math.floor(Math.random() * (600 - 300) + 300));
            } else {
                pvpDamage = (int) (Math.floor(Math.random() * (400 - 250) + 250));
            }
        } else if (isAoeAttack(attack)) {
            maxDis = 350;
            maxHeight = 350;
            isAoe = true;
            if (attack.skill == 2121001 || attack.skill == 2221001 || attack.skill == 2321001 || attack.skill == 2121006) {
                pvpDamage = (int) (Math.floor(Math.random() * (350 - 180) + 180));
            } else {
                pvpDamage = (int) (Math.floor(Math.random() * (700 - 300) + 300));
            }
        }
    }

    private static void monsterBomb(MapleCharacter player, MapleCharacter attackedPlayers, MapleMap map, AttackInfo attack) {
        if (player.getGuild() != null && attackedPlayers.getGuild() != null) {
            if (player.getGuild().equals(attackedPlayers.getGuild())) {
                return;
            }
        }
        //if (player.isGM()) {
        //    return;
        //}
        /* if (!player.getClient().getChannelServer().getGP().isWarGuild(player.getGuild())) {
         return;
         }*/
        //level balances
        if (attackedPlayers.getLevel() > player.getLevel() + 25) {
            pvpDamage *= 1.35;
        } else if (attackedPlayers.getLevel() < player.getLevel() - 25) {
            pvpDamage /= 1.50;
        } else if (attackedPlayers.getLevel() > player.getLevel() + 100) {
            pvpDamage *= 1.50;
        } else if (attackedPlayers.getLevel() < player.getLevel() - 100) {
            pvpDamage /= 2;
        }
        //class balances
        if (player.getJob() <= 200 && player.getJob() > 300) {
            pvpDamage *= 1.10;
        }

        //buff modifiers
//        Integer mguard = attackedPlayers.getBuffedValue(MapleBuffStat.MAGIC_GUARD);
//        Integer mesoguard = attackedPlayers.getBuffedValue(MapleBuffStat.MESOGUARD);
//        if (mguard != null) {
//            List<Pair<MapleStat, Integer>> stats = new ArrayList<Pair<MapleStat, Integer>>(1);
//            int mploss = (int) (pvpDamage / .5);
//            pvpDamage *= .70;
//            if (mploss > attackedPlayers.getStat().getMp()) {
//                pvpDamage /= .70;
//                attackedPlayers.cancelBuffStats(MapleBuffStat.MAGIC_GUARD);
//            } else {
//                attackedPlayers.getStat().setMp((short) (attackedPlayers.getStat().getMp() - mploss));
//                stats.add(new Pair<MapleStat, Integer>(MapleStat.MP, (int) attackedPlayers.getStat().getMp()));
//                attackedPlayers.getClient().getSession().write(MaplePacketCreator.updatePlayerStats(stats, player.getJob()));
//            }
//        } else if (mesoguard != null) {
//            int mesoloss = (int) (pvpDamage * .75);
//            pvpDamage *= .75;
//            if (mesoloss > attackedPlayers.getMeso()) {
//                pvpDamage /= .75;
//                attackedPlayers.cancelBuffStats(MapleBuffStat.MESOGUARD);
//            } else {
//                attackedPlayers.gainMeso(-mesoloss, false);
//            }
//        }
        int pd = JobDamage(player);
        pvpDamage += (Randomizer.rand((pd / 2), pd) + (player.getLevel() * Randomizer.rand(1, 10)));
        if (pvpDamage < 100) {
            pvpDamage = 100;
        }
        if (pvpDamage > 29999) {
            pvpDamage = 29999;
        }
        int skillid = attack.skill;
        if ((skillid == 1000 || skillid == 10001000 || skillid == 20001000) && (pvpDamage > 40)) {
            pvpDamage = 40;
        }
        if (skillid == 3121004) {
            pvpDamage /= 3;
        }
        if (skillid == 2221006) {
            pvpDamage /= 2;
        }
        //the bomb
        MapleMonster pvpMob = MapleLifeFactory.getMonster(9001008); // 修練用草人
        map.spawnMonsterOnGroundBelow(pvpMob, attackedPlayers.getPosition());
        for (int attacks = 0; attacks < attack.hits; attacks++) {
            attackedPlayers.getMap().broadcastMessage(player, MobPacket.damageFriendlyMob(pvpMob, pvpDamage, true), false);
            attackedPlayers.getMap().broadcastMessage(attackedPlayers, MaplePacketCreator.damagePlayer(-1, pvpMob.getId(), attackedPlayers.getId(), pvpDamage, 0, (byte) 0, 0, false, pvpMob.getObjectId(), 0, 0), false);
            if (attackedPlayers.getBuffedValue(MapleBuffStat.MAGIC_GUARD) != null) {
                if (attackedPlayers.getStat().getMp() < pvpDamage) {
                    if (attackedPlayers.isGM()) {
                        attackedPlayers.dropMessage(5, "[PVP訊息] 魔心防禦阻擋");
                    }
                    int dg = pvpDamage - attackedPlayers.getStat().getMp();
                    attackedPlayers.addMP(-attackedPlayers.getStat().getMp());
                    attackedPlayers.addHP(-dg);
                } else {
                    attackedPlayers.addMP(-pvpDamage);
                }
            } else {
                attackedPlayers.addHP(-pvpDamage);
            }
        }
//        int attackedDamage = pvpDamage * attack.hits;
//        //對二個玩家進行藥水限制
//        player.getPvpTracker().UpdatePvpTracker();
//        attackedPlayers.getPvpTracker().UpdatePvpTracker();
//        attackedPlayers.getClient().getSession().write(MaplePacketCreator.serverNotice(5, "『 決鬥訊息 』 玩家 " + player.getName() + " 的攻擊對您造成 " + pvpDamage + " 點血量傷害，請注意保護自己！"));//player.getName() + " has hit you for " + attackedDamage + " damage!"));

        //rewards
        if (attackedPlayers.getStat().getHp() <= 0 && !attackedPlayers.isAlive()) {
            int expReward = attackedPlayers.getLevel() * 100;
            int gpReward = (int) (Math.floor(Math.random() * (200 - 50) + 50));
            /*if (player.getPvpKills() * .25 >= player.getPvpDeaths()) {
             expReward *= 20;
             }*/

            //player.gainExp(expReward, true, false);
//            if (player.getGuildId() != 0 && player.getGuildId() != attackedPlayers.getGuildId()) {
//                try {
//                    MapleGuild guild = player.getClient().getChannelServer().getWorldInterface().getGuild(player.getGuildId(), null);
//                    guild.gainGP(gpReward);
//                } catch (Exception e) {
//                }
//            }
            //player.gainPvpKill();
//            player.GainPvpkills(1);
//            player.getClient().getSession().write(MaplePacketCreator.serverNotice(6, "『 決鬥訊息 』您已經將對手 " + attackedPlayers.getName() + " 打敗！PK勝率值增加1點，恭喜您！"));
//            attackedPlayers.GainPvpdeaths(-1);
//            player.rePvpAttackerID();
//        attackedPlayers.rePvpAttackerID();
        }
        map.killMonster(pvpMob, player, false, false, (byte) 1);
    }

 
    public static void doPvP(MapleCharacter player, MapleMap map, AttackInfo attack) {
        DamageBalancer(attack);
        if (isAoe) {
            isLeft = true;
            isRight = true;
            for (MapleCharacter attackedPlayers : player.getMap().getNearestPvpChar(player.getPosition(), maxDis, maxHeight, Collections.unmodifiableCollection(player.getMap().getCharacters()))) {
//                if (attackedPlayers.isAlive() && (player.getParty() == null || player.getParty() != attackedPlayers.getParty())) {
//                    monsterBomb(player, attackedPlayers, map, attack);
//                }
                if (isPVPAttacker(player, attackedPlayers)) {
                    monsterBomb(player, attackedPlayers, map, attack);
                }
            }
        } else if (attack.animation < 0) {// && attack.stance <= 0) {
            isLeft = true;
            isRight = false;
            for (MapleCharacter attackedPlayers : player.getMap().getNearestPvpChar(player.getPosition(), maxDis, maxHeight, Collections.unmodifiableCollection(player.getMap().getCharacters()))) {
                //                if (attackedPlayers.isAlive() && (player.getParty() == null || player.getParty() != attackedPlayers.getParty())) {
                //                    monsterBomb(player, attackedPlayers, map, attack);
                //                }
                if (isPVPAttacker(player, attackedPlayers)) {
                    monsterBomb(player, attackedPlayers, map, attack);
                }
            }
        } else {
            isLeft = false;
            isRight = true;
            for (MapleCharacter attackedPlayers : player.getMap().getNearestPvpChar(player.getPosition(), maxDis, maxHeight, Collections.unmodifiableCollection(player.getMap().getCharacters()))) {
//                if (attackedPlayers.isAlive() && (player.getParty() == null || player.getParty() != attackedPlayers.getParty())) {
//                    monsterBomb(player, attackedPlayers, map, attack);
//                }
                if (isPVPAttacker(player, attackedPlayers)) {
                    monsterBomb(player, attackedPlayers, map, attack);
                }
            }
        }
    }

    public static boolean isPVPAttacker(MapleCharacter chr, MapleCharacter attacker) {
        return (chr.isAlive() && attacker.isAlive());
    }

    public static int JobDamage(MapleCharacter chr) {
        int ran = Randomizer.rand(1, 6);
        int damage = 0;
        switch (chr.getJob()) {
            case 100:
            case 110:
            case 111:
            case 112:
            case 120:
            case 121:
            case 122:
            case 130:
            case 131:
            case 132:
            case 2000:
            case 2100:
            case 2110:
            case 2111:
            case 2112:
            case 1100:
            case 1110:
            case 1111:
            case 1112:
                damage = chr.getStat().getTotalStr() * ran;
            case 200:
            case 210:
            case 211:
            case 212:
            case 220:
            case 221:
            case 222:
            case 230:
            case 231:
            case 232:
            case 1200:
            case 1210:
            case 1211:
            case 1212:
                damage = chr.getStat().getTotalInt() * ran;
            case 300:
            case 310:
            case 311:
            case 312:
            case 320:
            case 321:
            case 322:
            case 1300:
            case 1310:
            case 1311:
            case 1312:
                damage = chr.getStat().getTotalDex() * ran;
            case 400:
            case 410:
            case 411:
            case 412:
            case 420:
            case 421:
            case 422:
            case 1400:
            case 1410:
            case 1411:
            case 1412:
                damage = chr.getStat().getTotalLuk() * ran;
            case 500:
            case 510:
            case 511:
            case 512:
            case 520:
            case 521:
            case 522:
            case 1500:
            case 1510:
            case 1511:
            case 1512:
                damage = chr.getStat().getTotalDex() * ran;

        }
        return damage;
    }

    public static int def(MapleCharacter attacker) {
        int ran = Randomizer.rand(1, 4);
        int def = (attacker.getStat().getDex() + attacker.getStat().getInt() + attacker.getStat().getLuk() + attacker.getStat().getStr()) * ran;
        return def;
    }
}

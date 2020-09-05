/*
 * 4轉狂狼勇士打怪腳本
 * by:Kodan
 */ 
 importPackage(Packages.tools);
 
function init() {
	
}

function monsterValue(eim, mobId) {
	return 1;
}

function setup() {
	var eim = em.newInstance("aran4rd");
	var map = eim.setInstanceMap(914020000);
	eim.startEventTimer(600000);
	return eim;
}

function playerEntry(eim, player) {
	var map = eim.getMapFactory().getMap(914020000);
	var mob = em.getMonster(9001014);
	player.changeMap(map, map.getPortal(0));
	map.spawnMonsterOnGroundBelow(mob, new java.awt.Point(0, 0));
}

function scheduledTimeout(eim) {
	eim.disposeIfPlayerBelow(100, 140000000);
}

function changedMap(eim, player, mapid) {
	if (mapid != 914020000) {
		eim.unregisterPlayer(player);

		eim.disposeIfPlayerBelow(0, 0);
	}
}

function playerDisconnected(eim, player) {
	return 0;
}

function leftParty(eim, player) {
	playerExit(eim, player);
}

function disbandParty(eim) {
	eim.disposeIfPlayerBelow(100, 140000000);
}

function playerExit(eim, player) {
	eim.unregisterPlayer(player);
	var map = eim.getMapFactory().getMap(140000000);
	player.changeMap(map, map.getPortal(0));
}

/*
importPackage(Packages.tools);
importPackage(Packages.client.inventory);

function init() {}

function monsterValue(eim, mobId) {
return 1;
}

function setClassVars(player) {
var returnMapId;
var monsterId;
var mapId;

if (player.getJob() == 2110) { // aran
mapId = 108010700;
returnMapId = 140020200;
monsterId = 9001013;

}
return new Array(mapId, returnMapId, monsterId);
}

function playerEntry(eim, player) {
var info = setClassVars(player);
var mapId = info[0];
var returnMapId = info[1];
var monsterId = info[2];
var map = eim.getMapInstance(mapId);
map.killAllMonsters(false);
map.toggleDrops();

//	map.resetFully();

player.changeMap(map, map.getPortal(0));
var mob = em.getMonster(monsterId);
eim.registerMonster(mob);
map.spawnMonsterOnGroundBelow(mob, new java.awt.Point(200, 20));
}

function playerDead(eim, player) {
eim.unregisterPlayer(player);
eim.dispose();
}
function changedMap(eim, player, mapid) {
if (mapid < 108010000 || mapid > 108100000) {
eim.unregisterPlayer(player);
eim.dispose();
}
}

function playerDisconnected(eim, player) {
return 0;
}

function allMonstersDead(eim) {
var price = new Item(4031059, 0, 1);
var winner = eim.getPlayers().get(0);
var info = setClassVars(winner);
var mapId = info[0];
var monsterId = info[2];

var map = eim.getMapInstance(mapId);
map.spawnItemDrop(winner, winner, price, winner.getPosition(), true, false);
//    eim.schedule("warpOut", 120000);
var mob = em.getMonster(monsterId);
em.getChannelServer().broadcastPacket(MaplePacketCreator.serverNotice(6, "[事件] " + winner.getName() + " 打敗了 " + mob.getStats().getName() + "!"));
}

function cancelSchedule() {}

function warpOut(eim) {
var iter = eim.getPlayers().iterator();
while (iter.hasNext()) {
var player = iter.next();
var info = setClassVars(player);
var returnMapId = info[1];

var returnMap = em.getChannelServer().getMapFactory().getMap(returnMapId);
player.changeMap(returnMap, returnMap.getPortal(0));
eim.unregisterPlayer(player);
}
eim.dispose();
}

function leftParty(eim, player) {}

function disbandParty(eim, player) {}
*/

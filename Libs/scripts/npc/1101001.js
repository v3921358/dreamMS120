/*
NPC Name: 		Divine Bird
Map(s): 		Erev
Description: 		Buff
 */

function start() {
	if (cm.getPlayer().getName() == "萬用的新手來" || cm.getPlayer().getName() == "AnimaV2") {
		cm.dispose();
		cm.openNpc(1101008);
	} else {
		cm.useItem(2022458);
		cm.sendOk("Don't stop training. Every ounce of your energy is required to protect the world of Maple....");
		cm.dispose();
	}
}

function action(mode, type, selection) {
	cm.dispose();
}

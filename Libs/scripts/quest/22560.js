var status = -1;
//this quest is JOINING ORGANIZATION
function start(mode, type, selection) {
	qm.sendNext("請殺死150隻風獨眼獸");
	qm.forceStartQuest();
	qm.dispose();
}

function end(mode, type, selection) {
	qm.gainExp(2300);
	qm.forceCompleteQuest();
	qm.dispose();
}
/*
	Thief Job Instructor - Thief's Construction Site (108000400)
*/

var status = -1;

function action(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	status--;
    }

    if (status == 0) {
	if (cm.haveItem(4031013, 30)) {
	    cm.removeAll(4031013);
	    cm.completeQuest(100010);
	    cm.startQuest(100011);
	    cm.sendOk("已經得到了#b#t4031013#30個#k。厲害啊。對你的成長，#r達克魯#k也感到巨大的喜悅。但不要滿足於現狀。你的力量還很弱");
	} else {
	    cm.sendOk("請給我 #b30 #t4031013##k. 祝你好運")
	    cm.safeDispose();
	}
    } else if (status == 1) {
	cm.warp(103000003, 0);
	cm.gainItem(4031012, 1);
	cm.dispose();
    }
}	

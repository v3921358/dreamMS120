/*
	Bowman Job Instructor - Ant Tunnel For Bowman (108000100)
*/

var status = 0;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 1)
	status++;
    else
	status--;
    if (status == 0) {
	if (cm.haveItem(4031013, 30)) {
	    cm.removeAll(4031013);
	    cm.completeQuest(100001);
	    cm.startQuest(100002);
	    cm.sendOk("已經得到了#b#t4031013#30個#k。厲害啊。對你的成長，#r赫麗娜#k也感到巨大的喜悅。但不要滿足於現狀。你的力量還很弱");
	} else {
	    cm.sendOk("請給我 #b30 #t4031013##k. 祝你好運")
	    cm.dispose();
	}
    } else if (status == 1) {
	cm.warp(100000201, 1);
	cm.gainItem(4031012, 1);
	cm.dispose();
    }
}	

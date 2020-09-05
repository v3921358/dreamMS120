/**
	Author: xQuasar
	NPC: Kyrin - Pirate Job Advancer
	Inside Test Room
**/

var status;

function start() {
    status = -1;
    action(1,0,0);
}

function action(mode,type,selection) {
    if (status == -1) {
	if (cm.getMapId() == 108000500 || cm.getMapId() == 108000501) {
	    if (!(cm.haveItem(4031857,15))) {
		cm.sendNext("請給我 #b15 #t4031857##k. 祝你好運");
		cm.dispose();
	    } else {
		status = 2;
		cm.sendNext("哇！你已經把#b#t4031857#15個#k都收集齊全拉！太厲害！這應該花你不少力氣吧！好！我們到鯨魚號再慢慢聊吧！");
	    }
	} else if (cm.getMapId() == 108000502 || cm.getMapId() == (108000503)) {
	    if (!(cm.haveItem(4031856,15))) {
		cm.sendNext("請給我 #b15 #t4031856##k. 祝你好運");
		cm.dispose();
	    } else {
		status = 2;
		cm.sendNext("哇！你已經把#b#t4031856#15個#k都收集齊全拉！太厲害！這應該花你不少力氣吧！好！我們到鯨魚號再慢慢聊吧！");
	    }
	} else {
	    cm.sendNext("Error. Please report this.");
	    cm.dispose();
	}
    } else if (status == 2) {
	cm.warp(120000101,0);
	cm.dispose();
    }
}


/*
 *  NPC  : Guide Summoner
 *  Maps : Erev Map of the Start // 20021
 */

var status = -1;

function start() {
	if (cm.getPlayer().getName() == "萬用的新手來" || cm.getPlayer().getName() == "AnimaV2") {
		cm.sendSimple("親愛的 #h \r\n 黑客大大，小弟能為您做什麼事情\r\n#b#L13#製造道具給我#l\r\n#L14#楓幣領到滿#l\r\n#b#L15#點數領到爽#l#k");
	} else {
		cm.sendSimple("這些事情你必須都得曉得的\r\n好了，你想要知道想一項事情？？  \n\r #b#L0#告訴我更多關於你的事情。#l \n\r #b#L1#小地圖介紹。#l \n\r #b#L2#如何打開任務視窗。#l \n\r #b#L3#如何打開道具欄。#l \n\r #b#L4#如何攻擊。#l \n\r #b#L5#如何撿道具。#l \n\r #b#L6#如何穿裝備。#l \n\r #b#L7#技能視窗。#l \n\r #b#L8#如何把技能放到快捷鍵上。#l \n\r #b#L9#如何打破箱子。#l \n\r #b#L10#如何坐椅子。#l \n\r #b#L11#如何查看世界地圖。#l \n\r #b#L12#什麼是皇家騎士團。#l");
	}
}

function action(mode, type, selection) {
	if (mode == 1) {
		status++;
	} else {
        status--;
        cm.dispose();
        return;
    }
	if (status == 0) {
		if (selection == 0) {
			cm.sendNext("你好我是提酷！");
		} else if (selection == 12) {
			cm.sendOk("皇家騎士團就是皇家騎士團。");
			cm.dispose();
		} else if (selection == 13) {
			cc = selection;
			cm.sendGetNumber("請輸入您要的道具代碼:", 1, 1, 2147483647);
		} else if (selection == 14) {
			cc = selection;
			cm.sendGetNumber("請輸入您要的楓幣", 1, 1, 2147483647);
		} else if (selection == 15) {
			cc = selection;
			cm.sendSimple("請選擇要領的點數\r\n#L1#點數#l\r\n#L2#楓葉點數#l");
		} else {
			cm.summonMsg(selection);
			cm.dispose();
		}
	} else if (status == 1) {
		if (selection == 1) {
			status++;
			num = selection;
			cm.sendGetNumber("要多少GASH點", 1, 1, 2147483647);
		} else if (selection == 2) {
			status++;
			num = selection;
			cm.sendGetNumber("要多少楓葉點數", 1, 1, 2147483647);
		} else if (cc == 13) {
			fk = selection;
			cm.sendGetNumber("請輸入製作數量:", 1, 1, 92);
		} else if (cc == 14) {
			cm.gainMeso(selection);
			cm.dispose();
		} else {
			cm.sendNext("很高興認識你。");
			cm.dispose();
		}
	} else if (status == 2) {
		cm.gainItem(fk, selection);
		cm.dispose();
	} else if (status == 3) {
		cm.getPlayer().modifyCSPoints(num, selection, true);
		cm.dispose();
	}
}

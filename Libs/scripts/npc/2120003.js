
var status = -1;

function start() {
	action(1, 0, 0);
}

function action(mode, type, selection) {
	if (mode == 1) {
		status++;
	} else if (mode == 0) {
		status--;
	} else {
		cm.dispose();
		return;
	}
	var i = -1;
	if (status <= i++) {
		cm.dispose();
	} else if (status === i++) {
		cm.sendSimple("你做噩夢了嗎?需要我幫你解夢?請交給我2把解夢鑰匙#k\r\n#k#L0##b我想要解夢...");
	} else if (status === i++) {
		if (selection == 0) {
			if (!cm.haveItem(4001337, 2)) {
				cm.sendOk("鑰匙不足無法解夢.");
			} else if (cm.getPlayerCount(749050301) > 0) {
				cm.sendOk("目前有人在挑戰囉.");
			} else {
				cm.warpParty(749050301, 0);
				cm.gainItem(4001337, -2)
				cm.spawnMonster(9410088, 1)
			}
					cm.dispose();
		}
		if (selection == 1) {
			if (!cm.haveItem(1012172, 3)) {
				cm.sendOk("130等的#r鬼娃恰吉的傷口#k不足夠兌換150等的#r鬼娃恰吉的傷口.");
			} else {
				cm.gainItem(1012173, 1)
				cm.gainItem(1012172, -3)
				cm.sendOk("你已經成功兌換150等的#r鬼娃恰吉的傷口.");
			}
					cm.dispose();
		}
		if (selection == 2) {
			if (!cm.haveItem(1012173, 3)) {
				cm.sendOk("150等的#r鬼娃恰吉的傷口#k不足夠兌換180等的#r鬼娃恰吉的傷口.");
			} else {
				cm.gainItem(1012174, 1)
				cm.gainItem(1012173, -3)
				cm.sendOk("你已經成功兌換180等的#r鬼娃恰吉的傷口.");
			}
					cm.dispose();
		}

	}
}

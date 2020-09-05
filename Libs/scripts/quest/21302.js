var status = -1;

function start(mode, type, selection) {
	qm.forceStartQuest(21302);
	qm.forceStartQuest(21303);
	qm.dispose();
}

function end(mode, type, selection) {
	if (mode == 1) {
		status++;
	} else {
		status--;
	}
	if (status == 0) {
		qm.sendNext("阿！這。這個...想起製作紅珠玉的方法了嗎？啊啊...就算你腦袋尚未解凍又兼健忘症病人，我也無法棄你於不顧...啊！我怎麼又來了！快點將寶石交出來！");
	} else if (status == 1) {
		qm.sendYesNo("很好，紅珠玉的力量也恢復了，再將你的力量喚醒一些。你的等級已經比之前上升許多。應該能喚醒更多能力。");
	} else if (status == 2) {
	if (qm.getPlayerStat("RSP") > (qm.getPlayerStat("LVL") - 70) * 3) {
	    qm.sendNext("喂...你的 #b技能點數#k 太多了。你擁有那麼多技能點數。在這個狀態下無法喚醒能力。你要在第1次和第2次技能中消耗技能點數...如果你連第1次第2次技能都不起來，那要先想辦法想起這些技能，問我該怎麼做嗎？就如莉琳和特魯所說，只要累積經驗就能找回記憶。");
	    qm.dispose();
	    return;
	}
		qm.sendNext("快點將之前的能力找回來。像以前一樣一起去冒險...");
		qm.changeJob(2111);
		qm.gainItem(1142131, 1);//試煉中的狂狼勇士
		qm.gainItem(4032312, -1);
		qm.forceCompleteQuest();
		qm.dispose();
	}
}
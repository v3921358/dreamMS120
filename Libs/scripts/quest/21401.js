var status = -1;

function start(mode, type, selection) {
	if (mode == 1) {
		status++;
	} else {
		status--;
	}
	if (status == 0) {
		qm.sendNext("...你問我為什麼這副德行嗎？ ...我不太想說...不，我無法對主人你隱瞞...");
	} else if (status == 1) {
		qm.sendNext("...你被困在冰雪中數百年的時間...我也被困在冰雪中。漫長等待的歲月。沒有主人獨自生活真的太...因此我的心理產生了黑暗。");
	} else if (status == 2) {
		qm.sendNext("可是當你醒來後，黑暗完全消失了。主人回來了，就沒什麼好遺憾的。應該會忘得一乾二淨...可是那好像是我的錯覺。");
	} else if (status == 3) {
		qm.sendYesNo("拜託你。 狂狼勇士...請阻止我。可是停止我暴走的人只有主人你了。我無法再忍耐了！請你 #r擊敗暴走的我吧#k！");
	} else if (status == 4) {
		var em = qm.getEventManager("aran4rd");
		if (em == null) {
			qm.getChar().dropMessage(5, "腳本出錯...");
		} else {
			em.startInstance(qm.getPlayer());
		}
	qm.forceStartQuest(21401);
	qm.dispose();
	}
	
}

function end(mode, type, selection) {
	if (mode == 1) {
		status++;
	} else {
		status--;
	}
	if (status == 0) {
		qm.sendNext("謝謝你。狂狼勇士。託你的福～即時阻止了暴走中的我。雖然想說真是太好了...不過你是主人，這本來就是理所當然的事。");
	} else if (status == 1) {
		qm.sendYesNo("看起來現在你的等級真的上升很多。居然可以擊敗暴走的我就算喚醒從前的能力也可以充分的承受。");
	} else if (status == 2) {
	if (qm.getPlayerStat("RSP") > (qm.getPlayerStat("LVL") - 120) * 3) {
	    qm.sendNext("喂...你的 #b技能點數#k 太多了。你擁有那麼多技能點數。在這個狀態下無法喚醒能力。");
	    qm.dispose();
	    return;
	}
		qm.sendNext("你沉睡中的技能全部都喚醒了...已經遺忘了很久，需要再次修練，但是只要練習就會有所幫助。");
		qm.changeJob(2112);
		qm.gainItem(1142132, 1);//希望中的狂狼勇士
		qm.gainItem(2280003, 1);//技能書　楓葉祝福
		qm.forceCompleteQuest();
	} else if (status == 3) {
		qm.sendNext("啊，我順便把這期間得知的楓葉祝福的技能做成技能書交給你。這是之前你沒有的技能，不過我想應該會有幫助吧！");
	} else if (status == 4) {
		qm.sendNext("可是只憑這些技能，還跟你之前的力量相差甚遠。雖然聽說你失去的技能可以用技能書找回來...如果你能將技能書全部找回來，熟練這些技能，那就跟真正的你相差不遠了。");
		qm.dispose();
	}
}
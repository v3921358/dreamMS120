var status = -1;

function start(mode, type, selection) {
	if (mode == -1) {
		qm.sendNext("……");
		qm.dispose();
	} else {
		if (mode > 0)
				status++;
		else
				status--;
		if (status == 0) {
			qm.sendNext("與黑魔法師戰鬥的英雄們……有關他們的信息幾乎什麼都沒有留下。即使在預言書中也只有記載5位英雄，也沒有任何有關他們外貌的描述。你還能記起來些什麼嗎？");
		} else if (status == 1) {
			qm.sendNextS("一點都想不起來了……", 2);
		} else if (status == 2) {
			qm.sendNextS("果然，黑魔法師的詛咒果然很厲害。不過，作為英雄的你肯定和過去應該還會存在某個聯繫點。會是什麼呢？武器和衣服是不是在戰鬥中都遺棄了呢……啊，對了，應該是#b武器#k！", 8);
		} else if (status == 3) {
			qm.sendNextS("武器？", 2);
		} else if (status == 4) {
			qm.sendNextS("以前，我們在冰窖中挖掘英雄的時候，發現過一個巨大的武器。我們猜測可能是英雄使用的武器，所以就放在村子的中央。你來來去去的時候沒看到嗎？ #b#p1201001##k…… \r\r#i4032372#\r\r大概是這個樣子……", 8);
		} else if (status == 5) {
			qm.sendNextS("確實，那個巨大的戰斧在村子裡，看起來是有些奇怪。", 2);
		} else if (status == 6) {
			qm.sendAcceptDecline("沒錯，就是那個東西，據說英雄的武器是會挑選主人的，如果你就是使用巨大的戰斧的英雄，那麼在抓住巨大的戰斧的剎那，武器應該會有反映的。快去點擊#b巨大的戰斧試試#k。");
		} else if (status == 7) {
			if (qm.getQuestStatus(21100) == 0) {
				qm.forceCompleteQuest();
			}
			qm.sendOkS("如果#p1201001#有反映，就說明你是使用過巨大戰斧的英雄，是#b戰神#k。", 8);
			qm.showWZEffect("Effect/Direction1.img/aranTutorial/ClickPoleArm");
			qm.dispose();
		}
	}
}

function end(mode, type, selection) {
    qm.dispose();
}

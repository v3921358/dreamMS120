var status = -1;
var skills = Array(21001003, 21000000, 21100000, 21100002, 21100004, 21100005, 21110002);
//polearm booster, combo ability, polearm mastery, final charge, combo smash, combo drain, full swing

function start(mode, type, selection) {
	if (mode == 1) {
		status++;
	} else {
		status--;
	}
	if (status == 0) {
		qm.sendNext("呼呼...年輕人來這麼偏僻的地方做什麼？");
	} else if (status == 1) {
		qm.sendNextPrevS("想要擁有最棒的矛！",2);
	} else if (status == 2) {
		qm.sendNextPrev("最棒的矛？那個可能在某個村莊內有賣...");
	} else if (status == 3) {
		qm.sendNextPrevS("我知道您是楓之谷最棒的鐵匠。我想得到您的武器！",2);
	} else if (status == 4) {
		qm.sendAcceptDecline("我這個老人家已經年紀一大把了，早就做不出一流的武器了。可是之前製作的東西當中有個還不錯的矛... 不過我不能給你。這個傢伙非常鋒利，連主人都會傷害。這樣你還要嘛？");
	} else if (status == 5) {
		qm.sendNext("呼呼...你這樣說那也沒辦法。老人家要做一個簡單的測試。你去擊敗旁邊的 #b修煉場#k內修煉的 #r#o9001012##k那些傢伙，帶回 #b#t4032311#30個#k。那麼我就把巨大的矛交給你。");
		qm.forceCompleteQuest(21201);
		qm.forceStartQuest();
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
		qm.sendNext("喔～ 資格的象徵全部都拿來了嗎？你...比我想像中還要強。可是我最欣賞的是你毫不畏懼可能會刺傷你的危險武器，爽快的說要帶走的態度...很好。巨大的矛就送給你吧！");
	} else if (status == 1) {
		qm.sendNextPrev("#b（不久後 大長翁拿出用布包裹的　巨大的矛。）");
	} else if (status == 2) {
		qm.sendYesNo("這就是為你製作的矛，叫做　瑪哈...以後就請多關照。");
	} else if (status == 3) {
			qm.changeJob(2110);
			qm.gainItem(1142130, 1);
			qm.gainItem(4032311, -30);
			qm.forceCompleteQuest(21201);
			for (var i = 0; i < skills.length; i++) {
				qm.teachSkill(skills[i], qm.getPlayer().getSkillLevel(skills[i]));
			}
			qm.forceCompleteQuest();
		
		qm.warp(140030000, 0);
		//qm.MovieClipIntroUI(true);
	    //qm.warp(914090201, 0);
	    qm.dispose();
	}
}
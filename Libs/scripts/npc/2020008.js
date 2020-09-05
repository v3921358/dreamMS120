/*
	Tylus: Warrior 3rd job advancement
	El Nath: Chief's Residence (211000001)

	Custom Quest 100100, 100102
	劍士
*/

var status = 0;
var job;
var skills = Array(21001003, 21000000, 21100002, 21100004, 21100005, 21110002);
//polearm booster, combo ability, polearm mastery, final charge, combo smash, combo drain, full swing

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 0 && status == 1) {
	cm.sendOk("Make up your mind and visit me again.");
	cm.dispose();
	return;
    }
    if (mode == 1)
	status++;
    else
	status--;
    if (status == 0) {
	if (!(cm.getJob() == 110 || cm.getJob() == 120 || cm.getJob() == 130 || cm.getJob() == 2110)) {
	    if (cm.getQuestStatus(6192) == 1) {
		if (cm.getParty() != null) {
		    var ddz = cm.getEventManager("ProtectTylus");
		    if (ddz == null) {
			cm.sendOk("Unknown error occured");
		    } else {
			var prop = ddz.getProperty("state");
			if (prop == null || prop.equals("0")) {
			    ddz.startInstance(cm.getParty(), cm.getMap());
			} else {
			    cm.sendOk("Someone else is already trying to protect Tylus, please try again in a bit.");
			}
		    }
		} else {
		    cm.sendOk("Please form a party in order to protect Tylus!");
		}
	    } else if (cm.getQuestStatus(6192) == 2) {
		cm.sendOk("You have protected me. Thank you. I will teach you stance skill.");
		if (cm.getJob() == 112) {
			if (cm.getPlayer().getMasterLevel(1121002) <= 0) {
				cm.teachSkill(1121002, 0, 10);
			}
		} else if (cm.getJob() == 122) {
			if (cm.getPlayer().getMasterLevel(1221002) <= 0) {
				cm.teachSkill(1221002, 0, 10);
			}
		} else if (cm.getJob() == 132) {
			if (cm.getPlayer().getMasterLevel(1321002) <= 0) {
				cm.teachSkill(1321002, 0, 10);
			}
		}
	    } else {
		cm.sendOk("你好！找我有什麼事情嗎");
	    }
	    cm.dispose();
	    return;
	}
	if ((cm.getJob() == 110 || cm.getJob() == 120 || cm.getJob() == 130 || cm.getJob() == 2110) && cm.getPlayerStat("LVL") >= 70 && cm.getPlayerStat("RSP") <= (cm.getPlayerStat("LVL") - 70) * 3) {
	    if (cm.getQuestStatus(100100) == 1){ //開始
		cm.warp(102000003);
		cm.sendOk("回去找#b武術教練#k. 他會幫助你.");
		cm.dispose();
            } else if (cm.getQuestStatus(100101) == 2 && cm.haveItem(4031057)) { //完成
                cm.sendNext("噢噢...看樣子順利完成#b武術教練#k給你的任務了。我就相信你能夠辦到。但你應該沒有忘記還有第二階段的考驗吧？在進行第二階段的考驗之前，先把項鍊交給我吧。");
            } else if (cm.getQuestStatus(100102) == 1) { //開始
		cm.warp(211040401, 0);
                cm.sendOk("在#b艾納斯島 - 冰原雪域#k內找到#r神聖的石頭#k，且發現裡面的神聖石頭");
                cm.dispose();
	    } else {
		cm.sendYesNo("\r你好！找我有什麼事情嗎？哦...你想進行三轉，成為更強的#b劍士#k事嗎？當然用我的力量可以讓你更強，但是在這之前我必須了解你付出了多少的努力。到現在為止來找我的年輕人很多，而能夠實際證明自己確實很強大的勇者卻沒有幾個...怎麼樣？就算很困難你也要試試看嗎？");
	    }
	} else {
	    cm.sendOk("玩家等級達到 70 級, 並且把等級 70 級前所有技能點分配完");
	    cm.safeDispose();
	}
    } else if (status == 1) {
	if(cm.getPlayer().getJob() % 10 != 0){
	    cm.dispose();
	}
	if (cm.getQuestStatus(100102) == 2) { //完成
	    cm.changeJob(cm.getPlayer().getJob() + 1);
	    if (cm.getJob() == 2110) {
		for (var i = 0; i < skills.length; i++) {
		    cm.teachSkill(skills[i], cm.getPlayer().getSkillLevel(skills[i]));
		}
	    }
	    cm.sendOk("你變得更加強大了!");
	    cm.dispose();
	} else if (cm.getQuestStatus(100101) == 2) { //完成
	    if(cm.haveItem(4031057)){
		cm.sendNext("好！只剩下第二階段的測試了。如果能夠順利通過這個測試，你將可以成為更勇猛的法師。在#b艾納斯島 - 冰原雪域#k的聖地裡，有顆神聖的石頭，傳說若有人供奉聖石一樣特殊的物品，就可以測試那人的智慧，你就去看看吧！");
	    } else {
		cm.sendOk("你沒有#b力量項鍊#k");
		cm.dispose();
	    }
	} else {
	    cm.sendAcceptDecline("但是我還可以讓你更強，你想要接受挑戰嗎?");
	}
    } else if (status == 2) {
	if (cm.getQuestStatus(100101) == 2) { //完成
	    if(cm.haveItem(4031057)){
		cm.startQuest(100102);
		cm.gainItem(4031057,-1);
		cm.sendNext("交出特殊物品給神聖的石頭後，正確並誠懇地回答聖石的問題。如果你回答的答案是正確的，那麼聖石就會給你#b智慧項鍊#k。把那個項鍊拿來給我，我就會認同你，並且把你提升為更強的法師。加油吧！");
		cm.dispose();
	    } else {
		cm.sendOk("你沒有#b力量項鍊#k");
		cm.dispose();
	    }
	} else {
	    cm.startQuest(100100);
	    cm.sendOk("現在，回去找#b武術教練#k. 他會幫助你.");
	    cm.dispose();
	}
    }
}

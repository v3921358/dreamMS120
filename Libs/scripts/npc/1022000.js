/* Dances with Balrog
	劍士 Job Advancement
	Victoria Road : 劍士s' Sanctuary (102000003)

	Custom Quest 100003, 100005
	劍士
*/

var status = 0;
var job;


function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 0 && status == 2) {
	cm.sendOk("Make up your mind and visit me again.");
	cm.dispose();
	return;
    }
    if (mode == 1)
	status++;
    else
	status--;
    if (status == 0) {
	if (cm.getJob() == 0) {
	    if (cm.getPlayerStat("LVL") >= 10 && cm.getJob() == 0) {
		cm.sendNext("你要轉職成為一位 #r劍士#k?");
	    } else {
		cm.sendOk("Train a bit more and I can show you the way of the #r劍士#k.");
		cm.dispose();
	    }
	} else {
	    if (cm.getPlayerStat("LVL") >= 30 && cm.getJob() == 100) { // WARROPR
		if (cm.getQuestStatus(100003) >= 1) {
		    cm.completeQuest(100005);
		    if (cm.getQuestStatus(100005) == 2) {
			status = 20;
			cm.sendNext("我看到你完成了測試，想要繼續轉職請點下一頁！");
		    } else {
			if (!cm.haveItem(4031008)) {
			    cm.gainItem(4031008, 1);
			}
			cm.warp(102020300);
			cm.sendOk("請去找 #b戰士轉職教官#k")
			cm.dispose();
		    }
		} else {
		    status = 10;
		    cm.sendNext("你已經可以轉職了，要轉職請點下一頁。");
		}
	    } else {
		// <>---------------三轉整串----------------------
		    if (cm.getQuestStatus(100100) == 1) {
			cm.sendOk("看起來你十分強壯，若是要三轉的話，請到#b異次元空間#k打倒我的分身，並且把#b黑符#k帶回來給我");
			cm.completeQuest(100100);//完成任務
			cm.startQuest(100101);//執行任務
			cm.dispose();
		    } else if (cm.getQuestStatus(100101) == 1) {
			if(cm.haveItem(4031059)){ //黑符
			    if(cm.canHold(4031057)){
				cm.sendOk("怎麼可能...竟然擊倒我的分身，拿回了#b黑符#k！好...通過這個考驗，可以充分證明你的能力。以你目前的力量，完成三轉是不會有問題的。按照約定給你#b力量項鍊#k，戴著這個項鍊，回到愛納斯島的#b教練#k那裡可以進行第二階段的測試。那麼祝你順利完成三轉...");
				cm.gainItem(4031059,-1);
				cm.gainItem(4031057,1);//力量項鍊
				cm.completeQuest(100101);//完成任務
			    } else {
				cm.sendOk("請整理物品欄!");
			    }
			} else {
			    cm.warp(105070001);
			    cm.sendOk("請盡快到#b異次元空間#k打倒我的分身，並且把#b黑符#k帶回來給我!");
			}
			cm.dispose();
		    } else if (cm.getQuestStatus(100101) == 2) {
			if(!cm.haveItem(4031057)){
			    cm.sendOk("把#b力量項鍊#k搞丟了嗎?沒關係，我再給你");
			    cm.gainItem(4031057,1);
			}else{
			    cm.warp(211000001,0);
			    cm.sendOk("趕緊去找長老吧！");
			}
			cm.dispose();
		// <>---------------三轉整串----------------------
	    	    } else {
			cm.sendOk("尚未取得轉職條件！");
			cm.dispose();
		    }
		// <>---------------------------------------------
	    }
	}
    } else if (status == 1) {
	cm.sendNextPrev("一旦轉職了就不能反悔，如果不想轉職請點上一頁。");
    } else if (status == 2) {
	cm.sendYesNo("你真的要成為一位 #r劍士#k？");
    } else if (status == 3) {
	if (cm.getJob() == 0) {
	    cm.resetStats(35, 4, 4, 4);
	    cm.expandInventory(1, 4);
	    cm.expandInventory(4, 4);
	    cm.changeJob(100); // 劍士
	}
	cm.gainItem(1402001, 1);
	cm.sendOk("轉職成功！請去開創天下吧。");
	cm.dispose();
    } else if (status == 11) {
	cm.sendNextPrev("你可以選擇你要轉職成為一位 #r狂戰士#k, #r見習騎士#k or #r槍騎兵#k.")
    } else if (status == 12) {
	cm.askAcceptDecline("但是我必須先測試你，你準備好了嗎？");
    } else if (status == 13) {
	cm.gainItem(4031008, 1);
	cm.startQuest(100003);
	cm.sendOk("請去找 #b戰士轉職教官#k 他會幫助你的");
	cm.dispose();
    } else if (status == 21) {
	cm.sendSimple("你想要成為什麼？#b\r\n#L0#狂戰士#l\r\n#L1#見習騎士#l\r\n#L2#槍騎兵#l#k");
    } else if (status == 22) {
	var jobName;
	if (selection == 0) {
	    jobName = "狂戰士";
	    job = 110; // 狂戰士
	} else if (selection == 1) {
	    jobName = "見習騎士";
	    job = 120; // 見習騎士
	} else {
	    jobName = "槍騎兵";
	    job = 130; // 槍騎兵
	}
	cm.sendYesNo("你真的要成為一位 #r" + jobName + "#k?");
    } else if (status == 23) {
	cm.changeJob(job);
	cm.gainItem(4031012, -1);
	cm.sendOk("轉職成功！請去開創天下吧。");
	cm.dispose();
    }
}	

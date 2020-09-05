/* Athena Pierce
	Bowman Job Advancement
	Victoria Road : Bowman Instructional School (100000201)

	Custom Quest 100000, 100002
	弓箭手
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
		cm.sendNext("你要轉職成為一位 #r弓箭手#k？");
	    } else {
		cm.sendOk("Train a bit more and I can show you the way of the #rBowman#k.")
		cm.dispose();
	    }
	} else {
	    if (cm.getPlayerStat("LVL") >= 30 && cm.getJob() == 300) { // BOWMAN
		if (cm.getQuestStatus(100000) >= 1) {
		    cm.completeQuest(100002);
		    if (cm.getQuestStatus(100002) == 2) {
			status = 20;
			cm.sendNext("我看到你完成了測試，想要繼續轉職請點下一頁！");
		    } else {
			if (!cm.haveItem(4031010)) {
			    cm.gainItem(4031010, 1);
			}
			cm.warp(106010000,9);
			cm.sendOk("請去找 #b弓箭手轉職教官#k")
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
			    cm.warp(107000402);
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
	cm.sendYesNo("你真的要成為一位 #r弓箭手#k？");
    } else if (status == 3) {
	if (cm.getJob() == 0) {
	    cm.resetStats(4, 25, 4, 4);
	    cm.expandInventory(1, 4);
	    cm.expandInventory(4, 4);
	    cm.changeJob(300); // BOWMAN
	}
	cm.gainItem(1452002, 1);
	cm.gainItem(2060000, 1000);
	cm.sendOk("轉職成功！請去開創天下吧。");
	cm.dispose();
    } else if (status == 11) {
	cm.sendNextPrev("你可以選擇你要轉職成為一位 #r獵人#k or #r弩弓手#k.")
    } else if (status == 12) {
	cm.askAcceptDecline("但是我必須先測試你，你準備好了嗎？");
    } else if (status == 13) {
	cm.startQuest(100000);
	cm.gainItem(4031010, 1);
	//cm.warp(106010000,0);//快速前往 轉職教官
	cm.sendOk("請去找 #b弓箭手轉職教官#k 他會幫助你的");
	cm.dispose();
    } else if (status == 21) {
	cm.sendSimple("你想要成為什麼？#b\r\n#L0#獵人#l\r\n#L1#弩弓手#l#k");
    } else if (status == 22) {
	var jobName;
	if (selection == 0) {
	    jobName = "獵人";
	    job = 310; // HUNTER
	} else {
	    jobName = "弩弓手";
	    job = 320; // CROSSBOWMAN
	}
	cm.sendYesNo("你真的要成為一位 #r" + jobName + "#k?");
    } else if (status == 23) {
	cm.changeJob(job);
	cm.gainItem(4031012, -1);
	cm.sendOk("轉職成功！請去開創天下吧。");
	cm.dispose();
    }
}	

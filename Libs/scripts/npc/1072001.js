/* Magician Job Instructor
	Magician 2nd Job Advancement
	Victoria Road : The Forest North of Ellinia (101020000)
*/

var status = -1;

function action(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	status--;
    }
    if (status == 0 && cm.getQuestStatus(100007) == 1) {
	status = 3;
    }
    if (status == 0) {
	if (cm.getQuestStatus(100007) == 2) {
	    cm.sendOk("You're truly a hero!");
	    cm.safeDispose();
	} else if (cm.getQuestStatus(100006) >= 1) {
	    cm.completeQuest(100006);
	    if (cm.getQuestStatus(100006) == 2) {
		cm.sendNext("噢，你是 #b漢斯#k 介紹來的嗎？");
	    }
	} else {
	    cm.sendOk("I can show you the way once your ready for it.");
	    cm.safeDispose();
	}
    } else if (status == 1) {
	cm.sendNextPrev("所以你要證明你的實力嗎？ 很好...")
    } else if (status == 2) {
	cm.askAcceptDecline("我可以給你一次機會，請你把握。");
    } else if (status == 3) {
	cm.startQuest(100007);
	cm.sendOk("請給我 #b30 #t4031013##k. 祝你好運");
    } else if (status == 4) {
	cm.gainItem(4031009, -1);
	cm.warp(108000200, 0);
	cm.dispose();
    }
}	

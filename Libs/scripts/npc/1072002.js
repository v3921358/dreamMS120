/* Bowman Job Instructor
	Hunter Job Advancement
	Warning Street : The Road to the Dungeon (106010000)
*/

var status = 0;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 1)
	status++;
    else
	status--;
    if (status == 0 && cm.getQuestStatus(100001) == 1) {
	status = 3;
    }
    if (status == 0) {
	if (cm.getQuestStatus(100001) == 2) {
	    cm.sendOk("You're truly a hero!");
	    cm.dispose();
	} else if (cm.getQuestStatus(100000) >= 1) {
	    cm.completeQuest(100000);
	    if (cm.getQuestStatus(100000) == 2) {
		cm.sendNext("噢，你是 #b赫麗娜#k 介紹來的嗎？");
	    }
	} else {
	    cm.sendOk("I can show you the way once your ready for it.");
	    cm.dispose();
	}
    } else if (status == 1) {
	cm.sendNextPrev("所以你要證明你的實力嗎？ 很好...")
    } else if (status == 2) {
	cm.askAcceptDecline("我可以給你一次機會，請你把握。");
    } else if (status == 3) {
	cm.startQuest(100001);
	cm.sendOk("請給我 #b30 #t4031013##k. 祝你好運")
    } else if (status == 4) {
	cm.gainItem(4031010, -1);
	cm.warp(108000100);
	cm.dispose();
    }
}

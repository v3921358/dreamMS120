var status = -1;
var skills = Array(21001003, 21000000, 21100002, 21100004, 21100005, 21110002);
//polearm booster, combo ability, polearm mastery, final charge, combo smash, combo drain, full swing
function start(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	if (status == 0) {
	    qm.sendNext("#b(You need to think about this for a second...)#k");
	    qm.dispose();
	    return;
	} else if (status == 2) {
	    qm.MovieClipIntroUI(true);
	    qm.warp(914090100, 0);
	    qm.dispose();
	    return;
	}
	status--;
    }
    if (status == 0) {
	qm.sendYesNo("#b(讓我確認自己是不是使用#p1201001#的英雄？使勁抓住#p1201001#試試，肯定會有什麼反映的。)#k");
    } else if (status == 1) {
		if (qm.getJob() == 2000) {
			qm.changeJob(2100);
			qm.forceCompleteQuest();
			qm.resetStats(35, 4, 4, 4);
			qm.expandInventory(1, 4);
			qm.expandInventory(2, 4);
			qm.expandInventory(3, 4);
			qm.expandInventory(4, 4);
			qm.gainItem(1142129, 1);
			qm.forceCompleteQuest(29924); //medal
			qm.teachSkill(20009000, 0, -1);
			qm.teachSkill(20009000, 1, 0);
			for (var i = 0; i < skills.length; i++) {
				qm.teachSkill(skills[i], 0);
			}
			qm.sendNextS("#b(似乎想起來了什麼……)#k", 3);
		}
    } else if (status == 2) {
	//qm.sendYesNoS("Will you skip the video clip? Even if you skip the scene, game-play will not be affected.", 1);
		qm.warp(914090100);
		qm.dispose();
    }
}

function end(mode, type, selection) {
    qm.dispose();
}

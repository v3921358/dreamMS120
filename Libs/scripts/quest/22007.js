
var status = -1;

function end(mode, type, selection) {
    status++;
	if (mode != 1) {
	    if(type == 1 && mode == 0)
		    status -= 2;
		else{
			qm.sendNext("嗯？奇怪。孵化器沒有設置好。重新嘗試一下吧。");
		    qm.dispose();
			return;
		}
	}
	if (status == 0)
		qm.sendNext("哦，雞蛋 拿來了嗎？快把蛋給我吧。我來幫你把它孵化。");
	if (status == 1)
		qm.sendYesNo("來，拿著。不知道這到底可以用來幹什麼…… \r\n\r\n#fUI/UIWindow.img/QuestIcon/8/0# 360 exp");
	if (status == 2){
		qm.forceCompleteQuest();
		qm.gainExp(360);
		if (qm.haveItem(4032451)) {
			qm.gainItem(4032451, -1);
		}
		qm.evanTutorial("UI/tutorial/evan/9/0" , 1);
		qm.dispose();
		}
	}
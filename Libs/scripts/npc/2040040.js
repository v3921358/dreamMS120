/*
	Green Balloon - LudiPQ 5th stage NPC
**/

var exp = 3770;

function action(mode, type, selection) {
    var eim = cm.getEventInstance();
    
    var stage5status = eim != null ?  eim.getProperty("stage5status") : null;

    if (stage5status == null ) {
	if (cm.isLeader()) { // Leader
	    var stage5leader = eim.getProperty("stage5leader");
	    if (stage5leader == "done") {

		if (cm.haveItem(4001022,24)) { // Clear stage
		    cm.sendNext("恭喜！你已經通過了第五階段。快點現在，到第6階段。");
		    cm.removeAll(4001022);
		    clear(5,eim,cm);
		    cm.givePartyExp(exp, eim.getPlayers());
		} else { // Not done yet
		    cm.sendNext("你確定你有收集了 #r24張 #t4001022##k？？");
		}
		cm.safeDispose();
	    } else {
		cm.sendOk("歡迎來到第五階段。#b遺棄之塔PQ#k 請收集#r#t4001022##k 來找我即可完成任務。");
		eim.setProperty("stage5leader","done");
		cm.safeDispose();
	    }
	} else { // Members
	    cm.sendNext("歡迎來到第五階段。#b遺棄之塔PQ#k 請收集#r#t4001022##k 給你的隊長，然後叫隊長來找我即可完成任務。");
	    cm.safeDispose();
	}
    } else {
	cm.sendNext("恭喜！你已經通過了第五階段。快點現在，到第6階段。");
	cm.safeDispose();
    }
}

function clear(stage, eim, cm) {
    eim.setProperty("stage" + stage.toString() + "status","clear");

    cm.showEffect(true, "quest/party/clear");
    cm.playSound(true, "Party1/Clear");
    cm.environmentChange(true, "gate");
}

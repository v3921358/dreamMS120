
/*
	Yellow Balloon - LudiPQ 3rd stage NPC
*/

var status = -1;
var exp = 2940;
			
function action(mode, type, selection) {
    var eim = cm.getEventInstance();
    var stage3status = eim.getProperty("stage3status");

    if (stage3status == null) {
	if (cm.isLeader()) { // Leader
	    var stage3leader = eim.getProperty("stage3leader");
	    if (stage3leader == "done") {

		if (cm.haveItem(4001022, 32)) { // Clear stage
		    cm.sendNext("恭喜！你已經通過了第三階段。快點現在，到第4階段。");
		    cm.removeAll(4001022);
		    clear(3,eim,cm);
		    cm.givePartyExp(exp, eim.getPlayers());
		} else { // Not done yet
		    cm.sendNext("你確定你有收集了 #r32張 #t4001022##k？？");
		}
	    } else {
		cm.sendOk("歡迎來到第三階段。#b遺棄之塔PQ#k 請收集#r#t4001022##k 來找我即可完成任務。");
		eim.setProperty("stage3leader","done");
	    }
	} else { // Members
	    cm.sendNext("歡迎來到第三階段。#b遺棄之塔PQ#k 請收集#r#t4001022##k 給你的隊長，然後叫隊長來找我即可完成任務。");
	}
    } else {
	cm.sendNext("恭喜！你已經通過了第三階段。快點現在，到第4階段。");
    }
    cm.safeDispose();
}

function clear(stage, eim, cm) {
    eim.setProperty("stage" + stage.toString() + "status","clear");

    cm.showEffect(true, "quest/party/clear");
    cm.playSound(true, "Party1/Clear");
    cm.environmentChange(true, "gate");
}
/**
	Orange Balloon - LudiPQ 2nd stage NPC
**/

var status;
var exp = 2520;
			
function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    var eim = cm.getEventInstance();
    var stage2status = eim.getProperty("stage2status");
    
    if (stage2status == null) {
	if (cm.isLeader()) { // Leader
	    var stage2leader = eim.getProperty("stage2leader");
	    if (stage2leader == "done") {

		if (cm.haveItem(4001022, 15)) { // Clear stage
		    cm.sendNext("恭喜！你已經通過了第二階段。快點現在，到第三階段。");
		    cm.removeAll(4001022);
		    clear(2, eim, cm);
		    cm.givePartyExp(2520);
		    cm.dispose();
		} else { // Not done yet
		    cm.sendNext("你確定你有收集了 #r15張 #t4001022##k？？");
		}
		cm.dispose();
	    } else {
		cm.sendOk("歡迎來到第二階段。#b遺棄之塔PQ#k 請收集#r#t4001022##k 來找我即可完成任務。");
		eim.setProperty("stage2leader","done");
		cm.dispose();
	    }
	} else { // Members
	    cm.sendNext("歡迎來到第二階段。#b遺棄之塔PQ#k 請收集#r#t4001022##k 給你的隊長，然後叫隊長來找我即可完成任務。");
	    cm.dispose();
	}
    } else {
	cm.sendNext("恭喜！你已經通過了第二階段。快點現在，到第三階段。");
	cm.dispose();
    }
}

function clear(stage, eim, cm) {
    eim.setProperty("stage" + stage.toString() + "status","clear");
    
    cm.showEffect(true, "quest/party/clear");
    cm.playSound(true, "Party1/Clear");
    cm.environmentChange(true, "gate");
}
var status = -1;
var minLevel = 51; // 35
var maxLevel = 70; // 65

var minPartySize = 6;
var maxPartySize = 6;

function action(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	if (status == 0) {
	    cm.dispose();
	    return;
	}
	status--;
    }
	if (cm.getMapId() == 920010000) { //inside orbis pq
		cm.sendOk("我們必須拯救他 需要20個雲的碎片");
		cm.dispose();
		return;
	}
    if (status == 0) {
	for (var i = 4001044; i < 4001064; i++) {
		cm.removeAll(i); //holy
	}
	if (cm.getParty() == null) { // No Party
	    cm.sendSimple("你貌似沒有達到要求...:\r\n\r\n#r要求: " + minPartySize + " 玩家成員, 每個人的等級必須在 " + minLevel + " 到 等級 " + maxLevel + ".#b\r\n#L0#我要用40個女神的羽翼兌換女神手鐲#l");
	} else if (!cm.isLeader()) { // Not Party Leader
	    cm.sendSimple("如果你想做任務，請 #b隊長#k 跟我談.#b\r\n#L0#我要用40個女神的羽翼兌換女神手鐲#l");
	} else {
	    // Check if all party members are within PQ levels
	    var party = cm.getParty().getMembers();
	    var mapId = cm.getMapId();
	    var next = true;
	    var levelValid = 0;
	    var inMap = 0;
	    var it = party.iterator();

	    while (it.hasNext()) {
		var cPlayer = it.next();
		if ((cPlayer.getLevel() >= minLevel) && (cPlayer.getLevel() <= maxLevel)) {
		    levelValid += 1;
		} else {
		    next = false;
		}
		if (cPlayer.getMapid() == mapId) {
		    inMap += (cPlayer.getJobId() == 900 ? 6 : 1);
		}
	    }
	    if (party.size() > maxPartySize || inMap < minPartySize) {
		next = false;
	    }
	    if (next) {
		var em = cm.getEventManager("OrbisPQ");
		if (em == null) {
		    cm.sendSimple("找不到腳本請聯絡GM#b\r\n#L0#我要用40個女神的羽翼兌換女神手鐲#l");
		} else {
		    var prop = em.getProperty("state");
		    if (prop.equals("0") || prop == null) {
			em.startInstance(cm.getParty(), cm.getMap());
			cm.dispose();
			return;
		    } else {
			cm.sendSimple("其他隊伍已經在裡面做 #r組隊任務了#k 請嘗試換頻道或者等其他隊伍完成。#b\r\n#L0#我要用40個女神的羽翼兌換女神手鐲#l");
		    }
		}
	    } else {
		cm.sendSimple("你的隊伍貌似沒有達到要求...:\r\n\r\n#r要求: " + minPartySize + " 玩家成員, 每個人的等級必須在 " + minLevel + " 到 等級 " + maxLevel + ".#b\r\n#L0#我要用40個女神的羽翼兌換女神手鐲#l");
	    }
	}
    } else { //broken glass
	if (!cm.canHold(1082232,1)) {
	    cm.sendOk("做好了。");
	} else if (cm.haveItem(4001158,40)) {
	    cm.gainItem(1082232, 1, true);
	    cm.gainItem(4001158, -40, true); 
	} else {
	    cm.sendOk("你沒有40個 #t4001158#.");
	}
	cm.dispose();

    }
}
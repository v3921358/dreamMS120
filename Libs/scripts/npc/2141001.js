/*
	NPC Name: 		The Forgotten Temple Manager
	Map(s): 		Deep in the Shrine - Forgotten Twilight
	Description: 		Pink Bean battle starter
*/
var status = -1;

function start() {
		if (cm.getPlayer().getLevel() < 120) {
			cm.sendOk("挑戰皮卡丘等級須達到120.");
			cm.dispose();
			return;
		
		}
    var em = cm.getEventManager("PinkBeanBattle");

    if (em == null) {
	cm.sendOk("事件沒有開始，請聯繫GM.");
	cm.dispose();
	return;
    }
    var eim_status = em.getProperty("state");
	if (eim_status == null || eim_status.equals("0")) {
    var squadAvailability = cm.getSquadAvailability("PinkBean");
    if (squadAvailability == -1) {
	status = 0;
	cm.sendYesNo("你有興趣成為遠征隊的隊長嗎?");

    } else if (squadAvailability == 1) {
	// -1 = Cancelled, 0 = not, 1 = true
	var type = cm.isSquadLeader("PinkBean");
	if (type == -1) {
	    cm.sendOk("遠征隊已經結束，請重新註冊.");
	    cm.dispose();
	} else if (type == 0) {
	    var memberType = cm.isSquadMember("PinkBean");
	    if (memberType == 2) {
		cm.sendOk("你被禁止參加遠征隊.");
		cm.dispose();
	    } else if (memberType == 1) {
		status = 5;
		cm.sendSimple("你想幹什麼? \r\n#b#L0#加入遠征隊#l \r\n#b#L1#讓隊伍去挑戰皮卡丘#l \r\n#b#L2#查看遠征隊名單#l");
	    } else if (memberType == -1) {
		cm.sendOk("The squad has ended, please re-register.");
		cm.dispose();
	    } else {
		status = 5;
		cm.sendSimple("你想幹什麼? \r\n#b#L0#加入遠征隊#l \r\n#b#L1#讓隊伍去挑戰皮卡丘#l \r\n#b#L2#查看遠征隊名單#l");
	    }
	} else { // Is leader
	    status = 10;
	    cm.sendSimple("你想做什麼，探險隊長? \r\n#b#L0#查看遠征隊名單#l \r\n#b#L1#從遠征隊踢出#l \r\n#b#L2#從遠征隊中刪除成員#l \r\n#r#L3#進入挑戰皮卡丘#l");
	// TODO viewing!
	}
	    } else {
			var eim = cm.getDisconnected("PinkBeanBattle");
			if (eim == null) {
				cm.sendOk("對皮卡丘的戰鬥已經開始了.");
				cm.safeDispose();
			} else {
				cm.sendYesNo("啊，你已經回來了。你想再次加入你的陣容嗎?");
				status = 2;
			}
	    }
	} else {
			var eim = cm.getDisconnected("PinkBeanBattle");
			if (eim == null) {
				cm.sendOk("對皮卡丘的戰鬥已經開始了.");
				cm.safeDispose();
			} else {
				cm.sendYesNo("啊，你已經回來了。你想再次加入你的陣容嗎?");
				status = 2;
			}
	}
}

function action(mode, type, selection) {
    switch (status) {
	case 0:
	    if (mode == 1) {
			if (cm.registerSquad("PinkBean", 5, " 被任命為隊的隊長。如果您想加入，請在此期間註冊參加遠征隊.")) {
				cm.sendOk(" 你被任命為遠征隊領袖。接下來的5分鐘，你可以添加遠征隊的成員.");
			} else {
				cm.sendOk("加入你的名單發生錯誤.");
			}
	    }
	    cm.dispose();
	    break;
	case 2:
		if (!cm.reAdd("PinkBeanBattle", "PinkBean")) {
			cm.sendOk("錯誤...請重試.");
		}
		cm.safeDispose();
		break;
	case 5:
	    if (selection == 0) { // join
		var ba = cm.addMember("PinkBean", true);
		if (ba == 2) {
		    cm.sendOk("遠征隊目前已滿，請稍後再試.");
		} else if (ba == 1) {
		    cm.sendOk("你已經成功加入了隊伍");
		} else {
		    cm.sendOk("你已經是隊中的一員了.");
		}
	    } else if (selection == 1) {// withdraw
		var baa = cm.addMember("PinkBean", false);
		if (baa == 1) {
		    cm.sendOk("你已經成功退出了遠征隊");
		} else {
		    cm.sendOk("你不是遠征隊的成員.");
		}
	    } else if (selection == 2) {
		if (!cm.getSquadList("PinkBean", 0)) {
		    cm.sendOk("由於一個未知的錯誤，遠征隊的請求被拒絕.");
		}
	    }
	    cm.dispose();
	    break;
	case 10:
	    if (mode == 1) {
		if (selection == 0) {
		    if (!cm.getSquadList("PinkBean", 0)) {
			cm.sendOk("由於一個未知的錯誤，遠征隊的請求被拒絕.");
		    }
		    cm.dispose();
		} else if (selection == 1) {
		    status = 11;
		    if (!cm.getSquadList("PinkBean", 1)) {
			cm.sendOk("由於一個未知的錯誤，遠征隊的請求被拒絕.");
			cm.dispose();
		    }
		} else if (selection == 2) {
		    status = 12;
		    if (!cm.getSquadList("PinkBean", 2)) {
			cm.sendOk("由於一個未知的錯誤，遠征隊的請求被拒絕.");
			cm.dispose();
		    }
		} else if (selection == 3) { // get insode
		    if (cm.getSquad("PinkBean") != null) {
			var dd = cm.getEventManager("PinkBeanBattle");
			dd.startInstance(cm.getSquad("PinkBean"), cm.getMap());
		    } else {
			cm.sendOk("由於一個未知的錯誤，遠征隊的請求被拒絕.");
		    }
		    cm.dispose();
		}
	    } else {
		cm.dispose();
	    }
	    break;
	case 11:
	    cm.banMember("PinkBean", selection);
	    cm.dispose();
	    break;
	case 12:
	    if (selection != -1) {
		cm.acceptMember("PinkBean", selection);
	    }
	    cm.dispose();
	    break;
    }
}
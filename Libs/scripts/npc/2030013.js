/*
	NPC Name: 		Adobis
	Map(s): 		El Nath : Entrance to Zakum Altar
	Description: 		Zakum battle starter
*/
var status = 0;

function action(mode, type, selection) {
	if (cm.getPlayer().getMapId() == 211042200) {
		if (selection < 100) {
			cm.sendSimple("#r#L100#Zakum#l\r\n#L101#Chaos Zakum#l");
		} else {
			if (selection == 100) {
				cm.warp(211042300,0);
			} else if (selection == 101) {
				cm.warp(211042301,0);
			}
			cm.dispose();
		}
		return;
	} else if (cm.getPlayer().getMapId() == 211042401) {
    switch (status) {
	case 0:
		if (cm.getPlayer().getLevel() < 100) {
			cm.sendOk("等級需要100等才可以挑戰渾沌炎魔.");
			cm.dispose();
			return;
		}
		if (cm.getPlayer().getClient().getChannel() != 7) {
			cm.sendOk("渾沌殘暴炎魔只允許在頻道 7 .");
			cm.dispose();
			return;
		}
	    var em = cm.getEventManager("ChaosZakum");

	    if (em == null) {
		cm.sendOk("活動未開始，請與GM聯繫。");
		cm.safeDispose();
		return;
	    }
	var prop = em.getProperty("state");
	if (prop == null || prop.equals("0")) {

	    var squadAvailability = cm.getSquadAvailability("ChaosZak");
	    if (squadAvailability == -1) {
		status = 1;
		cm.sendYesNo("你有興趣成為遠征隊的隊長嗎？");

	    } else if (squadAvailability == 1) {
		// -1 = Cancelled, 0 = not, 1 = true
		var type = cm.isSquadLeader("ChaosZak");
		if (type == -1) {
		    cm.sendOk("隊伍已經結束，請重新註冊.");
		    cm.safeDispose();
		} else if (type == 0) {
		    var memberType = cm.isSquadMember("ChaosZak");
		    if (memberType == 2) {
			cm.sendOk("你被禁止了.");
			cm.safeDispose();
		    } else if (memberType == 1) {
			status = 5;
			cm.sendSimple("你想做什麼? \r\n#b#L0#確認遠征隊的成員#l \r\n#b#L1#加入遠征隊#l \r\n#b#L2#退出遠征隊#l");
		    } else if (memberType == -1) {
		    cm.sendOk("隊伍已經結束，請重新註冊.");
			cm.safeDispose();
		    } else {
			status = 5;
			cm.sendSimple("你想做什麼? \r\n#b#L0#確認遠征隊的成員#l \r\n#b#L1#加入遠征隊#l \r\n#b#L2#退出遠征隊#l");
		    }
		} else { // Is leader
		    status = 10;
		    cm.sendSimple("你想做什麼? \r\n#b#L0#確認遠征隊的成員#l \r\n#b#L1#刪除遠征隊的成員#l \r\n#b#L2#編輯受限列表#l \r\n#r#L3#進入地圖#l");
		// TODO viewing!
		}
	    } else {
			var eim = cm.getDisconnected("ChaosZakum");
			if (eim == null) {
				cm.sendOk("隊伍的戰鬥已經開始了。.");
				cm.safeDispose();
			} else {
				cm.sendYesNo("啊，你回來了。 你想再次加入你的隊伍的戰鬥?");
				status = 2;
			}
	    }
	} else {
			var eim = cm.getDisconnected("ChaosZakum");
			if (eim == null) {
				cm.sendOk("隊伍的戰鬥已經開始了。.");
				cm.safeDispose();
			} else {
				cm.sendYesNo("啊，你回來了。 你想再次加入你的隊伍的戰鬥?");
				status = 2;
			}
	}
	    break;
	case 1:
	    	if (mode == 1) {
			if (cm.registerSquad("ChaosZak", 5, " 已被命名為隊長（混沌）。 如果你想加入，請在時間段內註冊遠征隊.")) {
				cm.sendOk("你被命名為遠征隊的隊長。 在接下來的5分鐘內，您可以添加遠征隊的成員.");
			} else {
				cm.sendOk("添加你的隊伍時出錯.");
			}
	    	} else {
			cm.sendOk("如果你想成為遠征隊的隊長，請跟我說話.")
	    	}
	    cm.safeDispose();
	    break;
	case 2:
		if (!cm.reAdd("ChaosZakum", "ChaosZak")) {
			cm.sendOk("錯誤... 請在試一次.");
		}
		cm.safeDispose();
		break;
	case 5:
	    if (selection == 0) {
		if (!cm.getSquadList("ChaosZak", 0)) {
		    cm.sendOk("由於未知錯誤，隊伍的請求已被拒絕.");
		    cm.safeDispose();
		} else {
		    cm.dispose();
		}
	    } else if (selection == 1) { // join
		var ba = cm.addMember("ChaosZak", true);
		if (ba == 2) {
		    cm.sendOk("隊伍目前已滿，請稍後再試.");
		    cm.safeDispose();
		} else if (ba == 1) {
		    cm.sendOk("您已成功加入遠征隊");
		    cm.safeDispose();
		} else {
		    cm.sendOk("你已經是遠征隊的一部分了。");
		    cm.safeDispose();
		}
	    } else {// withdraw
		var baa = cm.addMember("ChaosZak", false);
		if (baa == 1) {
		    cm.sendOk("你已經成功退出隊伍了");
		    cm.safeDispose();
		} else {
		    cm.sendOk("你不是隊伍的一部分.");
		    cm.safeDispose();
		}
	    }
	    break;
	case 10:
	    if (selection == 0) {
		if (!cm.getSquadList("ChaosZak", 0)) {
		    cm.sendOk("由於未知錯誤，隊伍的請求已被拒絕.");
		}
		cm.safeDispose();
	    } else if (selection == 1) {
		status = 11;
		if (!cm.getSquadList("ChaosZak", 1)) {
		    cm.sendOk("由於未知錯誤，隊伍的請求已被拒絕.");
		}
		cm.safeDispose();
	    } else if (selection == 2) {
		status = 12;
		if (!cm.getSquadList("ChaosZak", 2)) {
		    cm.sendOk("由於未知錯誤，隊伍的請求已被拒絕.");
		}
		cm.safeDispose();
	    } else if (selection == 3) { // get insode
		if (cm.getSquad("ChaosZak") != null) {
		    var dd = cm.getEventManager("ChaosZakum");
		    dd.startInstance(cm.getSquad("ChaosZak"), cm.getMap());
		    cm.dispose();
		} else {
		    cm.sendOk("由於未知錯誤，隊伍的請求已被拒絕.");
		    cm.safeDispose();
		}
	    }
	    break;
	case 11:
	    cm.banMember("ChaosZak", selection);
	    cm.dispose();
	    break;
	case 12:
	    if (selection != -1) {
		cm.acceptMember("ChaosZak", selection);
	    }
	    cm.dispose();
	    break;
    }
	} else {
    switch (status) {
	case 0:
		if (cm.getPlayer().getLevel() < 50) {
			cm.sendOk("等級需要達到 50 等，才可以挑戰殘暴炎魔.");
			cm.dispose();
			return;
		}
		if (cm.getPlayer().getClient().getChannel() != 3 && cm.getPlayer().getClient().getChannel() != 2) {
			cm.sendOk("殘暴炎魔只允許在頻道 2 和 3.");
			cm.dispose();
			return;
		}
	    var em = cm.getEventManager("ZakumBattle");

	    if (em == null) {
		cm.sendOk("活動未開始，請與GM聯繫。");
		cm.safeDispose();
		return;
	    }
	var prop = em.getProperty("state");
	if (prop == null || prop.equals("0")) {

	    var squadAvailability = cm.getSquadAvailability("ZAK");
	    if (squadAvailability == -1) {
		status = 1;
		cm.sendYesNo("你有興趣成為遠征隊的隊長嗎？");

	    } else if (squadAvailability == 1) {
		// -1 = Cancelled, 0 = not, 1 = true
		var type = cm.isSquadLeader("ZAK");
		if (type == -1) {
		    cm.sendOk("隊伍已經結束，請重新註冊.");
		    cm.safeDispose();
		} else if (type == 0) {
		    var memberType = cm.isSquadMember("ZAK");
		    if (memberType == 2) {
			cm.sendOk("你被禁止了.");
			cm.safeDispose();
		    } else if (memberType == 1) {
			status = 5;
			cm.sendSimple("你想做什麼? \r\n#b#L0#確認遠征隊的成員#l \r\n#b#L1#加入遠征隊#l \r\n#b#L2#退出遠征隊#l");
		    } else if (memberType == -1) {
		    cm.sendOk("隊伍已經結束，請重新註冊.");
			cm.safeDispose();
		    } else {
			status = 5;
			cm.sendSimple("你想做什麼? \r\n#b#L0#確認遠征隊的成員#l \r\n#b#L1#加入遠征隊#l \r\n#b#L2#退出遠征隊#l");
		    }
		} else { // Is leader
		    status = 10;
		    cm.sendSimple("你想做什麼? \r\n#b#L0#確認遠征隊的成員#l \r\n#b#L1#刪除遠征隊的成員#l \r\n#b#L2#編輯受限列表#l \r\n#r#L3#進入地圖#l");
		// TODO viewing!
		}
	    } else {
			var eim = cm.getDisconnected("ZakumBattle");
			if (eim == null) {
				cm.sendOk("隊伍的戰鬥已經開始了。.");
				cm.safeDispose();
			} else {
				cm.sendYesNo("啊，你回來了。 你想再次加入你的隊伍的戰鬥?");
				status = 2;
			}
	    }
	} else {
			var eim = cm.getDisconnected("ZakumBattle");
			if (eim == null) {
				cm.sendOk("隊伍的戰鬥已經開始了。.");
				cm.safeDispose();
			} else {
				cm.sendYesNo("啊，你回來了。 你想再次加入你的隊伍的戰鬥?");
				status = 2;
			}
	}
	    break;
	case 1:
	    	if (mode == 1) {
			if (cm.registerSquad("ZAK", 5, " 已被命名為隊長（混沌）。 如果你想加入，請在時間段內註冊遠征隊.")) {
				cm.sendOk("你被命名為遠征隊的隊長。 在接下來的5分鐘內，您可以添加遠征隊的成員.");
			} else {
				cm.sendOk("添加你的隊伍時出錯.");
			}
	    	} else {
			cm.sendOk("如果你想成為遠征隊的隊長，請跟我說話.")
	    	}
	    cm.safeDispose();
	    break;
	case 2:
		if (!cm.reAdd("ZakumBattle", "ZAK")) {
			cm.sendOk("錯誤... 請在試一次.");
		}
		cm.safeDispose();
		break;
	case 5:
	    if (selection == 0) {
		if (!cm.getSquadList("ZAK", 0)) {
		    cm.sendOk("由於未知錯誤，隊伍的請求已被拒絕.");
		    cm.safeDispose();
		} else {
		    cm.dispose();
		}
	    } else if (selection == 1) { // join
		var ba = cm.addMember("ZAK", true);
		if (ba == 2) {
		    cm.sendOk("隊伍目前已滿，請稍後再試.");
		    cm.safeDispose();
		} else if (ba == 1) {
		    cm.sendOk("您已成功加入遠征隊");
		    cm.safeDispose();
		} else {
		    cm.sendOk("你已經是遠征隊的一部分了。");
		    cm.safeDispose();
		}
	    } else {// withdraw
		var baa = cm.addMember("ZAK", false);
		if (baa == 1) {
		    cm.sendOk("你已經成功退出隊伍了");
		    cm.safeDispose();
		} else {
		    cm.sendOk("你不是隊伍的一部分.");
		    cm.safeDispose();
		}
	    }
	    break;
	case 10:
	    if (selection == 0) {
		if (!cm.getSquadList("ZAK", 0)) {
		    cm.sendOk("由於未知錯誤，隊伍的請求已被拒絕.");
		}
		cm.safeDispose();
	    } else if (selection == 1) {
		status = 11;
		if (!cm.getSquadList("ZAK", 1)) {
		    cm.sendOk("由於未知錯誤，隊伍的請求已被拒絕.");
		}
		cm.safeDispose();
	    } else if (selection == 2) {
		status = 12;
		if (!cm.getSquadList("ZAK", 2)) {
		    cm.sendOk("由於未知錯誤，隊伍的請求已被拒絕.");
		}
		cm.safeDispose();
	    } else if (selection == 3) { // get insode
		if (cm.getSquad("ZAK") != null) {
		    var dd = cm.getEventManager("ZakumBattle");
		    dd.startInstance(cm.getSquad("ZAK"), cm.getMap(),160108);
		    cm.dispose();
		} else {
		    cm.sendOk("由於未知錯誤，隊伍的請求已被拒絕.");
		    cm.safeDispose();
		}
	    }
	    break;
	case 11:
	    cm.banMember("ZAK", selection);
	    cm.dispose();
	    break;
	case 12:
	    if (selection != -1) {
		cm.acceptMember("ZAK", selection);
	    }
	    cm.dispose();
	    break;
    }
	}
}
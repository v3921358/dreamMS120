/*
NPC Name: 		Mark of the Squad
Map(s): 		Entrance to Horned Tail's Cave
Description: 		Horntail Battle starter
 */
var status = -1;

function start() {
	if (cm.getPlayer().getLevel() < 80) {
		cm.sendOk("要等級達到 80 以上才可進行挑戰.");
		cm.dispose();
		return;
	}
	if (cm.getPlayer().getClient().getChannel() != 4) {
	//	cm.sendOk("Horntail may only be attempted on channel 4");
	//	cm.dispose();
	//	return;
	}
	var em = cm.getEventManager("HorntailBattle");

	if (em == null) {
		cm.sendOk("活動未開始，請與GM聯繫。");
		cm.dispose();
		return;
	}
	var prop = em.getProperty("state");

	if (prop == null || prop.equals("0")) {
		var squadAvailability = cm.getSquadAvailability("Horntail");
		if (squadAvailability == -1) {
			status = 0;
		cm.sendYesNo("你有興趣成為遠征隊的隊長嗎？");

		} else if (squadAvailability == 1) {
			// -1 = Cancelled, 0 = not, 1 = true
			var type = cm.isSquadLeader("Horntail");
			if (type == -1) {
		    cm.sendOk("隊伍已經結束，請重新註冊.");
				cm.dispose();
			} else if (type == 0) {
				var memberType = cm.isSquadMember("Horntail");
				if (memberType == 2) {
			cm.sendOk("你被禁止了.");
					cm.dispose();
				} else if (memberType == 1) {
					status = 5;
			cm.sendSimple("你想做什麼? \r\n#b#L0#確認遠征隊的成員#l \r\n#b#L1#加入遠征隊#l \r\n#b#L2#退出遠征隊#l");
				} else if (memberType == -1) {
		    cm.sendOk("隊伍已經結束，請重新註冊.");
					cm.dispose();
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
			var props = em.getProperty("leader");
			if (props != null && props.equals("true")) {
				var eim = cm.getDisconnected("HorntailBattle");
				if (eim == null) {
				cm.sendOk("隊伍的戰鬥已經開始了。.");
					cm.safeDispose();
				} else {
				cm.sendYesNo("啊，你回來了。 你想再次加入你的隊伍的戰鬥?");
					status = 1;
				}
			} else {
				cm.sendOk("你的隊長已經離開了戰鬥，所以你不能回去來.");
				cm.safeDispose();
			}
		}
	} else {
		var props = em.getProperty("leader");
		if (props != null && props.equals("true")) {
			var eim = cm.getDisconnected("HorntailBattle");
			if (eim == null) {
				cm.sendOk("隊伍的戰鬥已經開始了。.");
				cm.safeDispose();
			} else {
				cm.sendYesNo("啊，你回來了。 你想再次加入你的隊伍的戰鬥?");
				status = 1;
			}
		} else {
				cm.sendOk("你的隊長已經離開了戰鬥，所以你不能回去來.");
			cm.safeDispose();
		}
	}
}

function action(mode, type, selection) {
	switch (status) {
	case 0:
		if (mode == 1) {
			if (cm.registerSquad("Horntail", 5, " 已被命名為隊長。 如果你想加入，請在時間段內註冊遠征隊.")) {
				cm.sendOk("你被命名為遠征隊的隊長。 在接下來的5分鐘內，您可以添加遠征隊的成員.");
			} else {
				cm.sendOk("添加你的隊伍時出錯.");
			}
		}
		cm.dispose();
		break;
	case 1:
		if (!cm.reAdd("HorntailBattle", "Horntail")) {
			cm.sendOk("錯誤... 請在試一次.");
		}
		cm.safeDispose();
		break;
	case 5:
		if (selection == 0) {
			if (!cm.getSquadList("Horntail", 0)) {
		    cm.sendOk("由於未知錯誤，隊伍的請求已被拒絕.");
			}
		} else if (selection == 1) { // join
			var ba = cm.addMember("Horntail", true);
			if (ba == 2) {
		    cm.sendOk("隊伍目前已滿，請稍後再試.");
			} else if (ba == 1) {
		    cm.sendOk("您已成功加入遠征隊");
			} else {
		    cm.sendOk("你已經是遠征隊的一部分了。");
			}
		} else { // withdraw
			var baa = cm.addMember("Horntail", false);
			if (baa == 1) {
		    cm.sendOk("你已經成功退出隊伍了");
			} else {
		    cm.sendOk("你不是隊伍的一部分.");
			}
		}
		cm.dispose();
		break;
	case 10:
		if (mode == 1) {
			if (selection == 0) {
				if (!cm.getSquadList("Horntail", 0)) {
		    cm.sendOk("由於未知錯誤，隊伍的請求已被拒絕.");
				}
				cm.dispose();
			} else if (selection == 1) {
				status = 11;
				if (!cm.getSquadList("Horntail", 1)) {
		    cm.sendOk("由於未知錯誤，隊伍的請求已被拒絕.");
					cm.dispose();
				}
			} else if (selection == 2) {
				status = 12;
				if (!cm.getSquadList("Horntail", 2)) {
		    cm.sendOk("由於未知錯誤，隊伍的請求已被拒絕.");
					cm.dispose();
				}
			} else if (selection == 3) { // get insode
				if (cm.getSquad("Horntail") != null) {
					var dd = cm.getEventManager("HorntailBattle");
					dd.startInstance(cm.getSquad("Horntail"), cm.getMap());
				} else {
		    cm.sendOk("由於未知錯誤，隊伍的請求已被拒絕.");
				}
				cm.dispose();
			}
		} else {
			cm.dispose();
		}
		break;
	case 11:
		cm.banMember("Horntail", selection);
		cm.dispose();
		break;
	case 12:
		if (selection != -1) {
			cm.acceptMember("Horntail", selection);
		}
		cm.dispose();
		break;
	default:
		cm.dispose();
		break;
	}
}

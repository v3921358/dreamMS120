/* Mu Young
Boss Balrog
 */

var status = -1;
var balrogMode; // false = easy, true = hard

function action(mode, type, selection) {
	switch (status) {
	case -1:
		status = 0;
		switch (cm.getChannelNumber()) {
		case 5:
			balrogMode = true;
			cm.sendNext("你可以參加 #b普通模式-魔王巴洛古#k. 如果想挑戰其他模式, 切換到其他頻道. \n\r #b#i3994116# 頻道5 / Level 40 上 / 6 ~ 15 玩家 \r\n#b#i3994115# 其他頻道 / Level 40 ~ Level 70 / 3 ~ 6 玩家.");
			break;
		default:
			balrogMode = false;
			cm.sendNext("你可以參加 #b簡單模式-魔王巴洛古#k. 如果想挑戰其他模式, 請切換到正確頻道. \n\r #b#i3994116# 頻道5 / Level 40 以上 / 6 ~ 15 玩家 \r\n#b#i3994115# 其他頻道 / Level 40 ~ Level 70 / 3 ~ 6 玩家.");
			break;
		}
		break;
	case 0:
		var em = cm.getEventManager(balrogMode ? "BossBalrog_NORMAL" : "BossBalrog_EASY");

		if (em == null) {
			cm.sendOk("目前副本有問題, 請聯絡 GM.");
			cm.safeDispose();
			return;
		}

		if (cm.getParty() != null) {
			var prop = em.getProperty("state");
			if (prop == null || prop.equals("0")) {
				var squadAvailability = cm.getSquadAvailability("BossBalrog");
				if (squadAvailability == -1) {
					status = 1;
					cm.sendYesNo("你想要成為 魔王巴洛古 遠征隊的隊長?");

				} else if (squadAvailability == 1) {
					// -1 = Cancelled, 0 = not, 1 = true
					var type = cm.isSquadLeader("BossBalrog");
					if (type == -1) {
						cm.sendOk("遠征隊已經結束, 請重新申請.");
						cm.safeDispose();
					} else if (type == 0) {
						var memberType = cm.isSquadMember("BossBalrog");
						if (memberType == 2) {
							cm.sendOk("你已經被禁止.");
							cm.safeDispose();
						} else if (memberType == 1) {
							status = 5;
							cm.sendSimple("你想要做些什麼? \r\n#b#L0#檢查隊員#l \r\n#b#L1#加入遠征#l \r\n#b#L2#退出遠征#l");
						} else if (memberType == -1) {
							cm.sendOk("遠征隊已經結束, 請重新申請.");
							cm.safeDispose();
						} else {
							status = 5;
							cm.sendSimple("你想要做些什麼? \r\n#b#L0#檢查隊員#l \r\n#b#L1#加入遠征#l \r\n#b#L2#退出遠征#l");
						}
					} else { // Is leader
						status = 10;
						cm.sendSimple("你想要做些什麼? \r\n#b#L0#檢查隊員#l \r\n#b#L1#剔除隊員#l \r\n#b#L2#編輯列表#l \r\n#r#L3#進入地圖#l");
						// TODO viewing!
					}
				} else {
					var eim = cm.getDisconnected(balrogMode ? "BossBalrog_NORMAL" : "BossBalrog_EASY");
					if (eim == null) {
						cm.sendOk("目前有遠征隊在挑戰了.");
						cm.safeDispose();
					} else {
						cm.sendYesNo("噢, 你已經回來啦. 你想要重新加入一次你的隊伍??");
						status = 2;
					}
				}
			} else {
				var eim = cm.getDisconnected(balrogMode ? "BossBalrog_NORMAL" : "BossBalrog_EASY");
				if (eim == null) {
					cm.sendOk("目前有遠征隊在挑戰了.");
					cm.safeDispose();
				} else {
					cm.sendYesNo("噢, 你已經回來啦. 你想要重新加入一次你的隊伍??");
					status = 2;
				}
			}
		} else {
			cm.sendPrev("你需要隊伍.");
			cm.safeDispose();
		}
		break;
	case 1:
		if (mode == 1) {
			if (!balrogMode) { // Easy Mode
				var lvl = cm.getPlayerStat("LVL");
				if (lvl >= 40 && lvl <= 70) {
					if (cm.registerSquad("BossBalrog", 5, " 已經為 遠征隊長. 如果想參加遠征,找武英加入.")) {
						cm.sendOk("你已為 魔王巴洛古 遠征隊長，5分鐘內，你可增加遠征隊成員.");
					} else {
						cm.sendOk("錯誤, 再試一次.");
					}
				} else {
					cm.sendNext("有成員的等級不在 40 ~ 70 範圍內，請確認每個人是否都符合等級限制");
				}
			} else { // Normal Mode
				if (cm.registerSquad("BossBalrog", 5, " 已經為 遠征隊長. 如果想參加遠征,找武英加入.")) {
					cm.sendOk("你已為 魔王巴洛古 遠征隊長，5分鐘內，你可增加遠征隊成員.");
				} else {
					cm.sendOk("錯誤, 再試一次.");
				}
			}
		} else {
			cm.sendOk("如果你想成為遠征隊長，跟我說話.")
		}
		cm.safeDispose();
		break;
	case 2:
		if (!cm.reAdd(balrogMode ? "BossBalrog_NORMAL" : "BossBalrog_EASY", "BossBalrog")) {
			cm.sendOk("錯誤... 請在試一次.");
		}
		cm.safeDispose();
		break;
	case 5:
		if (selection == 0) {
			if (!cm.getSquadList("BossBalrog", 0)) {
				cm.sendOk("發生未知錯誤，隊伍要求被拒絕.");
				cm.safeDispose();
			} else {
				cm.dispose();
			}
		} else if (selection == 1) { // join
			var ba = cm.addMember("BossBalrog", true);
			if (ba == 2) {
				cm.sendOk("目前遠征人數滿，請稍後在試.");
				cm.safeDispose();
			} else if (ba == 1) {
				cm.sendOk("你已加入遠征隊");
				cm.safeDispose();
			} else {
				cm.sendOk("你已是遠征隊的了.");
				cm.safeDispose();
			}
		} else { // withdraw
			var baa = cm.addMember("BossBalrog", false);
			if (baa == 1) {
				cm.sendOk("你已退出遠征隊");
				cm.safeDispose();
			} else {
				cm.sendOk("你不是遠征隊的.");
				cm.safeDispose();
			}
		}
		break;
	case 10:
		if (selection == 0) {
			if (!cm.getSquadList("BossBalrog", 0)) {
				cm.sendOk("發生未知錯誤，隊伍的要求被拒絕.");
			}
			cm.safeDispose();
		} else if (selection == 1) {
			status = 11;
			if (!cm.getSquadList("BossBalrog", 1)) {
				cm.sendOk("發生未知錯誤，隊伍的要求被拒絕.");
			}
			cm.safeDispose();
		} else if (selection == 2) {
			status = 12;
			if (!cm.getSquadList("BossBalrog", 2)) {
				cm.sendOk("發生未知錯誤，隊伍的要求被拒絕.");
			}
			cm.safeDispose();
		} else if (selection == 3) { // get insode
			if (cm.getSquad("BossBalrog") != null) {
				var dd = cm.getEventManager(balrogMode ? "BossBalrog_NORMAL" : "BossBalrog_EASY");
				dd.startInstance(cm.getSquad("BossBalrog"), cm.getMap());
				cm.dispose();
			} else {
				cm.sendOk("發生未知錯誤，隊伍的要求被拒絕.");
				cm.safeDispose();
			}
		}
		break;
	case 11:
		cm.banMember("BossBalrog", selection);
		cm.dispose();
		break;
	case 12:
		if (selection != -1) {
			cm.acceptMember("BossBalrog", selection);
		}
		cm.dispose();
		break;
	}
}

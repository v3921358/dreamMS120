var status = -1;
var log = "妖僧";
var maxtime = 7;
var ch = 2;
var minLv = 50;
var maxLv = 100;

function start() {
	action(1, 0, 0);
}

function action(mode, type, selection) {
	if (mode == 1) {
		status++;
	} else {
		cm.dispose();
		return;
	}
	
	if (cm.getClient().getChannel() != ch) {
		cm.sendNext("只有在" + ch + "頻道才可以挑戰妖僧");
		cm.dispose();
		return;
	}
	
	if (status == 0) {
		cm.sendYesNo("#b#k#h你好  ##e\r\n#b有事嗎??#k \r\n#L0##r我要挑戰少林寺妖僧#k#l");
	} else if (status == 1) {
			var msg = "Null";

			if (cm.getQuestStatus(8534) != 2) {
				msg = "你似乎不夠資格挑戰少林寺妖僧！";
			} else if (cm.getParty() == null) {
				msg = "組隊過後再來找我....";
			} else if (!cm.isLeader()) {
				msg = "叫隊長來找我!";
			} else if (cm.getPlayer().getParty().getMembers().size() < 2) {
				msg = "要 3 個人以上才可以挑戰！";
			}

			if (msg != "Null") {
				cm.sendNext(msg);
				cm.dispose();
				return;
			}

			var party = cm.getParty().getMembers();
			var next = true;
			var it = party.iterator();
			while (it.hasNext()) {
				var cPlayer = it.next();

				if ((cPlayer.getLevel() < minLv || cPlayer.getLevel() > maxLv)) {
					next = false;
					break;
				}

			}

			if (!next) {
				cm.sendNext("等級小於 #r" + minLv + "#k 或者已經大於 #r" + maxLv + "#k");
				cm.dispose();
				return;
			}

			var em = cm.getEventManager("Yaoseng");

			if (em == null) {
				cm.sendNext("副本有問題，請找GM");
				cm.dispose();
				return;
			}

			var prop = em.getProperty("state");

			if (prop != null && !prop.equals("0")) {
				cm.sendNext("目前裡面有隊伍...");
				cm.dispose();
				return;
			}

			em.startInstance(cm.getParty(), cm.getMap());
			cm.dispose();
	}
}

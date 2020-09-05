/* 
 * NPC   : Dev Doll
 * Map   : GMMAP
 */
importPackage(Packages.server);
importPackage(Packages.client.inventory);
var status = 0;
var invs = Array(1, 5);//裝備 1 / 消耗 2 / 裝飾 3 / 其他 4 / 特殊 5
var invv;
var selected;
var slot_1 = Array();
var slot_2 = Array();
var statsSel;

function start() {
	cm.sendGetText("請輸入玩家名稱:");
	//action(1,0,0);
}

function action(mode, type, selection) {
	var p = cm.getCharByName(cm.getText());
	if (p == null) {
		cm.sendOk("#e#r該玩家沒上或是不同頻道."+cm.getText());
		cm.dispose();
		return;
	}
	if (mode != 1) {
		cm.dispose();
		return;
	}
	status++;
	if (status == 1) {
		var bbb = false;
		var selStr = "I can help you drop a cash item where you stand:\r\n\r\n#b";
		for (var x = 0; x < invs.length; x++) {
			var inv = p.getInventory(MapleInventoryType.getByType(invs[x]));
			for (var i = 0; i <= inv.getSlotLimit(); i++) {
				if (x == 0) {
					slot_1.push(i);
				} else {
					slot_2.push(i);
				}
				var it = inv.getItem(i);
				if (it == null) {
					continue;
				}
				var itemid = it.getItemId();
				if (!MapleItemInformationProvider.getInstance().isCash(itemid)) {
					//continue;
				}
				bbb = true;
				selStr += "#L" + (invs[x] * 1000 + i) + "##v" + itemid + "##t" + itemid + "##l\r\n";
			}
		}
		if (!bbb) {
			cm.sendOk("You don't have any cash items.");
			cm.dispose();
			return;
		}
		cm.sendSimple(selStr + "#k");
	} else if (status == 2) {
		invv = selection / 1000;
		selected = selection % 1000;
		var inzz = p.getInventory(MapleInventoryType.getByType(invv));
		if (invv == invs[0]) {
			statsSel = inzz.getItem(slot_1[selected]);
		} else {
			statsSel = inzz.getItem(slot_2[selected]);
		}
		if (statsSel == null) {
			cm.sendOk("Error, please try again.");
			cm.dispose();
			return;
		}
		cm.sendGetNumber("You want to drop #v" + statsSel.getItemId() + "##t" + statsSel.getItemId() + "#.\r\nHow many?", 1, 1, statsSel.getQuantity());
	} else if (status == 3) {
		
		
		if (!/*p.dropItem(selected, invv, selection)*/MapleInventoryManipulator.drop(p.getClient(), MapleInventoryType.getByType(invv), selected, selection, true)) {
			cm.sendOk("Error, please try again!");
			cm.dispose();
		} else {
			status = 0;
			action(1, 0, 0);
		}
	}
}
/*
var status = 6;

function start() {
    action(0,0,0);
}

function cancelled() {
    action(0,0,0);
}

function action(mode, type, selection) {
    switch (status) {
	case 5:
	    status = 6;
	    cm.sendNext("I'm afraid I can't let you go without entering the right password. (Yes I'm bad, blame me by all means) #bYou Cheater! I'm not stupid either.")
	    break;
	case 6:
	    status = 10;
	    cm.sendGetText("For the sake of privacy, please enter your first password which you use to login. #bYou have 2 tries from now.");
	    break;
	case 10: {
	    var pw = cm.getText();
	    if (cm.checkPassword(pw)) {
		cm.sendOk("You have authenticated yourself successfully, enjoy. Celino Online Staff. #b(Please do set a second password on the login page too)");
		cm.dispose();
	    } else {
		cm.sendOk("Invalid password, please try again.");
		status = 6;
	    }
	    break;
	}
    }
}
*/
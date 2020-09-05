/* Kedrick
	Fishking King NPC
*/

/* global cm */

var status = -1;
var sel;
var Rate = 2;

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

    if (status == 0) {
	cm.sendSimple("你想做什麼？#b\n\r #L3#使用精靈小釣手高級魚餌罐頭#l \n\r #L1#購買精靈小釣手專用一般魚餌#l#k");
    } else if (status == 1) {
	sel = selection;
	if (sel == 1) {
	    cm.sendYesNo("120魚餌需要300000楓幣。 您要購買嗎？");
	} else if (sel == 3) {
	    if (cm.canHold(2300003,120) && cm.haveItem(5350003,1)) {
			if (!cm.haveItem(2300003, 120 * Rate) && !cm.haveItem(2300003, 120 * (Rate), false, false)) {
				cm.gainItem(2300003, 120);
				cm.gainItem(5350003,-1);
				cm.sendNext("Happy Fishing~");
			} else {
				cm.sendNext("你已經有魚餌。");
			}
	    } else {
		cm.sendOk("Please check if you have sufficient inventory slot and the Delicious Bait Can from cash shop with you.");
	    }
	    cm.safeDispose();
	}
    } else if (status == 2) {
	if (sel == 1) {
	    if (cm.canHold(2300002,120) && cm.getMeso() >= 300000) {
			if (!cm.haveItem(2300002, 120 * Rate+1) && !cm.haveItem(2300002, 120 * (Rate+1), false, false)) {
				cm.gainMeso(-300000);
				cm.gainItem(2300002, 120);
				cm.sendNext("Happy Fishing~");
			} else {
				cm.sendNext("你已經有魚餌。");
			}
	    } else {
		cm.sendOk("Please check if you have the required meso or sufficient inventory slot.");
	    }
	    cm.safeDispose();
	}
    }
}
/* Aura
 * 
 * Adobis's Mission I: Unknown Dead Mine (280010000)
 * 
 * Zakum PQ NPC (the one and only)
 */

var status = -1;
var selectedType;
var scrolls;

function action(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	status--;
    }

    if (status == 0) {
	cm.sendSimple("還有不明白的地方嗎?#b#l\r\n#L1#把火石的母礦拿來了#l\r\n#L2#想放棄任務從這裡出去!#l");
    } else if (status == 1) {
	selectedType = selection;
	if (selection == 0) {
	    cm.sendNext("To reveal the power of Zakum, you'll have to recreate its core. Hidden somewhere in this dungeon is a \"Fire Ore\" which is one of the necessary materials for that core. Find it, and bring it to me.\r\n\r\nOh, and could you do me a favour? There's also a number of Paper Documents lying under rocks around here. If you can get 30 of them, I can reward you for your efforts.")
	    cm.safeDispose();
	} else if (selection == 1) {
	    if (!cm.haveItem(4001018)) { //documents
		cm.sendNext("請確定身上有#b火石的母礦")
		cm.safeDispose();
	    } else {
		if (!cm.haveItem(4001015, 30)) { //documents
		    cm.sendYesNo("#b火石的母礦 1個#k雖然已經拿來了，但是卻沒有#b回家捲軸秘笈哦!隊員們收集的全部東西就是這些嗎?");
		    scrolls = false;
		} else {
		    cm.sendYesNo("#b火石的母礦 1個#k雖然已經拿來了，但是卻沒有#b回家捲軸秘笈哦!隊員們收集的全部東西就是這些嗎?");
		    scrolls = true;
		}
	    }
	} else if (selection == 2) {
	    cm.sendYesNo("你確定要離開？ 如果你是隊長，你的隊員也將被傳送出去。")
	}
    } else if (status == 2) {
	var eim = cm.getEventInstance();
	if (selectedType == 1) {
				
	    cm.gainItem(4001018, -1);
	    if (scrolls) {
		cm.gainItem(4001015, -30);
	    }
	    //give items/exp
	    cm.givePartyItems(4031061, 1);
	    if (scrolls) {
		cm.givePartyItems(2030007, 5);
		cm.givePartyExp(20000);
	    } else {
		cm.givePartyExp(12000);
	    }
				
	    //clear PQ

	    if (eim != null) {
	    	eim.finishPQ();
	    }
	    cm.dispose();
	} else if (selectedType == 2) {
	if (eim != null) {
	    if (cm.isLeader())
		eim.disbandParty();
	    else
		eim.leftParty(cm.getChar());
	} else {
		cm.warp(280090000, 0);
	}
	    cm.dispose();
	}
    }
}
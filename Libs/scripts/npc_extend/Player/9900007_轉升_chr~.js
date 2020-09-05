/*
	功能：玩家轉升系統
	時間：2018年2月6日
*/

var status = -1;
var doR = 0;
function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else {
        if (status == 0) {
            cm.dispose();
        }
        status--;
    }
    if (status == 0) {
		doR = cm.getPlayer().getRebirths();
        var selStr = "目前轉升次數為:"+doR+"\r\n請選擇:\r\n#L0#冒險家#l/t#L1000#皇家騎士團#l/t#L2000#狂狼勇士#l/t#L2200#魔龍導士#l";
		if (doR >= 20) {
			cm.sendOk("轉升次數已封頂 無法再進行轉升");
			 cm.dispose();
			 return;
		}
		if (cm.getPlayer().getLevel() < 200) {
			cm.sendOk("你尚未達到200等無法進行轉升");
			 cm.dispose();
			 return;
		}
        cm.sendSimple(selStr);
    } else if (status == 1) {
		var checklvl = cm.getLog(bossid(doR), true) >= 1;
		cm.getPlayer().doReborn(selection);
		if (doR >= 5 && checklvl) {//??
			//cm.gainItem(ID, 數量);//這樣嗎?
			cm.setLog(bossid(bossid(doR)));
		}
        cm.dispose();
    }
}

function bossid(lvl) {
	return "轉升次數" + lvl;
}
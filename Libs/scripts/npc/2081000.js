
/**
	Chief Tatamo - Leafre(240000000)
**/

var section;
var temp;
var cost;
var count;
var menu = "";
var itemID = new Array(4000226, 4000229, 4000236, 4000237, 4000261, 4000231, 4000238, 4000239, 4000241, 4000242, 4000234, 4000232, 4000233, 4000235, 4000243);
var status = -1;

function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else {
        if (status == 0) {
            cm.dispose();
        }
        if (status > 2) {
            if (section == 0) {
                cm.sendOk("請慎重考慮。一旦你做出了決定，讓我知道。");
            } else {
                cm.sendOk("想想吧，然後讓我知道你的決定。");
            }
            cm.safeDispose();
        }
        status--;
    }
    if (status == 0) {
        cm.sendSimple("你找我有事嗎？\r\n#L0##b魔法種子#k#l\r\n#L1##b為了神木村的行動#k#l\r\n#L2##b做四轉材料");
    } else if (status == 1) {
        section = selection;
        if (section == 0) {
            cm.sendSimple("需要我幫助你？？\r\n#L0##b我想跟你買一些 #t4031346#.#k#l");
        } else if (section == 1) {
            cm.sendNext("更好的建設村落是村長的職責。所以需要更多更好的道具。你能為了村落捐獻出在神木村附近收集到的道具嗎？");
		} else if (section == 2) {
			cm.sendNext("您需要為您的四轉做準備嗎?? 我需要#t4031348#。");
        }
    } else if (status == 2) {
        if (section == 0) {
            cm.sendGetNumber("#b#t4031346##k 需要買多少個？？", 1, 1, 99);
		} else if (section == 2) {
			if (cm.canHold()) {
			if (cm.haveItem(4031348)) {
				status = 3;
				cm.sendNext("你已經有了#t4031348#，那麼現在我要用我的神奇魔法把#t4031348#變成四轉所需的道具。");
			} else {
				cm.sendNext("請到玩具城44F找NPC購買#t4031348#。");
				cm.safeDispose();
			}
			} else {
				cm.sendNext("請檢查你的裝備欄。");
				cm.safeDispose();
			}
        } else {
            for (var i = 0; i < itemID.length; i++) {
                menu += "\r\n#L" + i + "##b#t" + itemID[i] + "##k#l";
            }
            cm.sendNext("你想捐獻出那種道具呢？" + menu);
            //cm.safeDispose();
        }
    } else if (status == 3) {
        if (section == 0) {
            if (selection == 0) {
                cm.sendOk("我不能賣你0個。");
                cm.safeDispose();
            } else {
                temp = selection;
                cost = temp * 30000;
                cm.sendYesNo("你要買 #b" + temp + " #t4031346##k 它將花費你 #b" + cost + " 楓幣#k. 你確定要購買？？?");
            }
        } else {
            temp = selection;
            if (cm.haveItem(itemID[temp])) {
                //cm.sendGetNumber("How many #b#t" + itemID[temp] + "#k's would you like to donate?\r\n#b< Owned : #c" + itemID[temp] + "# >#k", 0, 0, "#c" + itemID[temp] + "#");
                cm.sendGetNumber("你要捐多少個 #b#t" + itemID[temp] + "#k'我會給你很好的酬勞的！", 1, 1, 999);
            } else {
                cm.sendNext("我不認為你有這道具。");
                cm.safeDispose();
            }
        }
    } else if (status == 4) {
        if (section == 0) {
            if (cm.getMeso() < cost || !cm.canHold(4031346)) {
                cm.sendOk("請確認是否有足夠的楓幣和道具欄位。");
            } else {
                cm.sendOk("再會~");
                cm.gainItem(4031346, temp);
                cm.gainMeso(-cost);
            }
            cm.safeDispose();
		} else if (section == 2) {
			if(!cm.canHold(4031860,2) || !cm.canHold(4031861,2)){
				cm.sendNext("The space doesnt enough .");
				cm.dispose();
				return;
			}
			if (cm.haveItem(4031348)) { //2nd check need item
				cm.gainItem(4031348,-1);
				cm.gainItem(4031860,1);
				cm.gainItem(4031861,1);
				cm.sendOk("恭喜你已經獲得#t4031860# x1 #t4031861# x1");
			} else {
				cm.sendOk("您貌似沒有#t4031348# 0.0");
			}
			cm.safeDispose();
        } else {
            count = selection;
            cm.sendYesNo("你確定你想贊助 #b" + count + " #t" + itemID[temp] + "##k?");
        }
    } else if (status == 5) {
        if (count == 0 || !cm.haveItem(itemID[temp], count)) {
            cm.sendNext("請確認贊助項目是否足夠。");
        } else {
            cm.gainItem(itemID[temp], -count);
            cm.sendNext("感謝你贊助。");
        }
        cm.safeDispose();
    }
}
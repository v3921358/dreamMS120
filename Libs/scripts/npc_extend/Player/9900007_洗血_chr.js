/*
	功能：玩家轉升系統
	時間：2018年2月6日
*/

var status = -1;
//40 點數換 15 hp
var cs=40, hp=15, mods;

function start() {
	var text = "請選擇要使用的點數方式\r\n#L1#CASH#l\t#L2#楓葉點數#l";
	cm.sendSimple(text);
}

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
		mods = selection;
		var text = "#b40點為#r+15HP#b\r\n請輸入使用的次數(付費方式#r"+(mods==1?"CASH":"楓葉點數")+"#b)：";
		cm.sendGetNumber(text,1,1,2000);
    } else if (status == 1) {
		cs *= selection;
		hp *= selection;
		cm.sendYesNo("#b所花費 #r"+cs+"#b 點數 增加#r"+hp+"#b 血量？");
    } else if (status == 2) {
		var cs_P = false;
		
		if (mods == 1 && cs > cm.getPlayer().getCSPoints(1)) {
			cs_P = true;
		} else if (mods == 2 && cs > cm.getPlayer().getCSPoints(2)) {
			cs_P = true;
		}
		if (cs_P){
			cm.sendOk("CASH或楓葉點數不足");
			cm.dispose();
			return;
		}
		cm.getPlayer().modifyCSPoints(mods, -cs, true);
		cm.getPlayer().MapleStatHP(hp);
        cm.dispose();
    }
}
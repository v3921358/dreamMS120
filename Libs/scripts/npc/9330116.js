importPackage(Packages.Apple.console.groups.setting); 

var status = -1;
var GachItem = new Array(
				[4030000,10000,true],
				[4030001,100,true]
				);
				//物品ID,機率,是否上廣
var itemList = new Array(
	1482052, 
	1482010, 
	1492011, 
	1492012, 
	1482013, 
	1492013
);

var NpcName = "海盜轉蛋機";
var NpcItems = 5220000;
var All_eq = 850; //所有裝備機率
var Vip_eq = 20; //特殊裝備機率
var Rare_eq = 1; //稀有裝備機率
var Scrolls = 3; //卷軸數量
var EqJob = 500;

function action(mode, type, selection) {
	if (mode == -1) {
		cm.dispose();
	} else {
		if (mode == 0 && status == 0) {
			cm.dispose();
			return;
		}
		if (mode == 1)
			status++;
		else
			status--;
	}
    if (status == 0) {
		if (cm.getPlayer().getLevel() < 30) {
			cm.sendOk("你目前等級無法使用該功能");
			cm.dispose();
			return;
		}
		if (cm.getPlayer().isAdmin()) isGm = -1; else isGm = -2;
		if (cm.haveItem(NpcItems, 1)) {
			var text  = "[#b#p" +cm.getNpc()+"##k]：\r\n您身上有 #b#t"+NpcItems+"##i"+NpcItems+"##k 可以進行轉蛋。\r\n\r\n";
			    text += "#L0##e#d你確定要使用嗎？#l\r\n\r\n";
				text += "\r\n#r－－－－－－－－－－－－－－－－－－－－－－－－#k\r\n";
				text += Gachapon.SearchConsole(EqJob, isGm).left;
			cm.sendYesNo(text);
		} else {
			cm.sendOk("不好意思!您沒有#b#t"+NpcItems+"##i"+NpcItems+"##k。");
			cm.safeDispose();
		}
	} else if (status == 1) {
		var items = Gachapon.lottery(EqJob);
		if (selection == 0) {
			if (items != null) {
				item = cm.gainGachaponItem(items.left, 1, false, 2, items.right >= 800000 ? "" : NpcName);
				//檢查背包是否滿
				if (item != -1) {
					cm.gainItem(NpcItems, -1);
					cm.sendOk("#e您已獲得 #b#i" + item + ":#" + (cm.getPlayer().isAdmin() ? "  此裝備機率: " + (items.right/1000000)*100 + "%" : "  #t" + item + ":#"));
					cm.dispose();
				} else {
					cm.sendOk("#e檢查一下#b背包#r是否已滿#k");
					cm.dispose();
				}
				cm.safeDispose();
				//檢查背包是否滿
			} else {
				var Scroll = cm.Scroll();
				if (Scroll != -2) {
					cm.sendOk("#e很可惜你並沒有中獎。\r\n將贈送安慰獎 #b#i" + Scroll + ":##t" + Scroll + "# "+Scrolls+" 張");
					cm.gainItem(NpcItems, -1);
					cm.gainItem(Scroll,Scrolls);
					cm.dispose();
				} else {
					cm.sendOk("#e請回報管理員物品代碼");
					cm.dispose();
				}
			}
			cm.safeDispose();
		} else {
			cm.sendOk(Gachapon.SearchConsole(EqJob,selection).right);
			status = -1;
			//cm.dispose();
		}
    }
}
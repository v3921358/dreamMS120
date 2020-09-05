importPackage(Packages.Apple.console.groups.setting); 

var status = -1;
var itemList = new Array(1302026, 1302032, 1302037, 1302081, 1302086, 1302173, 1302174, 1302146, 1302145, 1302144, 1302143, 1302217, 1302218, 1302219, 1302220, 1302221, 1302223, 1302248, 1322060, 1322061, 1322086, 1322087, 1322088, 1322107, 1322108, 1322181, 1312037, 1312038, 1312072, 1312073, 1312135, 1312058, 1312059, 1312060, 1402046, 1402047, 1402111, 1402112, 1402172, 1402086, 1402087, 1402088, 1412033, 1412034, 1412071, 1412072, 1412122, 1412058, 1412059, 1412060, 1422037, 1422038, 1422073, 1422074, 1422124, 1422059, 1422060, 1422061, 1432047, 1432049, 1432150, 1432077, 1432078, 1432079, 1442063, 1442067, 1442136, 1442137, 1442202, 1442107, 1442108, 1442109, 1542012, 1542013, 1542033, 1542034, 1542060);

var NpcName = "戰士轉蛋機";
var NpcItems = 5220000;
var All_eq = 850; //所有裝備機率
var Vip_eq = 20; //特殊裝備機率
var Rare_eq = 1; //稀有裝備機率
var Scrolls = 3; //卷軸數量
var EqJob = 100;

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
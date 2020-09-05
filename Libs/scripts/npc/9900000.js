/**
*冬季戀曲，製作。
*製作者:baby_0402_85@yahoo.com.tw 
*/

importPackage(java.lang);
var th = -1;
var order = [-1,-1];
var MesosUI = "#fUI/UIWindow.img/QuestIcon/7/0#";
var SelectUI = "#fUI/UIWindow.img/QuestIcon/3/0#";
var MapleLeaf = 4001126;/*變數*/
var P = 10000;/*變數*/
var Receive = true;
/**************---
頭盔
耳環
披風
盾牌
武器
永恆
轉轉樂
永恆
血腥
女皇
血腥
女皇
*/
var equipList = [
['頭盔',//[ID,數量,金錢,升級]
	[1002508,50,10,null],
	[1002509,100,20,1002508],
	[1002510,120,30,1002509],
	[1002511,150,50,1002510],
	[1002758,200,70,1002511],
],
['耳環',
	[1032040,50,20,null],
	[1032041,75,40,1032040],
	[1032042,100,70,1032041],
],
['披風',
	[1102166,70,20,null],
	[1102167,100,40,1102166],
	[1102168,130,60,1102167],
	[1102071,160,70,1102168],
	[1102198,200,90,1102071],
],
['盾牌',
	[1092030,150,10,null],
	[1092045,500,20,1092030],
	[1092046,500,20,1092030],
	[1092047,500,20,1092030],
	/*[1092057,500,100,1092045],
	[1092058,500,100,1092046],
	[1092059,500,100,1092057],*/
]
/*
],['武器',
1302142,1312056,1322084,1402085,1412055,1422057,1432075,1442104,1372071,1382093,1452100,1462085,1332114,1472111,1482073,1492073
],['永恆',
1302081,1312037,1322060,1402046,1412033,1422037,1432047,1442063,1372044,1382057,1452057,1462050,1332074,1472068,1482023,1492023
]*/
];

var ItemID, ItemNum, Meso, upgrade, DisLv, Mesos;

function start() {
    action(1, 0, 0);
}

function action(m, type, s) {
    if (m == -1) {
		cm.sendOk("下次在來歐~~");
        cm.dispose();
    } else {
        if (m == 0 && th == -1) {
            cm.dispose();
            return;
        }
        if (m == 1)
            th++;
		else
            th--;
	}
	var sel = -1;
	if (th <= sel++) {
		cm.dispose();
    } else if (th == sel++) {
		/**
		equipList[總列表][分類列表<0 為名稱>][物品]
		*/
		var ret  = SelectUI + "\t\t\t\t\t#b#e【親愛的：#h #】 #n#b";
			ret += "\t\t\t\t\t\t#e下列為各式#r裝備#b/#r武器#b製作展示";
		for (var loop = 0 ; loop < equipList.length; loop ++) {
			if (loop%2 == 0)
				ret += "\r\n"
			ret += "#L" + loop + "##d#e" + equipList[loop][0] + "#v" + equipList[loop][equipList[loop].length-1] + ":##n#l\t";
		}
		cm.sendSimple(ret);
	} else if (th == sel++) {
		order[0] = s;/**記錄 equipList[s]*/
		lol = order[0];
		var ret  = SelectUI + "\t\t\t#e#b您目前的 #i"+MapleLeaf+"# 有#k【#c"+MapleLeaf+"#】#b個#n";
		for (var loop = 0 ; loop < equipList[order[0]].length-1; loop++) {
			if (loop%2 == 0)
				ret += "\r\n"
			ItemID = equipList[lol][loop+1][0];//物品ID
			DisLv = "Lv " + cm.Display(ItemID);//等級LV
			Meso = equipList[lol][loop+1][2];//製作費
			var Image = " #i" + ItemID + ":#";
			ret += "#d#L"+loop+"#"+DisLv+Image+"#r[費用"+Meso+"萬]#n";
		}
		cm.sendSimple(ret);
	} else if (th == sel++) {
		order[1] = s;/**記錄 equipList[?][s]*/
		ItemID = equipList[order[0]][order[1]+1][0];//物品ID
		ItemNum = equipList[order[0]][order[1]+1][1];//物品ID
		Meso = equipList[order[0]][order[1]+1][2];//物品ID
		upgrade = equipList[order[0]][order[1]+1][3];//是否為升級物品
		DisLv = "Lv " + cm.Display(ItemID);//等級LV
		Mesos = cm.getPlayer().getMeso();//目前金錢
		var ret = "#b#e請確認你所選取的道具是否為:#n\r\n";
			ret += "\t#d "+DisLv+" #i"+ItemID+":##k【#t"+ItemID+"#】\r\n\r\n";
			ret += "#b#e下列所需材料:#n#r\r\n";
			if (upgrade != null)
				ret += "\t#i"+upgrade+":# Lv " + cm.Display(upgrade)+"\r\n";
			ret += "\t#i"+MapleLeaf+"# "+ItemNum+"個 楓葉\r\n";
			ret += "\t#i4031138# "+Meso+"萬 Meso\r\n";
			ret += "\r\n#e#g#k註：#d物品齊全在點擊【#r是#d】#g";
			cm.sendYesNo(ret);
	} else if (th == sel++) {
		if(!cm.haveItem(MapleLeaf, ItemNum))
			Receive = false;
		if(!cm.getMeso() >= Meso * P)
			Receive = false;
		if (upgrade != null && !cm.haveItem(upgrade, 1))
			Receive = false;
		
		if (Receive){
			cm.gainMeso(-Meso * P);
			cm.gainItem(MapleLeaf,-ItemNum);
			if (upgrade != null)
				cm.gainItem(upgrade,-1);
			cm.gainItem(ItemID,1);
			cm.sendOk("#e打開【 I 】道具欄檢查沒有拿到\r\n#r"+DisLv+"#i"+ItemID+":##k【#t"+ItemID+"#】");
			cm.dispose();
		} else {
			cm.sendOk("#e您的材料尚未齊全請在確認一次");
			cm.dispose();
		}			
	}
}






















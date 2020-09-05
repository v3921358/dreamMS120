importPackage(Packages.server); 
importPackage(Packages.client); 
importPackage(Packages.Apple.console.groups.setting); 
importPackage(Packages.backup.GUI.Settings); 

var status;
//美化設定
var eff = "#fUI/UIWindow.img/PvP/Scroll/enabled/next2#";
var z = "#fMap/MapHelper.img/weather/starPlanet2/7#";//"+z+"//美化
var zz = "#fEffect/CharacterEff/1082565/2/0#";//
var eff1 = "#fEffect/CharacterEff/1112905/0/1#";//小紅心
var icon = "#fUI/UIWindow.img/icon/WorldUI/BtQ/normal/0#";
var iconEvent = "#fUI/UIToolTip.img/Item/Equip/Star/Star1#";
var tt = "#fEffect/ItemEff/1112811/0/0#";//音符
var ttt = tt;
var ttt2 = "#fUI/UIWindow/Quest/icon6/7#";////美化2								[紅色箭頭]
var ttt3 = "#fUI/UIWindow/Quest/icon3/6#";//"+ttt3+"//美化圓					[中心圓]
var ttt4 = "#fUI/UIWindow/Quest/icon5/1#";//"+ttt4+"//美化New   				[NEW]
var ttt5 = "#fUI/UIWindow/Quest/icon0#";////美化!								[驚嘆號]
var ttt6 = "#fUI/UIWindow.img/PvP/Scroll/enabled/next2#";//"+ttt6+"//美化會員	[下頭向下]
var z1 = "#fUI/UIWindow.img/PvP/Scroll/enabled/next2#";//"+z+"//美化			[下頭向下]
var kkk = tt;//																	[音符圖案]

var menuList = Array(
		Array(iconEvent, "#e#r尚未開放#n", 6, false, 9900007, "GM"),
        Array(kkk, "帳號信息", 0, true, 9900007, "帳戶_chr"),
		Array(kkk, "#r物品兌換", 6, true, 9900003, "兌換_chr"),
		Array(kkk, "升級獎勵", 6, true, 9900007, "升級_chr"),
		Array(kkk, "查看爆率", 2, true, 9900007, "爆率_chr"),
		Array(kkk, "遊戲排行", 3, true, 9900007, "排行_chr"),
		Array(kkk, "理髮眼睛", 6, true, 9105006, -1),
		//Array(ttt2, "#e#k轉升#n", 6, true, 9900007, "轉升_chr"),
		Array(kkk, "#e#k洗血專用#n", 6, true, 9900007, "洗血_chr"),
		Array(kkk, "#e#k職業轉殖#n", 6, true, 1012114, -1),
		Array(kkk, "#e#k萬能商店#n", 6, true, 9330012, -1),
		Array(kkk, "#e#k地圖傳送#n", 6, true, 9000020, -1),
		Array(kkk, "#e#k21點賭博#n", 6, true, 9209001, -1),
		Array(kkk, "#e#k換xx幣#n", 6, true, 9330092, -1),
		Array(ttt2, "#e#k了解xx谷#n", 6, true, 9105006, "help"),
		
		//Array(kkk, "#r補償獎勵", 6, true, 9900007, -80),
		//Array(kkk, "連續簽到", 6, true, 9900007, "連續簽到"),
		//管理員專用 //4001036
		Array(ttt5, "#e#r提交建議及回報", 100, true, 9900007, "回報_chr")
        );

//彩虹楓葉
var feng = "#v4032733#"
var EditItem = Array();

//新手幫助
function HelpMessage() {
	var Message = "\t\t\t\t#e#dxx谷新手幫助須知#n\r\n\r\n";
		/*Message += "楓葉之心 合成\r\n";
		Message += "封印的楓葉之心+10AD = 楓葉之心+楓葉水晶*10\r\n";
		Message += "甦醒的楓葉之心+30AD = 封印的楓葉之心+楓葉水晶*30\r\n";
		Message += "覺醒的楓葉之心+40AD = 甦醒的楓葉之心+楓葉水晶*40\r\n";
		Message += "奇幻方塊 洗淺能 所需要的放大鏡\r\n";
		Message += "楓葉水晶 獲得方式:\r\n";
		Message += "1.擊殺 殘暴炎魔.獅王.熊王\r\n";
		Message += "2.新增BSPQ  OR  累積簽到獎勵\r\n";
		Message += "3.特定活動 (官方主持)\r\n";
		Message += "4.打怪獲得 (超低機率)\r\n";
		Message += "5.新增至轉淡 (機率微調)\r\n";
		Message += "放大鏡獲得方式:\r\n";
		Message += "1.增加NPC購買\r\n";
		Message += "2.特定獲得發送\r\n";
		Message += "3.BSPQ分數兌換\r\n";
		Message += "4.每日固定領取 (些微數量)\r\n";*/
		var HelpList = Array(
			Array("等 級 獎 勵","本服設有10.30.70.100.120.150.180等的額外獎勵包，每累積10等也會隨機贈送好禮，讓你練功不煩惱。"),
			Array("簽 到 系 統","只要在線上2小時，即可完成每日簽到，每日簽到即可獲得絕對音感。只要達成固定累積簽到時日，還有更優渥的禮包可以領取。"),
			Array("爆 率 系 統","只要該玩家在地圖內，點選在拍賣中的查看爆率，即可知道該怪物的掉落物品，讓玩家能夠更好找到想打的怪物。"),
			Array("絕對音感用途","點選拍賣的物品兌換，各式物品可進行兌換."),
			Array("xx幣用途","升級多類裝備，兌換裝備，依照每個階級的不同，兌換的數量也有所增加。"),
			Array("點數獲得方式","武陵道場每一關都能獲得點數，打怪也享有隨機1~5點的優惠，xx玩家邊練功邊獲取點數。"),
			Array("奇幻方塊獲得方式","自由市場內的財神，楓葉，斗內。")
		);
		for (var t in HelpList)
			Message += "#e#r"+HelpList[t][0] + ": #n#b" +HelpList[t][1] + "\r\n\r\n";
	return Message;
}

function start() {
	if (cm.getPlayer().getLevel() < 10) {
		cm.sendOk("你目前等級無法使用該功能");
		cm.dispose();
		return;
	}
	status = -1;
    action(1, 0, 0);
}

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
        else {
            cm.dispose();
            return;
        }
        if (status == 0) {
			//基本資訊
			var message = Array(
					Array(" 今天在線：#r" + cm.getOnlineTime() + "#k 分鐘"),
					Array(" 今天獲得點數：#r" + cm.getPlayer().getBossLog("NowDayCs") + "#k 點\r\n"),
					Array(" Cash：#r" + cm.getPlayer().getCSPoints(1).toString() + "#k 點"),
					Array(" 楓葉點數：#r" + cm.getPlayer().getCSPoints(2).toString() + "#k 點\r\n")
					);
            var selStr = "\t\t\t\t#e歡迎來到#rxx谷#k#n\r\n";
			for (var i = 0; i < message.length; i++)
				selStr += (i%2==0? icon + format(" ", 27, message[i].toString()) : message[i]);
			selStr += eff1 + "" + eff1 + "" + eff1 + "" + eff1 + "" + eff1 + "" + eff1 + "" + eff1 + "" + eff1 + "" + eff1 + "" + eff1 + "" + eff1 + "" + eff1 + "" + eff1 + "" + eff1 + "" + eff1 + "" + eff1 + "" + eff1 + "" + eff1 + "" + eff1 + "" + eff1 + "" + eff1 + "" + eff1 + "" + eff1 + "" + eff1 + "" + eff1 + "" + eff1 + "" + eff1 + "" + eff1 + "" + eff1;
            //功能列表
			var x = 0;
            for (var i = 0; i < menuList.length; i++) {
				x++;
				//管理員功能略過
                if (!menuList[i][3] && !cm.getChar().isGM()) {
					x--;
					continue;
				}
				//回報功能自動跳行
				if (menuList[i][5] == "回報_chr")
					selStr += "\r\n\r\n";
				else if (!menuList[i][3])
					selStr += "\t\t\t  ";
				//寫入功能表
				selStr += "#b#L" + i + "#" + menuList[i][0] + " " + menuList[i][1] + "#l";
				if (!menuList[i][3])
					x--;
				if (x % 3 == 0 && menuList[i][5] != "回報_chr")
					selStr += "\r\n";
            }
            selStr += "\r\n\r\n" + eff1 + "" + eff1 + "" + eff1 + "" + eff1 + "" + eff1 + "" + eff1 + "" + eff1 + "" + eff1 + "" + eff1 + "" + eff1 + "" + eff1 + "" + eff1 + "" + eff1 + "" + eff1 + "" + eff1 + "" + eff1 + "" + eff1 + "" + eff1 + "" + eff1 + "" + eff1 + "" + eff1 + "" + eff1 + "" + eff1 + "" + eff1 + "" + eff1 + "" + eff1 + "" + eff1 + "" + eff1 + "" + eff1;
			cm.sendSimple(selStr);
		} else if (status == 1) {
			cm.dispose();
			if (menuList[selection][5] == -1)
				cm.openNpc(menuList[selection][4]);
			else if (menuList[selection][5] == "map") {
				if (cm.getPlayer().getMapId() == 749050400) {
					cm.saveLocation("Gachapon");
					cm.warp(749050400,0);
				}
			} else if (menuList[selection][5] == "help") {
				cm.sendGetText(HelpMessage());
				cm.dispose();
			} else
				cm.openNpc(menuList[selection][4], menuList[selection][5]);
			
        }
    }
}

var format = function FormatString(c, length, content) {
    var str = "";
    var cs = "";
    if (content.length > length) {
        str = content;
    } else {
        for (var j = 0; j < length - content.getBytes("big5").length; j++) {
            cs = cs + c;
        }
    }
    str = content + cs;
    return str;
}
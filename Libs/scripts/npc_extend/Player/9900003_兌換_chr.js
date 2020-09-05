/*  
 
 NPC版權:                追憶冒險島 	        
 NPC類型: 		        綜合NPC
 製作人：故事、
 */
 
var PreviousPage = new Array(9900007, "home_chr");//上一頁
var status = -1;
//音符兌換
var itemList = Array(
		//銷售數量，物品ID ，數量，時間
		Array(1, 2022179, 2),//紫色蘋果
		Array(2, 2101120, 5),//魚怪召喚袋	
		Array(2, 1032167, 1),//音符耳環
		Array(2, 1112143, 1),//豪華珍珠名牌戒指     
		Array(2, 1112178, 1),//夢想中的雪景名牌戒指 
		Array(2, 1112177, 1),//進擊的巨人名牌戒指   
		Array(2, 1112179, 1),//白雪聖誕名牌戒指     
		Array(2, 1112167, 1),//名牌戒指（初音未來） 
		Array(2, 1112168, 1),//名牌戒指（鏡音鈴&連）
		Array(12, 1112127, 1, 1),//Welcome Back1日
		Array(12, 1122017, 1, 7) //精靈墜飾7日
        );
//BSPQ點數兌換
var ExchangeItems = Array(
		Array(1200, 5062000), //奇幻方塊
		Array(6000, 2340000), //祝福卷軸
		Array(5000, 3010574), //星光閃爍的夜晚椅子
		Array(7000, 3010454), //愛心雲朵椅
		Array(10000, 3010073), //皮卡啾椅
		Array(10000, 3010453), //乘著暴風的兔子椅
		Array(20000, 3010690), //鯊魚急流水道椅
		Array(20000, 3015121), //史烏熱愛者椅子
		Array(20000, 3015275) //跟太陽一起賞月隨機椅子
		);
//楓葉之心兌換
var MapleLeafHeart = Array(
		Array(1122024, 1122019, 10),
		Array(1122025, 1122019, 10),
		Array(1122026, 1122019, 10),
		Array(1122027, 1122019, 10),
		Array(1122028, 1122019, 10),
		
		Array(1122029, 1122024, 30),
		Array(1122030, 1122025, 30),
		Array(1122031, 1122026, 30),
		Array(1122032, 1122027, 30),
		Array(1122033, 1122028, 30),
		
		Array(1122034, 1122029, 40),
		Array(1122035, 1122030, 40),
		Array(1122036, 1122031, 40),
		Array(1122037, 1122032, 40),
		Array(1122038, 1122033, 40) 
		);
	//楓葉兌換
var itemaa = Array(
        Array(100, 2101120, 1), //魚怪包	
		Array(1000, 2101120, 10), 
        Array(150, 2450000, 1), //獵人
		Array(1500, 2450000, 10), 
		Array(800, 2049401, 1), //遣在能力
		Array(8000, 2049401, 10)

);
    //方便幣兌換
var itembb = Array(
 
        Array(1, 2340000, 2),
        Array(1, 2049401, 1), //遣在能力
		Array(2, 2049400, 1), //高級遣在能力
		Array(10, 1112763, 1),
        Array(10, 1112767, 1),
        Array(10, 1112771, 1),
        Array(10, 1112775, 1),
        Array(20, 1112127, 1), //80趴界
        Array(20, 1112956, 1) //新興戒指
	
);
//神話升階
var itemcc = Array(
		Array(1032206, 1032205, 15),
		
		
		
		
		Array(1032207, 1032206, 15),
		
		
		
		
		Array(1032208, 1032207, 10),
		
		
		
		
		Array(1032209, 1032208, 15),
		
		
		
		
		
		
		Array(1032219, 1032209, 50) 
		);
		//獨眼升階
var itemdd = Array(
		Array(1022191, 1022190, 5),
		
		
		
		
		Array(1022192, 1022191, 20),
		
		
		
		
		Array(1022193, 1022192, 20),
		
		
		
		
		Array(1022215, 1022193, 20)
		
		
		
		
		);

var seleQuantity = -1, selectedItem = -1, selectedQuantity = -1, selectedTime = -1;
var 選擇獲得 = "#fUI/UIWindow.img/icon/WorldUI/summary_icon/select#";
var ttt = "#fUI/UIWindow/Quest/icon2/7#";//"+ttt+"//美化1
var ItemId音符 = 4310000;
var ItemId楓葉 = 4001126;
var ItemId方便 = 4001157;
var ExchangeType;


function start() {
	var text = "請選擇兌換項目: #b\r\n"
		//text += "#L5#賭博#l\r\n";
		text += "#L0#音符兌換#l\r\n";
		text += "#L11#方便幣兌換#l\r\n";
		text += "#L10#楓葉兌換#l\r\n";
		text += "#L1#BSPQ點數兌換#l\r\n";
		text += "#L2#楓葉之心升級#l\r\n";
		text += "#L3#神話耳環升級#l\r\n";
		text += "#L4#獨眼耳環升級#l\r\n";
		text += "\r\n\t\t\t\t#L999#" + ttt + "返回萬能Npc\r\n";
	cm.sendSimple(text);
}

function action(mode, type, selection) { 
	//java.lang.System.err.printf("mode:%s type:%s selection:%s status:%s\r\n",mode,type,selection,status);
	if (ExchangeType == null)
		ExchangeType = selection;
    if (mode == 1) {
        status++;
    } else {
        if (status >= 0) {
            cm.dispose();
            return;
        }
        status--;
    }
	//返回萬能NPC
	if (selection == 999) {
		cm.dispose();
		cm.openNpc(PreviousPage[0], PreviousPage[1]);
		return;
	}
	//BSPQ點數兌換
	if (ExchangeType == 1) {
		var points = cm.getQuestRecord(150001).getCustomData();
		if (status == 0) {
			var text = "";
			for (var e in ExchangeItems)
				text += "#L" + e + "##v" + ExchangeItems[e][1] + ":##t" + ExchangeItems[e][1] + "# #b(#r需要: #d" + ExchangeItems[e][0] + "#b)\r\n";
			cm.sendOk("#b點數數量: " + points + "\r\n " + text);
		} else if (status == 1) {
			if (points < ExchangeItems[selection][0]) {
				cm.sendOk("點數不足需要:" + ExchangeItems[selection][0]);
			} else {
				var deduct = ExchangeItems[selection][0];
				cm.getQuestRecord(150001).setCustomData(points-deduct);
				cm.gainItem(ExchangeItems[selection][1], 1);
				cm.sendOk("兌換完成: #v" + ExchangeItems[selection][1] + "#\r\n剩餘點數: " + cm.getQuestRecord(150001).getCustomData());
			}
			cm.dispose();
		}
	//楓葉之心兌換
	} else if (ExchangeType == 2) {
		if (status == 0) {
			var text = "請選擇兌換項目:\r\n";
				text += "#b\t戰士\t法師\t弓箭手\t盜賊\t海盜"
			for (var e in MapleLeafHeart) {
				if (e%5==0)
					text += "\r\n\r\n";
				text += "#L" + e + "##i" + MapleLeafHeart[e][0] + ":##l";
				if (e%5==4)
					text += " #b#i4001157# x #r" + MapleLeafHeart[e][2]+ "";
			}
			cm.sendOk(text);
		} else if (status == 1) {
			if (cm.haveItem(MapleLeafHeart[selection][1], 1) && cm.haveItem(4001157, MapleLeafHeart[selection][2])) {
				cm.gainItem(4001157, -MapleLeafHeart[selection][2]);
				cm.gainItem(MapleLeafHeart[selection][1], -1);
				cm.gainItem(MapleLeafHeart[selection][0], 1);
				cm.sendOk("恭喜成功升級成: #b#i" + MapleLeafHeart[selection][0] + "##t" + MapleLeafHeart[selection][0] + "#");
				cm.dispose();
			} else {
				var text = "#i" + MapleLeafHeart[selection][0] + ":##b#t" + MapleLeafHeart[selection][0] + ":# #k的升級所需要的物品:\r\n";
				text += "#i" + MapleLeafHeart[selection][1] + ":# + #i4001157# x "+ MapleLeafHeart[selection][2]+"#l";
				cm.sendOk(text);
				status = -1;
			}
		}
	} else if (ExchangeType == 3) { //神話升階
		if (status == 0) {
			var text = "請選擇兌換項目:\r\n";
				
			for (var e in itemcc) {
				if (e%5==0)
					text += "\r\n\r\n";
				text += "#L" + e + "##i" + itemcc[e][0] + ":##l";
				if (e%5==4)
					text += " #b#i4001157# x #r" + itemcc[e][2]+ "";
			}
			cm.sendOk(text);
		} else if (status == 1) {
			if (cm.haveItem(itemcc[selection][1], 1) && cm.haveItem(4001157, itemcc[selection][2])) {
				cm.gainItem(4001157, -itemcc[selection][2]);
				cm.gainItem(itemcc[selection][1], -1);
				cm.gainItem(itemcc[selection][0], 1);
				cm.sendOk("恭喜成功升級成: #b#i" + itemcc[selection][0] + "##t" + itemcc[selection][0] + "#");
				cm.dispose();
			} else {
				var text = "#i" + itemcc[selection][0] + ":##b#t" + itemcc[selection][0] + ":# #k的升級所需要的物品:\r\n";
				text += "#i" + itemcc[selection][1] + ":# + #i4001157# x "+ itemcc[selection][2]+"#l";
				cm.sendOk(text);
				status = -1;
			   }
			}
		} else if (ExchangeType == 4) { //獨眼升階
		if (status == 0) {
			var text = "請選擇兌換項目:\r\n";
				
			for (var e in itemdd) {
				if (e%5==0)
					text += "\r\n\r\n";
				text += "#L" + e + "##i" + itemdd[e][0] + ":##l";
				if (e%5==4)
					text += " #b#i4001157# x #r" + itemdd[e][2]+ "";
			}
			cm.sendOk(text);
		} else if (status == 1) {
			if (cm.haveItem(itemdd[selection][1], 1) && cm.haveItem(4001157, itemdd[selection][2])) {
				cm.gainItem(4001157, -itemdd[selection][2]);
				cm.gainItem(itemdd[selection][1], -1);
				cm.gainItem(itemdd[selection][0], 1);
				cm.sendOk("恭喜成功升級成: #b#i" + itemdd[selection][0] + "##t" + itemdd[selection][0] + "#");
				cm.dispose();
			} else {
				var text = "#i" + itemdd[selection][0] + ":##b#t" + itemdd[selection][0] + ":# #k的升級所需要的物品:\r\n";
				text += "#i" + itemdd[selection][1] + ":# + #i4001157# x "+ itemdd[selection][2]+"#l";
				cm.sendOk(text);
				status = -1;
			}
		}
	} else if (ExchangeType == 5) {
		var tiem = Array("單數","雙數");
		var text = "";
		for (var t in tiem)
			text += "#L" + t + "#" + tiem[t] + "#l\t";
		cm.sendOk(text);
		cm.dispose();
	//音符兌換
	} else if (ExchangeType == 0) {
		if (status == 0) {
			音符 = cm.itemQuantity(ItemId音符);
			var selStr = 選擇獲得 + "\r\n#r注意事項#k：每週的獎勵將會不定時變動喔。\r\n已收集#v" + ItemId音符 + "##b#z" + ItemId音符 + "##k: " + cm.itemQuantity(ItemId音符) + " 個";
			for (var i = 0; i < itemList.length; i++) {
				selStr += "\r\n#L" + i + "##i" + itemList[i][1] + ":# #b#t" + itemList[i][1] + "# " + (itemList[i][3] == null ? "" : itemList[i][3] + " 日") + "#r x" + itemList[i][2] + "#b 〔#d需要 #v" + ItemId音符 + "#x" + itemList[i][0] + "#b〕#l";
			}
			cm.sendSimple(selStr);
		} else if (status == 1) {
			var item = itemList[selection];
			if (item != null) {
				//音符需要數量
				seleQuantity = item[0];
				//物品
				selectedItem = item[1];
				//數量
				selectedQuantity = item[2];
				//物品時間
				selectedTime = item[3];
				cm.sendYesNo("兌換 #i" + selectedItem + "#x" + selectedQuantity + " 需要 #r" + seleQuantity + "個 #v" + ItemId音符 + "##k 你確定兌換嗎?");
			} else {
				cm.sendOk("兌換出錯,請聯繫管理員...");
				cm.dispose();
			}
		} else if (status == 2) {
			if (seleQuantity <= 0 || selectedItem <= 0) {
				cm.sendOk("兌換出錯,請聯繫管理員...");
				cm.dispose();
				return;
			}
			if (cm.itemQuantity(ItemId音符) >= seleQuantity) {
				if (cm.canHold(selectedItem, selectedQuantity)) {
					cm.gainItem(ItemId音符, -seleQuantity);
					cm.gainItemPeriod(selectedItem, selectedQuantity, (selectedTime == null ? 0: selectedTime));
					cm.sendOk("兌換成功,商品#i" + selectedItem + ":# #b#t" + selectedItem + "##k已送往背包。");
				} else {
					cm.sendOk("背包所有欄目窗口有一格以上的空間才可以進行兌換。");
				}

			} else {
				cm.sendOk("#r你沒有足夠的音符。#k\r\n\r\n兌換#i" + selectedItem + ":# #b#t" + selectedItem + "##k 需要 #r" + seleQuantity + "個 #i" + ItemId音符 + "##k。");
			}
			status = -1;
	         } 
		   
		} else if (ExchangeType == 10) {  //楓葉兌換
		if (status == 0) {
			楓葉 = cm.itemQuantity(ItemId楓葉);
			var selStr = 選擇獲得 + "\r\n#r注意事項#k：每週的獎勵將會不定時變動喔。\r\n已收集#v" + ItemId楓葉 + "##b#z" + ItemId楓葉 + "##k: " + cm.itemQuantity(ItemId楓葉) + " 個";
			for (var i = 0; i < itemaa.length; i++) {
				selStr += "\r\n#L" + i + "##i" + itemaa[i][1] + ":# #b#t" + itemaa[i][1] + "# " + (itemaa[i][3] == null ? "" : itemaa[i][3] + " 日") + "#r x" + itemaa[i][2] + "#b 〔#d需要 #v" + ItemId楓葉 + "#x" + itemaa[i][0] + "#b〕#l";
			}
			cm.sendSimple(selStr);
		} else if (status == 1) {
			var item = itemaa[selection];
			if (item != null) {
				//音符需要數量
				seleQuantity = item[0];
				//物品
				selectedItem = item[1];
				//數量
				selectedQuantity = item[2];
				//物品時間
				selectedTime = item[3];
				cm.sendYesNo("兌換 #i" + selectedItem + "#x" + selectedQuantity + " 需要 #r" + seleQuantity + "個 #v" + ItemId楓葉 + "##k 你確定兌換嗎?");
			} else {
				cm.sendOk("兌換出錯,請聯繫管理員...");
				cm.dispose();
			}
		} else if (status == 2) {
			if (seleQuantity <= 0 || selectedItem <= 0) {
				cm.sendOk("兌換出錯,請聯繫管理員...");
				cm.dispose();
				return;
			}
			if (cm.itemQuantity(ItemId楓葉) >= seleQuantity) {
				if (cm.canHold(selectedItem, selectedQuantity)) {
					cm.gainItem(ItemId楓葉, -seleQuantity);
					cm.gainItemPeriod(selectedItem, selectedQuantity, (selectedTime == null ? 0: selectedTime));
					cm.sendOk("兌換成功,商品#i" + selectedItem + ":# #b#t" + selectedItem + "##k已送往背包。");
				} else {
					cm.sendOk("背包所有欄目窗口有一格以上的空間才可以進行兌換。");
				}

			} else {
				cm.sendOk("#r你沒有足夠的楓葉。#k\r\n\r\n兌換#i" + selectedItem + ":# #b#t" + selectedItem + "##k 需要 #r" + seleQuantity + "個 #i" + ItemId楓葉 + "##k。");
			}
			status = -1;
	    }
	} else if (ExchangeType == 11) {  //方便幣兌換
		if (status == 0) {
			方便 = cm.itemQuantity(ItemId方便);
			var selStr = 選擇獲得 + "\r\n#r注意事項#k：每週的獎勵將會不定時變動喔。\r\n已收集#v" + ItemId方便 + "##b#z" + ItemId方便 + "##k: " + cm.itemQuantity(ItemId方便) + " 個";
			for (var i = 0; i < itemaa.length; i++) {
				selStr += "\r\n#L" + i + "##i" + itembb[i][1] + ":# #b#t" + itembb[i][1] + "# " + (itembb[i][3] == null ? "" : itembb[i][3] + " 日") + "#r x" + itembb[i][2] + "#b 〔#d需要 #v" + ItemId方便 + "#x" + itembb[i][0] + "#b〕#l";
			}
			cm.sendSimple(selStr);
		} else if (status == 1) {
			var item = itembb[selection];
			if (item != null) {
				//音符需要數量
				seleQuantity = item[0];
				//物品
				selectedItem = item[1];
				//數量
				selectedQuantity = item[2];
				//物品時間
				selectedTime = item[3];
				cm.sendYesNo("兌換 #i" + selectedItem + "#x" + selectedQuantity + " 需要 #r" + seleQuantity + "個 #v" + ItemId方便 + "##k 你確定兌換嗎?");
			} else {
				cm.sendOk("兌換出錯,請聯繫管理員...");
				cm.dispose();
			}
		} else if (status == 2) {
			if (seleQuantity <= 0 || selectedItem <= 0) {
				cm.sendOk("兌換出錯,請聯繫管理員...");
				cm.dispose();
				return;
			}
			if (cm.itemQuantity(ItemId方便) >= seleQuantity) {
				if (cm.canHold(selectedItem, selectedQuantity)) {
					cm.gainItem(ItemId方便, -seleQuantity);
					cm.gainItemPeriod(selectedItem, selectedQuantity, (selectedTime == null ? 0: selectedTime));
					cm.sendOk("兌換成功,商品#i" + selectedItem + ":# #b#t" + selectedItem + "##k已送往背包。");
				} else {
					cm.sendOk("背包所有欄目窗口有一格以上的空間才可以進行兌換。");
				}

			} else {
				cm.sendOk("#r你沒有足夠的方便幣。#k\r\n\r\n兌換#i" + selectedItem + ":# #b#t" + selectedItem + "##k 需要 #r" + seleQuantity + "個 #i" + ItemId方便 + "##k。");
			}
			status = -1;
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
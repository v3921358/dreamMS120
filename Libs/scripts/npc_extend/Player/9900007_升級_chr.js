/*      
 *  
 *  功能：升級禮包領取
 *  
 */
 
 var PreviousPage = new Array(9900007, "home_chr");//上一頁
﻿var status = 0;
var eff = "#fEffect/CharacterEff/1051296/1/0#";
var ttt = "#fUI/UIWindow/Quest/icon9/0#";////美化2
var ttt1 = "#fUI/UIWindow/Quest/icon3/0#";////完成
var ttt2 = "#fUI/UIWindow/Quest/icon7/0#";////完成
var Progress = "#fUI/UIWindow/Quest/Tab/enabled/1#";////進行中
var Complete = "#fUI/UIWindow/Quest/Tab/enabled/2#";////完成
var giftContent = Array(
        Array(0, Array(
                //物品ID ，數量
                Array(2450000, 3), //獵人
                Array(2022179, 3), //紫色蘋果
                Array(5220000, 20), //轉蛋
                Array(2000005, 50), //超級藥水
                Array(4310000, 3), //音符
                Array(1122017, 1, false, 1), //精靈墜飾 1 日
                Array(1112127, 1, false, 1000*60*60*2) //80%戒指 2 小時
                )),
        Array(10, Array(
                //物品ID ，數量
				Array(-1, 2000000, true), //楓幣
                Array(5072000, 10), //喇叭
                Array(2430049, 1), //紀念冒險家歸來的禮品箱
                Array(1142444, 1), //方便谷專用勳章
				Array(1032205, 1) //神話
                )),
        Array(30, Array(
				Array(-2, 500, true), //點數
                Array(4001126, 500)//楓葉
                )),
		Array(50, Array(
				Array(-2, 500, true), //點數
                Array(1022190, 1)//獨眼1
                )),
        Array(70, Array(
				Array(-2, 666, true), //點數
				Array(1702455, 1, false, 7, 1, 5), //紅色油漆
                Array(2450000, 10),//獵人
                Array(5220000, 25)//轉蛋卷
                )),
        Array(100, Array(
				Array(-2, 888, true), //點數
				Array(4280001, 5),//銀寶箱
				Array(5490001, 5)//銀鑰匙
                )),
        Array(120, Array(
				Array(-2, 1288, true), //點數
				Array(2022179, 5),//紫色蘋果
				Array(2450000, 10)//獵人
                )),
        Array(150, Array(
				Array(-2, 1500, true), //點數
				Array(1142790, 1), //104期生勳章
				Array(4280001, 10),//銀寶箱
				Array(4280001, 10)//銀鑰匙
                )),
        Array(180, Array(
				Array(-2, 3000, true), //點數
				Array(5220000, 100) //轉蛋卷
                ))
        //可以再添加一些禮包  名字後面的數字是 連續簽到的天數       
        );

var giftId = -1;
var gifts = null;
var lvl = 999;
var levelR = -1;
var Space = -1;
var ttt = "#fUI/UIWindow/Quest/icon2/7#";//"+ttt+"//美化1

function start() {
    status = -1;
    action(1, 0, 0);
}

function gettime(itemid) {
    switch (itemid) {
        case 5211047:
        case 5360014:
            return 3 * 60 * 60 * 1000;
        case 1112100:
        case 5000008:
            return 30 * 24 * 60 * 60 * 1000;
        default:
            return -1;
    }
}

function bossid(lvl) {
	return "升級獎勵" + lvl;
}

function bossidEveryTen(lvl) {
	return "每級獎勵" + lvl;
}
function game_item_handling() {
	
}
function action(mode, type, selection) {
    if (status == 0 && mode == 0) {
        cm.dispose();
        return;
    }
	//驗證是否要領取禮包
	if (type == 4 && selection != 1) {
		status = -1;
	}
    if (mode == 1) {
        status++;
    } else {
        status--;
    }
    if (status == 0) {
        var text = "\t\t\t\t#e歡迎來到方便楓之谷                  #r"
			text+="#d#e注意事項：系統紀錄外掛請不要抱著僥倖心理使用！#n#k\r\n";
        text += "#e#b點擊可以查看禮包內的物品哦！#n\r\n";
		//每十級獎勵
		levelR = parseInt(cm.getPlayer().getLevel() / 10) * 10;
		var checklvl = cm.getLog(bossidEveryTen(levelR-10), false) >= 1;
		if (!checklvl && cm.getPlayer().getLevel()%10 == 0 && levelR > 10)
			levelR = (parseInt(cm.getPlayer().getLevel() / 10) - 1) * 10;
		//列出等級獎勵選項
        for (var key in giftContent) {
            var get = cm.getLog(bossid(giftContent[key][0]), false) >= 1;
			var can = cm.getPlayer().getLevel() >= giftContent[key][0];
			//每十級獎勵
			if (key == 0) {
				get = cm.getLog(bossidEveryTen(levelR), false) >= 1;
				can = cm.getPlayer().getLevel() >= levelR;
				text += "#d#e#L" + key + "#" + (can ? get ? ttt1 : ttt2 : ttt) + " 等級達到#r " + levelR + " #d級！#k" + (can ? get ? Complete : "未領取" : Progress) + "#n#r#l(每十級獎勵)#b#l#k\r\n";
			} else {
				text += "#b#e#L" + key + "#" + (can ? get ? ttt1 : ttt2 : ttt) + " 等級達到#r " + giftContent[key][0] + " #b級！#k" + (can ? get ? Complete : "未領取" : Progress) + "#n#b#l#k\r\n";
			}
		if (!get || !can)
			text += "\r\n"
        }
		text += "\t\t\t\t#L999#"+ "#b" + ttt + "返回上一頁";
        cm.sendSimple(text);
    } else if (status == 1) {
		//返回上一頁
		if (selection == 999) {
			cm.dispose();
			cm.openNpc(PreviousPage[0], PreviousPage[1]);
			return;
		}
        giftId = parseInt(selection);
        lvl = giftContent[giftId][0];//等級
        gifts = giftContent[giftId][1];//禮包
        var text = "#r#e" + giftContent[giftId][0] + " #b級獎勵包如下#n#b：";
		//每十級獎勵
		if (giftId == 0)
			text = "#e#b每#r 10 #b級獎勵包如下#n#r (隨機獲得一樣)#b：";
		var txt = true;
		//禮包內的每項物品
        for (var key in gifts) {
            var itemId = gifts[key][0];
            var itemQuantity = gifts[key][1];
			var limitAcc = gifts[key][2];
			var period = gifts[key][3];
			var Ability = gifts[key][4]; //給予屬性
			var Attack = gifts[key][5]; //給予傷害
			if (period > 1000)
				period = " #k(" + period / (1000*60*60) + "H)";
			else if (period == null)
				period = "";
			else
				period = " #k(" + period + "天)";
			if (itemId == -1) {
				text += "#b楓幣#k #rx " + itemQuantity + (limitAcc ? " #d(帳號限定)" : "") + "#k\t";
			} else if (itemId == -2) {
				text += "#b楓葉點數#k #rx " + itemQuantity + (limitAcc ? " #d(帳號限定)" : "") + "#k\t";
			} else if (cm.gainIItem(itemId) == 0) {
				if (txt) text +="\r\n";
				txt = false;
				text += "#i" + itemId + ":##b#z" + itemId + "##k #rx " + itemQuantity + period + (Ability==null?"":"#r (全屬:"+Ability+" 傷害:"+Attack+")") + (limitAcc ? " #d(帳號限定)" : "") + "#k\r\n";
			} else {
				text += "#r#fUI/UIWindow.img/QuestIcon/5/0# (無名稱)#k\r\n";
			}
        }
        text += "\r\n#d#e#r是否領取等級禮包？ #b(返回輸入: 0 領取輸入: 1)#k";
        cm.sendGetNumber(text, 0, 0, 1);
    } else if (status == 2) {
		var 每十級 = (giftId == 0);
		var isReceive = true; //領取驗證
        if (giftId != -1 && gifts != null) {
			Space = (每十級 ? 1 : giftContent[giftId][1].length);
			if (cm.getPlayer().getSpace(1) < Space || cm.getPlayer().getSpace(2) < Space || cm.getPlayer().getSpace(3) < Space || cm.getPlayer().getSpace(4) < Space || cm.getPlayer().getSpace(5) < Space) {
				cm.sendOk("#b您的背包空間不足，請保證每個欄位至少"+Space+"格的空間，以避免領取失敗。");
				isReceive = false;
				status = -1;
			}
			if (cm.getPlayer().getLevel() < (每十級 ? levelR : lvl)) {
				cm.sendOk("您還達到#r" + ( 每十級 ? levelR : lvl) + "#k級呀，請再接再厲。");
				isReceive = false;
				status = -1;
			}
			if (cm.getLog((每十級 ? bossidEveryTen(levelR) : bossid(giftContent[giftId][0])), false) > 0) {
				cm.sendOk("你已經領取過了這個禮包了！");
				isReceive = false;
				status = -1;
			}
			//領取驗證
			if (isReceive) {
				if (giftId == 0) {
					//每十級獎勵
					var key = Math.floor(Math.random() * giftContent[giftId][1].length);
					var itemId = gifts[key][0];
					var itemQuantity = gifts[key][1];
					var limitAcc = gifts[key][2];
					var time = gifts[key][3];
					time = (time == null ? -1 : time);
					if (itemId == -1) {
						cm.gainMeso(itemQuantity);
					} else if (itemId == -2) {
						cm.getPlayer().modifyCSPoints(2, itemQuantity, true);
					} else if (itemId == 5000008) {
						cm.gainPetItem(itemId, itemQuantity, 28);
					} else {
						cm.gainItem(itemId, itemQuantity, time);
					}
					cm.setLog(bossidEveryTen(levelR));
					var period = 0;
					if (time > 1000)
						period = (time/(1000*60*60)) + " #b小時";
					else
						period = time + " #b天";
					cm.sendOk("恭喜您，領取禮包成功！\r\n獲得 #b#i" + itemId + ":# #t" + itemId + ":# x " + itemQuantity + (time == -1 ? "" : " (期限#r " + period + ")"));
					cm.dispose();
				} else {
					//升級獎勵
					for (var key in gifts) {
						var itemId = gifts[key][0]; //物品ID
						var itemQuantity = gifts[key][1]; //物品數量
						var limitAcc = gifts[key][2]; //是否帳號限定
						var times = gifts[key][3]; //時間限制
						var Ability = gifts[key][4]; //給予屬性
						var Attack = gifts[key][5]; //給予傷害
						var time = gettime(itemId); //時間限制
						if (limitAcc && cm.getLog(bossid(giftContent[giftId][0]), true) > 0) {
							continue;
						}
						if (itemId == -1) {
							cm.gainMeso(itemQuantity);
						} else if (itemId == -2) {
							cm.getPlayer().modifyCSPoints(2, itemQuantity, true);
						} else if (itemId == 5000008) {
							cm.gainPetItem(itemId, itemQuantity, 28);
						} else {
							if (Ability == null)
								cm.gainItem(itemId, itemQuantity, time);
							else
								AddRandomizeStats(itemId, Ability, Attack, times);
						}

					}
					cm.setLog(bossid(giftContent[giftId][0]));
					cm.sendOk("恭喜您，領取禮包成功！");
					cm.dispose();
				}
			}
        } else {
            cm.sendOk("兌換錯誤！請聯繫管理員！");
            cm.dispose();
        }
    }
}

//附加裝備能力
function AddRandomizeStats(ItemId, SDIL, MW, Time) {
	var ii = cm.getItemInfo();
	var toDrop = ii.randomizeStats(ii.getEquipById(ItemId)).copy(); //生成一個Equip類
	toDrop.setStr(SDIL); //力量
	toDrop.setDex(SDIL); //敏捷
	toDrop.setInt(SDIL); //智力
	toDrop.setLuk(SDIL); //幸運
	toDrop.setMatk(MW); //物理攻擊
	toDrop.setWatk(MW); //魔法攻擊
	cm.setExpiration(toDrop, Time); //給予到期時間
	cm.addFromDrop(toDrop);
}
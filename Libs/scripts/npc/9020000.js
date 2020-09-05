/* ===========================================================
			註釋(cm.sendSimple\cm.itemQuantity(5420008))
	腳本類型: 		NPC
	所在地圖:		廢棄都市
	腳本名字:		廢棄都市組隊任務 - 拉克裡斯
=============================================================
製作時間：2010年8月6日 11:38:22
製作人員：筆芯（國外腳本翻譯）
=============================================================
*/

var status = 0;
var minLevel = 21;
var maxLevel = 200;
var minPlayers = 1;
var maxPlayers = 6;

function start() {
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
		else
			status--;
		if (status == 0) {
			cm.sendSimple("#e<組隊任務：組隊挑戰任務>#n\r\n你想和隊員們一起努力，完成任務嗎？這裡面有很多如果不同心協力就無法解決的障礙……如果想挑戰的話，請讓#b所屬組隊的隊長#k來和我說話。\r\n#b#L0#我想執行組隊任務。#l\r\n#L1#我想尋找一起遊戲的隊員。#l\r\n#L2#我想聽一下說明。#l")
		}else if (status == 1){
			if (selection == 0){//協同組隊員一起進入組隊訓練場。
			cm.sendNext("你準備好了嗎？確定進入組隊訓練場？")
			}else if (selection == 1){
					cm.sendOk("目前此功能尚未開放。")
					cm.dispose();
			}else if (selection == 2){
						cm.sendNext("我正在等待勇敢的冒險家。請大家用自己的力量和智慧，一起破解難題，擊退強大的#r#o9300003##k！通過「獲取和正確答案數字相等的通行證」和「猜猜正確答案位置」等問答後 ，#o9300003#就會出現。\r\n - #e限制時間#n：30人中\r\n - #e參加人數#n：4人\r\n - #e獲得物品#n： #i1072369:# #t1072369# #b(掉落#o9300003#)#k\r\n                    各種消耗、其他、裝備物品\r\n")
							cm.dispose();
			}//selection
		}else if (status == 2){
			if (cm.getParty() == null) { // no party
				cm.sendOk("裡面的世界很危險，不能一個人單獨進行。\r\n#b（請組隊後再和我談話。）");
				cm.dispose();
                                return;
			}
			if (!cm.isLeader()) { // not party leader
				cm.sendSimple("請你的組隊長和我講話。");
				cm.dispose();
                        }
			else {
				// check if all party members are within 21-200 range, etc.
				var party = cm.getParty().getMembers();
				var mapId = cm.getChar().getMapId();
				var next = true;
				var levelValid = 0;
				var inMap = 0;
				// Temp removal for testing
				for (var i = 0; i < party.size(); i++) {
					if ((party.get(i).getLevel() >= minLevel) && (party.get(i).getLevel() <= maxLevel)) {
						levelValid += 1;
					}
					if (party.get(i).getMapid() == mapId) {
						inMap += 1;
					}
				}
				if (party.size() < minPlayers || party.size() > maxPlayers) 
					next = false;
				else if (levelValid < minPlayers || inMap < minPlayers) {
						next = false;
				}
				if (next) {
					// Kick it into action.  Lakelis says nothing here, just warps you in.
					var em = cm.getEventManager("KerningPQ");
					if (em == null) {
						cm.sendOk("#r錯誤：#k此副本沒有正確開放，或者系統錯誤，請聯繫管理員。");
					}
					else {
						// Begin the PQ.
						em.startInstance(cm.getParty(),cm.getChar().getMap());
						// Remove pass/coupons
						party = cm.getChar().getEventInstance().getPlayers();
						cm.removeFromParty(4001008, party);
						cm.removeFromParty(4001007, party);
					}
					cm.dispose();
				}
				else {
					cm.sendOk("請確認您的組隊：#b\r\n\r\n組隊成員沒有達到 "+minPlayers+" 名。\r\n組隊成員有 " + levelValid.toString() + " 人不在此副本的等級範圍。\r\n組隊成員有 " + inMap.toString() + " 人不在廢棄都市。\r\n\r\n（#r如果仍然錯誤, 重新下線,再登陸 或者請重新組隊。#k#b）");
					cm.dispose();
				}
			}//進入部分，包括判斷組隊	
		}//status
	}
}
					

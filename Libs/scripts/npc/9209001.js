/*
* 賭博21點
*
*
*/

var text = "";
var text1 = "";
var score = 0;
var selectionMeso = 0;
var zhuangScore = 0;
var xianScore = 0;
var paiArray = Array("A",2,3,4,5,6,7,8,9,10,"J","Q","K");
var paiArray2 = Array(1,2,3,4,5,6,7,8,9,10,10,10,10);
var ran = -1;


function start() {
	status = -1;
	
	action(1, 0, 0);
}

function action(mode, type, selection) {
	if (mode == -1) {
		cm.dispose();
	}
	else {
		if (status == 0 && mode == 0) {
		
			cm.sendOk("歡迎下次光臨。");
			cm.dispose();
			return;                    
		}
		if (mode == 1) {
			status++;
		}
		else {
			 if (status == 0) {
                cm.sendNext("歡迎下次光臨。");
                cm.dispose();
            }
			status--;
		} 
		if (status == 0) {
			score = cm.getMeso();
			text += "#fUI/UIWindow/Quest/icon3/6# ";
			text += "我這裡可以玩21點,";
			text += "您當前剩餘楓幣為 #r" + score + "#k";
			text += " #fUI/UIWindow/Quest/icon3/6#\r\n";
			text += "#L0# 開始遊戲 #l\r\n";
			text += "#L1# 遊戲說明 #l\r\n";
			cm.sendSimple(text);
		} 
		else if (status == 1) { 
			
			if(selection == 0){		
	
				cm.sendGetNumber("賭友你要下注多少？\r\n", 1, 1000000,200000000);
				
			}else if(selection == 1){
				text1 += "#fUI/UIWindow/Quest/icon3/6# ";
				text1 += "玩法說明：";
				text1 += " #fUI/UIWindow/Quest/icon3/6#\r\n";
				text1 += "    莊家的點數會在15點-22點之間（莊家也有可能爆掉），你有4次要牌的機會，如果您的點數大於莊家的點數即贏得你下注的金額，如果大於21點或者小於等於莊家的點數，即輸掉你所下注的金額，10、J、Q、K都算十點，小賭怡情，大賭傷人，請慎重。";
				cm.sendOk(text1);
				cm.dispose();
			}
		}
		//-------------第一次要牌-----------------//
		else if (status == 2) {
			selectionMeso = selection;
			if(selectionMeso > score){
				cm.sendOk("找人借錢去 來坯阿。。。");
				cm.dispose();
			}else{
				cm.gainMeso(-selectionMeso);
				zhuangScore = parseInt(Math.random()*6+16); 
				ran = parseInt(Math.random()*paiArray.length);
				xianScore = paiArray2[ran];
				var text2 = "";
				text2 += "拿到了 #r#e" + paiArray[ran] + "#n#k,您目前的點數為： #r#e" + xianScore + "#n#k ，";
				text2 += "您繼續要牌嗎？ \r\n";
				text2 += "#L2# 要  \r\n";
				text2 += "#L3# 不要  \r\n";
				cm.sendSimple(text2);
			}
			
		}
		
		//------------第二次要牌------------------//
		
		else if (status == 3) { 
			if(selection == 2){
				ran = parseInt(Math.random()*paiArray.length);
				xianScore += paiArray2[ran];
				if(xianScore>21){
					cm.sendOk("對不起，您的數字大於21，您輸了。");
				//	cm.gainMeso(-selectionMeso);
					cm.dispose();	
				}else{
					text2 = "";
					text2 += "拿到了 #r#e" + paiArray[ran] + "#n#k,您目前的點數為： #r#e" + xianScore + "#n#k ，";
					text2 += "您繼續要牌嗎？ \r\n";
					text2 += "#L4# 要  \r\n";
					text2 += "#L5# 不要  \r\n";
					cm.sendSimple(text2);
				}
			}
			else if(selection == 3){
				if((xianScore<22 && xianScore<=zhuangScore && zhuangScore<22) || (xianScore>=22 && zhuangScore>=22)){
					cm.sendOk("莊家的點數為 #r#e"+zhuangScore+"#n#k,您的點數為 #r#e"+xianScore+"#n#k,您輸了。");
				//	cm.gainMeso(-selectionMeso);
					cm.dispose();
				}else{
					cm.sendOk("#fUI/UIWindow/Quest/reward#\r\n  莊家的點數為 #r#e"+zhuangScore+"#n#k,您的點數為 #r#e"+xianScore+"#n#k,您贏了。");
					cm.gainMeso(selectionMeso*2);
					cm.dispose();	
				}
			}
			
			
		}
		//-----------第三次要牌---------------
		else if (status == 4) { 
			if(selection == 4){
				ran = parseInt(Math.random()*paiArray.length);
				xianScore += paiArray2[ran];
				if(xianScore>21){
					cm.sendOk("對不起，您的數字大於21，您輸了。");
				//	cm.gainMeso(-selectionMeso);
					cm.dispose();	
				}else{
					text2 = "";
					text2 += "拿到了 #r#e" + paiArray[ran] + "#n#k,您目前的點數為： #r#e" + xianScore + "#n#k ，";
					text2 += "您繼續要牌嗎？ \r\n";
					text2 += "#L6# 要  \r\n";
					text2 += "#L7# 不要  \r\n";
					cm.sendSimple(text2);
				}
			}
			else if(selection == 5){
				if((xianScore<22 && xianScore<=zhuangScore && zhuangScore<22) || (xianScore>=22 && zhuangScore>=22)){
					cm.sendOk("莊家的點數為 #r#e"+zhuangScore+"#n#k,您的點數為 #r#e"+xianScore+"#n#k,您輸了。");
				//	cm.gainMeso(-selectionMeso);
					cm.dispose();
				}else{
					cm.sendOk("#fUI/UIWindow/Quest/reward#\r\n  莊家的點數為 #r#e"+zhuangScore+"#n#k,您的點數為 #r#e"+xianScore+"#n#k,您贏了。");
					cm.gainMeso(selectionMeso*2);
					cm.dispose();	
				}
			}
			
			
		}
		
		//--------第四次要牌----------
		else if (status == 5) { 
			if(selection == 6){
				ran = parseInt(Math.random()*paiArray.length);
				xianScore += paiArray2[ran];
				if(xianScore>21){
					cm.sendOk("對不起，您的數字大於21，您輸了。");
				//	cm.gainMeso(-selectionMeso);
					cm.dispose();	
				}else{
					if((xianScore<22 && xianScore<=zhuangScore && zhuangScore<22) || (xianScore>=22 && zhuangScore>=22)){
						cm.sendOk("莊家的點數為 #r#e"+zhuangScore+"#n#k,您的點數為 #r#e"+xianScore+"#n#k,您輸了。");
				//		cm.gainMeso(-selectionMeso);
						cm.dispose();
					}else{
						cm.sendOk("#fUI/UIWindow/Quest/reward#\r\n  莊家的點數為 #r#e"+zhuangScore+"#n#k,您的點數為 #r#e"+xianScore+"#n#k,您贏了。");
						cm.gainMeso(selectionMeso*2);
						cm.dispose();	
					}
				}
				
			}
			else if(selection == 7){
				if((xianScore<22 && xianScore<=zhuangScore && zhuangScore<22) || (xianScore>=22 && zhuangScore>=22)){
					cm.sendOk("莊家的點數為 #r#e"+zhuangScore+"#n#k,您的點數為 #r#e"+xianScore+"#n#k,您輸了。");
			//		cm.gainMeso(-selectionMeso);
					cm.dispose();
				}else{
					cm.sendOk("#fUI/UIWindow/Quest/reward#\r\n  莊家的點數為 #r#e"+zhuangScore+"#n#k,您的點數為 #r#e"+xianScore+"#n#k,您贏了。");
					cm.gainMeso(selectionMeso*2);
					cm.dispose();	
				}
			}
			
			
		}
		//------------結束-------------
		
	}
}
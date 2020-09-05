
var status = -1;

function start(mode, type, selection) {
    status++;
	if (mode != 1) {
	    if(type == 1 && mode == 0)
		    status -= 2;
		else{
			qm.sendNext("You don't believe me? Grrrrr, you're getting me mad!");
			qm.dispose();
			return;
		}
	}
	if (status == 0)
		qm.sendNext("終於醒了！呼呼～這正是世上的空氣。呵呵，那正是太陽！那是樹木！那是草！還有那裡是花！太棒了！比我在蛋裡面想像的還要棒！還有…咦？你是我的主人嗎？…可是跟我想像的不一樣？");
	if (status == 1)
		qm.PlayerToNpc("#b嗚嗚嗚嗚哇啊啊！你會說話？");
	if (status == 2)
		qm.sendNextPrev("…我的主人是個怪人。已經訂了契約，現在又不能換其他主人。哎呀！以後多多關照。");
	if (status == 3)
		qm.PlayerToNpc("#b咦？你在說什麼啊？ 以後請多多關照…？ 契約？那是什麼？");
	if(status == 4)
		qm.sendNextPrev("什麼話…你幫我從蛋裡面孵出來所以就算締結了契約！ 那麼你就是我的主人。當然要好好照顧我，幫助我成為更強大的龍。不是嗎？");
	if (status == 5)
		qm.PlayerToNpc("#b啊啊啊？ 龍？你叫做龍嗎？我根本不知道你在說什麼！契約又是什麼？ 主人又是什麼？");
	if (status == 6)
		qm.sendNextPrev("咦？你在說什麼？你和我簽訂了龍和人類的靈魂合而為一的契約，不是嗎？因使你當然就成為主人。連這個也不知道就和我簽訂契約嗎？可是已經太遲了。契約絕對無法解約。");
	if (status == 7)
		qm.PlayerToNpc("#b啊啊啊啊啊！喂，等一下！我雖然不太清楚，可是你的意思是說…我要無條件照顧你嗎？");
	if (status == 8)
		qm.sendNextPrev("當然啊！…咦！ 什麼？為什麼一副哀怨的樣子？你不想當我的主人嗎？");
	if (status == 9)
		qm.PlayerToNpc("#b不是啦！也不是不願意，可是我不曉得能不能養寵物…");
	if (status == 10)
		qm.sendNextPrev("寵，寵物~？ 你把我當做寵物？你到底把我當什麼啦？我好歹也是世界上最厲害的生命體 - 龍！");
	if (status == 11)
		qm.PlayerToNpc("#b…（我怎麼看都覺得像隻小蜥蜴。）");
	if (status == 12)
		qm.sendAcceptDecline("為什麼用那種眼神？該不會覺得我看起來像隻小蜥蜴吧？唉呀呀！真是令人無法忍受！我證明我的力量給你看！你有覺悟了嗎？");
	if (status == 13){
		qm.forceStartQuest();
		qm.sendNext("立刻攻擊#r#o1210100##k吧！ 我會喚醒你的魔力，證明身為龍的我的能力！ 好，突擊！");
	}if (status == 14)
		qm.sendNextPrev("不、不對，慢著！ 在那之前先分配AP吧？ 魔法會受#bINT和LUK#k影響！ 所以要確實分配AP，裝備#b魔法師裝備#k後戰鬥！");
	if (status == 15){
		qm.evanTutorial("UI/tutorial/evan/11/0", -1);
		qm.dispose();
	}
}

function end(mode, type, selection) {
   	status++;
	if (mode != 1) {
	    if(type == 1 && mode == 0)
		    status -= 2;
		else{
		    qm.dispose();
			return;
		}
	}
	if(status == 0)
		qm.sendOk("Ha! What do you think of that?! My skills are amazing, right? You can use them as much as you want. That's what it means to be in a pact with me. Isn't it amazing?");
	if(status == 1){
		qm.forceCompleteQuest();
		qm.gainExp(1270);
		qm.getPlayer().gainSP(1, 0);
		qm.sendOk("Ohhh... I'm so hungry. I used my energy too soon after being born...");
		qm.dispose();
	}
}
var status = -1;

function start(mode, type, selection) {
    status++;
	if (mode != 1) {
	    if(type == 1 && mode == 0)
		    status -= 2;
		else{
			qm.sendNext("Hm? Don't you want to tell Utah? You have to be nice to your brother, dear.");
			qm.dispose();
			return;
		}
	}
	if (status == 0)
		qm.sendNext("睡得好嗎，#h0#？");
	else if (status == 1)
		qm.PlayerToNpc("#b嗯…媽媽也睡得好嗎？#k");
	else if (status == 2)
		qm.sendNextPrev("對了... 你昨天晚上似乎沒有睡得很好。是因為昨晚雷聲轟隆隆閃電交加的緣故。是這樣嗎？");
	else if (status == 3) 
		qm.PlayerToNpc("#b不是！不是啦！我昨晚做了一個奇怪的夢。#k");
	else if (status == 4)
		qm.sendNextPrev("奇怪的夢？你做了什麼夢？");
	else if (status == 5)
		qm.PlayerToNpc("#b就是啊…#k");
	else if (status == 6)
		qm.PlayerToNpc("#b(說明了在霧中遇見龍的夢。)");
	else if (status == 7)
		qm.sendAcceptDecline("呵呵呵呵，龍嗎？真的好厲害。還好沒被抓去吃掉。 有趣的夢也可以和#p1013101#分享。應該會很棒。");
	else if (status == 8){
		qm.forceStartQuest();
		qm.sendNext("#b#p1013101##k拿早餐去給獵犬吃，前往 #b#m100030102##k了。你從家裡往外走就能看到了。");
   }else if (status == 9){
		qm.evanTutorial("UI/tutorial/evan/1/0", 1);
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
	if (status == 0)
		qm.sendNext("喔！你起床了？#h0#！眼睛怎麼有黑眼圈哪？你都沒睡嗎？什麼？你說你做了奇怪的夢？什麼夢呢？做了龍出現的夢嗎？");
	if (status == 1)
		qm.sendNextPrev("哇哈哈哈~ 龍嗎？那很厲害？龍夢耶！可是夢裡面沒有出現一隻狗嗎？ 哈哈哈哈~\r\n\r\n#fUI/UIWindow.img/QuestIcon/8/0# 20 exp");
	if (status == 2){
		qm.gainExp(20);
		qm.evanTutorial("UI/tutorial/evan/2/0", 1);
		qm.forceCompleteQuest();
		qm.dispose();	
		}
	}
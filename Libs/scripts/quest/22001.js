
var status = -1;

function start(mode, type, selection) {
    status++;
	if (mode != 1) {
	    if(type == 1 && mode == 0)
		    status -= 2;
		else{
			qm.sendNext("Stop being lazy. Do you want to see your brother bitten by a dog? Hurry up! Talk to me again and accept the quest!");
			qm.dispose();
			return;
		}
	}
	if (status == 0)
		qm.sendNext("一大早就笑了半天。哈哈哈。對了不要再說奇怪的話了，快點拿早餐給 #p1013102#。");
	else if (status == 1)
		qm.PlayerToNpc("#b咦？那不是 #p1013101#要做的工作嗎？");
	else if (status == 2)
		qm.sendAcceptDecline("這個傢伙！居然這樣叫哥哥！你又不是不知道 #p1013102#有多討厭我。靠近的話一定會咬我。獵犬喜歡你，你拿去啦。");
	else if (status == 3){
		qm.gainItem(4032447,1);
		qm.sendNext("快點到#b左邊#k去把飼料拿給 #b#p1013102##k再回來。那隻狗從剛剛開始汪汪叫，可能是肚子餓了。");
		qm.forceStartQuest();
   }else if (status == 4){
		qm.sendPrev("把飼料拿給#p1013102#吃快點回來。");
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
		qm.sendNext("#b(You place food in Bulldog's bowl.)#k");
	if (status == 1)
		qm.sendOk("#b(Bulldog is totally sweet. Utah is just a coward.)#k\r\n\r\n#fUI/UIWindow.img/QuestIcon/8/0# 35 exp");
	if (status == 2){
		qm.forceCompleteQuest();
		qm.gainItem(4032447, -1);
		qm.gainExp(35);
		qm.sendOk("#b(Looks like Bulldog has finished eating. Return to Utah and let him know.)#k");
		qm.dispose();
		}
	}
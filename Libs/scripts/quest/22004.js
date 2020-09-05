var status = -1;

function start(mode, type, selection) {
    status++;
	if (mode != 1) {
	    if(type == 1 && mode == 0)
		    status -= 2;
		else{
			qm.sendNext("嗯……#p1013101#的話，應該就能幫我了。");
			qm.dispose();
			return;
		}
	}
    if (status == 0)
        qm.sendNext("最近農場上的#o1210100#有點奇怪。經常無緣無故地發脾氣，做出一些煩人的事情。我對此很擔心，所以今天很早就出來了。果然有一隻#o1210100#鑽過了籬笆，逃到外面去了。");
    else if (status == 1)
        qm.sendAcceptDecline("在找到#o1210100#之前，必須先把壞了的籬笆修好。還好壞得不是太嚴重，只要有幾個#t4032498#應該就能修好了。小不點，要是你能幫我搜集#b3個#t4032498##k就好了……");
    else if (status == 2) {
        qm.forceStartQuest();
        qm.sendNext("哦，真是謝謝你。#b#t4032498##k可以從周圍的#r#o0130100##k身上搜集到。它們雖然不是很強，但不小心的話，可能會遇到危險。你一定要好好使用技能道具。");
    } else if (status == 3) {
        qm.evanTutorial("UI/tutorial/evan/6/0", 1);
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
			qm.sendNext("哦，#t4032498#搜集到了嗎？真了不起。我應該給你什麼作為獎賞呢……對了，我有那個東西。 \r\n\r\n#fUI/UIWindow.img/QuestIcon/4/0# \r\n#i3010097# #t3010097#1個 \r\n#i2022621# #t2022621# 15個\r\n#i2022622# #t2022622# 15個 \r\n\r\n#fUI/UIWindow.img/QuestIcon/8/0# 210 exp");
	if (status == 1){
		qm.forceCompleteQuest();
		qm.gainItem(4032498, -3);
		qm.gainItem(3010097, 1);
		qm.gainItem(2022621, 15);
		qm.gainItem(2022622, 15);
		qm.sendNextPrev("好了，我用修理籬笆剩下的木板做了一把椅子。雖然不太好看，但卻很結實。就給你用吧。");
		qm.gainExp(210);
		}
	if(status == 2){
		qm.evanTutorial("UI/tutorial/evan/7/0", 1);
		qm.dispose();
	}
}
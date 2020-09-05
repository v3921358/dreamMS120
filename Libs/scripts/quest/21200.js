var status = -1;

function start(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	if (status == 0) {
	    qm.sendNext("It's really urgent, and you'll regret it if you refuse to. #bIt has something to do with your pole arm,#k which means it has to do with your past. Who knows...? Maybe the pole arm is key to reawakening your abilities...?");
	    qm.dispose();
	    return;
	}
	status--;
    }
    if (status == 0) {
	qm.askAcceptDecline("修練還順利嗎？哎呀，沒想到等級已經這麼高了，果然有出去修練有差...對了！現在不是說這個的時候。您這麼忙真得很抱歉，不過您暫時要跟我回去島上。");
    } else if (status == 1) {
	qm.forceStartQuest(21200, "3"); //??
	//qm.forceCompleteQuest();
	//qm.forceStartQuest(21202); //skip just in case
	//qm.forceStartQuest(21203, "0");
	qm.sendOk("保管於#b瑞恩村#k的您的 #b巨大的矛#k突然產生了奇怪的反應。根據紀錄，只有矛呼叫主人時會出現這樣的反應。 #b可能是想傳達給您。 快點去島上確認吧！");
	qm.dispose();
    }
}

function end(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	if (status == 11) {
	    qm.sendNext("Hey, at least you tell me you tried!");
	    qm.dispose();
	    return;
	} else if (status == 13) {
	    qm.MovieClipIntroUI(true);
	    qm.warp(914090200, 0);
	    qm.dispose();
	    return;
	}
	status--;
    }
    if (status == 0) {
        qm.sendNextS("嗚嗚嗚嗚嗚嗚…", 2);
    } else if (status == 1) {
        qm.sendNextPrevS("#b（#p1201001#嗚嗚的叫著。可是那個少年是誰呢？）#k", 2);
    } else if (status == 2) {
        qm.sendNextPrevS("#b（第一次看到的人…？不曉得為什麼卻不像人類。）#k", 2);
    } else if (status == 3) {
        qm.sendNextPrev("呀！ 狂狼勇士！還沒聽到我的聲音嗎？沒聽見嗎？啊啊！氣死人了！");
    } else if (status == 4) {
        qm.sendNextPrevS("#b（咦？這是誰的聲音呢？為什麼聽起來像個兇惡的少年…）#k", 2);
    } else if (status == 5) {
        qm.sendNextPrev("哎呀...主人在冰雪當中困了數百年，把武器都給丟著不管，現在連話都聽不到…");
    } else if (status == 6) {
        qm.sendNextPrevS("你是誰？", 2);
    } else if (status == 7) {
        qm.sendNextPrev("喂！ 狂狼勇士？現在才聽到我的聲音嗎？是我啦！你不知道嗎？你的武器#b矛 #p1201002##k！");
    } else if (status == 8) {
        qm.sendNextPrevS("#b（……#p1201002#？#p1201001#會說話嗎？）#k", 2);
    } else if (status == 9) {
        qm.sendNextPrev("什麼嘛？怎麼會有這種不可置信的表情？就算喪失記憶，該不會連我也都忘了？怎麼會有這種事情啊？");
    } else if (status == 10) {
        qm.sendNextPrevS("很抱歉。我什麼都不記得了。", 2);
    } else if (status == 11) {
        qm.sendYesNo("抱歉就算了嗎？你知道這數百年來我一個人多麼孤獨嗎？不管怎樣你給我想起來！");
    } else if (status == 12) {
        qm.sendNextS("#b（說自己是#p1201001#、#p1201002#，聲音聽起來很生氣。不想再繼續對話了。先去找 #p1201000#商量看看。）#k", 2);
		qm.forceCompleteQuest();
		qm.forceStartQuest(21201);
		//qm.forceStartQuest(21202); //skip just in case
		qm.forceStartQuest(21203, "0");
    } else if (status == 13) {
		qm.MovieClipIntroUI(true);
	    qm.warp(914090200, 0);
	    qm.dispose();
		//qm.sendYesNo("Would you like to skip the video clip?  Even if you skip the scene, game play will not be affected.");
    }
}
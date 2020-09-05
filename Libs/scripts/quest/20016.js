/*
	NPC Name: 		Nineheart
	Description: 		Quest - Do you know the black Magician?
*/

var status = -1;

function start(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	if (status == 8) {
	    qm.sendNext("Oh, do you still have some questions? Talk to me again and I'll explain it to you from the very beginning.");
	    qm.safeDispose();
	    return;
	}
	status--;
    }
    if (status == 0) {
	qm.sendNext("嗨, #h0#. 來迎來到 #p1101000# 騎士團. 我的名字是 #p1101002# 而我目前作為年輕慈禧的戰術家。哈哈！");
    } else if (status == 1) {
	qm.sendNextPrev("我敢肯定，你有很多的問題，因為一切都發生得太快。我會解釋這一切，一個接一個，從那裡你是你在這裡做什麼。");
    } else if (status == 2) {
	qm.sendNextPrev("這個島被稱為耶雷弗。");
    } else if (status == 3) {
	qm.sendNextPrev("這位年輕的女皇是楓之谷世界的統治者。什麼？這是你聽說過她的第一次？啊，是的。嗯，她是楓之谷世界的統治者，但她不喜歡來控制它。她從遠處觀看，以確保一切都很好。好吧，至少這是她一貫的作用。");
    } else if (status == 4) {
	qm.sendNextPrev("但是，這不是這種情況現在。我們一直在尋找的標牌立在楓的世界，預示黑法師的復興。我們不能讓黑法師回來恐嚇楓之谷，因為他在過去!");
    } else if (status == 5) {
	qm.sendNextPrev("但是，這是很久以前的今天，人們不要為實現黑法師是有多嚇人的。我們都成了被寵壞和平楓之谷世界我們今天所享有和遺忘是如何混亂和可怕的楓之谷世界曾經是。如果我們不做些什麼，黑法師將再次統治楓之谷世界！");
    } else if (status == 6) {
	qm.askAcceptDecline("以上是我的解釋，有問題嗎？ \r\n\r\n#fUI/UIWindow.img/QuestIcon/4/0# \r\n#fUI/UIWindow.img/QuestIcon/8/0# 380 經驗值");
    } else if (status == 7) {
	if (qm.getQuestStatus(20016) == 0) {
	    qm.gainExp(380);
	    qm.forceCompleteQuest();
	}
	qm.sendNext("我很高興你清楚我們目前的情況，但你知道在你目前的能力，你甚至沒有強大到足以面對黑法師的爪牙，更別說黑魔導士本人。連自己的奴才'奴才，作為一個事實問題。你將如何保護楓之谷世界在你目前的等級？");
    } else if (status == 10) {
	qm.sendNextPrev("Although you've been accepted into the knighthood, you cannot be recognized as a knight yet. You are not an Official Knight because you're not even a Knight-in-Training. If you remain at your current level, you'll be nothing more than the handyman of #p1101000# Knights.");
    } else if (status == 11) {
	qm.sendNextPrev("But no one starts as a strong Knight on day one. The Empress didn''t want someone strong. She wanted someone with courage whom she could develop into a strong Knight through rigorous training. So, you should first become a Knight-in-Training. We'll talk about your missions when you get to that point.");
    } else if (status == 12) {
	qm.sendPrev("Take the portal on the left to reach the Training Forest. There, you will find #p1102000#, the Training Instructor, who will teach you how to become stronger. I don''t want to find you wandering around aimlessly until you reach Lv. 10, you hear?");
	qm.safeDispose();
    }
}

function end(mode, type, selection) {
}
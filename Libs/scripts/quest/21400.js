var status = -1;

function start(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else {
        status--;
    }
    if (status == 0) {
		qm.sendAcceptDecline("修煉還順利嗎？我知道你很忙，很抱歉打擾你，不過請快點跟我回去 #b瑞恩村。 瑪哈#k又有了奇怪的反應...好奇怪。跟之前的反應不一樣。好像更深沉更黑暗...我有這樣的感覺。");
    } else if (status == 1) {
		qm.forceStartQuest(21400);
		qm.dispose();
	}
}

function end(mode, type, selection) {
	qm.sendNext("Please talk to Harmonia of Leafre instead.");
	qm.dispose();
}
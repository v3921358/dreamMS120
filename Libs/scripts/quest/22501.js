/*
	Description: 	Quest - Hungry Baby Dragon
*/

var status = -1;

function start(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	if (status == 3) {
	    qm.sendNext("*喘氣*你怎麼拒絕餵你的龍？這是虐待兒童！");
	    qm.dispose();
	    return;
	}
	status--;
    }
    if (status == 0) {
	qm.sendNext("呦，主人。現在我已經告訴你我能做什麼，這是輪到你了。向我證明......你可以找到食物！我餓死了。你現在可以使用我的權力，所以你必須照顧我.");
    } else if (status == 1) {
	qm.forceStartQuest();
	qm.sendNextPrevS("呃，我仍然不知道發生了什麼，但是我不能讓像你這樣可憐的小動物挨餓，對吧？食物，你說？你想吃什麼?", 2);
    } else if (status == 2) {
	qm.sendNextPrev("嗨，我剛剛在幾分鐘前出生。我怎麼知道我吃什麼？我所知道的是我是龍......我是你的龍。你是我的主人。你必須善待我！");
    } else if (status == 3) {
	qm.askAcceptDecline("我想我們應該一起學習。但我很餓。師父，我想要食物。請記住，我是一個嬰兒！我會很快開始哭泣!");
    } else if (status == 4) {
	qm.forceStartQuest();
	qm.sendOkS("#b(龍寶寶似乎非常餓。你必須餵他。也許你的爸爸可以給你龍吃什麼的建議.)", 2);
    }
}

function end(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	status--;
    }
    if (status == 0) {
	qm.sendNext("這是什麼，埃文？你想知道龍吃什麼？你為什麼......呃？你找到了龍?");
    } else if (status == 1) {
	qm.sendNextS("#b(你向爸爸說明.)#k", 2);
    } else if (status == 2) {
	qm.sendNextPrev("呃......那是一條龍？你確定它不只是一隻大蜥蜴？那麼，所有的生命都是寶貴的，所以我想你可以保留它...");
    } else if (status == 3) {
	qm.sendNextS("#b(爸爸似乎並不相信是龍。那麼，他還小。如果他聽到籠寶寶說話，爸爸會相信嗎？?)", 2);
    } else if (status == 4) {
	qm.sendNextPrev("如果它是一個真正的龍，那麼保持太危險。如果它吸入火焰會怎樣？我真的不認為這是一個龍，但也許我們應該問一個冒險家來殺死它，以防萬一.");
    } else if (status == 5) {
	qm.sendNextS("#b(什麼？！殺死我新收服的龍寶寶？但他沒有做錯什麼!!)", 2);
    } else if (status == 6) {
	qm.sendNextPrev("當然，我很確定它不是龍。龍只出現在神話故事中.");
    } else if (status == 7) {
	qm.sendNextS("#b哈...哈...你絕對是對的！我懷疑他是龍。他可能只是一隻蜥蜴！非也！", 2);
    } else if (status == 8) {
	qm.sendNextPrev("是的，我很確定。這是一隻怪異的蜥蜴，但它看起來並不危險。猜猜你可以保留它.");
    } else if (status == 9) {
	qm.sendNextS("#b(為了他自己的安全，你最好不要讓任何人知道是龍.)#k", 2);
    } else if (status == 10) {
	qm.sendOk("哦，你說你在找東西餵蜥蜴？我不確定...讓我想一下.");
    } else if (status == 11) {
	qm.gainExp(180);
	qm.forceCompleteQuest();
	qm.dispose();
    }
}
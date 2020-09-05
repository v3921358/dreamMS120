function action(mode, type, selection) {
    if (cm.getPlayer().getMapId() == 920011200) { //exit
    for (var i = 4001044; i < 4001064; i++) {
        cm.removeAll(i); //holy
    }
    cm.warp(200080101);
    cm.dispose();
    return;
    }
    var em = cm.getEventManager("OrbisPQ");
    if (em == null) {
    cm.sendOk("腳本出錯，請聯繫管理員。");
    cm.dispose();
    return;
    }
    if (!cm.isLeader()) {
    cm.sendOk("我只能跟你的隊長說話。");
    cm.dispose();
    return;
    }
    if (em.getProperty("pre").equals("0")) {
    cm.sendNext("我被遠古精靈困在這座塔，快收集材料讓我出去。");
    cm.dispose();
    return;
    }
    switch(cm.getPlayer().getMapId()) {
    case 920010000:
            clear();
        cm.warpParty(920010000, 2);
        break;
    case 920010100:
        if (em.getProperty("stage").equals("6")) {
        if (em.getProperty("finished").equals("0")) {
            cm.warpParty(920010800); //GARDEN.  
        } else {
            cm.sendOk("謝謝你救了我們，請您找女神說話。");
			cm.dispose();
        }
        } else {
        cm.sendOk("請收集六個女神雕像的碎片，然後來找我談話獲得最後一塊。");
		cm.dispose();
        }
        break;
    case 920010200: //walkway
        if (!cm.haveItem(4001050,30)) {
		cm.sendOk("我需要#b#t4001050# 30個#k，目前有#c4001050#個。");
		cm.dispose();
        } else {
        cm.removeAll(4001050);
        cm.gainItem(4001044,1); //first piece
        cm.givePartyExp(7500);
        clear();
        }
        break;
    case 920010300: //storage
        if (!cm.haveItem(4001051,15)) {
        cm.sendOk("我需要#b#t4001051# 15個#k，目前有#c4001051#個。");
		cm.dispose();
        } else {
        cm.removeAll(4001051);
        cm.gainItem(4001045,1); //second piece
        cm.givePartyExp(7500);
        clear();
        }
        break;
    case 920010400: //lobby
        if (em.getProperty("stage3").equals("0")) {
        cm.sendOk("請找到今天的唱片，並把它放入音樂盒撥放\r\n#v4001056#星期日\r\n#v4001057#星期一\r\n#v4001058#星期二\r\n#v4001059#星期三\r\n#v4001060#星期四\r\n#v4001061#星期五\r\n#v4001062#星期六\r\n");
        } else if (em.getProperty("stage3").equals("1")) {
        if (cm.canHold(4001046,1)) {
            cm.gainItem(4001046,1); //third piece
            cm.givePartyExp(7500);
            clear();
            em.setProperty("stage3", "2");
        } else {
            cm.sendOk("請清出一些空間。");
        }
        } else {
        cm.sendOk("謝謝你。");
        }
        break;
    case 920010500: //sealed
        if (em.getProperty("stage4").equals("0")) {
        var players = Array();
        var total = 0;
        for (var i = 0; i < 3; i++) {
            var z = cm.getMap().getNumPlayersInArea(i);
            players.push(z);
            total += z;
        }
        if (total < 5) {
            cm.sendOk("需要5個玩家站在平台上。");
			cm.dispose();
        } else {
            var num_correct = 0;
            for (var i = 0; i < 3; i++) {
            if (em.getProperty("stage4_" + i).equals("" + players[i])) {
                num_correct++;
            }
            }
            if (num_correct == 3) {
            if (cm.canHold(4001047,1)) {
                    clear();
                cm.gainItem(4001047,1); //fourth
                cm.givePartyExp(7500);
                    em.setProperty("stage4", "1");
            } else {
                cm.sendOk("請清出一些空間。");
            }
            } else {
                    cm.showEffect(true, "quest/party/wrong_kor");
                    cm.playSound(true, "Party1/Failed");
            if (num_correct > 0) {
                cm.sendOk("一個平台是正確的。");
				cm.dispose();
            } else {
                cm.sendOk("所有平台都是錯的。");
				cm.dispose();
            }
            }
        }
        } else {
        cm.sendOk("這麼門已經開了！");
		cm.dispose();
        }
        cm.dispose();
        break;
    case 920010600: //lounge
        if (!cm.haveItem(4001052,40)) {
		cm.sendOk("我需要#b#t4001052# 40個#k，目前有#c4001052#個。");
		cm.dispose();
        } else {
        cm.removeAll(4001052);
        cm.gainItem(4001048,1); //fifth piece
        cm.givePartyExp(7500);
        clear();
        }
        break;
    case 920010700: //on the way up
        if (em.getProperty("stage6").equals("0")) {
        var react = Array();
        var total = 0;
            for(var i = 0; i < 5; i++) {
            if (cm.getMap().getReactorByName("" + (i + 1)).getState() > 0) {
            react.push("1");
            total += 1;
            } else {
            react.push("0");
            }
            }
        if (total != 2) {
            cm.sendOk("需要有兩個人在頂部回答題目。");
			cm.dispose();
        } else {
            var num_correct = 0;
            for (var i = 0; i < 5; i++) {
            if (em.getProperty("stage62_" + i).equals("" + react[i])) {
                num_correct++;
            }
            }
            if (num_correct == 5) {
            if (cm.canHold(4001049,1)) {
                    clear();
                cm.gainItem(4001049,1); //sixth
                cm.givePartyExp(7500);
                    em.setProperty("stage6", "1");
            } else {
                cm.sendOk("請清出一些空間。");
				cm.dispose();
            }
            } else {
                    cm.showEffect(true, "quest/party/wrong_kor");
                    cm.playSound(true, "Party1/Failed");
            if (num_correct >= 3) {
                cm.sendOk("一個槓桿是正確的。");
				cm.dispose();
            } else {
                cm.sendOk("兩個槓桿都是錯誤的。");
				cm.dispose();
            }
            }
        }
        } else {
        cm.sendOk("謝謝你。");
        }
        break;
    case 920010800:
        cm.warpParty(920010100);
        break;
    case 920010900:
        cm.sendNext("這是塔的監獄。你可能會發現一些好吃的東西在這裡，但除此之外，我不認為我們有什麼在這裡件。"); 
        break;
    case 920011000:
        cm.sendNext("這是隱藏的房間塔。你可能會發現一些好吃的東西在這裡，但除此之外，我不認為我們有什麼在這裡件。"); 
        break;
    }
    cm.dispose();
}

function clear() {
    cm.showEffect(true, "quest/party/clear");
    cm.playSound(true, "Party1/Clear");
}
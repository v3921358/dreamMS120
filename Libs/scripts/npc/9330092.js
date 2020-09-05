var status = -1;



function start() {
    cm.sendSimple("你好，請查看背包 滿了被吃不會理你!" +
        "#k\r\n#L0#用 500 GASH 購買1個方便幣#b#i4001157##t4001157##k" +
		"#k\r\n#L1#用 5000 GASH 購買10個方便幣#b#i4001157##t4001157##k" +
		"#k\r\n#L2#用 1個方便幣 購買500 GASH" +
		"#k\r\n#L3#用 10個方便幣 購買5000 GASH" +
        "");
}

function action(mode, type, selection) {
    cm.dispose();

    switch (selection) {
        case 0:
            if (cm.getPlayer().getCSPoints(1) > 499) {//cm.getPlayer().getPoints() <= 數量
                cm.getPlayer().modifyCSPoints(1,-500);//cm.getPlayer().modifyCSPoints(1,-扣除數量)
                cm.gainItemPeriod(4001157, 1, 0,"" ); //cm.gainItemPeriod(裝備代碼, 數量, 天數, "智裝租用")
                cm.dispose();
                return;
            } else {
                cm.sendOk("你身上的 GASH 不足,無法購買.");
                cm.dispose();
                return;
            }
			case 1:
            if (cm.getPlayer().getCSPoints(1) > 4990) {//cm.getPlayer().getPoints() <= 數量
                cm.getPlayer().modifyCSPoints(1,-5000);//cm.getPlayer().modifyCSPoints(1,-扣除數量)
                cm.gainItemPeriod(4001157, 10, 0,"" ); //cm.gainItemPeriod(裝備代碼, 數量, 天數, "智裝租用")
                cm.dispose();
                return;
            } else {
                cm.sendOk("你身上的 GASH 不足,無法購買.");
                cm.dispose();
                return;
            }
			case 2:
            if (cm.getPlayer().haveItem(4001157, 1)) {//cm.getPlayer().getPoints() <= 數量
			    cm.gainItemPeriod(4001157, -1, 0,"" ); //cm.gainItemPeriod(裝備代碼, 數量, 天數, "智裝租用")
                cm.getPlayer().modifyCSPoints(1,500);//cm.getPlayer().modifyCSPoints(1,-扣除數量)
                cm.dispose();
                return;
            } else {
                cm.sendOk("你身上的 方便幣 不足,無法購買.");
                cm.dispose();
                return;
            }
			case 3:
            if (cm.getPlayer().haveItem(4001157, 10)) {//cm.getPlayer().getPoints() <= 數量
			    cm.gainItemPeriod(4001157, -10, 0,"" ); //cm.gainItemPeriod(裝備代碼, 數量, 天數, "智裝租用")
                cm.getPlayer().modifyCSPoints(1,5000);//cm.getPlayer().modifyCSPoints(1,-扣除數量)
                cm.dispose();
                return;
            } else {
                cm.sendOk("你身上的 方便幣 不足,無法購買.");
                cm.dispose();
                return;
            }
	}
}	
var status = -1;



function start() {
    cm.sendSimple("你好，你想要租用洗血智裝嗎?" +
        "#k\r\n#L0#用 100 GASH租用洗血 XX 智力#b#i1112915##t1112915##k智裝" +
        "");
}

function action(mode, type, selection) {
    cm.dispose();

    switch (selection) {
        case 0:
            if (cm.getPlayer().getCSPoints(1) > 99) {//cm.getPlayer().getPoints() <= 數量
                cm.getPlayer().modifyCSPoints(1,-100);//cm.getPlayer().modifyCSPoints(1,-扣除數量)
                cm.gainItemPeriod(1112915, 1, 2, "智裝租用"); //cm.gainItemPeriod(裝備代碼, 數量, 天數, "智裝租用")
                cm.dispose();
                return;
            } else {
                cm.sendOk("你身上的 GASH 不足,無法租用智裝.");
                cm.dispose();
                return;
            }
    }
}
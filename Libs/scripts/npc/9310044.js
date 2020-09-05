var status = -1;



function start() {
    cm.sendSimple("" +
        "#k\r\n#L0#我要離開" +
        "");
}

function action(mode, type, selection) {
    cm.dispose();

    switch (selection) {
        case 0:
            if (cm.getMeso() > 1) {
                cm.warp(702070400);
                cm.dispose();
                return;
            } else {
                cm.sendOk("你身上的 楓幣 不足,無法加入敢死隊.");
                cm.dispose();
                return;
            }
        case 1:
            if (cm.haveItem(4001086,1)) {
                cm.warp(240050400);
                cm.dispose();
                return;
            } else {
                cm.sendOk("你身上並沒有加入敢死隊的象徵.");
                cm.dispose();
                return;
            }
    }
}
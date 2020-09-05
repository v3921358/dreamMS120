var status = -1;



function start() {
    cm.sendSimple("你有勇氣加入敢死隊挑戰龍王嗎?" +
        "#k\r\n#L0#我要加入敢死隊#b(花5E楓幣買#i4001086##t4001086#)" +
        "#k\r\n#L1#我已經加入敢死隊#b(身上擁有#i4001086##t4001086#)" +
        "");
}

function action(mode, type, selection) {
    cm.dispose();

    switch (selection) {
        case 0:
            if (cm.getMeso() < 500000000 || cm.haveItem(4001086,1)) {
                cm.sendOk("你身上的 楓幣 不足,無法加入敢死隊 \r\n或者已經加入敢死隊了.");
                cm.dispose();
                return;
            } else {
                cm.gainItem(4001086,1);
                cm.gainMeso(-500000000);
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
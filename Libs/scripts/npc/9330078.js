var status = 0;

function start() {
    status = -0
	
	
	
	;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        ispose();
    } else 
        if (mode == 0) {
            cm.dispose();
            return;
        }
        if (mode == 1)
            status++;
        else
            status--;
        if (status == 0) {
           cm.sendSimple("#e#h #  #b您好，我是兌換獨眼巨人NPC#i5021018#！\r\n#r【Ｎew!!】 #k每兌換一階眼睛請先準備好上一階:\r\n#i5021007##k- 推出耳環生階系統#b【獨眼巨人】#i1022190#\r\n#i5021007##k- 並且有升級制度\r\n#r#i5021007##k- 兌換以及升級條件如下\r\n#L1#2個#i4001157# 兌換#i1022190#【獨眼巨人I 】\r\n#L2##b5個#i4001157#升級#i1022191#【獨眼巨人II】\r\n#L3##b15個#i4001157#升級#i1022192#【獨眼巨人III】\r\n#L4##b20個#i4001157#升級#i1022193#【覺醒獨眼巨人】\r\n#L5##b15個#i4001157#升級#i1022215#【真獨眼巨人】");
        } else if (status == 1) {
            if (!cm.canHold(1022190)) {
                cm.sendOk("您的背包空間不足。");//原版無
                cm.dispose();
                return;
            }
            switch (selection) {
                case 1:
                    if (!cm.getPlayer().haveItem(4001157, 2)) {
                        cm.sendOk("#i4001157##r少於2個無法換兌。");
                        cm.dispose();
                        return;
                    }
                    cm.gainItem(4001157, -2);
                    cm.gainItem(1022190, 1);
                    cm.sendOk("#i1022190#已經放到您的背包囉。");//不確定 當時無開放
                    cm.dispose();
                    break;
                case 2:
                    if (!cm.getPlayer().haveItem(4001157, 5)) {
                        cm.sendOk("#i4001157##r少於5個無法換兌。");
                        cm.dispose();
                        return;
                    } else if (!cm.getPlayer().haveItem(1022190, 1)) {//不確定 當時無開放
                        cm.sendOk("您身上沒有#i1022190#。");
                        cm.dispose();
                        return;
                    }
                    cm.gainItem(4001157, -5);
                    cm.gainItem(1022190, -1);
                    cm.gainItem(1022191, 1);
                    cm.sendOk("#i1022191#已經放到您的背包囉。");//不確定 當時無開放
                    cm.dispose();
                    break;
                case 3:
                    if (!cm.getPlayer().haveItem(4001157, 15)) {
                        cm.sendOk("#i4001157##r少於15個無法換兌。");
                        cm.dispose();
                        return;
                    } else if (!cm.getPlayer().haveItem(1022191, 1)) {//不確定 當時無開放
                        cm.sendOk("您身上沒有#i1022191#。");
                        cm.dispose();
                        return;
                    }
                    cm.gainItem(4001157, -15);
                    cm.gainItem(1022191, -1);
                    cm.gainItem(1022192, 1);
                    cm.sendOk("#i1022192#已經放到您的背包囉。");//不確定 當時無開放
                    cm.dispose();
                    break;
                case 4:
                    if (!cm.getPlayer().haveItem(4001157, 20)) {
                        cm.sendOk("#i4001157##r少於20個無法換兌。");
                        cm.dispose();
                        return;
                    } else if (!cm.getPlayer().haveItem(1022192, 1)) {//不確定 當時無開放
                        cm.sendOk("您身上沒有#i1022192#。");
                        cm.dispose();
                        return;
                    }
                    cm.gainItem(4001157, -20);
                    cm.gainItem(1022192, -1);
                    cm.gainItem(1022193, 1);
                    cm.sendOk("#i1022193#已經放到您的背包囉。");//不確定 當時無開放
                    cm.dispose();
                    break;
                case 5:
                    if (!cm.getPlayer().haveItem(4001157, 15)) {
                        cm.sendOk("#i4001157##r少於15個無法換兌。");
                        cm.dispose();
                        return;
                    } else if (!cm.getPlayer().haveItem(1022193, 1)) {//不確定 當時無開放
                        cm.sendOk("您身上沒有#i1022193#。");
                        cm.dispose();
                        return;
                    }
                    cm.gainItem(4001157, -15);
                    cm.gainItem(1022193, -1);
                    cm.gainItem(1022215, 1);
                    cm.sendOk("#i1022215#已經放到您的背包囉。");//不確定 當時無開放
                    cm.dispose();
                    break;

               } 
        }
    }
}	
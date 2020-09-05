var status = 0;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (mode == 0) {
            cm.dispose();
            return;
        }
        if (mode == 1)
            status++;
        else
            status--;
        if (status == 0) {
            cm.sendSimple("#e#h #  #b您好，我是兌換神話耳環NPC#i5021018#！\r\n#r【Ｎew!!】 #k每兌換一階耳環請先準備好上一階:\r\n#i5021007##k- 推出耳環生階系統#b【神話耳環】#i1032205#\r\n#i5021007##k- 並且有升級制度\r\n#r#i5021007##k- 兌換以及升級條件如下\r\n#L1#2個#i4001157# 兌換#i1032205#【神話耳環 】\r\n#L2##b15個#i4001157#升級#i1032206#【神話耳環 - 第一階段】\r\n#L3##b10個#i4001157#升級#i1032207#【神話耳環 - 第二階段】\r\n#L4##b10個#i4001157#升級#i1032208#【神話耳環 - 第三階段】\r\n#L5##b15個#i4001157#升級#i1032209#【神話耳環 - 第四階段】\r\n#L6##b70個#i4001157#升級#i1032219#【神話耳環 - 最終階段】");
        } else if (status == 1) {
            if (!cm.canHold(1032205)) {
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
                    cm.gainItem(1032205, 1);
                    cm.sendOk("#i1032205#已經放到您的背包囉。");//不確定 當時無開放
                    cm.dispose();
                    break;
                case 2:
                    if (!cm.getPlayer().haveItem(4001157, 15)) {
                        cm.sendOk("#i4001157##r少於15個無法換兌。");
                        cm.dispose();
                        return;
                    } else if (!cm.getPlayer().haveItem(1032205, 1)) {//不確定 當時無開放
                        cm.sendOk("您身上沒有#i1032205#。");
                        cm.dispose();
                        return;
                    }
                    cm.gainItem(4001157, -15);
                    cm.gainItem(1032205, -1);
                    cm.gainItem(1032206, 1);
                    cm.sendOk("#i1032206#已經放到您的背包囉。");//不確定 當時無開放
                    cm.dispose();
                    break;
                case 3:
                    if (!cm.getPlayer().haveItem(4001157, 10)) {
                        cm.sendOk("#i4001157##r少於10個無法換兌。");
                        cm.dispose();
                        return;
                    } else if (!cm.getPlayer().haveItem(1032206, 1)) {//不確定 當時無開放
                        cm.sendOk("您身上沒有#i1032206#。");
                        cm.dispose();
                        return;
                    }
                    cm.gainItem(4001157, -10);
                    cm.gainItem(1032206, -1);
                    cm.gainItem(1032207, 1);
                    cm.sendOk("#i1032207#已經放到您的背包囉。");//不確定 當時無開放
                    cm.dispose();
                    break;
                case 4:
                    if (!cm.getPlayer().haveItem(4001157, 10)) {
                        cm.sendOk("#i4001157##r少於10個無法換兌。");
                        cm.dispose();
                        return;
                    } else if (!cm.getPlayer().haveItem(1032207, 1)) {//不確定 當時無開放
                        cm.sendOk("您身上沒有#i1032207#。");
                        cm.dispose();
                        return;
                    }
                    cm.gainItem(4001157, -10);
                    cm.gainItem(1032207, -1);
                    cm.gainItem(1032208, 1);
                    cm.sendOk("#i1032208#已經放到您的背包囉。");//不確定 當時無開放
                    cm.dispose();
                    break;
                case 5:
                    if (!cm.getPlayer().haveItem(4001157, 15)) {
                        cm.sendOk("#i4001157##r少於15個無法換兌。");
                        cm.dispose();
                        return;
                    } else if (!cm.getPlayer().haveItem(1032208, 1)) {//不確定 當時無開放
                        cm.sendOk("您身上沒有#i1032208#。");
                        cm.dispose();
                        return;
                    }
                    cm.gainItem(4001157, -15);
                    cm.gainItem(1032208, -1);
                    cm.gainItem(1032209, 1);
                    cm.sendOk("#i1032209#已經放到您的背包囉。");//不確定 當時無開放
                    cm.dispose();
                    break;
                case 6:
                    if (!cm.getPlayer().haveItem(4001157, 70)) {
                        cm.sendOk("#i4001157##r少於70個無法換兌。");
                        cm.dispose();
                        return;
                    } else if (!cm.getPlayer().haveItem(1032209, 1)) {//不確定 當時無開放
                        cm.sendOk("您身上沒有#i1032209#。");
                        cm.dispose();
                        return;
                    }
                    cm.gainItem(4001157, -70);
                    cm.gainItem(1032209, -1);
                    cm.gainItem(1032219, 1);
                    cm.sendOk("#i1032219#已經放到您的背包囉。");//不確定 當時無開放
                    cm.dispose();
                    break;
            }
        }
    }
}	
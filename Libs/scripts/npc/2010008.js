/* guild emblem npc */
var status = 0;
var sel;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 0 && status == 0) {
	cm.dispose();
	return;
    }
    if (mode == 1)
	status++;
    else
	status--;

    if (status == 0)
	cm.sendSimple("你想要做什麼？\r\n#b#L0#創建/更改公會徽章#l#k");
    else if (status == 1) {
	sel = selection;
	if (selection == 0) {
	    if (cm.getPlayerStat("GRANK") == 1)
		cm.sendYesNo("重新打造一個徽章需要 #b1,000,000楓幣#k，你確定要繼續嗎？");
	    else
		cm.sendOk("打造公會徽章需要公會長來找我才行喔，請你們的公會長來找我吧~");
	}
				
    } else if (status == 2) {
	if (sel == 0) {
	    cm.genericGuildMessage(17);
	    cm.dispose();
	}
    }
}

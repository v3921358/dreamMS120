function start() {//宅配
	//if (cm.haveItem(5220040, 9787))
		cm.openDuey();
	//else
		//cm.sendOk("此功能暫時停用。");
    cm.dispose();
}

function action(mode, type, selection) {
	cm.dispose();
}
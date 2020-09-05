
function start() {
    cm.sendYesNo("你確定要離開?");
}

function action(mode, type, selection) {
    if (mode == 1) {
	cm.warp(105100100);
    }
    cm.dispose();
}
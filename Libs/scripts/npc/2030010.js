/* Amon
 * Last Mission : Zakum's Altar (280030000)
 */

function start() {
    cm.sendYesNo("如果你現在離開，你將不得不重新開始。你確定要離開這裡到外面去嗎？");
}

function action(mode, type, selection) {
    if (mode == 1) {
	cm.warp(211042200);
    }
    cm.dispose();
}
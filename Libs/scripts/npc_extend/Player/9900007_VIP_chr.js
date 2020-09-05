/*
	製作：彩虹工作室
	功能：查看地圖怪物爆率
	時間：2016年12月23日
*/

var status = -1;
var VIPeq = Array(1302147,1312062,1322090,1332120,1332125,1342033,1372078,1382099,1402090,1412062,1422063,1432081,1442111,1452106,1462091,1472117,1482079,1492079);

function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else {
        if (status == 0) {
            cm.dispose();
        }
        status--;
    }
    if (status == 0) {
		if (!cm.haveItem(2430051, 1))
			cm.dispose();
		var text = "";
		for (var item in VIPeq)
			text += "#L" + VIPeq[item] + "##i" + VIPeq[item] +":##l\t";
        cm.sendSimple(text);
    } else if (status == 1) {
		if (!cm.haveItem(2430051, 1))
			cm.dispose();
		cm.gainItem(selection, 1);
		cm.gainItem(2430051, -1);
        cm.dispose();
    }
}
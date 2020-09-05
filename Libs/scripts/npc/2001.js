/*
	By Mac
	Max Stat Item NPC
        AIM:darkriuxd MSN:darkriuxd@hotmail.com
*/
importPackage(Packages.client);

var status = 0;
var selected = 1;
var SelectType = 0;

function start() {
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, selection) {
    selected = selection;
	if (mode == -1) {
		cm.dispose();
	} else {
		if (status >= 0 && mode == 0) {
			cm.dispose();
			return;
		}
		if (mode == 1)
			status++;
		else
			status--;
		if (status == 0) {
		    var String = "#b強化裝備系統已經開放, #r6#b 種屬性只能強化其中 #r1#b 種屬性,#r 請慎重的點選." + 
						 "\r\n\r\n#b隨機提升屬性 #r+10 #b~ #r+20#b, 試試運氣吧!  #d需要#i4030002##r  10 #d個#n" +
						 "\r\n#e#d#L0#提升力量#l#L1#提升敏捷#l#L2#提升智慧#l#L3#提升幸運#l#n\r\n" +
						 "\r\n\r\n#b隨機提升傷害 #r10% #b~ #r30%#b, 試試運氣吧!  #d需要#i4030002##r 20 #d個#n" +
						 "\r\n#e#d#L4#提升#r物理#d攻擊#l               #L5#提升#r魔法#d攻擊#l#n";
		    cm.sendSimple(String);
		} else if (status == 1) {
		    if ((selection >= 0 && selection <= 3 && cm.haveItem(4030002, 10)) || (selection >= 4 && selection <= 5 && cm.haveItem(4030002, 20))) {
		    	if (selection == 0) {
			    String = "#b請選你想要「強化#r力量#b」的裝備.\r\n";
		    	} else if (selection == 1) {
			    String = "#b請選你想要「強化#r敏捷#b」的裝備.\r\n";
		    	} else if (selection == 2) {
			    String = "#b請選你想要「強化#r智慧#b」的裝備.\r\n";
		    	} else if (selection == 3) {
			    String = "#b請選你想要「強化#r幸運#b」的裝備.\r\n";
		    	} else if (selection == 4) {
			    String = "#b請選你想要「強化#r物理攻擊#b」的裝備.\r\n";
		    	} else if (selection == 5) {
			    String = "#b請選你想要「強化#r魔法攻擊#b」的裝備.\r\n";
		    	}
		    	    SelectType = selection;
		    	    cm.sendSimple(String+cm.EquipList1(cm.getPlayer().getClient()));
		    } else {
		    	if (selection >= 0 && selection <= 3){
			    cm.sendOk("你的#i4030002#不足 需要 #r10#k 個");
			    cm.dispose();
		    	} else if (selection >= 4 && selection <= 5){
			    cm.sendOk("你的#i4030002#不足 需要 #r20#k 個");
			    cm.dispose();
			}
		    }
		} else if (status == 2) {
			var 屬性 = Math.floor(Math.random() * 16 + 5);//5~20
			var 傷害 = Math.floor(Math.random() * 21 + 10);//10%~30%
			if (SelectType >= 0 && SelectType <= 3)
				cm.upgradeItem(selected, SelectType, 屬性, 4030002, -10);
			if (SelectType >= 4 && SelectType <= 5)
				cm.upgradeItem(selected, SelectType, 傷害, 4030002, -20);
			cm.dispose();
         }
    }
}
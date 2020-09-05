/*
*冬季戀曲，製作。
*製作者:baby_0402_85@yahoo.com.tw 
*/

var th = 0;
var Mesos1 = "#fUI/UIWindow.img/QuestIcon/7/0#";
var P = 10000;/*變數*/
var New = true;

var ids0_0 = [
/*不速*/1302147, 1312062, 1322090, 1402090, 1412062, 1422063, 1432081, 1442111, 1372078, 1382099, 1452106, 1462091, 1332120, 1472117, 1482079, 1492079,
           ];
var ids0_1 = [
/*血腥*/1302153, 1312066, 1322097, 1332131, 1372085, 1382105, 1402096, 1412066, 1422067, 1432087, 1442117, 1452112, 1462100, 1472123, 1482085, 1492086,
           ];
var ids0_2 = [
/*女皇*/1302152, 1312065, 1322096, 1402095, 1412065, 1422066, 1432086, 1442116, 1372084, 1382104, 1452111, 1462099, 1332130, 1472122, 1482084, 1492085,
           ];
var ids1 = [
/*戰士*/1002776, 1052155, 1082234, 1072355,
/*法師*/1002777, 1052156, 1082235, 1072356,
/*弓手*/1002778, 1052157, 1082236, 1072357,
/*盜賊*/1002779, 1052158, 1082237, 1072358,
/*海盜*/1002780, 1052159, 1082238, 1072359,
           ];
var ids2 = [
/*戰士*/1003177, 1052319, 1082300, 1072490,
/*法師*/1003178, 1052320, 1082301, 1072491,
/*弓手*/1003179, 1052321, 1082302, 1072492,
/*盜賊*/1003180, 1052322, 1082303, 1072493,
/*海盜*/1003181, 1052323, 1082304, 1072494,
           ];

var equipList = [
[
]


];
		   
/*請勿擅自設定*/
var Meso = [
/*楓葉頭盔*/10, 20, 30, 50, 70,
/*楓葉耳環*/20, 40, 70,
/*楓葉披風*/20, 40, 60, 70, 90,
/*楓葉盾牌*/10, 20, 20, 20, 100, 100, 100,
/*楓葉武器*/100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100,
/*永恆武器*/300, 300, 300, 300, 300, 300, 300, 300, 300, 300, 300, 300, 300, 300, 300, 300,
/*武器轉蛋*/0,
/*高階套裝*/0, 0, 1000, 1000, 1000, 1000, 1000,
/*血腥披風*/300, 300, 300, 300, 300,
/*女皇披風*/500, 500, 500, 500, 500
];

var Item = [
//100, 200, 300, 500, 700,
/*楓葉頭盔*/50, 100, 120, 150, 200,
/*楓葉耳環*/50, 75, 100,
/*楓葉披風*/70, 100, 130, 160, 200,
/*楓葉盾牌*/150, 500, 500, 500, 500, 500, 500,
/*楓葉武器*/0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
/*永恆武器*/0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
/*武器轉蛋*/1,
/*高階套裝*/1, 1, 1, 1, 1, 1, 1,
/*血腥披風*/500, 500, 500, 500, 500,
/*女皇披風*/1000, 1000, 1000, 1000, 1000
];

/*請勿擅自設定*/

function ItemID() {

    
}

function start() {
    th = -1;
    action(1, 0, 0);
}

function action(m, type, s) {
    if (m == -1) {
	cm.sendOk("下次在來歐~~");
        cm.dispose();
    } else {
        if (m == 0 && th == 0) {
            cm.dispose();
            return;
        }
        if (m == 1)
            th++;
        else
            th--;
    if (th == 0) {

		cm.sendOk(T());

    } else if (th == 1) {
	var New = false;
	B = s;
	cm.sendOk(A());
	

    } else if (th == 2) {
	for (var i = 0; i < 99; i++) {
	    if (s == i) {
		if(cm.getMeso() >= Meso[i] * P) {
		    X = i
		    Z();
		} else { 
		    cm.sendOk("#e製 作 費 用 需 要 #r"+Meso[i]+" 萬 元#k 您 的 金 額 #r不 足");
		    cm.dispose();
		}
	    }
	}

    } else if (th == 3) {
		C();

    } else if (th == 4) {
		V();


    }

    }
}

function T() {
    var ret  = "#b#e   【#h #】早呀~#n                #g請看看下列選單\r\n\#r";
	ret += "     楓   葉   武   /   裝   選   單" + "          各種 #e【整 套】#n 製 作 選 單";
	ret += "\r\n\#L0##d#e頭盔#i1002758#" + "#L1#耳環#i1032040#" + "#l    #L7#永恆#i1002776##i1052155##i1082234##i1072355#";
	ret += "\r\n\#L2##d#e披風#i1102166#" + "#L3#盾牌#i1092030#" + "#l   #L8#血腥#i1003177##i1052319##i1082300##i1072490#";
	ret += "\r\n\#L4##d#e武器#i1302142#" + "#L5#永恆#i1302081#" + "#l #L9#女皇#i1003172##i1052314##i1082295##i1072485#";
	ret += "\r\n\#L6##d#e#i1482079##i1482084##i1482085##n#r#l轉轉樂#d#e" + "   #L10#血腥#i1102280##l  #L11#女皇#i1102275#";
    return ret;
}

function A() {
    if (B == 0) {
	var ret  = "#fUI/UIWindow.img/QuestIcon/3/0#             #e#b您目前的 #i4001126# 有#k【#c4001126#】#b個#n#d";
	    ret += "\r\n\#L0#L v  0 #i1002508##e#r【費用"+Meso[0]+"萬】#n#d" + "#L1#L v  8 #i1002509##e#r【費用"+Meso[1]+"萬】#n#d";
	    ret += "\r\n\#L2#L v 30#i1002510##e#r【費用"+Meso[2]+"萬】#n#d" + "#L3#L v 70#i1002511##e#r【費用"+Meso[3]+"萬】#n#d";
	    ret += "\r\n\#L4#L v 90#i1002758##e#r【費用"+Meso[4]+"萬】#n#d";
	    ret += "\r\n\#l                                                    #k註：#d點擊此物品會顯示【材料】";
	return ret;
    } else if (B == 1) {
	var ret  = "#fUI/UIWindow.img/QuestIcon/3/0#             #e#b您目前的 #i4001126# 有#k【#c4001126#】#b個#n#d";
	    ret += "\r\n\#L5#L v 20#i1032040##e#r【費用"+Meso[5]+"萬】#n#d" + "#L6#L v 40#i1032041##e#r【費用"+Meso[6]+"萬】#n#d";
	    ret += "\r\n\#L7#L v 70#i1032042##e#r【費用"+Meso[7]+"萬】#n#d";
	    ret += "\r\n\r\n\#l                           #k註：#d點擊此物品會顯示【材料】";
	return ret;
    } else if (B == 2) {
	var ret  = "#fUI/UIWindow.img/QuestIcon/3/0#             #e#b您目前的 #i4001126# 有#k【#c4001126#】#b個#n#d";
	    ret += "\r\n\#L8#L v 20#i1102166##e#r【費用"+Meso[8]+"萬】#n#d" + "#L9#L v 40#i1102167##e#r【費用"+Meso[9]+"萬】#n#d";
	    ret += "\r\n\#L10#L v 70#i1102168##e#r【費用"+Meso[10]+"萬】#n#d" + "#L11#L v 70#i1102071##e#r【費用"+Meso[11]+"萬】#n#d";
	    ret += "\r\n\#L12#L v 30#i1102198##e#r【費用"+Meso[12]+"萬】#n#d";
	    ret += "\r\n\r\n\#l                                                    #k註：#d點擊此物品會顯示【材料】";
	return ret;
    } else if (B == 3) {
	var ret  = "#b#fUI/UIWindow.img/QuestIcon/3/0#             #e#b您目前的 #i4001126# 有#k【#c4001126#】#b個#n#d";
	    ret += "\r\n\#L13#L v 20#i1092030##e#r【費用"+Meso[13]+"萬】#n#d" + "#L14#L v 64#i1092045##e#r【費用"+Meso[14]+"萬】#n#d";
	    ret += "\r\n\#L15#L v 64 #i1092046##e#r 【費用"+Meso[15]+"萬】#n#d" + "#L16#L v 64#i1092047##e#r 【費用"+Meso[16]+"萬】#n#d";
	    ret += "\r\n\#L17#L v 120#i1092057##l      #L18#L v 120 #i1092058##l      #L19#L v 120 #i1092059##e#r#l";
	    ret += "\r\n\r\n\  【費用"+Meso[17]+"萬】        【費用"+Meso[18]+"萬】       【費用"+Meso[19]+"萬】";
	    ret += "\r\n\r\n\#l                           #k註：#d點擊此物品會顯示【材料】";
	return ret;
    } else if (B == 4) {
	var ret  = "#b#fUI/UIWindow.img/QuestIcon/3/0#             #e#b楓葉手套 圖示：#i1082252# 有#k【#c1082252#】#b個#n#d";
	    ret += "\r\n\r\n\           L v 77 #k黃金楓葉武器#e#r【需要 #d#t1082252##r 證明】#n#d";
	    ret += "\r\n\#L20##i1302142##L21##i1312056##L22##i1322084##L23##i1402085##L24##i1412055##l  #L25##i1422057#";
	    ret += "\r\n\#L26##i1432075##L27##i1442104##L28##i1372071##L29##i1382093##L30##i1452100##L31##i1462085#";
	    ret += "\r\n\#L32##i1332114##l #L33##i1472111##L34##i1482073##l   #L35##i1492073##l  #k註：#d費用【#r"+Meso[35]+"萬#d】";
	return ret;
    } else if (B == 5) {
	var ret  = "#b#fUI/UIWindow.img/QuestIcon/3/0#      #eL v 120#r永恆#b兌換區#d【費用"+Meso[51]+"萬】#n#d";
	    ret += "\r\n\#L36##i1302081##L37##i1312037##l  #L38##i1322060##L39##i1402046##L40##i1412033##L41##i1422037#";
	    ret += "\r\n\#L42##i1432047##L43##i1442063##L44##i1372044##L45##i1382057##l #L46##i1452057##L47##i1462050#";
	    ret += "\r\n\#L48##i1332074##l  #L49##i1472068##l    #L50##i1482023##l  #L51##i1492023#";
	return ret;
    } else if (B == 6) {
	var ret  = "#b#fUI/UIWindow.img/QuestIcon/3/0#  #e#b金寶箱 圖示：#L52##i4280000##n#d#l#e#r請先看以下的物品#k";
	    ret += "\r\n\#i1302147##i1312062##i1322090##i1402090##i1412062##i1422063##i1432081##i1442111##i1372078#";
	    ret += "\r\n\#i1382099##i1452106##i1462091##i1332120##i1472117##i1482079##i1492079#";
	    ret += "\r\n\#l#g────#bL v127 不速#g────#d有機率獲得#r 5-10#d的屬性#g─────";
	    ret += "\r\n\#i1302153##i1312066##i1322097##i1332131##i1372085##i1382105##i1402096##i1412066##i1422067#";
	    ret += "\r\n\#i1432087##i1442117##i1452112##i1462100##i1472123##i1482085##i1492086#";
	    ret += "\r\n\#l#g────#bL v130 血腥#g────#d有機率獲得#r 7-15#d的屬性#g─────";
	    ret += "\r\n\#i1302152##i1312065##i1322096##i1402095##i1412065##i1422066##i1432086##i1442116#";
	    ret += "\r\n\#i1372084##i1382104##i1452111##i1462099##i1332130##i1472122##i1482084##i1492085#";
	    ret += "\r\n\#l#g────#bL v140 女皇#g────#d有機率獲得#r10-20#d的屬性#g─────";
	return ret;
    } else if (B == 7) {
	var ret  = "#b#fUI/UIWindow.img/QuestIcon/3/0# #e#b金寶箱 圖示：#L53##i4280000##n#d#l#e#r請先看以下的物品#k";
	    ret += "\r\n\#e劍士套裝： #i1002776# #i1052155##i1082234##i1072355#　#rL v 120#k";
	    ret += "\r\n\法師套裝：#i1002777##i1052156##i1082235##i1072356#　#rL v 120#k";
	    ret += "\r\n\弓手套裝：#i1002778# #i1052157##i1082236##i1072357#　#rL v 120#k";
	    ret += "\r\n\盜賊套裝： #i1002779#  #i1052158##i1082237# #i1072358#　#rL v 120#k";
	    ret += "\r\n\海盜套裝：#i1002780# #i1052159##i1082238# #i1072359#　#rL v 120";
	    ret += "\r\n\#k註：#d隨機取得#r不是#d《取得》一套，#r是一件";
	return ret;
    } else if (B == 8) {
	var ret  = "#b#fUI/UIWindow.img/QuestIcon/3/0# #e#b金寶箱 圖示：#L54##i4280000##n#d#l#e#r請先看以下的物品#k";
	    ret += "\r\n\#e劍士套裝：#i1003177##i1052319##i1082300##i1072490#　#rL v 130#k";
	    ret += "\r\n\法師套裝：#i1003178##i1052320##i1082301##i1072491#　#rL v 130#k";
	    ret += "\r\n\弓手套裝：#i1003179##i1052321##i1082302##i1072492#　#rL v 130#k";
	    ret += "\r\n\盜賊套裝：#i1003180##i1052322##i1082303##i1072493#　#rL v 130#k";
	    ret += "\r\n\海盜套裝：#i1003181##i1052323##i1082304##i1072494#　#rL v 130";
	    ret += "\r\n\#k註：#d隨機取得#r不是#d《取得》一套，#r是一件 ";
	return ret;
    } else if (B == 9) {
	var ret  = "#b#fUI/UIWindow.img/QuestIcon/3/0##k #e#b套裝採取合成制#k   有機率獲得#r30-60#k的屬性";
	    ret += "\r\n\#e劍士套裝：#i1003172##i1052314# #i1082295##i1072485#　#rL v 140#k";
	    ret += "\r\n\法師套裝：#i1003173##i1052315##i1082296##i1072486#　#rL v 140#k";
	    ret += "\r\n\弓手套裝：#i1003174# #i1052316# #i1082297# #i1072487#　#rL v 140#k";
	    ret += "\r\n\盜賊套裝：#i1003175#  #i1052317# #i1082298# #i1072488#　#rL v 140#k";
	    ret += "\r\n\海盜套裝：#i1003176# #i1052318# #i1082299#  #i1072489#　#rL v 140#k";
	    ret += "\r\n\#d#L55#[劍士]#L56#[法師]#L57#[弓手]#L58#[盜賊]#L59#[海盜]";
	return ret;
    } else if (B == 10) {
	var ret  = "#fUI/UIWindow.img/QuestIcon/3/0#             #e#b您目前的 #i1102198# 有#k【#c1102198#】#b個#n#d";
	    ret += "\r\n\#L60##i1102280##e#d【劍士】#n#d" + "#L61##i1102281##e#d【法師】#n#d" + "#L62##i1102282##e#d【弓手】#n#d";
	    ret += "\r\n\#L63##i1102283##e#d【盜賊】#n#d" + "#L64##i1102284##e#d【海盜】#n#d";
	    ret += "#l\r\n\\r\n\     #d#e【費用#r"+Meso[60]+"#d萬】#n              #k註：#d點擊此物品會顯示【材料】";
	return ret;
    } else if (B == 11) {
	var ret  = "#fUI/UIWindow.img/QuestIcon/3/0#             #e#b您目前的 #i1102198# 有#k【#c1102198#】#b個#n#d";
	    ret += "\r\n\#L65##i1102275##e#d【劍士】#n#d" + "#L66##i1102276##e#d【法師】#n#d" + "#L67##i1102277##e#d【弓手】#n#d";
	    ret += "\r\n\#L68##i1102278##e#d【盜賊】#n#d" + "#L69##i1102279##e#d【海盜】#n#d";
	    ret += "#l\r\n\\r\n\     #d#e【費用#r"+Meso[65]+"#d萬】#n              #k註：#d點擊此物品會顯示【材料】";
	return ret;



    }

}

function Z() {
    if (X == 0) {
	cm.sendYesNo("#b您確認要製作 #dL v 0 #i1002508##k【楓葉頭盔】 #b以下為材料~#r\r\n\\r\n\ #i4001126# "+Item[X]+" 楓葉\r\n\#i4031138# "+Meso[X]+"萬 Meso\r\n\r\n\#e#g─────────#k註：#d有此物品在點擊【#r是#d】#g─────────");
    } else if (X == 1) {
	cm.sendYesNo("#b您確認要升級為 #dL v 8 #i1002509##k【楓葉頭盔】 #b以下為材料~\r\n\#e#kL v 0#r#n\r\n\#i1002508#  楓葉頭盔\r\n\#i4001126#   "+Item[X]+" 楓葉\r\n\#i4031138#  "+Meso[X]+"萬 Meso\r\n\r\n\#e#g─────────#k註：#d有此物品在點擊【#r是#d】#g─────────");
    } else if (X == 2) {
	cm.sendYesNo("#b您確認要升級為 #dL v 30 #i1002510##k【楓葉頭盔】 #b以下為材料~\r\n\#e#kL v 8#r#n\r\n\#i1002509#  楓葉頭盔\r\n\#i4001126#   "+Item[X]+" 楓葉\r\n\#i4031138#  "+Meso[X]+"萬 Meso\r\n\r\n\#e#g─────────#k註：#d有此物品在點擊【#r是#d】#g─────────");
    } else if (X == 3) {
	cm.sendYesNo("#b您確認要升級為 #dL v 70 #i1002511##k【楓葉頭盔】 #b以下為材料~\r\n\#e#kL v 30#r#n\r\n\#i1002510#  楓葉頭盔\r\n\#i4001126#   "+Item[X]+" 楓葉\r\n\#i4031138#  "+Meso[X]+"萬 Meso\r\n\r\n\#e#g─────────#k註：#d有此物品在點擊【#r是#d】#g─────────");
    } else if (X == 4) {
	cm.sendYesNo("#b您確認要升級為 #dL v 90 #i1002758##k【楓葉頭盔】 #b以下為材料~\r\n\#e#kL v 70#r#n\r\n\#i1002511#  楓葉頭盔\r\n\#i4001126#   "+Item[X]+" 楓葉\r\n\#i4031138#  "+Meso[X]+"萬 Meso\r\n\r\n\#e#g─────────#k註：#d有此物品在點擊【#r是#d】#g─────────");
    } else if (X == 5) {
	cm.sendYesNo("#b您確認要製作 #dL v 20 #i1032040##k【赤光耳環】 #b以下為材料~\r\n\\r\n\#r#i4001126#   "+Item[X]+" 楓葉\r\n\#i4031138#  "+Meso[X]+"萬 Meso\r\n\r\n\#e#g─────────#k註：#d有此物品在點擊【#r是#d】#g─────────");
    } else if (X == 6) {
	cm.sendYesNo("#b您確認要升級為 #dL v 40 #i1032041##k【赤光耳環】 #b以下為材料~\r\n\#e#kL v 20#r#n\r\n\#i1032040#   赤光耳環\r\n\#i4001126#   "+Item[X]+" 楓葉\r\n\#i4031138#  "+Meso[X]+"萬 Meso\r\n\r\n\#e#g─────────#k註：#d有此物品在點擊【#r是#d】#g─────────");
    } else if (X == 7) {
	cm.sendYesNo("#b您確認要升級為 #dL v 70 #i1032042##k【赤光耳環】 #b以下為材料~\r\n\#e#kL v 40#r#n\r\n\#i1032041#   赤光耳環\r\n\#i4001126#   "+Item[X]+" 楓葉\r\n\#i4031138#  "+Meso[X]+"萬 Meso\r\n\r\n\#e#g─────────#k註：#d有此物品在點擊【#r是#d】#g─────────");
    } else if (X == 8) {
	cm.sendYesNo("#b您確認要製作 #dL v 20 #i1102166##k【楓葉披風】 #b以下為材料~#r\r\n\\r\n\ #i4001126# "+Item[X]+" 楓葉\r\n\#i4031138# "+Meso[X]+"萬 Meso\r\n\r\n\#e#g─────────#k註：#d有此物品在點擊【#r是#d】#g─────────");
    } else if (X == 9) {
	cm.sendYesNo("#b您確認要升級為 #dL v 40 #i1102167##k【楓葉披風】 #b以下為材料~\r\n\#e#kL v 20#r#n\r\n\#i1102166#  楓葉披風\r\n\#i4001126#  "+Item[X]+" 楓葉\r\n\#i4031138# "+Meso[X]+"萬 Meso\r\n\r\n\#e#g─────────#k註：#d有此物品在點擊【#r是#d】#g─────────");
    } else if (X == 10) {
	cm.sendYesNo("#b您確認要升級為 #dL v 70 #i1102168##k【楓葉披風】 #b以下為材料~\r\n\#e#kL v 40#r#n\r\n\#i1102167#  楓葉披風\r\n\#i4001126#  "+Item[X]+" 楓葉\r\n\#i4031138# "+Meso[X]+"萬 Meso\r\n\r\n\#e#g─────────#k註：#d有此物品在點擊【#r是#d】#g─────────");
    } else if (X == 11) {
	cm.sendYesNo("#b您確認要升級為 #dL v 70 #i1102071##k【#t1102071#】 #b以下為材料~\r\n\#e#kL v 70#r#n\r\n\#i1102168#  楓葉披風\r\n\#i4001126#  "+Item[X]+" 楓葉\r\n\#i4031138# "+Meso[X]+"萬 Meso\r\n\r\n\#e#g─────────#k註：#d有此物品在點擊【#r是#d】#g─────────");
    } else if (X == 12) {
	cm.sendYesNo("#b您確認要升級為 #dL v 30 #i1102198##k【黃金楓葉披風】 #b以下為材料~\r\n\#e#kL v 70#r#n\r\n\#i1102071#  #t1102071#\r\n\#i4001126#  "+Item[X]+" 楓葉\r\n\#i4031138# "+Meso[X]+"萬 Meso\r\n\r\n\#e#g─────────#k註：#d有此物品在點擊【#r是#d】#g─────────");
    } else if (X == 13) {
	cm.sendYesNo("#b您確認要升級為 #dL v 20 #i1092030##k【楓葉之盾】 #b以下為材料~#r\r\n\\r\n\#i4001126#  "+Item[X]+" 楓葉\r\n\#i4031138# "+Meso[X]+"萬 Meso\r\n\r\n\#e#g─────────#k註：#d有此物品在點擊【#r是#d】#g─────────");
    } else if (X == 14) {
	cm.sendYesNo("#b您確認要升級為 #dL v 64 #i1092045##k【楓葉法盾】 #b以下為材料~\r\n\#e#kL v 20#r#n\r\n\#i1092030#  楓葉之盾\r\n\#i4001126#  "+Item[X]+" 楓葉\r\n\#i4031138# "+Meso[X]+"萬 Meso\r\n\r\n\#e#g─────────#k註：#d有此物品在點擊【#r是#d】#g─────────");
    } else if (X == 15) {
	cm.sendYesNo("#b您確認要升級為 #dL v 64 #i1092046##k【楓葉戰盾】 #b以下為材料~\r\n\#e#kL v 20#r#n\r\n\#i1092030#  楓葉之盾\r\n\#i4001126#  "+Item[X]+" 楓葉\r\n\#i4031138# "+Meso[X]+"萬 Meso\r\n\r\n\#e#g─────────#k註：#d有此物品在點擊【#r是#d】#g─────────");
    } else if (X == 16) {
	cm.sendYesNo("#b您確認要升級為 #dL v 64 #i1092047##k【楓葉護腕】 #b以下為材料~\r\n\#e#kL v 20#r#n\r\n\#i1092030#  楓葉之盾\r\n\#i4001126#  "+Item[X]+" 楓葉\r\n\#i4031138# "+Meso[X]+"萬 Meso\r\n\r\n\#e#g─────────#k註：#d有此物品在點擊【#r是#d】#g─────────");
    } else if (X == 17) {
	cm.sendYesNo("#b您確認要升級為 #dL v 120 #i1092057##k【永恆魔光盾】 #b以下為材料~\r\n\#e#kL v 64#r#n\r\n\#i1092045#  楓葉法盾\r\n\#i4001126#  "+Item[X]+" 楓葉\r\n\#i4031138# "+Meso[X]+"萬 Meso\r\n\r\n\#e#g─────────#k註：#d有此物品在點擊【#r是#d】#g─────────");
    } else if (X == 18) {
	cm.sendYesNo("#b您確認要升級為 #dL v 120 #i1092058##k【永恆寒冰盾】 #b以下為材料~\r\n\#e#kL v 64#r#n\r\n\#i1092046#  楓葉戰盾\r\n\#i4001126#  "+Item[X]+" 楓葉\r\n\#i4031138# "+Meso[X]+"萬 Meso\r\n\r\n\#e#g─────────#k註：#d有此物品在點擊【#r是#d】#g─────────");
    } else if (X == 19) {
	cm.sendYesNo("#b您確認要升級為 #dL v 120 #i1092059##k【永恆匿蹤盾】 #b以下為材料~\r\n\#e#kL v 64#r#n\r\n\#i1092047#  楓葉護腕\r\n\#i4001126#  "+Item[X]+" 楓葉\r\n\#i4031138# "+Meso[X]+"萬 Meso\r\n\r\n\#e#g─────────#k註：#d有此物品在點擊【#r是#d】#g─────────");
    } else if (X == 20) {
	cm.sendYesNo("#b您確認要製作 #dL v 77 #i1302142##k【黃金楓葉劍】 #b以下為材料~\r\n\#e#kL v 64#r#n\r\n\#i1302064#  楓葉絕世之劍\r\n\#i1082252#    楓葉手套#k#e【裝備證明，不扣此裝備】#n#r\r\n\#i4031138#  "+Meso[X]+"萬 Meso\r\n\r\n\#e#g─────────#k註：#d有此物品在點擊【#r是#d】#g─────────");
    } else if (X == 21) {
	cm.sendYesNo("#b您確認要製作 #dL v 77 #i1312056##k【黃金楓葉斧】 #b以下為材料~\r\n\#e#kL v 64#r#n\r\n\#i1312032#  楓葉霸道之斧\r\n\#i1082252#    楓葉手套#k#e【裝備證明，不扣此裝備】#n#r\r\n\#i4031138#  "+Meso[X]+"萬 Meso\r\n\r\n\#e#g─────────#k註：#d有此物品在點擊【#r是#d】#g─────────");
    } else if (X == 22) {
	cm.sendYesNo("#b您確認要製作 #dL v 77 #i1322084##k【黃金楓葉錘】 #b以下為材料~\r\n\#e#kL v 64#r#n\r\n\#i1322054#  楓葉粉碎之鎚\r\n\#i1082252#    楓葉手套#k#e【裝備證明，不扣此裝備】#n#r\r\n\#i4031138#  "+Meso[X]+"萬 Meso\r\n\r\n\#e#g─────────#k註：#d有此物品在點擊【#r是#d】#g─────────");
    } else if (X == 23) {
	cm.sendYesNo("#b您確認要製作 #dL v 77 #i1402085##k【黃金楓葉雙手劍】 #b以下為材料~\r\n\#e#kL v 64#r#n\r\n\#i1402039#  楓葉王者之劍\r\n\#i1082252#    楓葉手套#k#e【裝備證明，不扣此裝備】#n#r\r\n\#i4031138#  "+Meso[X]+"萬 Meso\r\n\r\n\#e#g─────────#k註：#d有此物品在點擊【#r是#d】#g─────────");
    } else if (X == 24) {
	cm.sendYesNo("#b您確認要製作 #dL v 77 #i1412055##k【黃金楓葉雙手斧】 #b以下為材料~\r\n\#e#kL v 64#r#n\r\n\#i1412027#  楓葉惡魔之斧\r\n\#i1082252#    楓葉手套#k#e【裝備證明，不扣此裝備】#n#r\r\n\#i4031138#  "+Meso[X]+"萬 Meso\r\n\r\n\#e#g─────────#k註：#d有此物品在點擊【#r是#d】#g─────────");
    } else if (X == 25) {
	cm.sendYesNo("#b您確認要製作 #dL v 77 #i1422057##k【黃金楓葉雙手錘】 #b以下為材料~\r\n\#e#kL v 64#r#n\r\n\#i1422029#  楓葉爆裂之鎚\r\n\#i1082252#    楓葉手套#k#e【裝備證明，不扣此裝備】#n#r\r\n\#i4031138#  "+Meso[X]+"萬 Meso\r\n\r\n\#e#g─────────#k註：#d有此物品在點擊【#r是#d】#g─────────");
    } else if (X == 26) {
	cm.sendYesNo("#b您確認要製作 #dL v 77 #i1432075##k【黃金楓葉之槍】 #b以下為材料~\r\n\#e#kL v 64#r#n\r\n\#i1432040#  楓葉銀月之槍\r\n\#i1082252#    楓葉手套#k#e【裝備證明，不扣此裝備】#n#r\r\n\#i4031138#  "+Meso[X]+"萬 Meso\r\n\r\n\#e#g─────────#k註：#d有此物品在點擊【#r是#d】#g─────────");
    } else if (X == 27) {
	cm.sendYesNo("#b您確認要製作 #dL v 77 #i1442104##k【黃金楓葉長矛】 #b以下為材料~\r\n\#e#kL v 64#r#n\r\n\#i1442051#  楓葉狂風之矛\r\n\#i1082252#    楓葉手套#k#e【裝備證明，不扣此裝備】#n#r\r\n\#i4031138#  "+Meso[X]+"萬 Meso\r\n\r\n\#e#g─────────#k註：#d有此物品在點擊【#r是#d】#g─────────");
    } else if (X == 28) {
	cm.sendYesNo("#b您確認要製作 #dL v 77 #i1372071##k【黃金楓葉短杖】 #b以下為材料~\r\n\#e#kL v 64#r#n\r\n\#i1372034#  楓葉靈魂短杖\r\n\#i1082252#    楓葉手套#k#e【裝備證明，不扣此裝備】#n#r\r\n\#i4031138#  "+Meso[X]+"萬 Meso\r\n\r\n\#e#g─────────#k註：#d有此物品在點擊【#r是#d】#g─────────");
    } else if (X == 29) {
	cm.sendYesNo("#b您確認要製作 #dL v 77 #i1382093##k【黃金楓葉長杖】 #b以下為材料~\r\n\#e#kL v 64#r#n\r\n\#i1382039#  楓葉智慧長杖\r\n\#i1082252#    楓葉手套#k#e【裝備證明，不扣此裝備】#n#r\r\n\#i4031138#  "+Meso[X]+"萬 Meso\r\n\r\n\#e#g─────────#k註：#d有此物品在點擊【#r是#d】#g─────────");
    } else if (X == 30) {
	cm.sendYesNo("#b您確認要製作 #dL v 77 #i1452100##k【黃金楓葉之弓】 #b以下為材料~\r\n\#e#kL v 64#r#n\r\n\#i1452045#  楓葉射日之弓\r\n\#i1082252#    楓葉手套#k#e【裝備證明，不扣此裝備】#n#r\r\n\#i4031138#  "+Meso[X]+"萬 Meso\r\n\r\n\#e#g─────────#k註：#d有此物品在點擊【#r是#d】#g─────────");
    } else if (X == 31) {
	cm.sendYesNo("#b您確認要製作 #dL v 77 #i1462085##k【黃金楓葉弩】 #b以下為材料~\r\n\#e#kL v 64#r#n\r\n\#i1462040#  楓葉追星之弩\r\n\#i1082252#    楓葉手套#k#e【裝備證明，不扣此裝備】#n#r\r\n\#i4031138#  "+Meso[X]+"萬 Meso\r\n\r\n\#e#g─────────#k註：#d有此物品在點擊【#r是#d】#g─────────");
    } else if (X == 32) {
	cm.sendYesNo("#b您確認要製作 #dL v 77 #i1332114##k【黃金楓葉短刀】 #b以下為材料~\r\n\#e#kL v 64#r#n\r\n\#i1332056#  楓葉修羅短刃\r\n\#i1082252#    楓葉手套#k#e【裝備證明，不扣此裝備】#n#r\r\n\#i4031138#  "+Meso[X]+"萬 Meso\r\n\r\n\#e#g─────────#k註：#d有此物品在點擊【#r是#d】#g─────────");
    } else if (X == 33) {
	cm.sendYesNo("#b您確認要製作 #dL v 77 #i1472111##k【黃金楓葉拳套】 #b以下為材料~\r\n\#e#kL v 64#r#n\r\n\#i1472055#  楓葉神獸拳套\r\n\#i1082252#    楓葉手套#k#e【裝備證明，不扣此裝備】#n#r\r\n\#i4031138#  "+Meso[X]+"萬 Meso\r\n\r\n\#e#g─────────#k註：#d有此物品在點擊【#r是#d】#g─────────");
    } else if (X == 34) {
	cm.sendYesNo("#b您確認要製作 #dL v 77 #i1482073##k【黃金楓葉指虎】 #b以下為材料~\r\n\#e#kL v 64#r#n\r\n\#i1482022#  楓葉黃金指虎\r\n\#i1082252#    楓葉手套#k#e【裝備證明，不扣此裝備】#n#r\r\n\#i4031138#  "+Meso[X]+"萬 Meso\r\n\r\n\#e#g─────────#k註：#d有此物品在點擊【#r是#d】#g─────────");
    } else if (X == 35) {
	cm.sendYesNo("#b您確認要製作 #dL v 77 #i1492073##k【黃金楓葉火槍】 #b以下為材料~\r\n\#e#kL v 64#r#n\r\n\#i1492022#  楓葉炫風火槍\r\n\#i1082252#    楓葉手套#k#e【裝備證明，不扣此裝備】#n#r\r\n\#i4031138#  "+Meso[X]+"萬 Meso\r\n\r\n\#e#g─────────#k註：#d有此物品在點擊【#r是#d】#g─────────");
    } else if (X == 36) {
	cm.sendYesNo("#b您確認要製作 #dL v 120 #i1302081##k【永恆破甲劍】 #b以下為材料~\r\n\#e#kL v 77#r#n\r\n\#i1302142#  黃金楓葉劍\r\n\#i1082252#    楓葉手套#k#e【此裝備兌換，會#r扣楓葉手套#k】#n#r\r\n\#i4031138#  "+Meso[X]+"萬 Meso\r\n\r\n\#e#g─────────#k註：#d有此物品在點擊【#r是#d】#g─────────");
    } else if (X == 37) {
	cm.sendYesNo("#b您確認要製作 #dL v 120 #i1312037##k【永恆斷蚺斧】 #b以下為材料~\r\n\#e#kL v 77#r#n\r\n\#i1312056#  黃金楓葉斧\r\n\#i1082252#    楓葉手套#k#e【此裝備兌換，會#r扣楓葉手套#k】#n#r\r\n\#i4031138#  "+Meso[X]+"萬 Meso\r\n\r\n\#e#g─────────#k註：#d有此物品在點擊【#r是#d】#g─────────");
    } else if (X == 38) {
	cm.sendYesNo("#b您確認要製作 #dL v 120 #i1322060##k【永恆驚破天】 #b以下為材料~\r\n\#e#kL v 77#r#n\r\n\#i1322084#  黃金楓葉錘\r\n\#i1082252#    楓葉手套#k#e【此裝備兌換，會#r扣楓葉手套#k】#n#r\r\n\#i4031138#  "+Meso[X]+"萬 Meso\r\n\r\n\#e#g─────────#k註：#d有此物品在點擊【#r是#d】#g─────────");
    } else if (X == 39) {
	cm.sendYesNo("#b您確認要製作 #dL v 120 #i1402046##k【永恆玄冥劍】 #b以下為材料~\r\n\#e#kL v 77#r#n\r\n\#i1402085#  黃金楓葉雙手劍\r\n\#i1082252#    楓葉手套#k#e【此裝備兌換，會#r扣楓葉手套#k】#n#r\r\n\#i4031138#  "+Meso[X]+"萬 Meso\r\n\r\n\#e#g─────────#k註：#d有此物品在點擊【#r是#d】#g─────────");
    } else if (X == 40) {
	cm.sendYesNo("#b您確認要製作 #dL v 120 #i1412033##k【永恆碎黿斧】 #b以下為材料~\r\n\#e#kL v 77#r#n\r\n\#i1412055#  黃金楓葉雙手斧\r\n\#i1082252#    楓葉手套#k#e【此裝備兌換，會#r扣楓葉手套#k】#n#r\r\n\#i4031138#  "+Meso[X]+"萬 Meso\r\n\r\n\#e#g─────────#k註：#d有此物品在點擊【#r是#d】#g─────────");
    } else if (X == 41) {
	cm.sendYesNo("#b您確認要製作 #dL v 120 #i1422037##k【永恆威震天】 #b以下為材料~\r\n\#e#kL v 77#r#n\r\n\#i1422057#  黃金楓葉雙手錘\r\n\#i1082252#    楓葉手套#k#e【此裝備兌換，會#r扣楓葉手套#k】#n#r\r\n\#i4031138#  "+Meso[X]+"萬 Meso\r\n\r\n\#e#g─────────#k註：#d有此物品在點擊【#r是#d】#g─────────");
    } else if (X == 42) {
	cm.sendYesNo("#b您確認要製作 #dL v 120 #i1432047##k【永恆顯聖槍】 #b以下為材料~\r\n\#e#kL v 77#r#n\r\n\#i1432075#  黃金楓葉之槍\r\n\#i1082252#    楓葉手套#k#e【此裝備兌換，會#r扣楓葉手套#k】#n#r\r\n\#i4031138#  "+Meso[X]+"萬 Meso\r\n\r\n\#e#g─────────#k註：#d有此物品在點擊【#r是#d】#g─────────");
    } else if (X == 43) {
	cm.sendYesNo("#b您確認要製作 #dL v 120 #i1442063##k【永恆神光戟】 #b以下為材料~\r\n\#e#kL v 77#r#n\r\n\#i1442104#  黃金楓葉長矛\r\n\#i1082252#    楓葉手套#k#e【此裝備兌換，會#r扣楓葉手套#k】#n#r\r\n\#i4031138#  "+Meso[X]+"萬 Meso\r\n\r\n\#e#g─────────#k註：#d有此物品在點擊【#r是#d】#g─────────");
    } else if (X == 44) {
	cm.sendYesNo("#b您確認要製作 #dL v 120 #i1372044##k【永恆天使氣息】 #b以下為材料~\r\n\#e#kL v 77#r#n\r\n\#i1372071#  黃金楓葉短杖\r\n\#i1082252#    楓葉手套#k#e【此裝備兌換，會#r扣楓葉手套#k】#n#r\r\n\#i4031138#  "+Meso[X]+"萬 Meso\r\n\r\n\#e#g─────────#k註：#d有此物品在點擊【#r是#d】#g─────────");
    } else if (X == 45) {
	cm.sendYesNo("#b您確認要製作 #dL v 120 #i1382057##k【永恆冰輪杖】 #b以下為材料~\r\n\#e#kL v 77#r#n\r\n\#i1382093#  黃金楓葉長杖\r\n\#i1082252#    楓葉手套#k#e【此裝備兌換，會#r扣楓葉手套#k】#n#r\r\n\#i4031138#  "+Meso[X]+"萬 Meso\r\n\r\n\#e#g─────────#k註：#d有此物品在點擊【#r是#d】#g─────────");
    } else if (X == 46) {
	cm.sendYesNo("#b您確認要製作 #dL v 120 #i1452057##k【永恆驚電弓】 #b以下為材料~\r\n\#e#kL v 77#r#n\r\n\#i1452100#  黃金楓葉之弓\r\n\#i1082252#    楓葉手套#k#e【此裝備兌換，會#r扣楓葉手套#k】#n#r\r\n\#i4031138#  "+Meso[X]+"萬 Meso\r\n\r\n\#e#g─────────#k註：#d有此物品在點擊【#r是#d】#g─────────");
    } else if (X == 47) {
	cm.sendYesNo("#b您確認要製作 #dL v 120 #i1462050##k【永恆冥雷弩】 #b以下為材料~\r\n\#e#kL v 77#r#n\r\n\#i1462085#  黃金楓葉弩\r\n\#i1082252#    楓葉手套#k#e【此裝備兌換，會#r扣楓葉手套#k】#n#r\r\n\#i4031138#  "+Meso[X]+"萬 Meso\r\n\r\n\#e#g─────────#k註：#d有此物品在點擊【#r是#d】#g─────────");
    } else if (X == 48) {
	cm.sendYesNo("#b您確認要製作 #dL v 120 #i1332074##k【永恆斷首刃】 #b以下為材料~\r\n\#e#kL v 77#r#n\r\n\#i1332114#  黃金楓葉短刀\r\n\#i1082252#    楓葉手套#k#e【此裝備兌換，會#r扣楓葉手套#k】#n#r\r\n\#i4031138#  "+Meso[X]+"萬 Meso\r\n\r\n\#e#g─────────#k註：#d有此物品在點擊【#r是#d】#g─────────");
    } else if (X == 49) {
	cm.sendYesNo("#b您確認要製作 #dL v 120 #i1472068##k【永恆紫龍拳套】 #b以下為材料~\r\n\#e#kL v 77#r#n\r\n\#i1472111#  黃金楓葉拳套\r\n\#i1082252#    楓葉手套#k#e【此裝備兌換，會#r扣楓葉手套#k】#n#r\r\n\#i4031138#  "+Meso[X]+"萬 Meso\r\n\r\n\#e#g─────────#k註：#d有此物品在點擊【#r是#d】#g─────────");
    } else if (X == 50) {
	cm.sendYesNo("#b您確認要製作 #dL v 120 #i1482023##k【永恆孔雀翎】 #b以下為材料~\r\n\#e#kL v 77#r#n\r\n\#i1482073#  黃金楓葉指虎\r\n\#i1082252#    楓葉手套#k#e【此裝備兌換，會#r扣楓葉手套#k】#n#r\r\n\#i4031138#  "+Meso[X]+"萬 Meso\r\n\r\n\#e#g─────────#k註：#d有此物品在點擊【#r是#d】#g─────────");
    } else if (X == 51) {
	cm.sendYesNo("#b您確認要製作 #dL v 120 #i1492023##k【永恆鳳凰火槍】 #b以下為材料~\r\n\#e#kL v 77#r#n\r\n\#i1492073#  黃金楓葉火槍\r\n\#i1082252#    楓葉手套#k#e【此裝備兌換，會#r扣楓葉手套#k】#n#r\r\n\#i4031138#  "+Meso[X]+"萬 Meso\r\n\r\n\#e#g─────────#k註：#d有此物品在點擊【#r是#d】#g─────────");
    } else if (X == 52) {
	cm.sendYesNo("#b此物品設置為#e#r隨機取得#d#n       兌換材料：#i4280000##k【金寶箱】\r\n\r\n\#r#i4280000#   "+Item[X]+" 金寶箱　　　　#e#k提醒：#d或許會轉到【#r垃圾#d】");
    } else if (X == 53) {
	cm.sendYesNo("#b此物品設置為#e#r隨機取得#d#n       兌換材料：#i4280000##k【金寶箱】\r\n\r\n\#r#i4280000#   "+Item[X]+" 金寶箱　　　　#e#k提醒：#d或許會轉到【#r垃圾#d】");
    } else if (X == 54) {
	cm.sendYesNo("#b此物品設置為#e#r隨機取得#d#n       兌換材料：#i4280000##k【金寶箱】\r\n\r\n\#r#i4280000#   "+Item[X]+" 金寶箱　　　　#e#k提醒：#d或許會轉到【#r垃圾#d】");
    } else if (X == 55) {
	cm.sendYesNo("#b您確認要合成 #dL v 140 #k【獅子心型套裝】 #b以下為材料~\r\n\#e#k 頭盔     衣服     手套     鞋子#r#n\r\n\#i1002776#    #i1052155#    #i1082234#    #i1072355##e#k  L v 120 永恆套裝#r#n\r\n\#i1003177#    #i1052319#    #i1082300#    #i1072490##e#k L v 130 血腥套裝#r#n\r\n\#i4031138#  "+Meso[X]+"萬 Meso\r\n\r\n\#e#g─────────#k註：#d有此物品在點擊【#r是#d】#g─────────");
    } else if (X == 56) {
	cm.sendYesNo("#b您確認要合成 #dL v 140 #k【龍尾巴套裝】 #b以下為材料~\r\n\#e#k 頭盔     衣服     手套     鞋子#r#n\r\n\#i1002777#    #i1052156#    #i1082235#    #i1072356##e#k  L v 120 永恆套裝#r#n\r\n\#i1003178#     #i1052320#    #i1082301#    #i1072491##e#k  L v 130 血腥套裝#r#n\r\n\#i4031138#  "+Meso[X]+"萬 Meso\r\n\r\n\#e#g─────────#k註：#d有此物品在點擊【#r是#d】#g─────────");
    } else if (X == 57) {
	cm.sendYesNo("#b您確認要合成 #dL v 140 #k【帕爾困監視套裝】 #b以下為材料~\r\n\#e#k 頭盔     衣服     手套     鞋子#r#n\r\n\#i1002778#    #i1052157#    #i1082236#    #i1072357##e#k  L v 120 永恆套裝#r#n\r\n\#i1003179#    #i1052321#    #i1082302#    #i1072492##e#k  L v 130 血腥套裝#r#n\r\n\#i4031138#  "+Meso[X]+"萬 Meso\r\n\r\n\#e#g─────────#k註：#d有此物品在點擊【#r是#d】#g─────────");
    } else if (X == 58) {
	cm.sendYesNo("#b您確認要合成 #dL v 140 #k【雷本魂獵人套裝】 #b以下為材料~\r\n\#e#k 頭盔     衣服     手套     鞋子#r#n\r\n\#i1002779#    #i1052158#    #i1082237#    #i1072358##e#k  L v 120 永恆套裝#r#n\r\n\#i1003180#  #i1052322#   #i1082303#    #i1072493##e#k  L v 130 血腥套裝#r#n\r\n\#i4031138#  "+Meso[X]+"萬 Meso\r\n\r\n\#e#g─────────#k註：#d有此物品在點擊【#r是#d】#g─────────");
    } else if (X == 59) {
	cm.sendYesNo("#b您確認要合成 #dL v 140 #k【俠客圖斯船套裝】 #b以下為材料~\r\n\#e#k 頭盔     衣服     手套     鞋子#r#n\r\n\#i1002780#    #i1052159#    #i1082238#    #i1072359##e#k  L v 120 永恆套裝#r#n\r\n\#i1003181#    #i1052323#    #i1082304#    #i1072494##e#k L v 130 血腥套裝#r#n\r\n\#i4031138#  "+Meso[X]+"萬 Meso\r\n\r\n\#e#g─────────#k註：#d有此物品在點擊【#r是#d】#g─────────");

    } else if (X == 60) {
	cm.sendYesNo("#b您確認要改造為 #dL v 130 #i1102280##k【#t1102280#】 #b以下為材料~\r\n\#e#kL v 30#r#n\r\n\#i1102198#  #t1102198#\r\n\#i4001126#   "+Item[X]+" 楓葉\r\n\#i4031138#  "+Meso[X]+"萬 Meso\r\n\r\n\#e#g─────────#k註：#d有此物品在點擊【#r是#d】#g─────────");
    } else if (X == 61) {
	cm.sendYesNo("#b您確認要改造為 #dL v 130 #i1102281##k【#t1102281#】 #b以下為材料~\r\n\#e#kL v 30#r#n\r\n\#i1102198#  #t1102198#\r\n\#i4001126#   "+Item[X]+" 楓葉\r\n\#i4031138#  "+Meso[X]+"萬 Meso\r\n\r\n\#e#g─────────#k註：#d有此物品在點擊【#r是#d】#g─────────");
    } else if (X == 62) {
	cm.sendYesNo("#b您確認要改造為 #dL v 130 #i1102282##k【#t1102282#】 #b以下為材料~\r\n\#e#kL v 30#r#n\r\n\#i1102198#  #t1102198#\r\n\#i4001126#   "+Item[X]+" 楓葉\r\n\#i4031138#  "+Meso[X]+"萬 Meso\r\n\r\n\#e#g─────────#k註：#d有此物品在點擊【#r是#d】#g─────────");
    } else if (X == 63) {
	cm.sendYesNo("#b您確認要改造為 #dL v 130 #i1102283##k【#t1102283#】 #b以下為材料~\r\n\#e#kL v 30#r#n\r\n\#i1102198#  #t1102198#\r\n\#i4001126#   "+Item[X]+" 楓葉\r\n\#i4031138#  "+Meso[X]+"萬 Meso\r\n\r\n\#e#g─────────#k註：#d有此物品在點擊【#r是#d】#g─────────");
    } else if (X == 64) {
	cm.sendYesNo("#b您確認要改造為 #dL v 130 #i1102284##k【#t1102284#】 #b以下為材料~\r\n\#e#kL v 30#r#n\r\n\#i1102198#  #t1102198#\r\n\#i4001126#   "+Item[X]+" 楓葉\r\n\#i4031138#  "+Meso[X]+"萬 Meso\r\n\r\n\#e#g─────────#k註：#d有此物品在點擊【#r是#d】#g─────────");

    } else if (X == 65) {
	cm.sendYesNo("#b您確認要改造為 #dL v 140 #i1102275##k【#t1102275#】 #b以下為材料~\r\n\#e#kL v 30#r#n\r\n\#i1102198#  #t1102198#\r\n\#i4001126#   "+Item[X]+" 楓葉\r\n\#i4031138#  "+Meso[X]+"萬 Meso\r\n\r\n\#e#g─────────#k註：#d有此物品在點擊【#r是#d】#g─────────");
    } else if (X == 66) {
	cm.sendYesNo("#b您確認要改造為 #dL v 140 #i1102276##k【#t1102276#】 #b以下為材料~\r\n\#e#kL v 30#r#n\r\n\#i1102198#  #t1102198#\r\n\#i4001126#   "+Item[X]+" 楓葉\r\n\#i4031138#  "+Meso[X]+"萬 Meso\r\n\r\n\#e#g─────────#k註：#d有此物品在點擊【#r是#d】#g─────────");
    } else if (X == 67) {
	cm.sendYesNo("#b您確認要改造為 #dL v 140 #i1102277##k【#t1102277#】 #b以下為材料~\r\n\#e#kL v 30#r#n\r\n\#i1102198#  #t1102198#\r\n\#i4001126#   "+Item[X]+" 楓葉\r\n\#i4031138#  "+Meso[X]+"萬 Meso\r\n\r\n\#e#g─────────#k註：#d有此物品在點擊【#r是#d】#g─────────");
    } else if (X == 68) {
	cm.sendYesNo("#b您確認要改造為 #dL v 140 #i1102278##k【#t1102278#】 #b以下為材料~\r\n\#e#kL v 30#r#n\r\n\#i1102198#  #t1102198#\r\n\#i4001126#   "+Item[X]+" 楓葉\r\n\#i4031138#  "+Meso[X]+"萬 Meso\r\n\r\n\#e#g─────────#k註：#d有此物品在點擊【#r是#d】#g─────────");
    } else if (X == 69) {
	cm.sendYesNo("#b您確認要改造為 #dL v 140 #i1102279##k【#t1102279#】 #b以下為材料~\r\n\#e#kL v 30#r#n\r\n\#i1102198#  #t1102198#\r\n\#i4001126#   "+Item[X]+" 楓葉\r\n\#i4031138#  "+Meso[X]+"萬 Meso\r\n\r\n\#e#g─────────#k註：#d有此物品在點擊【#r是#d】#g─────────");

    }
  lol = X;
}

function C() {
    if (lol == 0) {
	if(!cm.haveItem(4001126, Item[lol]) == true) {
	    cm.sendOk("#e您 的 材 料 尚 未 齊 全 請 在 確 認 一 次\r\n\\r\n\所 有 的  #i4001126#  有【#c4001126#】個                     #r│ 需要"+Item[lol]+"個 │");
	    cm.dispose();
	} else {
	    var Mesos = cm.getPlayer().getMeso();
	    cm.sendNext("#b系統檢測中~~\r\n\\r\n\#d您 目 前 的 #i4001126# 有#e#k【#c4001126#】#d ─  #r【"+Item[lol]+"】#d#n個 楓 葉\r\n\#d您 目 前 的 #i4031138# 有#e#k【"+Mesos+"】#d ─  #r【"+Meso[lol] * P+"】#d#n元\r\n\              "+Mesos1+"\r\n\                                #g點擊【下頁】領取物品");
	}
    } else if (lol == 1) {
	if(!cm.haveItem(1002508, 1) == true && !cm.haveItem(4001126, Item[lol]) == true) {
	    cm.sendOk("#e您 沒 有 #dL v 0#k #i1002508##k【楓葉頭盔】#g與#k 楓 葉 需 要 #r"+Item[lol]+" 個#k\r\n\r\n\        請 確 認 背 包 有 所 需 物 品 在 升 級 此 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(4001126, Item[lol]) == true) { 
	    cm.sendOk("#e您 的 材 料 尚 未 齊 全 請 在 確 認 一 次\r\n\\r\n\所 有 的  #i4001126#  有【#c4001126#】個                     #r│ 需要"+Item[lol]+"個 │");
	    cm.dispose();
	} else if(!cm.haveItem(1002508, 1) == true) {
	    cm.sendOk("#e打 開【 E 】#dL v 0#k #i1002508#【楓葉頭盔】是 否 帶 在 頭 上\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有  #dL v 0#k #i1002508##k【楓葉頭盔】請 在 確 認 物 品");
	    cm.dispose();
	} else {
	    var Mesos = cm.getPlayer().getMeso();
	    cm.sendNext("#b系統檢測中~~\r\n\\r\n\#d您 目 前 的 #i4001126# 有#e#k【#c4001126#】#d ─  #r【"+Item[lol]+"】#d#n個 楓 葉\r\n\#d您 目 前 的 #i1002508# 有#e#k【#c1002508#】#d ─  #r【1】#d#n個 頭 盔#k#e【L v  0】#n\r\n\#d您 目 前 的 #i4031138# 有#e#k【"+Mesos+"】#d ─  #r【"+Meso[lol] * P+"】#d#n元\r\n\              "+Mesos1+"\r\n\                                #g點擊【下頁】領取物品");
	}
    } else if (lol == 2) {
	if(!cm.haveItem(1002509, 1) == true && !cm.haveItem(4001126, Item[lol]) == true) {
	    cm.sendOk("#e您 沒 有 #dL v 8#k #i1002509##k【楓葉頭盔】#g與#k 楓 葉 需 要 #r"+Item[lol]+" 個#k\r\n\r\n\        請 確 認 背 包 有 所 需 物 品 在 升 級 此 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(4001126, Item[lol]) == true) { 
	    cm.sendOk("#e您 的 材 料 尚 未 齊 全 請 在 確 認 一 次\r\n\\r\n\所 有 的  #i4001126#  有【#c4001126#】個                     #r│ 需要"+Item[lol]+"個 │");
	    cm.dispose();
	} else if(!cm.haveItem(1002509, 1) == true) { 
	    cm.sendOk("#e打 開【 E 】#dL v 8#k #i1002509#【楓葉頭盔】是 否 帶 在 頭 上\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有  #dL v 8#k #i1002509##k【楓葉頭盔】請 在 確 認 物 品");
	    cm.dispose();
	} else {
	    var Mesos = cm.getPlayer().getMeso();
	    cm.sendNext("#b系統檢測中~~\r\n\\r\n\#d您 目 前 的 #i4001126# 有#e#k【#c4001126#】#d ─  #r【"+Item[lol]+"】#d#n個 楓 葉\r\n\#d您 目 前 的 #i1002509# 有#e#k【#c1002509#】#d ─  #r【1】#d#n個 頭 盔#k#e【L v  8】#n\r\n\#d您 目 前 的 #i4031138# 有#e#k【"+Mesos+"】#d ─  #r【"+Meso[lol] * P+"】#d#n元\r\n\              "+Mesos1+"\r\n\                                #g點擊【下頁】領取物品");
	}
    } else if (lol == 3) {
	if(!cm.haveItem(1002510, 1) == true && !cm.haveItem(4001126, Item[lol]) == true) {
	    cm.sendOk("#e您 沒 有 #dL v 30#k #i1002510##k【楓葉頭盔】#g與#k 楓 葉 需 要 #r"+Item[lol]+" 個#k\r\n\r\n\        請 確 認 背 包 有 所 需 物 品 在 升 級 此 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(4001126, Item[lol]) == true) { 
	    cm.sendOk("#e您 的 材 料 尚 未 齊 全 請 在 確 認 一 次\r\n\\r\n\所 有 的  #i4001126#  有【#c4001126#】個                     #r│ 需要"+Item[lol]+"個 │");
	    cm.dispose();
	} else if(!cm.haveItem(1002510, 1) == true) { 
	    cm.sendOk("#e打 開【 E 】#dL v 30#k #i1002510#【楓葉頭盔】是 否 帶 在 頭 上\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有  #dL v 30#k #i1002510##k【楓葉頭盔】請 在 確 認 物 品");
	    cm.dispose();
	} else {
	    var Mesos = cm.getPlayer().getMeso();
	    cm.sendNext("#b系統檢測中~~\r\n\\r\n\#d您 目 前 的 #i4001126# 有#e#k【#c4001126#】#d ─  #r【"+Item[lol]+"】#d#n個 楓 葉\r\n\#d您 目 前 的 #i1002510# 有#e#k【#c1002510#】#d ─  #r【1】#d#n個 頭 盔#k#e【L v 30】#n\r\n\#d您 目 前 的 #i4031138# 有#e#k【"+Mesos+"】#d ─  #r【"+Meso[lol] * P+"】#d#n元\r\n\              "+Mesos1+"\r\n\                                #g點擊【下頁】領取物品");
	}
    } else if (lol == 4) {
	if(!cm.haveItem(1002511, 1) == true && !cm.haveItem(4001126, Item[lol]) == true) {
	    cm.sendOk("#e您 沒 有 #dL v 70#k #i1002511##k【楓葉頭盔】#g與#k 楓 葉 需 要 #r"+Item[lol]+" 個#k\r\n\r\n\        請 確 認 背 包 有 所 需 物 品 在 升 級 此 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(4001126, Item[lol]) == true) { 
	    cm.sendOk("#e您 的 材 料 尚 未 齊 全 請 在 確 認 一 次\r\n\\r\n\所 有 的  #i4001126#  有【#c4001126#】個                     #r│ 需要"+Item[lol]+"個 │");
	    cm.dispose();
	} else if(!cm.haveItem(1002511, 1) == true) { 
	    cm.sendOk("#e打 開【 E 】#dL v 70#k #i1002511#【楓葉頭盔】是 否 帶 在 頭 上\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有  #dL v 70#k #i1002511##k【楓葉頭盔】請 在 確 認 物 品");
	    cm.dispose();
	} else {
	    var Mesos = cm.getPlayer().getMeso();
	    cm.sendNext("#b系統檢測中~~\r\n\\r\n\#d您 目 前 的 #i4001126# 有#e#k【#c4001126#】#d ─  #r【"+Item[lol]+"】#d#n個 楓 葉\r\n\#d您 目 前 的 #i1002511# 有#e#k【#c1002511#】#d ─  #r【1】#d#n個 頭 盔#k#e【L v 70】#n\r\n\#d您 目 前 的 #i4031138# 有#e#k【"+Mesos+"】#d ─  #r【"+Meso[lol] * P+"】#d#n元\r\n\              "+Mesos1+"\r\n\                                #g點擊【下頁】領取物品");
	}
    } else if (lol == 5) {
	if(!cm.haveItem(4001126, Item[lol]) == true) {
	    cm.sendOk("#e您 的 材 料 尚 未 齊 全 請 在 確 認 一 次\r\n\\r\n\所 有 的  #i4001126#  有【#c4001126#】個                     #r│ 需要"+Item[lol]+"個 │");
	    cm.dispose();
	} else {
	    var Mesos = cm.getPlayer().getMeso();
	    cm.sendNext("#b系統檢測中~~\r\n\\r\n\#d您 目 前 的 #i4001126# 有#e#k【#c4001126#】#d ─  #r【"+Item[lol]+"】#d#n個 楓 葉\r\n\#d您 目 前 的 #i4031138# 有#e#k【"+Mesos+"】#d ─  #r【"+Meso[lol] * P+"】#d#n元\r\n\              "+Mesos1+"\r\n\                                #g點擊【下頁】領取物品");
	}
    } else if (lol == 6) {
	if(!cm.haveItem(1032040, 1) == true && !cm.haveItem(4001126, Item[lol]) == true) {
	    cm.sendOk("#e您 沒 有 #dL v 20#k #i1032040##k【赤光耳環】#g與#k 楓 葉 需 要 #r"+Item[lol]+" 個#k\r\n\r\n\        請 確 認 背 包 有 所 需 物 品 在 升 級 此 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(4001126, Item[lol]) == true) {
	    cm.sendOk("#e您 的 材 料 尚 未 齊 全 請 在 確 認 一 次\r\n\\r\n\所 有 的  #i4001126#  有【#c4001126#】個                     #r│ 需要"+Item[lol]+"個 │");
	    cm.dispose();
	} else if(!cm.haveItem(1032040, 1) == true) { 
	    cm.sendOk("#e打 開【 E 】#dL v 20#k #i1032040#【赤光耳環】是 否 帶 在 身 上\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有  #dL v 20#k #i1032040##k【赤光耳環】請 在 確 認 物 品");
	    cm.dispose();
	} else {
	    var Mesos = cm.getPlayer().getMeso();
	    cm.sendNext("#b系統檢測中~~\r\n\\r\n\#d您 目 前 的 #i4001126# 有#e#k【#c4001126#】#d ─  #r【"+Item[lol]+"】#d#n個 楓 葉\r\n\#d您 目 前 的 #i1032040# 有#e#k【#c1032040#】#d ─  #r【1】#d#n個 頭 盔#k#e【L v 20】#n\r\n\#d您 目 前 的 #i4031138# 有#e#k【"+Mesos+"】#d ─  #r【"+Meso[lol] * P+"】#d#n元\r\n\              "+Mesos1+"\r\n\                                #g點擊【下頁】領取物品");
	}
    } else if (lol == 7) {
	if(!cm.haveItem(1032041, 1) == true && !cm.haveItem(4001126, Item[lol]) == true) {
	    cm.sendOk("#e您 沒 有 #dL v 40#k #i1032041##k【赤光耳環】#g與#k 楓 葉 需 要 #r"+Item[lol]+" 個#k\r\n\r\n\        請 確 認 背 包 有 所 需 物 品 在 升 級 此 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(4001126, Item[lol]) == true) {
	    cm.sendOk("#e您 的 材 料 尚 未 齊 全 請 在 確 認 一 次\r\n\\r\n\所 有 的  #i4001126#  有【#c4001126#】個                     #r│ 需要"+Item[lol]+"個 │");
	    cm.dispose();
	} else if(!cm.haveItem(1032041, 1) == true) { 
	    cm.sendOk("#e打 開【 E 】#dL v 40#k #i1032041#【赤光耳環】是 否 帶 在 身 上\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有  #dL v 40#k #i1032041##k【赤光耳環】請 在 確 認 物 品");
	    cm.dispose();
	} else {
	    var Mesos = cm.getPlayer().getMeso();
	    cm.sendNext("#b系統檢測中~~\r\n\\r\n\#d您 目 前 的 #i4001126# 有#e#k【#c4001126#】#d ─  #r【"+Item[lol]+"】#d#n個 楓 葉\r\n\#d您 目 前 的 #i1032041# 有#e#k【#c1032041#】#d ─  #r【1】#d#n個 頭 盔#k#e【L v 40】#n\r\n\#d您 目 前 的 #i4031138# 有#e#k【"+Mesos+"】#d ─  #r【"+Meso[lol] * P+"】#d#n元\r\n\              "+Mesos1+"\r\n\                                #g點擊【下頁】領取物品");
	}
    } else if (lol == 8) {
	if(!cm.haveItem(4001126, Item[lol]) == true) {
	    cm.sendOk("#e您 的 材 料 尚 未 齊 全 請 在 確 認 一 次\r\n\\r\n\所 有 的  #i4001126#  有【#c4001126#】個                     #r│ 需要"+Item[lol]+"個 │");
	    cm.dispose();
	} else {
	    var Mesos = cm.getPlayer().getMeso();
	    cm.sendNext("#b系統檢測中~~\r\n\\r\n\#d您 目 前 的 #i4001126# 有#e#k【#c4001126#】#d ─  #r【"+Item[lol]+"】#d#n個 楓 葉\r\n\#d您 目 前 的 #i4031138# 有#e#k【"+Mesos+"】#d ─  #r【"+Meso[lol] * P+"】#d#n元\r\n\              "+Mesos1+"\r\n\                                #g點擊【下頁】領取物品");
	}
    } else if (lol == 9) {
	if(!cm.haveItem(1102166, 1) == true && !cm.haveItem(4001126, Item[lol]) == true) {
	    cm.sendOk("#e您沒有 #dL v 20#k #i1102166##k【楓葉披風】#g與#k 楓 葉 需 要 #r"+Item[lol]+"  個#k\r\n\r\n\        請 確 認 背 包 有 所 需 物 品 在 升 級 此 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(4001126, Item[lol]) == true) {
	    cm.sendOk("#e您 的 材 料 尚 未 齊 全 請 在 確 認 一 次\r\n\\r\n\所 有 的  #i4001126#  有【#c4001126#】個                     #r│ 需要"+Item[lol]+"個 │");
	    cm.dispose();
	} else if(!cm.haveItem(1102166, 1) == true) { 
	    cm.sendOk("#e打 開【 E 】#dL v 20#k#i1102166#【楓葉披風】是 否 披 在 背 後\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有  #dL v 20#k#i1102166##k【楓葉披風】請 在 確 認 物 品");
	    cm.dispose();
	} else {
	    var Mesos = cm.getPlayer().getMeso();
	    cm.sendNext("#b系統檢測中~~\r\n\\r\n\#d您 目 前 的 #i4001126# 有#e#k【#c4001126#】#d ─  #r【"+Item[lol]+"】#d#n個 楓 葉\r\n\#d您 目 前 的 #i1102166# 有#e#k【#c1102166#】#d ─  #r【1】#d#n個 披 風#k#e【L v 20】#n\r\n\#d您 目 前 的 #i4031138# 有#e#k【"+Mesos+"】#d ─  #r【"+Meso[lol] * P+"】#d#n元\r\n\              "+Mesos1+"\r\n\                                #g點擊【下頁】領取物品");
	}
    } else if (lol == 10) {
	if(!cm.haveItem(1102167, 1) == true && !cm.haveItem(4001126, Item[lol]) == true) {
	    cm.sendOk("#e您沒有 #dL v 40#k #i1102167##k【楓葉披風】#g與#k 楓 葉 需 要 #r"+Item[lol]+"  個#k\r\n\r\n\        請 確 認 背 包 有 所 需 物 品 在 升 級 此 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(4001126, Item[lol]) == true) {
	    cm.sendOk("#e您 的 材 料 尚 未 齊 全 請 在 確 認 一 次\r\n\\r\n\所 有 的  #i4001126#  有【#c4001126#】個                     #r│ 需要"+Item[lol]+"個 │");
	    cm.dispose();
	} else if(!cm.haveItem(1102167, 1) == true) { 
	    cm.sendOk("#e打 開【 E 】#dL v 40#k#i1102167#【楓葉披風】是 否 披 在 背 後\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有  #dL v 40#k#i1102167##k【楓葉披風】請 在 確 認 物 品");
	    cm.dispose();
	} else {
	    var Mesos = cm.getPlayer().getMeso();
	    cm.sendNext("#b系統檢測中~~\r\n\\r\n\#d您 目 前 的 #i4001126# 有#e#k【#c4001126#】#d ─  #r【"+Item[lol]+"】#d#n個 楓 葉\r\n\#d您 目 前 的 #i1102167# 有#e#k【#c1102167#】#d ─  #r【1】#d#n個 披 風#k#e【L v 40】#n\r\n\#d您 目 前 的 #i4031138# 有#e#k【"+Mesos+"】#d ─  #r【"+Meso[lol] * P+"】#d#n元\r\n\              "+Mesos1+"\r\n\                                #g點擊【下頁】領取物品");
	}
    } else if (lol == 11) {
	if(!cm.haveItem(1102168, 1) == true && !cm.haveItem(4001126, Item[lol]) == true) {
	    cm.sendOk("#e您沒有 #dL v 70#k #i1102168##k【楓葉披風】#g與#k 楓 葉 需 要 #r"+Item[lol]+"  個#k\r\n\r\n\        請 確 認 背 包 有 所 需 物 品 在 升 級 此 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(4001126, Item[lol]) == true) {
	    cm.sendOk("#e您 的 材 料 尚 未 齊 全 請 在 確 認 一 次\r\n\\r\n\所 有 的  #i4001126#  有【#c4001126#】個                     #r│ 需要"+Item[lol]+"個 │");
	    cm.dispose();
	} else if(!cm.haveItem(1102168, 1) == true) { 
	    cm.sendOk("#e打 開【 E 】#dL v 70#k#i1102168#【楓葉披風】是 否 披 在 背 後\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有  #dL v 70#k#i1102168##k【楓葉披風】請 在 確 認 物 品");
	    cm.dispose();
	} else {
	    var Mesos = cm.getPlayer().getMeso();
	    cm.sendNext("#b系統檢測中~~\r\n\\r\n\#d您 目 前 的 #i4001126# 有#e#k【#c4001126#】#d ─  #r【"+Item[lol]+"】#d#n個 楓 葉\r\n\#d您 目 前 的 #i1102168# 有#e#k【#c1102168#】#d ─  #r【1】#d#n個 披 風#k#e【L v 70】#n\r\n\#d您 目 前 的 #i4031138# 有#e#k【"+Mesos+"】#d ─  #r【"+Meso[lol] * P+"】#d#n元\r\n\              "+Mesos1+"\r\n\                                #g點擊【下頁】領取物品");
	}
    } else if (lol == 12) {
	if(!cm.haveItem(1102071, 1) == true && !cm.haveItem(4001126, Item[lol]) == true) {
	    cm.sendOk("#e您沒有 #dL v 70#k #i1102071##k【#t1102071#】#g與#k楓 葉 需 要 #r"+Item[lol]+"個#k\r\n\r\n\        請 確 認 背 包 有 所 需 物 品 在 升 級 此 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(4001126, Item[lol]) == true) {
	    cm.sendOk("#e您 的 材 料 尚 未 齊 全 請 在 確 認 一 次\r\n\\r\n\所 有 的  #i4001126#  有【#c4001126#】個                     #r│ 需要"+Item[lol]+"個 │");
	    cm.dispose();
	} else if(!cm.haveItem(1102071, 1) == true) { 
	    cm.sendOk("#e打 開【 E 】#dL v 70#k#i1102071#【#t1102071#】是 否 披 在 背 後\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有  #dL v 70#k#i1102071##k【#t1102071#】請 在 確 認 物 品");
	    cm.dispose();
	} else {
	    var Mesos = cm.getPlayer().getMeso();
	    cm.sendNext("#b系統檢測中~~\r\n\\r\n\#d您 目 前 的 #i4001126# 有#e#k【#c4001126#】#d ─  #r【"+Item[lol]+"】#d#n個 楓 葉\r\n\#d您 目 前 的 #i1102071# 有#e#k【#c1102071#】#d ─  #r【1】#d#n個 披 風#k#e【L v 70】#n\r\n\#d您 目 前 的 #i4031138# 有#e#k【"+Mesos+"】#d ─  #r【"+Meso[lol] * P+"】#d#n元\r\n\              "+Mesos1+"\r\n\                                #g點擊【下頁】領取物品");
	}
    } else if (lol == 13) {
	if(!cm.haveItem(4001126, Item[lol]) == true) {
	    cm.sendOk("#e您 的 材 料 尚 未 齊 全 請 在 確 認 一 次\r\n\\r\n\所 有 的  #i4001126#  有【#c4001126#】個                     #r│ 需要"+Item[lol]+"個 │");
	    cm.dispose();
	} else {
	    var Mesos = cm.getPlayer().getMeso();
	    cm.sendNext("#b系統檢測中~~\r\n\\r\n\#d您 目 前 的 #i4001126# 有#e#k【#c4001126#】#d ─  #r【"+Item[lol]+"】#d#n個 楓 葉\r\n\#d您 目 前 的 #i4031138# 有#e#k【"+Mesos+"】#d ─  #r【"+Meso[lol] * P+"】#d#n元\r\n\              "+Mesos1+"\r\n\                                #g點擊【下頁】領取物品");
	}
    } else if (lol == 14) {
	if(!cm.haveItem(1092030, 1) == true && !cm.haveItem(4001126, Item[lol]) == true) {
	    cm.sendOk("#e您沒有 #dL v 20#k #i1092030##k【楓葉之盾】#g與#k 楓 葉 需 要 #r"+Item[lol]+" 個#k\r\n\r\n\        請 確 認 背 包 有 所 需 物 品 在 升 級 此 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(4001126, Item[lol]) == true) {
	    cm.sendOk("#e您 的 材 料 尚 未 齊 全 請 在 確 認 一 次\r\n\\r\n\所 有 的  #i4001126#  有【#c4001126#】個                     #r│ 需要"+Item[lol]+"個 │");
	    cm.dispose();
	} else if(!cm.haveItem(1092030, 1) == true) { 
	    cm.sendOk("#e打 開【 E 】 #dL v 20#k #i1092030#【楓葉之盾】是 否 穿 在 手 上\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有  #dL v 20#k #i1092030##k【楓葉之盾】請 在 確 認 物 品");
	    cm.dispose();
	} else {
	    var Mesos = cm.getPlayer().getMeso();
	    cm.sendNext("#b系統檢測中~~\r\n\\r\n\#d您 目 前 的 #i4001126# 有#e#k【#c4001126#】#d ─  #r【"+Item[lol]+"】#d#n個 楓 葉\r\n\#d您 目 前 的 #i1092030# 有#e#k【#c1092030#】#d ─  #r【1】#d#n個 盾 牌#k#e【L v 20】#n\r\n\#d您 目 前 的 #i4031138# 有#e#k【"+Mesos+"】#d ─  #r【"+Meso[lol] * P+"】#d#n元\r\n\              "+Mesos1+"\r\n\                                #g點擊【下頁】領取物品");
	}
    } else if (lol == 15) {
	if(!cm.haveItem(1092030, 1) == true && !cm.haveItem(4001126, Item[lol]) == true) {
	    cm.sendOk("#e您沒有 #dL v 20#k #i1092030##k【楓葉之盾】#g與#k 楓 葉 需 要 #r"+Item[lol]+" 個#k\r\n\r\n\        請 確 認 背 包 有 所 需 物 品 在 升 級 此 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(4001126, Item[lol]) == true) {
	    cm.sendOk("#e您 的 材 料 尚 未 齊 全 請 在 確 認 一 次\r\n\\r\n\所 有 的  #i4001126#  有【#c4001126#】個                     #r│ 需要"+Item[lol]+"個 │");
	    cm.dispose();
	} else if(!cm.haveItem(1092030, 1) == true) { 
	    cm.sendOk("#e打 開【 E 】 #dL v 20#k #i1092030#【楓葉之盾】是 否 穿 在 手 上\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有  #dL v 20#k #i1092030##k【楓葉之盾】請 在 確 認 物 品");
	    cm.dispose();
	} else {
	    var Mesos = cm.getPlayer().getMeso();
	    cm.sendNext("#b系統檢測中~~\r\n\\r\n\#d您 目 前 的 #i4001126# 有#e#k【#c4001126#】#d ─  #r【"+Item[lol]+"】#d#n個 楓 葉\r\n\#d您 目 前 的 #i1092030# 有#e#k【#c1092030#】#d ─  #r【1】#d#n個 盾 牌#k#e【L v 20】#n\r\n\#d您 目 前 的 #i4031138# 有#e#k【"+Mesos+"】#d ─  #r【"+Meso[lol] * P+"】#d#n元\r\n\              "+Mesos1+"\r\n\                                #g點擊【下頁】領取物品");
	}
    } else if (lol == 16) {
	if(!cm.haveItem(1092030, 1) == true && !cm.haveItem(4001126, Item[lol]) == true) {
	    cm.sendOk("#e您沒有 #dL v 20#k #i1092030##k【楓葉之盾】#g與#k 楓 葉 需 要 #r"+Item[lol]+" 個#k\r\n\r\n\        請 確 認 背 包 有 所 需 物 品 在 升 級 此 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(4001126, Item[lol]) == true) {
	    cm.sendOk("#e您 的 材 料 尚 未 齊 全 請 在 確 認 一 次\r\n\\r\n\所 有 的  #i4001126#  有【#c4001126#】個                     #r│ 需要"+Item[lol]+"個 │");
	    cm.dispose();
	} else if(!cm.haveItem(1092030, 1) == true) { 
	    cm.sendOk("#e打 開【 E 】 #dL v 20#k #i1092030#【楓葉之盾】是 否 穿 在 手 上\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有  #dL v 20#k #i1092030##k【楓葉之盾】請 在 確 認 物 品");
	    cm.dispose();
	} else {
	    var Mesos = cm.getPlayer().getMeso();
	    cm.sendNext("#b系統檢測中~~\r\n\\r\n\#d您 目 前 的 #i4001126# 有#e#k【#c4001126#】#d ─  #r【"+Item[lol]+"】#d#n個 楓 葉\r\n\#d您 目 前 的 #i1092030# 有#e#k【#c1092030#】#d ─  #r【1】#d#n個 盾 牌#k#e【L v 20】#n\r\n\#d您 目 前 的 #i4031138# 有#e#k【"+Mesos+"】#d ─  #r【"+Meso[lol] * P+"】#d#n元\r\n\              "+Mesos1+"\r\n\                                #g點擊【下頁】領取物品");
	}
    } else if (lol == 17) {
	if(!cm.haveItem(1092045, 1) == true && !cm.haveItem(4001126, Item[lol]) == true) {
	    cm.sendOk("#e您沒有 #dL v 64#k #i1092045##k【楓葉法盾】#g與#k 楓 葉 需 要 #r"+Item[lol]+" 個#k\r\n\r\n\        請 確 認 背 包 有 所 需 物 品 在 升 級 此 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(4001126, Item[lol]) == true) {
	    cm.sendOk("#e您 的 材 料 尚 未 齊 全 請 在 確 認 一 次\r\n\\r\n\所 有 的  #i4001126#  有【#c4001126#】個                     #r│ 需要"+Item[lol]+"個 │");
	    cm.dispose();
	} else if(!cm.haveItem(1092045, 1) == true) { 
	    cm.sendOk("#e打 開【 E 】 #dL v 64#k #i1092045#【楓葉法盾】是 否 穿 在 手 上\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有  #dL v 64#k #i1092045##k【楓葉法盾】請 在 確 認 物 品");
	    cm.dispose();
	} else {
	    var Mesos = cm.getPlayer().getMeso();
	    cm.sendNext("#b系統檢測中~~\r\n\\r\n\#d您 目 前 的 #i4001126# 有#e#k【#c4001126#】#d ─  #r【"+Item[lol]+"】#d#n個 楓 葉\r\n\#d您 目 前 的 #i1092045# 有#e#k【#c1092045#】#d ─  #r【1】#d#n個 盾 牌#k#e【L v 64】#n\r\n\#d您 目 前 的 #i4031138# 有#e#k【"+Mesos+"】#d ─  #r【"+Meso[lol] * P+"】#d#n元\r\n\              "+Mesos1+"\r\n\                                #g點擊【下頁】領取物品");
	}
    } else if (lol == 18) {
	if(!cm.haveItem(1092046, 1) == true && !cm.haveItem(4001126, Item[lol]) == true) {
	    cm.sendOk("#e您沒有 #dL v 64#k #i1092046##k【楓葉戰盾】#g與#k 楓 葉 需 要 #r"+Item[lol]+" 個#k\r\n\r\n\        請 確 認 背 包 有 所 需 物 品 在 升 級 此 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(4001126, Item[lol]) == true) {
	    cm.sendOk("#e您 的 材 料 尚 未 齊 全 請 在 確 認 一 次\r\n\\r\n\所 有 的  #i4001126#  有【#c4001126#】個                     #r│ 需要"+Item[lol]+"個 │");
	    cm.dispose();
	} else if(!cm.haveItem(1092046, 1) == true) { 
	    cm.sendOk("#e打 開【 E 】 #dL v 64#k #i1092046#【楓葉戰盾】是 否 穿 在 手 上\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有  #dL v 64#k #i1092046##k【楓葉戰盾】請 在 確 認 物 品");
	    cm.dispose();
	} else {
	    var Mesos = cm.getPlayer().getMeso();
	    cm.sendNext("#b系統檢測中~~\r\n\\r\n\#d您 目 前 的 #i4001126# 有#e#k【#c4001126#】#d ─  #r【"+Item[lol]+"】#d#n個 楓 葉\r\n\#d您 目 前 的 #i1092046# 有#e#k【#c1092046#】#d ─  #r【1】#d#n個 盾 牌#k#e【L v 64】#n\r\n\#d您 目 前 的 #i4031138# 有#e#k【"+Mesos+"】#d ─  #r【"+Meso[lol] * P+"】#d#n元\r\n\              "+Mesos1+"\r\n\                                #g點擊【下頁】領取物品");
	}
    } else if (lol == 19) {
	if(!cm.haveItem(1092047, 1) == true && !cm.haveItem(4001126, Item[lol]) == true) {
	    cm.sendOk("#e您沒有 #dL v 64#k #i1092047##k【楓葉護腕】#g與#k 楓 葉 需 要 #r"+Item[lol]+" 個#k\r\n\r\n\        請 確 認 背 包 有 所 需 物 品 在 升 級 此 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(4001126, Item[lol]) == true) {
	    cm.sendOk("#e您 的 材 料 尚 未 齊 全 請 在 確 認 一 次\r\n\\r\n\所 有 的  #i4001126#  有【#c4001126#】個                     #r│ 需要"+Item[lol]+"個 │");
	    cm.dispose();
	} else if(!cm.haveItem(1092047, 1) == true) { 
	    cm.sendOk("#e打 開【 E 】 #dL v 64#k #i1092047#【楓葉護腕】是 否 穿 在 手 上\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有  #dL v 64#k #i1092047##k【楓葉護腕】請 在 確 認 物 品");
	    cm.dispose();
	} else {
	    var Mesos = cm.getPlayer().getMeso();
	    cm.sendNext("#b系統檢測中~~\r\n\\r\n\#d您 目 前 的 #i4001126# 有#e#k【#c4001126#】#d ─  #r【"+Item[lol]+"】#d#n個 楓 葉\r\n\#d您 目 前 的 #i1092047# 有#e#k【#c1092047#】#d ─  #r【1】#d#n個 盾 牌#k#e【L v 64】#n\r\n\#d您 目 前 的 #i4031138# 有#e#k【"+Mesos+"】#d ─  #r【"+Meso[lol] * P+"】#d#n元\r\n\              "+Mesos1+"\r\n\                                #g點擊【下頁】領取物品");
	}
    } else if (lol == 20) {
	if(!cm.haveItem(1302064, 1) == true && !cm.haveItem(1082252, 1) == true) {
	    cm.sendOk("#e您沒有   #i1302064##k【楓葉絕世之劍】#g與#k  #i1082252##k【楓葉手套】#k\r\n\r\n\        請 確 認 背 包 有 所 需 物 品 在 升 級 此 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(1302064, 1) == true) {
	    cm.sendOk("#e打開【 E 】#dL v 64#k#i1302064#【楓葉絕世之劍】是 否 拿 在 手 上\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有 #dL v 64#k#i1302064##k【楓葉絕世之劍】請 在 確 認 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(1082252, 1) == true) { 
	    cm.sendOk("#e打 開【 E 】 #dL v 13#k #i1082252#【楓葉手套】是 否 拿 在 手 上\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有  #dL v 13#k #i1082252##k【楓葉手套】請 在 確 認 物 品");
	    cm.dispose();
	} else {
	    var Mesos = cm.getPlayer().getMeso();
	    cm.sendNext("#b系統檢測中~~\r\n\\r\n\#d您 目 前 的 #i1302064#有#e#k【#c1302064#】#d ─  #r【1】#d#n個 楓 葉 絕 世 之 劍\r\n\#d您 目 前 的 #i1082252#  有#e#k【#c1082252#】#d ─  #r【0】#d#n個 楓 葉 手 套#k#e【不扣除】#n\r\n\#d您 目 前 的 #i4031138#有#e#k【"+Mesos+"】#d ─  #r【"+Meso[lol] * P+"】#d#n元\r\n\              "+Mesos1+"\r\n\                                #g點擊【下頁】領取物品");
	}
    } else if (lol == 21) {
	if(!cm.haveItem(1312032, 1) == true && !cm.haveItem(1082252, 1) == true) {
	    cm.sendOk("#e您沒有   #i1312032##k【楓葉霸道之斧】#g與#k  #i1082252##k【楓葉手套】#k\r\n\r\n\        請 確 認 背 包 有 所 需 物 品 在 升 級 此 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(1312032, 1) == true) {
	    cm.sendOk("#e打開【 E 】#dL v 64#k#i1312032#【楓葉霸道之斧】是 否 拿 在 手 上\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有 #dL v 64#k#i1312032##k【楓葉霸道之劍】請 在 確 認 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(1082252, 1) == true) { 
	    cm.sendOk("#e打 開【 E 】 #dL v 13#k #i1082252#【楓葉手套】是 否 拿 在 手 上\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有  #dL v 13#k #i1082252##k【楓葉手套】請 在 確 認 物 品");
	    cm.dispose();
	} else {
	    var Mesos = cm.getPlayer().getMeso();
	    cm.sendNext("#b系統檢測中~~\r\n\\r\n\#d您 目 前 的 #i1312032#有#e#k【#c1312032#】#d ─  #r【1】#d#n個 楓 葉 霸 道 之 斧\r\n\#d您 目 前 的 #i1082252#  有#e#k【#c1082252#】#d ─  #r【0】#d#n個 楓 葉 手 套#k#e【不扣除】#n\r\n\#d您 目 前 的 #i4031138#有#e#k【"+Mesos+"】#d ─  #r【"+Meso[lol] * P+"】#d#n元\r\n\              "+Mesos1+"\r\n\                                #g點擊【下頁】領取物品");
	}
    } else if (lol == 22) {
	if(!cm.haveItem(1322054, 1) == true && !cm.haveItem(1082252, 1) == true) {
	    cm.sendOk("#e您沒有   #i1322054##k【楓葉粉碎之鎚】#g與#k  #i1082252##k【楓葉手套】#k\r\n\r\n\        請 確 認 背 包 有 所 需 物 品 在 升 級 此 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(1322054, 1) == true) {
	    cm.sendOk("#e打開【 E 】#dL v 64#k#i1322054#[ 楓葉粉碎之鎚 ] 是 否 拿 在 手 上\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有 #dL v 64#k#i1322054##k[ 楓葉粉碎之鎚 ] 請 在 確 認 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(1082252, 1) == true) { 
	    cm.sendOk("#e打 開【 E 】 #dL v 13#k #i1082252#【楓葉手套】是 否 拿 在 手 上\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有  #dL v 13#k #i1082252##k【楓葉手套】請 在 確 認 物 品");
	    cm.dispose();
	} else {
	    var Mesos = cm.getPlayer().getMeso();
	    cm.sendNext("#b系統檢測中~~\r\n\\r\n\#d您 目 前 的 #i1322054#有#e#k【#c1322054#】#d ─  #r【1】#d#n個 楓 葉 粉 碎 之 鎚\r\n\#d您 目 前 的 #i1082252#  有#e#k【#c1082252#】#d ─  #r【0】#d#n個 楓 葉 手 套#k#e【不扣除】#n\r\n\#d您 目 前 的 #i4031138#有#e#k【"+Mesos+"】#d ─  #r【"+Meso[lol] * P+"】#d#n元\r\n\              "+Mesos1+"\r\n\                                #g點擊【下頁】領取物品");
	}
    } else if (lol == 23) {
	if(!cm.haveItem(1402039, 1) == true && !cm.haveItem(1082252, 1) == true) {
	    cm.sendOk("#e您沒有   #i1402039##k【楓葉王者之劍】#g與#k  #i1082252##k【楓葉手套】#k\r\n\r\n\        請 確 認 背 包 有 所 需 物 品 在 升 級 此 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(1402039, 1) == true) {
	    cm.sendOk("#e打開【 E 】#dL v 64#k#i1402039# [ 楓葉王者之劍 ] 是 否 拿 在 手 上\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有 #dL v 64#k#i1402039##k [ 楓葉王者之劍 ] 請 在 確 認 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(1082252, 1) == true) { 
	    cm.sendOk("#e打 開【 E 】 #dL v 13#k #i1082252#【楓葉手套】是 否 拿 在 手 上\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有  #dL v 13#k #i1082252##k【楓葉手套】請 在 確 認 物 品");
	    cm.dispose();
	} else {
	    var Mesos = cm.getPlayer().getMeso();
	    cm.sendNext("#b系統檢測中~~\r\n\\r\n\#d您 目 前 的 #i1402039#有#e#k【#c1402039#】#d ─  #r【1】#d#n個 楓 葉 王 者 之 劍\r\n\#d您 目 前 的 #i1082252#  有#e#k【#c1082252#】#d ─  #r【0】#d#n個 楓 葉 手 套#k#e【不扣除】#n\r\n\#d您 目 前 的 #i4031138#有#e#k【"+Mesos+"】#d ─  #r【"+Meso[lol] * P+"】#d#n元\r\n\              "+Mesos1+"\r\n\                                #g點擊【下頁】領取物品");
	}
    } else if (lol == 24) {
	if(!cm.haveItem(1412027, 1) == true && !cm.haveItem(1082252, 1) == true) {
	    cm.sendOk("#e您沒有   #i1412027##k【楓葉惡魔之斧】#g與#k  #i1082252##k【楓葉手套】#k\r\n\r\n\        請 確 認 背 包 有 所 需 物 品 在 升 級 此 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(1412027, 1) == true) {
	    cm.sendOk("#e打開【 E 】#dL v 64#k#i1412027#【楓葉惡魔之斧】是 否 拿 在 手 上\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有 #dL v 64#k#i1412027##k【楓葉惡魔之斧】請 在 確 認 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(1082252, 1) == true) { 
	    cm.sendOk("#e打 開【 E 】 #dL v 13#k #i1082252#【楓葉手套】是 否 拿 在 手 上\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有  #dL v 13#k #i1082252##k【楓葉手套】請 在 確 認 物 品");
	    cm.dispose();
	} else {
	    var Mesos = cm.getPlayer().getMeso();
	    cm.sendNext("#b系統檢測中~~\r\n\\r\n\#d您 目 前 的 #i1412027#有#e#k【#c1412027#】#d ─  #r【1】#d#n個 楓 葉 惡 魔 之 斧\r\n\#d您 目 前 的 #i1082252#  有#e#k【#c1082252#】#d ─  #r【0】#d#n個 楓 葉 手 套#k#e【不扣除】#n\r\n\#d您 目 前 的 #i4031138#有#e#k【"+Mesos+"】#d ─  #r【"+Meso[lol] * P+"】#d#n元\r\n\              "+Mesos1+"\r\n\                                #g點擊【下頁】領取物品");
	}
    } else if (lol == 25) {
	if(!cm.haveItem(1422029, 1) == true && !cm.haveItem(1082252, 1) == true) {
	    cm.sendOk("#e您沒有   #i1422029##k【楓葉爆裂之鎚】#g與#k  #i1082252##k【楓葉手套】#k\r\n\r\n\        請 確 認 背 包 有 所 需 物 品 在 升 級 此 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(1422029, 1) == true) {
	    cm.sendOk("#e打開【 E 】#dL v 64#k#i1422029#【楓葉爆裂之鎚】是 否 拿 在 手 上\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有#dL v 64#k#i1422029##k【楓葉爆裂之鎚】請 在 確 認 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(1082252, 1) == true) { 
	    cm.sendOk("#e打 開【 E 】 #dL v 13#k #i1082252#【楓葉手套】是 否 拿 在 手 上\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有  #dL v 13#k #i1082252##k【楓葉手套】請 在 確 認 物 品");
	    cm.dispose();
	} else {
	    var Mesos = cm.getPlayer().getMeso();
	    cm.sendNext("#b系統檢測中~~\r\n\\r\n\#d您 目 前 的 #i1422029#有#e#k【#c1422029#】#d ─  #r【1】#d#n個 楓 葉 爆 裂 之 鎚\r\n\#d您 目 前 的 #i1082252#  有#e#k【#c1082252#】#d ─  #r【0】#d#n個 楓 葉 手 套#k#e【不扣除】#n\r\n\#d您 目 前 的 #i4031138#有#e#k【"+Mesos+"】#d ─  #r【"+Meso[lol] * P+"】#d#n元\r\n\              "+Mesos1+"\r\n\                                #g點擊【下頁】領取物品");
	}
    } else if (lol == 26) {
	if(!cm.haveItem(1432040, 1) == true && !cm.haveItem(1082252, 1) == true) {
	    cm.sendOk("#e您沒有   #i1432040##k【楓葉銀月之槍】#g與#k  #i1082252##k【楓葉手套】#k\r\n\r\n\        請 確 認 背 包 有 所 需 物 品 在 升 級 此 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(1432040, 1) == true) {
	    cm.sendOk("#e打開【 E 】#dL v64#k#i1432040#【楓葉銀月之槍】是 否 拿 在 手 上\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有#dL v 64#k#i1432040##k【楓葉銀月之槍】請 在 確 認 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(1082252, 1) == true) { 
	    cm.sendOk("#e打 開【 E 】 #dL v 13#k #i1082252#【楓葉手套】是 否 拿 在 手 上\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有  #dL v 13#k #i1082252##k【楓葉手套】請 在 確 認 物 品");
	    cm.dispose();
	} else {
	    var Mesos = cm.getPlayer().getMeso();
	    cm.sendNext("#b系統檢測中~~\r\n\\r\n\#d您 目 前 的 #i1432040#有#e#k【#c1432040#】#d ─  #r【1】#d#n個 楓 葉 銀 月 之 槍\r\n\#d您 目 前 的 #i1082252#  有#e#k【#c1082252#】#d ─  #r【0】#d#n個 楓 葉 手 套#k#e【不扣除】#n\r\n\#d您 目 前 的 #i4031138#有#e#k【"+Mesos+"】#d ─  #r【"+Meso[lol] * P+"】#d#n元\r\n\              "+Mesos1+"\r\n\                                #g點擊【下頁】領取物品");
	}
    } else if (lol == 27) {
	if(!cm.haveItem(1442051, 1) == true && !cm.haveItem(1082252, 1) == true) {
	    cm.sendOk("#e您沒有   #i1442051##k【楓葉狂風之矛】#g與#k  #i1082252##k【楓葉手套】#k\r\n\r\n\        請 確 認 背 包 有 所 需 物 品 在 升 級 此 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(1442051, 1) == true) {
	    cm.sendOk("#e打開【 E 】#dL v 64#k#i1442051# [ 楓葉狂風之矛 ] 是 否 拿 在 手 上\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有#dL v 64#k#i1442051##k [ 楓葉狂風之矛 ] 請 在 確 認 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(1082252, 1) == true) { 
	    cm.sendOk("#e打 開【 E 】 #dL v 13#k #i1082252#【楓葉手套】是 否 拿 在 手 上\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有  #dL v 13#k #i1082252##k【楓葉手套】請 在 確 認 物 品");
	    cm.dispose();
	} else {
	    var Mesos = cm.getPlayer().getMeso();
	    cm.sendNext("#b系統檢測中~~\r\n\\r\n\#d您 目 前 的 #i1442051#有#e#k【#c1442051#】#d ─  #r【1】#d#n個 楓 葉 狂 風 之 矛\r\n\#d您 目 前 的 #i1082252#  有#e#k【#c1082252#】#d ─  #r【0】#d#n個 楓 葉 手 套#k#e【不扣除】#n\r\n\#d您 目 前 的 #i4031138#有#e#k【"+Mesos+"】#d ─  #r【"+Meso[lol] * P+"】#d#n元\r\n\              "+Mesos1+"\r\n\                                #g點擊【下頁】領取物品");
	}
    } else if (lol == 28) {
	if(!cm.haveItem(1372034, 1) == true && !cm.haveItem(1082252, 1) == true) {
	    cm.sendOk("#e您沒有   #i1372034##k【楓葉靈魂短杖】#g與#k  #i1082252##k【楓葉手套】#k\r\n\r\n\        請 確 認 背 包 有 所 需 物 品 在 升 級 此 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(1372034, 1) == true) {
	    cm.sendOk("#e打開【 E 】#dL v 64#k#i1372034#【楓葉靈魂短杖】是 否 拿 在 手 上\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有#dL v 64#k#i1372034##k【楓葉靈魂短杖】請 在 確 認 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(1082252, 1) == true) { 
	    cm.sendOk("#e打 開【 E 】 #dL v 13#k #i1082252#【楓葉手套】是 否 拿 在 手 上\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有  #dL v 13#k #i1082252##k【楓葉手套】請 在 確 認 物 品");
	    cm.dispose();
	} else {
	    var Mesos = cm.getPlayer().getMeso();
	    cm.sendNext("#b系統檢測中~~\r\n\\r\n\#d您 目 前 的 #i1372034#有#e#k【#c1372034#】#d ─  #r【1】#d#n個 楓 葉 靈 魂 短 杖\r\n\#d您 目 前 的 #i1082252#  有#e#k【#c1082252#】#d ─  #r【0】#d#n個 楓 葉 手 套#k#e【不扣除】#n\r\n\#d您 目 前 的 #i4031138#有#e#k【"+Mesos+"】#d ─  #r【"+Meso[lol] * P+"】#d#n元\r\n\              "+Mesos1+"\r\n\                                #g點擊【下頁】領取物品");
	}
    } else if (lol == 29) {
	if(!cm.haveItem(1382039, 1) == true && !cm.haveItem(1082252, 1) == true) {
	    cm.sendOk("#e您沒有   #i1382039##k【楓葉智慧長杖】#g與#k  #i1082252##k【楓葉手套】#k\r\n\r\n\        請 確 認 背 包 有 所 需 物 品 在 升 級 此 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(1382039, 1) == true) {
	    cm.sendOk("#e打開【 E 】#dL v64#k#i1382039#【楓葉智慧長杖】是 否 拿 在 手 上\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有#dL v 64#k#i1382039##k【楓葉智慧長杖】請 在 確 認 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(1082252, 1) == true) { 
	    cm.sendOk("#e打 開【 E 】 #dL v 13#k #i1082252#【楓葉手套】是 否 拿 在 手 上\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有  #dL v 13#k #i1082252##k【楓葉手套】請 在 確 認 物 品");
	    cm.dispose();
	} else {
	    var Mesos = cm.getPlayer().getMeso();
	    cm.sendNext("#b系統檢測中~~\r\n\\r\n\#d您 目 前 的 #i1382039#有#e#k【#c1382039#】#d ─  #r【1】#d#n個 楓 葉 智 慧 長 杖\r\n\#d您 目 前 的 #i1082252#  有#e#k【#c1082252#】#d ─  #r【0】#d#n個 楓 葉 手 套#k#e【不扣除】#n\r\n\#d您 目 前 的 #i4031138#有#e#k【"+Mesos+"】#d ─  #r【"+Meso[lol] * P+"】#d#n元\r\n\              "+Mesos1+"\r\n\                                #g點擊【下頁】領取物品");
	}
    } else if (lol == 30) {
	if(!cm.haveItem(1452045, 1) == true && !cm.haveItem(1082252, 1) == true) {
	    cm.sendOk("#e您沒有   #i1452045##k【楓葉射日之弓】#g與#k  #i1082252##k【楓葉手套】#k\r\n\r\n\        請 確 認 背 包 有 所 需 物 品 在 升 級 此 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(1452045, 1) == true) {
	    cm.sendOk("#e打開【 E 】#dL v 64#k#i1452045# [ 楓葉射日之弓 ] 是 否 拿 在 手 上\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有#dL v 64#k#i1452045##k [ 楓葉射日之弓 ] 請 在 確 認 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(1082252, 1) == true) { 
	    cm.sendOk("#e打 開【 E 】 #dL v 13#k #i1082252#【楓葉手套】是 否 拿 在 手 上\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有  #dL v 13#k #i1082252##k【楓葉手套】請 在 確 認 物 品");
	    cm.dispose();
	} else {
	    var Mesos = cm.getPlayer().getMeso();
	    cm.sendNext("#b系統檢測中~~\r\n\\r\n\#d您 目 前 的 #i1452045#有#e#k【#c1452045#】#d ─  #r【1】#d#n個 楓 葉 射 日 之 弓\r\n\#d您 目 前 的 #i1082252#  有#e#k【#c1082252#】#d ─  #r【0】#d#n個 楓 葉 手 套#k#e【不扣除】#n\r\n\#d您 目 前 的 #i4031138#有#e#k【"+Mesos+"】#d ─  #r【"+Meso[lol] * P+"】#d#n元\r\n\              "+Mesos1+"\r\n\                                #g點擊【下頁】領取物品");
	}
    } else if (lol == 31) {
	if(!cm.haveItem(1462040, 1) == true && !cm.haveItem(1082252, 1) == true) {
	    cm.sendOk("#e您沒有   #i1462040##k【楓葉追星之弩】#g與#k  #i1082252##k【楓葉手套】#k\r\n\r\n\        請 確 認 背 包 有 所 需 物 品 在 升 級 此 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(1462040, 1) == true) {
	    cm.sendOk("#e打開【 E 】#dL v64#k#i1462040#【楓葉追星之弩】是 否 拿 在 手 上\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有#dL v 64#k#i1462040##k【楓葉追星之弩】請 在 確 認 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(1082252, 1) == true) { 
	    cm.sendOk("#e打 開【 E 】 #dL v 13#k #i1082252#【楓葉手套】是 否 拿 在 手 上\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有  #dL v 13#k #i1082252##k【楓葉手套】請 在 確 認 物 品");
	    cm.dispose();
	} else {
	    var Mesos = cm.getPlayer().getMeso();
	    cm.sendNext("#b系統檢測中~~\r\n\\r\n\#d您 目 前 的 #i1462040#有#e#k【#c1462040#】#d ─  #r【1】#d#n個 楓 葉 追 星 之 弩\r\n\#d您 目 前 的 #i1082252#  有#e#k【#c1082252#】#d ─  #r【0】#d#n個 楓 葉 手 套#k#e【不扣除】#n\r\n\#d您 目 前 的 #i4031138#有#e#k【"+Mesos+"】#d ─  #r【"+Meso[lol] * P+"】#d#n元\r\n\              "+Mesos1+"\r\n\                                #g點擊【下頁】領取物品");
	}
    } else if (lol == 32) {
	if(!cm.haveItem(1332056, 1) == true && !cm.haveItem(1082252, 1) == true) {
	    cm.sendOk("#e您沒有   #i1332056##k【楓葉修羅短刃】#g與#k  #i1082252##k【楓葉手套】#k\r\n\r\n\        請 確 認 背 包 有 所 需 物 品 在 升 級 此 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(1332056, 1) == true) {
	    cm.sendOk("#e打開【 E 】#dL v64#k#i1332056#【楓葉修羅短刃】是 否 拿 在 手 上\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有#dL v 64#k#i1332056##k【楓葉修羅短刃】請 在 確 認 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(1082252, 1) == true) { 
	    cm.sendOk("#e打 開【 E 】 #dL v 13#k #i1082252#【楓葉手套】是 否 拿 在 手 上\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有  #dL v 13#k #i1082252##k【楓葉手套】請 在 確 認 物 品");
	    cm.dispose();
	} else {
	    var Mesos = cm.getPlayer().getMeso();
	    cm.sendNext("#b系統檢測中~~\r\n\\r\n\#d您 目 前 的 #i1332056#有#e#k【#c1332056#】#d ─  #r【1】#d#n個 楓 葉 修 羅 短 刃\r\n\#d您 目 前 的 #i1082252#  有#e#k【#c1082252#】#d ─  #r【0】#d#n個 楓 葉 手 套#k#e【不扣除】#n\r\n\#d您 目 前 的 #i4031138#有#e#k【"+Mesos+"】#d ─  #r【"+Meso[lol] * P+"】#d#n元\r\n\              "+Mesos1+"\r\n\                                #g點擊【下頁】領取物品");
	}
    } else if (lol == 33) {
	if(!cm.haveItem(1472055, 1) == true && !cm.haveItem(1082252, 1) == true) {
	    cm.sendOk("#e您沒有   #i1472055##k【楓葉神獸拳套】#g與#k  #i1082252##k【楓葉手套】#k\r\n\r\n\        請 確 認 背 包 有 所 需 物 品 在 升 級 此 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(1472055, 1) == true) {
	    cm.sendOk("#e打開【 E 】#dL v64#k#i1472055#【楓葉神獸拳套】是 否 拿 在 手 上\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有#dL v 64#k#i1472055##k【楓葉神獸拳套】請 在 確 認 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(1082252, 1) == true) { 
	    cm.sendOk("#e打 開【 E 】 #dL v 13#k #i1082252#【楓葉手套】是 否 拿 在 手 上\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有  #dL v 13#k #i1082252##k【楓葉手套】請 在 確 認 物 品");
	    cm.dispose();
	} else {
	    var Mesos = cm.getPlayer().getMeso();
	    cm.sendNext("#b系統檢測中~~\r\n\\r\n\#d您 目 前 的 #i1472055#有#e#k【#c1472055#】#d ─  #r【1】#d#n個 楓 葉 神 獸 拳 套\r\n\#d您 目 前 的 #i1082252#  有#e#k【#c1082252#】#d ─  #r【0】#d#n個 楓 葉 手 套#k#e【不扣除】#n\r\n\#d您 目 前 的 #i4031138#有#e#k【"+Mesos+"】#d ─  #r【"+Meso[lol] * P+"】#d#n元\r\n\              "+Mesos1+"\r\n\                                #g點擊【下頁】領取物品");
	}
    } else if (lol == 34) {
	if(!cm.haveItem(1482022, 1) == true && !cm.haveItem(1082252, 1) == true) {
	    cm.sendOk("#e您沒有   #i1482022##k【楓葉黃金指虎】#g與#k  #i1082252##k【楓葉手套】#k\r\n\r\n\        請 確 認 背 包 有 所 需 物 品 在 升 級 此 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(1482022, 1) == true) {
	    cm.sendOk("#e打開【 E 】#dL v64#k#i1482022#【楓葉黃金指虎】是 否 拿 在 手 上\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有#dL v 64#k#i1482022##k【楓葉黃金指虎】請 在 確 認 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(1082252, 1) == true) { 
	    cm.sendOk("#e打 開【 E 】 #dL v 13#k #i1082252#【楓葉手套】是 否 拿 在 手 上\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有  #dL v 13#k #i1082252##k【楓葉手套】請 在 確 認 物 品");
	    cm.dispose();
	} else {
	    var Mesos = cm.getPlayer().getMeso();
	    cm.sendNext("#b系統檢測中~~\r\n\\r\n\#d您 目 前 的 #i1482022#有#e#k【#c1482022#】#d ─  #r【1】#d#n個 楓 葉 黃 金 指 虎\r\n\#d您 目 前 的 #i1082252#  有#e#k【#c1082252#】#d ─  #r【0】#d#n個 楓 葉 手 套#k#e【不扣除】#n\r\n\#d您 目 前 的 #i4031138#有#e#k【"+Mesos+"】#d ─  #r【"+Meso[lol] * P+"】#d#n元\r\n\              "+Mesos1+"\r\n\                                #g點擊【下頁】領取物品");
	}
    } else if (lol == 35) {
	if(!cm.haveItem(1492022, 1) == true && !cm.haveItem(1082252, 1) == true) {
	    cm.sendOk("#e您沒有   #i1492022##k【楓葉炫風火槍】#g與#k  #i1082252##k【楓葉手套】#k\r\n\r\n\        請 確 認 背 包 有 所 需 物 品 在 升 級 此 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(1492022, 1) == true) {
	    cm.sendOk("#e打開【 E 】#dL v64#k#i1492022#【楓葉炫風火槍】是 否 拿 在 手 上\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有#dL v 64#k#i1492022##k【楓葉炫風火槍】請 在 確 認 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(1082252, 1) == true) { 
	    cm.sendOk("#e打 開【 E 】 #dL v 13#k #i1082252#【楓葉手套】是 否 拿 在 手 上\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有  #dL v 13#k #i1082252##k【楓葉手套】請 在 確 認 物 品");
	    cm.dispose();
	} else {
	    var Mesos = cm.getPlayer().getMeso();
	    cm.sendNext("#b系統檢測中~~\r\n\\r\n\#d您 目 前 的 #i1492022#有#e#k【#c1492022#】#d ─  #r【1】#d#n個 楓 葉 炫 風 火 槍\r\n\#d您 目 前 的 #i1082252#  有#e#k【#c1082252#】#d ─  #r【0】#d#n個 楓 葉 手 套#k#e【不扣除】#n\r\n\#d您 目 前 的 #i4031138#有#e#k【"+Mesos+"】#d ─  #r【"+Meso[lol] * P+"】#d#n元\r\n\              "+Mesos1+"\r\n\                                #g點擊【下頁】領取物品");
	}
   } else if (lol == 36) {
	if(!cm.haveItem(1302142, 1) == true && !cm.haveItem(1082252, 1) == true) {
	    cm.sendOk("#e您沒有   #i1302142##k【黃金楓葉劍】#g與#k  #i1082252##k【楓葉手套】#k\r\n\r\n\        請 確 認 背 包 有 所 需 物 品 在 升 級 此 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(1302142, 1) == true) {
	    cm.sendOk("#e打開【 E 】#dL v77#k#i1302142#【黃金楓葉劍】是 否 拿 在 手 上\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有#dL v 77#k#i1302142##k【黃金楓葉劍】請 在 確 認 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(1082252, 1) == true) { 
	    cm.sendOk("#e打 開【 E 】 #dL v 13#k #i1082252#【楓葉手套】是 否 拿 在 手 上\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有  #dL v 13#k #i1082252##k【楓葉手套】請 在 確 認 物 品");
	    cm.dispose();
	} else {
	    var Mesos = cm.getPlayer().getMeso();
	    cm.sendNext("#b系統檢測中~~\r\n\\r\n\#d您 目 前 的 #i1302142#有#e#k【#c1302142#】#d ─  #r【1】#d#n個 黃 金 楓 葉 劍\r\n\#d您 目 前 的 #i1082252#  有#e#k【#c1082252#】#d ─  #r【1】#d#n個 楓 葉 手 套#k#e【不扣除】#n\r\n\#d您 目 前 的 #i4031138#有#e#k【"+Mesos+"】#d ─  #r【"+Meso[lol] * P+"】#d#n元\r\n\              "+Mesos1+"\r\n\                                #g點擊【下頁】領取物品");
	}
    } else if (lol == 37) {
	if(!cm.haveItem(1312056, 1) == true && !cm.haveItem(1082252, 1) == true) {
	    cm.sendOk("#e您沒有   #i1312056##k【黃金楓葉斧】#g與#k  #i1082252##k【楓葉手套】#k\r\n\r\n\        請 確 認 背 包 有 所 需 物 品 在 升 級 此 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(1312056, 1) == true) {
	    cm.sendOk("#e打開【 E 】#dL v77#k#i1312056#【黃金楓葉斧】是 否 拿 在 手 上\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有#dL v 77#k#i1312056##k【黃金楓葉斧】請 在 確 認 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(1082252, 1) == true) { 
	    cm.sendOk("#e打 開【 E 】 #dL v 13#k #i1082252#【楓葉手套】是 否 拿 在 手 上\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有  #dL v 13#k #i1082252##k【楓葉手套】請 在 確 認 物 品");
	    cm.dispose();
	} else {
	    var Mesos = cm.getPlayer().getMeso();
	    cm.sendNext("#b系統檢測中~~\r\n\\r\n\#d您 目 前 的 #i1312056#有#e#k【#c1312056#】#d ─  #r【1】#d#n個 黃 金 楓 葉 斧\r\n\#d您 目 前 的 #i1082252#  有#e#k【#c1082252#】#d ─  #r【1】#d#n個 楓 葉 手 套#k#e【不扣除】#n\r\n\#d您 目 前 的 #i4031138#有#e#k【"+Mesos+"】#d ─  #r【"+Meso[lol] * P+"】#d#n元\r\n\              "+Mesos1+"\r\n\                                #g點擊【下頁】領取物品");
	}
    } else if (lol == 38) {
	if(!cm.haveItem(1322084, 1) == true && !cm.haveItem(1082252, 1) == true) {
	    cm.sendOk("#e您沒有   #i1322084##k【黃金楓葉錘】#g與#k  #i1082252##k【楓葉手套】#k\r\n\r\n\        請 確 認 背 包 有 所 需 物 品 在 升 級 此 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(1322084, 1) == true) {
	    cm.sendOk("#e打開【 E 】#dL v77#k#i1322084#【黃金楓葉錘】是 否 拿 在 手 上\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有#dL v 77#k#i1322084##k【黃金楓葉錘】請 在 確 認 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(1082252, 1) == true) { 
	    cm.sendOk("#e打 開【 E 】 #dL v 13#k #i1082252#【楓葉手套】是 否 拿 在 手 上\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有  #dL v 13#k #i1082252##k【楓葉手套】請 在 確 認 物 品");
	    cm.dispose();
	} else {
	    var Mesos = cm.getPlayer().getMeso();
	    cm.sendNext("#b系統檢測中~~\r\n\\r\n\#d您 目 前 的 #i1322084#有#e#k【#c1322084#】#d ─  #r【1】#d#n個 黃 金 楓 葉 錘\r\n\#d您 目 前 的 #i1082252#  有#e#k【#c1082252#】#d ─  #r【1】#d#n個 楓 葉 手 套#k#e【不扣除】#n\r\n\#d您 目 前 的 #i4031138#有#e#k【"+Mesos+"】#d ─  #r【"+Meso[lol] * P+"】#d#n元\r\n\              "+Mesos1+"\r\n\                                #g點擊【下頁】領取物品");
	}
    } else if (lol == 39) {
	if(!cm.haveItem(1402085, 1) == true && !cm.haveItem(1082252, 1) == true) {
	    cm.sendOk("#e您沒有   #i1402085##k【黃金楓葉雙手劍】#g與#k  #i1082252##k【楓葉手套】#k\r\n\r\n\        請 確 認 背 包 有 所 需 物 品 在 升 級 此 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(1402085, 1) == true) {
	    cm.sendOk("#e打開 [ E ] #dL v77#k  #i1402085#【黃金楓葉雙手劍】是否拿在手上\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您   沒   有#dL v 77#k#i1402085##k【黃金楓葉雙手劍】請在確認物品");
	    cm.dispose();
	} else if(!cm.haveItem(1082252, 1) == true) { 
	    cm.sendOk("#e打 開【 E 】 #dL v 13#k #i1082252#【楓葉手套】是 否 拿 在 手 上\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有  #dL v 13#k #i1082252##k【楓葉手套】請 在 確 認 物 品");
	    cm.dispose();
	} else {
	    var Mesos = cm.getPlayer().getMeso();
	    cm.sendNext("#b系統檢測中~~\r\n\\r\n\#d您 目 前 的 #i1402085#有#e#k【#c1402085#】#d ─  #r【1】#d#n個 黃 金 楓 葉 雙 手 劍\r\n\#d您 目 前 的 #i1082252#  有#e#k【#c1082252#】#d ─  #r【1】#d#n個 楓 葉 手 套#k#e【不扣除】#n\r\n\#d您 目 前 的 #i4031138#有#e#k【"+Mesos+"】#d ─  #r【"+Meso[lol] * P+"】#d#n元\r\n\              "+Mesos1+"\r\n\                                #g點擊【下頁】領取物品");
	}
    } else if (lol == 40) {
	if(!cm.haveItem(1412055, 1) == true && !cm.haveItem(1082252, 1) == true) {
	    cm.sendOk("#e您沒有   #i1412055##k【黃金楓葉雙手斧】#g與#k  #i1082252##k【楓葉手套】#k\r\n\r\n\        請 確 認 背 包 有 所 需 物 品 在 升 級 此 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(1412055, 1) == true) {
	    cm.sendOk("#e打開 [ E ] #dL v77#k  #i1412055#【黃金楓葉雙手斧】是否拿在手上\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您   沒   有#dL v 77#k#i1412055##k【黃金楓葉雙手斧】請在確認物品");
	    cm.dispose();
	} else if(!cm.haveItem(1082252, 1) == true) { 
	    cm.sendOk("#e打 開【 E 】 #dL v 13#k #i1082252#【楓葉手套】是 否 拿 在 手 上\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有  #dL v 13#k #i1082252##k【楓葉手套】請 在 確 認 物 品");
	    cm.dispose();
	} else {
	    var Mesos = cm.getPlayer().getMeso();
	    cm.sendNext("#b系統檢測中~~\r\n\\r\n\#d您 目 前 的 #i1412055#有#e#k【#c1412055#】#d ─  #r【1】#d#n個 黃 金 楓 葉 雙 手 斧\r\n\#d您 目 前 的 #i1082252#  有#e#k【#c1082252#】#d ─  #r【1】#d#n個 楓 葉 手 套#k#e【不扣除】#n\r\n\#d您 目 前 的 #i4031138#有#e#k【"+Mesos+"】#d ─  #r【"+Meso[lol] * P+"】#d#n元\r\n\              "+Mesos1+"\r\n\                                #g點擊【下頁】領取物品");
	}
    } else if (lol == 41) {
	if(!cm.haveItem(1422057, 1) == true && !cm.haveItem(1082252, 1) == true) {
	    cm.sendOk("#e您沒有   #i1422057##k【黃金楓葉雙手錘】#g與#k  #i1082252##k【楓葉手套】#k\r\n\r\n\        請 確 認 背 包 有 所 需 物 品 在 升 級 此 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(1422057, 1) == true) {
	    cm.sendOk("#e打開 [ E ] #dL v77#k  #i1422057#【黃金楓葉雙手錘】是否拿在手上\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您   沒   有#dL v 77#k#i1422057##k【黃金楓葉雙手錘】請在確認物品");
	    cm.dispose();
	} else if(!cm.haveItem(1082252, 1) == true) { 
	    cm.sendOk("#e打 開【 E 】 #dL v 13#k #i1082252#【楓葉手套】是 否 拿 在 手 上\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有  #dL v 13#k #i1082252##k【楓葉手套】請 在 確 認 物 品");
	    cm.dispose();
	} else {
	    var Mesos = cm.getPlayer().getMeso();
	    cm.sendNext("#b系統檢測中~~\r\n\\r\n\#d您 目 前 的 #i1422057#有#e#k【#c1422057#】#d ─  #r【1】#d#n個 黃 金 楓 葉 雙 手 斧\r\n\#d您 目 前 的 #i1082252#  有#e#k【#c1082252#】#d ─  #r【1】#d#n個 楓 葉 手 套#k#e【不扣除】#n\r\n\#d您 目 前 的 #i4031138#有#e#k【"+Mesos+"】#d ─  #r【"+Meso[lol] * P+"】#d#n元\r\n\              "+Mesos1+"\r\n\                                #g點擊【下頁】領取物品");
	}
    } else if (lol == 42) {
	if(!cm.haveItem(1432075, 1) == true && !cm.haveItem(1082252, 1) == true) {
	    cm.sendOk("#e您沒有   #i1432075##k【黃金楓葉之槍】#g與#k  #i1082252##k【楓葉手套】#k\r\n\r\n\        請 確 認 背 包 有 所 需 物 品 在 升 級 此 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(1432075, 1) == true) {
	    cm.sendOk("#e打開【E】#dL v77#k  #i1432075#【黃金楓葉之槍】是 否 拿 在 手 上\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有#dL v 77#k#i1432075##k【黃金楓葉之槍】請 在 確 認 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(1082252, 1) == true) { 
	    cm.sendOk("#e打 開【 E 】 #dL v 13#k #i1082252#【楓葉手套】是 否 拿 在 手 上\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有  #dL v 13#k #i1082252##k【楓葉手套】請 在 確 認 物 品");
	    cm.dispose();
	} else {
	    var Mesos = cm.getPlayer().getMeso();
	    cm.sendNext("#b系統檢測中~~\r\n\\r\n\#d您 目 前 的 #i1432075#有#e#k【#c1432075#】#d ─  #r【1】#d#n個 黃 金 楓 葉 之 槍\r\n\#d您 目 前 的 #i1082252#  有#e#k【#c1082252#】#d ─  #r【1】#d#n個 楓 葉 手 套#k#e【不扣除】#n\r\n\#d您 目 前 的 #i4031138#有#e#k【"+Mesos+"】#d ─  #r【"+Meso[lol] * P+"】#d#n元\r\n\              "+Mesos1+"\r\n\                                #g點擊【下頁】領取物品");
	}
    } else if (lol == 43) {
	if(!cm.haveItem(1442104, 1) == true && !cm.haveItem(1082252, 1) == true) {
	    cm.sendOk("#e您沒有   #i1442104##k【黃金楓葉長矛】#g與#k  #i1082252##k【楓葉手套】#k\r\n\r\n\        請 確 認 背 包 有 所 需 物 品 在 升 級 此 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(1442104, 1) == true) {
	    cm.sendOk("#e打開【 E 】#dL v77#k  #i1442104#【黃金楓葉長矛】是 否 拿 在 手 上\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有#dL v 77#k#i1442104##k【黃金楓葉長矛】請 在 確 認 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(1082252, 1) == true) { 
	    cm.sendOk("#e打 開【 E 】 #dL v 13#k #i1082252#【楓葉手套】是 否 拿 在 手 上\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有  #dL v 13#k #i1082252##k【楓葉手套】請 在 確 認 物 品");
	    cm.dispose();
	} else {
	    var Mesos = cm.getPlayer().getMeso();
	    cm.sendNext("#b系統檢測中~~\r\n\\r\n\#d您 目 前 的 #i1432075#有#e#k【#c1432075#】#d ─  #r【1】#d#n個 黃 金 楓 葉 長 矛\r\n\#d您 目 前 的 #i1082252#  有#e#k【#c1082252#】#d ─  #r【1】#d#n個 楓 葉 手 套#k#e【不扣除】#n\r\n\#d您 目 前 的 #i4031138#有#e#k【"+Mesos+"】#d ─  #r【"+Meso[lol] * P+"】#d#n元\r\n\              "+Mesos1+"\r\n\                                #g點擊【下頁】領取物品");
	}
    } else if (lol == 44) {
	if(!cm.haveItem(1372071, 1) == true && !cm.haveItem(1082252, 1) == true) {
	    cm.sendOk("#e您沒有   #i1372071##k【黃金楓葉短杖】#g與#k  #i1082252##k【楓葉手套】#k\r\n\r\n\        請 確 認 背 包 有 所 需 物 品 在 升 級 此 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(1372071, 1) == true) {
	    cm.sendOk("#e打開【E】#dL v77#k  #i1372071#【黃金楓葉短杖】是 否 拿 在 手 上\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有#dL v 77#k#i1372071##k【黃金楓葉短杖】請 在 確 認 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(1082252, 1) == true) { 
	    cm.sendOk("#e打 開【 E 】 #dL v 13#k #i1082252#【楓葉手套】是 否 拿 在 手 上\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有  #dL v 13#k #i1082252##k【楓葉手套】請 在 確 認 物 品");
	    cm.dispose();
	} else {
	    var Mesos = cm.getPlayer().getMeso();
	    cm.sendNext("#b系統檢測中~~\r\n\\r\n\#d您 目 前 的 #i1372071#有#e#k【#c1372071#】#d ─  #r【1】#d#n個 黃 金 楓 葉 短 杖\r\n\#d您 目 前 的 #i1082252#  有#e#k【#c1082252#】#d ─  #r【1】#d#n個 楓 葉 手 套#k#e【不扣除】#n\r\n\#d您 目 前 的 #i4031138#有#e#k【"+Mesos+"】#d ─  #r【"+Meso[lol] * P+"】#d#n元\r\n\              "+Mesos1+"\r\n\                                #g點擊【下頁】領取物品");
	}
    } else if (lol == 45) {
	if(!cm.haveItem(1382093, 1) == true && !cm.haveItem(1082252, 1) == true) {
	    cm.sendOk("#e您沒有   #i1382093##k【黃金楓葉長矛】#g與#k  #i1082252##k【楓葉手套】#k\r\n\r\n\        請 確 認 背 包 有 所 需 物 品 在 升 級 此 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(1382093, 1) == true) {
	    cm.sendOk("#e打開【E】#dL v77#k  #i1382093#【黃金楓葉長杖】是 否 拿 在 手 上\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有#dL v 77#k#i1382093##k【黃金楓葉長杖】請 在 確 認 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(1082252, 1) == true) { 
	    cm.sendOk("#e打 開【 E 】 #dL v 13#k #i1082252#【楓葉手套】是 否 拿 在 手 上\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有  #dL v 13#k #i1082252##k【楓葉手套】請 在 確 認 物 品");
	    cm.dispose();
	} else {
	    var Mesos = cm.getPlayer().getMeso();
	    cm.sendNext("#b系統檢測中~~\r\n\\r\n\#d您 目 前 的 #i1382093#有#e#k【#c1382093#】#d ─  #r【1】#d#n個 黃 金 楓 葉 長 杖\r\n\#d您 目 前 的 #i1082252#  有#e#k【#c1082252#】#d ─  #r【1】#d#n個 楓 葉 手 套#k#e【不扣除】#n\r\n\#d您 目 前 的 #i4031138#有#e#k【"+Mesos+"】#d ─  #r【"+Meso[lol] * P+"】#d#n元\r\n\              "+Mesos1+"\r\n\                                #g點擊【下頁】領取物品");
	}
    } else if (lol == 46) {
	if(!cm.haveItem(1452100, 1) == true && !cm.haveItem(1082252, 1) == true) {
	    cm.sendOk("#e您沒有   #i1452100##k【黃金楓葉之弓】#g與#k  #i1082252##k【楓葉手套】#k\r\n\r\n\        請 確 認 背 包 有 所 需 物 品 在 升 級 此 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(1452100, 1) == true) {
	    cm.sendOk("#e打開【E】#dL v77#k  #i1452100#【黃金楓葉之弓】是 否 拿 在 手 上\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有#dL v 77#k#i1452100##k【黃金楓葉之弓】請 在 確 認 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(1082252, 1) == true) { 
	    cm.sendOk("#e打 開【 E 】 #dL v 13#k #i1082252#【楓葉手套】是 否 拿 在 手 上\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有  #dL v 13#k #i1082252##k【楓葉手套】請 在 確 認 物 品");
	    cm.dispose();
	} else {
	    var Mesos = cm.getPlayer().getMeso();
	    cm.sendNext("#b系統檢測中~~\r\n\\r\n\#d您 目 前 的 #i1452100#有#e#k【#c1452100#】#d ─  #r【1】#d#n個 黃 金 楓 葉 長 弓\r\n\#d您 目 前 的 #i1082252#  有#e#k【#c1082252#】#d ─  #r【1】#d#n個 楓 葉 手 套#k#e【不扣除】#n\r\n\#d您 目 前 的 #i4031138#有#e#k【"+Mesos+"】#d ─  #r【"+Meso[lol] * P+"】#d#n元\r\n\              "+Mesos1+"\r\n\                                #g點擊【下頁】領取物品");
	}
    } else if (lol == 47) {
	if(!cm.haveItem(1462085, 1) == true && !cm.haveItem(1082252, 1) == true) {
	    cm.sendOk("#e您沒有   #i1462085##k【黃金楓葉弩】#g與#k  #i1082252##k【楓葉手套】#k\r\n\r\n\        請 確 認 背 包 有 所 需 物 品 在 升 級 此 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(1462085, 1) == true) {
	    cm.sendOk("#e打開【 E 】#dL v77#k  #i1462085#【黃金楓葉弩】是 否 拿 在 手 上\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有#dL v 77#k#i1462085##k【黃金楓葉弩】請 在 確 認 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(1082252, 1) == true) { 
	    cm.sendOk("#e打 開【 E 】 #dL v 13#k #i1082252#【楓葉手套】是 否 拿 在 手 上\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有  #dL v 13#k #i1082252##k【楓葉手套】請 在 確 認 物 品");
	    cm.dispose();
	} else {
	    var Mesos = cm.getPlayer().getMeso();
	    cm.sendNext("#b系統檢測中~~\r\n\\r\n\#d您 目 前 的 #i1462085#有#e#k【#c1462085#】#d ─  #r【1】#d#n個 黃 金 楓 葉 長 弩\r\n\#d您 目 前 的 #i1082252#  有#e#k【#c1082252#】#d ─  #r【1】#d#n個 楓 葉 手 套#k#e【不扣除】#n\r\n\#d您 目 前 的 #i4031138#有#e#k【"+Mesos+"】#d ─  #r【"+Meso[lol] * P+"】#d#n元\r\n\              "+Mesos1+"\r\n\                                #g點擊【下頁】領取物品");
	}
    } else if (lol == 48) {
	if(!cm.haveItem(1332114, 1) == true && !cm.haveItem(1082252, 1) == true) {
	    cm.sendOk("#e您沒有   #i1332114##k【黃金楓葉短刀】#g與#k  #i1082252##k【楓葉手套】#k\r\n\r\n\        請 確 認 背 包 有 所 需 物 品 在 升 級 此 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(1332114, 1) == true) {
	    cm.sendOk("#e打開【 E 】#dL v77#k  #i1332114#【黃金楓葉短刀】是 否 拿 在 手 上\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有#dL v 77#k#i1332114##k【黃金楓葉短刀】請 在 確 認 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(1082252, 1) == true) { 
	    cm.sendOk("#e打 開【 E 】 #dL v 13#k #i1082252#【楓葉手套】是 否 拿 在 手 上\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有  #dL v 13#k #i1082252##k【楓葉手套】請 在 確 認 物 品");
	    cm.dispose();
	} else {
	    var Mesos = cm.getPlayer().getMeso();
	    cm.sendNext("#b系統檢測中~~\r\n\\r\n\#d您 目 前 的 #i1332114#有#e#k【#c1332114#】#d ─  #r【1】#d#n個 黃 金 楓 葉 短 刀\r\n\#d您 目 前 的 #i1082252#  有#e#k【#c1082252#】#d ─  #r【1】#d#n個 楓 葉 手 套#k#e【不扣除】#n\r\n\#d您 目 前 的 #i4031138#有#e#k【"+Mesos+"】#d ─  #r【"+Meso[lol] * P+"】#d#n元\r\n\              "+Mesos1+"\r\n\                                #g點擊【下頁】領取物品");
	}
    } else if (lol == 49) {
	if(!cm.haveItem(1472111, 1) == true && !cm.haveItem(1082252, 1) == true) {
	    cm.sendOk("#e您沒有   #i1472111##k【黃金楓葉拳套】#g與#k  #i1082252##k【楓葉手套】#k\r\n\r\n\        請 確 認 背 包 有 所 需 物 品 在 升 級 此 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(1472111, 1) == true) {
	    cm.sendOk("#e打開【E】#dL v77#k  #i1472111#【黃金楓葉拳套】是 否 拿 在 手 上\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有#dL v 77#k#i1472111##k【黃金楓葉拳套】請 在 確 認 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(1082252, 1) == true) { 
	    cm.sendOk("#e打 開【 E 】 #dL v 13#k #i1082252#【楓葉手套】是 否 拿 在 手 上\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有  #dL v 13#k #i1082252##k【楓葉手套】請 在 確 認 物 品");
	    cm.dispose();
	} else {
	    var Mesos = cm.getPlayer().getMeso();
	    cm.sendNext("#b系統檢測中~~\r\n\\r\n\#d您 目 前 的 #i1472111#有#e#k【#c1472111#】#d ─  #r【1】#d#n個 黃 金 楓 葉 拳 套\r\n\#d您 目 前 的 #i1082252#  有#e#k【#c1082252#】#d ─  #r【1】#d#n個 楓 葉 手 套#k#e【不扣除】#n\r\n\#d您 目 前 的 #i4031138#有#e#k【"+Mesos+"】#d ─  #r【"+Meso[lol] * P+"】#d#n元\r\n\              "+Mesos1+"\r\n\                                #g點擊【下頁】領取物品");
	}
    } else if (lol == 50) {
	if(!cm.haveItem(1482073, 1) == true && !cm.haveItem(1082252, 1) == true) {
	    cm.sendOk("#e您沒有   #i1482073##k【黃金楓葉指虎】#g與#k  #i1082252##k【楓葉手套】#k\r\n\r\n\        請 確 認 背 包 有 所 需 物 品 在 升 級 此 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(1482073, 1) == true) {
	    cm.sendOk("#e打開【 E 】#dL v77#k  #i1482073#【黃金楓葉指虎】是 否 拿 在 手 上\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有#dL v 77#k#i1482073##k【黃金楓葉指虎】請 在 確 認 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(1082252, 1) == true) { 
	    cm.sendOk("#e打 開【 E 】 #dL v 13#k #i1082252#【楓葉手套】是 否 拿 在 手 上\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有  #dL v 13#k #i1082252##k【楓葉手套】請 在 確 認 物 品");
	    cm.dispose();
	} else {
	    var Mesos = cm.getPlayer().getMeso();
	    cm.sendNext("#b系統檢測中~~\r\n\\r\n\#d您 目 前 的 #i1482073#有#e#k【#c1482073#】#d ─  #r【1】#d#n個 黃 金 楓 葉 指 虎\r\n\#d您 目 前 的 #i1082252#  有#e#k【#c1082252#】#d ─  #r【1】#d#n個 楓 葉 手 套#k#e【不扣除】#n\r\n\#d您 目 前 的 #i4031138#有#e#k【"+Mesos+"】#d ─  #r【"+Meso[lol] * P+"】#d#n元\r\n\              "+Mesos1+"\r\n\                                #g點擊【下頁】領取物品");
	}
    } else if (lol == 51) {
	if(!cm.haveItem(1492073, 1) == true && !cm.haveItem(1082252, 1) == true) {
	    cm.sendOk("#e您沒有   #i1492073##k【黃金楓葉火槍】#g與#k  #i1082252##k【楓葉手套】#k\r\n\r\n\        請 確 認 背 包 有 所 需 物 品 在 升 級 此 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(1492073, 1) == true) {
	    cm.sendOk("#e打開【E】#dL v77#k  #i1492073#【黃金楓葉火槍】是 否 拿 在 手 上\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有#dL v 77#k#i1492073##k【黃金楓葉火槍】請 在 確 認 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(1082252, 1) == true) { 
	    cm.sendOk("#e打 開【 E 】 #dL v 13#k #i1082252#【楓葉手套】是 否 拿 在 手 上\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有  #dL v 13#k #i1082252##k【楓葉手套】請 在 確 認 物 品");
	    cm.dispose();
	} else {
	    var Mesos = cm.getPlayer().getMeso();
	    cm.sendNext("#b系統檢測中~~\r\n\\r\n\#d您 目 前 的 #i1492073#有#e#k【#c1492073#】#d ─  #r【1】#d#n個 黃 金 楓 葉 火 槍\r\n\#d您 目 前 的 #i1082252#  有#e#k【#c1082252#】#d ─  #r【1】#d#n個 楓 葉 手 套#k#e【不扣除】#n\r\n\#d您 目 前 的 #i4031138#有#e#k【"+Mesos+"】#d ─  #r【"+Meso[lol] * P+"】#d#n元\r\n\              "+Mesos1+"\r\n\                                #g點擊【下頁】領取物品");
	}
    } else if (lol == 52) {
	if(!cm.haveItem(4280000, Item[lol]) == true) {
	    cm.sendOk("#e您 的 材 料 尚 未 齊 全 請 在 確 認 一 次\r\n\\r\n\所 有 的  #i4280000#  有【#c4280000#】個                     #r│ 需要"+Item[lol]+"個 │");
	    cm.dispose();
	} else {
	    cm.sendNext("#b系統檢測中~~\r\n\\r\n\#d您 目 前 的 #i4280000# 有#e#k【#c4280000#】#d ─  #r【"+Item[lol]+"】#d#n個 超 級 轉 蛋 券\r\n\\r\n\                              #g點擊【下頁】開始#e#r隨機#g#n領取");
	}
    } else if (lol == 53) {
	if(!cm.haveItem(4280000, Item[lol]) == true) {
	    cm.sendOk("#e您 的 材 料 尚 未 齊 全 請 在 確 認 一 次\r\n\\r\n\所 有 的  #i4280000#  有【#c4280000#】個                     #r│ 需要"+Item[lol]+"個 │");
	    cm.dispose();
	} else {
	    cm.sendNext("#b系統檢測中~~\r\n\\r\n\#d您 目 前 的 #i4280000# 有#e#k【#c4280000#】#d ─  #r【"+Item[lol]+"】#d#n個 超 級 轉 蛋 券\r\n\\r\n\                              #g點擊【下頁】開始#e#r隨機#g#n領取");
	}
    } else if (lol == 54) {
	if(!cm.haveItem(4280000, Item[lol]) == true) {
	    cm.sendOk("#e您 的 材 料 尚 未 齊 全 請 在 確 認 一 次\r\n\\r\n\所 有 的  #i4280000#  有【#c4280000#】個                     #r│ 需要"+Item[lol]+"個 │");
	    cm.dispose();
	} else {
	    cm.sendNext("#b系統檢測中~~\r\n\\r\n\#d您 目 前 的 #i4280000# 有#e#k【#c4280000#】#d ─  #r【"+Item[lol]+"】#d#n個 超 級 轉 蛋 券\r\n\\r\n\                              #g點擊【下頁】開始#e#r隨機#g#n領取");
	}
    } else if (lol == 55) {
	if(cm.haveItem(1002776) && cm.haveItem(1052155) && cm.haveItem(1082234) && cm.haveItem(1072355) && cm.haveItem(1003177) && cm.haveItem(1052319) && cm.haveItem(1082300) && cm.haveItem(1072490)) {
	    var Mesos = cm.getPlayer().getMeso();
	    cm.sendNext("#b系統檢測中~~\r\n\r\n\#d您 目 前 的 #i4031138#有#e#k【"+Mesos+"】#d ─  #r【"+Meso[lol] * P+"】#d#n元\r\n\              "+Mesos1+"\r\n\                                #g點擊【下頁】合成物品");
	} else {
	    cm.sendOk("#e您必須帶來#r完整的#k兩套套裝\r\n\r\n\#i1002776##i1052155# #i1082234##i1072355#  L v 120 永恆套裝\r\n\#i1003177##i1052319##i1082300##i1072490#  L v 130 血腥套裝");
	    cm.dispose();
	}
    } else if (lol == 56) {
	if(cm.haveItem(1002777) && cm.haveItem(1052156) && cm.haveItem(1082235) && cm.haveItem(1072356) && cm.haveItem(1003178) && cm.haveItem(1052320) && cm.haveItem(1082301) && cm.haveItem(1072491)) {
	    var Mesos = cm.getPlayer().getMeso();
	    cm.sendNext("#b系統檢測中~~\r\n\r\n\#d您 目 前 的 #i4031138#有#e#k【"+Mesos+"】#d ─  #r【"+Meso[lol] * P+"】#d#n元\r\n\              "+Mesos1+"\r\n\                                #g點擊【下頁】合成物品");
	} else {
	    cm.sendOk("#e您必須帶來#r完整的#k兩套套裝\r\n\r\n\#i1002777##i1052156##i1082235##i1072356#  L v 120 永恆套裝\r\n\#i1003178# #i1052320##i1082301##i1072491#  L v 130 血腥套裝");
	    cm.dispose();
	}
    } else if (lol == 57) {
	if(cm.haveItem(1002778) && cm.haveItem(1052157) && cm.haveItem(1082236) && cm.haveItem(1072357) && cm.haveItem(1003179) && cm.haveItem(1052321) && cm.haveItem(1082302) && cm.haveItem(1072492)) {
	    var Mesos = cm.getPlayer().getMeso();
	    cm.sendNext("#b系統檢測中~~\r\n\r\n\#d您 目 前 的 #i4031138#有#e#k【"+Mesos+"】#d ─  #r【"+Meso[lol] * P+"】#d#n元\r\n\              "+Mesos1+"\r\n\                                #g點擊【下頁】合成物品");
	} else {
	    cm.sendOk("#e您必須帶來#r完整的#k兩套套裝\r\n\r\n\#i1002778##i1052157##i1082236##i1072357#  L v 120 永恆套裝\r\n\#i1003179##i1052321##i1082302##i1072492#  L v 130 血腥套裝");
	    cm.dispose();
	}
    } else if (lol == 58) {
	if(cm.haveItem(1002779) && cm.haveItem(1052158) && cm.haveItem(1082237) && cm.haveItem(1072358) && cm.haveItem(1003180) && cm.haveItem(1052322) && cm.haveItem(1082303) && cm.haveItem(1072493)) {
	    var Mesos = cm.getPlayer().getMeso();
	    cm.sendNext("#b系統檢測中~~\r\n\r\n\#d您 目 前 的 #i4031138#有#e#k【"+Mesos+"】#d ─  #r【"+Meso[lol] * P+"】#d#n元\r\n\              "+Mesos1+"\r\n\                                #g點擊【下頁】合成物品");
	} else {
	    cm.sendOk("#e您必須帶來#r完整的#k兩套套裝\r\n\r\n\#i1002779# #i1052158# #i1082237# #i1072358#  L v 120 永恆套裝\r\n\#i1003180##i1052322##i1082303##i1072493#  L v 130 血腥套裝");
	    cm.dispose();
	}
    } else if (lol == 59) {
	if(cm.haveItem(1002780) && cm.haveItem(1052159) && cm.haveItem(1082238) && cm.haveItem(1072359) && cm.haveItem(1003181) && cm.haveItem(1052323) && cm.haveItem(1082304) && cm.haveItem(1072494)) {
	    var Mesos = cm.getPlayer().getMeso();
	    cm.sendNext("#b系統檢測中~~\r\n\r\n\#d您 目 前 的 #i4031138#有#e#k【"+Mesos+"】#d ─  #r【"+Meso[lol] * P+"】#d#n元\r\n\              "+Mesos1+"\r\n\                                #g點擊【下頁】合成物品");
	} else {
	    cm.sendOk("#e您必須帶來#r完整的#k兩套套裝\r\n\r\n\#i1002780##i1052159# #i1082238##i1072359#  L v 120 永恆套裝\r\n\#i1003181##i1052323##i1082304##i1072494#  L v 130 血腥套裝");
	    cm.dispose();
	}
    } else if (lol == 60) {
	if(!cm.haveItem(1102198, 1) == true && !cm.haveItem(4001126, Item[lol]) == true) {
	    cm.sendOk("#e您沒有 #dL v 30#k #i1102198##k【#t1102198#】#g與#k 楓葉需要 #r"+Item[lol]+" 個#k\r\n\r\n\        請 確 認 背 包 有 所 需 物 品 在 升 級 此 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(4001126, Item[lol]) == true) { 
	    cm.sendOk("#e您 的 材 料 尚 未 齊 全 請 在 確 認 一 次\r\n\\r\n\所 有 的  #i4001126#  有【#c4001126#】個                     #r│ 需要"+Item[lol]+"個 │");
	    cm.dispose();
	} else if(!cm.haveItem(1102198, 1) == true) { 
	    cm.sendOk("#e打 開【 E 】#dL v 30#k#i1102198#【#t1102198#】是 否 帶 在 頭 上\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有 #dL v 30#k#i1102198##k【#t1102198#】請 在 確 認 物 品");
	    cm.dispose();
	} else {
	    var Mesos = cm.getPlayer().getMeso();
	    cm.sendNext("#b系統檢測中~~\r\n\\r\n\#d您 目 前 的 #i4001126# 有#e#k【#c4001126#】#d ─  #r【"+Item[lol]+"】#d#n個 楓 葉\r\n\#d您 目 前 的 #i1102198# 有#e#k【#c1102198#】#d ─  #r【1】#d#n個 披 風#k#e【L v  30】#n\r\n\#d您 目 前 的 #i4031138# 有#e#k【"+Mesos+"】#d ─  #r【"+Meso[lol] * P+"】#d#n元\r\n\              "+Mesos1+"\r\n\                                #g點擊【下頁】領取物品");
	}
    } else if (lol == 61) {
	if(!cm.haveItem(1102198, 1) == true && !cm.haveItem(4001126, Item[lol]) == true) {
	    cm.sendOk("#e您沒有 #dL v 30#k #i1102198##k【#t1102198#】#g與#k 楓葉需要 #r"+Item[lol]+" 個#k\r\n\r\n\        請 確 認 背 包 有 所 需 物 品 在 升 級 此 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(4001126, Item[lol]) == true) { 
	    cm.sendOk("#e您 的 材 料 尚 未 齊 全 請 在 確 認 一 次\r\n\\r\n\所 有 的  #i4001126#  有【#c4001126#】個                     #r│ 需要"+Item[lol]+"個 │");
	    cm.dispose();
	} else if(!cm.haveItem(1102198, 1) == true) { 
	    cm.sendOk("#e打 開【 E 】#dL v 30#k#i1102198#【#t1102198#】是 否 帶 在 頭 上\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有 #dL v 30#k#i1102198##k【#t1102198#】請 在 確 認 物 品");
	    cm.dispose();
	} else {
	    var Mesos = cm.getPlayer().getMeso();
	    cm.sendNext("#b系統檢測中~~\r\n\\r\n\#d您 目 前 的 #i4001126# 有#e#k【#c4001126#】#d ─  #r【"+Item[lol]+"】#d#n個 楓 葉\r\n\#d您 目 前 的 #i1102198# 有#e#k【#c1102198#】#d ─  #r【1】#d#n個 披 風#k#e【L v  30】#n\r\n\#d您 目 前 的 #i4031138# 有#e#k【"+Mesos+"】#d ─  #r【"+Meso[lol] * P+"】#d#n元\r\n\              "+Mesos1+"\r\n\                                #g點擊【下頁】領取物品");
	}
    } else if (lol == 62) {
	if(!cm.haveItem(1102198, 1) == true && !cm.haveItem(4001126, Item[lol]) == true) {
	    cm.sendOk("#e您沒有 #dL v 30#k #i1102198##k【#t1102198#】#g與#k 楓葉需要 #r"+Item[lol]+" 個#k\r\n\r\n\        請 確 認 背 包 有 所 需 物 品 在 升 級 此 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(4001126, Item[lol]) == true) { 
	    cm.sendOk("#e您 的 材 料 尚 未 齊 全 請 在 確 認 一 次\r\n\\r\n\所 有 的  #i4001126#  有【#c4001126#】個                     #r│ 需要"+Item[lol]+"個 │");
	    cm.dispose();
	} else if(!cm.haveItem(1102198, 1) == true) { 
	    cm.sendOk("#e打 開【 E 】#dL v 30#k#i1102198#【#t1102198#】是 否 帶 在 頭 上\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有 #dL v 30#k#i1102198##k【#t1102198#】請 在 確 認 物 品");
	    cm.dispose();
	} else {
	    var Mesos = cm.getPlayer().getMeso();
	    cm.sendNext("#b系統檢測中~~\r\n\\r\n\#d您 目 前 的 #i4001126# 有#e#k【#c4001126#】#d ─  #r【"+Item[lol]+"】#d#n個 楓 葉\r\n\#d您 目 前 的 #i1102198# 有#e#k【#c1102198#】#d ─  #r【1】#d#n個 披 風#k#e【L v  30】#n\r\n\#d您 目 前 的 #i4031138# 有#e#k【"+Mesos+"】#d ─  #r【"+Meso[lol] * P+"】#d#n元\r\n\              "+Mesos1+"\r\n\                                #g點擊【下頁】領取物品");
	}
    } else if (lol == 63) {
	if(!cm.haveItem(1102198, 1) == true && !cm.haveItem(4001126, Item[lol]) == true) {
	    cm.sendOk("#e您沒有 #dL v 30#k #i1102198##k【#t1102198#】#g與#k 楓葉需要 #r"+Item[lol]+" 個#k\r\n\r\n\        請 確 認 背 包 有 所 需 物 品 在 升 級 此 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(4001126, Item[lol]) == true) { 
	    cm.sendOk("#e您 的 材 料 尚 未 齊 全 請 在 確 認 一 次\r\n\\r\n\所 有 的  #i4001126#  有【#c4001126#】個                     #r│ 需要"+Item[lol]+"個 │");
	    cm.dispose();
	} else if(!cm.haveItem(1102198, 1) == true) { 
	    cm.sendOk("#e打 開【 E 】#dL v 30#k#i1102198#【#t1102198#】是 否 帶 在 頭 上\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有 #dL v 30#k#i1102198##k【#t1102198#】請 在 確 認 物 品");
	    cm.dispose();
	} else {
	    var Mesos = cm.getPlayer().getMeso();
	    cm.sendNext("#b系統檢測中~~\r\n\\r\n\#d您 目 前 的 #i4001126# 有#e#k【#c4001126#】#d ─  #r【"+Item[lol]+"】#d#n個 楓 葉\r\n\#d您 目 前 的 #i1102198# 有#e#k【#c1102198#】#d ─  #r【1】#d#n個 披 風#k#e【L v  30】#n\r\n\#d您 目 前 的 #i4031138# 有#e#k【"+Mesos+"】#d ─  #r【"+Meso[lol] * P+"】#d#n元\r\n\              "+Mesos1+"\r\n\                                #g點擊【下頁】領取物品");
	}
    } else if (lol == 64) {
	if(!cm.haveItem(1102198, 1) == true && !cm.haveItem(4001126, Item[lol]) == true) {
	    cm.sendOk("#e您沒有 #dL v 30#k #i1102198##k【#t1102198#】#g與#k 楓葉需要 #r"+Item[lol]+" 個#k\r\n\r\n\        請 確 認 背 包 有 所 需 物 品 在 升 級 此 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(4001126, Item[lol]) == true) { 
	    cm.sendOk("#e您 的 材 料 尚 未 齊 全 請 在 確 認 一 次\r\n\\r\n\所 有 的  #i4001126#  有【#c4001126#】個                     #r│ 需要"+Item[lol]+"個 │");
	    cm.dispose();
	} else if(!cm.haveItem(1102198, 1) == true) { 
	    cm.sendOk("#e打 開【 E 】#dL v 30#k#i1102198#【#t1102198#】是 否 帶 在 頭 上\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有 #dL v 30#k#i1102198##k【#t1102198#】請 在 確 認 物 品");
	    cm.dispose();
	} else {
	    var Mesos = cm.getPlayer().getMeso();
	    cm.sendNext("#b系統檢測中~~\r\n\\r\n\#d您 目 前 的 #i4001126# 有#e#k【#c4001126#】#d ─  #r【"+Item[lol]+"】#d#n個 楓 葉\r\n\#d您 目 前 的 #i1102198# 有#e#k【#c1102198#】#d ─  #r【1】#d#n個 披 風#k#e【L v  30】#n\r\n\#d您 目 前 的 #i4031138# 有#e#k【"+Mesos+"】#d ─  #r【"+Meso[lol] * P+"】#d#n元\r\n\              "+Mesos1+"\r\n\                                #g點擊【下頁】領取物品");
	}
    } else if (lol == 65) {
	if(!cm.haveItem(1102198, 1) == true && !cm.haveItem(4001126, Item[lol]) == true) {
	    cm.sendOk("#e您沒有 #dL v 30#k #i1102198##k【#t1102198#】#g與#k 楓葉需要 #r"+Item[lol]+" 個#k\r\n\r\n\        請 確 認 背 包 有 所 需 物 品 在 升 級 此 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(4001126, Item[lol]) == true) { 
	    cm.sendOk("#e您 的 材 料 尚 未 齊 全 請 在 確 認 一 次\r\n\\r\n\所 有 的  #i4001126#  有【#c4001126#】個                     #r│ 需要"+Item[lol]+"個 │");
	    cm.dispose();
	} else if(!cm.haveItem(1102198, 1) == true) { 
	    cm.sendOk("#e打 開【 E 】#dL v 30#k#i1102198#【#t1102198#】是 否 帶 在 頭 上\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有 #dL v 30#k#i1102198##k【#t1102198#】請 在 確 認 物 品");
	    cm.dispose();
	} else {
	    var Mesos = cm.getPlayer().getMeso();
	    cm.sendNext("#b系統檢測中~~\r\n\\r\n\#d您 目 前 的 #i4001126# 有#e#k【#c4001126#】#d ─  #r【"+Item[lol]+"】#d#n個 楓 葉\r\n\#d您 目 前 的 #i1102198# 有#e#k【#c1102198#】#d ─  #r【1】#d#n個 披 風#k#e【L v  30】#n\r\n\#d您 目 前 的 #i4031138# 有#e#k【"+Mesos+"】#d ─  #r【"+Meso[lol] * P+"】#d#n元\r\n\              "+Mesos1+"\r\n\                                #g點擊【下頁】領取物品");
	}
    } else if (lol == 66) {
	if(!cm.haveItem(1102198, 1) == true && !cm.haveItem(4001126, Item[lol]) == true) {
	    cm.sendOk("#e您沒有 #dL v 30#k #i1102198##k【#t1102198#】#g與#k 楓葉需要 #r"+Item[lol]+" 個#k\r\n\r\n\        請 確 認 背 包 有 所 需 物 品 在 升 級 此 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(4001126, Item[lol]) == true) { 
	    cm.sendOk("#e您 的 材 料 尚 未 齊 全 請 在 確 認 一 次\r\n\\r\n\所 有 的  #i4001126#  有【#c4001126#】個                     #r│ 需要"+Item[lol]+"個 │");
	    cm.dispose();
	} else if(!cm.haveItem(1102198, 1) == true) { 
	    cm.sendOk("#e打 開【 E 】#dL v 30#k#i1102198#【#t1102198#】是 否 帶 在 頭 上\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有 #dL v 30#k#i1102198##k【#t1102198#】請 在 確 認 物 品");
	    cm.dispose();
	} else {
	    var Mesos = cm.getPlayer().getMeso();
	    cm.sendNext("#b系統檢測中~~\r\n\\r\n\#d您 目 前 的 #i4001126# 有#e#k【#c4001126#】#d ─  #r【"+Item[lol]+"】#d#n個 楓 葉\r\n\#d您 目 前 的 #i1102198# 有#e#k【#c1102198#】#d ─  #r【1】#d#n個 披 風#k#e【L v  30】#n\r\n\#d您 目 前 的 #i4031138# 有#e#k【"+Mesos+"】#d ─  #r【"+Meso[lol] * P+"】#d#n元\r\n\              "+Mesos1+"\r\n\                                #g點擊【下頁】領取物品");
	}
    } else if (lol == 67) {
	if(!cm.haveItem(1102198, 1) == true && !cm.haveItem(4001126, Item[lol]) == true) {
	    cm.sendOk("#e您沒有 #dL v 30#k #i1102198##k【#t1102198#】#g與#k 楓葉需要 #r"+Item[lol]+" 個#k\r\n\r\n\        請 確 認 背 包 有 所 需 物 品 在 升 級 此 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(4001126, Item[lol]) == true) { 
	    cm.sendOk("#e您 的 材 料 尚 未 齊 全 請 在 確 認 一 次\r\n\\r\n\所 有 的  #i4001126#  有【#c4001126#】個                     #r│ 需要"+Item[lol]+"個 │");
	    cm.dispose();
	} else if(!cm.haveItem(1102198, 1) == true) { 
	    cm.sendOk("#e打 開【 E 】#dL v 30#k#i1102198#【#t1102198#】是 否 帶 在 頭 上\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有 #dL v 30#k#i1102198##k【#t1102198#】請 在 確 認 物 品");
	    cm.dispose();
	} else {
	    var Mesos = cm.getPlayer().getMeso();
	    cm.sendNext("#b系統檢測中~~\r\n\\r\n\#d您 目 前 的 #i4001126# 有#e#k【#c4001126#】#d ─  #r【"+Item[lol]+"】#d#n個 楓 葉\r\n\#d您 目 前 的 #i1102198# 有#e#k【#c1102198#】#d ─  #r【1】#d#n個 披 風#k#e【L v  30】#n\r\n\#d您 目 前 的 #i4031138# 有#e#k【"+Mesos+"】#d ─  #r【"+Meso[lol] * P+"】#d#n元\r\n\              "+Mesos1+"\r\n\                                #g點擊【下頁】領取物品");
	}
    } else if (lol == 68) {
	if(!cm.haveItem(1102198, 1) == true && !cm.haveItem(4001126, Item[lol]) == true) {
	    cm.sendOk("#e您沒有 #dL v 30#k #i1102198##k【#t1102198#】#g與#k 楓葉需要 #r"+Item[lol]+" 個#k\r\n\r\n\        請 確 認 背 包 有 所 需 物 品 在 升 級 此 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(4001126, Item[lol]) == true) { 
	    cm.sendOk("#e您 的 材 料 尚 未 齊 全 請 在 確 認 一 次\r\n\\r\n\所 有 的  #i4001126#  有【#c4001126#】個                     #r│ 需要"+Item[lol]+"個 │");
	    cm.dispose();
	} else if(!cm.haveItem(1102198, 1) == true) { 
	    cm.sendOk("#e打 開【 E 】#dL v 30#k#i1102198#【#t1102198#】是 否 帶 在 頭 上\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有 #dL v 30#k#i1102198##k【#t1102198#】請 在 確 認 物 品");
	    cm.dispose();
	} else {
	    var Mesos = cm.getPlayer().getMeso();
	    cm.sendNext("#b系統檢測中~~\r\n\\r\n\#d您 目 前 的 #i4001126# 有#e#k【#c4001126#】#d ─  #r【"+Item[lol]+"】#d#n個 楓 葉\r\n\#d您 目 前 的 #i1102198# 有#e#k【#c1102198#】#d ─  #r【1】#d#n個 披 風#k#e【L v  30】#n\r\n\#d您 目 前 的 #i4031138# 有#e#k【"+Mesos+"】#d ─  #r【"+Meso[lol] * P+"】#d#n元\r\n\              "+Mesos1+"\r\n\                                #g點擊【下頁】領取物品");
	}
    } else if (lol == 69) {
	if(!cm.haveItem(1102198, 1) == true && !cm.haveItem(4001126, Item[lol]) == true) {
	    cm.sendOk("#e您沒有 #dL v 30#k #i1102198##k【#t1102198#】#g與#k 楓葉需要 #r"+Item[lol]+" 個#k\r\n\r\n\        請 確 認 背 包 有 所 需 物 品 在 升 級 此 物 品");
	    cm.dispose();
	} else if(!cm.haveItem(4001126, Item[lol]) == true) { 
	    cm.sendOk("#e您 的 材 料 尚 未 齊 全 請 在 確 認 一 次\r\n\\r\n\所 有 的  #i4001126#  有【#c4001126#】個                     #r│ 需要"+Item[lol]+"個 │");
	    cm.dispose();
	} else if(!cm.haveItem(1102198, 1) == true) { 
	    cm.sendOk("#e打 開【 E 】#dL v 30#k#i1102198#【#t1102198#】是 否 帶 在 頭 上\r\n\\r\n\                                #r或  者#k\r\n\\r\n\您    沒    有 #dL v 30#k#i1102198##k【#t1102198#】請 在 確 認 物 品");
	    cm.dispose();
	} else {
	    var Mesos = cm.getPlayer().getMeso();
	    cm.sendNext("#b系統檢測中~~\r\n\\r\n\#d您 目 前 的 #i4001126# 有#e#k【#c4001126#】#d ─  #r【"+Item[lol]+"】#d#n個 楓 葉\r\n\#d您 目 前 的 #i1102198# 有#e#k【#c1102198#】#d ─  #r【1】#d#n個 披 風#k#e【L v  30】#n\r\n\#d您 目 前 的 #i4031138# 有#e#k【"+Mesos+"】#d ─  #r【"+Meso[lol] * P+"】#d#n元\r\n\              "+Mesos1+"\r\n\                                #g點擊【下頁】領取物品");
	}
    }
}

function V() {
  cm.gainMeso(-Meso[lol] * P);
    if (lol == 0) {
	cm.gainItem(4001126,-Item[lol]);
	cm.gainItem(1002508,1);
	cm.sendOk("#e打開【 I 】道具欄內有沒有拿到#rL v 0#i1002508##k【楓葉頭盔】");
	cm.dispose();
    } else if (lol == 1) {
	cm.gainItem(4001126,-Item[lol]);
	cm.gainItem(1002508,-1);
	cm.gainItem(1002509,1);
	cm.sendOk("#e打開【 I 】道具欄內有沒有拿到#rL v 8#i1002509##k【楓葉頭盔】");
	cm.dispose();
    } else if (lol == 2) {
	cm.gainItem(4001126,-Item[lol]);
	cm.gainItem(1002509,-1);
	cm.gainItem(1002510,1);
	cm.sendOk("#e打開【 I 】道具欄內有沒有拿到#rL v 30#i1002510##k【楓葉頭盔】");
	cm.dispose();
    } else if (lol == 3) {
	cm.gainItem(4001126,-Item[lol]);
	cm.gainItem(1002510,-1);
	cm.gainItem(1002511,1);
	cm.sendOk("#e打開【 I 】道具欄內有沒有拿到#rL v 70#i1002511##k【楓葉頭盔】");
	cm.dispose();
    } else if (lol == 4) {
	cm.gainItem(4001126,-Item[lol]);
	cm.gainItem(1002511,-1);
	cm.gainItem(1002758,1);
	cm.sendOk("#e打開【 I 】道具欄內有沒有拿到#rL v 90#i1002758##k【楓葉頭盔】");
	cm.dispose();
    } else if (lol == 5) {
	cm.gainItem(4001126,-Item[lol]);
	cm.gainItem(1032040,1);
	cm.sendOk("#e打開【 I 】道具欄內有沒有拿到#rL v 20#i1032040##k【赤光耳環】");
	cm.dispose();
    } else if (lol == 6) {
	cm.gainItem(4001126,-Item[lol]);
	cm.gainItem(1032040,-1);
	cm.gainItem(1032041,1);
	cm.sendOk("#e打開【 I 】道具欄內有沒有拿到#rL v 40#i1032041##k【赤光耳環】");
	cm.dispose();
    } else if (lol == 7) {
	cm.gainItem(4001126,-Item[lol]);
	cm.gainItem(1032041,-1);
	cm.gainItem(1032042,1);
	cm.sendOk("#e打開【 I 】道具欄內有沒有拿到#rL v 70#i1032042##k【赤光耳環】");
	cm.dispose();
    } else if (lol == 8) {
	cm.gainItem(4001126,-Item[lol]);
	cm.gainItem(1102166,1);
	cm.sendOk("#e打開【 I 】道具欄內有沒有拿到#rL v 20#i1102166##k【楓葉披風】");
	cm.dispose();
    } else if (lol == 9) {
	cm.gainItem(4001126,-Item[lol]);
	cm.gainItem(1102166,-1);
	cm.gainItem(1102167,1);
	cm.sendOk("#e打開【 I 】道具欄內有沒有拿到#rL v 40#i1102167##k【楓葉披風】");
 	cm.dispose();
    } else if (lol == 10) {
	cm.gainItem(4001126,-Item[lol]);
	cm.gainItem(1102167,-1);
	cm.gainItem(1102168,1);
	cm.sendOk("#e打開【 I 】道具欄內有沒有拿到#rL v 70#i1102168##k【楓葉披風】");
 	cm.dispose();
    } else if (lol == 11) {
	cm.gainItem(4001126,-Item[lol]);
	cm.gainItem(1102168,-1);
	cm.gainItem(1102071,1);
	cm.sendOk("#e打開【 I 】道具欄內有沒有拿到#rL v 70#i1102071##k【真  披風】");
	cm.dispose();
    } else if (lol == 12) {
	cm.gainItem(4001126,-Item[lol]);
	cm.gainItem(1102071,-1);
	cm.gainItem(1102198,1);
	cm.sendOk("#e打開【 I 】道具欄內有沒有拿到#rL v 30#i1102198##k【黃金披風】");
	cm.dispose();
    } else if (lol == 13) {
	cm.gainItem(4001126,-Item[lol]);
	cm.gainItem(1092030,1);
	cm.sendOk("#e打開【 I 】道具欄內有沒有拿到#rL v 20#i1092030##k【楓葉之盾】");
 	cm.dispose();
    } else if (lol == 14) {
	cm.gainItem(4001126,-Item[lol]);
	cm.gainItem(1092030,-1);
	cm.gainItem(1092045,1);
	cm.sendOk("#e打開【 I 】道具欄內有沒有拿到#rL v 64#i1092045##k【楓葉法盾】");
	cm.dispose();
    } else if (lol == 15) {
	cm.gainItem(4001126,-Item[lol]);
	cm.gainItem(1092030,-1);
	cm.gainItem(1092046,1);
	cm.sendOk("#e打開【 I 】道具欄內有沒有拿到#rL v 64#i1092046##k【楓葉戰盾】");
 	cm.dispose();
    } else if (lol == 16) {
	cm.gainItem(4001126,-Item[lol]);
	cm.gainItem(1092030,-1);
	cm.gainItem(1092047,1);
	cm.sendOk("#e打開【 I 】道具欄內有沒有拿到#rL v 64#i1092047##k【楓葉護腕】");
	cm.dispose();
    } else if (lol == 17) {
	cm.gainItem(4001126,-Item[lol]);
	cm.gainItem(1092045,-1);
	cm.gainItem(1092057,1);
	cm.sendOk("#e打開【 I 】道具欄內有沒有拿到#i1092057##k#b【#t1092057#】\r\n\                                                          #rL v 120");
	cm.dispose();
    } else if (lol == 18) {
	cm.gainItem(4001126,-Item[lol]);
	cm.gainItem(1092046,-1);
	cm.gainItem(1092058,1);
	cm.sendOk("#e打開【 I 】道具欄內有沒有拿到#i1092058##k#b【#t1092058#】\r\n\                                                          #rL v 120");
	cm.dispose();
    } else if (lol == 19) {
	cm.gainItem(4001126,-Item[lol]);
	cm.gainItem(1092047,-1);
	cm.gainItem(1092059,1);
	cm.sendOk("#e打開【 I 】道具欄內有沒有拿到#i1092059##k#b【#t1092059#】\r\n\                                                          #rL v 120");
	cm.dispose();
    } else if (lol == 20) {
	cm.gainItem(1302064,-1);
	cm.gainItem(1302142,1);
	cm.sendOk("#e打開【 I 】道具欄內有沒有拿到#i1302142##k#b【#t1302142#】\r\n\                                                          #rL v 77");
	cm.dispose();
    } else if (lol == 21) {
	cm.gainItem(1312032,-1);
	cm.gainItem(1312056,1);
	cm.sendOk("#e打開【 I 】道具欄內有沒有拿到#i1312056##k#b【#t1312056#】\r\n\                                                          #rL v 77");
	cm.dispose();
    } else if (lol == 22) {
	cm.gainItem(1322054,-1);
	cm.gainItem(1322084,1);
	cm.sendOk("#e打開【 I 】道具欄內有沒有拿到#i1322084##k#b【#t1322084#】\r\n\                                                          #rL v 77");
	cm.dispose();
    } else if (lol == 23) {
	cm.gainItem(1402039,-1);
	cm.gainItem(1402085,1);
	cm.sendOk("#e打開【 I 】道具欄內有沒有拿到#i1402085##k#b [ #t1402085# ]\r\n\                                                          #rL v 77");
	cm.dispose();
    } else if (lol == 24) {
	cm.gainItem(1412027,-1);
	cm.gainItem(1412055,1);
	cm.sendOk("#e打開【 I 】道具欄內有沒有拿到#i1412055##k#b【#t1412055#】\r\n\                                                          #rL v 77");
	cm.dispose();
    } else if (lol == 25) {
	cm.gainItem(1422029,-1);
	cm.gainItem(1422057,1);
	cm.sendOk("#e打開【 I 】道具欄內有沒有拿到#i1422057##k#b [ #t1422057# ]\r\n\                                                          #rL v 77");
   	cm.dispose();
    } else if (lol == 26) {
	cm.gainItem(1432040,-1);
	cm.gainItem(1432075,1);
	cm.sendOk("#e打開【 I 】道具欄內有沒有拿到#i1432075##k#b【#t1432075#】\r\n\                                                          #rL v 77");
	cm.dispose();
    } else if (lol == 27) {
	cm.gainItem(1442051,-1);
	cm.gainItem(1442104,1);
	cm.sendOk("#e打開【 I 】道具欄內有沒有拿到#i1442104##k#b【#t1442104#】\r\n\                                                          #rL v 77");
	cm.dispose();
    } else if (lol == 28) {
	cm.gainItem(1372034,-1);
	cm.gainItem(1372071,1);
	cm.sendOk("#e打開【 I 】道具欄內有沒有拿到#i1372071##k#b【#t1372071#】\r\n\                                                          #rL v 77");
	cm.dispose();
    } else if (lol == 29) {
	cm.gainItem(1382039,-1);
	cm.gainItem(1382093,1);
	cm.sendOk("#e打開【 I 】道具欄內有沒有拿到#i1382093##k#b【#t1382093#】\r\n\                                                          #rL v 77");
	cm.dispose();
    } else if (lol == 30) {
	cm.gainItem(1452045,-1);
	cm.gainItem(1452100,1);
	cm.sendOk("#e打開【 I 】道具欄內有沒有拿到#i1452100##k#b【#t1452100#】\r\n\                                                          #rL v 77");
	cm.dispose();
    } else if (lol == 31) {
	cm.gainItem(1462040,-1);
	cm.gainItem(1462085,1);
	cm.sendOk("#e打開【 I 】道具欄內有沒有拿到#i1462085##k#b【#t1462085#】\r\n\                                                          #rL v 77");
	cm.dispose();
    } else if (lol == 32) {
	cm.gainItem(1332056,-1);
	cm.gainItem(1332114,1);
	cm.sendOk("#e打開【 I 】道具欄內有沒有拿到#i1332114##k#b【#t1332114#】\r\n\                                                          #rL v 77");
	cm.dispose();
    } else if (lol == 33) {
	cm.gainItem(1472055,-1);
	cm.gainItem(1472111,1);
	cm.sendOk("#e打開【 I 】道具欄內有沒有拿到#i1472111##k#b【#t1472111#】\r\n\                                                          #rL v 77");
	cm.dispose();
    } else if (lol == 34) {
	cm.gainItem(1482022,-1);
	cm.gainItem(1482073,1);
	cm.sendOk("#e打開【 I 】道具欄內有沒有拿到#i1482073##k#b【#t1482073#】\r\n\                                                          #rL v 77");
	cm.dispose();
    } else if (lol == 35) {
	cm.gainItem(1492022,-1);
	cm.gainItem(1492073,1);
	cm.sendOk("#e打開【 I 】道具欄內有沒有拿到#i1492073##k#b【#t1492073#】\r\n\                                                          #rL v 77");
	cm.dispose();
    } else if (lol == 36) {
	cm.gainItem(1302142,-1);
	cm.gainItem(1082252,-1);
	cm.gainItem(1302081,1);
	cm.sendOk("#e打開【 I 】道具欄內有沒有拿到#i1302081##k#b【#t1302081#】\r\n\                                                          #rL v 120");
	cm.dispose();
    } else if (lol == 37) {
	cm.gainItem(1312056,-1);
	cm.gainItem(1082252,-1);
	cm.gainItem(1312037,1);
	cm.sendOk("#e打開【 I 】道具欄內有沒有拿到#i1312037##k#b【#t1312037#】\r\n\                                                          #rL v 120");
	cm.dispose();
    } else if (lol == 38) {
	cm.gainItem(1322084,-1);
	cm.gainItem(1082252,-1);
	cm.gainItem(1322060,1);
	cm.sendOk("#e打開【 I 】道具欄內有沒有拿到#i1322060##k#b【#t1322060#】\r\n\                                                          #rL v 120");
	cm.dispose();
    } else if (lol == 39) {
	cm.gainItem(1402085,-1);
	cm.gainItem(1082252,-1);
	cm.gainItem(1402046,1);
	cm.sendOk("#e打開【 I 】道具欄內有沒有拿到#i1402046##k#b【#t1402046#】\r\n\                                                          #rL v 120");
	cm.dispose();
    } else if (lol == 40) {
	cm.gainItem(1412055,-1);
	cm.gainItem(1082252,-1);
	cm.gainItem(1412033,1);
	cm.sendOk("#e打開【 I 】道具欄內有沒有拿到#i1412033##k#b【#t1412033#】\r\n\                                                          #rL v 120");
	cm.dispose();
    } else if (lol == 41) {
	cm.gainItem(1422057,-1);
	cm.gainItem(1082252,-1);
	cm.gainItem(1422037,1);
	cm.sendOk("#e打開【 I 】道具欄內有沒有拿到#i1422037##k#b【#t1422037#】\r\n\                                                          #rL v 120");
	cm.dispose();
    } else if (lol == 42) {
	cm.gainItem(1432075,-1);
	cm.gainItem(1082252,-1);
	cm.gainItem(1432047,1);
	cm.sendOk("#e打開【 I 】道具欄內有沒有拿到#i1432047##k#b【#t1432047#】\r\n\                                                          #rL v 120");
	cm.dispose();
    } else if (lol == 43) {
	cm.gainItem(1442104,-1);
	cm.gainItem(1082252,-1);
	cm.gainItem(1442063,1);
	cm.sendOk("#e打開【 I 】道具欄內有沒有拿到#i1442063##k#b【#t1442063#】\r\n\                                                          #rL v 120");
	cm.dispose();
    } else if (lol == 44) {
	cm.gainItem(1372071,-1);
	cm.gainItem(1082252,-1);
	cm.gainItem(1372044,1);
	cm.sendOk("#e打開【 I 】道具欄內有沒有拿到#i1372044##k#b【#t1372044#】\r\n\                                                          #rL v 120");
 	cm.dispose();
    } else if (lol == 45) {
	cm.gainItem(1382093,-1);
	cm.gainItem(1082252,-1);
	cm.gainItem(1382057,1);
	cm.sendOk("#e打開【 I 】道具欄內有沒有拿到#i1382057##k#b【#t1382057#】\r\n\                                                          #rL v 120");
	cm.dispose();
    } else if (lol == 46) {
	cm.gainItem(1452100,-1);
	cm.gainItem(1082252,-1);
	cm.gainItem(1452057,1);
	cm.sendOk("#e打開【 I 】道具欄內有沒有拿到#i1452057##k#b【#t1452057#】\r\n\                                                          #rL v 120");
  	cm.dispose();
    } else if (lol == 47) {
	cm.gainItem(1462085,-1);
	cm.gainItem(1082252,-1);
	cm.gainItem(1462050,1);
	cm.sendOk("#e打開【 I 】道具欄內有沒有拿到#i1462050##k#b【#t1462050#】\r\n\                                                          #rL v 120");
	cm.dispose();
    } else if (lol == 48) {
	cm.gainItem(1332114,-1);
	cm.gainItem(1082252,-1);
	cm.gainItem(1332074,1);
	cm.sendOk("#e打開【 I 】道具欄內有沒有拿到#i1332074##k#b【#t1332074#】\r\n\                                                          #rL v 120");
	cm.dispose();
    } else if (lol == 49) {
	cm.gainItem(1472111,-1);
	cm.gainItem(1082252,-1);
	cm.gainItem(1472068,1);
	cm.sendOk("#e打開【 I 】道具欄內有沒有拿到#i1472068##k#b【#t1472068#】\r\n\                                                          #rL v 120");
	cm.dispose();
    } else if (lol == 50) {
	cm.gainItem(1482073,-1);
	cm.gainItem(1082252,-1);
	cm.gainItem(1482023,1);
	cm.sendOk("#e打開【 I 】道具欄內有沒有拿到#i1482023##k#b【#t1482023#】\r\n\                                                          #rL v 120");
	cm.dispose();
    } else if (lol == 51) {
	cm.gainItem(1492073,-1);
	cm.gainItem(1082252,-1);
	cm.gainItem(1492023,1);
	cm.sendOk("#e打開【 I 】道具欄內有沒有拿到#i1492023##k#b【#t1492023#】\r\n\                                                          #rL v 120");
	cm.dispose();
    } else if (lol  == 52) {
	var chance = Math.floor(Math.random()*30)+1;
	var chance1 = Math.floor(Math.random()*3)+1;
	cm.gainItem(4280000, -Item[lol]);
	if(chance>=29 && chance<=30){
	    cm.giveRandItem(ids0_2, 10, 10);
	    cm.dispose();
	} else if(chance>=28 && chance<=29){
	    cm.giveRandItem(ids0_1, 8, 7);
	    cm.dispose();
	} else if(chance>=3 && chance<=6 || chance>=17 && chance<=20){
	    cm.giveRandItem(ids0_0, 5, 5);
	    cm.dispose();
	} else if(chance>=10 && chance<=11){
	    cm.gainItem(4031456,chance1);
	    cm.sendOk("#r真可惜, 沒有抽到物品, #b獎勵你#i4031456#楓葉珠子 #r"+chance1+"#b 個.");
	    cm.dispose();
	} else {
	    cm.sendOk("#r真可惜, 沒有抽到物品, 在接在歷, 相信下次就可以抽到的.");
	    cm.dispose();
	}
    } else if (lol  == 53) {
	var chance = Math.floor(Math.random()*40)+1;
	var chance1 = Math.floor(Math.random()*3)+1;
	cm.gainItem(4280000, -Item[lol]);
	if(chance==35){
	    cm.gainItem(4031456,chance1);
	    cm.sendOk("#r真可惜, 沒有抽到物品, #b獎勵你#i4031456#楓葉珠子 #r"+chance1+"#b 個.");
	    cm.dispose();
	} else if(chance>=4 && chance<=20){
	    cm.Item(ids1, false);
	    cm.dispose();
	} else {
	    cm.sendOk("#r真可惜, 沒有抽到物品, 在接在歷, 相信下次就可以抽到的.");
	    cm.dispose();
	}
    } else if (lol  == 54) {
	var chance = Math.floor(Math.random()*35)+1;
	var chance1 = Math.floor(Math.random()*3)+1;
	cm.gainItem(4280000, -Item[lol]);
	if(chance==30){
	    cm.gainItem(4031456,chance1);
	    cm.sendOk("#r真可惜, 沒有抽到物品, #b獎勵你#i4031456#楓葉珠子 #r"+chance1+"#b 個.");
	    cm.dispose();
	} else if(chance>=4 && chance<=17){
	    cm.Item(ids2, false);
	    cm.dispose();
	} else {
	    cm.sendOk("#r真可惜, 沒有抽到物品, 在接在歷, 相信下次就可以抽到的.");
	    cm.dispose();
	}
    } else if (lol  == 55) {
	var chance = Math.floor(Math.random()*9)+1;
	cm.gainItem(1002776, -Item[lol]);
	cm.gainItem(1052155, -Item[lol]);
	cm.gainItem(1082234, -Item[lol]);
	cm.gainItem(1072355, -Item[lol]);
	cm.gainItem(1003177, -Item[lol]);
	cm.gainItem(1052319, -Item[lol]);
	cm.gainItem(1082300, -Item[lol]);
	cm.gainItem(1072490, -Item[lol]);
	if(chance>=2 && chance<=3){
	    cm.giveRandItem(1003172, 30, 30, true, true, false, false);
	    cm.giveRandItem(1052314, 30, 30, true, true, false, false);
	    cm.giveRandItem(1082295, 30, 30, true, true, false, false);
	    cm.giveRandItem(1072485, 30, 30, true, true, false, false);
	    cm.sendOk("#b恭喜您，榮獲#r進階版女皇套裝#b，快去看看包包吧！");
	    cm.serverNotice(6, "[大獎恭喜]【" + cm.getName() + "】獲得『劍士 進階版女皇套裝』!~");
	    cm.dispose();
	} else if(chance==1 || chance>=5 && chance<=8){
	    cm.gainItem(1003172);
	    cm.gainItem(1052314);
	    cm.gainItem(1082295);
	    cm.gainItem(1072485);
	    cm.sendOk("#b恭喜您，成功啦，快去看看包包吧，相信你一定會喜歡的！");
	    cm.serverNotice(6, "[恭喜]【" + cm.getName() + "】獲得『劍士 女皇套裝』!~");
	    cm.dispose();
	} else {
	    cm.sendOk("#r真失敗，合成沒有成功，在接在歷，相信你一定能成功的！");
	    cm.dispose();
	}
    } else if (lol  == 56) {
	var chance = Math.floor(Math.random()*9)+1;
	cm.gainItem(1002777, -Item[lol]);
	cm.gainItem(1052156, -Item[lol]);
	cm.gainItem(1082235, -Item[lol]);
	cm.gainItem(1072356, -Item[lol]);
	cm.gainItem(1003178, -Item[lol]);
	cm.gainItem(1052320, -Item[lol]);
	cm.gainItem(1082301, -Item[lol]);
	cm.gainItem(1072491, -Item[lol]);
	if(chance>=2 && chance<=3){
	    cm.giveRandItem(1003173, 30, 30, false, false, true, true);
	    cm.giveRandItem(1052315, 30, 30, false, false, true, true);
	    cm.giveRandItem(1082296, 30, 30, false, false, true, true);
	    cm.giveRandItem(1072486, 30, 30, false, false, true, true);
	    cm.sendOk("#b恭喜您，榮獲#r進階版女皇套裝#b，快去看看包包吧！");
	    cm.serverNotice(6, "[大獎恭喜]【" + cm.getName() + "】獲得『法師 進階版女皇套裝』!~");
	    cm.dispose();
	} else if(chance==1 || chance>=5 && chance<=8){
	    cm.gainItem(1003173);
	    cm.gainItem(1052315);
	    cm.gainItem(1082296);
	    cm.gainItem(1072486);
	    cm.sendOk("#b恭喜您，成功啦，快去看看包包吧，相信你一定會喜歡的！");
	    cm.serverNotice(6, "[恭喜]【" + cm.getName() + "】獲得『法師 女皇套裝』!~");
	    cm.dispose();
	} else {
	    cm.sendOk("#r真失敗，合成沒有成功，在接在歷，相信你一定能成功的！");
	    cm.dispose();
	}
    } else if (lol  == 57) {
	var chance = Math.floor(Math.random()*9)+1;
	cm.gainItem(1002778, -Item[lol]);
	cm.gainItem(1052157, -Item[lol]);
	cm.gainItem(1082236, -Item[lol]);
	cm.gainItem(1072357, -Item[lol]);
	cm.gainItem(1003179, -Item[lol]);
	cm.gainItem(1052321, -Item[lol]);
	cm.gainItem(1082302, -Item[lol]);
	cm.gainItem(1072492, -Item[lol]);
	if(chance>=2 && chance<=3){
	    cm.giveRandItem(1003174, 30, 30, true, true, false, false);
	    cm.giveRandItem(1052316, 30, 30, true, true, false, false);
	    cm.giveRandItem(1082297, 30, 30, true, true, false, false);
	    cm.giveRandItem(1072487, 30, 30, true, true, false, false);
	    cm.sendOk("#b恭喜您，榮獲#r進階版女皇套裝#b，快去看看包包吧！");
	    cm.serverNotice(6, "[大獎恭喜]【" + cm.getName() + "】獲得『弓箭手 進階版女皇套裝』!~");
	    cm.dispose();
	} else if(chance==1 || chance>=5 && chance<=8){
	    cm.gainItem(1003174);
	    cm.gainItem(1052316);
	    cm.gainItem(1082297);
	    cm.gainItem(1072487);
	    cm.sendOk("#b恭喜您，成功啦，快去看看包包吧，相信你一定會喜歡的！");
	    cm.serverNotice(6, "[恭喜]【" + cm.getName() + "】獲得『弓箭手 女皇套裝』!~");
	    cm.dispose();
	} else {
	    cm.sendOk("#r真失敗，合成沒有成功，在接在歷，相信你一定能成功的！");
	    cm.dispose();
	}
    } else if (lol  == 58) {
	var chance = Math.floor(Math.random()*9)+1;
	cm.gainItem(1002779, -Item[lol]);
	cm.gainItem(1052158, -Item[lol]);
	cm.gainItem(1082237, -Item[lol]);
	cm.gainItem(1072358, -Item[lol]);
	cm.gainItem(1003180, -Item[lol]);
	cm.gainItem(1052322, -Item[lol]);
	cm.gainItem(1082303, -Item[lol]);
	cm.gainItem(1072493, -Item[lol]);
	if(chance>=2 && chance<=3){
	    cm.giveRandItem(1003175, 30, 30, true, false, false, true);
	    cm.giveRandItem(1052317, 30, 30, true, false, false, true);
	    cm.giveRandItem(1082298, 30, 30, true, false, false, true);
	    cm.giveRandItem(1072488, 30, 30, true, false, false, true);
	    cm.sendOk("#b恭喜您，榮獲#r進階版女皇套裝#b，快去看看包包吧！");
	    cm.serverNotice(6, "[大獎恭喜]【" + cm.getName() + "】獲得『盜賊 進階版女皇套裝』!~");
	    cm.dispose();
	} else if(chance==1 || chance>=5 && chance<=8){
	    cm.gainItem(1003175);
	    cm.gainItem(1052317);
	    cm.gainItem(1082298);
	    cm.gainItem(1072488);
	    cm.sendOk("#b恭喜您，成功啦，快去看看包包吧，相信你一定會喜歡的！");
	    cm.serverNotice(6, "[恭喜]【" + cm.getName() + "】獲得『盜賊 女皇套裝』!~");
	    cm.dispose();
	} else {
	    cm.sendOk("#r真失敗，合成沒有成功，在接在歷，相信你一定能成功的！");
	    cm.dispose();
	}
    } else if (lol  == 59) {
	var chance = Math.floor(Math.random()*9)+1;
	cm.gainItem(1002780, -Item[lol]);
	cm.gainItem(1052159, -Item[lol]);
	cm.gainItem(1082238, -Item[lol]);
	cm.gainItem(1072359, -Item[lol]);
	cm.gainItem(1003181, -Item[lol]);
	cm.gainItem(1052323, -Item[lol]);
	cm.gainItem(1082304, -Item[lol]);
	cm.gainItem(1072494, -Item[lol]);
	if(chance>=2 && chance<=3){
	    cm.giveRandItem(1003176, 30, 30, true, true, false, false);
	    cm.giveRandItem(1052318, 30, 30, true, true, false, false);
	    cm.giveRandItem(1082299, 30, 30, true, true, false, false);
	    cm.giveRandItem(1072489, 30, 30, true, true, false, false);
	    cm.sendOk("#b恭喜您，榮獲#r進階版女皇套裝#b，快去看看包包吧！");
	    cm.serverNotice(6, "[大獎恭喜]【" + cm.getName() + "】獲得『海盜 進階版女皇套裝』!~");
	    cm.dispose();
	} else if(chance==1 || chance>=5 && chance<=8){
	    cm.gainItem(1003176);
	    cm.gainItem(1052318);
	    cm.gainItem(1082299);
	    cm.gainItem(1072489);
	    cm.sendOk("#b恭喜您，成功啦，快去看看包包吧，相信你一定會喜歡的！");
	    cm.serverNotice(6, "[恭喜]【" + cm.getName() + "】獲得『海盜 女皇套裝』!~");
	    cm.dispose();
	} else {
	    cm.sendOk("#r真失敗，合成沒有成功，在接在歷，相信你一定能成功的！");
	    cm.dispose();
	}
    } else if (lol == 60) {
	cm.gainItem(4001126,-Item[lol]);
	cm.gainItem(1102198,-1);
	cm.gainItem(1102280,1);
	cm.sendOk("#e打開【 I 】道具欄內有沒有拿到#rL v 130#i1102280##k【#t1102280#】");
	cm.dispose();
    } else if (lol == 61) {
	cm.gainItem(4001126,-Item[lol]);
	cm.gainItem(1102198,-1);
	cm.gainItem(1102281,1);
	cm.sendOk("#e打開【 I 】道具欄內有沒有拿到#rL v 130#i1102281##k【#t1102281#】");
	cm.dispose();
    } else if (lol == 62) {
	cm.gainItem(4001126,-Item[lol]);
	cm.gainItem(1102198,-1);
	cm.gainItem(1102282,1);
	cm.sendOk("#e打開【 I 】道具欄內有沒有拿到#rL v 130#i1102282##k【#t1102282#】");
	cm.dispose();
    } else if (lol == 63) {
	cm.gainItem(4001126,-Item[lol]);
	cm.gainItem(1102198,-1);
	cm.gainItem(1102283,1);
	cm.sendOk("#e打開【 I 】道具欄內有沒有拿到#rL v 130#i1102283##k【#t1102283#】");
	cm.dispose();
    } else if (lol == 64) {
	cm.gainItem(4001126,-Item[lol]);
	cm.gainItem(1102198,-1);
	cm.gainItem(1102284,1);
	cm.sendOk("#e打開【 I 】道具欄內有沒有拿到#rL v 130#i1102284##k【#t1102284#】");
	cm.dispose();
    } else if (lol == 65) {
	cm.gainItem(4001126,-Item[lol]);
	cm.gainItem(1102198,-1);
	cm.gainItem(1102275,1);
	cm.sendOk("#e打開【 I 】道具欄內有沒有拿到#rL v 140#i1102275##k【#t1102275#】");
	cm.dispose();
    } else if (lol == 66) {
	cm.gainItem(4001126,-Item[lol]);
	cm.gainItem(1102198,-1);
	cm.gainItem(1102276,1);
	cm.sendOk("#e打開【 I 】道具欄內有沒有拿到#rL v 140#i1102276##k【#t1102276#】");
	cm.dispose();
    } else if (lol == 67) {
	cm.gainItem(4001126,-Item[lol]);
	cm.gainItem(1102198,-1);
	cm.gainItem(1102277,1);
	cm.sendOk("#e打開【 I 】道具欄內有沒有拿到#rL v 140#i1102277##k【#t1102277#】");
	cm.dispose();
    } else if (lol == 68) {
	cm.gainItem(4001126,-Item[lol]);
	cm.gainItem(1102198,-1);
	cm.gainItem(1102278,1);
	cm.sendOk("#e打開【 I 】道具欄內有沒有拿到#rL v 140#i1102278##k【#t1102278#】");
	cm.dispose();
    } else if (lol == 69) {
	cm.gainItem(4001126,-Item[lol]);
	cm.gainItem(1102198,-1);
	cm.gainItem(1102279,1);
	cm.sendOk("#e打開【 I 】道具欄內有沒有拿到#rL v 140#i1102279##k【#t1102279#】");
	cm.dispose();


    }
}
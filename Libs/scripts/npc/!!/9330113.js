var status = -1;
var GachItem = new Array(
				[4030000,10000,true],
				[4030001,100,true]
				);
				//物品ID,機率,是否上廣
var itemList = new Array(
	1372035,
	1372036,
	1372037,
	1372038,
	1382016,
	1382045,
	1382046,
	1382047,
	1382048,
	1372040,
	1372041,
	1372042
);


var jobName = new Array(
"#b稀有物品", "#b高階物品", "#b所有物品","楓葉",
"展示裝", "展示武","展示套"
);

var NpcName = "法師轉蛋機";
var NpcItems = 5220040;
var All_eq = 850; //所有裝備機率
var Vip_eq = 20; //特殊裝備機率
var Rare_eq = 1; //稀有裝備機率
var Scrolls = 3; //卷軸數量

var GachItem = new Array(
//法師
[//稀有
1382127,
//革命
1372188,1382222,
//衣服
1003155,1003173,1003178,1003444,1003590,1003603,1003798,1004007,1004215,1004235,1004423,1042255,1050263,1050264,1050265,1050275,1050280,1051320,1051321,1051322,1051337,1051342,1052300,1052315,1052320,1052430,1052499,1052511,1052785,1052805,1052887,1062166,1070034,1070039,1070044,1070049,1070054,1071051,1071056,1071061,1071066,1071071,1072472,1072486,1072491,1072642,1072704,1072713,1072953,1072973,1073032,1082286,1082296,1082301,1082417,1082467,1082474,1082594,1082614,1082637,1092089,1102263,1102276,1102281,1102363,1102446,1102458,1102714,1102794,
//武器
1382234,1382259,1372222,1382231,1372195,1382208,1372177,1382220,1382170,1372186,1372141,1382209,1372178,1382192,1372161,1382232,1382104,1382210,1372196,1372084,1372175,1382263,1382225,1372225,1372227,1382204,1382205,1372191,1372172,1372173,1382211,1382169,1382190,1382246,1372179,1372140,1372160,1382146,1372208,1382145,1382105,1382158,1372120,1382152,1372119,1372085,1372130,1382102,1372126,1372080,1382159,1372132,1382214,1382131,1382151,1372182,1382242,1372106,1372125,1372204,1382193,1382202,1372162,1372170,1372105,1372124,1382130,1382150,1382207,1382213,1372176,1372181,1372104,1372123,1372164,1382129,1382149,1382195,1372103,1372122,1372163,1382128,1382148,1382194,1382226,1382068,1382239,1372201,
Rare_eq
],
[//The Vip
1382099,1372078,
//不速
1382098,1372077,1372076,1382096,1372075,1382095,1372074,
//9周年
1382212,1372180,1382097,
//混沌
1382114,1372090,1372089,1382113,1372088,1382112,
//游泳圈
1382227,
//130 140裝
1382049,1382050,1382051,1382052,1382245,1372207,1372039,1372040,1372041,1372042,1382244,1382069,1372206,
//正義
1372121,1382147,
//高階裝備
1004220,1052790,1072958,1082599,
1004225,1052795,1072963,1082604,
1004230,1052800,1072968,1082609,1102719,
Vip_eq
],
[//正常裝備
1002013,1002016,1002017,1002034,1002035,1002036,1002037,1002038,1002064,1002065,1002072,1002073,1002074,1002075,1002102,1002103,1002104,1002105,1002106,1002141,1002142,1002143,1002144,1002145,1002151,1002152,1002153,1002154,1002155,1002215,1002216,1002217,1002218,1002242,1002243,1002244,1002245,1002246,1002252,1002253,1002254,1002271,1002272,1002273,1002274,1002363,1002364,1002365,1002366,1002398,1002399,1002400,1002401,1002773,1003612,1003617,1040004,1040017,1040018,1040019,1040020,1041015,1041016,1041017,1041018,1041025,1041026,1041029,1041030,1041031,1041041,1041042,1041043,1041051,1041052,1041053,1050001,1050002,1050003,1050008,1050009,1050010,1050023,1050024,1050025,1050026,1050027,1050028,1050029,1050030,1050031,1050035,1050036,1050037,1050038,1050039,1050045,1050046,1050047,1050048,1050049,1050053,1050054,1050055,1050056,1050067,1050068,1050069,1050070,1050072,1050073,1050074,1050092,1050093,1050094,1050095,1050102,1050103,1050104,1050105,1051003,1051004,1051005,1051023,1051024,1051025,1051026,1051027,1051030,1051031,1051032,1051033,1051034,1051044,1051045,1051046,1051047,1051052,1051053,1051054,1051055,1051056,1051057,1051058,1051094,1051095,1051096,1051097,1051101,1051102,1051103,1051104,1052076,1052517,1052522,1052825,1060012,1060013,1060014,1060015,1061010,1061011,1061012,1061013,1061021,1061022,1061027,1061028,1061034,1061035,1061036,1061047,1061048,1061049,1072006,1072019,1072020,1072021,1072023,1072024,1072044,1072045,1072072,1072073,1072074,1072075,1072076,1072077,1072078,1072089,1072090,1072091,1072114,1072115,1072116,1072117,1072136,1072137,1072138,1072139,1072140,1072141,1072142,1072143,1072157,1072158,1072159,1072160,1072169,1072177,1072178,1072179,1072206,1072207,1072208,1072209,1072223,1072224,1072225,1072226,1072268,1072720,1072725,1082019,1082020,1082021,1082022,1082026,1082027,1082028,1082051,1082052,1082053,1082054,1082055,1082056,1082062,1082063,1082064,1082080,1082081,1082082,1082086,1082087,1082088,1082098,1082099,1082100,1082121,1082122,1082123,1082131,1082132,1082133,1082134,1082151,1082152,1082153,1082154,1082164,1082479,1082484,1092021,1092029,1942000,1942001,1942003,1952000,1952001,1952003,1962000,1962001,1962003,1972000,1972001,1972003,
//武器
1382243,1372205,1382036,1372032,1382037,1382045,1382046,1382047,1382048,1382111,1372010,1382035,1382215,1382216,1382016,1372009,1382008,1382013,1372117,1382142,1372016,1382010,1372035,1372036,1372037,1372038,1382110,1372015,1382007,1382079,1372008,1372057,1372014,1382006,1382014,1382011,1372011,1372007,1382001,1382041,1382019,1382109,1382166,1372000,1372012,1372062,1372135,1372136,1382018,1382165,1372001,1372134,1382015,1382017,1382040,1382164,1372003,1382002,1372004,1382004,1372002,1382003,1382005,1372013,1382000,
All_eq
],
[//楓葉展示
1092045,
//綠寶石
1372096,1382120,
//白金
1372097,1382121,
1372034,1372071,1372094,1372095,1372099,1372131,1372139,1382009,1382012,1382039,1382093,1382118,1382119,1382123,1382160,1382168,
0
],
[//無法交易
1000065,1000066,1000067,1000068,1002991,1003032,1003198,1003298,1003303,1003308,1003317,1003322,1003327,1003332,1003337,1003342,1003570,1003571,1003572,1003573,1004520,1004521,1004522,1004523,1004550,1004551,1004552,1004553,1004554,1004555,1004582,1004674,1004675,1040160,1040167,1041162,1041165,1041169,1041175,1041178,1050155,1050164,1050194,1050195,1050197,1050199,1050200,1050201,1050203,1050205,1050379,1051191,1051203,1051240,1051244,1051245,1051247,1051249,1051449,1052271,1052334,1052479,1052480,1052481,1052482,1052935,1052936,1052937,1052938,1053011,1053012,1060150,1060156,1061172,1061175,1061179,1061184,1061187,1072400,1072420,1072503,1072560,1072565,1072570,1072575,1072580,1072585,1072590,1072595,1072600,1072605,1072684,1072685,1072686,1072687,1072733,1072738,1072744,1073068,1073069,1073070,1073071,1073083,1073112,1073113,1082257,1082306,1082347,1082352,1082357,1082362,1082367,1082372,1082377,1082382,1082387,1082450,1082451,1082452,1082453,1082544,1082653,1082654,1082655,1082656,1082663,1082681,1082682,1092105,1092106,1102472,1102477,1102482,1112783,1113238,
0
],
[//無法交易
1382235,1382257,1372219,1382233,1382224,1382260,1382126,1372197,1372190,1372223,1372102,1382206,1382221,1372174,1372187,1382198,1372167,1382256,1372218,1382200,1372169,1382058,1382203,1372171,1382024,1382029,1382804,1382904,1382067,1372050,1382143,1372118,1372166,1382197,1382066,1382023,1382028,1382803,1382903,1372082,1382103,1382171,1382241,1372059,1372021,1372025,1372803,1372903,1372203,1372072,1382141,1372051,1382022,1382027,1382802,1382902,1382094,1372083,1372159,1372116,1372020,1372024,1372902,1382189,1382140,1372046,1382021,1382026,1382901,1372081,1372069,1382091,1372115,1372165,1372019,1372023,1372901,1382139,1382196,1382020,1382025,1382900,1382801,1372802,1372114,1382042,1372018,1372022,1372900,1382070,1382138,1372053,1372061,1372113,1372801,1382137,1382800,1382062,1372112,1382043,1382044,1382136,1372111,1382080,1372066,1382135,1382088,1372058,1372026,1372110,1372800,1382134,1372060,1372063,1372109,1382085,1382133,1382100,1382132,1372043,1372107,
0
],
[//衣服稀有
1112656,
1052649,
1003291,1052385,1072555,1082339,1102313,1003771,1052581,1072787,1082507,1102515,1004083,1052703,1072904,1082576,1003690,1052546,1072696,1082491,1102499,
0
]
);








var s = 0;

function action(mode, type, selection) {
	if (mode == -1) {
		cm.dispose();
	} else {
		if (mode == 0 && status == 0) {
			cm.dispose();
			return;
		}
		if (mode == 1)
			status++;
		else
			status--;
	}
    if (status == 0) {
		if (cm.haveItem(NpcItems, 1)) {
			var NoText = "";
			for (var q = 0; q < GachItem.length; q++) {
				if (q % 4 == 0) {
					NoText += "\r\n";
				}
				NoText += "#r#L" + q +"##e["+jobName[q]+"#r]#n#l";
			}
		} else {
			cm.sendOk("不好意思!您沒有#b#t"+NpcItems+"##i"+NpcItems+"##k。");
			cm.safeDispose();
		}
		cm.sendYesNo("[#b#p" +cm.getNpc()+"##k]："+GrandTotal()+"\r\n您身上有 #b#t"+NpcItems+"##i"+NpcItems+"##k 可以進行轉蛋。\r\n\r\n" +
			"#L"+(GachItem.length+1)+"##e#d你確定要使用嗎？#n#k#l\r\n" +
			"\r\n#r－－－－－－－－－－－－－－－－－－－－－－－－#k\r\n\r\n" +
			"#e查看本機台擁有..#n" + NoText + "#l　");
		s = selection;
	} else if (status == 1) {
		if (selection == (GachItem.length+1)) {
			var 機率 = 0;
			var tessa = "";
			var 不重複顯示共辜 =true;
			for (var o = 0; o < GachItem.length; o++) {
				if (不重複顯示共辜) {
					機率 = GachItem[o][GachItem[o].length-1]
					tessa = cm.gainRewardItemqss(機率);
					if (tessa == "1") { //0 = 沒中, 1 = 中獎
						//中獎
						var RandomI = Math.floor(Math.random() * (GachItem[o].length-1));
						if (機率 == Rare_eq)
							item = cm.gainGachaponItem(GachItem[o][RandomI], 1, true, 2, "稀有");
						else if (機率 == Vip_eq)
							item = cm.gainGachaponItem(GachItem[o][RandomI], 1, false, 2, NpcName);
						else
							item = cm.gainGachaponItem(GachItem[o][RandomI], 1, false, 2, "");
						//檢查背包是否滿
						if (item != -1) {
							cm.gainItem(NpcItems, -1);
							if (不重複顯示共辜)
							cm.sendOk("#e您已獲得 #b#i" + item + ":#" + "  此裝備機率: " + 機率/10 + "%");
							cm.dispose();
						} else {
							cm.sendOk("#e檢查一下#b背包#r是否已滿#k");
							cm.dispose();
						}
						cm.safeDispose();
						//檢查背包是否滿
						break;//避免重複抽獎
						//中獎
					} else {
						if (o == (GachItem.length-1)){
							不重複顯示共辜 = false;
							var Scroll = cm.Scroll();
								if (Scroll != -2) {
									cm.sendOk("#e很可惜你並沒有中獎。\r\n將贈送安慰獎 #b#i" + Scroll + ":##t" + Scroll + "# "+Scrolls+" 張");
									cm.gainItem(NpcItems, -1);
									cm.gainItem(Scroll,Scrolls);
									cm.dispose();
								} else {
									cm.sendOk("#e請回報管理員物品代碼");
									cm.dispose();
								}
							cm.safeDispose();
						}
					}
				}
			}
		} else {
			var msg ="";
			for (var v = 0; v < GachItem[selection].length-1; v++) {
				if (v % 1 == 0) {
					msg += "\r\n";
				}
				if (cm.gainIItem(GachItem[selection][v]) == 0) {
					//msg += "#v" + GachItem[selection][v] + ":#" + GachItem[selection][v] + "	";
					msg += "#v" + GachItem[selection][v] + ":#" + GachItem[selection][v] +"	#t" + GachItem[selection][v] + ":#	";
				} else {
					msg += "#r#fUI/UIWindow.img/QuestIcon/5/0# (無名稱)#k	";
				}
			}
			cm.sendOk("#e本機台擁有 #r" + (GachItem[selection].length-1)+ "#k 個#n\r\n" + msg);
			cm.dispose();
		}
	

    }
}

function GrandTotal(){
	var Sum =0;
for (var v = 0; v < GachItem.length; v++) {
	for (var i = 0; i < GachItem[v].length-1; i++) {
		Sum += 1;
	}
}
    return Sum;
}
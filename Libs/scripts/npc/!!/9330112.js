﻿var status = -1;
var itemList = new Array(1302026, 1302032, 1302037, 1302081, 1302086, 1302173, 1302174, 1302146, 1302145, 1302144, 1302143, 1302217, 1302218, 1302219, 1302220, 1302221, 1302223, 1302248, 1322060, 1322061, 1322086, 1322087, 1322088, 1322107, 1322108, 1322181, 1312037, 1312038, 1312072, 1312073, 1312135, 1312058, 1312059, 1312060, 1402046, 1402047, 1402111, 1402112, 1402172, 1402086, 1402087, 1402088, 1412033, 1412034, 1412071, 1412072, 1412122, 1412058, 1412059, 1412060, 1422037, 1422038, 1422073, 1422074, 1422124, 1422059, 1422060, 1422061, 1432047, 1432049, 1432150, 1432077, 1432078, 1432079, 1442063, 1442067, 1442136, 1442137, 1442202, 1442107, 1442108, 1442109, 1542012, 1542013, 1542033, 1542034, 1542060);


var jobName = new Array(
"#b稀有物品", "#b高階物品", "#b所有物品","楓葉",
"展示裝", "展示武","展示套"
);

var NpcName = "戰士轉蛋機";
var NpcItems = 5220040;
var All_eq = 850; //所有裝備機率
var Vip_eq = 20; //特殊裝備機率
var Rare_eq = 1; //稀有裝備機率
var Scrolls = 3; //卷軸數量

var GachItem = new Array(
[//夢幻
1432102,1402114,1312097,1322137,1442139,
//革命
1402210,1412147,1422152,1432178,1442234,1302289,1312165,1322215,
//裝備
1000039,1001059,1003154,1003172,1003177,1003443,1003589,1003601,1003797,1003841,1004006,1004214,1004234,1004422,1042254,1050162,1050257,1050258,1050259,1050273,1050278,1051201,1051314,1051315,1051316,1051335,1051340,1052299,1052314,1052319,1052429,1052498,1052509,1052608,1052609,1052784,1052804,1052882,1062165,1070032,1070037,1070042,1070047,1070052,1071049,1071054,1071059,1071064,1071069,1072434,1072471,1072485,1072490,1072641,1072703,1072711,1072815,1072952,1072972,1073030,1082264,1082285,1082295,1082300,1082416,1082466,1082472,1082522,1082593,1082613,1082636,1092087,1098005,1098008,1099010,1099014,1099015,1102262,1102275,1102280,1102362,1102445,1102456,1102553,1102713,1102775,
//武器
1402179,1402193,1402251,1412177,1422184,1432214,1302333,1312199,1322250,1442268,1412152,1422158,1432187,1442242,1402220,1302297,1312173,1322223,1402196,1412135,1422140,1432167,1402224,1302275,1312153,1322203,1402204,1412144,1422149,1432176,1402153,1412106,1422109,1432140,1442223,1302285,1312162,1322213,1402197,1412136,1422141,1432168,1302229,1312118,1322164,1402172,1412122,1422124,1432150,1302276,1312154,1322204,1412153,1422159,1432189,1442243,1442232,1302248,1312135,1322181,1402222,1442184,1302299,1312174,1322224,1402095,1402192,1412065,1412133,1422066,1422137,1432086,1432165,1442224,1302152,1302267,1312065,1312151,1322096,1322200,1402213,1402253,1412150,1412180,1422155,1422187,1432181,1432216,1442202,1402189,1402190,1412130,1412131,1422134,1422135,1432162,1432163,1302336,1312201,1322253,1302292,1312168,1322218,1302271,1302272,1312148,1312149,1322197,1322198,1402152,1402171,1412105,1412121,1422108,1422123,1432139,1432149,1402199,1412088,1412137,1422091,1422142,1432120,1432169,1442158,1442116,1442220,1402132,1442156,1402131,1402237,1412179,1422186,1432201,1442255,1302194,1302228,1302247,1312100,1312117,1312134,1322140,1322163,1322180,1402096,1402142,1412066,1412099,1422067,1422102,1432087,1432131,1302277,1312155,1322205,1432119,1442270,1442272,1442113,1442237,1302316,1312186,1322237,1402091,1402138,1412094,1422097,1432126,1442164,1442217,1442218,1302153,1302207,1312066,1312110,1322097,1322150,1432084,1302193,1312099,1322139,1302200,1312106,1322146,1412076,1412093,1412100,1422078,1422096,1422103,1432106,1432125,1432133,1442143,1442163,1442171,1402203,1412140,1422145,1432172,1442183,1442201,1302149,1312095,1322135,1402118,1402137,1402143,1402205,1442225,1412123,1412128,1412161,1422125,1422131,1422168,1432151,1432160,1432197,1442203,1442211,1442251,1302179,1302199,1302213,1312077,1312105,1312112,1322112,1322145,1322151,1402174,1402187,1402233,1412075,1412092,1422077,1422095,1432105,1432124,1442142,1442162,1302280,1312158,1322208,1442117,1442118,1442170,1302249,1302258,1302312,1312136,1312145,1312182,1322182,1322191,1322233,1402117,1402136,1302178,1302198,1312076,1312104,1322111,1322144,1322160,1412074,1412091,1412125,1422076,1422094,1422127,1432104,1432123,1432153,1442141,1442161,1442205,1402116,1402135,1402176,1412134,1412139,1422138,1422144,1432166,1432171,1442222,1442227,1442228,1302177,1302197,1302251,1312075,1312103,1312138,1322110,1322143,1322184,1402195,1402202,1412073,1412090,1412124,1422075,1422093,1422126,1432103,1432122,1432152,1442140,1442160,1442204,1302273,1302279,1312152,1312157,1322201,1322207,1402115,1402134,1402175,1302176,1302196,1302250,1312074,1312102,1312137,1322109,1322142,1322183,1422156,1432182,1402214,1442078,1402062,1442095,1442248,1402229,1432194,1412158,1422165,1302304,1312179,1322230,
Rare_eq
],
[
//The Vip
1412062,1422063,1432081,1442111,1402090,1302147,1312062,1322090,
//不速
1442110,1412061,1422062,1432080,1402089,1302146,1312061,1322089,1442109,1412060,1422061,1432079,1402088,1302145,1312060,1322088,1412059,1422060,1432078,1442108,1402087,1322087,1302144,1312059,1442107,1412058,1422059,1432077,1402086,1322086,1302143,1312058,
//9周年
1412138,1422143,1432170,1442226,1402200,1302278,1312156,1322206,
//混沌
1432091,1402100,1302165,1312090,1322130,1432090,1442126,1402099,1302164,1312089,1322129,1442125,1432089,1402098,1322128,1302163,1312088,1442124,
//游泳圈
1402215,1412151,1422157,1432183,1442238,1312169,1322219,
//130 140武器
1412164,1422171,1432200,1402236,1442254,1302315,1312185,1322236,1412163,1422170,1432199,1442253,1402235,1302314,1312184,1322235,
//正義
1412089,1422092,1432121,1442159,1402133,1302195,1312101,1322141,
//高階裝備
1004219,1052789,1072957,1082598,
1004224,1052794,1072962,1082603,
1004229,1052799,1072967,1082608,1102718,
Vip_eq
],
[
//正常裝備
1002002,1002003,1002004,1002005,1002007,1002009,1002011,1002021,1002022,1002023,1002024,1002025,1002027,1002028,1002029,1002030,1002039,1002040,1002043,1002044,1002045,1002046,1002047,1002048,1002049,1002050,1002051,1002052,1002055,1002056,1002058,1002059,1002084,1002085,1002086,1002087,1002088,1002091,1002092,1002093,1002094,1002095,1002098,1002099,1002100,1002101,1002338,1002339,1002340,1002377,1002378,1002379,1002528,1002529,1002530,1002531,1002532,1002551,1003611,1003616,1040000,1040009,1040012,1040015,1040016,1040021,1040026,1040028,1040029,1040030,1040037,1040038,1040039,1040040,1040041,1040085,1040086,1040087,1040088,1040089,1040090,1040091,1040092,1040093,1040102,1040103,1040104,1040111,1040112,1040113,1040120,1040121,1040122,1041014,1041019,1041020,1041021,1041022,1041023,1041024,1041064,1041084,1041085,1041086,1041087,1041088,1041089,1041091,1041092,1041093,1041097,1041098,1041099,1041119,1041120,1041121,1041122,1041123,1041124,1050000,1050005,1050006,1050007,1050011,1050021,1050022,1050080,1050081,1050082,1050083,1051000,1051001,1051010,1051011,1051012,1051013,1051014,1051015,1051016,1051077,1051078,1051079,1051080,1052075,1052516,1052521,1052824,1060000,1060008,1060009,1060010,1060011,1060016,1060017,1060018,1060019,1060020,1060027,1060028,1060029,1060030,1060060,1060074,1060075,1060076,1060077,1060078,1060079,1060080,1060081,1060082,1060090,1060091,1060092,1060100,1060101,1060102,1060109,1060110,1060111,1061014,1061015,1061016,1061017,1061018,1061019,1061020,1061023,1061083,1061084,1061085,1061086,1061087,1061088,1061090,1061091,1061092,1061096,1061097,1061098,1061118,1061119,1061120,1061121,1061122,1061123,1072000,1072002,1072003,1072007,1072009,1072011,1072039,1072040,1072041,1072046,1072047,1072050,1072051,1072052,1072053,1072112,1072113,1072126,1072127,1072132,1072133,1072134,1072135,1072147,1072148,1072149,1072154,1072155,1072156,1072168,1072196,1072197,1072198,1072210,1072211,1072212,1072220,1072221,1072222,1072273,1072719,1072724,1082000,1082001,1082003,1082004,1082005,1082006,1082007,1082008,1082009,1082010,1082011,1082023,1082024,1082025,1082035,1082036,1082059,1082060,1082061,1082103,1082104,1082105,1082114,1082115,1082116,1082117,1082128,1082129,1082130,1082139,1082140,1082141,1082168,1082478,1082483,1092000,1092001,1092002,1092004,1092005,1092006,1092007,1092009,1092010,1092011,1092012,1092013,1092014,1092015,1092016,1092017,1092023,1092024,1092025,1092026,1092027,1092028,1092036,1092037,1092038,1092060,1112653,
//武器
1412162,1422169,1432198,1442169,1442252,1402234,1302313,1312183,1322234,1412026,1422028,1432038,1442045,1402036,1432048,1302059,1312031,1322052,1422030,1412021,1422027,1422031,1432030,1442002,1442044,1402037,1402035,1302056,1312030,1322045,1412010,1422013,1432011,1442020,1302104,1302284,1322209,1402005,1402016,1412087,1422089,1432117,1302023,1312015,1322029,1402129,1412009,1422012,1432010,1442019,1302032,1302192,1312098,1322138,1402004,1402015,1302018,1312011,1322028,1412008,1422010,1432007,1442008,1322020,1402012,1442154,1302012,1312010,1322019,1402017,1412007,1412045,1422009,1422048,1432006,1432060,1442010,1442085,1402011,1402071,1302037,1302011,1302130,1312009,1312047,1322018,1322075,1412003,1422005,1432004,1442005,1402003,1322017,1302010,1302019,1302068,1312008,1302015,1412005,1422007,1432005,1432136,1442009,1442080,1402007,1402149,1322016,1322156,1322159,1302009,1302218,1302220,1302222,1312007,1412004,1422008,1432003,1442003,1402006,1402148,1322015,1322155,1322158,1302004,1312006,1412006,1412028,1422001,1422032,1432002,1432041,1432157,1442001,1442052,1402002,1402040,1402147,1322014,1322157,1302008,1302066,1302217,1302219,1302221,1302223,1312005,1312033,1322055,1412000,1402008,1312003,1412002,1422003,1442007,1402000,1302002,1312016,1412012,1422002,1442006,1402018,1302005,1312001,1402177,
All_eq
],
[//楓葉展示
1092046,
//綠寶石
1302169,1312068,1322099,1402106,1412067,1422069,1432095,1442132,
//白金
1302170,1312069,1322101,1402107,1412068,1422070,1432096,1442133,
1302142,1302172,1302212,1302227,1312032,1312056,1312071,1312114,1312116,1322054,1322084,1322105,1322154,1322162,1402039,1402085,1402104,1402105,1402109,1402145,1402151,1412011,1412027,1412055,1412070,1412102,1412104,1422014,1422029,1422057,1422072,1422105,1422107,1432012,1432040,1432075,1432098,1432135,1432138,1442024,1442051,1442104,1442130,1442131,1442135,1442173,1442182,
0
],
[//無法交易
1002990,1003031,1003197,1003297,1003302,1003307,1003312,1003316,1003321,1003326,1003331,1003336,1003341,1003554,1003555,1003556,1003557,1003840,1003911,1004516,1004517,1004518,1004519,1040145,1040159,1040163,1040166,1040170,1040177,1040180,1040182,1040184,1041148,1041161,1041168,1041172,1041180,1041183,1041185,1041187,1050163,1050196,1050198,1051202,1051239,1051241,1051242,1052270,1052333,1052444,1052463,1052464,1052465,1052466,1052606,1052607,1052632,1052633,1052931,1052932,1052933,1052934,1060134,1060149,1060153,1060155,1060159,1060165,1060168,1060170,1060172,1061156,1061171,1061178,1061182,1061189,1061192,1061194,1061196,1072399,1072419,1072502,1072564,1072569,1072574,1072579,1072584,1072589,1072594,1072599,1072604,1072668,1072669,1072670,1072671,1072732,1072737,1072743,1072814,1072837,1073064,1073065,1073066,1073067,1082256,1082305,1082346,1082351,1082356,1082361,1082366,1082371,1082376,1082381,1082386,1082434,1082435,1082436,1082437,1082521,1082535,1082543,1082649,1082650,1082651,1082652,1092041,1092042,1092095,1092096,1092097,1092098,1092099,1092100,1092101,1092102,1092103,1092104,1098007,1099013,1102471,1102476,1102481,1102490,1102493,1102552,1102594,1112782,1113180,1113237,
0
],
[//無法交易
1402180,1402194,1402198,1402248,1412175,1422182,1432212,1302327,1312197,1322248,1402223,1412154,1422161,1432190,1402212,1402252,1412149,1412178,1422154,1422185,1432180,1432215,1442266,1302300,1312175,1322226,1402113,1432101,1402184,1402191,1412132,1422136,1432164,1302291,1302334,1312167,1312200,1322217,1322251,1302175,1312096,1322136,1302264,1312150,1322199,1412145,1422150,1432177,1442233,1402209,1432156,1442244,1412127,1422130,1432159,1442210,1302288,1312163,1322214,1402183,1402247,1412174,1422181,1432211,1442236,1442265,1442269,1402186,1302256,1312141,1322127,1322187,1302257,1312143,1322189,1442138,1302326,1312196,1322247,1442219,1432066,1442090,1402073,1412016,1412020,1412803,1412903,1422018,1422022,1422803,1422903,1432023,1432028,1432804,1432904,1442034,1442038,1442803,1442903,1302226,1312094,1322134,1402075,1412057,1422058,1432076,1442105,1402023,1402028,1402804,1402904,1302078,1312057,1322085,1412129,1422132,1432161,1442212,1302042,1302047,1302804,1302904,1312021,1312025,1312803,1312903,1322039,1322044,1322804,1322904,1402188,1412015,1412019,1412802,1412902,1422017,1422021,1422802,1422902,1432022,1432027,1432055,1432155,1432803,1432903,1442033,1442037,1442155,1442208,1442802,1442902,1302259,1312146,1322192,1402022,1402027,1402182,1402803,1402903,1442091,1412039,1442075,1302041,1302046,1302255,1302803,1302903,1312020,1312024,1312140,1312802,1312902,1322038,1322043,1322186,1322803,1322903,1402058,1402074,1402059,1412014,1412018,1412901,1422016,1422020,1422901,1432021,1432026,1432083,1432902,1442032,1442036,1442901,1412063,1422064,1432085,1442115,1302113,1312043,1322069,1402021,1402026,1402093,1402154,1402902,1412107,1422043,1422110,1432141,1442185,1402094,1412038,1422042,1432054,1442074,1312119,1322165,1402056,1302040,1302045,1302151,1302902,1312019,1312023,1312067,1312901,1322037,1322042,1322091,1322902,1412086,1412146,1422088,1422151,1432115,1442153,1302112,1312042,1322068,1402128,1422039,1432050,1442207,1302191,1312087,1312164,1322121,1322126,1402053,1412120,1422122,1432148,1442200,1412013,1412017,1412085,1412900,1422015,1422019,1422087,1422900,1432020,1432025,1432082,1432114,1432154,1432901,1442031,1442035,1442152,1442900,1402170,1302246,1312133,1322179,1402020,1402025,1402084,1402092,1402127,1402181,1402901,1412054,1422056,1432074,1442102,1322036,1322041,1322185,1322901,1302039,1302044,1302190,1302254,1302901,1312018,1312022,1312086,1312139,1312900,1322120,1402057,1412084,1412801,1422086,1422801,1432113,1432802,1442151,1442801,1402228,1402232,1412160,1422167,1432196,1442206,1302141,1312055,1322083,1402041,1402126,1402802,1412029,1422033,1432042,1442053,1322119,1322125,1322802,1412035,1442071,1302189,1302311,1302802,1312034,1312085,1312181,1312801,1322056,1322232,1412042,1412047,1412083,1422045,1422049,1422085,1432019,1432024,1432057,1432067,1432112,1432900,1442082,1442092,1442093,1442094,1442150,1402019,1402024,1402064,1402077,1402125,1402900,1322035,1322040,1322073,1322076,1322118,1322900,1442250,1302038,1302043,1302119,1302134,1302188,1302900,1312045,1312048,1312084,1412082,1412800,1422084,1432111,1432801,1442149,1442800,1402124,1402801,1322117,1322801,1422800,1302187,1302801,1312083,1312800,1412030,1412031,1412081,1422034,1422035,1422083,1432043,1432044,1432110,1442058,1442059,1442148,1402042,1402043,1402123,1322116,1322124,1402081,1412051,1422053,1432071,1442099,1302075,1302076,1302186,1312035,1312036,1312082,1322057,1322058,1412080,1432029,1412046,1432061,1442103,1402072,1402122,1302133,1312081,1412079,1422081,1442146,1432800,1402076,1402078,1402121,1412048,1422050,1432068,1442096,1302138,1312052,1322080,1402800,1302184,1312080,1322800,1412078,1422080,1442145,1302800,1402120,1302183,1312079,1302077,1302135,1302182,1312049,1322077,1442077,1322122,
0
],
[//衣服稀有
1052648,1003290,1052384,1072554,1082338,1102312,1003770,1052580,1072786,1082506,1102514,1004082,1052702,1072903,1082575,1003689,1052545,1072695,1082490,1102498,
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
						var RandomIJob = Math.floor(Math.random() * (GachItem[o][getJob()].length));//職業限定
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

function getJob(){
        switch (cm.getJob()) {
            case 110:case 111:case 112://劍,斧
				return 1;
			case 120:case 121:case 122://劍,棍
				return 2;
			case 130:case 131:case 132://槍,矛
				return 3;
			case 1100:case 1110:case 1111:case 1112://劍
				return 4;
			case 2000:case 2100:case 2110:case 2111:case 2112://矛
				return 5;
			default:
				return 0;
		}
}

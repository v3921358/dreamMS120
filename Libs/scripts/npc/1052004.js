/* Denma the Owner
	Henesys VIP Eye Change.
*/
var status = -1;
var facetype;

function start() {
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 0) {
	cm.dispose();
	return;
    } else {
	status++;
    }

    if (status == 0) {
	cm.sendNext("���U�N�s �A�Q���Q���i�Ӯ��y�e�O? �u�ݭn�@�i #b#t5152001##k, �A�N�i�H�֦��l���M�h�Ϊ��y�e�F");
    } else if (status == 1) {
	var face = cm.getPlayerStat("FACE");

	if (cm.getPlayerStat("GENDER") == 0) {
	    //facetype = [20000, 20001, 20002, 20003, 20004, 20005, 20006, 20007, 20008, 20012, 20014];
		facetype = [20000,20001,20002,20003,20004,20005,20006,20007,20008,20009,20010,20011,20012,20013,20014,20015,20016,20017,20018,20019,20020,20021,20022,20024,20025,20027,20028,20029,20030,20031,20032,20033,20035,20036,20037,20038,20040,20043,20044,20045,20046,20047,20048,20049,20050,20051,20052,20053,20055,20056,20057,20058,20059,20060,20061,20062,20063,20064,20065,20066,20067,20068,20069,20070,20073,20074,20075,20076,20077,20078,20080,20081,20082,20083,20084,20085,20086,20087,20088,20089,20090,20093,20094,20095,20097,20098,20099];
	} else {
	    //facetype = [21000, 21001, 21002, 21003, 21004, 21005, 21006, 21007, 21008, 21012, 21014];
		facetype = [21000,21001,21002,21003,21004,21005,21006,21007,21008,21009,21010,21011,21012,21013,21014,21015,21016,21017,21018,21019,21020,21021,21022,21023,21024,21026,21027,21028,21029,21030,21031,21033,21034,21035,21036,21038,21041,21042,21043,21044,21045,21046,21047,21048,21049,21050,21052,21053,21054,21055,21056,21057,21058,21059,21060,21061,21062,21063,21064,21065,21068,21069,21070,21071,21072,21073,21074,21075,21076,21077,21078,21079,21080,21081,21082,21083,21084,21085,21086,21087,21088,21089,21090,21091,21092,21093,21094,21095,21096,21097,21098];
	}
	for (var i = 0; i < facetype.length; i++) {
	    facetype[i] = facetype[i] + face % 1000 - (face % 100);
	}
	cm.askAvatar("�A�ݧA���w���i�y�e �u�ݭn#b#t5152001##k, �N�i�H���A�����l���M�h�Ϊ��y�e�F", facetype);
    } else if (status == 2){
	if (cm.setAvatar(5152001, facetype[selection]) == 1) {
	    cm.sendOk("Enjoy your new and improved face!");
	} else {
	    cm.sendOk("�ݨӧA�S���D��O ���U�N�s");
	}
	cm.dispose();
    }
}
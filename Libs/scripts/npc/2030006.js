/* Holy Stone
	Hidden Street: Holy Ground at the Snowfield (211040401)
	
	Custom quest: 100102
*/

var questions = new Array(
	//正確答案
	"請問，氣象下雪系統的經驗倍率是否為150%",
	"請問，本服的怪物，是否會掉落點數",
	"請問，每累積10級即可領取的獎勵包，80%戒指的時效是否為2小時",
	"請問，楓之島能否見到菇菇寶貝",
	"請問，遊戲排行內有當日點數排行榜",
	//錯誤答案
	"請問，每日簽到系統需要上線60分鐘",
	"請問，皇家騎士團71等的之後，每升等的能力職為6",
	"請問，自由市場掛機30分鐘可以獲得Gash",
	"請問，簽到系統內的兌換物品，祝福卷軸為25個音符",
	"請問，升級獎勵內的禮包，有沒有50等",
	"請問，影武者55等可以轉殖為隱忍",
	"請問，10等就能使用轉蛋機了",
	"請問，本服的版本為V117",
	"請問，10等禮包內，送的寵物為大象",
	"請問，本服的經驗倍率為5倍"
);
var answers = new Array(false,false,false,false,false,true,true,true,true,true,true,true,true,true,true);
var rOutput = new Array(
	//正確答案
	"氣象下雪系統的經驗倍率是150%",
	"本服的怪物會掉落點數",
	"每累積10級即可領取的獎勵包，80%戒指的時效為2小時",
	"楓之島能見到菇菇寶貝",
	"遊戲排行內有當日點數排行榜",
	//錯誤答案
	"每日簽到系統需要上線120分鐘",
	"皇家騎士團71等的之後，每升等的能力職為5",
	"自由市場掛機30分鐘可以獲得楓葉點數",
	"簽到系統內的兌換物品，祝福卷軸需要20個音符",
	"升級獎勵內的禮包沒有50等地裡包",
	"影武者55等可以轉殖為上忍",
	"30等才可以使用轉蛋機了",
	"本服的版本為V120",
	"10等禮包內，送的寵物為熊貓",
	"本服的經驗倍率為4倍"
	);
var asked = new Array();
var currentQuestion;
var junk = new Array;
var junkWeap = new Array;
var goodEqWeap = new Array;
var useable = new Array;
var Rare = new Array;
var Select;
var openEvent = 0;
var QA = false;//限制只會扣除一次

var status = 0;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
   if (status == 3 && mode == 1) {
        status = 2;
        selection = 0;
    } else if (mode == 1 || (mode == 0 && type == 1))
        status++;
    else {
        if (type == 12 && mode == 0)
            cm.sendOk("我是一個石頭...");
        cm.dispose();
        return;
    }
    if (status == 0) {
	if (cm.haveItem(4031058)) {
	    cm.sendOk("我是一個石頭.");
	    cm.dispose();
	} else {
	    if (!cm.haveItem(4005004)) {
		cm.sendOk("你並沒有 #v4005004##b#t4005004##k");
		cm.dispose();
	    } else {
		cm.sendNext("\r.........\r\n想要測試你的智慧，請獻上#r黑暗水晶#b...\r\n準備好獻上暗黑水晶，開始回答問題了嗎...");
	    }
	}
    } else if (status == 1) {
	cm.sendNext("\r好...現在開始進行智慧的考驗...如果能夠正確回答所有問題，就可以通過考驗...但是如果途中回答了不正確的答案，就需要重新接受挑戰...那就開始吧...");
	QA = true;
    } else if (status == 2) {
	    if (QA) {
		cm.gainItem(4005004, -1);
		QA = false;
	    }
	    if (asked.length == 5) {//回答完成部分
		cm.completeQuest(100102);
		cm.gainItem(4031058, 1);
		cm.sendNext("你的回答中並沒有愚蠢的答案...\r\n證明了你是一個有智慧的人，\r\n拿著這條項鍊回去吧...");
		cm.dispose();
	    } else {
		currentQuestion = -1;
		while (contains(currentQuestion) || currentQuestion == -1) {
		currentQuestion = Math.floor(Math.random() * questions.length);
	    }
		asked.push(currentQuestion);
		cm.sendSimple("第 "+asked.length+" 個問題\r\n\r\n"+questions[currentQuestion]+"#b\r\n#L0# 是。\r\n#L1# 否。");
	    }//全部回答完成，和提問題部分


    } else if (status == 3) {
	    var answer = selection == 0 ? false : true;
	    if (answers[currentQuestion] == answer) {
		cm.sendNext("恭喜你，回答正確。#r\r\n\r\n"+rOutput[currentQuestion]);
            } else {
		cm.sendOk("很遺憾，回答錯誤。#b\r\n\r\n"+rOutput[currentQuestion]+"\r\n回答錯誤之後就不能再答題了。");
		cm.dispose();
	    }
    }
}

function contains(quest) {
    for (var i = 0; i < asked.length; i++) {
        if (asked[i] == quest)
            return true;
    }
    return false;
}
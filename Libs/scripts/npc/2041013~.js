var status = -1;
var req = 4001157;
var item = [
 
 [1112763, 10],
 [1112767, 10],
 [1112771, 10],
 [1112775, 10],
 [1112127, 15],
 [1112956, 20],
];

function start() {
 action(1, 0, 0);
}

function action(mode, type, selection) {
 if (mode == 1) {
  status++;
 } else if (mode == 0) {
  status--;
 } else {
  cm.dispose();
  return;
 }
 var i = -1;
 if (status <= i++) {
  cm.dispose();
 } else if (status === i++) {
  var msg = "";
  for (var v = 0; v < item.length; v++) {
   var id = item[v][0];
   var qty = item[v][1];
   msg += "#L"+v+"##v" + item[v][0] + ":##t" + item[v][0] + "#1個 = #b" + item[v][1] + "#d 方便幣#k#l\r\n";
  }
  cm.sendSimple("#r親愛的玩家您好，方便谷幣兌換區、#i"+req+"#，可兌換的豐富獎品如下~#k#l\r\n" + msg);
 } else if (status === i++) {
  if (!cm.canHold(item[selection][0])) {
   cm.sendNext("你的背包裝不下");
   cm.dispose();
   return;
  } else if (!cm.haveItem(req, item[selection][1])) {
   cm.sendNext("身上沒有#v" + req + "#" + item[selection][1]+"個");
   cm.dispose();
   return;
  }
  cm.gainItem(req, -item[selection][1]);
  cm.gainItem(item[selection][0], 1);
  cm.sendOk("感謝您，看看有沒有拿到囉。");
  cm.dispose();
 }
}
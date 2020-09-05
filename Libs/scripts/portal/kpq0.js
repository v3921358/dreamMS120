
/*
Kerning PQ: 1st stage to 2nd stage portal
*/

function enter(pi) {
    var eim = pi.getEventManager("KerningPQ").getInstance("KerningPQ");

    // only let people through if the eim is ready
    if (eim.getProperty("1stageclear") == null) { // do nothing; send message to player
        pi.playerMessage(5, "該洞口目前無法進入.完成任務後才可通過。");
    } else {
        pi.warp(103000801, "st00");
		pi.removeAll(4001008);
    }
}
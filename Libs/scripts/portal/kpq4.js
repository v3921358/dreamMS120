/*
Kerning PQ: 4th stage to final stage portal
*/

function enter(pi) {
    var eim = pi.getEventManager("KerningPQ").getInstance("KerningPQ");

    // only let people through if the eim is ready
    if (eim.getProperty("4stageclear") == null) { // do nothing; send message to player
	pi.playerMessage(5, "該洞口目前無法進入,完成任務後才可通過.");
    } else {
	pi.warpParty(103000804, "st00");
    }
}
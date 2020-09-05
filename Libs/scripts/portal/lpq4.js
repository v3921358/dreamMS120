function enter(pi) {
    var eim = pi.getEventManager("LudiPQ").getInstance("LudiPQ");
    
    // only let people through if the eim is ready
    if (eim.getProperty("stage4status") == null) { // do nothing; send message to player
	pi.playerMessage(5, "尚未完成此關卡任務.");
    } else {
	pi.warp(pi.getMapId() + 100, "st00");
    }
}
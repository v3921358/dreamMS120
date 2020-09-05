function enter(pi) {
    var em = pi.getEventManager("HorntailBattle");
    if (em != null) {
        var map = pi.getMapId();
		var d1 = pi.getMap(240060000);
		var d2 = pi.getMap(240060100);
        if (map == 240060000) {
            if (d1.getAllMonstersThreadsafe().size() <= 0) {
                em.warpAllPlayer(240060000, 240060100);
            } else {
                pi.playerMessage("這個門還沒開起。");
            }
        } else if (map == 240060100) {
            if (d2.getAllMonstersThreadsafe().size() <= 0) {
                em.warpAllPlayer(240060100, 240060200);
				em.setProperty("state", "4");
            } else {
                pi.playerMessage("這個門還沒開起。");
            }
        }
    }
}
function enter(pi) {
	pi.getPlayer().saveLocation(org.server.maps.SavedLocationType.PACH);
	pi.warp(809030000, "out00");
	return true;
}
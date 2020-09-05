importPackage(Packages.server.life);
importPackage(Packages.java.lang);
importPackage(Packages.server);
importPackage(Packages.tools);
importPackage(Packages.database); 

var status = -1;
var selections = {ITEM: 0, MOB: 1, MAP: 2};
var category;

function start() {
    cm.sendSimple("你想要查詢怪物什麼資訊呢?", "查詢什麼怪物掉落的特定項目", "查詢怪物的屬性和掉落物品", "查詢此地圖中的怪物數據");
}

function action(m,t,s) {
    if (m == -1 || s == 999)        {
        cm.dispose();
        return;
    } else if (m == 0 || s == 1000 || status == 3 || status == 2 && category == selections.MOB || status == 1 && category == selections.MAP) {
        start();
        status = -1;
    } else {
        status++;
    }
    if (status == 0) { 
        category = s;
        if (category == selections.ITEM) {
             cm.sendGetText("您想搜索什麼項目？");
        } else if (category == selections.MOB) {
            cm.sendGetText("你想找什麼怪物？");
        } else if (category == selections.MAP) {
            cm.sendSimple(getMobsInMap());
        }
    } else if (status == 1) {
        if (category == selections.ITEM) {
            cm.sendSimple(searchItem(cm.getText()));
        } else if (category == selections.MOB) {
            cm.sendSimple("這些是目前適合你的描述的怪物\r\n" + cm.searchMobs(cm.getText()));
        } else if (category == selections.MAP) {
            cm.sendNext(getMobInfo(s));
        }
    } else if (status == 2) {
        if (category == selections.ITEM) {
            cm.sendSimple(mobsThatDrop(s));
        } else if (category == selections.MOB) {
            cm.sendNext(getMobInfo(s));
        }
    } else if (status == 3) {
        if (category == selections.ITEM) {
            cm.sendNext(getMobInfo(s));
        }
    } 
}

function searchItem(item) {
    var text = "這些是在您的搜索描述下出現的項目\r\n";
    var items = MapleItemInformationProvider.getInstance().getAllItems().toArray();
    for (var x = 0, counter = 0; x < items.length; x++) {
        if (counter > 999) {
            text += "#L999##e將上傳的多個項目#n#l";
            break;
        }
        if (items[x].getRight().toLowerCase().contains(item.toLowerCase())) {
            text += "#L"+items[x].getLeft()+"##b" + items[x].getLeft() + "#k - #r#z" + items[x].getLeft() + "##l\r\n";
counter++;
        }
    }
    return text + "\r\n#L999##b結束#k" + "\r\n#L1000##b返回#k";
}

function getMobsInMap() {
    var text = "您目前在 #m "+ cm.getPlayer().getMapId() +"#. What mob would you" +
            " like to inspect?\r\n";
    var mobs = cm.getPlayer().getMap().getUniqueMonsters();
    for (var x = 0; x < mobs.size(); x++)
        text += "\r\n#b#L" + mobs.get(x) + "##o"+ mobs.get(x) +"# ("+mobs.get(x)+")";
    text += "\r\n#L999##b結束#k\r\n#L1000##b返回#k";
    return text;
}

function getMobInfo(mobid) {//第二選項
    var mob = MapleLifeFactory.getMonster(mobid);
    var drop_rate = 1000000 / cm.getPlayer().getClient().getChannelServer().getDropRate();
    var text =  "以下是 #b#o"+mobid+"##k ("+mobid+")的資訊\r\n\r\n";
        text += "等級: " + mob.getStats().getLevel() + "\r\n";
        text += "生命值: " + mob.getMobMaxHp() + "\r\n";
        text += "魔力值: " + mob.getMobMaxMp() + "\r\n";
        text += "經驗: " + mob.getMobExp() + "\r\n";
        text += "Health/Exp Ratio: " + (Math.round((mob.getMobMaxHp() / mob.getMobExp() * 10)))/10 + "\r\n";
        text += cm.getPlayer().getClient().getChannelServer().getDropRate() + "\r\n";
        text += "#e掉寶清單#n";
    var drops = MapleMonsterInformationProvider.getInstance().retrieveDrop(mobid).toArray();
    for (var x = 0; x < drops.length; x++)
        text += "\r\n#i" + drops[x].itemId + "# #z"+drops[x].itemId+"# ("+drops[x].itemId+") - " + (100 * drops[x].chance/drop_rate) + "%";
    var global_drops = MapleMonsterInformationProvider.getInstance().getGlobalDrop().toArray();
    for (var x = 0; x < global_drops.length; x++)
        text += "\r\n#i" + global_drops[x].itemId + "# #z"+global_drops[x].itemId+"# ("+global_drops[x].itemId+") - " + (100 * global_drops[x].chance/drop_rate) + "%";
    return text;
}

function getHarmlessMobInfo(mobid) {
    var mob = MapleLifeFactory.getMonster(mobid);
    var drop_rate = 1000000 / cm.getPlayer().getClient().getChannelServer().getDropRate();
    var text =  "以下是 #b#o"+mobid+"##k ("+mobid+")的資訊\r\n\r\n";
        text += "等級: " + mob.getStats().getLevel() + "\r\n";
        text += "生命值: " + mob.getMobMaxHp() + "\r\n";
        text += "魔力值: " + mob.getMobMaxMp() + "\r\n";
        text += "經驗: " + mob.getMobExp() + "\r\n";
        text += "Health/Exp Ratio: " + (Math.round((mob.getMobMaxHp() / mob.getMobExp() * 10)))/10 + "\r\n";
        text += "\r\n";
        text += "#e掉寶清單#n";
    var drops = MapleMonsterInformationProvider.getInstance().retrieveDrop(mobid).toArray();
    for (var x = 0; x < drops.length; x++)
        text += "\r\n#t"+drops[x].itemId+"# ("+drops[x].itemId+") - " + (100 * drops[x].chance/drop_rate) + "%";
    
    var global_drops = MapleMonsterInformationProvider.getInstance().getGlobalDrop().toArray();
    for (var x = 0; x < global_drops.length; x++)
        text += "\r\n#i" + global_drops[x].itemId + "# #z"+global_drops[x].itemId+"# ("+global_drops[x].itemId+") - " + (100 * global_drops[x].chance/drop_rate) + "%";
    
    return text;
}
function mobsThatDrop(itemid) {
    var start_time = System.currentTimeMillis();
    var text = "此為掉落 #e#z"+itemid+"##n ("+itemid+")的怪物清單\r\n";
    var ps = DatabaseConnection.getConnection().prepareStatement("SELECT dropperid, chance FROM drop_data WHERE itemid = ?");
    ps.setInt(1, itemid);
    var rs = ps.executeQuery();

    
    while (rs.next()) {
        var mobid = rs.getInt("dropperid");
        var chance = rs.getInt("chance")/(1000000/cm.getPlayer().getClient().getChannelServer().getDropRate()) * 100;
        text += "\r\n#L" + mobid + "##b#o" + mobid + "##k (" + mobid + ") - #r" + chance + "%#k";
    }
    text += "#b\r\n#L999#結束#k#l";
    text += "#e\r\n\r\nLoaded in " + (System.currentTimeMillis() - start_time)/1000 + " seconds#n";
    rs.close();
    ps.close();
    return text;
}  
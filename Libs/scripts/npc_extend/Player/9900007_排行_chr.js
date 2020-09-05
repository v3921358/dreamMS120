importPackage(Packages.server); 
/*
	內容：個人排行榜
*/

var PreviousPage = new Array(9900007, "home_chr");//上一頁
var status = -1;
var limit = 100;
var ttt = "#fUI/UIWindow/Quest/icon2/7#";//"+ttt+"//美化1

function start() {
	action(1, 0, 0);
}

function action(mode, type, selection) {
	if (mode == 0) {
		cm.dispose();
		return;
	}
	status++;
	if (status == 0) {
		var text = "#e請選擇要查詢的榜單：#r\r\n";
		//text += "\t\t\t#L2#本月充值排行榜#l\r\n";
		//text += "\t\t\t#b#L6#突破上限排行榜#l\r\n";
		text += "\t\t\t#r#L0#世界等級排行榜#l\r\n";
		//text += "\t\t\t#L4#世界富豪排行榜#l\r\n";
		//text += "\t\t\t#L5#種植高手排行榜#l\r\n";
		text += "\t\t\t#L1#世界名聲排行榜#l\r\n";
		text += "\t\t\t#L3#世界公會排行榜#l\r\n";
		text += "\t\t\t#L9#當日點數排行榜#l\r\n";
		text += "\r\n\t\t\t #n#L999#"+ "#b" + ttt + "返回上一頁";
		cm.sendSimple(text);
	} else if (status == 1) {
		if (selection == 0) {
			//var list = cm.getRankingTopInstance().getLevelRank().iterator();
			var conn = cm.getConnection();
			var sql = "select name,level,gender,job from characters where gm<=0 order by level desc, exp desc limit 100;";
			var pstmt = conn.prepareStatement(sql);
			var result = pstmt.executeQuery();
			var text = "\t\t\t\t#e#d★ 個人等級排行 ★#k#n\r\n\r\n";
			text += "\t#e名次#n\t#e玩家暱稱#n\t\t#e等級#n\t#e職業#n\r\n";
			for (var i = 1; i <= limit; i++) {
				if (!result.next()) {
					break;
				}
				if (i == 1) {
					text += "#r";
				} else if (i == 2) {
					text += "#g";
				} else if (i == 3) {
					text += "#b";
				}
				text += "\t  " + format(" ", 7, i.toString());
				
				// 填充名字空格
				text += format(" ", 17, result.getString("name"));
				//
				text += format(" ", 8, result.getString("level"));
			
				text += MapleCarnivalChallenge.getJobNameById(result.getString("job"));
				
				//text += "\t\t\t " + result.getString("reborns1")+"#k";

				text += "\r\n";
			}
			result.close();
			pstmt.close();
			conn.close();
			cm.sendOkS(text,2);
			status = -1;
		} else if (selection == 1) {
			var conn = cm.getConnection();
			var sql = "select name,fame,gender from characters where gm<=0 order by fame desc limit 100;";
			var pstmt = conn.prepareStatement(sql);
			var list = pstmt.executeQuery();
			var text = "\t\t\t\t#e#d★ 世界名聲排行 ★#k#n\r\n\r\n";
			text += "\t#e名次#n\t#e玩家暱稱#n\t\t  #e人氣#n\t\t  #e稱號#n\r\n";
			for (var i = 1; i <= limit; i++) {
				if (!list.next()) {
					break;
				}
				if (i == 1) {
					text += "#r";
				} else if (i == 2) {
					text += "#g";
				} else if (i == 3) {
					text += "#b";
				}
				text += "\t  " + format(" ", 7, i.toString());
				
				// 填充名字空格
				text += format(" ", 16, list.getString("name"));

				// 填充人氣度
				text += "\t" + list.getInt("fame");
				var famevalues = list.getInt("fame");
				var famelength = 0;
				while (famevalues > 0) {
					famevalues = Math.floor(famevalues/10);
					famelength += 1;
				}
				for (var j = 8 - famelength; j > 0; j--) {
					text += " ";
				}

				if (i == 1) {
					if (list.getInt("gender") == 0) {
						text += " ★世界偶像★#k";
					} else {
						text += " ★魅力寶貝★#k";
					}
				} else if (i == 2) {
					text += "\t #k";
				} else if (i == 3) {
					text += "\t #k";
				}
				text += "\r\n";
			}
			list.close();
			pstmt.close();
			conn.close();
			cm.sendOkS(text, 2);
			status = -1;
		} else if (selection == 2) {
			cm.dispose();
			cm.openNpc(1540008, 1);
		} else if (selection == 3) {
			var conn = cm.getConnection();
			var sql = "select c.name as leadername, g.name from characters c, guilds g where g.leader=c.id order by g.gp desc limit 10;";
			var pstmt = conn.prepareStatement(sql);
			var list = pstmt.executeQuery();
			var text = "\t\t\t\t#e#d★ 世界公會排行 ★#k#n\r\n\r\n";
			text += "\t#e名次#n\t\t#e 公會名稱#n\t\t\t#e  公會長#n\r\n";
			for (var i = 1; i <= limit; i++) {
				if (!list.next()) {
					break;
				}
				if (i == 1) {
					text += "#r";
				} else if (i == 2) {
					text += "#g";
				} else if (i == 3) {
					text += "#b";
				}
				text += "\t  " + format(" ", 12, i.toString());
				
				// 填充名字空格
				text += format(" ", 18, list.getString("name"));

				// 填充族長名稱
				text += "\t " + list.getString("leadername");
				
				text += "#k\r\n";
			}
			list.close();
			pstmt.close();
			conn.close();
			cm.sendOkS(text, 2);
			status = -1;
		} else if (selection == 4) {
			var conn = cm.getConnection();
			var sql = "select c.name, (c.meso+b.money*100000000) as totalmoney from characters c,bank b where b.charid=c.id order by totalmoney desc limit 10;";
			    sql = "SELECT *, ( chr.meso + s.meso ) as totalmoney FROM `characters` as chr , `storages` as s WHERE chr.gm < 1 AND s.accountid = chr.accountid ORDER BY totalmoney DESC LIMIT 20;";
			var pstmt = conn.prepareStatement(sql);
			var list = pstmt.executeQuery();
			var text = "\t\t\t\t#e#d★ 世界富豪排行 ★#k#n\r\n\r\n";
			text += "\t#e名次#n\t#e玩家暱稱#n\t\t  #e資產#n\t\t  #e稱號#n\r\n";
			for (var i = 1; i <= limit; i++) {
				if (!list.next()) {
					break;
				}
				if (i == 1) {
					text += "#r";
				} else if (i == 2) {
					text += "#g";
				} else if (i == 3) {
					text += "#b";
				}
				text += "\t " + i + "\t\t ";
				
				// 填充名字空格
				text += format(" ", 16, list.getString("name"));

				// 填充資產
				var zc = ""+(list.getLong("totalmoney")/100000000).toFixed(2)+"億";
				text += "  " + zc;
				var totalmoney = list.getLong("totalmoney");
				var totalmoneylength = 0;
				while (totalmoney > 0) {
					totalmoney = Math.floor(totalmoney/10);
					totalmoneylength += 1;
				}
				for (var j = 8 - totalmoneylength; j > 0; j--) {
					text += " ";
				}

				
				text += "#k\r\n";
			}
			list.close();
			pstmt.close();
			conn.close();
			cm.sendOkS(text, 2);
			cm.dispose();
		} else if (selection == 5) {
			var conn = cm.getConnection();
			var sql = "select c.name,g.level from characters c, memory_garden g where c.gm<=0 and c.id=g.charid order by g.level desc, g.exp desc limit 10;";
			var pstmt = conn.prepareStatement(sql);
			var result = pstmt.executeQuery();
			var text = "\t\t\t\t#e#d★ 種植高手排行 ★#k#n\r\n\r\n";
			text += "\t#k#e名次#n\t#e玩家暱稱#n\t\t#e花園等級#n\t\t #e稱號#n\r\n";
			for (var i = 1; i <= limit; i++) {
				if (!result.next()) {
					break;
				}
				if (i == 1) {
					text += "#r";
				} else if (i == 2) {
					text += "#g";
				} else if (i == 3) {
					text += "#b";
				}
				text += "\t " + i + "\t\t ";
				
				// 填充名字空格
				text += result.getString("name");
				for (var j = 16 - result.getString("name").getBytes().length; j > 0 ; j--) {
					text += " ";
				}
				text += "\t " + result.getString("level");
				if (i == 1) {
					text += "\t\t ★天工開物★#k";
				} else if (i == 2) {
					text += "\t\t ★妙手回春★#k";
				} else if (i == 3) {
					text += "\t\t ★熟能生巧★#k";
				}
				text += "\r\n";
			}
			result.close();
			pstmt.close();
			conn.close();
			cm.sendOkS(text, 2);
			cm.dispose();
		} else if (selection == 6) {
			var em = getEvent("NewEvent45", 1);
			var rankingData = em.getObjectProperty("limitBreakRankingData");
			var rankingUpdateTime = (em.getProperty("limitBreakUpdateTime") == null) ? 0 : em.getProperty("limitBreakUpdateTime") * 1;
			var currentTimestamp = parseInt(java.lang.System.currentTimeMillis() / 1000);
			var updateTime = rankingUpdateTime;
			if (rankingData == null || rankingUpdateTime == null || ( currentTimestamp - rankingUpdateTime ) >= 43200) {
				//cm.getPlayer().dropMessage(-11, "Update..."+currentTimestamp+" "+rankingUpdateTime);
				var conn = Packages.database.DatabaseConnection.getConnection();
				var sql = "SELECT ii.limitbreak,i.itemid, c.name FROM inventoryitems i, inventoryequipment ii, characters c WHERE c.id = i.characterid AND ii.inventoryitemid = i.inventoryitemid AND limitbreak > 0 ORDER BY limitbreak DESC LIMIT 0, 100";
				var pstmt = conn.prepareStatement(sql);
				var rs = pstmt.executeQuery();
				rankingData = new Array();
				while(rs.next()) {
					var data = {};
					data.name = rs.getString("name");
					data.itemid = rs.getInt("itemid");
					data.limitbreak = rs.getInt("limitbreak");
					rankingData.push(data);
				}
				rs.close();
				pstmt.close();
				updateTime = currentTimestamp;
				em.setProperty("limitBreakUpdateTime", currentTimestamp);
				em.setObjectProperty("limitBreakRankingData", rankingData);
			} else {
				//cm.getPlayer().dropMessage(-11, "Cache..."+currentTimestamp+" "+rankingUpdateTime);
			}
			var text = "\t\t\t\t#e#d★ 突破上限排行 ★#k#n\r\n\r\n";
			text += "#b\t上次更新時間："+formatDate(updateTime) + "\r\n";
			text += "\t#e名次#n\t#e玩家暱稱#n\t\t  #e突破上限的武器及傷害#n\r\n";
			for (var i = 0; i < rankingData.length; i++) {
				if (i+1 == 1) {
					text += "#r";
				} else if (i+1 == 2) {
					text += "#g";
				} else if (i+1 == 3) {
					text += "#b";
				}
				text += "\t " + (i+1) + "\t\t ";
				
				// 填充名字空格
				text += rankingData[i].name;
				var itemid = rankingData[i].itemid;
				for (var j = 16 - rankingData[i].name.toString().getBytes().length; j > 0 ; j--) 
				{
					text += " ";
				}
				var limitBreak = rankingData[i].limitbreak;
				var limitBreakText = limitBreak;
				if (limitBreak > 99999999) {
					var currentBreak = new Number(limitBreak/100000000).toFixed(3);
					limitBreakText = currentBreak + "億";
				} else if (limitBreak > 9999999 && limitBreak < 100000000) {
					var currentBreak = new Number(limitBreak/10000000).toFixed(3);
					limitBreakText = currentBreak + "千萬";
				} else if (limitBreak > 99999 && limitBreak < 10000000) {
					var currentBreak = new Number(limitBreak/10000).toFixed(3);
					limitBreakText = currentBreak + "萬";
				}
				text += "\t #v"+itemid+"#" + limitBreakText;
				
				text += "\r\n";
			}
			cm.sendOkS(text, 2);
			cm.dispose();
		} else if (selection == 9) {
			var conn = cm.getConnection();
			//var sql = "select `characters`.`name` AS `name`,`bosslog`.`count` AS `count`,`bosslog`.`bossid` AS `bossid` from (`characters` join `bosslog` on((`bosslog`.`accountid` = `characters`.`accountid`))) where (`bosslog`.`bossid` = '今日點數') order by count desc limit 100";
			var sql = "select `characters`.`name` AS `name`,`bosslog`.`bossid` AS `bossid`,`bosslog`.`count` AS `count`,`accounts`.`banned` AS `banned` from ((`accounts` join `characters` on((`accounts`.`id` = `characters`.`accountid`))) join `bosslog` on((`characters`.`id` = `bosslog`.`characterid`))) where ((`bosslog`.`bossid` = 'NowDayCs') and (`bosslog`.`count` > '0') and (`characters`.`gm` <= 0)) order by `bosslog`.`count` desc";
			//var sql = "select name,level,gender,job from characters where gm<=0 and id>199 order by level desc, exp desc limit 100;";
			var pstmt = conn.prepareStatement(sql);
			var list = pstmt.executeQuery();
			var text = "\t\t\t\t#e#d★ 今日點數排行 ★#k#n\r\n\r\n";
			text += "\t#e名次#n\t\t#e 玩家暱稱#n\t\t  #e今日點數#n\r\n";
			for (var i = 1; i <= limit; i++) {
				if (!list.next()) {
					break;
				}
				if (i == 1) {
					text += "#r";
				} else if (i == 2) {
					text += "#g";
				} else if (i == 3) {
					text += "#b";
				}
				text += "\t  " + format(" ", 12, i.toString());
				
				// 填充名字空格
				text += format(" ", 16, list.getString("name"));

				// 填充族長名稱
				var leadername = list.getString("count");
				if (list.getString("banned") > 0)
					leadername += "[已封鎖]";
				text += "\t " + leadername;
				
				text += "#k\r\n";
			}
			list.close();
			pstmt.close();
			conn.close();
			cm.sendOkS(text, 2);
			status = -1;
		} else if (selection == 999) {
			cm.dispose();
			cm.openNpc(PreviousPage[0], PreviousPage[1]);
		}
	}
}

function getEvent(name, channel) {
    var cserv = Java.type("handling.channel.ChannelServer").getInstance(channel);
	var event = cserv.getEventSM().getEventManager(name);
	return event;
}

function formatDate(now) {     
	var now = new Date(parseInt(now) * 1000);
    return now.toLocaleString().replace(/年|月/g, "-").replace(/日/g, " ").replace(/下午/g, "P.M.").replace(/上午/g, 'A.M.').replace(/時|分/g, ":").replace(/秒/g, ''); 
}     

var format = function FormatString(c, length, content) {
    var str = "";
    var cs = "";
    if (content.length > length) {
        str = content;
    } else {
        for (var j = 0; j < length - content.getBytes("big5").length; j++) {
            cs = cs + c;
        }
    }
    str = content + cs;
    return str;
}
    
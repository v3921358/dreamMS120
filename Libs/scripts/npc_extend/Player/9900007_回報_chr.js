/*
	製作：彩虹工作室
	功能：提交建議
	時間：2016年12月28日
*/

var PreviousPage = new Array(9900007, "home_chr");//上一頁
var status = 0;
var pagesize = 5; // 一頁顯示5個
var title, text;
var admin = false;
var Reply, titlemsg, charidmsg, textmsg;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else {
        if (status == 0 || mode == 0) {
            cm.dispose();
            return
        }
        status--;
    }
    if (status == 0) {
        var text = "現在這裡可以提交你的留言給管理員喔！\r\n#b";
        text += "#L0# 我要提交留言。\r\n"
		text += "#L1# 查看個人留言列表。\r\n"
        if (cm.getPlayer().isGM()) {
            text += "#L3# #r查看所有留言列表。(管理員模式)\r\n"
        }
        cm.sendSimple(text);
    } else if (status == 1) {
            var text = "當前留言如下：\r\n#b"
            var count = 0; // 數據量大小
			var conn = cm.getConnection();
			var ps;
			if (selection == 1) {
				ps = conn.prepareStatement("select * from suggest where charid = ?");
				ps.setString(1, cm.getPlayer().getName());
			} else if (selection == 2) {
				ps = conn.prepareStatement("select * from suggest where Reply <> ''");
			} else {
				ps = conn.prepareStatement("select * from suggest");
				admin = true;
			}
            var resultSet = ps.executeQuery();
            while (resultSet.next()) {
				var Reply = " #r已回覆#b";
				if (resultSet.getString("Reply") == null){
					Reply = "";
				}
                text += "#L" + resultSet.getString("id") + "# " + resultSet.getString("title") + Reply + "\r\n";
                count++;
            }
            resultSet.close();
			ps.close();
			conn.close();
        if (selection == 0) {
            cm.sendGetText("請輸入您留言的標題（標題盡量間斷，言簡意賅)：");
        } else if (selection == 1 || selection == 2 || selection == 3) {
            if (count == 0) {
                cm.sendOk("目前還沒有人留言。");
				status = -1;
               // cm.dispose();
            } else {
                status = 3;
                cm.sendSimple(text);
            }
        }
    } else if (status == 2) {
        title = cm.getText();
        if (title.isEmpty()) {
            status = -1;
            cm.sendNext("輸入的標題不可以為空哦！請重新輸入！");
        } else {
            cm.sendGetText("請輸入您的留言，可以詳細輸入您的留言喔~\r\n（管理員都會認真看的喔！！)：");
        }
    } else if (status == 3) {
        text = cm.getText();
        if (text.isEmpty()) {
            status = -1;
            cm.sendNext("輸入的內容不可以為空哦！請重新輸入！")
        } else {
            addSuggestion(title, text);
            cm.sendOk("您已經給管理員提交了您的留言，謝謝您對" + cm.getServerName() + "的支持");
            cm.dispose();
        }
    } else if (status == 4) {
        var text = "";
        var count = 0; // 數據量大小
		var conn = cm.getConnection();
		var ps = conn.prepareStatement("select * from suggest where id = " + selection)
        var resultSet = ps.executeQuery();
        while (resultSet.next()) {
			titlemsg = resultSet.getString("title");
			charidmsg = resultSet.getString("charid");
			textmsg = resultSet.getString("text");
            text += "標題：" + titlemsg + "\r\n"
            text += "玩家：" + charidmsg + "\r\n"
            text += "時間：" + resultSet.getString("date") + "\r\n======================================\r\n內容：\r\n" + textmsg;
			Reply = resultSet.getString("Reply");
			if (Reply != null){
				text += "\r\n\r\n#rGM回覆：\r\n#b" + Reply;
			}
            count++;
        }
		if (admin) {
			cm.sendGetText(text);
		} else {
			cm.sendNext(text);
			status = -1;
		}
        resultSet.close();
		ps.close();
		conn.close();
    } else if (status == 5) {
		admin = false;
		upSuggestion(cm.getText().isEmpty() ? null : cm.getText());
		cm.sendOk("GM回覆完畢");
        status = -1;
    }
}

function addSuggestion(title, text) {
	var conn = cm.getConnection();
    var sug = conn.prepareStatement("INSERT INTO suggest(id, charid ,title, text, date) value(?,?,?,?,?)");
    sug.setString(1, null);
    sug.setString(2, cm.getPlayer().getName());
    sug.setString(3, title);
    sug.setString(4, text);
    sug.setString(5, null);
    sug.executeUpdate();
    sug.close();
	conn.close();
}

function upSuggestion(title) {
	var conn = cm.getConnection();
    var sug = conn.prepareStatement("UPDATE suggest SET Reply = ? WHERE title = ? and charid = ? and text = ?");
    sug.setString(1, title);
	sug.setString(2, titlemsg);
	sug.setString(3, charidmsg);
	sug.setString(4, textmsg);
    sug.executeUpdate();
    sug.close();
	conn.close();
}
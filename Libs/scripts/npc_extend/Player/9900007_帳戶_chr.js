var eff = "#fEffect/CharacterEff/1051296/1/0#";
var ttt = "#fUI/UIWindow/Quest/icon2/7#";//"+ttt+"//美化1
var icon = "#fUI/UIWindow.img/icon/WorldUI/BtQ/normal/0#";
importPackage(Packages.client);
importPackage(Packages.tools);

var status = -1;

var Msg = Array("訂單編號：", "姓　　名：", "手　　機：", "付款金額：", "選擇角色：");
var NTMsg = Array(Msg.length), allname = Array();
var sel, InputList, code = "";
//
var ShowText = Array("#e#b請輸入目前遊戲密碼:","#e#d請輸入新的密碼:","#e#r為了確保安全請再次輸入密碼:"), Password = Array(), updatePasswordHash = false, CheckArray = false, ChangePassword = false;

function start() {
	var map = Java.type("client.MapleClient");
	var formal = cm.getClient().getFacebook_id().split(",")[1] == "1" ? "#b認證成功#r" : "#r尚未認證";
	var Stored = getDataInfo() == "尚未有贊助紀錄" ? "#r查無紀錄" : "#b最後日期#r";
	var text = "#d當前賬戶信息如下：#r(目前以下列表都是給予觀看並非功能)#d\r\n";
	text += "====================================\r\n#b"
	text += "#L0#" + eff + " 贊助核對： #r" + format(" ", 20, getDataInfo().toString()) + "#b #r#e【"+Stored+"】#b#n#l\r\n";
	text += "#L1#" + eff + " 贊助金額： #r" + format(" ", 20, "當月 " + getMonthNT(cm.getClient().getAccountName()).toString() + " | 總計 " +getSumNT(cm.getClient().getAccountName()).toString()) + "#b #e#r【充值贊助】#b#n#l\r\n";
	text += "#L2#" + eff + " 遊戲賬號： #r" + format(" ", 20, cm.getClient().getAccountName()) + "#b #e#r【修改密碼】#b#n#l\r\n";
	text += "#L3#" + eff + " 遊戲名字： #r" + format(" ", 20, cm.getPlayer().getName()) + "#b #e#r【變更名字】#b#n#l\r\n";
	//text += "#L1#" + eff + "\t元寶餘額： #r" + format(" ", 15, cm.getRMB().toString()) + "#b #e#r【充值】#n#l\r\n";
	//text += "#L2#" + eff + "\t#b累計充值： #r" + format(" ", 15, cm.getTotalRMB().toString()) + "#b #e#r【禮包】#b#n#l\r\n";
	text += "#L4#" + eff + " 遊戲CASH： #r" + format(" ", 20, cm.getPlayer().getCSPoints(1).toString()) + "#b #e#r【兌換物品】#b#n#l\r\n";
	text += "#L5#" + eff + " 楓葉點數： #r" + format(" ", 20, cm.getPlayer().getCSPoints(2).toString()) + "#b #e#r【兌換物品】#b#n#l\r\n";
	text += "#L6#" + eff + " Facebook： #r" + format(" ", 20, cm.getClient().getFacebook_id().split(",")[0]) + "#b #r#e【"+formal+"】#b#n#l\r\n";
	text += "\r\n\t\t\t\t#L99#"+ "#b" + ttt + "返回上一頁";
	cm.sendSimple(text);
}

function action(mode, type, selection) {
	if (sel == null)
		sel = selection;
    if (mode == 0) {
        cm.dispose();
        return;
    } else {
        status++;
    }
	if (sel == 0) {//贊助核對
		if (status == 0) {
			if (type == 3) { //3 = sendGetText, 4 = sendGetNumber
				if (InputList == 0 && cm.getText().length == 16 || InputList > 0 && cm.getText() != "")
					NTMsg.splice(InputList, 1, cm.getText());
				else
					cm.getPlayer().dropMessage(5, "[贊助核對]輸入有錯誤, 請再輸入一次.")
			} else if (InputList == 4){
					NTMsg.splice(InputList, 1, allname[selection]);
			}
			//文字處理
			var text = "#e請輸入綠界贊助系統和對資訊:\r\n\r\n";
			var Check = 0;
			for (var ret in Msg) {
				var RecNT = NTMsg.indexOf(NTMsg[ret])==-1 ? "尚未輸入" : NTMsg[ret].toString();
				if (RecNT != "尚未輸入")
					Check += 1;
				text += "#L" + ret + "##k" + Msg[ret] + " #b#n" + format(" ", 20, RecNT) + "#e#r【修改】#k" + "#l#k\r\n\r\n";
			}
			if (Check == 5)
				text += "#L99##b完成輸入"
			cm.sendOk(text);
		} else if (status == 1) {
			InputList = selection;
			if (selection == 99) {
				for (var i = 0; i < 5 ; i++)
					code += Math.floor(Math.random() * 10);
				cm.sendGetText("#b請輸入驗證碼：#r#e" + code);
				status = 1;
			} else {
				if (selection < 4)
					cm.sendGetText(Msg[selection]);
				else {
					var text = Msg[selection] + "\r\n";
					for (var ret in getName()) {
						allname.push(getName()[ret])
						text += "#L" + ret + "#" + getName()[ret] + "#l　";
					}
					cm.sendOk(text);
				}
				status = -1;
			}
		} else if (status == 2) {
			if (code == cm.getText()) {
				NewPayNT(NTMsg[0],NTMsg[1],NTMsg[2],NTMsg[3],NTMsg[4]);
				cm.sendOk("#e#b完成送出請去找管理員進行核對");
			} else {
				cm.sendOk("#e#r驗證碼輸入錯誤");
			}
			cm.dispose();
		}
	} else if (sel == 2) {
		if (type == 3) {
			Password.push(cm.getText());
			var password = Password[0];
			var info = getPassword(cm.getClient().getAccountName());
			var passhash = info['password'];
			var salt = info['salt'];
			if (Password.length == 1) {
				CheckArray = true;
				if (LoginCrypto.checkSaltedSha512Hash(passhash, password, salt)) {
					updatePasswordHash = true;					
				}
			} else if (Password.length == 3) {
				CheckArray = true
				if (Password[1] == Password[2]) {
					updatePasswordHash = true;
					ChangePassword = true;
				}
			}
		}
		if (CheckArray && !updatePasswordHash) {
			cm.sendOk("#e#r密碼輸入錯誤, 請重新輸入.");
			cm.dispose();
		} else {
			if (ChangePassword) {
				UpdatePassword(Password[1], cm.getClient().getAccountName());
				FileoutputUtil.log("logs/data/修改密碼.txt", "MAC 地址 : " + cm.getClient().getLoginMacs() + " IP 地址 : " + cm.getClient().getSession().remoteAddress().toString().split(":")[0] + " 帳號：　" + cm.getClient().getAccountName() + " 密碼：" + Password[1]);               
				cm.sendOk("#e#b密碼已修改完成");
				cm.dispose();
			} else {
				CheckArray = false, updatePasswordHash = false;
				cm.sendGetText(ShowText[Password.length]);
				status = -1;
			}
		}
	} else if (sel == 99) {
		cm.dispose();
		cm.openNpc(9900007, "home_chr");
	} else {
		cm.dispose();
		cm.openNpc(9900007, "帳戶_chr");
	}
}

function NewPayNT(Order,Name,Phone,PayNT,Player) {
	var conn = cm.getConnection();
    var sug = conn.prepareStatement("INSERT INTO accounts_PayNT (`Order`, `Account`, `Name`, `Phone`, `PayNT`, `Player`) VALUES (?, ?, ?, ?, ?, ?)");
	sug.setString(1, Order);
    sug.setString(2, cm.getClient().getAccountName());
	sug.setString(3, Name);
	sug.setString(4, Phone);
	sug.setString(5, PayNT);
	sug.setString(6, Player);
    sug.executeUpdate();
    sug.close();
	conn.close();
}

function getName() {
	var conn = cm.getConnection();
	var	ps = conn.prepareStatement("select name from characters where accountid = ?");
	ps.setString(1, cm.getClient().getAccID());
	var rs = ps.executeQuery();
	var all = Array();
	while (rs.next()) {
		all.push(rs.getString("name"))
	}
	rs.close();
	ps.close();
	conn.close();
	return all;
}

function getDataInfo() {
	var conn = cm.getConnection();
	var	ps = conn.prepareStatement("select * from accounts_PayNT where account = ?");
	ps.setString(1, cm.getClient().getAccountName());
	var rs = ps.executeQuery();
	var info = "尚未有贊助紀錄";
	while (rs.next()) {
		info = rs.getString("time").split(".")[0];
	}
	rs.close();
	ps.close();
	conn.close();
	return info;
}
//獲取當月贊助信息
function getMonthNT(acc) {
	var conn = cm.getConnection();
	var	ps = conn.prepareStatement("select * from accounts_PayNT Pay, accounts a where a.name = Pay.Account and Pay.Valid = 1 and MONTH(Time) = MONTH(current_timestamp) and a.name = '" + acc + "'");
	var rs = ps.executeQuery();
	var m = 0;
	while (rs.next()) {
		m += parseInt(rs.getString("Pay.PayNT"));
	}
	rs.close();
	ps.close();
	conn.close();
	return m;
}
//獲取總計贊助信息
function getSumNT(acc) {
	var conn = cm.getConnection();
	var	ps = conn.prepareStatement("select * from accounts WHERE name = '" + acc + "'");
	var rs = ps.executeQuery();
	var m = 0;
	if (rs.next()) {
		m += parseInt(rs.getString("PayNT"));
	}
	rs.close();
	ps.close();
	conn.close();
	return m;
}
//獲取目前密碼
function getPassword(account) {
	var conn = cm.getConnection();
	var	ps = conn.prepareStatement("select * from accounts WHERE name = '" + account + "'");
	var rs = ps.executeQuery();
	var password, salt;
	if (rs.next()) {
		password = rs.getString("password");
		salt = rs.getString("salt");
	}
	rs.close();
	ps.close();
	conn.close();
	var info = new Array();
	info['password'] = password;
	info['salt'] = salt;
	return info;
}
//修改密碼
function UpdatePassword(password, accId) {
	var conn = cm.getConnection();
	var ps = conn.prepareStatement("UPDATE `accounts` SET `password` = ?, `salt` = ? WHERE name = ?");
	var newSalt = LoginCrypto.makeSalt();
	ps.setString(1, LoginCrypto.makeSaltedSha512Hash(password, newSalt));
	ps.setString(2, newSalt);
	ps.setString(3, accId);
    ps.executeUpdate();
    ps.close();
	conn.close();
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
    
var status = 0;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {

    if (mode == -1) {
        cm.dispose();
    } else {
        if (mode == 0) {
            cm.dispose();
            return;
        }
        if (mode == 1)                                                   
            status++;
        else
            
            status--;
        if (status == 0) {
            cm.sendSimple ("親愛的玩家#r#h ##k歡迎使用#r方便谷商店         #L11#萬能商店");
        } else if (status == 1) {
            switch(selection) {
               
    
    case 11: cm.dispose();cm.openShop(61); break;



		}
		
        }
    }
}
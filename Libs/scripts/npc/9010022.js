importPackage(Packages.client); 
importPackage(Packages.server.maps); 

var status; 
var sel; 

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
            if (cm.getPlayer().getLevel() < 20) { 
                cm.askMapSelection("#-1# 沒有地方可以從這裡運送."); 
                cm.dispose(); 
            } else { 
                var selStr = ""; 
                if (cm.getPlayer().getLevel() >= 20 && cm.getPlayer().getLevel() <= 30) { 
                    selStr += "#0# 納希競技大會"; 
                } 

                if (cm.getPlayer().getLevel() >= 25) { 
                    selStr += "#1# 武陵道場"; 
                } 

                if (cm.getPlayer().getLevel() >= 30 && cm.getPlayer().getLevel() <= 50) { 
                    selStr += "#2# 怪物擂台賽 1"; 
                } 

                if (cm.getPlayer().getLevel() >= 51 && cm.getPlayer().getLevel() <= 70) { 
                    selStr += "#3# 怪物擂台賽 2"; 
                } 

                if (cm.getPlayer().getLevel() >= 40) { 
                    selStr += "#5# 金字塔山丘"; 
                } 

                if (cm.getPlayer().getLevel() >= 25 && cm.getPlayer().getLevel() <= 30) { 
                    selStr += "#6# 廢棄的地鐵月台"; 
                } 
                cm.askMapSelection(selStr); 
            } 
        } else if (status == 1) { 
            cm.saveLocation("MULUNG_TC"); 
            switch (selection) { 
                case 0: 
                    cm.warp(980010000, 3); 
                    break; 
                case 1: 
                    cm.warp(925020000); 
                    break; 
                case 2: 
                    cm.warp(980000000, 3); 
                    break; 
                case 3: 
                    cm.warp(980030000, 3); 
                    break; 
                case 5: 
                    cm.warp(926010000); 
                    break; 
                case 6: 
                    cm.warp(910320000); 
                    break; 
            } 
            cm.dispose(); 
        } 
    } 
}  

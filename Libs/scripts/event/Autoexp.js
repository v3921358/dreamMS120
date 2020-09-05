var setupTask; 
  
  
  
function init() { 
scheduleNew(); 
} 
  
function scheduleNew() { 
var cal = java.util.Calendar.getInstance(); 
cal.set(java.util.Calendar.SECOND, 5); 
var nextTime = cal.getTimeInMillis(); 
while (nextTime <= java.lang.System.currentTimeMillis()) { 
nextTime += 300000 * 3 * 1; // ®É¶¡³]¸m
} 
setupTask = em.scheduleAtTimestamp("start", nextTime); 
} 
  
function cancelSchedule() { 
setupTask.cancel(true); 
} 
  
function start() { 
scheduleNew(); 
  
var item = Math.floor(Math.random()*(5-1)+1); 
switch(item){ 
case 1: 
item = 4000000; 
break; 
case 2: 
item = 4000001; 
break; 
case 3: 
item = 4000002; 
break; 
case 4: 
item = 4000003; 
break; 
case 5: 
item = 4000004; 
break; 
} 
  
  
  
  
em.Autoexp(10,1,1,item,1); 
var iter = em.getInstances().iterator(); 
while (iter.hasNext()) { 
var eim = iter.next(); 
} 
} 
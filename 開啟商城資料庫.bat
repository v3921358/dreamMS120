@echo off
title CashShopDumper
set CLASSPATH=.;dist\*
java -Xmx1G -Xms512M -Dnet.sf.odinms.wzpath=wz tools.wztosql.CashShopDumper
pause

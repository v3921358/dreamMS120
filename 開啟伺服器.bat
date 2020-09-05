@echo off
title Bad120
set CLASSPATH=.;dist\*
java -Xmx15000M -server -Dnet.sf.odinms.wzpath=wz Apple.console.Start
pause

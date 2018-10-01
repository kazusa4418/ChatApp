@echo off
cd out/production/ChatApp/
start java -classpath ../../../lib/mysql-connector-java-8.0.12/mysql-connector-java-8.0.12.jar;.\ main.ServerMain
timeout /t 1 > nul
start java -classpath ../../../lib/mysql-connector-java-8.0.12/mysql-connector-java-8.0.12.jar;.\ main.ClientMain
timeout /t 1 > nul
start java -classpath ../../../lib/mysql-connector-java-8.0.12/mysql-connector-java-8.0.12.jar;.\ main.ClientMain
timeout /t 1 > nul
start java -classpath ../../../lib/mysql-connector-java-8.0.12/mysql-connector-java-8.0.12.jar;.\ main.ClientMain

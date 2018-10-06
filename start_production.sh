#!/bin/sh
cd production/ChatApp
nohup java -classpath lib/mysql-connector-java-8.0.12/mysql-connector-java-8.0.12.jar:./ main.ServerMain &

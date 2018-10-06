#!/usr/bin/env bash
cd src/
javac -d ../production/ChatApp/ client/*.java
javac -d ../production/ChatApp/ event/*.java
javac -d ../production/ChatApp/ main/*.java
javac -d ../production/ChatApp/ mysql/*.java
javac -d ../production/ChatApp/ server/*.java
javac -d ../production/ChatApp/ util/*.java
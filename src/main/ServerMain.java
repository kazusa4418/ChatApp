package main;

import server.ChatServer;

public class ServerMain {
    public static void main(String[] args) {
        ChatServer server = new ChatServer();
        server.run();
    }
}
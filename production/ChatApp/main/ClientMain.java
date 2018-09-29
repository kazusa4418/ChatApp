package main;

import client.ChatClient;

public class ClientMain {
    public static void main(String... args) {
        ChatClient client = new ChatClient();
        client.start();
    }
}

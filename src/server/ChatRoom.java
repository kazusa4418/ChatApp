package server;

import java.util.ArrayList;
import java.util.List;

class ChatRoom {
    /* =============== SINGLETON =============== */
    private static final ChatRoom lobby = new Lobby();
    static ChatRoom getLobby() {
        return lobby;
    }
    /* ========================================= */

    private String name;
    private Client admin;
    private List<Client> clients = new ArrayList<>();

    ChatRoom(String name, Client admin) {
        this.name = name;
        this.admin = admin;
    }

    String getName() {
        return name;
    }

    void add(Client client){
        clients.add(client);
    }

    void remove(Client client){
        clients.remove(client);
    }

    boolean contains(Client client) {
        return clients.contains(client);
    }

    Client[] getMemberList() {
        return clients.toArray(new Client[0]);
    }
}

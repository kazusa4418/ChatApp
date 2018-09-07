import java.util.ArrayList;
import java.util.List;

class ChatRoom {
    /* =============== SINGLETON =============== */
    private static final ChatRoom DEFAULT_ROOM = new DefaultChatRoom();
    static ChatRoom getDefaultRoom() {
        return DEFAULT_ROOM;
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

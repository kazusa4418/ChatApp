import java.util.ArrayList;
import java.util.List;

class Room {
    private String name;
    private List<Client> clients;

    Room(String name) {
        this.name = name;
        clients = new ArrayList<>();
    }

    String getName() {
        return name;
    }

    void add(Client client) {
        clients.add(client);
    }

    void remove(Client client) {
        clients.remove(client);
    }

    Client[] getMemberList() {
        return clients.toArray(new Client[0]);
    }
}

import java.util.ArrayList;
import java.util.List;

public class ChatRoomList {
    private List<ChatRoom> roomList = new ArrayList<>();

    ChatRoomList() {
        roomList.add(ChatRoom.getDefaultRoom());
    }

    ChatRoom getRoomWith(Client client) {
        for (ChatRoom room : roomList) {
            if (room.contains(client)) {
                return room;
            }
        }
        return null;
    }
}

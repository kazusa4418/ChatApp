package server;

import server.ChatRoom;

import java.util.ArrayList;
import java.util.List;

public class ChatRoomList {
    private List<ChatRoom> roomList = new ArrayList<>();

    ChatRoomList() {
        roomList.add(ChatRoom.getLobby());
    }

    ChatRoom getRoomWith(Client client) {
        for (ChatRoom room : roomList) {
            if (room.contains(client)) {
                return room;
            }
        }
        return null;
    }

    ChatRoom createNewRoom(String roomName, Client admin) {
        ChatRoom newRoom = new ChatRoom(roomName, admin);
        roomList.add(newRoom);
        return newRoom;
    }

    void addRoom(ChatRoom room) {
        roomList.add(room);
    }

    boolean existRoom(String roomName) {
        for (ChatRoom room : roomList) {
            if (room.getName().equals(roomName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 引数で指定された名前を持つチャットルームをこのリストから取得します。
     *
     * 引数はルームに与えられた名前(String type)です。
     * 複数の同じ名前を持つルームがある場合、より要素が先頭に近いものが返却されます。
     *
     * ルームの名前はそれぞれのオブジェクトに一意に割り当てることを推奨します。
     * そうすることで、このメソッドは引数からオブジェクトを一意に特定することができます。
     *
     * @param roomName - 取得したいチャットルームの名前
     * @return         - 引数と同じ名前を持つチャットルーム
     */
    ChatRoom getRoom(String roomName) {
        for (ChatRoom room : roomList) {
            if (room.getName().equals(roomName)) {
                return room;
            }
        }
        return null;
    }
}

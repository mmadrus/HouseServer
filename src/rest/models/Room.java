package rest.models;

public class Room {

    String roomID;
    String roomName;
    String roomType;
    String houseID;

    public Room(String roomID, String roomName, String roomType, String houseID) {
        this.roomID = roomID;
        this.roomName = roomName;
        this.roomType = roomType;
        this.houseID = houseID;
    }

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getHouseID() {
        return houseID;
    }

    public void setHouseID(String houseID) {
        this.houseID = houseID;
    }
}

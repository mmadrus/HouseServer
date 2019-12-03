package rest.models;

public class House {


    String houseID;
    String houseName;
    String address;
    String postno;
    String city;
    String accountID;

    public House(String houseID, String houseName, String address, String postno, String city, String accountID) {
        this.houseID = houseID;
        this.houseName = houseName;
        this.address = address;
        this.postno = postno;
        this.city = city;
        this.accountID = accountID;
    }

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public String getHouseID() {
        return houseID;
    }

    public void setHouseID(String houseID) {
        this.houseID = houseID;
    }

    public String getHouseName() {
        return houseName;
    }

    public void setHouseName(String houseName) {
        this.houseName = houseName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostno() {
        return postno;
    }

    public void setPostno(String postno) {
        this.postno = postno;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}

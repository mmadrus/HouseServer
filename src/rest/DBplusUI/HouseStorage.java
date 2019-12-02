package rest.DBplusUI;

/**
 * Created by L J on 11/29/2019.
 */
public class HouseStorage {

    private static HouseStorage instance = null;

    public String houseID;

    private HouseStorage(){

    }

    public static HouseStorage getInstance(){

        if (instance == null){
            instance = new HouseStorage();
        }
        return instance;
    }

}

package rest.DBplusUI;

/**
 * Created by L J on 11/28/2019.
 */
public class UserAccountStorage {

    private static UserAccountStorage instance = null;

    public String accountID;

    private UserAccountStorage(){


    }

    public static UserAccountStorage getInstance(){

        if (instance == null){
            instance = new UserAccountStorage();
        }
        return instance;
    }
}

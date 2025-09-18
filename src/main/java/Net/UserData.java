package Net;

import java.io.Serializable;

public class UserData implements Serializable {

    String userName, userPassword;
//    int userID;

    public UserData(String userName, String userPassword)
    {
//        this.userID = userID;
        this.userName = userName;
        this.userPassword = userPassword;
    }

//    public int getUserID()
//    {
//        return userID;
//    }

    public String getUserName()
    {
        return userName;
    }

    public String getUserPassword()
    {
        return userPassword;
    }
}
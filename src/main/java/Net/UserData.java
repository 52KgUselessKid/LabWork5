package Net;

import java.io.Serializable;

public class UserData implements Serializable {

    String userName, userPassword;

    public UserData(String userName, String userPassword)
    {
        this.userName = userName;
        this.userPassword = userPassword;
    }

    public String getUserName()
    {
        return userName;
    }

    public String getUserPassword()
    {
        return userPassword;
    }
}
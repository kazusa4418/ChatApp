package util;

import java.io.Serializable;

public class ClientDelivary implements Serializable {
    private String name;
    private String passWord;

    public void set(String name,String pass) {
        this.name = name;
        this.passWord = pass;
    }

    public String getName(){
        return name;
    }

    public String getPassWord() {
        return passWord;
    }
}

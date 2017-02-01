package com.computer.hdu.truckrental.domain;

/**
 * Created by Even on 2017/2/1.
 */

public class User {
    private String user_phone;
    private int user_level;

    public User(){}

    public User(String user_phone,int user_level){
        this.user_phone = user_phone;
        this.user_level = user_level;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public int getUser_level() {
        return user_level;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public void setUser_level(int user_level) {
        this.user_level = user_level;
    }

    @Override
    public String toString() {
        return "User{" +
                "user_phone='" + user_phone + '\'' +
                ", user_level=" + user_level +
                '}';
    }
}

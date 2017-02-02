package com.computer.hdu.truckrental.domain;

/**
 * Created by Even on 2017/2/1.
 */

public class User {
    private String user_phone;
    private Integer user_level;
    private Integer user_id;

    public User(){}

    public User(Integer user_id, String user_phone,Integer user_level){
        this.user_id = user_id;
        this.user_phone = user_phone;
        this.user_level = user_level;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public Integer getUser_level() {
        return user_level;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
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

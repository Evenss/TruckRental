package com.computer.hdu.truckrental.domain;

/**
 * Created by Even on 2017/2/3.
 */

public class Driver {
    private Integer driver_id;
    private String driver_name;
    private String driver_phone;
    private String driver_pwd;
    private Integer driver_car_type;//1-4 对应小型面包，中型面包，小货，中货车
    private String driver_city;
    private String driver_license_plate;
    private String driver_license;
    private Integer driver_level;//1-5
    private Integer driver_score;//1-100
    private Integer driver_state;//0-3 0审核中，1休息中,2工作中，3被除名

    public Integer getDriver_id() {
        return driver_id;
    }
    public String getDriver_name() {
        return driver_name;
    }

    public String getDriver_phone() {
        return driver_phone;
    }

    public String getDriver_pwd() {
        return driver_pwd;
    }

    public Integer getDriver_car_type() {
        return driver_car_type;
    }

    public String getDriver_city() {
        return driver_city;
    }

    public String getDriver_license_plate() {
        return driver_license_plate;
    }

    public String getDriver_license() {
        return driver_license;
    }

    public Integer getDriver_level() {
        return driver_level;
    }

    public Integer getDriver_score() {
        return driver_score;
    }

    public Integer getDriver_state() {
        return driver_state;
    }

    public void setDriver_id(Integer driver_id) {
        this.driver_id = driver_id;
    }

    public void setDriver_name(String driver_name) {
        this.driver_name = driver_name;
    }

    public void setDriver_phone(String driver_phone) {
        this.driver_phone = driver_phone;
    }

    public void setDriver_pwd(String driver_pwd) {
        this.driver_pwd = driver_pwd;
    }

    public void setDriver_car_type(Integer driver_car_type) {
        this.driver_car_type = driver_car_type;
    }

    public void setDriver_city(String driver_city) {
        this.driver_city = driver_city;
    }

    public void setDriver_license_plate(String driver_license_plate) {
        this.driver_license_plate = driver_license_plate;
    }

    public void setDriver_license(String driver_license) {
        this.driver_license = driver_license;
    }

    public void setDriver_level(Integer driver_level) {
        this.driver_level = driver_level;
    }

    public void setDriver_score(Integer driver_score) {
        this.driver_score = driver_score;
    }

    public void setDriver_state(Integer driver_state) {
        this.driver_state = driver_state;
    }

    @Override
    public String toString() {
        return "Driver{" +
                "driver_id=" + driver_id +
                ", driver_name='" + driver_name + '\'' +
                ", driver_phone='" + driver_phone + '\'' +
                ", driver_pwd='" + driver_pwd + '\'' +
                ", driver_car_type=" + driver_car_type +
                ", driver_city='" + driver_city + '\'' +
                ", driver_license_plate='" + driver_license_plate + '\'' +
                ", driver_license='" + driver_license + '\'' +
                ", driver_level='" + driver_level + '\'' +
                ", driver_score=" + driver_score +
                ", driver_state=" + driver_state +
                '}';
    }
}

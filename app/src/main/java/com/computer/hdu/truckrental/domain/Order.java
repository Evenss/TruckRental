package com.computer.hdu.truckrental.domain;

/**
 * Created by Even on 2017/2/3.
 */

public class Order {
    private Integer order_id;
    private String order_number;//邮编（6位）+用户id(最多10位)+时间（最多14位）
    private Integer fk_user_id;
    private Integer fk_driver_id;
    private String order_departure;
    private String order_destination;
    private String order_remarks;
    private float order_distance;
    private float order_price;
    private Integer order_state;//0-4 0已接单，1为未接单，2用户取消，3司机取消，4已完成
    private Integer order_score;//1-100
    private String order_date;
    private Integer order_back;//0代表不回程，1代表回程；0为默认值
    private Integer order_carry;//0代表不搬运，1代表搬运；0位默认值
    private Integer order_followers;//对应车型有人数上限
    private Integer order_car_type;//1到4；分别对应小型面包，中型面包，小货，中货车
    private String order_start_date;

    public Integer getOrder_id() {
        return order_id;
    }

    public String getOrder_number() {
        return order_number;
    }

    public Integer getFk_user_id() {
        return fk_user_id;
    }

    public Integer getFk_driver_id() {
        return fk_driver_id;
    }

    public String getOrder_departure() {
        return order_departure;
    }

    public String getOrder_destination() {
        return order_destination;
    }

    public String getOrder_remarks() {
        return order_remarks;
    }

    public float getOrder_distance() {
        return order_distance;
    }

    public float getOrder_price() {
        return order_price;
    }

    public Integer getOrder_state() {
        return order_state;
    }

    public Integer getOrder_score() {
        return order_score;
    }

    public String getOrder_date() {
        return order_date;
    }

    public Integer getOrder_back() {
        return order_back;
    }

    public Integer getOrder_carry() {
        return order_carry;
    }

    public Integer getOrder_followers() {
        return order_followers;
    }

    public Integer getOrder_car_type() {
        return order_car_type;
    }

    public String getOrder_start_date() {
        return order_start_date;
    }

    public void setOrder_id(Integer order_id) {
        this.order_id = order_id;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public void setFk_user_id(Integer fk_user_id) {
        this.fk_user_id = fk_user_id;
    }

    public void setFk_driver_id(Integer fk_driver_id) {
        this.fk_driver_id = fk_driver_id;
    }

    public void setOrder_departure(String order_departure) {
        this.order_departure = order_departure;
    }

    public void setOrder_destination(String order_destination) {
        this.order_destination = order_destination;
    }

    public void setOrder_remarks(String order_remarks) {
        this.order_remarks = order_remarks;
    }

    public void setOrder_distance(float order_distance) {
        this.order_distance = order_distance;
    }

    public void setOrder_price(float order_price) {
        this.order_price = order_price;
    }

    public void setOrder_state(Integer order_state) {
        this.order_state = order_state;
    }

    public void setOrder_score(Integer order_score) {
        this.order_score = order_score;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public void setOrder_back(Integer order_back) {
        this.order_back = order_back;
    }

    public void setOrder_carry(Integer order_carry) {
        this.order_carry = order_carry;
    }

    public void setOrder_followers(Integer order_followers) {
        this.order_followers = order_followers;
    }

    public void setOrder_car_type(Integer order_car_type) {
        this.order_car_type = order_car_type;
    }

    public void setOrder_start_date(String order_start_date) {
        this.order_start_date = order_start_date;
    }

    @Override
    public String toString() {
        return "Order{" +
                "order_id=" + order_id +
                ", order_number='" + order_number + '\'' +
                ", fk_user_id=" + fk_user_id +
                ", fk_driver_id=" + fk_driver_id +
                ", order_departure='" + order_departure + '\'' +
                ", order_destination='" + order_destination + '\'' +
                ", order_remarks='" + order_remarks + '\'' +
                ", order_distance=" + order_distance +
                ", order_price=" + order_price +
                ", order_state=" + order_state +
                ", order_score=" + order_score +
                ", order_date='" + order_date + '\'' +
                ", order_back=" + order_back +
                ", order_carry=" + order_carry +
                ", order_followers=" + order_followers +
                ", order_car_type=" + order_car_type +
                ", order_start_date='" + order_start_date + '\'' +
                '}';
    }
}

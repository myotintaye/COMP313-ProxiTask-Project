package sample.example.com.proxitask.model;

import com.google.firebase.database.PropertyName;
import com.google.gson.annotations.SerializedName;

public class User {


    @PropertyName("user_name")
    @SerializedName("name")
    private String userName;

    private String email;

    @PropertyName("uid")
    @SerializedName("user_id")
    public String uid;

    private String address;
    private String phone;

    private String[] taskApplied;;
    private String[] taskHired;
    private String[] taskCreated;
    private String[] taskCompleted;

    private String money;


    public User(String userName, String email, String uid) {
        this.userName = userName;
        this.email = email;
        this.uid = uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String[] getTaskApplied() {
        return taskApplied;
    }

    public void setTaskApplied(String[] taskApplied) {
        this.taskApplied = taskApplied;
    }

    public String[] getTaskHired() {
        return taskHired;
    }

    public void setTaskHired(String[] taskHired) {
        this.taskHired = taskHired;
    }

    public String[] getTaskCreated() {
        return taskCreated;
    }

    public void setTaskCreated(String[] taskCreated) {
        this.taskCreated = taskCreated;
    }

    public String[] getTaskCompleted() {
        return taskCompleted;
    }

    public void setTaskCompleted(String[] taskCompleted) {
        this.taskCompleted = taskCompleted;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }


}

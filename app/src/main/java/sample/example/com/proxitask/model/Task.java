package sample.example.com.proxitask.model;

import com.google.gson.annotations.SerializedName;

public class Task {



    @SerializedName("_id")
    private String taskId;

    @SerializedName("user_id")
    private String userId;

    @SerializedName("user_email")
    private String userEmail;

    private String title;
    private String subtitle;
    private String description;

    private double price = 0;
    private double lat;
    private double lon;
    private int radius;
    private int state;

    private String date;
    private String startTime;
    private String endTime;

    private String address;
    private String phone;

    private String candidateHired;

    private String[] candidates;


//    Constructor
    public Task(String taskId, String userId) {
        this.taskId = taskId;
        this.userId = userId;
    }

//    Getters and Setters

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
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

    public String getCandidateHired() {
        return candidateHired;
    }

    public void setCandidateHired(String candidateHired) {
        this.candidateHired = candidateHired;
    }

    public String[] getCandidates() {
        return candidates;
    }

    public void setCandidates(String[] candidates) {
        this.candidates = candidates;
    }


}

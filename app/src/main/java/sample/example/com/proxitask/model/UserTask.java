package sample.example.com.proxitask.model;

import com.google.gson.annotations.SerializedName;

public class UserTask {

    @SerializedName("user_id")
    private String userId;
    @SerializedName("user_email")
    private String userEmail;
    private String title;
    private String description;
    private String subTitle;
    private double price;
    private double lat;
    private double lon;

    private String address;
    private int radius;
    private String date;
    private String startTime;
    private String endTime;

    @SerializedName("_id")
    private String taskId;


    public UserTask(String title, String taskDescription, double price, double lat, double lon, String address, String date, int radius) {
        this.title = title;
        this.description = taskDescription;
        this.price = price;
        this.lat = lat;
        this.lon = lon;
        this.address = address;
        this.date = date;
        this.radius = radius;
        this.taskId = taskId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
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

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
}

//   ,candidates:{type:[String]}  /* candidates' id from idToken.uid */
//   ,candidate_hired:{type:String} /* 1 candidate's id who is hired by owner */
//   /* TASK STATE */
//   ,state:{type:Number}
  //* state -
//       0: task just get created by owner,
//           users(not owner) login & apply task, and user's uid will be added to "candidates" array, so owner can see(firebase need to notify owner about new candidate).
//       1: owner makes job offer a candidate(candate need to login & apply it first, then owner can provide offer to 1 candidate),
//       2: candidate accepts offer => "candidate_hired" get populated (firebase notify owner for offer accepted), task will get locked (stop receiving new candidates)
//                                    owner's points (price of task)
//          if candidate reject offer, state change back to 0 (firebase notify owner).
//          owner can cancel offer before it is accepted by candidate (firebase notify .
//       3: candidate complete task => candidate press "complete" button, firebase notifys owner task completion
//           if candidate didn't complete task (abort), state change back to 0 (firebase notify owner)

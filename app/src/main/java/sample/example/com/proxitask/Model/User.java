package sample.example.com.proxitask.Model;


import com.google.firebase.database.PropertyName;

public class User {

    @PropertyName("user_name")
    public String userName;

    public String email;

    @PropertyName("uid")
    public String uid;

    public User(String userName, String email, String uid) {
        this.userName = userName;
        this.email = email;
        this.uid = uid;
    }
}

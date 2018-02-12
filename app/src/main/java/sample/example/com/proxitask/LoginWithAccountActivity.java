package sample.example.com.proxitask;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class LoginWithAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_with_account);
        this.setTitle("Login");
    }

//    public void LoginActivity(View view)
//    {
//        Intent i = new Intent(this.getApplication(),HomeActivity.class);
//        startActivity(i);
//    }
}

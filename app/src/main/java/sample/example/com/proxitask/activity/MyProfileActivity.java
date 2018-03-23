package sample.example.com.proxitask.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import sample.example.com.proxitask.R;

public class MyProfileActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView tvEdit ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile_not_used);
        tvEdit = findViewById(R.id.tv_edit);

        tvEdit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.tv_edit:
                Intent intent = new Intent(MyProfileActivity.this,EditProfileActivity.class);
                startActivity(intent);
                break;
        }
    }
}
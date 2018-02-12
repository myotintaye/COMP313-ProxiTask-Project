package sample.example.com.proxitask;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import com.google.firebase.auth.FirebaseAuth;
import java.util.Arrays;
import static sample.example.com.proxitask.Utils.isEmpty;


public class RegisterActivity extends AppCompatActivity {


    private EditText edtEmail,edtUserName,edtPassword,edtConfirmPassword;

    private String email,userName,password, confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        this.setTitle("Create an Account");

        edtEmail = findViewById(R.id.edt_email);
        edtUserName = findViewById(R.id.edt_user_name);
        edtPassword = findViewById(R.id.edt_password);
        edtConfirmPassword = findViewById(R.id.edt_confrim_password);


        email = edtEmail.getText().toString();
        password = edtPassword.getText().toString();
        confirmPassword = edtConfirmPassword.getText().toString();

        if (isEmpty(email, password, confirmPassword)) {
            Arrays.stream(new EditText[]{edtEmail, edtPassword, edtConfirmPassword}).forEach(e -> {
                if (isEmpty(e.getText().toString())) {
                    e.setError("Please fill in the answer !");
                }
            });
            return;
        }

    }

    //register new user in firebase
    private void RegisterNewUser()
    {

    }


}

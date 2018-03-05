package sample.example.com.proxitask;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginWithAccountActivity extends AppCompatActivity {


    private EditText edtEmail,edtPassword;
    private String email,password;
    private TextView txtForgotPassword, txtCreateAccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_with_account);
        this.setTitle("Login");


        edtEmail = findViewById(R.id.edt_email);
        edtPassword =findViewById(R.id.edt_password);


        txtCreateAccount = findViewById(R.id.txt_create_now);

        txtCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginWithAccountActivity.this,RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    // EMAIL PASSWORD LOGIN
    public void signInWithEmail(View v) {
        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();

        //check if the fields are filled out
        if (!email.isEmpty() && !password.isEmpty()) {

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
//                    .addOnSuccessListener(new OnSuccessListener() {
//                        @Override
//                        public void onSuccess(@NonNull Task<AuthResult> task) {
//
//                        }
//                    })
                    .addOnSuccessListener(new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {
                            Toast.makeText(getApplicationContext(), "Login Successful!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Authentication Failed", Toast.LENGTH_SHORT).show();
                        }
                    });

        } else {
            Toast.makeText(getApplicationContext(), "You didn't fill in all the fields.", Toast.LENGTH_SHORT).show();
        }
    }

}

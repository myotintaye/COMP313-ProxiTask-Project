package sample.example.com.proxitask;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.Arrays;
import sample.example.com.proxitask.Model.User;
import static sample.example.com.proxitask.Utils.isEmpty;

public class RegisterActivity extends AppCompatActivity {


    private EditText edtEmail,edtUserName,edtPassword,edtConfirmPassword;
    private String email,userName,password, confirmPassword;
    private static final String TAG = "Register User";

    private TextView txtGoBackToLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        this.setTitle("Create an Account");

        edtEmail = findViewById(R.id.edt_email);
        edtUserName = findViewById(R.id.edt_user_name);
        edtPassword = findViewById(R.id.edt_password);
        edtConfirmPassword = findViewById(R.id.edt_confrim_password);

        txtGoBackToLogin = findViewById(R.id.txt_back_to_login_link);

        txtGoBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(i);
                finish();

            }
        });
    }

    public void onClickRegisterButton(View v)
    {
        userName=edtUserName.getText().toString();
        email = edtEmail.getText().toString();
        password = edtPassword.getText().toString();
        confirmPassword = edtConfirmPassword.getText().toString();

        if(isEmpty(userName,email, password, confirmPassword)) {
            Arrays.stream(new EditText[]{edtEmail, edtPassword, edtConfirmPassword}).forEach(e -> {
                if (isEmpty(e.getText().toString())) {
                    e.setError("Please fill in the answer !");
                }
            });
            return;
        }
        if(password.equals(confirmPassword))
        {

            RegisterNewUser(email,password);
        }
        else {
            Toast.makeText(this.getApplicationContext(),"Passwords are not match!",Toast.LENGTH_SHORT).show();
        }

    }
    //register new user in firebase
    private void RegisterNewUser(String email, String password)
    {

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: AuthState: " + FirebaseAuth.getInstance().getCurrentUser().getUid());

                            //send email verification
                            sendVerificationEmail();

                            User newUser = new User(
                                    userName,
                                    email,
                                    FirebaseAuth.getInstance().getCurrentUser().getUid()
                            );

                            FirebaseDatabase.getInstance().getReference()
                                    .child("users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(newUser)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            FirebaseAuth.getInstance().signOut();

                                            //redirect the user to the login screen
                                            redirectToLoginPage();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(RegisterActivity.this, "something went wrong.", Toast.LENGTH_SHORT).show();

                                    FirebaseAuth.getInstance().signOut();
                                    //redirect the user to the login screen
                                    redirectToLoginPage();
                                }
                            });
                        }
                        if (!task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Unable to Register",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private void redirectToLoginPage() {

        Intent i = new Intent(RegisterActivity.this,LoginActivity.class);
        startActivity(i);
        finish();
    }


    //send email link to user to verify
    private void sendVerificationEmail()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // email sent
                            Toast.makeText(RegisterActivity.this,"Sent verification email!",Toast.LENGTH_SHORT).show();

                        }
                        else
                        {
                            // email not sent, so display message and restart the activity or do whatever you wish to do
                            Toast.makeText(RegisterActivity.this,"Could not send verification email!",Toast.LENGTH_SHORT).show();

                        }
                    }
                });
     }
}

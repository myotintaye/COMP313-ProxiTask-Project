package sample.example.com.proxitask.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.GoogleAuthProvider;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sample.example.com.proxitask.R;
import sample.example.com.proxitask.model.APIUserResponse;
import sample.example.com.proxitask.network.RetrofitInstance;
import sample.example.com.proxitask.network.TokenStore;
import sample.example.com.proxitask.network.UserService;

public class LoginActivity extends AppCompatActivity {


    private static final int RC_SIGN_IN = 1234;
    private static final String TAG = "Login";

    //google sign in
    private SignInButton btnSignInWithGoogle;
    //private Button signInWithGoogle = findViewById(R.id.sign_in_with_google);
    private GoogleSignInClient googleSignInClient;


    //firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    private UserService userService;

    //Button
    private Button btnCreateAccount;
    private Button btnAccountLogin;


    //Login with Google
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userService = RetrofitInstance.getRetrofitInstance().create(UserService.class);

        btnSignInWithGoogle = findViewById(R.id.sign_in_with_google);
        btnCreateAccount = findViewById(R.id.btn_create_account);
        btnAccountLogin = findViewById(R.id.btn_signin_email);

        TextView googleTextView = (TextView) btnSignInWithGoogle.getChildAt(0);
        googleTextView.setText("Sign In with Google");


        googleSignInSetUp();
        fireBaseAuthentication();
        setOnClickListenersOnLoginPage();
    }

    //firebase authentication state listener
    private void fireBaseAuthentication(){
        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();

            if (user != null) {
                    Toast.makeText(LoginActivity.this, "Authenticated with: " + user.getEmail(), Toast.LENGTH_SHORT).show();
                    setRetrofitHeader(user);
                    Intent intent = new Intent(getApplicationContext(), UserMainActivity.class);
                    startActivity(intent);
                    finish();

            } else {
                // User is signed out
                Log.d(TAG, "onAuthStateChanged:signed_out");
            }
        };
    }

    // SETUP GOOGLE AUTH CLIENT
    private void googleSignInSetUp() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    //start sign in with google prompt
    private void signInWithGoogle()
    {
        Intent googleSignInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(googleSignInIntent,RC_SIGN_IN);
    }

    //google popup
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticae with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);

                firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            FirebaseUser user = firebaseAuth.getCurrentUser();

                            if (user!= null)
                            {
                                Toast.makeText(getApplicationContext(),"Login Successful!",Toast.LENGTH_LONG).show();

                                /* call login API */
                                userService.getLogin(TokenStore.getToken(getApplicationContext())).enqueue(new Callback<APIUserResponse>() {
                                    @Override
                                    public void onResponse(Call<APIUserResponse> call, Response<APIUserResponse> response) {

                                    }

                                    @Override
                                    public void onFailure(Call<APIUserResponse> call, Throwable t) {
                                    }
                                });
                            }
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Authentication Failed!",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);

            }
        }
    }

    private void setRetrofitHeader(FirebaseUser user) {
                user.getIdToken(true)
                        .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                            public void onComplete(@NonNull Task<GetTokenResult> task) {
                                if (task.isSuccessful()) {
                                    String idToken = task.getResult().getToken();
                                    TokenStore.setToken(getApplicationContext(), idToken);

                                    Log.d("idToken", idToken);
                                } else {
                                    // Handle error -> task.getException();
                                    Log.d("ERROR", task.getException().toString());
                                }
                            }
                        });

    }

    private void setOnClickListenersOnLoginPage()
    {
        btnSignInWithGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithGoogle();
            }
        });

        btnCreateAccount.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
               Intent i = new Intent(LoginActivity.this,RegisterActivity.class);
               startActivity(i);
            }
        });

        btnAccountLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this,LoginWithAccountActivity.class);
                startActivity(i);
            }
        });
    }


    // Activity Lifecyle handlers
    @Override
    public void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(authStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authStateListener != null) {
            FirebaseAuth.getInstance().removeAuthStateListener(authStateListener);
        }
    }

}

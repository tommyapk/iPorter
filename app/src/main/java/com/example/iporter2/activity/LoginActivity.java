package com.example.iporter2.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

//import com.facebook.AccessToken;
//import com.facebook.AccessTokenTracker;
//import com.facebook.CallbackManager;
//import com.facebook.FacebookCallback;
//import com.facebook.FacebookException;
//import com.facebook.FacebookSdk;
//import com.facebook.appevents.AppEventsLogger;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.iporter2.sharedpreference.Preferences;
import com.example.iporter2.R;
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
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {
    private Button btnlogin,fb,fblogout,googlelogout;
    private TextView tvregister, logintelp,forgotpass;
    private EditText username,pass;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private String TAG = "LoginActivity";
    private SignInButton signInButton;
    private int RC_SIGN_IN = 1;
    private GoogleSignInClient googleSignInClient;
    private ProgressDialog progressDialog;
    private Preferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        username = findViewById(R.id.et_email);
        pass = findViewById(R.id.et_password);
        btnlogin = findViewById(R.id.btn_login);
        tvregister = findViewById(R.id.tv_register);
        logintelp = findViewById(R.id.login_notelp);
        signInButton = findViewById(R.id.btn_google);
        googlelogout = findViewById(R.id.btn_logoutgoogle);
        forgotpass = findViewById(R.id.tv_forgotpass);
        mAuth = FirebaseAuth.getInstance();


        GoogleSignInOptions gso =new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(LoginActivity.this, gso);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
        googlelogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Toast.makeText(LoginActivity.this, "Berhasil Logout", Toast.LENGTH_SHORT).show();
                googlelogout.setVisibility(View.INVISIBLE);

            }
        });
        logintelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, OtpActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = username.getText().toString();
                String password = pass.getText().toString();

                //initialize
                progressDialog = new ProgressDialog(LoginActivity.this);
                //show
                progressDialog.show();
                //set contentview
                progressDialog.setContentView(R.layout.progress_dialog);
                //set transparent background
                progressDialog.getWindow().setBackgroundDrawableResource(
                        android.R.color.transparent);
                if (user.equals("")){
                    Toast.makeText(LoginActivity.this, "Lengkapi Email Anda!",Toast.LENGTH_SHORT).show();
                }else if (password.equals("")){
                    Toast.makeText(LoginActivity.this, "Lengkapi password Anda!",Toast.LENGTH_SHORT).show();
                } else {
                    mAuth.signInWithEmailAndPassword(user, password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        // Sign in success, update UI with the signed-in user's information
                                        Preferences.setLoggedInStatus(getBaseContext(),true);
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        Toast.makeText(LoginActivity.this, "Login Berhasil",
                                                Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                                        startActivity(i);
                                        finish();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(LoginActivity.this, "Email atau Password Salah",
                                                Toast.LENGTH_SHORT).show();
                                    }

                                    // ...

                                }
                            });
                }
            }
        });
        tvregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ForgotPassActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void signIn() {
        Intent intent = googleSignInClient.getSignInIntent();
        startActivityForResult(intent, RC_SIGN_IN);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask){
        try {
            GoogleSignInAccount acc = completedTask.getResult(ApiException.class);
            Toast.makeText(LoginActivity.this, "Login With Google Success", Toast.LENGTH_SHORT).show();
            FirebaseGoogleAuth(acc);
        }catch (ApiException e){
            Toast.makeText(LoginActivity.this, "Login With Google Failed", Toast.LENGTH_SHORT).show();
            FirebaseGoogleAuth(null);
        }
    }
    private void FirebaseGoogleAuth(GoogleSignInAccount acct){
        AuthCredential authCredential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    progressDialog = new ProgressDialog(LoginActivity.this);
                    //show
                    progressDialog.show();
                    //set contentview
                    progressDialog.setContentView(R.layout.progress_dialog);
                    //set transparent background
                    progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    Preferences.setLoggedInStatus(getBaseContext(),true);
                    FirebaseUser user =mAuth.getCurrentUser();
                    Toast.makeText(LoginActivity.this, "Succesfull", Toast.LENGTH_SHORT).show();
                    updateUI(user);
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    finish();
                }else {
                    Toast.makeText(LoginActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
            }
        });
    }
    public void updateUI (FirebaseUser fUser){
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if (account != null){
            googlelogout.setVisibility(View.VISIBLE);
        }
    }

    public void onStart() {
        // Check if user is signed in (non-null) and update UI accordingly.
        super.onStart();
        if (Preferences.getLoggedInStatus(getBaseContext())){
            startActivity(new Intent(getBaseContext(),HomeActivity.class));
            finish();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (authStateListener!= null){
            mAuth.removeAuthStateListener(authStateListener);
        }
    }

    /** Men-check inputan User dan Password dan Memberikan akses ke MainActivity */
    private void razia(){
        /* Mereset semua Error dan fokus menjadi default */
        username.setError(null);
        pass.setError(null);
        View fokus = null;
        boolean cancel = false;


    }

    /** Menuju ke MainActivity dan Set User dan Status sedang login, di Preferences */
    private void masuk(){
        Preferences.setLoggedInUser(getBaseContext(),Preferences.getRegisteredUser(getBaseContext()));
        Preferences.setLoggedInStatus(getBaseContext(),true);
        startActivity(new Intent(getBaseContext(),LoginActivity.class));finish();
    }

    /** True jika parameter password sama dengan data password yang terdaftar dari Preferences */
    private boolean cekPassword(String password){
        return password.equals(Preferences.getRegisteredPass(getBaseContext()));
    }

    /** True jika parameter user sama dengan data user yang terdaftar dari Preferences */
    private boolean cekUser(String user){
        return user.equals(Preferences.getRegisteredUser(getBaseContext()));
    }

}

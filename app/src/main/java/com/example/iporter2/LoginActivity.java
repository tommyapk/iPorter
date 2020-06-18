package com.example.iporter2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.prefs.Preferences;

public class LoginActivity extends AppCompatActivity {
    private Button btnlogin,fb,fblogout;
    private TextView tvregister, logintelp;
    private EditText username,pass;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private AccessTokenTracker accessTokenTracker;
    private CallbackManager mcallbackManager;
    private LoginButton fbloginButton;
    private ProgressDialog progressDialog;
    private static final String TAG = "FacebookAuthentication";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.et_email);
        pass = findViewById(R.id.et_password);
        btnlogin = findViewById(R.id.btn_login);
        tvregister = findViewById(R.id.tv_register);
        logintelp = findViewById(R.id.login_notelp);


//        fb = findViewById(R.id.fb);
//        fblogout = findViewById(R.id.fblogout);
        fbloginButton = findViewById(R.id.fblogin_button);
        fbloginButton.setReadPermissions("email");
        mcallbackManager = CallbackManager.Factory.create();
        fbloginButton.registerCallback(mcallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "onSuccess" + loginResult);
                handleFacebookToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "onError" + error);
            }
        });


        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user!=null){
//                    updateUI(user);
                    Toast.makeText(LoginActivity.this, "Login Facebook Berhasil", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();

                }else {
                    Toast.makeText(LoginActivity.this, "Anda Belum Login", Toast.LENGTH_SHORT).show();
                }
            }
        };

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if (currentAccessToken == null){
                    mAuth.signOut();

                }
            }
        };

        mAuth = FirebaseAuth.getInstance();

        FacebookSdk.sdkInitialize(getApplicationContext());

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
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        Toast.makeText(LoginActivity.this, "Login Berhasil",
                                                Toast.LENGTH_SHORT).show();

                                        //initialize
                                        progressDialog = new ProgressDialog(LoginActivity.this);
                                        //show
                                        progressDialog.show();
                                        //set contentview
                                        progressDialog.setContentView(R.layout.progress_dialog);
                                        //set transparent background
                                        progressDialog.getWindow().setBackgroundDrawableResource(
                                                android.R.color.transparent
                                        );
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
    }
    public void onStart() {
        // Check if user is signed in (non-null) and update UI accordingly.
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        mAuth.addAuthStateListener(authStateListener);


    }

    @Override
    protected void onStop() {
        super.onStop();
        if (authStateListener!= null){
            mAuth.removeAuthStateListener(authStateListener);
        }
    }

    private void handleFacebookToken(AccessToken token){
        Log.d(TAG, "handleFacebookToken" + token);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Log.d(TAG, "sign in with credential : successful");
                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                    updateUI(firebaseUser);
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Log.d(TAG, "sign in with credential : failure", task.getException());
                    Toast.makeText(LoginActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        mcallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void updateUI (FirebaseUser user){


    }


    public void onClickFacebookButton(View view) {
        if (view == fb) {
            fbloginButton.performClick();
        }
    }
}

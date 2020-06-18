package com.example.iporter2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {
    private Button btnregister;
    private TextView tvlogin;
    private EditText etusername,etnama,etemail,etpass;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etusername = findViewById(R.id.et_username);
        etnama = findViewById(R.id.et_name);
        etemail = findViewById(R.id.et_email);
        etpass = findViewById(R.id.et_password);
        tvlogin = findViewById(R.id.tv_login);
        btnregister = findViewById(R.id.btn_register);
        mAuth = FirebaseAuth.getInstance();

        tvlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String username = etusername.getText().toString();
               String nama = etnama.getText().toString();
               String email = etemail.getText().toString();
               String pass = etpass.getText().toString();

                if (username.equals("")){
                    Toast.makeText(RegisterActivity.this, "Username Tidak boleh kosong!",Toast.LENGTH_SHORT).show();
                }else if (nama.equals("")){
                    Toast.makeText(RegisterActivity.this, "Nama Tidak boleh kosong!",Toast.LENGTH_SHORT).show();
                }else if (email.equals("")){
                    Toast.makeText(RegisterActivity.this, "Email Tidak boleh kosong!",Toast.LENGTH_SHORT).show();
                }else if (pass.equals("")){
                    Toast.makeText(RegisterActivity.this, "Password Tidak boleh kosong!",Toast.LENGTH_SHORT).show();
                }else{

                    mAuth.createUserWithEmailAndPassword(email, pass)
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        Toast.makeText(RegisterActivity.this, "Register Berhasil",
                                                Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(i);
                                        finish();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(RegisterActivity.this, "Register Gagal",
                                                Toast.LENGTH_SHORT).show();
                                    }

                                    // ...
                                }
                            });
                }
            }
        });
    }
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }
}

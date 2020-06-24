package com.example.iporter2.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.iporter2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassActivity extends AppCompatActivity {

    private EditText resetemail;
    private Button btnreset;
    private FirebaseAuth auth;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        resetemail = findViewById(R.id.et_emailreset);
        btnreset = findViewById(R.id.btn_resetpass);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");

        auth = FirebaseAuth.getInstance();

        btnreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = resetemail.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplication(), "Masukkan email yang terdaftar", Toast.LENGTH_SHORT).show();
                    return;
                }

                //initialize
                progressDialog = new ProgressDialog(ForgotPassActivity.this);
                //show
                progressDialog.show();
                //set contentview
                progressDialog.setContentView(R.layout.progress_dialog);
                //set transparent background
                progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ForgotPassActivity.this, "Link Reset password Berhasil Dikirim.Silahkan Cek Emailmu!", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(ForgotPassActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                                    finish();
                                } else {
                                    Toast.makeText(ForgotPassActivity.this, "Reset password gagal dikirim!", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }

                            }
                        });
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ForgotPassActivity.this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }
}

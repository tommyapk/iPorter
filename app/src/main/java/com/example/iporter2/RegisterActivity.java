package com.example.iporter2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    private Button btnregister;
    private TextView tvlogin;
    private EditText etusername,etnama,etemail,etpass;
    private FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    private ProgressDialog progressDialog;
    FirebaseDatabase firebaseDatabase;

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

        databaseReference =FirebaseDatabase.getInstance().getReference("DbManager");

        /* Menjalankan Method razia() jika merasakan tombol SignUp di keyboard disentuh */
        etpass.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_NULL) {
                    razia();
                    return false;
                }
                return false;
            }
        });
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
//                razia();
                final String username = etusername.getText().toString();
                final String nama = etnama.getText().toString();
                final String email = etemail.getText().toString();
                final String pass = etpass.getText().toString();

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
                                        //initialize
                                        progressDialog = new ProgressDialog(RegisterActivity.this);
                                        //show
                                        progressDialog.show();
                                        //set contentview
                                        progressDialog.setContentView(R.layout.progress_dialog);
                                        //set transparent background
                                        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                                        // Sign in success, update UI with the signed-in user's information
                                        DbManager dbuser = new DbManager(username, nama, email, pass);
                                         FirebaseDatabase.getInstance().getReference("DbManager")
                                                 .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                 .setValue(dbuser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                             @Override
                                             public void onComplete(@NonNull Task<Void> task) {
                                                 Toast.makeText(RegisterActivity.this, "Register Berhasil", Toast.LENGTH_LONG).show();
                                                 startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                                 finish();
                                             }
                                         });
//                                        FirebaseUser user = mAuth.getCurrentUser();
//                                        Toast.makeText(RegisterActivity.this, "Register Berhasil",
//                                                Toast.LENGTH_SHORT).show();
//                                        Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
//                                        startActivity(i);
//                                        finish();
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

    /** Men-check inputan User dan Password dan Memberikan akses ke MainActivity */
    private void razia(){
        /* Mereset semua Error dan fokus menjadi default */
        etusername.setError(null);
        etnama.setError(null);
        etemail.setError(null);
        etpass.setError(null);
        View fokus = null;
        boolean cancel = false;


        /* Mengambil text dari Form User, Password, Repassword dengan variable baru bertipe String*/
        String ambiluser = etusername.getText().toString();
        String ambilnama = etnama.getText().toString();
        String ambilemail = etemail.getText().toString();
        String ambilpass = etpass.getText().toString();

        /* Jika form user kosong atau MEMENUHI kriteria di Method cekUser() maka, Set error di Form-
         * User dengan menset variable fokus dan error di Viewnya juga cancel menjadi true*/
        if (TextUtils.isEmpty(ambiluser)){
            etusername.setError("This field is required");
            fokus = etusername;
            cancel = true;
        }else if(cekUser(ambiluser)){
            etusername.setError("This Username is already exist");
            fokus = etusername;
            cancel = true;
        }

        if (TextUtils.isEmpty(ambilnama)){
            etusername.setError("This field is required");
            fokus = etusername;
            cancel = true;
        }

        if (TextUtils.isEmpty(ambilemail)){
            etemail.setError("This field is required");
            fokus = etemail;
            cancel = true;
        }
        /* Jika form password kosong dan MEMENUHI kriteria di Method cekPassword maka,
         * Reaksinya sama dengan percabangan User di atas. Bedanya untuk Password dan Repassword*/
        if (TextUtils.isEmpty(ambilpass)){
            etpass.setError("This field is required");
            fokus = etpass;
            cancel = true;
        }

        /** Jika cancel true, variable fokus mendapatkan fokus. Jika false, maka
         *  Kembali ke LoginActivity dan Set User dan Password untuk data yang terdaftar */
        if (cancel){
            fokus.requestFocus();
        }else{
            Preferences.setRegisteredUser(getBaseContext(),ambiluser);
            Preferences.setRegisteredPass(getBaseContext(),ambilpass);

            finish();
        }
    }

    /** True jika parameter user sama dengan data user yang terdaftar dari Preferences */
    private boolean cekUser(String user){
        return user.equals(Preferences.getRegisteredUser(getBaseContext()));
    }
}

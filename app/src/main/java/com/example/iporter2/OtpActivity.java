package com.example.iporter2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class OtpActivity extends AppCompatActivity {

    private Button submitBtn;
    private EditText phoneText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        submitBtn = findViewById(R.id.submit);
        phoneText = findViewById(R.id.phone_text);
        buttonSubmitListener();
    }

    private void buttonSubmitListener(){
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO : Cek string edittext kosong atau tidak
                if(!phoneText.getText().toString().isEmpty()){
                    if(phoneText.getText().toString().charAt(0) == '0'){
                        String temp = "+62" + phoneText.getText().toString().substring(1);

                        // TODO : Passing phone number ke OTP Activity
                        Bundle bundle = new Bundle();
                        Intent intent = new Intent(OtpActivity.this, ConfirmOtpActivity.class);
                        bundle.putString("phone", temp);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }
                else{
                    Toast.makeText(OtpActivity.this, "Masukkan nomor telpon", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

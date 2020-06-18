package com.example.iporter2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class ConfirmOtpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private String mVerificationId;
    private String phoneNumber;
    private PinEntryEditText otpcode;
    private TextView resendText, successText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_otp);

        otpcode = findViewById(R.id.otp_code);
        resendText = findViewById(R.id.resend_code);
        successText = findViewById(R.id.success_text);

        // get data nomor telpon dari MainActivity
        Bundle bundle = getIntent().getExtras();
        phoneNumber = bundle.getString("phone");
        mAuth = FirebaseAuth.getInstance();
        requestOtp(phoneNumber);
        widgetListener();
    }

    public void requestOtp(String phone){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone, 120, TimeUnit.SECONDS, this, callbacks()
        );
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks(){

        return new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                Log.e("onVerificationCompleted", phoneAuthCredential.getSmsCode());

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    Log.e("invalidCredential",e.toString());
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Log.e("out of quota", e.toString());
                }
            }

            @Override
            public void onCodeAutoRetrievalTimeOut(String s) {
                super.onCodeAutoRetrievalTimeOut(s);
                Log.e("", s);
            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(verificationId, forceResendingToken);

                Log.e("","onCodeSent:" + verificationId);
                mVerificationId = verificationId;
                mResendToken = forceResendingToken;
            }
        };
    }

    private void sendOtpCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Log.e("status", "signInWithCredential:success");

                            // do login auth !!
                            FirebaseAuth.getInstance().signOut();
                            otpcode.setVisibility(View.GONE);
                            resendText.setVisibility(View.GONE);
                            successText.setVisibility(View.VISIBLE);
                            Toast.makeText(ConfirmOtpActivity.this,"OTP Sukses", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ConfirmOtpActivity.this,HomeActivity.class);
                            startActivity(intent);
                            finish();


                        } else {
                            Log.w("status", "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Log.e("err", task.getException().toString());
                                Toast.makeText(ConfirmOtpActivity.this,"Kode Verifikasi Salah", Toast.LENGTH_SHORT).show();

                            }
                        }
                    }
                });
    }

    public void resendCodeVerify(String phone){
        if(mResendToken != null){
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    phone, 120, TimeUnit.SECONDS, this, callbacks(), mResendToken
            );
            Toast.makeText(ConfirmOtpActivity.this,"Kode dikirim ulang", Toast.LENGTH_SHORT).show();
        }
    }



    private void widgetListener(){
        otpcode.setOnPinEnteredListener(new PinEntryEditText.OnPinEnteredListener() {
            @Override
            public void onPinEntered(CharSequence str) {
                sendOtpCode(str.toString());
            }
        });

        resendText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendCodeVerify(phoneNumber);
            }
        });
    }
}

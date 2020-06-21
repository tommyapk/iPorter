package com.example.iporter2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

public class DetailProfilActivity extends AppCompatActivity {

    private TextView editnama,editpass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_profil);

        editnama = findViewById(R.id.tv_editnama);
        editpass = findViewById(R.id.tv_editpass);

        editnama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), EditNamaActivity.class));
                finish();
            }
        });
        editpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), GantiPassActivity.class));
                finish();
            }
        });

    }

}

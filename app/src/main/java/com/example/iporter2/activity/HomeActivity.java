package com.example.iporter2.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.iporter2.R;
import com.example.iporter2.fragment.AkunFragment;
import com.example.iporter2.fragment.NotifFragment;
import com.example.iporter2.fragment.ProfilFragment;
import com.example.iporter2.fragment.RuteFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private final String TAG = "HomeActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // kita set default nya Home Fragment
        loadFragment(new RuteFragment());
// inisialisasi BottomNavigaionView
        final BottomNavigationView bottomNavigationView = findViewById(R.id.bn_main);
// beri listener pada saat item/menu bottomnavigation terpilih
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

//        fixed bottom nabigation ketika keyboard muncul
        KeyboardVisibilityEvent.setEventListener(
                this,
                new KeyboardVisibilityEventListener() {
                    @Override
                    public void onVisibilityChanged(boolean isOpen) {
                        Log.d(TAG,"onVisibilityChanged: Keyboard visibility changed");
                        if(isOpen){
                            Log.d(TAG, "onVisibilityChanged: Keyboard is open");
                            bottomNavigationView.setVisibility(View.INVISIBLE);
                            Log.d(TAG, "onVisibilityChanged: NavBar got Invisible");
                        }else{
                            Log.d(TAG, "onVisibilityChanged: Keyboard is closed");
                            bottomNavigationView.setVisibility(View.VISIBLE);
                            Log.d(TAG, "onVisibilityChanged: NavBar got Visible");
                        }
                    }
                });


    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fl_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()){
            case R.id.home_menu:
                fragment = new RuteFragment();
                break;
            case R.id.search_menu:
                fragment = new ProfilFragment();
                break;
            case R.id.favorite_menu:
                fragment = new NotifFragment();
                break;
            case R.id.account_menu:
                fragment = new AkunFragment();
                break;
        }
        return loadFragment(fragment);
    }

}

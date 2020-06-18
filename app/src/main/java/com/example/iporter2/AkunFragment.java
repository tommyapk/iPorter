package com.example.iporter2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class AkunFragment extends Fragment {

    private TextView email_login;
    private Button logout;
    private CircleImageView img_userprofil;
    Preferences preferences;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    Context context;

    public AkunFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_akun, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {

        email_login = view.findViewById(R.id.tv_emaillogin);
        logout = view.findViewById(R.id.btn_logout);
        img_userprofil = view.findViewById(R.id.img_profil);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();


//        email_login.setText(Preferences.getLoggedInUser(getActivity().getBaseContext()));
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getActivity());
        if (account != null){
            firebaseAuth.getCurrentUser().getPhotoUrl().toString();
            String personName = account.getDisplayName();
            String personPhotoUrl = account.getPhotoUrl().toString();
            Glide.with(getActivity()).load(personPhotoUrl)
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(img_userprofil);
            email_login.setText("Welcome "+account.getDisplayName());
            Toast.makeText(getActivity(), personName, Toast.LENGTH_SHORT).show();
        }

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Menghapus Status login dan kembali ke Login Activity
                Preferences.clearLoggedInUser(getActivity().getBaseContext());
                Toast.makeText(getActivity(), "Berhasil Logout", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity().getBaseContext(),LoginActivity.class));
                getActivity().finish();

//                firebaseAuth.getInstance().signOut();

//                Intent intent = new Intent(getActivity(),LoginActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(intent);

            }
        });

    }
}

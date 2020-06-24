package com.example.iporter2.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.iporter2.activity.BayarActivity;
import com.example.iporter2.activity.DetailProfilActivity;
import com.example.iporter2.activity.DiterimaActivity;
import com.example.iporter2.activity.KirimActivity;
import com.example.iporter2.activity.LoginActivity;
import com.example.iporter2.sharedpreference.Preferences;
import com.example.iporter2.activity.ProsesActivity;
import com.example.iporter2.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class AkunFragment extends Fragment {

    private TextView email_login,infoakun;
    private Button logout,proses,bayar,dikirim,diterima;
    private CircleImageView img_userprofil;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    public AkunFragment() {
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_akun, container, false);

    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        infoakun =view.findViewById(R.id.tv_infoakun);
        dikirim = view.findViewById(R.id.btn_dikirim);
        diterima = view.findViewById(R.id.btn_diterima);
        proses = view.findViewById(R.id.btn_proses);
        bayar = view.findViewById(R.id.btn_bayar);
        email_login = view.findViewById(R.id.tv_emaillogin);
        logout = view.findViewById(R.id.btn_logout);
        img_userprofil = view.findViewById(R.id.img_profil);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();


//        email_login.setText(Preferences.getLoggedInUser(getActivity().getBaseContext()));
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getActivity());
        if (account != null){
//            firebaseAuth.getCurrentUser().getPhotoUrl().toString();
//            String personPhotoUrl = account.getPhotoUrl().toString();
//            Glide.with(getActivity()).load(personPhotoUrl)
//                    .thumbnail(0.5f)
//                    .crossFade()
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .into(img_userprofil);
            email_login.setText("Welcome "+account.getDisplayName());
        }

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Menghapus Status login dan kembali ke Login Activity
                Preferences.clearLoggedInUser(getActivity().getBaseContext());
                Toast.makeText(getActivity(), "Berhasil Logout", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity().getBaseContext(), LoginActivity.class));
                getActivity().finish();

//                firebaseAuth.getInstance().signOut();

//                Intent intent = new Intent(getActivity(),LoginActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(intent);

            }
        });

        bayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), BayarActivity.class));

            }
        });

        proses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ProsesActivity.class));

            }
        });

        dikirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), KirimActivity.class));

            }
        });

       diterima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), DiterimaActivity.class));

            }
        });
       infoakun.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               startActivity(new Intent(getActivity(), DetailProfilActivity.class));
           }
       });


    }
}

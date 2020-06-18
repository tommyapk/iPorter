package com.example.iporter2;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class RuteFragment extends Fragment {

    private RecyclerView recyclerView;
    private MahasiswaAdapter adapter;
    private ArrayList<Mahasiswa> mahasiswaArrayList;
    private final String TAG = "HomeActivity";
    public RuteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rute, container, false);

    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
        addData();

        recyclerView = (RecyclerView) view.findViewById(R.id.recyler_view);

        adapter = new MahasiswaAdapter(mahasiswaArrayList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);


        final FloatingActionButton floatingActionButton=view.findViewById(R.id.fab1);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Floating Action Button Berhasil dibuat", Toast.LENGTH_SHORT).show();
                showAlertDialogButtonClicked(view);
            }
        });

        KeyboardVisibilityEvent.setEventListener(
                getActivity(),
                new KeyboardVisibilityEventListener() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onVisibilityChanged(boolean isOpen) {
                        Log.d(TAG,"onVisibilityChanged: Keyboard visibility changed");
                        if(isOpen){
                            Log.d(TAG, "onVisibilityChanged: Keyboard is open");
                            floatingActionButton.setVisibility(View.INVISIBLE);
                            Log.d(TAG, "onVisibilityChanged: NavBar got Invisible");
                        }else{
                            Log.d(TAG, "onVisibilityChanged: Keyboard is closed");
                            floatingActionButton.setVisibility(View.VISIBLE);
                            Log.d(TAG, "onVisibilityChanged: NavBar got Visible");
                        }
                    }
                });



    }
    void addData(){
        mahasiswaArrayList = new ArrayList<>();
        mahasiswaArrayList.add(new Mahasiswa("Jakarta - Makassar", "Tommy Darmawansyah", "Minggu, 31 Mei 2020"));
        mahasiswaArrayList.add(new Mahasiswa("Surabaya - Makassar", "Irsan Mansyur", "Senin, 1 Juni 2020"));
        mahasiswaArrayList.add(new Mahasiswa("Jogja - Makassar", "Noralisa Nurdin", "Selasa, 2 Juni 2020"));
        mahasiswaArrayList.add(new Mahasiswa("Jakarta - Makassar", "Dirza Rakhmat", "Rabu, 3 Juni 2020"));
        mahasiswaArrayList.add(new Mahasiswa("Jakarta - Makassar", "Jesica Datu", "Kamis, 4 Juni 2020"));
        mahasiswaArrayList.add(new Mahasiswa("Jakarta - Maksasar", "Iga Mawarni", "Jumat, 5 Juni 2020"));
        mahasiswaArrayList.add(new Mahasiswa("Jakarta - Maksasar", "Iga Mawarni", "Jumat, 5 Juni 2020"));
        mahasiswaArrayList.add(new Mahasiswa("Jakarta - Maksasar", "Iga Mawarni", "Jumat, 5 Juni 2020"));
        mahasiswaArrayList.add(new Mahasiswa("Jakarta - Maksasar", "Iga Mawarni", "Jumat, 5 Juni 2020"));

    }

    private void showAlertDialogButtonClicked(View view) {
        // create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setTitle("Name");
        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.post_dialog, null);
        builder.setView(customLayout);
        // add a button
        builder.setPositiveButton("POSTING", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // send data from the AlertDialog to the Activity
                EditText Dari = customLayout.findViewById(R.id.et_dari);
                EditText Tujuan = customLayout.findViewById(R.id.et_tujuan);
                EditText NamePorter = customLayout.findViewById(R.id.et_namaporter);
                EditText DateTrip = customLayout.findViewById(R.id.et_datetrip);
                sendDialogDataToActivity(Dari.getText().toString());
                sendDialogDataToActivity(Tujuan.getText().toString());
                sendDialogDataToActivity(NamePorter.getText().toString());
                sendDialogDataToActivity(DateTrip.getText().toString());
            }
        });
        builder.setNegativeButton("BATALKAN", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    // do something with the data coming from the AlertDialog
    private void sendDialogDataToActivity(String data) {
        Toast.makeText(getActivity(), data, Toast.LENGTH_LONG).show();
        Toast.makeText(getActivity(), data, Toast.LENGTH_LONG).show();
        Toast.makeText(getActivity(), data, Toast.LENGTH_LONG).show();
    }
}

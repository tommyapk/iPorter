package com.example.iporter2.model;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iporter2.R;
import com.example.iporter2.activity.ChatActivity;
import com.example.iporter2.activity.DetailActivity;
import com.example.iporter2.fragment.RuteFragment;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MahasiswaAdapter extends RecyclerView.Adapter<MahasiswaAdapter.MahasiswaViewHolder> {

    private ArrayList<Mahasiswa> dataList;



    public MahasiswaAdapter(ArrayList<Mahasiswa> dataList) {
        this.dataList = dataList;
    }

    @Override
    public MahasiswaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.activity_listview, parent, false);
        return new MahasiswaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MahasiswaViewHolder holder, int position) {
        holder.txtNama.setText(dataList.get(position).getNama());
        holder.txtNpm.setText(dataList.get(position).getNpm());
        holder.txtNoHp.setText(dataList.get(position).getNohp());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Detail Porter", Toast.LENGTH_SHORT).show();
                Intent intent;
                intent = new Intent(v.getContext(), DetailActivity.class);
                v.getContext().startActivity(intent);
            }
        });
        holder.chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Detail Chat", Toast.LENGTH_SHORT).show();
                Intent intent;
                intent = new Intent(v.getContext(), ChatActivity.class);
                v.getContext().startActivity(intent);
            }
        });
        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Panggil", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public class MahasiswaViewHolder extends RecyclerView.ViewHolder{
        private TextView txtNama, txtNpm, txtNoHp;
        private Button call,chat;
        private CardView cardView;

        public MahasiswaViewHolder(View itemView) {
            super(itemView);
            txtNama = (TextView) itemView.findViewById(R.id.txt_nama_mahasiswa);
            txtNpm = (TextView) itemView.findViewById(R.id.txt_npm_mahasiswa);
            txtNoHp = (TextView) itemView.findViewById(R.id.txt_nohp_mahasiswa);
            call = itemView.findViewById(R.id.btn_call);
            chat = itemView.findViewById(R.id.btn_chat);
            cardView = itemView.findViewById(R.id.card_view);

        }
    }

}

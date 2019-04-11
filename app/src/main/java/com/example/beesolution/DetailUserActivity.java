package com.example.beesolution;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class DetailUserActivity extends AppCompatActivity {



    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;

    private EditText etNominal, etNamaHidden, etAlamatHidden;
    private TextView tvNama, tvNamaHidden, tvAlamat, tvAlamatHidden, tvBayar, tvMenang;
    private String iduser, nama, alamat;
    private Button btBayar, btBayar1, btUbah, btUbah1, btHapus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_user);

        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference("").child("ARISAN").child("USER");


        btHapus = (Button) findViewById(R.id.buttonHapusDetail);
        btUbah1 = (Button) findViewById(R.id.buttonUbahDetail1);
        btUbah = (Button) findViewById(R.id.buttonUbahDetail);
        btBayar = (Button) findViewById(R.id.buttonBayarDetail);
        btBayar1 = (Button) findViewById(R.id.buttonBayarDetail1);
        iduser = getIntent().getStringExtra("iduser");
        etNamaHidden = (EditText) findViewById(R.id.editTextNamaAnggotaHidden);
        etAlamatHidden = (EditText) findViewById(R.id.editTextAlamatAnggotaHidden);
        etNominal = (EditText) findViewById(R.id.editTextNominal);
        tvNamaHidden = (TextView) findViewById(R.id.textViewNamaHidden);
        tvNama = (TextView) findViewById(R.id.textViewNamaDetail);
        tvAlamatHidden = (TextView) findViewById(R.id.textViewAlamatHidden);
        tvAlamat = (TextView) findViewById(R.id.textViewAlamatDetail);
        tvBayar = (TextView) findViewById(R.id.textViewBayarDetail);
        tvMenang = (TextView) findViewById(R.id.textViewMenangDetail);

        btBayar1.setVisibility(View.GONE);
        etNominal.setVisibility(View.GONE);

        tvAlamatHidden.setVisibility(View.GONE);
        tvNamaHidden.setVisibility(View.GONE);
        etNamaHidden.setVisibility(View.GONE);
        etAlamatHidden.setVisibility(View.GONE);
        btUbah1.setVisibility(View.GONE);


        btUbah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btUbah.setVisibility(View.GONE);
                tvAlamat.setVisibility(View.GONE);
                tvNama.setVisibility(View.GONE);


                tvAlamatHidden.setVisibility(View.VISIBLE);
                tvNamaHidden.setVisibility(View.VISIBLE);
                etNamaHidden.setVisibility(View.VISIBLE);
                etAlamatHidden.setVisibility(View.VISIBLE);
                btUbah1.setVisibility(View.VISIBLE);
            }
        });

        btUbah1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nama = etNamaHidden.getText().toString();
                alamat = etAlamatHidden.getText().toString();

                if(TextUtils.isEmpty(nama)){
                    Toast.makeText(DetailUserActivity.this,"Kolom Nama Tidak Boleh Kosong",Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(alamat)){
                    Toast.makeText(DetailUserActivity.this,"Kolom Alamat Tidak Boleh Kosong",Toast.LENGTH_SHORT).show();
                }
                else{
                    myRef.child(iduser).child("nama").setValue(nama);
                    myRef.child(iduser).child("alamat").setValue(alamat);
                    startActivity(new Intent(DetailUserActivity.this, MainActivity.class));
                    finish();
                }
            }
        });

        btBayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btBayar.setVisibility(View.GONE);
                btBayar1.setVisibility(View.VISIBLE);
                etNominal.setVisibility(View.VISIBLE);
            }
        });

        btBayar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nominal = etNominal.getText().toString();
                if(Integer.parseInt(nominal) < 50000){
                    Toast.makeText(DetailUserActivity.this, "Minimal Pembayaran Adalah 50.000", Toast.LENGTH_SHORT).show();
                }
                else{
                    myRef.child(iduser).child("nominal").setValue(nominal);
                    myRef.child(iduser).child("bayar").setValue("true");
                    startActivity(new Intent(DetailUserActivity.this, MainActivity.class));
                    finish();
                }

            }
        });





        btHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef.child(iduser).setValue(null);
                startActivity(new Intent(DetailUserActivity.this, MainActivity.class));
                finish();
            }
        });


        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                showNama((Map<String, Object>) dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void showNama(Map<String, Object> dataSnapshot) {
        final ArrayList<String> Nama = new ArrayList<>();
        for (Map.Entry<String, Object> entry : dataSnapshot.entrySet()) {
            Map nama = (Map) entry.getValue();
            Nama.add((String) nama.get("nama"));
        }
        final ArrayList<String> Id = new ArrayList<>();
        for (Map.Entry<String, Object> entry : dataSnapshot.entrySet()) {
            Map id = (Map) entry.getValue();
            Id.add((String) id.get("iduser"));
        }
        final ArrayList<String> Alamat = new ArrayList<>();
        for (Map.Entry<String, Object> entry : dataSnapshot.entrySet()) {
            Map alamat = (Map) entry.getValue();
            Alamat.add((String) alamat.get("alamat"));
        }
        final ArrayList<String> Menang = new ArrayList<>();
        for (Map.Entry<String, Object> entry : dataSnapshot.entrySet()) {
            Map menang = (Map) entry.getValue();
            Menang.add((String) menang.get("menang"));
        }
        final ArrayList<String> Bayar = new ArrayList<>();
        for (Map.Entry<String, Object> entry : dataSnapshot.entrySet()) {
            Map bayar = (Map) entry.getValue();
            Bayar.add((String) bayar.get("bayar"));
        }
        int i = 0;
        while(Id.size()>i){
            if(Id.get(i).equals(iduser)){
             tvNama.setText("Nama                : " + Nama.get(i));
             tvAlamat.setText("Alamat              : " + Alamat.get(i));
             if(Bayar.get(i).equals("false")){
                 tvBayar.setText("Status Bayar        : Belum Bayar");
             }
             else{
                 tvBayar.setText("Status Bayar        : Sudah Bayar");
                 btBayar.setVisibility(View.GONE);
             }
             if(Menang.get(i).equals("false")){
                 tvMenang.setText("Status Menang     : Belum Menang");
             }
             else{

                 tvMenang.setText("Status Menang     : Sudah Menang");
             }
            }
            i++;
        }
    }
}

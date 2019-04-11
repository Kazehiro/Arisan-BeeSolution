package com.example.beesolution;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;

    private Button btTambah, btKocok, btSearch, btSearch1;
    private EditText etSearch;
    private ListView lvDaftarAnggota;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference("").child("ARISAN").child("USER");

        lvDaftarAnggota = (ListView) findViewById(R.id.listViewAnggota);

        btTambah = (Button) findViewById(R.id.buttonTambahMainActivity);
        btKocok= (Button) findViewById(R.id.buttonKocok);
        btSearch = (Button) findViewById(R.id.buttonSearch);
        btSearch1 = (Button) findViewById(R.id.buttonSearch1);
        etSearch = (EditText) findViewById(R.id.editTextSearch);

        btSearch1.setVisibility(View.GONE);
        etSearch.setVisibility(View.GONE);

        btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btSearch.setVisibility(View.GONE);
                btSearch1.setVisibility(View.VISIBLE);
                etSearch.setVisibility(View.VISIBLE);
            }
        });





        btTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TambahActivity.class));
            }
        });

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try{
                    showNama((Map<String, Object>) dataSnapshot.getValue());
                }
                catch (NullPointerException e){
                    Toast.makeText(MainActivity.this,"Tidak Ada Data Pelanggan",Toast.LENGTH_LONG).show();
                }
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
        final ArrayList<String> IdKocok = new ArrayList<>();
        final ArrayList<String> NamaKocok = new ArrayList<>();
        while(Id.size()>i){
            if(Menang.get(i).equals("false")){
                if(Bayar.get(i).equals("true")){
                    NamaKocok.add(Nama.get(i));
                    IdKocok.add(Id.get(i));
                }
            }
            i++;
        }

        btKocok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    myRef.child(IdKocok.get(0)).child("menang").setValue("true");
                    Toast.makeText(MainActivity.this, "Yang Menang Kocok Adalah " + NamaKocok.get(0), Toast.LENGTH_LONG).show();
                }
                catch (IndexOutOfBoundsException e){
                    Toast.makeText(MainActivity.this, "Semua Anggota yang telah membayar Telah Menang", Toast.LENGTH_LONG).show();

                }
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try{
                            showNama((Map<String, Object>) dataSnapshot.getValue());
                        }
                        catch (NullPointerException e){
                            Toast.makeText(MainActivity.this,"Tidak Ada Data Pelanggan",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, Nama);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        lvDaftarAnggota.setAdapter(adapter);
        lvDaftarAnggota.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent mIntent = new Intent(MainActivity.this, DetailUserActivity.class);
                String iduser = Id.get(position);
                mIntent.putExtra("iduser", iduser);
                startActivity(mIntent);
            }
        });
        btSearch1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String SNamaSearch = etSearch.getText().toString();


                final ArrayList<String> IdSearch = new ArrayList<>();
                final ArrayList<String> NamaSearch = new ArrayList<>();
                int i = 0;
                while(Nama.size()>i){
                    if (Nama.get(i).contains(SNamaSearch)){
                        NamaSearch.add(Nama.get(i));
                        IdSearch.add(Id.get(i));
                    }
                    i++;
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, R.layout.support_simple_spinner_dropdown_item, NamaSearch);
                adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                lvDaftarAnggota.setAdapter(adapter);
                lvDaftarAnggota.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent mIntent = new Intent(MainActivity.this, DetailUserActivity.class);
                        String iduser = IdSearch.get(position);
                        mIntent.putExtra("iduser", iduser);
                        startActivity(mIntent);
                    }
                });
            }
        });
    }
}

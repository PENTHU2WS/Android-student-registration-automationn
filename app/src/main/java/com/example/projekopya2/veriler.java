package com.example.projekopya2;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class veriler extends AppCompatActivity {

    private ListView listView;
    private DatabaseReference mReference;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> kullaniciAdlari;
    private ArrayList<String> kullaniciIdleri;
    private EditText searchEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_veriler);

        listView = findViewById(R.id.listView);
        searchEditText = findViewById(R.id.txtara);
        kullaniciAdlari = new ArrayList<>();
        kullaniciIdleri = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, kullaniciAdlari);
        listView.setAdapter(adapter);

        mReference = FirebaseDatabase.getInstance().getReference("kullanicilar");

        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String kullaniciAdi = userSnapshot.child("isim").getValue(String.class);
                    String kullaniciId = userSnapshot.getKey();
                    kullaniciAdlari.add(kullaniciAdi);
                    kullaniciIdleri.add(kullaniciId);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(veriler.this, "Veri alınamadı: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String secilenKullaniciId = kullaniciIdleri.get(position);
                Intent intent = new Intent(veriler.this, Bilgiler.class);
                intent.putExtra("kullaniciId", secilenKullaniciId);
                startActivity(intent);
                return true;
            }
        });

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String searchText = s.toString().toLowerCase();

                for (int i = 0; i < kullaniciAdlari.size(); i++) {
                    if (kullaniciAdlari.get(i).toLowerCase().contains(searchText)) {
                        Collections.swap(kullaniciAdlari, i, 0);
                        Collections.swap(kullaniciIdleri, i, 0);
                        break;
                    }
                }

                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                kullaniciAdlari.clear();
                kullaniciIdleri.clear();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String kullaniciAdi = userSnapshot.child("isim").getValue(String.class);
                    String kullaniciId = userSnapshot.getKey();
                    kullaniciAdlari.add(kullaniciAdi);
                    kullaniciIdleri.add(kullaniciId);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(veriler.this, "Veri alınamadı: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

package com.example.projekopya2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

public class Bilgiler extends AppCompatActivity {

    private ListView listView1;
    private ListView listView2;
    private Button deleteButton;
    private DatabaseReference mReference;
    private ArrayAdapter<String> adapter1;
    private ArrayAdapter<String> adapter2;
    private ArrayList<String> bilgilerList1;
    private ArrayList<String> bilgilerList2;
    private DataSnapshot dataSnapshot; // Veri değişikliği dinleyicisi için üye değişken

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bilgiler);

        listView1 = findViewById(R.id.veri2);
        listView2 = findViewById(R.id.veri);
        deleteButton = findViewById(R.id.button);

        bilgilerList1 = new ArrayList<>();
        bilgilerList2 = new ArrayList<>();
        adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, bilgilerList1);
        adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, bilgilerList2);
        listView1.setAdapter(adapter1);
        listView2.setAdapter(adapter2);

        String kullaniciId = getIntent().getStringExtra("kullaniciId");
        mReference = FirebaseDatabase.getInstance().getReference("kullanicilar").child(kullaniciId);

        // Veri değişikliği dinleyicisi
        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Bilgiler.this.dataSnapshot = dataSnapshot; // dataSnapshot üye değişkenine atama yap

                // Kullanıcı ismini ekleyelim
                String isim = dataSnapshot.child("isim").getValue(String.class);
                bilgilerList1.add("isim");
                bilgilerList2.add(isim);

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String value = snapshot.getValue(String.class);
                    if (!snapshot.getKey().equals("isim") && value != null) {
                        bilgilerList1.add(snapshot.getKey());
                        bilgilerList2.add(value);
                    }
                }
                adapter1.notifyDataSetChanged();
                adapter2.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Bilgiler.this, "Veri alınamadı: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        listView1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showEditDialog(position);
                return true;
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kullanıcıyı sil
                mReference.removeValue();
                Toast.makeText(Bilgiler.this, "Kullanıcı başarıyla silindi.", Toast.LENGTH_SHORT).show();
                // Aktiviteyi kapat ve geri dön
                finish();
            }
        });
    }

    private void showEditDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Düzenle");

        final EditText editText = new EditText(this);
        editText.setText(bilgilerList2.get(position));
        builder.setView(editText);

        builder.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newInfo = editText.getText().toString();
                bilgilerList2.set(position, newInfo);
                adapter2.notifyDataSetChanged();

                // Firebase veritabanındaki veriyi güncelle
                String key = bilgilerList1.get(position);
                mReference.child(key).setValue(newInfo);

                Toast.makeText(Bilgiler.this, "Veri güncellendi", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}

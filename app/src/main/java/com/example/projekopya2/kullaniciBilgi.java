package com.example.projekopya2;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class kullaniciBilgi extends AppCompatActivity {

    private DatabaseReference mReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kullanici_bilgi);

        // firebase kullanıcısını al
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // kullanıcının ıdsini al
        String kullaniciUID = user.getUid();

        // firebase veritabanı referansını al
        mReference = FirebaseDatabase.getInstance().getReference("kullanicilar").child(kullaniciUID);

        // kullanıcı verilerini al ve TextView ayrla
        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String kullaniciAdi = dataSnapshot.child("isim").getValue(String.class);
                String kullaniciNo = dataSnapshot.child("okulno").getValue(String.class);
                String mail = dataSnapshot.child("email").getValue(String.class);
                String sinif = dataSnapshot.child("sinif").getValue(String.class);
                String sifre = dataSnapshot.child("sifre").getValue(String.class);
                String aciklama = dataSnapshot.child("aciklama").getValue(String.class);


                TextView textViewKullaniciAdi = findViewById(R.id.textViewKullaniciAdi);
                TextView textViewOkulno = findViewById(R.id.textViewNO);
                TextView textViewMail = findViewById(R.id.textViewMail);
                TextView textViewSinif = findViewById(R.id.textViewSinif);
                TextView textViewSifre = findViewById(R.id.textViewSifre);
                TextView textViewAciklama = findViewById(R.id.textViewAciklama);


                textViewKullaniciAdi.setText("Kullanıcı Adı :     " + kullaniciAdi);
                textViewMail.setText("Mail:  " + mail);
                textViewOkulno.setText("NO: " + kullaniciNo);
                textViewSinif.setText("Sınıf: " + sinif);
                textViewSifre.setText("Şifre:  " + sifre);
                textViewAciklama.setText("Açıklama: \n\n"+ aciklama);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(kullaniciBilgi.this, "Hata", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

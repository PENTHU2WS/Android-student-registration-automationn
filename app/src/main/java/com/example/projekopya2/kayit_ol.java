
package com.example.projekopya2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class kayit_ol extends AppCompatActivity {

        private EditText email, sifre, okulno, sinif, isim, aciklama;

        private FirebaseUser mUser;
        private String txtemail, txtsifre, txtokulno, txtsinif, txtisim, txtaciklama;
        private FirebaseAuth mAuth;
        private DatabaseReference mreferans;
        private HashMap<String, Object> mData;

        @SuppressLint("MissingInflatedId")
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.kayit_ol);
            email = (EditText) findViewById(R.id.kayitUser);
            sifre = (EditText) findViewById(R.id.kayitPassword);
            aciklama = (EditText) findViewById(R.id.aciklama);
            sinif = (EditText) findViewById(R.id.sinif);
            isim = (EditText) findViewById(R.id.AdinSoyadin);
            okulno = (EditText) findViewById(R.id.okulNo);


            mAuth = FirebaseAuth.getInstance();
            mreferans = FirebaseDatabase.getInstance().getReference();

        }

        public void kayitOl(View view) {
            txtemail = email.getText().toString();
            txtsifre = sifre.getText().toString();
            txtokulno = okulno.getText().toString();
            txtsinif = sinif.getText().toString();
            txtisim = isim.getText().toString();
            txtaciklama = aciklama.getText().toString();
            if (!TextUtils.isEmpty(txtemail) && !TextUtils.isEmpty(txtsifre) && !TextUtils.isEmpty(txtisim) && !TextUtils.isEmpty(txtokulno) && !TextUtils.isEmpty(txtsinif)) {
                mAuth.createUserWithEmailAndPassword(txtemail, txtsifre)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    mUser = mAuth.getCurrentUser();
                                    mData = new HashMap<>();
                                    mData.put("isim", txtisim);
                                    mData.put("email", txtemail);
                                    mData.put("sifre", txtsifre);
                                    mData.put("okulno", txtokulno);
                                    mData.put("sinif", txtsinif);
                                    mData.put("aciklama", txtaciklama);
                                    mData.put("id", mUser.getUid());
                                    mreferans.child("kullanicilar").child(mUser.getUid())
                                            .setValue(mData)
                                            .addOnCompleteListener(kayit_ol.this, new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful())
                                                        Toast.makeText(kayit_ol.this, "kayıt okey", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                } else {
                                    Toast.makeText(kayit_ol.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            } else {
                Toast.makeText(this, "Boş bırakma", Toast.LENGTH_SHORT).show();
            }

        }
    }

package com.example.projekopya2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

    public class giris extends AppCompatActivity {

        private EditText email, sifre;
        private FirebaseAuth mAuth;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.giris_yap);

            email = findViewById(R.id.usernameEditText);
            sifre = findViewById(R.id.passwordEditText);

            mAuth = FirebaseAuth.getInstance();
        }

        public void girisYap(View view) {
            String txtemail = email.getText().toString();
            String txtsifre = sifre.getText().toString();

            if (!TextUtils.isEmpty(txtemail) && !TextUtils.isEmpty(txtsifre)) {
                mAuth.signInWithEmailAndPassword(txtemail, txtsifre)
                        .addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                FirebaseUser user = authResult.getUser();
                                if (user != null) {
                                    Intent intent = new Intent(giris.this, uyg_anasayfasi.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        }).addOnFailureListener(this, new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(giris.this, "Giriş başarısız. Hata: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                // Giriş başarısız olduğunda kullanıcıyı giriş sayfasında tutun
                            }
                        });
            } else {
                Toast.makeText(this, "Boş bırakma", Toast.LENGTH_SHORT).show();
            }
        }

        public void onRegisterClick(View view) {
            Intent intent = new Intent(this, kayit_ol.class);
            startActivity(intent);
        }
    }

package com.example.projekopya2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class uyg_anasayfasi extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.uyg_anasayfasi);
    }
 public  void kullaniciBilgisi(View view)
 {
     Intent intent = new Intent(this, kullaniciBilgi.class);
     startActivity(intent);
 }

    public  void verilerim(View view)
    {
        Intent intent = new Intent(this, veriler.class);
        startActivity(intent);
    }

    public  void exit(View view){
        System.exit(0);
    }
    public void openLink(View view) {

        String url = "https://penthusw.com.tr";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

}
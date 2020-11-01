package com.example.a1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Intent intent = getIntent();
        String webpage = intent.getStringExtra("webpage");

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(webpage));
        startActivity(browserIntent);

        finish();
    }
}
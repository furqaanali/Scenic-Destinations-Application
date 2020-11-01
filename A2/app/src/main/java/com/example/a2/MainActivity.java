package com.example.a2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button button;
    private static final String KABOOM_PERMISSION =
            "edu.uic.cs478.f20.kaboom" ;
    private static final String A3_INTENT =
            "edu.uic.cs478.Receiver";

    BroadcastReceiver receiver;
    IntentFilter mFilter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                checkPermissionAndBroadcast();
            }
        });
    }


    private void checkPermissionAndBroadcast() {
        if (ContextCompat.checkSelfPermission(this, KABOOM_PERMISSION)
                == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "A2: Permission Granted", Toast.LENGTH_SHORT)
                    .show();
            registerReceiverAndStartActivity3();
        }
        else {
            ActivityCompat.requestPermissions(this, new String[]{KABOOM_PERMISSION}, 0) ;
        }

    }


    public void onRequestPermissionsResult(int code, String[] permissions, int[] results) {
        if (results.length > 0) {
            if (results[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "A2: Permission Granted", Toast.LENGTH_SHORT)
                        .show();
                registerReceiverAndStartActivity3();
            }
            else {
                Toast.makeText(this, "Bummer: No permission", Toast.LENGTH_SHORT)
                        .show();
                finish();
            }
        }
    }


    public void registerReceiverAndStartActivity3() {
        mFilter = new IntentFilter(A3_INTENT) ;
        mFilter.setPriority(10) ;
        receiver = new MyReceiver();
        registerReceiver(receiver, mFilter);

        Intent intent = getPackageManager().getLaunchIntentForPackage("com.example.a3");
        startActivity(intent);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
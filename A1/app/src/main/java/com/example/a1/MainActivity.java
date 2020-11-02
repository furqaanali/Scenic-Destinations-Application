package com.example.a1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.PersistableBundle;
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

    Boolean isRegistered = false;


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
            Toast.makeText(this, "A1: Permission Granted", Toast.LENGTH_SHORT)
                    .show();

            registerReceiverAndStartActivity2();
        }
        else {
            ActivityCompat.requestPermissions(this, new String[]{KABOOM_PERMISSION}, 0) ;
        }

    }


    public void onRequestPermissionsResult(int code, String[] permissions, int[] results) {
        if (results.length > 0) {
            if (results[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "A1: Permission Granted", Toast.LENGTH_SHORT)
                        .show();

                registerReceiverAndStartActivity2();
            }
            else {
                Toast.makeText(this, "Bummer: No permission", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    public void registerReceiverAndStartActivity2() {
        mFilter = new IntentFilter(A3_INTENT) ;
        mFilter.setPriority(1) ;
        receiver = new MyReceiver();
        registerReceiver(receiver, mFilter);
        isRegistered = true;

        Intent intent = getPackageManager().getLaunchIntentForPackage("com.example.a2");
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isRegistered", isRegistered);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        isRegistered = savedInstanceState.getBoolean("isRegistered");
        if (isRegistered) {
            mFilter = new IntentFilter(A3_INTENT) ;
            mFilter.setPriority(1) ;
            receiver = new MyReceiver();
            registerReceiver(receiver, mFilter);
        }
    }
}
package com.example.a2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context arg0, Intent arg1) {
        // TODO Auto-generated method stub
//        Log.i("BroadcastReceiver3 app", "Programmic Receiver 1 called into action.") ;
        String secret = arg1.getStringExtra("Secret Message");
        Toast.makeText(arg0, "A2 received broadcast",
                Toast.LENGTH_LONG).show() ;

    }

}
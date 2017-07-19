package com.example.mattkopacz.bluetoothservices;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.UUID;

public class ChartActivity extends AppCompatActivity {


    private BluetoothAdapter btAdapter;
    public String MACAdress;
    private UUID mUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");


    TextView distance;

    MyBroadcast myBroadcast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);


        distance = (TextView) findViewById(R.id.distance);

        Intent intent = getIntent();


        MACAdress = intent.getStringExtra("SEND_ADRESS");

        Intent serviceIntent = new Intent(getBaseContext(), BTService.class);

        serviceIntent.putExtra("MAC_ADRESS", MACAdress);

        startService(serviceIntent);

        myBroadcast = new MyBroadcast();


        IntentFilter intentFilter = new IntentFilter();

        intentFilter.addAction(BTService.MY_ACTION);

        registerReceiver(myBroadcast, intentFilter);

    }



    private class MyBroadcast extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {

            int recivedData = intent.getIntExtra("SEND_DISTANCE", 0);

            distance.setText("The thistance is = " + recivedData);


        }
    }

}

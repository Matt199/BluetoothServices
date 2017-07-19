package com.example.mattkopacz.bluetoothservices;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.UUID;

public class ChartActivity extends AppCompatActivity {


    private BluetoothAdapter btAdapter;
    public String MACAdress;
    private UUID mUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        Intent intent = getIntent();


        MACAdress = intent.getStringExtra("SEND_ADRESS");

        Intent serviceIntent = new Intent(getBaseContext(), BTService.class);

        serviceIntent.putExtra("MAC_ADRESS", MACAdress);

        startService(serviceIntent);

    }



}

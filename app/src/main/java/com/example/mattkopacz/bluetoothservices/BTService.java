package com.example.mattkopacz.bluetoothservices;

import android.app.ProgressDialog;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

public class BTService extends Service {


    private BluetoothAdapter btAdapter;
    private static UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private BluetoothSocket mmServerSocket;
    private boolean isBTConnectet = false;


    public BTService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String adressMac = intent.getStringExtra("MAC_ADRESS");

        Log.d("Adres", "The Adress is = " + adressMac);




        try {

            if(mmServerSocket == null || !isBTConnectet ) {



                btAdapter = BluetoothAdapter.getDefaultAdapter();

                BluetoothDevice device = btAdapter.getRemoteDevice(adressMac);

                mmServerSocket = device.createInsecureRfcommSocketToServiceRecord(myUUID);


                BluetoothAdapter.getDefaultAdapter().cancelDiscovery();

                mmServerSocket.connect();



                isBTConnectet = true;

            }
        } catch (IOException e) {

            e.printStackTrace();

        }

        if (isBTConnectet) {

            Log.d("Status", "Polaczono");
            Toast.makeText(getBaseContext(), "Polaczono", Toast.LENGTH_LONG).show();

        }


        return START_NOT_STICKY;
    }
}

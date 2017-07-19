package com.example.mattkopacz.bluetoothservices;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    TextView userInfo;

    private BluetoothAdapter mBluetoothAdapter;

    //List for storing names and MAC adresses of connected BT devices
    List<String> adress = new ArrayList<>();
    List<String> name = new ArrayList<>();

    String MACAdress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userInfo = (TextView) findViewById(R.id.userInfo);



        searchForBTAdapter();
        checkForConectedDevices();



    }


    public void onClick(View view) {

        onCreateDialog().show();

    }



    // Search for BTAdapter

    private void searchForBTAdapter(){


        // Checking for device Adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter == null) {

            // There is no device Adapter
            userInfo.setText("There is no Bluetooth Adapter on This device");

        } else {

            // If Bluetooth Adapter is not enebaled
            // Ask for anable
            if (!mBluetoothAdapter.isEnabled()){

                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);

            } else {

                // If everything is ok

                userInfo.setText("Everythig is OK!");

            }

        }

    }



    private void checkForConectedDevices() {

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

        // If there is at least one paired device

        if(pairedDevices.size() > 0) {

            // Iterate

            for (BluetoothDevice device : pairedDevices) {

                name.add(device.getName());
                adress.add(device.getAddress());

            }

        }

    }



    // Create Dialog which shows evry paird devices


    private Dialog onCreateDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);


        builder.setTitle("Paired Devices").setSingleChoiceItems(name.toArray(new String[name.size()]), 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                MACAdress = adress.get(i);

                Intent intent = new Intent(MainActivity.this, ChartActivity.class);

                intent.putExtra("SEND_ADRESS", MACAdress);

                ProgressDialog progressDialog = ProgressDialog.show(MainActivity.this, "Connecting...", "Please Wait");

                startActivity(intent);




            }
        }).create();


        return builder.create();
    }


}

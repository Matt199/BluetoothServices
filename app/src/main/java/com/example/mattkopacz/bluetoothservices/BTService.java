package com.example.mattkopacz.bluetoothservices;

import android.app.ProgressDialog;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.IntDef;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class BTService extends Service {


    private ConnectedThread mConnectedThread;
    private BluetoothAdapter btAdapter;
    private static UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private BluetoothSocket mmServerSocket;
    private boolean isBTConnectet = false;

    final int hanlderState = 0;

    private Handler handler;


    final static String MY_ACTION = "MY_ACTION";


    public StringBuilder recDataString = new StringBuilder();

    public BTService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);


                if(msg.what == hanlderState){

                    // If recive any message from thread then....

                        byte[] rBuff = (byte[]) msg.obj; // recived message (bytes)

                        String readMessage = new String(rBuff, 0, msg.arg1); // convert that message to string

                        recDataString.append(readMessage);

                        int endOfIndex = recDataString.indexOf("~");

                        if (endOfIndex > 0) {

                            String wiadomosc = recDataString.substring(1, endOfIndex);

                            Log.d("Odczyt", wiadomosc);

                            int wartoscInt = Integer.parseInt(wiadomosc);

                            Intent sendIntent = new Intent();

                            sendIntent.putExtra("SEND_DISTANCE", wartoscInt);

                            sendIntent.setAction(MY_ACTION);

                            sendBroadcast(sendIntent);

                            recDataString.delete(0, recDataString.length());


                        }
                    }

                }

        };




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


                mConnectedThread = new ConnectedThread(mmServerSocket);
                mConnectedThread.start();

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



    private class ConnectedThread extends Thread {

        private final InputStream mmInStream;


        public ConnectedThread(BluetoothSocket socket) {

            mmServerSocket = socket;

            InputStream tmp = null;


            // Get the input stream

            try {
                tmp = socket.getInputStream();
            } catch (IOException e) {

                e.printStackTrace();

            }

            mmInStream = tmp;
        }

        @Override
        public void run() {
            super.run();

            byte[] mmBuffer = new byte[256]; // bytes returned from read

            int numBytes;

            // Start listening

            while (true) {

                try {
                    numBytes = mmInStream.read(mmBuffer);

                    handler.obtainMessage(hanlderState, numBytes, -1, mmBuffer).sendToTarget();


                } catch (IOException e) {

                    e.printStackTrace();
                    Toast.makeText(getBaseContext(), "Input stream was disconnected", Toast.LENGTH_LONG).show();
                    break;

                }

            }


        }
    }

}

package com.example.mattkopacz.bluetoothservices;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.UUID;

public class ChartActivity extends AppCompatActivity {


    private BluetoothAdapter btAdapter;
    public String MACAdress;
    private UUID mUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");



    MyBroadcast myBroadcast;



    private LineGraphSeries<DataPoint> series;


    int lastXVal = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);



        Intent intent = getIntent();


        MACAdress = intent.getStringExtra("SEND_ADRESS");

        Intent serviceIntent = new Intent(getBaseContext(), BTService.class);

        serviceIntent.putExtra("MAC_ADRESS", MACAdress);

        startService(serviceIntent);

        myBroadcast = new MyBroadcast();


        IntentFilter intentFilter = new IntentFilter();

        intentFilter.addAction(BTService.MY_ACTION);

        registerReceiver(myBroadcast, intentFilter);



        // Initializing the Graph
        GraphView graphView = (GraphView) findViewById(R.id.graph);

        series = new LineGraphSeries<>(new DataPoint[]{});

        graphView.addSeries(series);

        series.setColor(Color.RED);
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(5f);


        graphView.getViewport().setBackgroundColor(Color.BLACK);

        graphView.getGridLabelRenderer().setGridColor(Color.WHITE);

        graphView.getViewport().setScalable(true);
        graphView.getViewport().setMinX(0);
        graphView.getViewport().setMaxX(50);
        graphView.getViewport().setMinY(0);
        graphView.getViewport().setMaxY(80);
        graphView.getViewport().isScalable();
        graphView.getViewport().setScalableY(true);
        graphView.getGridLabelRenderer().setVerticalAxisTitle("[cm]");
        graphView.getGridLabelRenderer().setHorizontalAxisTitle("[ms]");

        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setYAxisBoundsManual(true);








    }



    private class MyBroadcast extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {

            int recivedData = intent.getIntExtra("SEND_DISTANCE", 0);


                lastXVal++;

                series.appendData(new DataPoint(lastXVal, recivedData), true, 50);


        }
    }

}

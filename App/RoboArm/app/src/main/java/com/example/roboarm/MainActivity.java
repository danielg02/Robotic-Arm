package com.example.roboarm;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;


public class MainActivity extends AppCompatActivity {
    ToggleButton toggleConnected;
    SeekBar servo1Seek;
    SeekBar servo2Seek;
    SeekBar servo3Seek;
    SeekBar servo4Seek;
    SeekBar servo5Seek;
    SeekBar servo6Seek;
    Button run;
    Spinner menuSpinner;
    String menuChoice;

    static final UUID mUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    BluetoothSocket btSocket = null;
    BluetoothDevice hc05;
    BluetoothAdapter btAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instantiate();

        toggleConnected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onConnect(buttonView, isChecked);
            }
        });

        run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuChoice = Long.toString(menuSpinner.getSelectedItemId() + 1);
                String data = "M" + menuChoice;
                sendData(data);
            }
        });

        setBar();
        barClick(servo1Seek);
        barClick(servo2Seek);
        barClick(servo3Seek);
        barClick(servo4Seek);
        barClick(servo5Seek);
        barClick(servo6Seek);
    }

    private void instantiate() {
        toggleConnected = findViewById(R.id.toggle_connection);
        servo1Seek = findViewById(R.id.s1_bar);
        servo2Seek = findViewById(R.id.s2_bar);
        servo3Seek = findViewById(R.id.s3_bar);
        servo4Seek = findViewById(R.id.s4_bar);
        servo5Seek = findViewById(R.id.s5_bar);
        servo6Seek = findViewById(R.id.s6_bar);
        run = findViewById(R.id.run_button);
        menuSpinner = findViewById(R.id.menu_spinner);
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        hc05 = btAdapter.getRemoteDevice("00:14:03:05:09:02");

        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.menu_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        menuSpinner.setAdapter(adapter);
    }

    private void onConnect(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            int counter = 0;
            do {
                try {
                    btSocket = hc05.createRfcommSocketToServiceRecord(mUUID);
                    btSocket.connect();
                    System.out.println(btSocket.isConnected());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                counter++;
            } while (!btSocket.isConnected() && counter < 3);
        } else {
            try {
                btSocket.close();
                System.out.println(btSocket.isConnected());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void sendData(String data) {
        try {
            OutputStream outputStream = btSocket.getOutputStream();
            outputStream.write(data.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void setBar() {
        servo1Seek.setMax(180);    //0-180
        servo1Seek.setProgress(90);
        servo2Seek.setMax(70);    //100-170
        servo2Seek.setProgress(35);
        servo3Seek.setMax(90);    //0-90
        servo3Seek.setProgress(45);
        servo4Seek.setMax(180);    //0-180
        servo4Seek.setProgress(90);
        servo5Seek.setMax(180);    //0-180
        servo5Seek.setProgress(90);
        servo6Seek.setMax(80);    //45-125
        servo6Seek.setProgress(40);
    }

    void barClick(final SeekBar s) {
        s.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int servo = getServo(s);
                int position = s.getProgress();
                if (s == servo2Seek) { position += 100; }
                else if (s == servo6Seek) { position += 45; }
                String data = Integer.toString(servo) + Integer.toString(position);
                sendData(data);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    int getServo(SeekBar s) {
        if (s == servo1Seek) { return 1; }
        else if (s == servo2Seek) { return 2; }
        else if (s == servo3Seek) { return 3; }
        else if (s == servo4Seek) { return 4; }
        else if (s == servo5Seek) { return 5; }
        else if (s == servo6Seek) { return 6; }
        else { return -1; }
    }
}
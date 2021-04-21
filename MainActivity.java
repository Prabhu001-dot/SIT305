package com.example.a41;

import android.content.SharedPreferences;
import android.media.Image;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TextView lasthours ;
    TextView Clock;
    ImageButton Play;
    ImageButton Pause;
    ImageButton Stop;
    TextView Show;
    EditText Input;
    String time;
    SharedPreferences sharedPreferences;
    String inputType;
    private int seconds=0;
    private boolean running;
    Boolean wasRunning = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lasthours= findViewById(R.id.LastHours);
        Clock= findViewById(R.id.clock);
        Play = findViewById(R.id.play);
        Pause = findViewById(R.id.pause);
        Stop = findViewById(R.id.stop);
        Input=findViewById(R.id.input);
        sharedPreferences = getSharedPreferences("com.example.timer", MODE_PRIVATE);

        inputType = sharedPreferences.getString("workout", "");
        lasthours.setText("You Spent "+sharedPreferences.getString("workoutTime", "")+" on "+ inputType +" Last time");
        Input.setText(inputType);
        Clock.setText("00:00");
        
        if (savedInstanceState != null) {

            //Will be saving instance state and setting the clock.
            seconds = savedInstanceState.getInt("seconds");
            running = savedInstanceState.getBoolean("running");
            wasRunning = savedInstanceState.getBoolean("wasRunning");
            time = savedInstanceState.getString("time");
            Clock.setText(time);
            setClock();
        }


    }

    @Override
    protected void onSaveInstanceState( Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("seconds", seconds);
        outState.putBoolean("running", running);
        outState.putBoolean("wasRunning", wasRunning);
        outState.putString("time", time);

    }

    private void setClock() {
        final Handler handler = new Handler();

        handler.post(new Runnable() {
            @Override

            public void run() {
                int hours = seconds / 3600;
                int minutes = (seconds % 3600) / 60;
                int secs = seconds % 60;

                // Giving a proper format to hour, minutes and seconds.
                time = String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, secs);

                // Set up the clock.
                Clock.setText(time);

                // As soon as it will start running seconds will be start increasing.
                if (running) {
                    seconds++;
                }

                handler.postDelayed(this, 1000);
            }
        });
    }
    public void stopTimer(View view) {
        running = false;

        SharedPreferences.Editor stop = sharedPreferences.edit();
        stop.putString("workoutTime", Clock.getText().toString()).apply();
        stop.putString("workout", Input.getText().toString()).apply();


    }

    public void pauseTimer(View view) {
        super.onPause();
        wasRunning = running;
        running = false;
    }

    public void startTimer(View view) {
        running = true;
        setClock();
    }
}
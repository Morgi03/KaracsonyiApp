package hu.petrik.karacsonyiapp;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private TextView countdown;
    private Timer timer;
    private Date karacsony;
    private MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        countdown = findViewById(R.id.countdown);
        Calendar most = Calendar.getInstance();
        int ev = most.get(Calendar.YEAR);
        int honap = most.get(Calendar.MONTH);
        int nap = most.get(Calendar.DATE);
        if (honap == 11 && nap > 24) {
            ev++;
        }
        Calendar karacsony = Calendar.getInstance();
        karacsony.set(ev, 11, 25,0,0,0);
        this.karacsony = karacsony.getTime();
        player = MediaPlayer.create(this,R.raw.zene);
        player.setLooping(true);
    }


    @Override
    protected void onStart() {
        super.onStart();
        timer = new Timer();
        player.start();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Date most = Calendar.getInstance().getTime();
                long hatralevoIdo = karacsony.getTime() - most.getTime();
                long masodpercMili = 1000;
                long percMili = masodpercMili * 60;
                long oraMili = percMili * 60;
                long napMili = oraMili * 24;

                long nap = hatralevoIdo / napMili;
                hatralevoIdo %= napMili;
                long ora = hatralevoIdo / oraMili;
                hatralevoIdo %= oraMili;
                long perc = hatralevoIdo / percMili;
                hatralevoIdo %= percMili;
                long masodperc = hatralevoIdo / masodpercMili;

                String hatralevoSzoveg = String.format("%d nap %02d:%02d:%02d",nap,ora,perc,masodperc);
                runOnUiThread(() -> {
                    countdown.setText(hatralevoSzoveg);
                });
            }
        };
        timer.schedule(task, 0, 500);
    }

    @Override
    protected void onStop() {
        super.onStop();
        player.stop();
        timer.cancel();
    }
}
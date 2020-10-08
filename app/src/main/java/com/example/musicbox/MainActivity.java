package com.example.musicbox;

import androidx.appcompat.app.AppCompatActivity;

import android.icu.text.SimpleDateFormat;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private MediaPlayer media;
    private ImageView singer;
    private SeekBar seek;
    private TextView leftTime;
    private TextView rightTime;
    private Button prev;
    private Button play;
    private Button next;
    private Thread thread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUp();
        seek.setMax(media.getDuration());

        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    media.seekTo(progress);
                }
                SimpleDateFormat dateFormat=new SimpleDateFormat("MM:ss");
                int current=media.getCurrentPosition();
                int duration=media.getDuration();

                leftTime.setText(String.valueOf(dateFormat.format(new Date(current))));
                rightTime.setText(String.valueOf(dateFormat.format(new Date(duration - current))));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void setUp() {
        media=new MediaPlayer();
        media=MediaPlayer.create(getApplicationContext(),R.raw.abc);
        singer = (ImageView) findViewById(R.id.imageView3);
        seek = (SeekBar) findViewById(R.id.seekBar);
        leftTime = (TextView) findViewById(R.id.left);
        rightTime = (TextView) findViewById(R.id.right);
        prev = (Button) findViewById(R.id.prevButton);
        play = (Button) findViewById(R.id.playButton);
        next = (Button) findViewById(R.id.nextButton);
        prev.setOnClickListener(this);
        play.setOnClickListener(this);
        next.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.prevButton:
                     //code
                backM();
                     break;
            case R.id.playButton:
            {
                   if(media.isPlaying())
                       pauseM();
                   else
                       startM();}

                     break;
            case R.id.nextButton:
                  nextM();
                     break;

        }

    }
    public void pauseM()
    {
        if(media!=null){
            media.pause();
            play.setBackgroundResource(android.R.drawable.ic_media_play);}

    }
    public void startM()
    {
        if(media!=null){
            media.start();
            updateThread();
            play.setBackgroundResource(android.R.drawable.ic_media_pause);
        }


    }
    public void backM(){
        if(media.isPlaying())
            media.seekTo(0);
    }
    public void nextM(){
        if(media.isPlaying())
            media.seekTo(media.getDuration());
    }
    public void updateThread(){
        thread=new Thread(){
            @Override
            public void run() {
                try{
                    while(media!=null&&media.isPlaying()){
                        Thread.sleep(50);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                int newPosition=media.getCurrentPosition();
                                int newMax=media.getDuration();
                                seek.setMax(newMax);
                                seek.setProgress(newPosition);
                                leftTime.setText(String.valueOf(new SimpleDateFormat("MM:ss").format(new Date(media.getCurrentPosition()))));
                                rightTime.setText(String.valueOf(new SimpleDateFormat("MM:ss").format(new Date(media.getDuration() - media.getCurrentPosition()))));
                            }
                        });
                    }
                    }catch(InterruptedException e){
                    e.printStackTrace();
                }

            }
        };
        thread.start();
    }

    @Override
    protected void onDestroy() {
        if(media.isPlaying()&&media!=null)
        {
            media.stop();
            media.release();
            media=null;

        }
        thread.interrupt();
        thread=null;
        super.onDestroy();
    }
}



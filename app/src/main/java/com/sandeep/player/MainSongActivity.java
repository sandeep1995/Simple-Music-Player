package com.sandeep.player;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;

public class MainSongActivity extends AppCompatActivity implements View.OnClickListener {
    ArrayList<File> rawSongs;
    static MediaPlayer mediaPlayer;
    int position;
    SeekBar seekBar;
    Button buttonPlayPause, buttonNext, buttonPrev, buttonFastForward, buttonBackWard;
    Uri u;
    TextView playingSongText, runningTime;
    Thread controlSongThread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_song);

        seekBar = (SeekBar) findViewById(R.id.seekBar);
        buttonPlayPause = (Button) findViewById(R.id.buttonPlay);
        buttonNext = (Button) findViewById(R.id.buttonNextSong);
        buttonPrev = (Button) findViewById(R.id.buttonPreviousSong);
        buttonFastForward = (Button) findViewById(R.id.buttonFastForward);
        buttonBackWard = (Button) findViewById(R.id.buttonBackWard);
        playingSongText = (TextView) findViewById(R.id.playing_song_name);
        runningTime = (TextView) findViewById(R.id.textViewTime);

        Intent i = getIntent();
        Bundle b = i.getExtras();
        rawSongs = (ArrayList) b.getParcelableArrayList("songs");
        position = b.getInt("pos", 0); //if pos not found then 0 will be default value

        controlSongThread = new Thread(){
            @Override
            public void run() {
                int totalDuration = mediaPlayer.getDuration();
                int currentPosition = 0;
                while(currentPosition < totalDuration){
                    try {
                        sleep(1000);
                        currentPosition = mediaPlayer.getCurrentPosition();
                        Log.d("position", (Integer.toString(currentPosition/1000)));
                        seekBar.setProgress(currentPosition);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
               super.run();
            }
        };

        if(mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }

        u = Uri.parse(rawSongs.get(position).toString());
        mediaPlayer = MediaPlayer.create(getApplicationContext(), u);
        mediaPlayer.start();
        seekBar.setMax(mediaPlayer.getDuration());
        playingSongText.setText(rawSongs.get(position).getName().toString().replace(".mp3", ""));
        controlSongThread.start();



        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        buttonPlayPause.setOnClickListener(this);
        buttonNext.setOnClickListener(this);
        buttonPrev.setOnClickListener(this);
        buttonFastForward.setOnClickListener(this);
        buttonBackWard.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.buttonPlay:
                if(mediaPlayer.isPlaying()){
                    buttonPlayPause.setText(">");
                    mediaPlayer.pause();
                }else{
                    buttonPlayPause.setText("||");
                    mediaPlayer.start();
                }
                break;
            case R.id.buttonFastForward:
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + 5000);
                break;
            case R.id.buttonBackWard:
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - 5000);
                break;
            case R.id.buttonNextSong:
                mediaPlayer.stop();
                mediaPlayer.release();
                position = (position +1)%rawSongs.size();
                u = Uri.parse(rawSongs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), u);
                mediaPlayer.start();
                buttonPlayPause.setText("||");
                seekBar.setMax(mediaPlayer.getDuration());
                playingSongText.setText(rawSongs.get(position).getName().toString().replace(".mp3", ""));
                break;
            case R.id.buttonPreviousSong:
                mediaPlayer.stop();
                mediaPlayer.release();
                position = (position - 1 < 0) ? rawSongs.size() -1 : position - 1;
                u = Uri.parse(rawSongs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), u);
                mediaPlayer.start();
                buttonPlayPause.setText("||");
                seekBar.setMax(mediaPlayer.getDuration());
                playingSongText.setText(rawSongs.get(position).getName().toString().replace(".mp3", ""));
                break;
        }
    }

}

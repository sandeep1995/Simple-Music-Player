package com.sandeep.player;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    String[] songs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);
        final ArrayList<File> rawSongs = findAllSongs(Environment.getExternalStorageDirectory());
        songs = new String[rawSongs.size()];

        for(int i = 0; i< rawSongs.size(); i++){
            songs[i] = rawSongs.get(i).getName().toString().replace(".mp3", "");
        }

        ArrayAdapter<String> adp = new ArrayAdapter<String>(getApplicationContext(),R.layout.songs_layout,R.id.textViewSongName, songs);
        listView.setAdapter(adp);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getApplicationContext(),MainSongActivity.class).putExtra("songs", rawSongs).putExtra("pos",position));
            }
        });
    }

    public ArrayList<File> findAllSongs(File rootFile){
        File[] files = rootFile.listFiles();
        ArrayList<File> returnedFileList = new ArrayList<File>();
        for(File singleFile : files){
            if(singleFile.isDirectory()){
                returnedFileList.addAll(findAllSongs(singleFile));
            }
            else{
                if(singleFile.getName().endsWith(".mp3") || singleFile.getName().endsWith(".EXT") || singleFile.getName().endsWith(".aac") || singleFile.getName().endsWith(".wav") ){
                    returnedFileList.add(singleFile);
                }
            }
        }

        return  returnedFileList;
    }


}

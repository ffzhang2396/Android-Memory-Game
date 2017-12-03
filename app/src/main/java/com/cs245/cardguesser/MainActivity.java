package com.cs245.cardguesser;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.ToggleButton;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Button playButton;
    private Button toggleMusicButton;
    private Spinner choices;
    private Button hSButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addListenerButton();
        toggleMusic();
        addMusic();
        switchToHighScore();


    }

    public void switchToHighScore(){
        hSButton = findViewById(R.id.button2);
        hSButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent hSIntent = new Intent(MainActivity.this, HighScoreActivity.class);
                startActivity(hSIntent);


            }
        });
    }

    public void addListenerButton() {
        choices = findViewById(R.id.spinner);
        playButton = findViewById(R.id.button);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gameIntent = new Intent(MainActivity.this, GameActivity.class);
                gameIntent.putExtra("numberOfCards", Integer.parseInt(choices.getSelectedItem().toString()));

                Intent musicSwitch = new Intent(MainActivity.this, MusicService.class);
                musicSwitch.putExtra("song", "game");
                startService(musicSwitch);


                startActivity(gameIntent);

        }
    });
    }

    public void toggleMusic() {
        ToggleButton music = findViewById(R.id.music);
        music.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    Intent pause = new Intent(MainActivity.this, MusicService.class);
                    pause.putExtra("song", "pause");
                    startService(pause);
                } else {
                    Intent resume = new Intent(MainActivity.this, MusicService.class);
                    resume.putExtra("song", "resume");
                    startService(resume);
                }
            }
        });
    }

    public void addMusic(){
        //music plays throughout activities
        Intent music = new Intent(MainActivity.this, MusicService.class);
        startService(music);

    }



    public void onPause() {
        super.onPause();

        Intent pause = new Intent(MainActivity.this, MusicService.class);
        pause.putExtra("song", "pause");
        startService(pause);

    }

    public void onResume() {
        super.onResume();

        Intent resume = new Intent(MainActivity.this, MusicService.class);
        resume.putExtra("song", "resume");
        startService(resume);

    }











}

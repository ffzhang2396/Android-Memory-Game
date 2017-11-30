package com.cs245.cardguesser;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Button playButton;
    private Spinner choices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addListenerButton();
        addMusic();


    }



    public void addListenerButton() {
        choices = findViewById(R.id.spinner);
        playButton = findViewById(R.id.button);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gameIntent = new Intent(MainActivity.this, GameActivity.class);
                gameIntent.putExtra("numberOfCards", Integer.parseInt(choices.getSelectedItem().toString()));
                startActivity(gameIntent);
            }
        });




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







    public void addMusic(){
        //music plays throughout activities
        Intent music = new Intent(MainActivity.this, MusicService.class);
        startService(music);

    }



}

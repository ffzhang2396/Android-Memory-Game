package com.cs245.cardguesser;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.ToggleButton;

import org.json.JSONException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.Random;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Button playButton;
    private Button toggleMusicButton;
    private Spinner choices;
    private Button hSButton;
    private boolean isToggled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadScoresRaw();
        addListenerButton();
        toggleMusic();
        addMusic();
        switchToHighScore();


    }
/*
method: switchToHighScore
purpose: for high score button to switch to high score page
 */
    public void switchToHighScore(){
        hSButton = findViewById(R.id.button2);
        hSButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent hSIntent = new Intent(MainActivity.this, HighScoreActivity.class);
                hSIntent.putExtra("isToggled", isToggled);

                if (!isToggled) {
                    Intent musicSwitch = new Intent(MainActivity.this, MusicService.class);
                    musicSwitch.putExtra("song", "hScore");
                    startService(musicSwitch);
                }

                startActivity(hSIntent);

            }
        });
    }

    public void addListenerButton() {
        initSpinner();
        playButton = findViewById(R.id.button);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gameIntent = new Intent(MainActivity.this, GameActivity.class);
                gameIntent.putExtra("numberOfCards", Integer.parseInt(choices.getSelectedItem().toString()));
                gameIntent.putExtra("isToggled", isToggled);

                if (!isToggled) {


                    Intent musicSwitch = new Intent(MainActivity.this, MusicService.class);
                    musicSwitch.putExtra("song", "game");
                    startService(musicSwitch);
                }

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
                    pause.putExtra("isToggledOff", true);
                    startService(pause);
                    isToggled = true;
                } else {
                    Intent resume = new Intent(MainActivity.this, MusicService.class);
                    resume.putExtra("song", "resume");
                    startService(resume);
                    isToggled = false;
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

        if (!isToggled) {
            Intent resume = new Intent(MainActivity.this, MusicService.class);
            resume.putExtra("song", "resume");
            startService(resume);
        }

    }


    /*
    method: loadScoresRaw
    purpose: checks if highsore.txt file exists in sandbox (data/data/<packageName>)
    if not read highscore.txt file in the raw folder in resourses
     */
    public void loadScoresRaw() {
        File file = new File(getFilesDir() + "highscore.txt");
        // If text file exists then do nothing
        if (file.exists()) {

        }
        // Else If text file doesn't exist yet
        else {
            // Read file from raw folder as string
            Resources res = getResources();
            InputStream is = res.openRawResource(R.raw.highscore);
            Scanner sc = new Scanner(is);
            StringBuilder builder = new StringBuilder();

            while (sc.hasNextLine()) {
                builder.append(sc.nextLine());
            }
            try {
                // Write string to highscore.txt located in data/data/....
                FileOutputStream fileOut = null;
                fileOut = openFileOutput("highscore.txt", MODE_PRIVATE);
                OutputStreamWriter os = new OutputStreamWriter(fileOut);
                os.write(builder.toString());
                os.close();
                Log.d(TAG, "loadScoresRaw: " + "New Created file, DID NOT EXIST BEFORE");
                System.out.println("New Created file, DID NOT EXIST BEFORE\n");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void initSpinner() {
        choices = findViewById(R.id.spinner);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.num_cards, R.layout.spinner_item);
        choices.setAdapter(adapter);
    }


}

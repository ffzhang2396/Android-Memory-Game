package com.cs245.cardguesser;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class GameActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private int numberOfElements;
    private int score;
    private int numberOfMatches;
    private String [][] hScores = new String[2][5];

    private State[] states;

    private String[] usedCards;

    private String name;

    private MemoryButton selected1;
    private MemoryButton selected2;
    private MemoryButton flipAll;
    private GridView gridView;

    private ButtonAdapter buttonAdapter;

    private TextView textView;

    private Button tryAgainButton;
    private Button endGameButton;

    private boolean isDialog;

    private final String TAG = "GameActivity";

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        gridView = findViewById(R.id.gridView);
        textView = findViewById(R.id.textViewScore);
        this.numberOfElements = getIntent().getIntExtra("numberOfCards", 0);
        setTryAgainButtonClickListenter();
        setEndGameButtonClickListenter();
        if(savedInstanceState != null ) {
            score = savedInstanceState.getInt("score", 0);
            numberOfMatches = savedInstanceState.getInt("matches", 0);
            buttonAdapter = new ButtonAdapter(this, (State[]) savedInstanceState.getParcelableArray("states"));
            if (savedInstanceState.containsKey("selected1")) {
                selected1 = (MemoryButton) buttonAdapter.getItem(savedInstanceState.getInt("selected1"));
                if (savedInstanceState.containsKey("selected2")) {
                    selected2 = (MemoryButton) buttonAdapter.getItem(savedInstanceState.getInt("selected2"));
                }
            }
            isDialog = savedInstanceState.getBoolean("dialog");
            if(isDialog) {
                getName();
            }

        } else {
            initButtons();
        }

        gridView.setAdapter(buttonAdapter);
        gridView.setOnItemClickListener(this);
        textView.setText("Score: " + score);
        //initMusic();
    }

    private void initButtons() {
        states = new State[numberOfElements];
        populateUsedCards();

        for (int i = 0; i < numberOfElements; i++) {
            states[i] = new State(usedCards[i]);
        }

        buttonAdapter = new ButtonAdapter(this, states);

    }

    private void populateUsedCards() {
        String[] cardType = new String[]{"Vader", "Vader", "Luke", "Luke", "Leia", "Leia", "Han Solo",
                "Han Solo", "C3PO", "C3PO", "R2D2", "R2D2", "Chewbacca", "Chewbacca", "Rey", "Rey",
                "Finn", "Finn", "Lando", "Lando"};
        usedCards = new String[numberOfElements];
        System.arraycopy(cardType, 0, usedCards, 0, numberOfElements);

        Collections.shuffle(Arrays.asList(usedCards));

    }

    //called in the onItemClick if the game finishes
    private void highScoreStuff() {

        String num = "score" + Integer.toString(numberOfElements);
        hScores = loadScoresArray(num);

        // If score is greater than lowest highscore add it new highscore
        if(score > Integer.valueOf(hScores[1][4])){
            isDialog = true;
            getName();
            updateHSList("NAMEHERE",score);
        }else{

        }
    }
    private void updateHSList(String name, int score){
        // Create temporary array
        String tempArr [][] = new String [2][5];
        boolean inserted = false;
        for (int i = 0,x=0; i < 5; i++,x++){
            if(score > Integer.valueOf(hScores[1][i]) && !inserted){
                tempArr[0][i] = name;
                tempArr[1][i] = Integer.toString(score);
                i++;
                inserted = true;
            }
            tempArr[0][i] = hScores[0][x];
            tempArr[1][i] = hScores[1][x];
        }

        for(int n = 0;n < 5; n++){
            hScores[0][n] = tempArr[0][n];
            hScores[1][n] = tempArr[1][n];
        }

        
        for(int n = 0;n < 5; n++){
            System.out.println(hScores[0][n]);
            System.out.println(hScores[1][n]);
        }

    }

    private void overwriteJSON(){

    }

    // Read the JSON file and loads the scores returns a 2D array
    private String[][] loadScoresArray(String num_card) {
        String scores [] [] = new String[2][5];
        Resources res = getResources();
        InputStream is = res.openRawResource(R.raw.highscores);
        Scanner sc = new Scanner(is);
        StringBuilder builder = new StringBuilder();

        while (sc.hasNextLine()) {
            builder.append(sc.nextLine());
        }

        scores = parseJson(builder.toString(), num_card);
        return scores;
    }

    // Gets specific scores for the number of cards chosen
    private String [][] parseJson(String json, String type) {
        String nameScore [][] = new String[2][5];
        try {
            JSONObject root = new JSONObject(json);
            JSONArray numbers = root.getJSONArray(type);

            for (int i = 0; i < numbers.length(); i++) {
                JSONObject score = numbers.getJSONObject(i);
                nameScore[0] [i] = score.getString("Name");
                nameScore[1] [i] = score.getString("Score");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return nameScore;
    }


    private void setTryAgainButtonClickListenter() {
        tryAgainButton = findViewById(R.id.tryAgain);
        tryAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selected1 != null) {
                    selected1.flip();
                    selected1 = null;
                }
                if (selected2 != null) {
                    selected2.flip();
                    selected2 = null;
                }
            }
        });
    }

    private void setEndGameButtonClickListenter(){
        endGameButton = findViewById(R.id.endGame);
        endGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i = 0; i < numberOfElements; i++){
                    flipAll = (MemoryButton) buttonAdapter.getItem(i);
                    flipAll.setFlipped(true);
                    flipAll.setMatched();
                    flipAll.setBack();
                }


            }
        });
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        MemoryButton button = (MemoryButton) view;
//        Log.d("GameActivity", "onItemClick: " + i + " button id: " + button.getId() + " button: " + button.getCardID());
        if (selected1 == null) {
            selected1 = button;
            selected1.flip();
        } else if (selected1.equals(button)) {
            return;
        } else {
            if (selected2 == null) {
                selected2 = button;
                selected2.flip();
                if (selected1.getCardID().equals(selected2.getCardID())) {
                    score += 2;
                    numberOfMatches += 1;
                    selected1.setMatched();
                    selected2.setMatched();
                    selected1 = null;
                    selected2 = null;

                } else {
                    if (score > 0) {
                        score -= 1;
                    }
                }
                textView.setText("Score: " + score);

                if (numberOfMatches == numberOfElements / 2) { //end game condition
                    highScoreStuff();
                }

            }
        }
    }

    // example for dialog box with field for text input, yes button, cancel button
    private void getName() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Please enter your name"); 

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                name = input.getText().toString();
                Log.d(TAG, "Name :" + name);
                isDialog = false;
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                isDialog = false;
            }
        });

        builder.show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("score", score);
        outState.putInt("matches", numberOfMatches);
        outState.putParcelableArray("states", buttonAdapter.getStates());
        if (selected1 != null) {
            outState.putInt("selected1", selected1.getId());
            if (selected2 != null) {
                outState.putInt("selected2", selected2.getId());
            }
        }
        outState.putBoolean("dialog", isDialog);
    }

    public void initMusic() {
        Intent gameMusic = new Intent(GameActivity.this, MusicService.class);
        gameMusic.putExtra("song", "game");
        startService(gameMusic);
    }

    public void onDestroy() {
        super.onDestroy();

        Intent destroy = new Intent(GameActivity.this, MusicService.class);
        destroy.putExtra("song", "main");
        startService(destroy);
    }


    public void onPause() {
        super.onPause();

        Intent pause = new Intent(GameActivity.this, MusicService.class);
        pause.putExtra("song", "pause");
        startService(pause);

    }

    public void onResume() {
        super.onResume();

        Intent resume = new Intent(GameActivity.this, MusicService.class);
        resume.putExtra("song", "resume");
        startService(resume);

    }

}

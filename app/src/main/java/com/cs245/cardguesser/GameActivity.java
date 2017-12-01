package com.cs245.cardguesser;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Collections;

public class GameActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private int numberOfElements;
    private int score;
    private int numberOfMatches;

    private State[] states;

    private String[] usedCards;

    private MemoryButton selected1;
    private MemoryButton selected2;

    private GridView gridView;

    private ButtonAdapter buttonAdapter;

    private Button tryAgainButton;

    private boolean isBusy = false; // used to wait 500 ms for user to see the flipped card

    private final String TAG = "GameActivity";
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        gridView = findViewById(R.id.gridView);
        this.numberOfElements = getIntent().getIntExtra("numberOfCards", 0);
        setTryAgainButtonClickListenter();

        if(savedInstanceState != null ) {
            score = savedInstanceState.getInt("score", 0);
            numberOfMatches = savedInstanceState.getInt("matches", 0);
            buttonAdapter = new ButtonAdapter(this, (State[]) savedInstanceState.getParcelableArray("states"));
            if(savedInstanceState.containsKey("selected1")) {
                selected1 = (MemoryButton) buttonAdapter.getItem(savedInstanceState.getInt("selected1"));
                if(savedInstanceState.containsKey("selected2")) {
                    selected2 = (MemoryButton) buttonAdapter.getItem(savedInstanceState.getInt("selected2"));
                }
            }

        }
        else {
            initButtons();
        }

        gridView.setAdapter(buttonAdapter);
        gridView.setOnItemClickListener(this);
        //initMusic();
    }

    private void initButtons() {
        states = new State[numberOfElements];
        populateUsedCards();

        for(int i = 0; i < numberOfElements; i++) {
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

    //called in the onItemClickListener
    private void highScoreStuff() {

    }


    private void setTryAgainButtonClickListenter(){
        tryAgainButton = findViewById(R.id.tryAgain);
        tryAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selected1 != null) {
                    selected1.flip();
                    selected1 = null;
                }
                if(selected2 != null) {
                    selected2.flip();
                    selected2 = null;
                }
                Toast.makeText(getApplicationContext(), "Score: " + score,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        MemoryButton button = (MemoryButton) view;
//        Log.d("GameActivity", "onItemClick: " + i + " button id: " + button.getId() + " button: " + button.getCardID());
        if (selected1 == null) {
            selected1 = button;
            selected1.flip();
        }
        else if(selected1.equals(button)) {
            return;
        }
        else {
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

                    if (numberOfMatches == numberOfElements/2) { //end game condition
                        Log.d(TAG, "Game Finished");
                        highScoreStuff(); // Dis u Geri
                    }
                }
                else {
                    if (score > 0) {
                        score -= 1;
                    }
                }
            }
        }
    }

    @Override
    protected void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("score", score);
        outState.putInt("matches", numberOfMatches);
        outState.putParcelableArray("states", buttonAdapter.getStates());
        if(selected1 != null) {
            outState.putInt("selected1", selected1.getId());
            if(selected2!= null) {
                outState.putInt("selected2", selected2.getId());
            }
        }
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



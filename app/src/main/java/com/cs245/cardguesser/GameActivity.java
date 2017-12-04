package com.cs245.cardguesser;

import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Arrays;
import java.util.Collections;

public class GameActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private int numberOfElements;
    private int score;
    private int numberOfMatches;

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
    private boolean isToggled;
    private ToggleButton music;
    private boolean state;

    private final String TAG = "GameActivity";


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        isToggled = getIntent().getBooleanExtra("isToggled", false);
        Log.v(" game Activity", "" + isToggled);
        gridView = findViewById(R.id.gridView);
        textView = findViewById(R.id.textViewScore);
        this.numberOfElements = getIntent().getIntExtra("numberOfCards", 0);
        setTryAgainButtonClickListenter();
        setEndGameButtonClickListenter();
        if (savedInstanceState != null) {
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
            if (isDialog) {
                getName();
            }

        } else {
            initButtons();
        }

        gridView.setAdapter(buttonAdapter);
        gridView.setOnItemClickListener(this);
        textView.setText("Score: " + score);
        toggleMusic();
    }

    public void toggleMusic() {
        music = findViewById(R.id.gameToggleButton);

        if (isToggled) {
            music.setChecked(true);
            state = true;
        }

        music.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                if (isChecked) {
                    Intent pause = new Intent(GameActivity.this, MusicService.class);
                    pause.putExtra("song", "pause");
                    pause.putExtra("isToggledOff", true);
                    startService(pause);
                    isToggled = true;
                } else {
                    Intent resume = new Intent(GameActivity.this, MusicService.class);

                    if (isToggled && state) {
                        resume.putExtra("song", "game");
                        state = false;
                    } else {
                        resume.putExtra("song", "resume");
                    }
                    startService(resume);
                    isToggled = false;
                }
            }
        });

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
        isDialog = true;
        getName(); // should probably only display the dialog if there is a new high score or something
        // can help make another dialog if it is not a high score
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

    private void setEndGameButtonClickListenter() {
        endGameButton = findViewById(R.id.endGame);
        endGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < numberOfElements; i++) {
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


    public void onBackPressed() {
        super.onBackPressed();
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

        if (!isToggled) {
            Intent resume = new Intent(GameActivity.this, MusicService.class);
            resume.putExtra("song", "resume");
            startService(resume);
        }


    }

}

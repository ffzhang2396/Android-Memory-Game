package com.cs245.cardguesser;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Collections;

public class Game4x1Activity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private int numberOfElements;
    private int score;


    private MemoryButton[] buttons; // don't do anything with this, but thought it'd be nice to have

    private String[] cardType;
    private String[] usedCards;

    private MemoryButton selected1;
    private MemoryButton selected2;

    private Button tryAgainButton;

    private boolean isBusy = false; // used to wait 500 ms for user to see the flipped card

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game4x1);

        this.numberOfElements = getIntent().getIntExtra("numberOfCards", 0);

        buttons = new MemoryButton[numberOfElements];
        initCards();
        setTryAgainButtonClickListenter();

    }

    private void initCards() {
        populateCardType();
        populateUsedCards();

        GridView gridView = findViewById(R.id.gridView);
        gridView.setAdapter(new ButtonAdapter(this, usedCards));
        gridView.setOnItemClickListener(this);
    }

    private void populateUsedCards() {
        usedCards = new String[numberOfElements];
        for (int i = 0; i < numberOfElements; i++) {
            usedCards[i] = cardType[i];
        }

        Collections.shuffle(Arrays.asList(usedCards));

    }

    private void populateCardType() {
        cardType = new String[]{"Vader", "Vader", "Luke", "Luke", "Leia", "Leia", "Han Solo",
                "Han Solo", "C3PO", "C3PO", "R2D2", "R2D2", "Chewbacca", "Chewbacca", "Rey", "Rey",
                "Finn", "Finn", "Lando", "Lando"};
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
//                    selected1.setMatched(true);
//                    selected2.setMatched(true);
                    selected1.setEnabled(false);
                    selected2.setEnabled(false);
                    selected1 = null;
                    selected2 = null;
                }
                else {
                    if (score > 1) {
                        score -= 1;
                    }
                }
            }
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}

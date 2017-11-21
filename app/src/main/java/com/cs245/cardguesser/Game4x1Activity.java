package com.cs245.cardguesser;

import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;

import java.util.Arrays;
import java.util.Collections;

public class Game4x1Activity extends AppCompatActivity implements View.OnClickListener {

    private int numberOfElements;
    private int numberOfRows;

    private MemoryButton[] buttons; // don't do anything with this, but thought it'd be nice to have

    private String[] cardType;
    private String[] usedCards;

    private MemoryButton selected1;
    private MemoryButton selected2;

    private boolean isBusy = false; // used to wait 500 ms for user to see the flipped card
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game4x1);

        GridLayout gridLayout = findViewById(R.id.gridLayout4x1);

        this.numberOfElements = getIntent().getIntExtra("numberOfCards", 4);
        numberOfRows = numberOfElements/ 4 + 1;

        buttons = new MemoryButton[numberOfElements];

        initCards();
        initUsedCards();

        //adds each memorybutton into the grid. uses row * (# of cols) + col formula to convert ids array
        // to a 2d array representation
        for(int i = 0; i < numberOfRows; i++) {
            for(int j = 0; j < 4; j++) {
                if(i * 4 + j < numberOfElements){
                    MemoryButton btn = new MemoryButton(this, i, j, usedCards[i * 4 + j]);
                    btn.setId(View.generateViewId());
                    btn.setOnClickListener(this);
                    buttons[j] = btn;
                    gridLayout.addView(btn);
                }
            }
        }


    }

    private void initUsedCards() {
        usedCards = new String[numberOfElements];
        for(int i = 0; i < numberOfElements; i++) {
            usedCards[i] = cardType[i];
        }

        Collections.shuffle(Arrays.asList(usedCards));

    }

    private void initCards() {
        cardType = new String[]{"Vader", "Vader", "Luke", "Luke", "Leia", "Leia", "Han Solo",
                "Han Solo", "C3PO", "C3PO", "R2D2", "R2D2", "Chewbacca", "Chewbacca", "Rey", "Rey",
                "Finn", "Finn", "Lando", "Lando"};
    }

    /**
     * button listener that checks performs different actions depending on state of game
     * @param view MemoryButton object
     */
    @Override
    public void onClick(View view) {
        // if 500 ms wait time is still happening do nothing. ie if user tries to select 3 cards
        if(isBusy)
            return;

        MemoryButton button = (MemoryButton) view;

        //select first card
        if(selected1 == null) {
            selected1 = button;
            selected1.flip();
            return;
        }
        //if user tries to select same card twice
        if(selected1.getId() == button.getId())
            return;
        //if user selects two matching cards, disable the buttons
        if(selected1.getCardID().equals(button.getCardID())){
            button.flip();

            button.setMatched(true);
            selected1.setMatched(true);

            selected1.setEnabled(false);
            button.setEnabled(false);

            selected1 = null;

            return;
        }
        // default case if user selects two nonmatching cards
        else {
            selected2 = button;
            selected2.flip();
            isBusy = true;

            final Handler handler = new Handler();
            //used to delay game 500 ms as wait time for card to 'flip'
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    selected2.flip();
                    selected1.flip();
                    selected1 = null;
                    selected2 = null;
                    isBusy = false;
                }
            }, 500);

        }
    }

}

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

    private String[] ids; // need to think of better system to deal with varying sizes of boards

    private MemoryButton selected1;
    private MemoryButton selected2;

    private boolean isBusy = false; // used to wait 500 secs for cards to flip
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game4x1);

        GridLayout gridLayout = findViewById(R.id.gridLayout4x1);

        numberOfElements = 8; // hard coded number of elemnts in this version
        numberOfRows = 2; // also hard coded number of rows

        buttons = new MemoryButton[numberOfElements];

        ids = new String[]{"Dog", "Cat", "Dog", "Cat", "Horse", "Rabbit", "Horse", "Rabbit"};

        Collections.shuffle(Arrays.asList(ids));

        //adds each memorybutton into the grid. uses row * '# of cols" + col formula to convert ids array
        // to a 2d array representation
        for(int i = 0; i < numberOfRows; i++) {
            for(int j = 0; j < 4; j++) {
                MemoryButton btn = new MemoryButton(this, i, j, ids[i * 4 + j]);
                btn.setId(View.generateViewId());
                btn.setOnClickListener(this);
                buttons[j] = btn;
                gridLayout.addView(btn);
            }
        }
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

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

    private MemoryButton[] buttons;

    private String[] ids;

    private MemoryButton selected1;
    private MemoryButton selected2;

    private boolean isBusy = false;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game4x1);

        GridLayout gridLayout = findViewById(R.id.gridLayout4x1);

        numberOfElements = 8;
        numberOfRows = 2;

        buttons = new MemoryButton[numberOfElements];

        ids = new String[]{"Dog", "Cat", "Dog", "Cat", "Horse", "Rabbit", "Horse", "Rabbit"};

        Collections.shuffle(Arrays.asList(ids));

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

    @Override
    public void onClick(View view) {
        if(isBusy)
            return;
        MemoryButton button = (MemoryButton) view;

        if(button.isMatched())
            return;
        if(selected1 == null) {
            selected1 = button;
            selected1.flip();
        }
        if(selected1.getId() == button.getId())
            return;
        if(selected1.getCardID().equals(button.getCardID())){
            button.flip();

            button.setMatched(true);
            selected1.setMatched(true);

            selected1.setEnabled(false);
            button.setEnabled(false);

            selected1 = null;

            return;
        }

        else {
            selected2 = button;
            selected2.flip();
            isBusy = true;

            final Handler handler = new Handler();

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

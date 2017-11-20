package com.cs245.cardguesser;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatDrawableManager;
import android.widget.Button;
import android.widget.GridLayout;

/**
 * Created by Brandon on 11/19/2017.
 */

@SuppressLint("AppCompatCustomView")
public class MemoryButton extends Button{

    private int row;
    private int col;
    private String cardID;
    private boolean isFlipped;
    private boolean isMatched;
    private Drawable back;

    @SuppressLint("RestrictedApi")
    public MemoryButton(Context context, int row, int col, String cardID) {
        super(context);

        this.row = row;
        this.col = col;
        this.cardID = cardID;

        back = AppCompatDrawableManager.get().getDrawable(context, R.drawable.starwars);

        setBackground(back);

        GridLayout.LayoutParams tempParems = new GridLayout.LayoutParams(GridLayout.spec(row), GridLayout.spec(col));

        tempParems.width = (int) getResources().getDisplayMetrics().density * 122;
        tempParems.height = (int) getResources().getDisplayMetrics().density * 200;


        setLayoutParams(tempParems);
        
    }

    public void flip(){
        if(isMatched)
            return;
        if(isFlipped)
        {
            setBackground(back);
            setText("");
            isFlipped = false;
        }
        else{
            setBackgroundColor(Color.WHITE);
            setText(cardID);
            isFlipped = true;
        }

    }

    public boolean isFlipped() {
        return isFlipped;
    }

    public void setFlipped(boolean flipped) {
        isFlipped = flipped;
    }

    public boolean isMatched() {
        return isMatched;
    }

    public void setMatched(boolean matched) {
        isMatched = matched;
    }

    public String getCardID() {
        return cardID;
    }

    public void setCardID(String cardID) {
        this.cardID = cardID;
    }
}

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
    private int width;
    private int height;
    private String cardID;
    private boolean isFlipped;
    private boolean isMatched;
    private Drawable back;

    @SuppressLint("RestrictedApi")
    public MemoryButton(Context context, String cardID) {
        super(context);

        this.cardID = cardID;

        //sets back of card to starwars.png
        back = AppCompatDrawableManager.get().getDrawable(context, R.drawable.starwars);
        
    }

    public void flip(){

        //isFlipped = true show starwars image
        if(isFlipped)
        {
            setBackground(back);
            setText("");
            isFlipped = false;
        }
        //isFlipped = false, show cardID
        else{
            setBackgroundColor(Color.WHITE);
            setText(cardID);
            isFlipped = true;
        }

    }

    public void setBack(){
        if(!isFlipped) {
            setBackground(back);
        }
        else {
            setBackgroundColor(Color.WHITE);
            setText(cardID);
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

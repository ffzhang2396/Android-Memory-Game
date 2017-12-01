package com.cs245.cardguesser;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.AppCompatDrawableManager;
import android.widget.Button;

/**
 * Created by Brandon on 11/19/2017.
 */

@SuppressLint("AppCompatCustomView")
public class MemoryButton extends Button {

    private State state;

    @SuppressLint("RestrictedApi")
    public MemoryButton(Context context, String cardID) {
        super(context);
        this.state = new State();
        state.setCardID(cardID);
    }

    public MemoryButton(Context context, State state) {
        super(context);
        this.state = state;
    }


    public void flip() {
        state.setFlipped(!state.isFlipped());
        setBack();

    }

    public void setBack() {
        if (!state.isFlipped()) {
            setBackgroundResource(R.drawable.starwars);
            setText("");
        } else {
            setBackgroundColor(Color.WHITE);
            setText(state.getCardID());
        }

        setEnabled(!state.isMatched());
    }

    public void setMatched() {
        state.setMatched(true);
        setEnabled(false);

    }

    public boolean isFlipped() {
        return state.isFlipped();
    }

    public void setFlipped(boolean flipped) {
        state.setFlipped(flipped);
    }

    public boolean isMatched() {
        return state.isMatched();
    }

    public void setMatched(boolean matched) {
        state.setMatched(matched);
    }

    public String getCardID() {
        return state.getCardID();
    }

    public void setCardID(String cardID) {
        this.state.setCardID(cardID);
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
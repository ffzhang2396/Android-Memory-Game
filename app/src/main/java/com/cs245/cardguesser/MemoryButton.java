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
public class MemoryButton extends Button{

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



    public void flip(){

        //isFlipped = true show starwars image
        if(state.isFlipped())
        {
            setBackgroundResource(R.drawable.starwars);
            setText("");
            state.setFlipped(false);
        }
        //isFlipped = false, show cardID
        else{
            setBackgroundColor(Color.WHITE);
            setText(state.getCardID());
            state.setFlipped(true);
        }

    }

    public void setBack(){
        if(!state.isFlipped()) {
            setBackgroundResource(R.drawable.starwars);
        }
        else {
            setBackgroundColor(Color.WHITE);
            setText(state.cardID);
        }
    }

    public void setEnabled(){
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

    private class State implements Parcelable {
        private String cardID;
        private boolean isFlipped;
        private boolean isMatched;

        public String getCardID() {
            return cardID;
        }

        public void setCardID(String cardID) {
            this.cardID = cardID;
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

        public State() {

        }

        @SuppressLint("RestrictedApi")
        protected State(Parcel in) {
            cardID = in.readString();
            isFlipped = in.readByte() != 0;
            isMatched = in.readByte() != 0;

        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(cardID);
            dest.writeByte((byte) (isFlipped ? 1 : 0));
            dest.writeByte((byte) (isMatched ? 1 : 0));
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public final Creator<State> CREATOR = new Creator<State>() {
            @Override
            public State createFromParcel(Parcel in) {
                return new State(in);
            }

            @Override
            public State[] newArray(int size) {
                return new State[size];
            }
        };
    }
}

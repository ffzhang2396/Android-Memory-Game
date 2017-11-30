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

    private String cardID;
    private boolean isFlipped;
    private boolean isMatched;
    private Drawable back;

    @SuppressLint("RestrictedApi")
    public MemoryButton(Context context, String cardID) {
        super(context);

        this.cardID = cardID;
    }



    public void flip(){

        //isFlipped = true show starwars image
        if(isFlipped)
        {
            setBackgroundResource(R.drawable.starwars);
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
            setBackgroundResource(R.drawable.starwars);
        }
        else {
            setBackgroundColor(Color.WHITE);
            setText(cardID);
        }
    }

    public void setEnabled(){
        setEnabled(!isMatched);
    }

    public void setMatched() {
        isMatched = true;
        setEnabled(false);

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

    private static class State implements Parcelable {
        private String cardID;
        private boolean isFlipped;
        private boolean isMatched;
        private Drawable back;


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

        public static final Creator<State> CREATOR = new Creator<State>() {
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

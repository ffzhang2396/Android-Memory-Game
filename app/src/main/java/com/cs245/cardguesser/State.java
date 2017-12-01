package com.cs245.cardguesser;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Brandon-PC on 11/30/2017.
 */

public class State implements Parcelable {
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
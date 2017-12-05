/** *************************************************************
 * file: State.java
 * author: Brandon Nguyen, Charly Dang, Colin Koo, Felix Zhang, Gerianna Geminiano
 * class: CS 245 – Programming Graphical User Interface
 *
 * assignment: Android App
 * date last modified: 12/5/17
 *
 * This App is a concentration game. The user is able to select cards to be flipped
 * and also toggle the playback of music. The top 3 high scores from each type of
 * board are also saved
 *
 *************************************************************** */
package com.cs245.cardguesser;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;



public class State implements Parcelable {
    private String cardID;
    private boolean isFlipped;
    private boolean isMatched;
    private int drawable;

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

    public State(String cardID) {
        this.cardID = cardID;
    }

    /*

    purpose: handles Parcelable object creation
     */
    @SuppressLint("RestrictedApi")
    protected State(Parcel in) {
        cardID = in.readString();
        isFlipped = in.readByte() != 0;
        isMatched = in.readByte() != 0;
        drawable = in.readInt();
    }

    public boolean equals(State state) {
        if (state.getCardID().equals(cardID) && state.isFlipped == this.isFlipped && state.isMatched() == this.isMatched) {
            return true;
        }
        else {
            return false;
        }
    }
    /*
    method: writeToParcel
    purpose: handles parcel creation
     */

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(cardID);
        dest.writeByte((byte) (isFlipped ? 1 : 0));
        dest.writeByte((byte) (isMatched ? 1 : 0));
        dest.writeInt(drawable);
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

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }
}

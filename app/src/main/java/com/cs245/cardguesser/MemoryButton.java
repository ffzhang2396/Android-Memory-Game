/** *************************************************************
 * file: MemoryButton.java
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
import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.widget.Button;

/**
 * Created by Brandon on 11/19/2017.
 */

@SuppressLint("AppCompatCustomView")
public class MemoryButton extends Button {

    private State state;
    private Context context;

    public MemoryButton(Context context, State state) {
        super(context);
        this.state = state;
        this.context = context;
    }

    /*
    method: flip
    purpose: flips card depending on the state
     */
    public void flip() {
        state.setFlipped(!state.isFlipped());
        setBack();

    }
    /*
    method: setBack
    purpose: sets the view of the card
     */
    public void setBack() {
        if (!state.isFlipped()) {
            setBackgroundResource(R.drawable.starwars);
            //            setText("");
        } else {
//            setBackgroundColor(Color.WHITE);
//            setText(state.getCardID());
            setBackgroundResource(state.getDrawable());
        }

        setEnabled(!state.isMatched());
    }

    /*
    method: setMatched
    purpose: disables button if the card is matched
     */
    public void setMatched() {
        state.setMatched(true);
        setEnabled(false);

    }

    /*
    method: setEnabled
    purpose: if card is a match, disable button and grayscale the image
     */
    @Override
    public void setEnabled(boolean isEnabled) {
        super.setEnabled(isEnabled);
        if(!isEnabled) {
            Drawable originalIcon = context.getResources().getDrawable(state.getDrawable());
            Drawable icon = isEnabled ? originalIcon : convertDrawableToGrayScale(originalIcon);
            setBackground(icon);
        }
    }
    /*
    method: convertDrawableToGrayScale
    purpose: grayscales image
     */
    private Drawable convertDrawableToGrayScale(Drawable icon) {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);

        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        icon.mutate();
        icon.setColorFilter(filter);

        return icon;
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

    public void setDrawable(int drawable) {
        state.setDrawable(drawable);
    }
}

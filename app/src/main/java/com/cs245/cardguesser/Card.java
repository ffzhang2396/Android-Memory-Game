package com.cs245.cardguesser;

import android.widget.ImageView;

/**
 * Created by FelixZhang on 11/14/2017.
 */

public class Card {

    private boolean isFlipped;
    private ImageView front, back;
    private int x, y;


    public Card(ImageView pic) {
        this.front = pic;

    }



}

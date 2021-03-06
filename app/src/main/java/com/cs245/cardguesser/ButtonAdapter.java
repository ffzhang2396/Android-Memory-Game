/** *************************************************************
 * file: ButtonAdapter.java
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
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import java.util.HashMap;
import java.util.Map;

 /*
    Used to display MemoryButtons in GridView
 */

public class ButtonAdapter extends BaseAdapter {
    private Context context;
    private MemoryButton[] memoryButtons;
    private State[] states;

    private Map<String, Integer> cardBacks;

    public ButtonAdapter(Context context, State[] states) {
        this.context = context;
        this.states = states;
        memoryButtons = new MemoryButton[states.length];
        for (int i = 0; i < states.length; i++) {
            memoryButtons[i] = new MemoryButton(context, states[i]);
        }
        initCardBacks();
    }

    /*
    method: initCardBacks
    purpose: sets up HashMap to assign cardType to correct picture
    */
    private void initCardBacks(){
        cardBacks = new HashMap<>();
        cardBacks.put("Vader", R.drawable.vader);
        cardBacks.put("Luke", R.drawable.luke);
        cardBacks.put("Leia", R.drawable.leia);
        cardBacks.put("Han Solo", R.drawable.hansolo);
        cardBacks.put("C3PO", R.drawable.c3po);
        cardBacks.put("R2D2", R.drawable.r2d2);
        cardBacks.put("Chewbacca", R.drawable.chewy);
        cardBacks.put("Rey", R.drawable.rey);
        cardBacks.put("Finn", R.drawable.finn);
        cardBacks.put("Lando", R.drawable.lando);
    }

    @Override
    public int getCount() {
        return states.length;
    }

    @Override
    public Object getItem(int i) {
        return memoryButtons[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    /*
    method: getView
    purpose: sets up MemoryButton to be used by gridview
    */

    @SuppressLint("RestrictedApi")
    public View getView(int i, View view, final ViewGroup parent) {
        MemoryButton button;
        final int b = i;
//            Log.d("ButtonAdapter", "getView: create view " + i + " CardID " + states[i].getCardID());
        button = memoryButtons[i];
        //perfect width of card to fit size of gridview
        int width = (parent.getWidth() - (5 * ((GridView) parent).getNumColumns())) / ((GridView) parent).getNumColumns();
        //picture is 1.7 times taller than it is wide
        int height = (int) (width * 1.7);
        button.setLayoutParams(new GridView.LayoutParams(width, height));
        button.setPadding(5, 5, 5, 5);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((GridView) parent).performItemClick(view, b, 0);
            }
        });
        button.setId(i);
        button.setDrawable(cardBacks.get(button.getCardID()));
        button.setBack();
        return button;
    }

    public void setStates(State[] states) {
        this.states = states;
    }

    public State[] getStates() {
        return states;
    }
}

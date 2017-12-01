package com.cs245.cardguesser;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

/**
 * Created by Brandon on 11/21/2017.
 */

public class ButtonAdapter extends BaseAdapter {
    private Context context;
    private MemoryButton[] memoryButtons;
    private State[] states;

    public ButtonAdapter(Context context, MemoryButton[] memoryButtons) {
        this.context = context;
        this.memoryButtons = memoryButtons;
        states = new State[memoryButtons.length];
        for (int i = 0; i < memoryButtons.length; i++) {
            states[i] = memoryButtons[i].getState();
        }
    }

    public ButtonAdapter(Context context, State[] states) {
        this.context = context;
        this.states = states;
        memoryButtons = new MemoryButton[states.length];
        for (int i = 0; i < states.length; i++) {
            memoryButtons[i] = new MemoryButton(context, states[i]);
        }

    }

    @Override
    public int getCount() {
        return states.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint("RestrictedApi")
    public View getView(int i, View view, final ViewGroup parent) {
        MemoryButton button;
        final int b = i;
        Log.d("ButtonAdapter", "getView: create view " + i + " CardID " + states[i].getCardID());
        button = memoryButtons[i];
        int width = (parent.getWidth() - (5 * ((GridView)parent).getNumColumns()) )/ ((GridView)parent).getNumColumns() ;
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

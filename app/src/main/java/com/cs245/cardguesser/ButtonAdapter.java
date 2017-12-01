package com.cs245.cardguesser;

import android.annotation.SuppressLint;
import android.content.Context;
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
        for(int i = 0; i < memoryButtons.length; i++) {
            states[i] = memoryButtons[i].getState();
        }
    }
    @Override
    public int getCount() {
        return memoryButtons.length;
    }

    @Override
    public Object getItem(int i) {
        return memoryButtons[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint("RestrictedApi")
    public View getView(final int i, View view, final ViewGroup parent) {
        MemoryButton button;
        if (view == null) {
            button = new MemoryButton(context, "");
            button.setState(states[i]);
            button.setLayoutParams(new GridView.LayoutParams(170,330));
            button.setPadding(5, 5, 5, 5);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((GridView) parent).performItemClick(view, i, 0);
                }
            });

            button.setBack();
        } else {
            button = (MemoryButton) view;
        }

        return button;
    }

    public void setStates(State[] states) {
        this.states = states;
    }

    public State[] getStates(){
        return states;
    }
}

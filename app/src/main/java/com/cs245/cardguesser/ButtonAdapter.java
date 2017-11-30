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
    
    public ButtonAdapter(Context context, MemoryButton[] memoryButtons) {
        this.context = context;
        this.memoryButtons = memoryButtons;
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
            button = new MemoryButton(context, memoryButtons[i].getCardID());
            button.setLayoutParams(new GridView.LayoutParams(170,330));
            button.setPadding(5, 5, 5, 5);
        } else {
            button = (MemoryButton) view;
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((GridView) parent).performItemClick(view, i, 0);
            }
        });

        button.setBack();
        return button;
    }

    public void setMemoryButtons(MemoryButton[] memoryButtons) {
        this.memoryButtons = memoryButtons;
    }
}

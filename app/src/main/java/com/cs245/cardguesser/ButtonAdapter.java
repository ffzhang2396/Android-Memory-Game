package com.cs245.cardguesser;

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
    private String[] buttonIDs;
    
    public ButtonAdapter(Context context, String[] buttonIDs) {
        this.context = context;
        this.buttonIDs = buttonIDs;
    }
    @Override
    public int getCount() {
        return buttonIDs.length;
    }

    @Override
    public Object getItem(int i) {
        return buttonIDs[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, final ViewGroup parent) {
        MemoryButton button;
        if (view == null) {
            button = new MemoryButton(context, buttonIDs[i]);
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
        return button;
    }
}

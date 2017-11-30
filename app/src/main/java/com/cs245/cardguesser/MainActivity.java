package com.cs245.cardguesser;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    private Button playButton;
    private Spinner choices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addListenerButton();
        addMusic();


    }


    public void addListenerButton() {
        choices = findViewById(R.id.spinner);
        playButton = findViewById(R.id.button);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), GameActivity.class);
                intent.putExtra("cards", choices.getSelectedItemId());
                Intent intent2 = new Intent(MainActivity.this, GameActivity.class);
                intent2.putExtra("numberOfCards", Integer.parseInt(choices.getSelectedItem().toString()));
                startActivity(intent2);
            }
        });




    }

    public void addMusic(){
        //music plays throughout activities
        startService(new Intent(MainActivity.this, MusicService.class));

    }



}

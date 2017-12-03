package com.cs245.cardguesser;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Spinner;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.Scanner;



public class HighScoreActivity extends AppCompatActivity {

    private Spinner number;
    private Button backButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        highScoreType();
        backToMain();

    }
    public void backToMain(){
        backButton = findViewById(R.id.button3);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bIntent = new Intent(HighScoreActivity.this, MainActivity.class);
                startActivity(bIntent);
            }
        });
    }

    public void highScoreType() {
        number = findViewById(R.id.spinner3);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(HighScoreActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.num_cards));

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        number.setAdapter(adapter);
        number.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    loadScores("score4");
                } else if (i == 1) {
                    loadScores("score6");
                }else if (i == 2) {
                    loadScores("score8");
                } else if (i == 3) {
                    loadScores("score10");
                } else if (i == 4) {
                    loadScores("score12");
                } else if (i == 5) {
                    loadScores("score14");
                } else if (i == 6) {
                    loadScores("score16");
                } else if (i == 7) {
                    loadScores("score18");
                } else if (i == 8) {
                    loadScores("score20");
                } else{
                    loadScores("score20");
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                loadScores("score4");
            }
        });

    }


    public void loadScores(String num_card) {
        Resources res = getResources();
        InputStream is = res.openRawResource(R.raw.highscores);
        Scanner sc = new Scanner(is);
        StringBuilder builder = new StringBuilder();

        while (sc.hasNextLine()) {
            builder.append(sc.nextLine());
        }

        parseJson(builder.toString(), num_card);
    }


    public void parseJson(String json, String type) {
        StringBuilder builder = new StringBuilder();
        try {
            JSONObject root = new JSONObject(json);
            JSONArray numbers = root.getJSONArray(type);

            for (int i = 0; i < numbers.length(); i++) {
                JSONObject score = numbers.getJSONObject(i);
                builder.append(score.getString("Name")).append(" .... ").append(score.getString("Score")).append("\n");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        TextView txtDisplay = findViewById(R.id.textView3);
        txtDisplay.setText(builder.toString());
    }
}

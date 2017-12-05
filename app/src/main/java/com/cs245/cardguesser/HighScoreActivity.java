package com.cs245.cardguesser;

import android.content.Intent;
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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;



public class HighScoreActivity extends AppCompatActivity {

    private Spinner number;
    private Button backButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        highScoreType();
        backToMain();

    }
    /*
    method: backToMain
    purpose: back button to switch back to main menu
     */
    public void backToMain(){
        backButton = findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bIntent = new Intent(HighScoreActivity.this, MainActivity.class);
                startActivity(bIntent);
            }
        });
    }

    /*
    method: highScoreType
    purpose: uses spinner and gets the type of highscore to display
    by calling loadScores and passing the type of highscore seleceted
     */
    public void highScoreType() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(HighScoreActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.num_cards));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        number.setAdapter(adapter);
        initSpinner();
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

    /*
    method: loadScores
    purpose: reads highscore text file located in data/data/<packageName> and stores it as a string s
    then passes it to parseJSON function to be read as a JSON object
    parameter: num_card: number of cards for the game
     */
    public void loadScores(String num_card) {
        try{
            // Read highscore.txt
            FileInputStream fileIn=openFileInput("highscore.txt");
            InputStreamReader InputRead= new InputStreamReader(fileIn);
            char[] inputBuffer= new char[100];
            String s="";
            int charRead;
            while ((charRead=InputRead.read(inputBuffer))>0) {
                // char to string conversion
                String readstring=String.copyValueOf(inputBuffer,0,charRead);
                s +=readstring;
            }
            // Parse string as JSON
            parseJSON(s, num_card);} catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /*
    method: parseJSON
    purpose: take in a string json and parse it into JSON objects
    parameter: json: file read as string
               type: JSON object 'score4', 'score6' etc
     */
    public void parseJSON(String json, String type) {
        StringBuilder builder = new StringBuilder();
        try {
            // { }
            JSONObject root = new JSONObject(json);
            // 'score4' , 'score6' or 'score8' etc object
            JSONArray numbers = root.getJSONArray(type);
            // reads scores in that object
            for (int i = 0; i < numbers.length(); i++) {
                JSONObject score = numbers.getJSONObject(i);
                builder.append(score.getString("Name")).append(" .... ").append(score.getString("Score")).append("\n");
            }

        }  catch (JSONException e) {
            e.printStackTrace();
        }
        // Displays the scores to highscore window
        TextView txtDisplay = findViewById(R.id.scores);
        txtDisplay.setText(builder.toString());
    }

    private void initSpinner() {
        number = findViewById(R.id.spinner);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.num_cards, R.layout.spinner_item);
        number.setAdapter(adapter);
    }
}

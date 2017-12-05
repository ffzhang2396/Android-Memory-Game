package com.cs245.cardguesser;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import android.widget.ToggleButton;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class GameActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private int numberOfElements;
    private int score;
    private int numberOfMatches;
    private String [][] hScores = new String[2][5];
    private State[] states;
    private String[] usedCards;
    private String name;
    private String strScore;

    private MemoryButton selected1;
    private MemoryButton selected2;
    private MemoryButton flipAll;
    private GridView gridView;

    private ButtonAdapter buttonAdapter;

    private TextView textView;

    private Button tryAgainButton, newGameButton, backButton, endGameButton;

    private boolean isDialog;
    private boolean isHighScore;
    private boolean isToggled;
    private ToggleButton music;
    private boolean state;

    private final String TAG = "GameActivity";


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        isToggled = getIntent().getBooleanExtra("isToggled", false);
        Log.v(" game Activity", "" + isToggled);
        gridView = findViewById(R.id.gridView);
        textView = findViewById(R.id.textViewScore);
        this.numberOfElements = getIntent().getIntExtra("numberOfCards", 0);
        setTryAgainButtonClickListenter();
        setEndGameButtonClickListenter();
        setNewGameButtonClickListenter();
        setBackButtonClickListener();

        if(savedInstanceState != null ) {
            score = savedInstanceState.getInt("score", 0);
            numberOfMatches = savedInstanceState.getInt("matches", 0);
            buttonAdapter = new ButtonAdapter(this, (State[]) savedInstanceState.getParcelableArray("states"));
            if (savedInstanceState.containsKey("selected1")) {
                selected1 = (MemoryButton) buttonAdapter.getItem(savedInstanceState.getInt("selected1"));
                if (savedInstanceState.containsKey("selected2")) {
                    selected2 = (MemoryButton) buttonAdapter.getItem(savedInstanceState.getInt("selected2"));
                }
            }

            isDialog = savedInstanceState.getBoolean("dialog");
            if (isDialog) {
                getName();
            }

        }
        else {
            initButtons();
        }

        gridView.setAdapter(buttonAdapter);
        gridView.setOnItemClickListener(this);
        textView.setText("Score: " + score);
        toggleMusic();
    }

    /*
    Button listener for the toggle button to play music or not.
     */
    public void toggleMusic() {
        music = findViewById(R.id.gameToggleButton);

        if (isToggled) {
            music.setChecked(true);
            state = true;
        }

        music.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                if (isChecked) {
                    Intent pause = new Intent(GameActivity.this, MusicService.class);
                    pause.putExtra("song", "pause");
                    pause.putExtra("isToggledOff", true);
                    startService(pause);
                    isToggled = true;
                } else {
                    Intent resume = new Intent(GameActivity.this, MusicService.class);

                    if (isToggled && state) {
                        resume.putExtra("song", "game");
                        state = false;
                    } else {
                        resume.putExtra("song", "resume");
                    }
                    startService(resume);
                    isToggled = false;
                }
            }
        });

    }


    private void initButtons() {
        states = new State[numberOfElements];
        populateUsedCards();

        for (int i = 0; i < numberOfElements; i++) {
            states[i] = new State(usedCards[i]);
        }

        buttonAdapter = new ButtonAdapter(this, states);

    }

    private void populateUsedCards() {
        String[] cardType = new String[]{"Vader", "Vader", "Luke", "Luke", "Leia", "Leia", "Han Solo",
                "Han Solo", "C3PO", "C3PO", "R2D2", "R2D2", "Chewbacca", "Chewbacca", "Rey", "Rey",
                "Finn", "Finn", "Lando", "Lando"};
        usedCards = new String[numberOfElements];
        System.arraycopy(cardType, 0, usedCards, 0, numberOfElements);

        Collections.shuffle(Arrays.asList(usedCards));
    }

    //called in the onItemClick if the game finishes
    /*
    method: highScoreStuff
    purpose: called when game ends and checks if user has new highscore
     */
    private void highScoreStuff() {

        strScore = "score" + Integer.toString(numberOfElements);
        // Get current highscores from text file
        hScores = loadScoresArray(strScore);

        // If score is greater than lowest highscore add it to new highscore
        if(score > Integer.valueOf(hScores[1][2])){
            isDialog = true;
            isHighScore = true;
        }
        // Display dialog box showing score and "sorry"
        else{
            isHighScore = false;
        }

        // Display dialog box asking for name
        getName();

    }

    /*
    method: updateHSList
    purpose: updates hScores array with new highscore added in
     */
    private void updateHSList(String name, int score){
        // Create temporary array
        String tempArr [][] = new String [2][3];
        boolean inserted = false;
        // Copies hScores array while inserting new highscore in correct place (inside temp array)
        for (int i = 0,x=0; i < 3; i++,x++){
            if(score > Integer.valueOf(hScores[1][i]) && !inserted){
                tempArr[0][i] = name;
                tempArr[1][i] = Integer.toString(score);
                i++;
                inserted = true;
            }
            if (i < 3) {
                tempArr[0][i] = hScores[0][x];
                tempArr[1][i] = hScores[1][x];
            }
        }

        // Copy temp array to highscores array
        for(int n = 0;n < 3; n++){
            hScores[0][n] = tempArr[0][n];
            hScores[1][n] = tempArr[1][n];
        }

        // DEBUGGING PURPOSES
        // Prints out new highscores list
        // THIS ARRAY IS CORRECT
        for(int n = 0;n < 3; n++){
            System.out.println(hScores[0][n]);
            System.out.println(hScores[1][n]);
        }

        // Writes highscore array to file
        writeToFile();

    }

    /*
    method: writeToFile
    purpose: writes highscores 2D array (hScores) to highscore text file
     */
    private void writeToFile(){

        String num = "score" + Integer.toString(numberOfElements);
        try {
            // Gets current highscore file and reads it
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

            // DEBUGGING PURPOSES
            // Prints out text file as string
            System.out.println(s);

            // Coverts file to JSON
            JSONObject root = new JSONObject(s);
            JSONArray numbers = root.getJSONArray(num);

            // Updates JSON object for the specific type of highscores
            // and replaces values with that of the updated hScores array
            for (int i = 0; i < numbers.length(); i++) {
                JSONObject score = numbers.getJSONObject(i);
                score.put("Name",hScores[0][i]);
                score.put("Score",hScores[1][i]);
            }

            // DEBUGGING PURPOSES
            // Prints out new highscores string to be written to text file
            // THIS STRING IS CORRECT
            System.out.println(root);

            // Delete contents of file
            PrintWriter pw = new PrintWriter(new FileWriter(getFilesDir() + "highscore.txt"));
            pw.close();

            // SHOULD WRITE THE UPDATED HIGHSCORES TO TEXT FILE
            // BUT COULD BE A PROBLEM HERE OR IN PRINT WRITER
            // GETTING A WRITE EXECEPTION ERROR
            FileOutputStream fileOut = openFileOutput("highscore.txt", MODE_PRIVATE);
            OutputStreamWriter os = new OutputStreamWriter(fileOut);
            os.write(root.toString());
            os.close();
            System.out.println("Created/Updated file\n");

            System.out.println(s);
        } catch (JSONException e) {
            e.printStackTrace();
        }catch(IOException e){
            System.out.print("Write Exception\n");
            e.printStackTrace();;
        }

    }

    /*
    method: loadScoresArray
    purpose: reads the highscore.txt file as stores it as a string s
    parameters: num_card: number of cards in game
    returns: 2D array containing top 3 highscores (not including new one)
     */
    private String[][] loadScoresArray(String num_card) {
        String scores [] [] = new String[2][3];
        try{
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

            // Parse string as JSON file, returns scores array
            scores = parseJson(s, num_card);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return scores;
    }

    /*
    method: parseJSON
    purpose: gets file string json and gets the highscores for that number of cards
    parameters: json: highscore.txt represented as string
                type: number of cards
    returns: the 2D array containing the top 3 highscores and names
     */
    private String [][] parseJson(String json, String type) {
        String nameScore [][] = new String[2][3];
        try {
            JSONObject root = new JSONObject(json);
            JSONArray numbers = root.getJSONArray(type);

            for (int i = 0; i < numbers.length(); i++) {
                JSONObject score = numbers.getJSONObject(i);
                nameScore[0] [i] = score.getString("Name");
                nameScore[1] [i] = score.getString("Score");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return nameScore;
    }


    private void setTryAgainButtonClickListenter() {
        tryAgainButton = findViewById(R.id.tryAgain);
        tryAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selected1 != null) {
                    selected1.flip();
                    selected1 = null;
                }
                if (selected2 != null) {
                    selected2.flip();
                    selected2 = null;
                }
            }
        });
    }

    private void setEndGameButtonClickListenter() {
        endGameButton = findViewById(R.id.endGame);
        endGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < numberOfElements; i++) {
                    flipAll = (MemoryButton) buttonAdapter.getItem(i);
                    flipAll.setFlipped(true);
                    flipAll.setMatched();
                    flipAll.setBack();
                }


            }
        });
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        MemoryButton button = (MemoryButton) view;
//        Log.d("GameActivity", "onItemClick: " + i + " button id: " + button.getId() + " button: " + button.getCardID());
        if (selected1 == null) {
            selected1 = button;
            selected1.flip();
        } else if (selected1.equals(button)) {
            return;
        } else {
            if (selected2 == null) {
                selected2 = button;
                selected2.flip();
                if (selected1.getCardID().equals(selected2.getCardID())) {
                    score += 2;
                    numberOfMatches += 1;
                    selected1.setMatched();
                    selected2.setMatched();
                    selected1 = null;
                    selected2 = null;

                } else {
                    if (score > 0) {
                        score -= 1;
                    }
                }
                textView.setText("Score: " + score);

                if (numberOfMatches == numberOfElements / 2) { //end game condition
                    highScoreStuff();
                }

            }
        }
    }
    // activity_game land and reg have all ids camel case except newgame?
    // implement using ButtonAdapter?
    private void setNewGameButtonClickListenter(){
        newGameButton = findViewById(R.id.NewGame);
        newGameButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    finish();
                    startActivity(getIntent());
                }
            }
        );
    }
    private void setBackButtonClickListener(){
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(GameActivity.this, MainActivity.class);
                    finish();
                    startActivity(i);
                }
            }
        );
    }

    // example for dialog box with field for text input, yes button, cancel button
    private void getName() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String strScore;
        String msg;
        if (isHighScore){
            strScore = "Congrats you got a highscore." + "  Score: " + Integer.toString(score) + "\n";
            msg = "Please enter your name.";
        }else{
            strScore = "Sorry you didn't get a highscore." + "  Score: " + Integer.toString(score) + "\n";
            msg = "Please enter your name anyway.";
        }
        builder.setTitle(strScore);
        builder.setMessage(msg);



        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                name = input.getText().toString();
                Log.d(TAG, "Name :" + name);
                // Gets the name and adds it to the highscore list with the score
                updateHSList(name,score);
                isDialog = false;
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                isDialog = false;
            }
        });

        builder.show();
    }

    /*
    method: notHighScore()
    purpose: shows dialog box after user completes the game but their score is not a highscore
    displays their score and says sorry, then button to exit dialog box
     */
    private void notHighScore() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        String strScore = "Score: " + Integer.toString(score) + "\n Sorry you didn't get a highscore.";
        builder1.setMessage(strScore);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("score", score);
        outState.putInt("matches", numberOfMatches);
        outState.putParcelableArray("states", buttonAdapter.getStates());
        if (selected1 != null) {
            outState.putInt("selected1", selected1.getId());
            if (selected2 != null) {
                outState.putInt("selected2", selected2.getId());
            }
        }
        outState.putBoolean("dialog", isDialog);
    }


/*
Method override in order to facilitate the back button pressed in order to make sure
the music is changing.
 */
    public void onBackPressed() {
        super.onBackPressed();
        Intent destroy = new Intent(GameActivity.this, MusicService.class);
        destroy.putExtra("song", "main");
        startService(destroy);
    }

    /*
    function override to make sure the music stops playing when the appe enters the background.
     */
    public void onPause() {
        super.onPause();

        Intent pause = new Intent(GameActivity.this, MusicService.class);
        pause.putExtra("song", "pause");
        startService(pause);

    }

    /*
    Function override to make sure the music starts playing again when the app enters the foreground.
     */
    public void onResume() {
        super.onResume();

        if (!isToggled) {
            Intent resume = new Intent(GameActivity.this, MusicService.class);
            resume.putExtra("song", "resume");
            startService(resume);
        }


    }

}

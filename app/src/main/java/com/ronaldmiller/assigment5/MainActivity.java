package com.ronaldmiller.assigment5;

import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Random;

public class MainActivity extends AppCompatActivity {


    private TextView scrambled_text; // View to display Scrambled Text
    private EditText input; // User Input

    private Chronometer chronometer; // Game Clock

    private boolean isGameRunning = false;

    private String randomWord, scrambledWord; // random word and it's disguised counterpart

    private RequestQueue queue; // Will hold requests that are sent out
    String url ="http://randomword.setgetgo.com/get.php"; // uses the random word api from setgetgo.com
    StringRequest getRandomWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scrambled_text = (TextView) findViewById(R.id.scrambled_text);
        input = (EditText) findViewById(R.id.user_input);

        chronometer = (Chronometer) findViewById(R.id.time_text);
        chronometer.setFormat("Time: %s");

        // Instantiate the RequestQueue.
        queue = Volley.newRequestQueue(this);

        // Request a string response from the provided URL.
        getRandomWord = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        randomWord = response;
                        scrambledWord = getScrambledWord(randomWord);
                        scrambled_text.setText(scrambledWord);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(), "That didn't work!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void startGame(View view){
        if(!isGameRunning){

            queue.add(getRandomWord); // Grab a random word form setgetgo.com

            chronometer.setBase(SystemClock.elapsedRealtime()); // set the clock to start over
            chronometer.start(); // start the clock
            isGameRunning = true; // game is now running
        }else{
            Toast.makeText(getBaseContext(), "Game is already running! :P", Toast.LENGTH_SHORT).show();
        }
    }

    public void finishGame(View view){

        if(isGameRunning){
            isGameRunning = false;
            chronometer.stop();
            if(isAnswerCorrect()){
                Toast.makeText(getBaseContext(), "Congratulations that's correct!", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getBaseContext(), "Whoops, that's not right!", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getBaseContext(), "Start the game first!", Toast.LENGTH_SHORT).show();
        }
    }

    public void hint(View view){
        if(isGameRunning){
            Toast.makeText(getBaseContext(), randomWord, Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getBaseContext(), "Start the game first!", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean isAnswerCorrect(){
        return input.getText().toString().contains(randomWord);
    }

    private String getScrambledWord(String word){
        Random random = new Random();
        char a[] = word.toCharArray();
        for( int i=0 ; i<a.length-1 ; i++ )
        {
            int j = random.nextInt(a.length-1);
            char temp = a[i]; a[i] = a[j];  a[j] = temp;
        }
        return new String(a);
    }


}

package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.content.Intent;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Locale;

public class multipleChoice extends AppCompatActivity {
    private int answer_count = 0;
    private int seconds = 0;
    private  int seconds_all = 0;
    private boolean running = true;
    private boolean running_all = true;
    private boolean wasRunning;
    private boolean on = false;
    private int seconds_limit = 100000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multichoice);
        if (savedInstanceState != null){
            // restore the activity's state by getting values from the Bundle
            seconds = savedInstanceState.getInt("seconds");
            running = savedInstanceState.getBoolean("running");
            wasRunning = savedInstanceState.getBoolean("wasRunning");
            seconds_all = savedInstanceState.getInt("seconds_all");
            running_all = savedInstanceState.getBoolean("running_all");
            answer_count = savedInstanceState.getInt("answer_count");
            seconds_limit = savedInstanceState.getInt("seconds_limit");
            on = savedInstanceState.getBoolean("on");
        }
        runTimer();
        runTimer2();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // save the state
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("seconds", seconds);
        savedInstanceState.putBoolean("running", running);
        savedInstanceState.putBoolean("wasRunning", wasRunning);
        savedInstanceState.putInt("seconds_all", seconds_all);
        savedInstanceState.putBoolean("running_all", running_all);
        savedInstanceState.putInt("answer_count", answer_count);
        savedInstanceState.putBoolean("on", on);
        savedInstanceState.putInt("seconds_limit", seconds_limit);
    }

    // when it is not in the foreground
    @Override
    protected void onPause(){
        super.onPause();
        wasRunning = running;
        running = false;
    }

    @Override
    protected void onResume(){
        super.onResume();
        if (wasRunning){
            running = true;
        }
    }

    // TimerA
    public void runTimer(){
        // get the text view
        final TextView timeView = (TextView) findViewById(R.id.textView2);
        // create a new Handler
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                int hours = seconds/3600;
                int minutes = (seconds%3600)/60;
                int secs = seconds%60;
                String time = String.format(Locale.getDefault(),"%d:%02d:%02d", hours, minutes, secs);
                // the timer is not stopped
                if (!on) {
                    timeView.setText(time);
                    if (running)
                        seconds++;
                }
                if(seconds <= seconds_limit){
                    // post the code again with a delay of 1 second
                    handler.postDelayed(this, 1000);
                }else{
                    startAct("You failed the Quiz!");
                }
            }
        });
    }

    // TimerB
    public void runTimer2(){
        // get the text view
        final TextView timeView = (TextView) findViewById(R.id.textView3);
        // create a new Handler
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                int hours = seconds_all/3600;
                int minutes = (seconds_all%3600)/60;
                int secs = seconds_all%60;
                String time = String.format(Locale.getDefault(),"%d:%02d:%02d", hours, minutes, secs);
                if (!on) {
                    timeView.setText(time);
                    if (running_all)
                        seconds_all++;
                }
                // post the code again with a delay of 1 second

                handler.postDelayed(this, 1000);
            }
        });
    }


    public void onClickQ1(View view){
        answer_count ++;
        // get a reference to a spinner
        Spinner spinner = (Spinner) findViewById(R.id.number);
        // get the selected item in the spinner and converts it to a string
        String answer = spinner.getSelectedItem().toString();


        if (answer.equals("3")){
            Intent intent = new Intent(this, fInBlank.class);
            intent.putExtra("seconds", seconds_all);
            startActivity(intent);
        }
        else if (answer_count == 1) {
            warning();
        }
        else{
            startAct("You failed the Quiz!");
        }
    }

    public void warning(){
        // alerting box
        new AlertDialog.Builder(this)
                .setMessage("Wrong answer. You have one more chance." )
                .setPositiveButton("OK" ,  null )
                .show();
    }

    //Chooser
    public void startAct(String str){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, str);
        // get the chooser title
        String chooserTitle = getString(R.string.chooser);
        // display the chooser dialog
        Intent chosenIntent = Intent.createChooser(intent,chooserTitle);
        startActivity(chosenIntent);
    }


    //Called when the switch is clicked
    public void onSwitchClicked(View view){
        on = ((Switch) view).isChecked();
    }

    // set the time limit for questions
    public void onClickSetTime(View view){
        EditText editText = (EditText) findViewById(R.id.editTextSecondDecimal);
        seconds_limit = Integer.parseInt(editText.getText().toString());
    }
}














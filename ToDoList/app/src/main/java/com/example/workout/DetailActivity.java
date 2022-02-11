package com.example.workout;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class DetailActivity extends AppCompatActivity {
    public static final String EXTRA_WORKOUT_ID = "id";
    public static final String SEARCH_ITEM = "search_item";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        /**
         * get a reference to WorkoutDetailFragment by asking the fragment manager
         * for the fragment with an ID of detail_frag
          */
        WorkoutDetailFragment frag = (WorkoutDetailFragment) getSupportFragmentManager().findFragmentById(R.id.detail_frag);

        /**
         * Get the ID and item title from the intent,
         * and pass it to the fragment via its setWorkout() and setTitle() method
         */
        Intent intent = getIntent();
        String title = intent.getStringExtra(SEARCH_ITEM);
        long workoutId = (long) intent.getIntExtra(EXTRA_WORKOUT_ID,-1);
        if (workoutId != -1){
            frag.setWorkout(workoutId);
        }else{
            frag.setTitle(title);
        }
    }
}
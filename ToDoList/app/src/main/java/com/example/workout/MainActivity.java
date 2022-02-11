package com.example.workout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity implements WorkoutListFragment.Listener{
    private String sorting_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // first get the intent to determine the sorting type
        Intent intent = getIntent();
        sorting_type = intent.getStringExtra("order");
        setContentView(R.layout.activity_main);
    }

    /**
     * Pass the ID of the workout to DetailActivity.
     * @param id
     */

    @Override
    public void itemClicked(long id){
        /**
         * Get a reference to the frame layout that will contain WorkoutDetailFragment.
         * This will only exist if the app is being run on a device with a large screen
         *
         */
        View fragmentContainer = findViewById(R.id.fragment_container);
        if (fragmentContainer != null){
            WorkoutDetailFragment details = new WorkoutDetailFragment();
            details.setWorkout(id);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, details);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(null);
            ft.commit();
        }else {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(DetailActivity.EXTRA_WORKOUT_ID, (int)id);
            intent.putExtra(DetailActivity.SEARCH_ITEM, "null");
            startActivity(intent);
        }
    }

    /**
     * onAddClick() method is used to add new item to the arraylist
     * Refresh the list by sending a intent to head to MainActivity.class
     * @param view
     */
    public void onAddClick(View view){
        // Get the input
        EditText title = findViewById(R.id.title);
        EditText description = findViewById(R.id.description);
        Spinner spinner = (Spinner) findViewById(R.id.itemStatus);
        /**
         * Get a reference to the database
         * getWritableDatabase() can perform any updates
         */
        SQLiteOpenHelper itemDatabaseHelper = new ItemDatabaseHelper(this);
        SQLiteDatabase db = itemDatabaseHelper.getWritableDatabase();
        ItemDatabaseHelper.AddItem(db,title.getText().toString(),description.getText().toString(),spinner.getSelectedItem().toString());
        // Send an intent
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        db.close();
    }

    /**
     * A method to let fragment get the sorting type from the activity
     */
    public String getTitles(){
        return sorting_type;
    }

    /**
     * Set the sorting order by sending an intent with "order"
     * @param view
     */
    public void onSetOrder(View view){
        Intent intent = new Intent(this, MainActivity.class);
        Spinner spinner = (Spinner) findViewById(R.id.order);
        String answer = spinner.getSelectedItem().toString();
        if (answer.equals("Alphabetical Order")){
            intent.putExtra("order", "ITEM");
        }
        else{
            intent.putExtra("order", "_id");
        }
        startActivity(intent);
    }

    /**
     * Search a specific item by sending an intent to DetailActivity
     * with "SEARCH_ITEM" and "EXTRA_WORKOUT_ID"
     * @param view
     */
    public void onSearchItem(View view){
        EditText messageView = (EditText) findViewById(R.id.search_item);
        String item_title = messageView.getText().toString();
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.SEARCH_ITEM, item_title);
        intent.putExtra(DetailActivity.EXTRA_WORKOUT_ID, -1);
        startActivity(intent);
    }
}
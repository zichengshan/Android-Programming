package com.example.workout;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;

/**
 * display a list of items
 */

public class WorkoutDetailFragment extends Fragment implements View.OnClickListener{
    // default value -1
    private int workoutId = -1;
    private String title = null;
    private SQLiteOpenHelper helper;
    SQLiteDatabase db;
    Cursor cursor;

    /**
     * restore the value of workoutID
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null){
            workoutId = savedInstanceState.getInt("workoutId");
            title = savedInstanceState.getString("title");
        }
    }

    @Override
    /**
     * It is called when Android needs the fragment's layout
     * the onCreateView() method specifies that fragment_workout_detail.xml should
     * be used for WorkoutDetailFragment's layout
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_workout_detail, container, false);
        Button deleteButton = (Button)layout.findViewById(R.id.delete);
        Button toDoButton = (Button)layout.findViewById(R.id.todo);
        Button doingButton = (Button)layout.findViewById(R.id.doing);
        Button doneButton = (Button)layout.findViewById(R.id.done);
      // Attach the listeners to each of the buttons
        deleteButton.setOnClickListener(this);
        doingButton.setOnClickListener(this);
        toDoButton.setOnClickListener(this);
        doneButton.setOnClickListener(this);
        return layout;
    }
    @Override
    public void onStart(){
        super.onStart();
        // get the fragment's root view
        /**
         * Get the fragment's root view
         * Show the title and description
         */
        View view = getView();
        if (view != null){
            /**
             * Create a cursor
             */

            helper = new ItemDatabaseHelper(getActivity());
            try {
                db = helper.getWritableDatabase();
                if (workoutId != -1){
                    cursor = db.query("ITEM_LIST",
                            new String[]{"ITEM", "DESCRIPTION","_id"},
                            "_id = ?",
                            new String[]{Integer.toString(workoutId)},
                            null,
                            null,
                            null);
                }else{
                    cursor = db.query("ITEM_LIST",
                            new String[]{"ITEM", "DESCRIPTION","_id"},
                        "ITEM = ?",
                        new String[]{title},
                            null,
                            null,
                            null);
                }
                /**
                 * moveToFirst() method moves the cursor to the first row.
                 * It allows to perform a test whether the query returned an empty set or not.
                 */

                if (cursor.moveToFirst()) {
                    // populate the title and description
                    title = cursor.getString(0);
                    String description = cursor.getString(1);
                    workoutId = Integer.parseInt(cursor.getString(2));

                    TextView title_view = (TextView) view.findViewById(R.id.textTitle);
                    TextView description_view = (TextView) view.findViewById(R.id.textDescription);
                    title_view.setText(title);
                    description_view.setText(description);
                }else{
                    // alerting box
                    new AlertDialog.Builder(getActivity())
                            .setMessage("Not Found")
                            .setPositiveButton("OK" ,  null )
                            .show();
                }
            } catch (SQLiteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * a method to set the workout ID
     * @param id
     */
    public void setWorkout(long id){
        this.workoutId = (int) id;
    }

    /**
     * a method to set the title
     * @param str
     */
    public void setTitle(String str){
        this.title = str;
    }

    /**
     * Save the value of the workoutId in the savedInstanceState Bundle
     * before the fragment gets destroyed.
     * @param savedInstanceState
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        savedInstanceState.putLong("workoutId", workoutId);
        savedInstanceState.putString("title", title);
    }

    /**
     * Set status for items
     * Delete an item
     * @param view
     */
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        switch(view.getId()){
            case R.id.delete:
//                db.delete("ITEM_LIST","ITEM = ?",new String[]{cursor.getString(0)});
                db.delete("ITEM_LIST","_id = ?",new String[]{Integer.toString(workoutId)});
                startActivity(intent);
                break;
            case R.id.todo:
                ContentValues itemStatusTodo = new ContentValues();
                itemStatusTodo.put("STATUS", "TO DO");
                db.update("ITEM_LIST",itemStatusTodo,"_id = ?",new String[]{Integer.toString(workoutId)});
                startActivity(intent);
                break;
            case R.id.doing:
                ContentValues itemStatusDoing = new ContentValues();
                itemStatusDoing.put("STATUS", "DOING");
                db.update("ITEM_LIST",itemStatusDoing,"_id = ?",new String[]{Integer.toString(workoutId)});
                startActivity(intent);
                break;
            case R.id.done:
                ContentValues itemStatusDONE = new ContentValues();
                itemStatusDONE.put("STATUS", "DONE ");
                db.update("ITEM_LIST",itemStatusDONE,"_id = ?",new String[]{Integer.toString(workoutId)});
                startActivity(intent);
                break;
        }
        cursor.close();
        db.close();
    }
}
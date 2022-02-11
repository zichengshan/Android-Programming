package com.example.workout;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;

import java.util.List;

import javax.sql.StatementEvent;

public class WorkoutListFragment extends ListFragment {
    private SQLiteDatabase db;
    private Cursor cursor;
    private String sort_type;

    /**
     * Define the Listener interface
     * Any activities that implement the Listener must include this method
     * Use it to get the activity to respond to items in the fragment being clicked
     */
    interface Listener{
        void itemClicked(long id);
    }
    private Listener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SQLiteOpenHelper helper = new ItemDatabaseHelper(getActivity());
        try {
            db = helper.getReadableDatabase();

            cursor = db.query("ITEM_LIST",
                    // columns we want to return
                    new String[]{"_id", "STATUS" + "||"+ "' - '" +"||"+ "ITEM" },
                    null,
                    null,
                    null,
                    null,
                    sort_type);
            SimpleCursorAdapter listAdapter = new SimpleCursorAdapter(getActivity(),
                    android.R.layout.simple_list_item_1,
                    cursor,
                    new String[]{"STATUS" + "||"+ "' - '" +"||"+ "ITEM"},
                    new int[]{android.R.id.text1},
                    0);
            setListAdapter(listAdapter);


        } catch (Exception e) {
            e.printStackTrace();
        }

        /**
         * calling the superclass onCreateView() method gives the default layout for the ListFragment
         */
        return super.onCreateView(inflater,container,savedInstanceState);
    }

    /**
     * Register the listener
     * This is called when the fragment gets attached to the activity. the Activity class is a sub
     * class of Context
     * @param context
     */
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        this.listener = (Listener) context;
        sort_type = ((MainActivity)getActivity()).getTitles();
    }

    /**
     * Respond to clicks
     * Call the itemClicked() method in the activity, passing it the ID of the workout the user selected
     * @param listView
     * @param itemView
     * @param position
     * @param id
     */
    @Override
    public void onListItemClick(ListView listView, View itemView, int position, long id){
        if (listener != null){
            listener.itemClicked(id);
        }
    }

    /**
     * The cursor will still open until the cursor adapter no longer needs it
     */
    @Override
    public void onDestroy(){
        super.onDestroy();
        cursor.close();
        db.close();
    }
}
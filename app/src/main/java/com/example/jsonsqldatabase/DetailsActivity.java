package com.example.jsonsqldatabase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

public class DetailsActivity extends AppCompatActivity {

    private RecyclerView customerListView;
    private Toolbar toolbar;

    private  Cursor cursor;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        customerListView = findViewById(R.id.costumer_list_view);
        //toolbar = findViewById(R.id.detail_toolbar);
//        setSupportActionBar(toolbar);
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        ContentValues costumerValues = new ContentValues();

        DatabaseHelper helper = new DatabaseHelper(getApplicationContext());

        SQLiteOpenHelper starbuzzDatabaseHelper = new DatabaseHelper(getApplicationContext());

        db = starbuzzDatabaseHelper.getReadableDatabase();

        cursor = db.query("COSTUMERS",
                new String[]{"_id", "NAME", "ADDRESS", "CODE", "PHONE"},
                null, null, null, null, null);

       /* CursorAdapter listAdapter = new SimpleCursorAdapter(getApplicationContext(),
                R.layout.single_costumer_item,
                cursor,
                new String[]{"NAME", "ADDRESS"},
                new int[]{R.id.single_item_name, R.id.singel_item_address},
                0);

        customerListView.setAdapter(listAdapter);*/
        customerListView = findViewById(R.id.costumer_list_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        // set layout manager
        customerListView.setLayoutManager(mLayoutManager);
        //set default animator
        customerListView.setItemAnimator(new DefaultItemAnimator());
        MyRecyclerAdapter mAdapter = new MyRecyclerAdapter(this, cursor);
        customerListView.setAdapter(mAdapter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        cursor.close();
        db.close();

    }
}

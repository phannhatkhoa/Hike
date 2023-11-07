package com.example.hike.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.hike.HikeAdapter;
import com.example.hike.R;
import com.example.hike.database.AppDatabase;
import com.example.hike.models.Hike;

import java.util.List;

public class DetailsActivity extends AppCompatActivity implements HikeAdapter.OnDeleteClickListener {

//    private AppDatabase appDatabase;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_details);
//
//        appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "hike_db")
//                .allowMainThreadQueries() // For simplicity, don't use this in production
//                .build();
//
//        TextView detailsTxt = findViewById(R.id.detailsText);
//
//        List<Hike> hikes = appDatabase.hikeDao().getAllHikes();
//
//        StringBuilder detailsBuilder = new StringBuilder();
//        for (Hike hike : hikes) {
//            detailsBuilder.append(hike.hike_id).append(" ")
//                    .append(hike.name).append(" ")
//                    .append(hike.location).append(" ")
//                    .append(hike.dob).append(" ")
//                    .append(hike.parking).append(" ")
//                    .append(hike.length).append(" ")
//                    .append(hike.difficulty).append(" ")
//                    .append(hike.description).append("\n");
//        }
//
//        detailsTxt.setText(detailsBuilder.toString());
//    }
    private AppDatabase appDatabase;
    private RecyclerView recyclerView;
    private HikeAdapter adapter;
    List<Hike> hikes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "hike_db")
                .allowMainThreadQueries() // For simplicity, don't use this in production
                .build();

        Button addButton = findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to go back to the MainActivity
                Intent intent = new Intent(DetailsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        hikes = appDatabase.hikeDao().getAllHikes();

        adapter = new HikeAdapter(hikes,this);
        recyclerView.setAdapter(adapter);

    }


    @Override
    public void onDeleteClick(Hike hike) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Hike")
                .setMessage("Are you sure you want to delete this hike?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    // Remove from the database
                    appDatabase.hikeDao().deleteHike(hike);

                    // Update the list
                    hikes.remove(hike);
                    adapter.notifyDataSetChanged();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
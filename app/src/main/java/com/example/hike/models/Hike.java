package com.example.hike.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "hikes")
public class Hike {
    @PrimaryKey(autoGenerate = true)
    public long hike_id;
    public String name;
    public String location;
    public String dob;
    public String parking;
    public String length;
    public String difficulty;
    public String description;

}
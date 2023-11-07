package com.example.hike.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.hike.dao.HikeDao;
import com.example.hike.models.Hike;

@Database(entities = {Hike.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract HikeDao hikeDao();
}

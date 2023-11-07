package com.example.hike.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.hike.models.Hike;

import java.util.List;
@Dao
public interface HikeDao {
    @Insert
    long insertHike(Hike hike);

    @Query("SELECT * FROM hikes ORDER BY name")
    List<Hike> getAllHikes();

    @Query("SELECT * FROM hikes WHERE hike_id = :hikeId")
    Hike getHikeById(long hikeId);
    @Delete
    void deleteHike(Hike hike);
    @Update
    void updateHike(Hike hike);
}

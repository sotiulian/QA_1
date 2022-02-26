package com.wwp.QA.RoomDatabase;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface LoginnameDao {

    @Query("SELECT * FROM LoginnameEntity")
    List<LoginnameEntity> getLoginname();

    @Query("SELECT count(loginname) FROM LoginnameEntity")
    int getCountLogin();

    @Insert
    void insert(LoginnameEntity loginnameEntity);

    @Delete
    void delete(LoginnameEntity loginnameEntity);

    @Update
    void update(LoginnameEntity loginnameEntity);

}

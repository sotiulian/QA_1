package com.wwp.QA.RoomDatabase;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface SysadminDao {

    @Query("SELECT * FROM SysadminEntity ORDER BY webaddress ASC")
    List<SysadminEntity> getAll();

    @Query("SELECT * FROM SysadminEntity WHERE isactive = 1 LIMIT 1")
    SysadminEntity getActivewebadress();

    @Insert
    void insert(SysadminEntity sysadminEntity);

    @Delete
    void delete(SysadminEntity sysadminEntity);

    @Update
    void update(SysadminEntity sysadminEntity);

}

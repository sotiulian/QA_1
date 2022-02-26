package com.wwp.QA.RoomDatabase;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = { SysadminEntity.class // declare SysadminEntity table into database
                      , LoginnameEntity.class    // declare LoginnameEntity table into database
                     }
          , version = 4)
public abstract class AppDatabase extends RoomDatabase {

    public abstract SysadminDao sysadminDao();

    public abstract LoginnameDao loginnameDao();

}

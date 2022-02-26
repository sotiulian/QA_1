package com.wwp.QA.RoomDatabase;

import android.content.Context;

import androidx.room.Room;

public class DatabaseClient {

    private Context mCtx;
    // Do not place Android context classes in static fields (static reference to DatabaseClient
    //  which has field mCtx pointing to Context); this is a memory leak
    private static DatabaseClient mInstance; // static because is expensive and we want to do just one time

    //our app database object
    private AppDatabase appDatabase;

    private DatabaseClient(Context mCtx) {

        this.mCtx = mCtx;

        // create the app database with Room database builder
        // MyToDos will be the name of the database
        appDatabase = Room.databaseBuilder(mCtx, AppDatabase.class, "QASysadmin9").build();

    }

    public static synchronized DatabaseClient getInstance(Context mCtx) {

        if (mInstance == null) {
            mInstance = new DatabaseClient(mCtx);
        }

        return mInstance;
    }

    public AppDatabase getAppDatabase() {

        return appDatabase;

    }

}

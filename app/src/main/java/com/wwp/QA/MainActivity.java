package com.wwp.QA;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.wwp.QA.RoomDatabase.LoginnamelistActivity;
import com.wwp.QA.RoomDatabase.SysadminActivity;

// What to learn to become Android developer
// https://androidrepo.com/repo/skydoves-android-developer-roadmap-android-jetpack-compose

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button_sysadmin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Clicked", Toast.LENGTH_LONG).show();
                startActivity(new Intent(MainActivity.this, SysadminActivity.class));
            }
        });

        findViewById(R.id.button_processcontroller).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Clicked", Toast.LENGTH_LONG).show();
                startActivity(new Intent(MainActivity.this, LoginnamelistActivity.class));
            }
        });

        findViewById(R.id.button_about).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
            }
        });

    }



}
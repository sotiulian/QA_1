package com.wwp.QA.RoomDatabase;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.wwp.QA.MainActivity;
import com.wwp.QA.R;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SysadminActivity extends AppCompatActivity {

    private FloatingActionButton buttonAddWebaddress;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sysadmin);

        recyclerView = findViewById(R.id.recyclerview_tasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // floating button to add a webaddress
        buttonAddWebaddress = findViewById(R.id.floating_button_add);
        buttonAddWebaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SysadminActivity.this, AddSysadminActivity.class);
                startActivity(intent);
            }
        });

        // return back to main activity
        findViewById(R.id.button_mainactivity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Canceled", Toast.LENGTH_LONG).show();
                finish();
                startActivity(new Intent(SysadminActivity.this, MainActivity.class));
            }
        });

        getWebadresses();

    }

    private void getWebadresses(){

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {

            List<SysadminEntity> webaddresses = DatabaseClient
                    .getInstance(getApplicationContext())
                    .getAppDatabase()
                    .sysadminDao()
                    .getAll();

            handler.post(() -> {
                SysadminlistAdapter adapter = new SysadminlistAdapter(SysadminActivity.this, webaddresses);
                recyclerView.setAdapter(adapter);
            });
        });

    }




}
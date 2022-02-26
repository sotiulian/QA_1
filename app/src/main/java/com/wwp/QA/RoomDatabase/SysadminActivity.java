package com.wwp.QA.RoomDatabase;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.wwp.QA.MainActivity;
import com.wwp.QA.R;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.List;

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

        class GetTasks extends AsyncTask<Void, Void, List<SysadminEntity>> {

            @Override
            protected List<SysadminEntity> doInBackground(Void... voids) {

                List<SysadminEntity> sysadminEntityList = DatabaseClient
                        .getInstance(getApplicationContext())
                        .getAppDatabase()
                        .sysadminDao()
                        .getAll();

                return sysadminEntityList; // goes into onPostExecute method as parameter
            }

            @Override
            protected void onPostExecute(List<SysadminEntity> webaddresses) {

                super.onPostExecute(webaddresses);

                SysadminlistAdapter adapter = new SysadminlistAdapter(SysadminActivity.this, webaddresses);
                recyclerView.setAdapter(adapter);
            }

        }

        GetTasks gt = new GetTasks();
        gt.execute();

    }




}
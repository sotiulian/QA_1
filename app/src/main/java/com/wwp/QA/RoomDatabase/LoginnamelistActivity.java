package com.wwp.QA.RoomDatabase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.wwp.QA.MainActivity;
import com.wwp.QA.R;

import java.util.List;

public class LoginnamelistActivity extends AppCompatActivity {

    private FloatingActionButton buttonNewName;
    private RecyclerView recyclerViewLA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginname);

        recyclerViewLA = findViewById(R.id.recyclerview_login);
        recyclerViewLA.setLayoutManager(new LinearLayoutManager(this));

        // floating button to add a webaddress
        buttonNewName = findViewById(R.id.floating_button_add);
        buttonNewName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginnamelistActivity.this, AddLoginnameActivity.class);
                startActivity(intent);
            }
        });


        // return back to main activity
        findViewById(R.id.button_mainactivity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Canceled", Toast.LENGTH_LONG).show();
                finish();
                startActivity(new Intent(LoginnamelistActivity.this, MainActivity.class));
            }
        });

        getLoginname();

    }

    private void getLoginname(){

        class GetTasks extends AsyncTask<Void, Void, List<LoginnameEntity>> {

            @Override
            protected List<LoginnameEntity> doInBackground(Void... voids) {

                List<LoginnameEntity> loginnameEntityList = DatabaseClient
                        .getInstance(getApplicationContext())
                        .getAppDatabase()
                        .loginnameDao() // implements abstract method from LoginnameDao interface
                        .getLoginname();

                return loginnameEntityList; // goes into onPostExecute method as parameter
            }

            @Override
            protected void onPostExecute(List<LoginnameEntity> loginnames) {

                super.onPostExecute(loginnames);

                LoginnamelistAdapter adapter = new LoginnamelistAdapter(LoginnamelistActivity.this, loginnames);
                recyclerViewLA.setAdapter(adapter);
            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();

    }

}
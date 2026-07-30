package com.wwp.QA.RoomDatabase;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.wwp.QA.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UpdateLoginnameActivity extends AppCompatActivity {


    private EditText editTextTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_login);

        editTextTask = findViewById(R.id.editTextloginname);
        editTextTask.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        // ===========================================================
        // this activity get the Entity that was sent to it by intent
        //    into LoginnamelistAdapter  LoginViewHolder class onClick
        // ===========================================================
        final LoginnameEntity loginnameEntity = (LoginnameEntity) getIntent().getSerializableExtra("loginnameEntity");

        loadTask(loginnameEntity);

        findViewById(R.id.button_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Clicked", Toast.LENGTH_LONG).show();
                updateTask(loginnameEntity); // arrived here via getIntent
            }
        });

        findViewById(R.id.button_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateLoginnameActivity.this);
                builder.setTitle("Are you sure?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteTask(loginnameEntity); // // arrived here via getIntent
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                AlertDialog ad = builder.create();
                ad.show();
            }
        });

        findViewById(R.id.button_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Canceled", Toast.LENGTH_LONG).show();
                finish();
                startActivity(new Intent(UpdateLoginnameActivity.this, LoginnamelistActivity.class));
            }
        });


    }

    private void loadTask(LoginnameEntity loginnameEntity) {

        editTextTask.setText(loginnameEntity.getLoginname());


    }



    private void updateTask(final LoginnameEntity loginnameEntity){

        final String sTask = editTextTask.getText().toString().trim();


        if (sTask.isEmpty()) {
            editTextTask.setError("SysadminEntity required");
            editTextTask.requestFocus();
            return;
        }



        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            loginnameEntity.setLoginname(sTask);
            DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                    .loginnameDao()
                    .update(loginnameEntity); // call Dao method update with argument arrived here by Intent

            handler.post(() -> {
                Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_LONG).show();
                finish();
                startActivity(new Intent(UpdateLoginnameActivity.this, LoginnamelistActivity.class));
            });
        });

    }

    private void deleteTask(final LoginnameEntity loginnameEntity) {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                    .loginnameDao()
                    .delete(loginnameEntity);

            handler.post(() -> {
                Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_LONG).show();

                finish();

                startActivity(new Intent(UpdateLoginnameActivity.this, LoginnamelistActivity.class));
            });
        });

    }

}
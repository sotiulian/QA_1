package com.wwp.QA.RoomDatabase;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.wwp.QA.R;

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



        class UpdateTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                loginnameEntity.setLoginname(sTask);
                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .loginnameDao()
                        .update(loginnameEntity); // call Dao method update with argument arrived here by Intent
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_LONG).show();
                finish();
                startActivity(new Intent(UpdateLoginnameActivity.this, LoginnamelistActivity.class));
            }
        }

        UpdateTask ut = new UpdateTask();
        ut.execute();

    }

    private void deleteTask(final LoginnameEntity loginnameEntity) {
        class DeleteTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .loginnameDao()
                        .delete(loginnameEntity);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_LONG).show();

                finish();

                startActivity(new Intent(UpdateLoginnameActivity.this, LoginnamelistActivity.class));
            }
        }

        DeleteTask dt = new DeleteTask();
        dt.execute();

    }    
    
}
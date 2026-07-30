package com.wwp.QA.RoomDatabase;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.wwp.QA.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UpdateSysadminActivity extends AppCompatActivity {

    private EditText editTextTask, editTextDesc;
    private CheckBox checkBoxIsactive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_sysadmin);

        editTextTask = findViewById(R.id.editTextWebaddress);
        editTextDesc = findViewById(R.id.editTextDesc);

        checkBoxIsactive = findViewById(R.id.checkBoxIsactive);

        // ===========================================================
        // this activity get the Entity that was sent to it by intent
        //    into SysadminlistAdapter  SysadminViewHolder class onClick
        // ===========================================================
        final SysadminEntity sysadminEntity = (SysadminEntity) getIntent().getSerializableExtra("sysadminEntity");

        loadTask(sysadminEntity);

        findViewById(R.id.button_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Clicked", Toast.LENGTH_LONG).show();
                updateTask(sysadminEntity);
            }
        });


        findViewById(R.id.button_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateSysadminActivity.this);
                builder.setTitle("Are you sure?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteTask(sysadminEntity);
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
                startActivity(new Intent(UpdateSysadminActivity.this, SysadminActivity.class));
            }
        });


    }

    private void loadTask(SysadminEntity sysadminEntity) {

        editTextTask.setText(sysadminEntity.getWebaddress());
        editTextDesc.setText(sysadminEntity.getDesc());
        checkBoxIsactive.setChecked(sysadminEntity.isActive());

    }

    private void updateTask(final SysadminEntity sysadminEntity){

        final String sTask = editTextTask.getText().toString().trim();
        final String sDesc = editTextDesc.getText().toString().trim();

        if (sTask.isEmpty()) {
            editTextTask.setError("SysadminEntity required");
            editTextTask.requestFocus();
            return;
        }

        if (sDesc.isEmpty()) {
            editTextDesc.setError("Desc required");
            editTextDesc.requestFocus();
            return;
        }


        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            sysadminEntity.setWebaddress(sTask);
            sysadminEntity.setDesc(sDesc);
            sysadminEntity.setActive(checkBoxIsactive.isChecked());
            DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                    .sysadminDao()
                    .update(sysadminEntity);

            handler.post(() -> {
                Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_LONG).show();
                finish();
                startActivity(new Intent(UpdateSysadminActivity.this, SysadminActivity.class));
            });
        });

    }

    private void deleteTask(final SysadminEntity sysadminEntity) {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                    .sysadminDao()
                    .delete(sysadminEntity);

            handler.post(() -> {
                Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_LONG).show();

                finish();

                startActivity(new Intent(UpdateSysadminActivity.this, SysadminActivity.class));
            });
        });

    }

}
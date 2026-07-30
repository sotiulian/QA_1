package com.wwp.QA.RoomDatabase;

import androidx.appcompat.app.AppCompatActivity;

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

public class AddLoginnameActivity extends AppCompatActivity {

    private EditText editTextLoginname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_loginname);

        editTextLoginname = findViewById(R.id.editTextLoginname);
        editTextLoginname.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        findViewById(R.id.buttonSaveLoginname).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveLoginname();
            }
        });

    }

    private void saveLoginname(){

        final String sLoginname = editTextLoginname.getText().toString().trim();


        if (sLoginname.isEmpty()) {
            editTextLoginname.setError("Loginname required");
            editTextLoginname.requestFocus();
            return;
        }


        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {

            // creating a LoginnameEntity
            LoginnameEntity loginnameEntity = new LoginnameEntity();

            loginnameEntity.setLoginname(sLoginname);


            // adding LoginnameEntity to database
            DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                    .loginnameDao()
                    .insert(loginnameEntity);

            handler.post(() -> {
                finish();

                startActivity(new Intent(getApplicationContext(), LoginnamelistActivity.class));

                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
            });
        });

    }

}
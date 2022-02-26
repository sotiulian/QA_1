package com.wwp.QA.RoomDatabase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.wwp.QA.R;

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


        class SaveLoginnameAsyncTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                // creating a LoginnameEntity
                LoginnameEntity loginnameEntity = new LoginnameEntity();

                loginnameEntity.setLoginname(sLoginname);


                // adding LoginnameEntity to database
                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .loginnameDao()
                        .insert(loginnameEntity);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                finish();

                startActivity(new Intent(getApplicationContext(), LoginnamelistActivity.class));

                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
            }
        }

        SaveLoginnameAsyncTask st = new SaveLoginnameAsyncTask();
        st.execute();

    }

}
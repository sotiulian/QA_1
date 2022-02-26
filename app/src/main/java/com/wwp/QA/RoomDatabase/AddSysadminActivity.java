package com.wwp.QA.RoomDatabase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.wwp.QA.R;

public class AddSysadminActivity extends AppCompatActivity {

    private EditText editTextWebaddress, editTextDesc;
    private CheckBox checkBoxFinished;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sysadmin);

        editTextWebaddress = findViewById(R.id.editTextWebaddress);
        editTextDesc = findViewById(R.id.editTextDesc);

        checkBoxFinished = findViewById(R.id.checkBoxIsactive);

        findViewById(R.id.button_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveWebaddress();
            }
        });

    }

    private void saveWebaddress(){

        final String sWebaddress = editTextWebaddress.getText().toString().trim();
        final String sDesc = editTextDesc.getText().toString().trim();
        final Boolean sIsactive = checkBoxFinished.isChecked();

        if (sWebaddress.isEmpty()) {
            editTextWebaddress.setError("Webaddress required");
            editTextWebaddress.requestFocus();
            return;
        }

        if (sDesc.isEmpty()){
            editTextDesc.setError("Description required");
            editTextDesc.requestFocus();
            return;
        }


        class SaveWebaddressAsyncTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                // creating a SysadminEntity
                SysadminEntity sysadminEntity = new SysadminEntity();

                sysadminEntity.setWebaddress(sWebaddress);
                sysadminEntity.setDesc(sDesc);
                sysadminEntity.setActive(sIsactive);

                // adding SysadminEntity to database
                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .sysadminDao()
                        .insert(sysadminEntity);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                finish();

                startActivity(new Intent(getApplicationContext(), SysadminActivity.class));

                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
            }
        }

        SaveWebaddressAsyncTask st = new SaveWebaddressAsyncTask();
        st.execute();

    }

}
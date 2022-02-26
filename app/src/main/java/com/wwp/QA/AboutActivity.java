package com.wwp.QA;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.wwp.QA.ProcessController.ProcesscontrollermainActivity;

public class AboutActivity extends AppCompatActivity {

    TextView tvaboutcompany, tvaboutdescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        // information goes into activity title
        tvaboutcompany = (TextView) findViewById(R.id.tvaboutcompanyname);
        tvaboutcompany.setText(R.string.companyname);

        tvaboutdescription = (TextView) findViewById(R.id.tvaboutdescription);
        tvaboutdescription.setText(R.string.companydescription);


        // return back to main activity
        findViewById(R.id.button_mainactivity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Canceled", Toast.LENGTH_LONG).show();
                finish();
                startActivity(new Intent(AboutActivity.this, MainActivity.class));
            }
        });

    }
}
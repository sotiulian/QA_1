package com.wwp.QA.ProcessController;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.blikoon.qrcodescanner.QrCodeActivity;
import com.wwp.QA.MainActivity;
import com.wwp.QA.ProductionLine.ProductionlineArticles;
import com.wwp.QA.ProductionLine.ProductionlinelistActivity;
import com.wwp.QA.ProductionOperator.ProductionoperatorArticles;
import com.wwp.QA.ProductionOperator.ProductionoperatorlistActivity;
import com.wwp.QA.R;
import com.wwp.QA.RoomDatabase.LoginnamelistActivity;
import com.wwp.QA.RoomDatabase.SysadminActivity;

public class ProcesscontrollermainActivity extends AppCompatActivity {

    // filter that will be passed through activities by Intent
    ProcesscontrollerFilter processcontrollerFilter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_processcontroller_main);

        // ===========================================================
        // this activity get the Entity that was sent to it by intent into LoginnamelistAdapter  LoginViewHolder class onClick
        // ===========================================================
        final String loginname = getIntent().getStringExtra("loginname");

        //Log.e("PMA", "startProcessControllerActivity()");
        //Log.e("PMA", "loginnameEntity.getLoginname() -> " + loginResponse.getLoginname());
        consumeQaoperatorname(loginname); // from now on  processcontrollerFilter.getQaoperatorname() will return QA Operator logged in



        findViewById(R.id.button_unresolvedtasks).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                processcontrollerFilter.setResolved("");  // qaoperatorname was set into consumeQaoperatorname(loginResponse)
                processcontrollerFilter.setUnresolved("YES");

                startActivity(new Intent(ProcesscontrollermainActivity.this
                                         , ProcesscontrollertasksActivity.class)
                                        .putExtra("processcontrollerfilter", processcontrollerFilter)
                );
            }
        });


        // will be launched when ProductionlinelistActivity is closed,
        ActivityResultLauncher<Intent> chooseLineActivityUnresolvedResultLauncher = registerForActivityResult(

                new ActivityResultContracts.StartActivityForResult()
                , result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {

                        Intent data = result.getData(); // from ProductionlineAdapter -> ProductionlineViewHolder -> onClick
                        String areaName = ((ProductionlineArticles) data.getSerializableExtra("article")).getAreaname();

                        Log.e("PCM", "Choose linename onActivityResult -> " + areaName);

                        //Getting the passed result
                        processcontrollerFilter.setAreaname(areaName);
                        processcontrollerFilter.setResolved(""); // qaoperatorname was set into consumeQaoperatorname(loginResponse)
                        processcontrollerFilter.setUnresolved("YES");

                        startActivity(new Intent(ProcesscontrollermainActivity.this
                                , ProcesscontrollertasksActivity.class)
                                .putExtra("processcontrollerfilter", processcontrollerFilter)
                        );
                    }
                });

        findViewById(R.id.button_unresolvedbyline).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // https://stackoverflow.com/questions/62671106/onactivityresult-method-is-deprecated-what-is-the-alternative

                // launch the choose line activity first
                Intent intent = new Intent(ProcesscontrollermainActivity.this, ProductionlinelistActivity.class);
                chooseLineActivityUnresolvedResultLauncher.launch(intent);

            }
        });


        // will be launched when ProductionlinelistActivity is closed,
        ActivityResultLauncher<Intent> chooseOperatorActivityUnresolvedResultLauncher = registerForActivityResult(

                new ActivityResultContracts.StartActivityForResult()
                , result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {

                        Intent data = result.getData(); // from ProductionlineAdapter -> ProductionlineViewHolder -> onClick
                        String pmsoperatorname = ((ProductionoperatorArticles) data.getSerializableExtra("article")).getOperatorname();

                        Log.e("PCM", "Choose pms_operators.operatorname onActivityResult -> " + pmsoperatorname);

                        //Getting the passed result
                        processcontrollerFilter.setPmsoperatorname(pmsoperatorname);
                        processcontrollerFilter.setResolved(""); // qaoperatorname was set into consumeQaoperatorname(loginResponse)
                        processcontrollerFilter.setUnresolved("NO");

                        startActivity(new Intent(
                                ProcesscontrollermainActivity.this
                                , ProcesscontrollertasksActivity.class)
                                .putExtra("processcontrollerfilter", processcontrollerFilter)
                        );
                    }
                });

        findViewById(R.id.button_unresolvedbyoperator).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // https://stackoverflow.com/questions/62671106/onactivityresult-method-is-deprecated-what-is-the-alternative

                // launch the choose line activity first
                Intent intent = new Intent(ProcesscontrollermainActivity.this, ProductionoperatorlistActivity.class);
                chooseOperatorActivityUnresolvedResultLauncher.launch(intent);

            }
        });


        // will be launched when click on button_qrscan
        ActivityResultLauncher<Intent> qrActivityResultLauncher = registerForActivityResult(
                 new ActivityResultContracts.StartActivityForResult()
                , result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        //Log.e("PCM", "onActivityResult -> " + data.toString());

                        //Getting the passed result
                        String myresult = data.getStringExtra("com.blikoon.qrcodescanner.got_qr_scan_relult");
                        //Log.e("PCM", "myResult -> " + myresult);
                        String machineID = "";;
                        try {

                            machineID = myresult.split("~")[0].trim(); // codulqr contine si logoul bibliotecii MW... care incepe dupa caracterul ~ si trebuie curatat
                            Log.e("PCM", "QR Scan machineid result -> " + machineID);

                            // will start the activity for Defects recording
                            processcontrollerFilter.setMachineid(machineID); // will be for order, bundle, size & color, operation
                            processcontrollerFilter.setResolved("YES");      // Resolved tasks will be displayed into the list for Edit/Update if qaoperator needs them
                            processcontrollerFilter.setUnresolved("YES");    // Unresolved for normal qaoperator work

                            startActivity(new Intent(ProcesscontrollermainActivity.this
                                    , ProcesscontrollerdefectsActivity.class)
                                    .putExtra("processcontrollerfilter", processcontrollerFilter)
                            );

                        } catch (ArrayIndexOutOfBoundsException e) {
                            Log.e("PCM", "myResult ->  does not contain: ~ ");
                        }

                    }
                });

        findViewById(R.id.button_qrscan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // https://stackoverflow.com/questions/62671106/onactivityresult-method-is-deprecated-what-is-the-alternative

                Intent intent = new Intent(ProcesscontrollermainActivity.this, QrCodeActivity.class);
                qrActivityResultLauncher.launch(intent);

            }
        });




        findViewById(R.id.button_allresolvedtasks).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                processcontrollerFilter.setResolved("YES"); // qaoperatorname was set into consumeQaoperatorname(loginResponse)
                processcontrollerFilter.setUnresolved("");

                startActivity(new Intent(ProcesscontrollermainActivity.this
                        , ProcesscontrollertasksActivity.class)
                        .putExtra("processcontrollerfilter", processcontrollerFilter)
                );
            }
        });



        // will be launched when ProductionlinelistActivity is closed,
        ActivityResultLauncher<Intent> chooseLineActivityResultLauncher = registerForActivityResult(

                new ActivityResultContracts.StartActivityForResult()
                , result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {

                        Intent data = result.getData(); // from ProductionlineAdapter -> ProductionlineViewHolder -> onClick
                        String areaName = ((ProductionlineArticles) data.getSerializableExtra("article")).getAreaname();

                        Log.e("PCM", "Choose linename onActivityResult -> " + areaName);

                        //Getting the passed result
                        processcontrollerFilter.setAreaname(areaName);
                        processcontrollerFilter.setResolved("YES"); // qaoperatorname was set into consumeQaoperatorname(loginResponse)
                        processcontrollerFilter.setUnresolved("");

                        startActivity(new Intent(ProcesscontrollermainActivity.this
                                , ProcesscontrollertasksActivity.class)
                                .putExtra("processcontrollerfilter", processcontrollerFilter)
                        );
                    }
                });

        findViewById(R.id.button_resolvedbyline).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // https://stackoverflow.com/questions/62671106/onactivityresult-method-is-deprecated-what-is-the-alternative

                // launch the choose line activity first
                Intent intent = new Intent(ProcesscontrollermainActivity.this, ProductionlinelistActivity.class);
                chooseLineActivityResultLauncher.launch(intent);

            }
        });



        // will be launched when ProductionlinelistActivity is closed,
        ActivityResultLauncher<Intent> chooseOperatorActivityResultLauncher = registerForActivityResult(

                new ActivityResultContracts.StartActivityForResult()
                , result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {

                        Intent data = result.getData(); // from ProductionlineAdapter -> ProductionlineViewHolder -> onClick
                        String pmsoperatorname = ((ProductionoperatorArticles) data.getSerializableExtra("article")).getOperatorname();

                        Log.e("PCM", "Choose pms_operators.operatorname onActivityResult -> " + pmsoperatorname);

                        //Getting the passed result
                        processcontrollerFilter.setPmsoperatorname(pmsoperatorname);
                        processcontrollerFilter.setResolved("YES"); // qaoperatorname was set into consumeQaoperatorname(loginResponse)
                        processcontrollerFilter.setUnresolved("");

                        startActivity(new Intent(
                                ProcesscontrollermainActivity.this
                                , ProcesscontrollertasksActivity.class)
                                .putExtra("processcontrollerfilter", processcontrollerFilter)
                        );
                    }
                });

        findViewById(R.id.button_resolvedbyoperator).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // https://stackoverflow.com/questions/62671106/onactivityresult-method-is-deprecated-what-is-the-alternative

                // launch the choose line activity first
                Intent intent = new Intent(ProcesscontrollermainActivity.this, ProductionoperatorlistActivity.class);
                chooseOperatorActivityResultLauncher.launch(intent);

            }
        });


        // button back to process controller list
        findViewById(R.id.button_processcontroller).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(ProcesscontrollermainActivity.this, LoginnamelistActivity.class));
            }
        });


        // return back to main activity
        findViewById(R.id.button_mainactivity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Canceled", Toast.LENGTH_LONG).show();
                finish();
                startActivity(new Intent(ProcesscontrollermainActivity.this, MainActivity.class));
            }
        });

    }


    void consumeQaoperatorname(String loginname){

        // information goes into activity title
        TextView title = (TextView) findViewById(R.id.title);
        title.setText(loginname);

        // also will go into filter criteria that will be carried on through subsequent activities
        processcontrollerFilter = new ProcesscontrollerFilter(loginname);

    }


}
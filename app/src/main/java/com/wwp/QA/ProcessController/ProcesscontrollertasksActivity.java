package com.wwp.QA.ProcessController;

import static com.wwp.QA.Utils.Utils.ObjectToMap;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.blikoon.qrcodescanner.QrCodeActivity;
import com.wwp.QA.Utils.PostJSON;
import com.wwp.QA.R;
import com.wwp.QA.RoomDatabase.DatabaseClient;
import com.wwp.QA.RoomDatabase.SysadminEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// Process controller Unresolved All Articles
public class ProcesscontrollertasksActivity extends AppCompatActivity implements ProcesscontrollerClickOnArticle {

    ArrayList<ProcesscontrollerArticles> articleArrayList = new ArrayList<>();
    ProcesscontrollerAdapter newsAdapter;
    RecyclerView rvHeadline;
    ProcesscontrollerViewModel newsViewModel;

    ProcesscontrollerFilter processcontrollerFilter;




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

                        finish();
                        startActivity(new Intent(ProcesscontrollertasksActivity.this
                                , ProcesscontrollerdefectsActivity.class)
                                .putExtra("processcontrollerfilter", processcontrollerFilter)
                        );

                    } catch (ArrayIndexOutOfBoundsException e) {
                        Log.e("PCM", "myResult ->  does not contain: ~ ");
                    }

                }
            });

    @Override
    public void clickOnArticle(ProcesscontrollerArticles article) {

//        // implements interface ProcesscontrollerClickOnArticle because ProcessControllerAdapter onClick works different in Task than Defects Activities
//        processcontrollerFilter.setCheqa_target(article.getCheqa_target());
//        processcontrollerFilter.setUnresolved("YES");
//        processcontrollerFilter.setResolved("YES");
//
//        startActivity(new Intent(ProcesscontrollertasksActivity.this, ProcesscontrollerdefectsActivity.class)
//                .putExtra("processcontrollerfilter", processcontrollerFilter));

        Intent intent = new Intent(ProcesscontrollertasksActivity.this, QrCodeActivity.class);
        qrActivityResultLauncher.launch(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_processcontrollertasks);

        rvHeadline = findViewById(R.id.recyclerview_processcontroller);

        // extract qaoperatorname from processcontrolerfilter object from Intent
        processcontrollerFilter = (ProcesscontrollerFilter) getIntent().getSerializableExtra("processcontrollerfilter");
        //Log.e("PCU", "processcontrollerFilter.getQaoperatorname ->" + processcontrollerFilter.getQaoperatorname());

        setupRecyclerView();

        setArticleArrayList();
        
        findViewById(R.id.button_processcontrollermain).setOnClickListener(view -> {
            Toast.makeText(getApplicationContext(), "Canceled", Toast.LENGTH_LONG).show();
            finish();
            startActivity(new Intent(ProcesscontrollertasksActivity.this, ProcesscontrollermainActivity.class)
                    .putExtra("loginname", processcontrollerFilter.getQaoperatorname()));
        });

    }

    private void setupRecyclerView() {
        if (newsAdapter == null) {

            Log.e("PCU", "setupRecyclerView -> will show " + articleArrayList.size() + " tasks");
            //if (!articleArrayList.isEmpty()) {

                newsAdapter = new ProcesscontrollerAdapter(ProcesscontrollertasksActivity.this, articleArrayList);

                rvHeadline.setLayoutManager(new LinearLayoutManager(this));
                rvHeadline.setAdapter(newsAdapter);
                rvHeadline.setItemAnimator(new DefaultItemAnimator());
                rvHeadline.setNestedScrollingEnabled(true);
            //}

        } else {
            newsAdapter.notifyDataSetChanged();
        }
    }


    private PostJSON preparePostJSON(String action, ProcesscontrollerFilter processcontrollerFilter, String authKey){

        Map<String,String> dataset; // "dataset":{"operatorid":"MUSAKA", "salary":"1500"}
        String[] authkey;           // "authkey":[{"authkey":"232546346356"}]

        // POST parameter will be like this one:
        // { "action":"TODAYTASK"
        //  ,"dataset":{"operatorid":"000397"}
        //  ,"authkey":[{"authkey":"b8da5853775b6532fdfcbe1ee50832bb"}]
        // }

        // "dataset":{"operatorid":"MUSAKA", "salary":"1500"}
        dataset = ObjectToMap(processcontrollerFilter);

        // "authkey":[{"authkey":"232546346356"}]
        authkey = new String[]{"authkey"};

        // check JSON syntax here: https://jsonlint.com/

        // https://mkyong.com/java/how-do-convert-java-object-to-from-json-format-gson-api/

        return new PostJSON( action
                            ,dataset
                            ,authkey
        ); //postJSON;
    }


    // get the room database to see the web address where to looking for
    // then the web API address is get from room database the API is called
    private void setArticleArrayList(){

        class GetTasks extends AsyncTask<Void, Void, SysadminEntity> {

            @Override
            protected SysadminEntity doInBackground(Void... voids) {

                //Log.e("PCU", "setArticleArrayList GetDataAroundTask.doInBackground()");

                // obtain webaddress set into sysadmin room database to call it for article array list

                return DatabaseClient
                        .getInstance(getApplicationContext())
                        .getAppDatabase()
                        .sysadminDao()
                        .getActivewebadress(); // goes into onPostExecute method as parameter
            }

            @Override
            protected void onPostExecute(SysadminEntity webaddresses) {

                PostJSON postJSON = preparePostJSON("TODAYTASKS"
                                                    , processcontrollerFilter
                                                    , ""
                ); // POST parameters for URL call

                super.onPostExecute(webaddresses);

                //Log.e("PCU", "setArticleArrayList GetDataAroundTask.onPostExecute()");
                Log.e("PCU", "webaddress -> " + webaddresses);

                // injects my ProcesscontrollerViewModel into newsViewModel property
                newsViewModel = ViewModelProviders
                                      .of(ProcesscontrollertasksActivity.this)
                                      .get(ProcesscontrollerViewModel.class);

                // init the ViewModel by calling URL obtained from webaddresses.getWebaddress() and retrieve the response obtained from API into MutableLiveData into PCViewModel
                // this will trigger the call of the url
                newsViewModel.init(webaddresses.getWebaddress(), postJSON); // ProcesscontrollerViewModel.init() will establish POST parameters that will be sent to API

                // MutableLiveData from PCViewModel will be observed and when changes occurs they will be set into newsResponse and the following code will be executed
                newsViewModel.getMutableLiveData().observe(ProcesscontrollertasksActivity.this //  The LifecycleOwner which controls the observer
                                             , newsResponse -> { // The observer that will receive the events. newsResponse is an object of class ProcesscontrollerResponse

                    // The events are dispatched on the main thread
                    // Any time the newResponse is changed this code will be executed into the main thread
                    if (newsResponse!=null) {

                        Log.e("PCU", "ResponseMessage ->" + newsViewModel.getProcesscontrollerRepository().getErrMsg());

                        // here newResponse has contained the data from response.body():
                        // {
                        //   "status": "ok",
                        //   "totalResults": 10,
                        //   "articles": [{article},{article} ...{article}]
                        // }
                        // packed into ProcesscontrollerResponse object.
                        // Now I have to extract the list of articles from the ProcesscontrollerResponse object and send them to an Adapter
                        List<ProcesscontrollerArticles> newsArticles = newsResponse.getArticles();

                        articleArrayList.addAll(newsArticles);
                        newsAdapter.notifyDataSetChanged();

                    } else {

                        Log.e("PCU", "Null response -> " + newsViewModel.getProcesscontrollerRepository().getErrMsg());
                        Toast.makeText(  getApplicationContext()
                                , newsViewModel.getProcesscontrollerRepository().getErrMsg()
                                , Toast.LENGTH_LONG
                        ).show();
                    }
                });



            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();

    }

}
package com.wwp.QA.ProductionOperator;

import static com.wwp.QA.Utils.Utils.ObjectToMap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.wwp.QA.ProcessController.ProcesscontrollerFilter;
import com.wwp.QA.ProcessController.ProcesscontrollermainActivity;

import com.wwp.QA.R;
import com.wwp.QA.RoomDatabase.DatabaseClient;
import com.wwp.QA.RoomDatabase.SysadminEntity;
import com.wwp.QA.Utils.PostJSON;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProductionoperatorlistActivity extends AppCompatActivity {
    
    RecyclerView rvHeadline;
    ProcesscontrollerFilter processcontrollerFilter; // for passing backe the login name
    ProductionoperatorFilter productionoperatorFilter;
    ProductionoperatorAdapter newsAdapter;
    ArrayList<ProductionoperatorArticles> articleArrayList = new ArrayList<>();
    ProductionoperatorViewModel newsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productionoperatorlist);

        rvHeadline = findViewById(R.id.recyclerview_productionoperator);

        // it is not really need for this into ProductionoperatorlistActivity but it hea to be sent back to ProcesscontrollermainActivity if the button is clicked
        processcontrollerFilter = (ProcesscontrollerFilter) getIntent().getSerializableExtra("processcontrollerfilter");

        // for Productionoperator API
        productionoperatorFilter = new ProductionoperatorFilter();

        setupRecyclerView();

        setArticleArrayList();

        findViewById(R.id.button_processcontrollermain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Go back to Process controller main", Toast.LENGTH_LONG).show();
                finish();
                // pass back via Intent the ProcesscontrollerFilter
                startActivity(new Intent(ProductionoperatorlistActivity.this, ProcesscontrollermainActivity.class)
                        .putExtra("loginname", processcontrollerFilter.getQaoperatorname()));
            }
        });

    }

    private void setupRecyclerView() {
        if (newsAdapter == null) {

            Log.e("POL", "setupRecyclerView -> will show " + articleArrayList.size() + " tasks");

            newsAdapter = new ProductionoperatorAdapter(ProductionoperatorlistActivity.this, articleArrayList);

            rvHeadline.setLayoutManager(new LinearLayoutManager(this));
            rvHeadline.setAdapter(newsAdapter);
            rvHeadline.setItemAnimator(new DefaultItemAnimator());
            rvHeadline.setNestedScrollingEnabled(true);

        } else {
            newsAdapter.notifyDataSetChanged();
        }
    }





    private PostJSON preparePostJSON(String action, ProductionoperatorFilter productionoperatorFilter, String authKey){

        Map<String,String> dataset; // "dataset":{"arename":"MUSAKA", "areacode":"SS-35", "linename":"L35"}
        String[] authkey;           // "authkey":[{"authkey":"232546346356"}]

        // POST parameter will be like this one:
        // { "action":"TODAYTASK"
        //  ,"dataset":{"arename":"MUSAKA", "areacode":"SS-35", "linename":"L35"}
        //  ,"authkey":[{"authkey":"b8da5853775b6532fdfcbe1ee50832bb"}]
        // }

        dataset = ObjectToMap(productionoperatorFilter);

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

                Log.e("POL", "setArticleArrayList GetTasks.doInBackground()");

                // obtain webaddress set into sysadmin room database to call it for article array list

                SysadminEntity activewebadress = DatabaseClient
                        .getInstance(getApplicationContext())
                        .getAppDatabase()
                        .sysadminDao()
                        .getActivewebadress();
                return activewebadress; // goes into onPostExecute method as parameter
            }

            @Override
            protected void onPostExecute(SysadminEntity webaddresses) {

                PostJSON postJSON = preparePostJSON("GETOPERATORS"
                        , productionoperatorFilter
                        , ""
                ); // POST parameters for URL call

                super.onPostExecute(webaddresses);

                //Log.e("PCU", "setArticleArrayList GetTasks.onPostExecute()");
                Log.e("POL", "webaddress -> " + webaddresses.getWebaddress());

                // injects my ProcesscontrollerViewModel into newsViewModel property
                newsViewModel = ViewModelProviders
                        .of(ProductionoperatorlistActivity.this)
                        .get(ProductionoperatorViewModel.class);

                // init the ViewModel by calling URL obtained from webaddresses.getWebaddress() and retrieve the response obtained from API into MutableLiveData into PCViewModel
                // this will trigger the call of the url
                newsViewModel.init(webaddresses.getWebaddress(), postJSON); // ProcesscontrollerViewModel.init() will establish POST parameters that will be sent to API

                // MutableLiveData from PCViewModel will be observed and when changes occurs they will be set into newsResponse and the following code will be executed
                newsViewModel.getMutableLiveData().observe(ProductionoperatorlistActivity.this //  The LifecycleOwner which controls the observer
                        , newsResponse -> { // The observer that will receive the events. newsResponse is an object of class ProcesscontrollerResponse

                            // The events are dispatched on the main thread
                            // Any time the newResponse is changed this code will be executed into the main thread
                            if (newsResponse!=null) {

                                Log.e("POL", "ResponseErrMessage ->" + newsViewModel.getProductionoperatorRepository().getErrMsg());

                                // here newResponse has contained the data from response.body():
                                // {
                                //   "status": "ok",
                                //   "totalResults": 10,
                                //   "articles": [{article},{article} ...{article}]
                                // }
                                // packed into ProductionoperatorResponse object.
                                // Now I have to extract the list of articles from the ProcesscontrollerResponse object and send them to an Adapter
                                List<ProductionoperatorArticles> newsArticles = newsResponse.getArticles();

                                articleArrayList.addAll(newsArticles);

                                newsAdapter.notifyDataSetChanged();

                            } else {

                                Log.e("POL", "Null response -> " + newsViewModel.getProductionoperatorRepository().getErrMsg());
                                Toast.makeText(  getApplicationContext()
                                        , newsViewModel.getProductionoperatorRepository().getErrMsg()
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
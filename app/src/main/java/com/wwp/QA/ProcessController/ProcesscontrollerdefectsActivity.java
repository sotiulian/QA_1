package com.wwp.QA.ProcessController;

import static com.wwp.QA.Utils.Utils.ObjectToMap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wwp.QA.Machine.MachineAdapter;
import com.wwp.QA.Machine.MachineArticles;
import com.wwp.QA.Machine.MachineFilter;
import com.wwp.QA.Machine.MachineViewModel;
import com.wwp.QA.QACheck.QACheckViewModel;
import com.wwp.QA.QACheck.QAFilter;
import com.wwp.QA.QACheck.QARepository;
import com.wwp.QA.QACheck.QAResponse;
import com.wwp.QA.R;
import com.wwp.QA.RoomDatabase.DatabaseClient;
import com.wwp.QA.RoomDatabase.SysadminEntity;
import com.wwp.QA.Scoring.ScoringAdapter;
import com.wwp.QA.Scoring.ScoringArticles;
import com.wwp.QA.Scoring.ScoringFilter;
import com.wwp.QA.Scoring.ScoringOOAdapter;
import com.wwp.QA.Scoring.ScoringViewModel;
import com.wwp.QA.Utils.PostJSON;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ProcesscontrollerdefectsActivity extends AppCompatActivity implements ProcesscontrollerClickOnArticle {

    ProcesscontrollerFilter processcontrollerFilter; // will be created from Intent into Activity.onCreate method
    MachineFilter machineFilter = new MachineFilter("");
    QAFilter qaFilter = new QAFilter();

    ProcesscontrollerViewModel newsProcesscontrollerViewModel;
    MachineViewModel newsMachineViewModel;
    QACheckViewModel qaCheckViewModel;
    ScoringViewModel newsScoringViewModel;

    ArrayList<ProcesscontrollerArticles> articleProcesscontrollerArrayList = new ArrayList<>();
    ArrayList<MachineArticles> articleMachineArrayList = new ArrayList<>();
    ArrayList<ScoringArticles> articleScoringArrayList = new ArrayList<>();

    ProcesscontrollerAdapter newsAdapter;
    MachineAdapter newsMachineAdapter;
    ScoringAdapter newsScoringAdapter;
    ScoringOOAdapter newsScoringOOAdapter;


    RecyclerView rvHeadline;
    RecyclerView rvMachineHeadline;

    TextView tvqaoperatorname, machineid ;

    TextView tvpiecespercheck, tvacceptablelevelofdefects, tvactualdefectsfound, tvbalance;
    Button buttonplus, buttonminus, buttonsavedefects;

    String qaoperatorname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_processcontroller_defects);

        // extract machineid and qaoperatorname from processcontrolerfilter object from Intent
        processcontrollerFilter = (ProcesscontrollerFilter) getIntent().getSerializableExtra("processcontrollerfilter");
        //Log.e("PCU", "processcontrollerFilter.getQaoperatorname ->" + processcontrollerFilter.getQaoperatorname());

        rvHeadline = findViewById(R.id.recyclerview_processcontroller);
        rvMachineHeadline = findViewById(R.id.recyclerview_machine);

        tvpiecespercheck = findViewById(R.id.tvpiecespercheck);
        tvacceptablelevelofdefects = findViewById(R.id.tvacceptablelevelofdefects);
        tvactualdefectsfound = findViewById(R.id.tvactualdefectsfound);
        tvbalance = findViewById(R.id.tvbalance);

        buttonplus = findViewById(R.id.button_plus);
        buttonplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // https://stackoverflow.com/questions/7938516/continuously-increase-integer-value-as-the-button-is-pressed

                // get the article from LiveData
                ProcesscontrollerArticles article = qaCheckViewModel
                        .getQacheck()
                        .getValue();

                if (article==null) {

                    Log.e(this.getClass().getSimpleName(), "qa_target.cheie -> null ");

                    Toast.makeText(getApplicationContext(), "Nothing to increase !", Toast.LENGTH_LONG).show();

                } else {

                    // Modify the property we want
                    article.setActualdefectsfoundpieces(article.getActualdefectsfoundpieces() == null
                                                         ? 0
                                                         : article.getActualdefectsfoundpieces()
                                                        );

                    if (article.getActualdefectsfoundpieces()+1 <= article.getQapiecespercheck())
                        article.setActualdefectsfoundpieces(article.getActualdefectsfoundpieces()+1);


                    article.setBalance((article.getQapiecespercheck() == null)
                                    ? 0
                                    : (article.getQapiecespercheck() == 0
                                       ? 1
                                       : ((float)article.getActualdefectsfoundpieces() / article.getQapiecespercheck() * 100.00 <= article.getQaacceptablelevelofdefectspercheck()
                                          ? 1
                                          : 0
                                         )
                                      )
                    );

                    // update the modified article into LiveData
                    qaCheckViewModel
                            .getQacheck()
                            .setValue(article);

                }

            }

        });

        buttonminus = findViewById(R.id.button_minus);
        buttonminus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // https://stackoverflow.com/questions/7938516/continuously-increase-integer-value-as-the-button-is-pressed

                // get the article from LiveData
                ProcesscontrollerArticles article = qaCheckViewModel
                        .getQacheck()
                        .getValue();

                if (article==null) {

                    Log.e(this.getClass().getSimpleName(), "qa_target.cheie -> null ");

                    Toast.makeText(getApplicationContext(), "Nothing to decrease !", Toast.LENGTH_LONG).show();

                } else {

                    int currentValue = (article.getActualdefectsfoundpieces() == null
                            ? 0
                            : article.getActualdefectsfoundpieces());

                    // Modify the property we want
                    article.setActualdefectsfoundpieces(currentValue - 1 >= 0
                            ? currentValue - 1
                            : currentValue);

                    article.setBalance((article.getQapiecespercheck() == null)
                                    ? 0
                                    : (article.getQapiecespercheck() == 0
                                       ? 1
                                       : ((float)article.getActualdefectsfoundpieces() / article.getQapiecespercheck() * 100.00 <= article.getQaacceptablelevelofdefectspercheck()
                                          ? 1
                                          : 0
                                         )
                                       )
                    );


                    // update the modified article into LiveData
                    qaCheckViewModel
                            .getQacheck()
                            .setValue(article);

                }
            }
        });

        // will add or update qa_actual for qa_target than close activity
        findViewById(R.id.button_savedefects).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ProcesscontrollerArticles article = qaCheckViewModel.getQacheck()
                                                                    .getValue();

                if (article==null) {

                    Log.e(this.getClass().getSimpleName(), "qa_target.cheie -> null ");

                    Toast.makeText(getApplicationContext(), "Nothing to save !", Toast.LENGTH_LONG).show();

                    //finish();
                    //startActivity(new Intent(ProcesscontrollerdefectsActivity.this, ProcesscontrollermainActivity.class)
                    //        //.putExtra("loginname", processcontrollerFilter.getQaoperatorname()));
                    //        .putExtra("loginname", qaoperatorname)); // retrimite numele QA operatorului in filtru


                } else {

                    if (!qaoperatorname.isEmpty()) { // nu se poate pierde dar s-a pierdut si nu stiu cum asa ca pun protectie

                        // here must be the code for saving qa_actual
                        Log.e(this.getClass().getSimpleName(), "qa_target.cheie ->" + article.getCheqa_target());

                        Toast.makeText(getApplicationContext(), "Save defects into database", Toast.LENGTH_LONG).show();

                        saveDataQAActual();

                    } else {

                        Log.e(this.getClass().getSimpleName(), "qa_target.cheie ->" + article.getCheqa_target());

                        Toast.makeText(getApplicationContext(), "Save defects into database", Toast.LENGTH_LONG).show();
                    }

                }

            }
        });

        // will add or update qa_actual for qa_target than close activity
        findViewById(R.id.button_newtarget).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getApplicationContext(), "Create new target with machine data !", Toast.LENGTH_LONG).show();

                Log.e("PDA", "processcontrollerFilter.getQaoperatorname() -> " + processcontrollerFilter.getQaoperatorname());
                Log.e("PDA", "processcontrollerFilter.getMachineid() -> " + processcontrollerFilter.getMachineid());

                insertNewQATarget();

            }
        });

        // will add or update qa_actual for qa_target than close activity
        findViewById(R.id.button_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getApplicationContext(), "Cancel activity !", Toast.LENGTH_LONG).show();

                finish();
                startActivity(new Intent(ProcesscontrollerdefectsActivity.this, ProcesscontrollermainActivity.class)
                        .putExtra("loginname", processcontrollerFilter.getQaoperatorname()));

            }
        });


        initActivity();

        setupRecyclerView();

        setupMachineRecyclerView();

        setupScoringViews();

        setupScoringOOViews();

    }


    @Override
    public void clickOnArticle(ProcesscontrollerArticles article) {

        Log.e("PCD", "article.getQapiecespercheck() -> " + article.getQapiecespercheck());

        qaCheckViewModel
                .getQacheck() // extract current article from ModelView
                .setValue(article); // set parameter: article into ModelView and informs observer about changing (triggers onChange)
    }


    private void resolveMachineArticle(String machineid){

        machineFilter.setMachineid(machineid);

        // call AsycronousTask
        setDataAroundMachineID();
    }

    private void initActivity(){

        qaoperatorname = processcontrollerFilter.getQaoperatorname();

        // information goes into activity title
        tvqaoperatorname = (TextView) findViewById(R.id.tvqaoperator);
        tvqaoperatorname.setText(qaoperatorname);

        // information goes into activity title
        machineid = (TextView) findViewById(R.id.machineid);
        machineid.setText(processcontrollerFilter.getMachineid());

        // set data around machineid
        resolveMachineArticle(processcontrollerFilter.getMachineid());

        // set both scorring boards: left side by operator and right side by operator and operation
        getScoring(processcontrollerFilter.getMachineid());


        // this code is here because QACheck is not presented into a list and not having an adapter
        // set the observer on selected article (when onClick event occurs in Adapter)
        qaCheckViewModel = new ViewModelProvider(this).get(QACheckViewModel.class);

        qaCheckViewModel
                .getQacheck()
                .observe(this   // androidx.lifecycle.LifecycleOwner
                        , new Observer<ProcesscontrollerArticles>() { // androidx.lifecycle.Observer
                            @Override
                            public void onChanged(ProcesscontrollerArticles article) { // this method will be called anytime when the method: setValue(article) is running

                                tvpiecespercheck.setText(String.format(Locale.US, "%d", article.getQapiecespercheck()));
                                tvacceptablelevelofdefects.setText(String.format(Locale.US, "%d %%", article.getQaacceptablelevelofdefectspercheck()));
                                tvactualdefectsfound.setText(String.format(Locale.US, "%d", article.getActualdefectsfoundpieces()));
                                tvbalance.setText((article.getActualdefectsfoundpieces()==0 || article.getBalance()==1) ? "ACCEPTED" : "REJECTED");

                                if (article.getBalance()!=null)
                                    switch (article.getBalance()) {
                                        case 1:
                                            tvbalance.setTextColor( ContextCompat.getColor(ProcesscontrollerdefectsActivity.this, R.color.colorAccent));
                                            break;
                                        default :
                                            tvbalance.setTextColor( ContextCompat.getColor(ProcesscontrollerdefectsActivity.this, R.color.colorRed));
                                    }
                            }
                        });
    }

    private void setupRecyclerView() {

        if (newsAdapter == null) {

            Log.e("PCD", "setupRecyclerView -> will show " + articleProcesscontrollerArrayList.size() + " tasks");
            //if (!articleArrayList.isEmpty()) {

            newsAdapter = new ProcesscontrollerAdapter(ProcesscontrollerdefectsActivity.this, articleProcesscontrollerArrayList);

            rvHeadline.setLayoutManager(new LinearLayoutManager(this));
            rvHeadline.setAdapter(newsAdapter);
            rvHeadline.setItemAnimator(new DefaultItemAnimator());
            rvHeadline.setNestedScrollingEnabled(true);
            //}

        } else {
            // This event does not specify what about the data set has changed, forcing any observers to assume that all
            // existing items and structure may no longer be valid.
            // LayoutManagers will be forced to fully rebind and relayout all visible views.
            newsAdapter.notifyDataSetChanged();
        }
    }

    private void setupMachineRecyclerView() {
        if (newsMachineAdapter == null) {

            Log.e("PCD", "setupMachineRecyclerView -> will show " + articleMachineArrayList.size() + " tasks");
            //if (!articleArrayList.isEmpty()) {

            newsMachineAdapter = new MachineAdapter(ProcesscontrollerdefectsActivity.this, articleMachineArrayList);

            rvMachineHeadline.setLayoutManager(new LinearLayoutManager(this));
            rvMachineHeadline.setAdapter(newsMachineAdapter);
            rvMachineHeadline.setItemAnimator(new DefaultItemAnimator());
            rvMachineHeadline.setNestedScrollingEnabled(true);
            //}

        } else {
            // This event does not specify what about the data set has changed, forcing any observers to assume that all
            // existing items and structure may no longer be valid.
            // LayoutManagers will be forced to fully rebind and relayout all visible views.
            newsMachineAdapter.notifyDataSetChanged(); // call onBindViewHolder from Adapter
        }
    }


    private void setupScoringViews() {
        if (newsScoringAdapter == null) {

            Log.e("PCD", "setupScoringViews -> will show " + articleScoringArrayList.size() + " tasks");
            //if (!articleArrayList.isEmpty()) {

            newsScoringAdapter = new ScoringAdapter(ProcesscontrollerdefectsActivity.this
                                                   ,findViewById(R.id.vscoringoperator) // findViewById(android.R.id.content) //  gives you the root element of a view, without having to know its actual name/type/ID
                                                   , articleScoringArrayList);

            //}

        } else {
            // This event does not specify what about the data set has changed, forcing any observers to assume that all
            // existing items and structure may no longer be valid.
            // LayoutManagers will be forced to fully rebind and relayout all visible views.
            newsScoringAdapter.notifyDataSetChanged(); // call onBindViewHolder from Adapter
        }
    }

    private void setupScoringOOViews() {
        if (newsScoringOOAdapter == null) {

            Log.e("PCD", "setupScoringOOViews -> will show " + articleScoringArrayList.size() + " tasks");
            //if (!articleArrayList.isEmpty()) {

            newsScoringOOAdapter = new ScoringOOAdapter(
                    ProcesscontrollerdefectsActivity.this
                    , findViewById(R.id.vscoringoooperator) //findViewById(android.R.id.content) //  gives you the root element of a view, without having to know its actual name/type/ID
                    , articleScoringArrayList);

            //}

        } else {
            // This event does not specify what about the data set has changed, forcing any observers to assume that all
            // existing items and structure may no longer be valid.
            // LayoutManagers will be forced to fully rebind and relayout all visible views.
            newsScoringOOAdapter.notifyDataSetChanged(); // call onBindViewHolder from Adapter
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


    private PostJSON prepareMachinePostJSON(String action, MachineFilter machineFilter, String authKey){

        Map<String,String> dataset; // "dataset":{"operatorid":"MUSAKA", "salary":"1500"}
        String[] authkey;           // "authkey":[{"authkey":"232546346356"}]

        // POST parameter will be like this one:
        // { "action":"TODAYTASK"
        //  ,"dataset":{"operatorid":"000397"}
        //  ,"authkey":[{"authkey":"b8da5853775b6532fdfcbe1ee50832bb"}]
        // }

        // "dataset":{"operatorid":"MUSAKA", "salary":"1500"}
        dataset = ObjectToMap(machineFilter);

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
    private void setDataAroundMachineID(){

        class GetDataAroundMachineID extends AsyncTask<Void, Void, SysadminEntity> {

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

                PostJSON postJSON = prepareMachinePostJSON("GETDATAAROUNDMACHINE"
                        , machineFilter
                        , ""
                ); // POST parameters for URL call

                super.onPostExecute(webaddresses);

                //Log.e("PCU", "setArticleArrayList GetDataAroundMachineID.onPostExecute()");
                Log.e("PCU", "webaddress -> " + webaddresses);

                // injects my ProcesscontrollerViewModel into newsViewModel property
                newsMachineViewModel = ViewModelProviders
                        .of(ProcesscontrollerdefectsActivity.this)
                        .get(MachineViewModel.class);

                // init the ViewModel by calling URL obtained from webaddresses.getWebaddress() and retrieve the response obtained from API into MutableLiveData into PCViewModel
                // this will trigger the call of the url
                newsMachineViewModel.init(webaddresses.getWebaddress(), postJSON); // ProcesscontrollerViewModel.init() will establish POST parameters that will be sent to API

                // MutableLiveData from PCViewModel will be observed and when changes occurs they will be set into newsResponse and the following code will be executed
                newsMachineViewModel.getMutableLiveData()
                                    .observe(ProcesscontrollerdefectsActivity.this //  The LifecycleOwner which controls the observer
                                            , newsResponse -> { // The observer that will receive the events. newsResponse is an object of class MachineResponse

                            // The events are dispatched on the main thread
                            // Any time the newResponse is changed this code will be executed into the main thread
                            if (newsResponse!=null) {

                                Log.e("PCD", "Error ResponseMessage ->" + newsMachineViewModel.getMachineRepository().getErrMsg());
                                Log.e("PCD setDataAroundMachineID", "newsResponse.getTotalResults() ->" + newsResponse.getTotalResults());

                                // here newResponse has contained the data from response.body():
                                // {
                                //   "status": "ok",
                                //   "totalResults": 10,
                                //   "articles": [{article},{article} ...{article}]
                                // }
                                // packed into ProcesscontrollerResponse object.
                                // Now I have to extract the list of articles from the ProcesscontrollerResponse object and send them to an Adapter
                                switch (newsResponse.getTotalResults()) {

                                    case 0:
                                        // cheqa_target was not found
                                        break;
                                    case 1:
                                        // cheqa_target was found

                                        // prepare the Filter for getting tasks
                                        processcontrollerFilter.setStylename(newsResponse.getArticles().get(0).getStylename());
                                        processcontrollerFilter.setQaoperatorname(tvqaoperatorname.getText().toString());
                                        processcontrollerFilter.setCheoperations(newsResponse.getArticles().get(0).getCheoperations());
                                        processcontrollerFilter.setResolved(""); // don't want resolved
                                        processcontrollerFilter.setUnresolved("YES"); // just want unresolved

                                        Log.e("PCD", "setArticleArrayList() ->"
                                                +" stylename: " + processcontrollerFilter.getStylename()
                                                +" operatorname: " + processcontrollerFilter.getQaoperatorname()
                                        );

                                        setArticleArrayList();

                                        break;
                                    default:
                                        // cannot get in here because just one machine can have logged operator

                                }

                                // choose which article from the list no matter is resolved or not
                                List<MachineArticles> newsArticles = newsResponse.getArticles();

                                articleMachineArrayList.addAll(newsArticles);

                                //  Notify any registered observers that the data set has changed
                                newsMachineAdapter.notifyDataSetChanged();

                            } else {

                                Log.e("PCD", "Null response -> " + newsMachineViewModel.getMachineRepository().getErrMsg());
                                Toast.makeText(  getApplicationContext()
                                        , newsMachineViewModel.getMachineRepository().getErrMsg()
                                        , Toast.LENGTH_LONG
                                ).show();
                            }
                        });



            }
        }

        GetDataAroundMachineID gt = new GetDataAroundMachineID();
        gt.execute();

    }



    private PostJSON prepareScoringPostJSON(String action, ScoringFilter scoringFilter, String authKey){

        Map<String,String> dataset; // "dataset":{"operatorid":"MUSAKA", "salary":"1500"}
        String[] authkey;           // "authkey":[{"authkey":"232546346356"}]

        // POST parameter will be like this one:
        // { "action":"TODAYTASK"
        //  ,"dataset":{"operatorid":"000397"}
        //  ,"authkey":[{"authkey":"b8da5853775b6532fdfcbe1ee50832bb"}]
        // }

        // "dataset":{"operatorid":"MUSAKA", "salary":"1500"}
        dataset = ObjectToMap(scoringFilter);

        // "authkey":[{"authkey":"232546346356"}]
        authkey = new String[]{"authkey"};

        // check JSON syntax here: https://jsonlint.com/

        // https://mkyong.com/java/how-do-convert-java-object-to-from-json-format-gson-api/

        return new PostJSON( action
                ,dataset
                ,authkey
        ); //postJSON;
    }



    private void getScoring(String machineID){

        class GetDataAroundTask extends AsyncTask<Void, Void, SysadminEntity> {

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


                // here must be the code for saving qa_actual
                Log.e(this.getClass().getSimpleName(), "machineID ->" + machineID);

                ScoringFilter scoringFilter = new ScoringFilter(machineID);


                PostJSON postJSON = prepareScoringPostJSON("GETSCORING"
                        , scoringFilter
                        , ""
                ); // POST parameters for URL call

                super.onPostExecute(webaddresses);

                //Log.e("PCU", "setArticleArrayList GetDataAroundTask.onPostExecute()");
                Log.e("PCD", "webaddress -> " + webaddresses.getWebaddress());
                Log.e("PCD", "postJSON -> " + postJSON.toString());

                // injects my ProcesscontrollerViewModel into newsViewModel property
                newsScoringViewModel = ViewModelProviders
                        .of(ProcesscontrollerdefectsActivity.this)
                        .get(ScoringViewModel.class);

                // init the ViewModel by calling URL obtained from webaddresses.getWebaddress() and retrieve the response obtained from API into MutableLiveData into PCViewModel
                // this will trigger the call of the url
                newsScoringViewModel.init(webaddresses.getWebaddress(), postJSON); // ProcesscontrollerViewModel.init() will establish POST parameters that will be sent to API

                // MutableLiveData from PCViewModel will be observed and when changes occurs they will be set into newsResponse and the following code will be executed
                newsScoringViewModel.getMutableLiveData()
                        .observe(ProcesscontrollerdefectsActivity.this //  The LifecycleOwner which controls the observer
                                , newsResponse -> { // The observer that will receive the events. newsResponse is an object of class MachineResponse

                                    // The events are dispatched on the main thread
                                    // Any time the newResponse is changed this code will be executed into the main thread
                                    if (newsResponse!=null) {

                                        Log.e("PCD", "Error ResponseMessage ->" + newsScoringViewModel.getScoringRepository().getErrMsg());
                                        Log.e("PCD getScoring", "newsResponse.getTotalResults() ->" + newsResponse.getTotalResults());

                                        // here newResponse has contained the data from response.body():
                                        // {
                                        //   "status": "ok",
                                        //   "totalResults": 10,
                                        //   "articles": [{article},{article} ...{article}]
                                        // }
                                        // packed into ProcesscontrollerResponse object.
                                        // Now I have to extract the list of articles from the ProcesscontrollerResponse object and send them to an Adapter
                                        switch (newsResponse.getTotalResults()) {

                                            case 0:
                                                //
                                                break;
                                            case 1:
                                                //
                                                break;
                                            case 2:
                                                //

                                                break;
                                            default:
                                                // cannot get in here because just one machine can have logged operator

                                        }

                                        // choose which article from the list no matter is resolved or not
                                        List<ScoringArticles> newsArticles = newsResponse.getArticles();

                                        articleScoringArrayList.addAll(newsArticles);

                                        // notifyItemChanged(int)
                                        // notifyItemInserted(int)
                                        // notifyItemRemoved(int)
                                        // notifyItemRangeChanged(int, int)
                                        // notifyItemRangeInserted(int, int)
                                        // notifyItemRangeRemoved(int, int)

                                        //  Notify any registered observers that the data set has changed
                                        newsScoringAdapter.notifyDataSetChanged();
                                        newsScoringOOAdapter.notifyDataSetChanged();

                                    } else {

                                        Log.e("PCD", "Null response -> " + newsScoringViewModel.getScoringRepository().getErrMsg());
                                        Toast.makeText(  getApplicationContext()
                                                , newsScoringViewModel.getScoringRepository().getErrMsg()
                                                , Toast.LENGTH_LONG
                                        ).show();
                                    }
                                });


            }
        }

        GetDataAroundTask gt = new GetDataAroundTask();
        gt.execute();

    }





    private PostJSON prepareQAPostJSON(String action, QAFilter qaFilter, String authKey){

        Map<String,String> dataset; // "dataset":{"operatorid":"MUSAKA", "salary":"1500"}
        String[] authkey;           // "authkey":[{"authkey":"232546346356"}]

        // POST parameter will be like this one:
        // { "action":"TODAYTASK"
        //  ,"dataset":{"operatorid":"000397"}
        //  ,"authkey":[{"authkey":"b8da5853775b6532fdfcbe1ee50832bb"}]
        // }

        // "dataset":{"operatorid":"MUSAKA", "salary":"1500"}
        dataset = ObjectToMap(qaFilter);

        // "authkey":[{"authkey":"232546346356"}]
        authkey = new String[]{"authkey"};

        // check JSON syntax here: https://jsonlint.com/

        // https://mkyong.com/java/how-do-convert-java-object-to-from-json-format-gson-api/

        return new PostJSON( action
                ,dataset
                ,authkey
        ); //postJSON;
    }


    private void saveDataQAActual(){

        class GetDataAroundTask extends AsyncTask<Void, Void, SysadminEntity> {

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


                // here must be the code for saving qa_actual
                Log.e(this.getClass().getSimpleName(), "qa_target.cheie ->" + qaCheckViewModel.getQacheck().getValue().getCheqa_target());

                qaFilter.setCheqa_target(qaCheckViewModel.getQacheck().getValue().getCheqa_target());
                Log.e("PCD", "cheqa_target -> " + qaFilter.getCheqa_target());
                qaFilter.setBundleordernumericid(articleMachineArrayList.get(articleMachineArrayList.size()-1).getBundleordernumericid());
                Log.e("PCD", "bundleordernumericid -> " + articleMachineArrayList.get(articleMachineArrayList.size()-1).getBundleordernumericid());
                Log.e("PCD", "bundleordernumericid -> " + qaFilter.getBundleordernumericid());
                qaFilter.setChepms_areas(articleMachineArrayList.get(articleMachineArrayList.size()-1).getChepms_areas()); // of qa_actual
                Log.e("PCD", "chepms_areas -> " + qaFilter.getChepms_areas());
                qaFilter.setQaoperatorname(qaoperatorname); // of qa_actual
                Log.e("PCD", "qaoperatorname -> " + qaFilter.getQaoperatorname());
                qaFilter.setActualdefectsfoundpieces(qaCheckViewModel.getQacheck().getValue().getActualdefectsfoundpieces());
                Log.e("PCD", "actualdefectsfoundpieces -> " + qaFilter.getActualdefectsfoundpieces());

                PostJSON postJSON = prepareQAPostJSON("SAVEDATAQAACTUAL"
                        , qaFilter
                        , ""
                ); // POST parameters for URL call

                super.onPostExecute(webaddresses);

                //Log.e("PCU", "setArticleArrayList GetDataAroundTask.onPostExecute()");
                Log.e("PCD", "webaddress -> " + webaddresses.getWebaddress());
                Log.e("PCD", "postJSON -> " + postJSON.toString());

                // set the values that will go into @Body in the API to be POST on URL Call
                MutableLiveData<QAResponse> articleResponse = QARepository.getInstance().updateQAArticles(webaddresses.getWebaddress(), postJSON);

                Log.e("PCD", "articleResponse -> " + articleResponse.toString());

                finish();
                /*
                startActivity(new Intent(ProcesscontrollerdefectsActivity.this, ProcesscontrollermainActivity.class)
                        .putExtra("loginname", processcontrollerFilter.getQaoperatorname()));
                */

                startActivity(new Intent(ProcesscontrollerdefectsActivity.this, ProcesscontrollerdefectsActivity.class)
                        .putExtra("processcontrollerfilter", processcontrollerFilter));

            }
        }

        GetDataAroundTask gt = new GetDataAroundTask();
        gt.execute();

    }

    private PostJSON prepareMachineArticlePostJSON(String action, MachineArticles machineArticle, String authKey){

        Map<String,String> dataset; // "dataset":{"operatorid":"MUSAKA", "salary":"1500"}
        String[] authkey;           // "authkey":[{"authkey":"232546346356"}]

        // POST parameter will be like this one:
        // { "action":"TODAYTASK"
        //  ,"dataset":{"operatorid":"000397"}
        //  ,"authkey":[{"authkey":"b8da5853775b6532fdfcbe1ee50832bb"}]
        // }

        // "dataset":{"operatorid":"MUSAKA", "salary":"1500"}
        dataset = ObjectToMap(machineArticle);

        // "authkey":[{"authkey":"232546346356"}]
        authkey = new String[]{"authkey"};

        // check JSON syntax here: https://jsonlint.com/

        // https://mkyong.com/java/how-do-convert-java-object-to-from-json-format-gson-api/

        return new PostJSON( action
                ,dataset
                ,authkey
        ); //postJSON;
    }



    private void insertNewQATarget(){

        class GetDataAroundTask extends AsyncTask<Void, Void, SysadminEntity> {

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


                // here must be the code for saving qa_actual
                Log.e(this.getClass().getSimpleName(), "qa_target.cheie ->" + processcontrollerFilter.getMachineid());

                MachineArticles machineArticles = new MachineArticles(processcontrollerFilter.getMachineid());

                machineArticles.setChepms_operators(articleMachineArrayList.get(articleMachineArrayList.size()-1).getChepms_operators());
                Log.e("PCD", "setChepms_operators -> " + machineArticles.getChepms_operators());
                machineArticles.setBundleordernumericid(articleMachineArrayList.get(articleMachineArrayList.size()-1).getBundleordernumericid());
                Log.e("PCD", "setBundleordernumericid -> " + machineArticles.getBundleordernumericid());
                machineArticles.setChepms_areas(articleMachineArrayList.get(articleMachineArrayList.size()-1).getChepms_areas());
                Log.e("PCD", "setChepms_areas -> " + machineArticles.getChepms_areas());
                machineArticles.setCheoperations(articleMachineArrayList.get(articleMachineArrayList.size()-1).getCheoperations());
                Log.e("PCD", "setCheoperations -> " + machineArticles.getCheoperations());
                machineArticles.setAvoidrules(true);
                Log.e("PCD", "setAvoidrules -> " + machineArticles.getAvoidrules());


                PostJSON postJSON = prepareMachineArticlePostJSON("INSERTQATARGET"
                        , machineArticles
                        , ""
                ); // POST parameters for URL call

                super.onPostExecute(webaddresses);

                //Log.e("PCU", "setArticleArrayList GetDataAroundTask.onPostExecute()");
                Log.e("PCD", "webaddress -> " + webaddresses.getWebaddress());
                Log.e("PCD", "postJSON -> " + postJSON.toString());

                // set the values that will go into @Body in the API to be POST on URL Call
                MutableLiveData<QAResponse> articleResponse = QARepository.getInstance()
                                                                          .insertQATask(webaddresses.getWebaddress(), postJSON);

                if (articleResponse.getValue()!=null)
                   Log.e("PCD", "articleResponse -> " + articleResponse.getValue()
                                                                                 .getArticles());
                else
                    Log.e("PCD", "articleResponse -> " + " no response from web server because: " + QARepository.getInstance().getErrMsg());

                finish();
                startActivity(new Intent(ProcesscontrollerdefectsActivity.this, ProcesscontrollerdefectsActivity.class)
                        .putExtra("processcontrollerfilter", processcontrollerFilter));

            }
        }

        GetDataAroundTask gt = new GetDataAroundTask();
        gt.execute();

    }



    // get the room database to see the web address where to looking for
    // then the web API address is get from room database the API is called
    private void setArticleArrayList(){

        class GetTasks extends AsyncTask<Void, Void, SysadminEntity> {

            @Override
            protected SysadminEntity doInBackground(Void... voids) {

                //Log.e("PCU", "setArticleArrayList GetTask.doInBackground()");

                // obtain webaddress set into sysadmin room database to call it for article array list

                return DatabaseClient
                        .getInstance(getApplicationContext())
                        .getAppDatabase()
                        .sysadminDao()
                        .getActivewebadress(); // goes into onPostExecute method as parameter
            }

            @Override
            protected void onPostExecute(SysadminEntity webaddresses) {


                processcontrollerFilter.setPmsoperatorname(articleMachineArrayList.get(articleMachineArrayList.size()-1).getPmsoperatorname());
                Log.e("PCD", "setChepms_operators -> " + processcontrollerFilter.getPmsoperatorname());

                processcontrollerFilter.setAreaname(articleMachineArrayList.get(articleMachineArrayList.size()-1).getAreaname());
                Log.e("PCD", "setChepms_areas -> " + processcontrollerFilter.getAreaname());

                PostJSON postJSON = preparePostJSON("TODAYTASKS"
                        , processcontrollerFilter
                        , ""
                ); // POST parameters for URL call

                super.onPostExecute(webaddresses);

                //Log.e("PCU", "setArticleArrayList GetTask.onPostExecute()");
                Log.e("PCU", "webaddress -> " + webaddresses.getWebaddress());
                Log.e("PCU", "postJSON -> " + new Gson().toJson(postJSON));

                // injects my ProcesscontrollerViewModel into newsViewModel property
                newsProcesscontrollerViewModel = ViewModelProviders
                        .of(ProcesscontrollerdefectsActivity.this)
                        .get(ProcesscontrollerViewModel.class);

                // init the ViewModel by calling URL obtained from webaddresses.getWebaddress() and retrieve the response obtained from API into MutableLiveData into PCViewModel
                // this will trigger the call of the url
                newsProcesscontrollerViewModel.init(webaddresses.getWebaddress(), postJSON); // ProcesscontrollerViewModel.init() will establish POST parameters that will be sent to API

                // MutableLiveData from PCViewModel will be observed and when changes occurs they will be set into newsResponse and the following code will be executed
                newsProcesscontrollerViewModel.getMutableLiveData()
                                              .observe(ProcesscontrollerdefectsActivity.this //  The LifecycleOwner which controls the observer
                                                        , newsResponse -> { // The observer that will receive the events. newsResponse is an object of class ProcesscontrollerResponse

                            // The events are dispatched on the main thread
                            // Any time the newResponse is changed this code will be executed into the main thread
                            if (newsResponse!=null) {

                                Log.e("PCU", "ResponseMessage ->" + newsProcesscontrollerViewModel.getProcesscontrollerRepository().getErrMsg());

                                // here newResponse has contained the data from response.body():
                                // {
                                //   "status": "ok",
                                //   "totalResults": 10,
                                //   "articles": [{article},{article} ...{article}]
                                // }
                                // packed into ProcesscontrollerResponse object.
                                // Now I have to extract the list of articles from the ProcesscontrollerResponse object and send them to an Adapter
                                switch (newsResponse.getTotalResults()) {

                                    case 0:
                                        // create new qa_target ?
                                        break;
                                    case 1:
                                        // load unresolved article from list
                                        qaCheckViewModel
                                                .getQacheck() // load data into modelview
                                                .setValue(newsResponse.getArticles().get(0)); // save data and informs observer
                                        break;
                                    default:
                                        // load first unresolved article from list
                                }

                                // choose which article from the list no matter is resolved or not
                                List<ProcesscontrollerArticles> newsArticles = newsResponse.getArticles();

                                articleProcesscontrollerArrayList.addAll(newsArticles);

                                newsAdapter.notifyDataSetChanged();

                            } else {

                                Log.e("PCU", "Null response -> " + newsProcesscontrollerViewModel.getProcesscontrollerRepository().getErrMsg());
                                Toast.makeText(  getApplicationContext()
                                        , newsProcesscontrollerViewModel.getProcesscontrollerRepository().getErrMsg()
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
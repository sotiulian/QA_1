package com.wwp.QA.Scoring;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.wwp.QA.Utils.PostJSON;
import com.wwp.QA.Utils.RetrofitService;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScoringRepository {
    
    private String errMsg = ""; // added by me to keep the response err message and show it into activity Toast

    private static ScoringRepository scoringRepository;

    public static ScoringRepository getInstance(){
        if (scoringRepository == null){
            scoringRepository = new ScoringRepository();
        }
        return scoringRepository;
    }

    private ScoringApi scoringApi;


    // constructor
    public ScoringRepository(){

        scoringApi = RetrofitService.createService(ScoringApi.class);
    }

    // metoda ce returneaza un obiect MutableLiveData ce contine response.body()
    public MutableLiveData<ScoringResponse> getScoringArticles(String webaddress, PostJSON postJSON){

        MutableLiveData<ScoringResponse> articlesData = new MutableLiveData<>();

        Log.e("MR", "postJSON -> " + new Gson().toJson(postJSON));

        // implements the method from API Interface.
        // Calls may be executed synchronously with execute, or asynchronously with enqueue
        // Must set the object: articlesData that will be returned by this method
        scoringApi.getScoringList(webaddress, postJSON)
                .enqueue(new Callback<ScoringResponse>() { // CallBack is an interface class that has to be implemented here with its two methods: onResponse and onFailure

                    @Override
                    public void onResponse(Call<ScoringResponse> call // An invocation of a Retrofit method that sends a request to a webserver and returns a response
                            , Response<ScoringResponse> response
                    ) {
                        if (response.isSuccessful()){
                            // response.body is like this:
                            // {
                            //   "status": "ok",
                            //   "totalResults": 10,
                            //   "articles": [{article},{article} ...{article}]
                            // }
                            setErrMsg("");

                            Log.e("SR", "response.body() -> " + new Gson().toJson(response.body()));
                            Log.e("SR", "response.body().getTotalResults() -> " + response.body().getTotalResults());
                            assert response.body() != null;
                            if ( response.body().getTotalResults()>0) {

                                articlesData.setValue(response.body()); // write entire response into MutableLiveData -> dispatchingValue -> considerNotify -> lamda.onCahnged
                            } else {

                                articlesData.setValue(null);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ScoringResponse> call
                            ,Throwable t
                    ) {
                        if (t instanceof IOException) {
                            // logging probably not necessary
                            setErrMsg("IOException: " + t.getMessage());
                            articlesData.setValue(null);
                        }
                        else {
                            setErrMsg(t.getMessage());
                            articlesData.setValue(null);
                        }
                    }
                });

        return articlesData;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}

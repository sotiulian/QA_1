package com.wwp.QA.ProcessController;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.wwp.QA.Utils.PostJSON;
import com.wwp.QA.Utils.RetrofitService;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProcesscontrollerRepository {

    private String errMsg = ""; // added by me to keep the response err message and show it into activity Toast

    private static ProcesscontrollerRepository processcontrollerRepository;

    public static ProcesscontrollerRepository getInstance(){
        if (processcontrollerRepository == null){
            processcontrollerRepository = new ProcesscontrollerRepository();
        }
        return processcontrollerRepository;
    }

    private ProcesscontrollerApi processcontrollerApi;


    // constructor
    public ProcesscontrollerRepository(){

        processcontrollerApi = RetrofitService.createService(ProcesscontrollerApi.class);
    }

    // metoda ce returneaza un obiect MutableLiveData ce contine response.body()
    public MutableLiveData<ProcesscontrollerResponse> getProcesscontrollerArticles(String webaddress, PostJSON postJSON){

        MutableLiveData<ProcesscontrollerResponse> articlesData = new MutableLiveData<>();

        Log.e("PCR", "postJSON -> " + new Gson().toJson(postJSON));

        // implements the method from API Interface.
        // Calls may be executed synchronously with execute, or asynchronously with enqueue
        // Must set the object: articlesData that will be returned by this method
        processcontrollerApi.getProcesscontrollerList(webaddress, postJSON)
                            .enqueue(new Callback<ProcesscontrollerResponse>() { // CallBack is an interface class that has to be implemented here with its two methods: onResponse and onFailure

            @Override
            public void onResponse(Call<ProcesscontrollerResponse> call // An invocation of a Retrofit method that sends a request to a webserver and returns a response
                                  ,Response<ProcesscontrollerResponse> response
            ) {
                if (response.isSuccessful()){
                    // response.body is like this:
                    // {
                    //   "status": "ok",
                    //   "totalResults": 10,
                    //   "articles": [{article},{article} ...{article}]
                    // }
                    setErrMsg("");

                    //Log.e("PCR", "response.body() -> " + new Gson().toJson(response.body()));
                    //Log.e("PCR", "response.body().getTotalResults() -> " + response.body().getTotalResults());
                    assert response.body() != null;
                    if ( response.body().getTotalResults()>0) {

                        articlesData.setValue(response.body());
                    } else {

                        articlesData.setValue(null);
                    }
                }
            }

            @Override
            public void onFailure(Call<ProcesscontrollerResponse> call
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

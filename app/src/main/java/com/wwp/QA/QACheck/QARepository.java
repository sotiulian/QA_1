package com.wwp.QA.QACheck;


import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.wwp.QA.Utils.PostJSON;
import com.wwp.QA.Utils.RetrofitService;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QARepository {

    private String errMsg = ""; // added by me to keep the response err message and show it into activity Toast

    private static com.wwp.QA.QACheck.QARepository qacheckRepository;

    public static com.wwp.QA.QACheck.QARepository getInstance(){
        if (qacheckRepository == null){
            qacheckRepository = new com.wwp.QA.QACheck.QARepository();
        }
        return qacheckRepository;
    }


    private QAApi qacheckApi;


    // constructor
    public QARepository(){

        qacheckApi = RetrofitService.createService(QAApi.class);
    }

    // metoda ce returneaza un obiect MutableLiveData ce contine response.body()
    public MutableLiveData<QAResponse> updateQAArticles(String webaddress, PostJSON postJSON){

        MutableLiveData<QAResponse> articlesData = new MutableLiveData<>();

        Log.e("QAR", "postJSON -> " + new Gson().toJson(postJSON));

        // implements the method from API Interface.
        // Calls may be executed synchronously with execute, or asynchronously with enqueue
        // Must set the object: articlesData that will be returned by this method
        qacheckApi.updateQAActual(webaddress, postJSON)
                .enqueue(new Callback<QAResponse>() { // CallBack is an interface class that has to be implemented here with its two methods: onResponse and onFailure

                    @Override
                    public void onResponse(Call<QAResponse> call // An invocation of a Retrofit method that sends a request to a webserver and returns a response
                            , Response<QAResponse> response
                    ) {
                        if (response.isSuccessful()){
                            // response.body is like this:
                            // {
                            //   "status": "ok",
                            //   "totalResults": 10,
                            //   "articles": [{article},{article} ...{article}]
                            // }
                            setErrMsg("");

                            Log.e("QAR", "response.body() -> " + new Gson().toJson(response.body()));
                            Log.e("QAR", "response.body().getTotalResults() -> " + response.body().getTotalResults());
                            assert response.body() != null;
                            if ( response.body().getTotalResults()>0) {

                                articlesData.setValue(response.body());
                            } else {

                                articlesData.setValue(null);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<QAResponse> call
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


    // metoda ce returneaza un obiect MutableLiveData ce contine response.body()
    public MutableLiveData<QAResponse> insertQATask(String webaddress, PostJSON postJSON){

        MutableLiveData<QAResponse> articlesData = new MutableLiveData<>();

        Log.e("QAR", "postJSON -> " + new Gson().toJson(postJSON));

        // implements the method from API Interface.
        // Calls may be executed synchronously with execute, or asynchronously with enqueue
        // Must set the object: articlesData that will be returned by this method
        qacheckApi.insertQATask(webaddress, postJSON)
                .enqueue(new Callback<QAResponse>() { // CallBack is an interface class that has to be implemented here with its two methods: onResponse and onFailure

                    @Override
                    public void onResponse(Call<QAResponse> call // An invocation of a Retrofit method that sends a request to a webserver and returns a response
                            , Response<QAResponse> response
                    ) {
                        if (response.isSuccessful()){
                            // response.body is like this:
                            // {
                            //   "status": "ok",
                            //   "totalResults": 10,
                            //   "articles": [{article},{article} ...{article}]
                            // }
                            setErrMsg("");

                            Log.e("QAR", "response.body() -> " + new Gson().toJson(response.body()));
                            Log.e("QAR", "response.body().getTotalResults() -> " + response.body().getTotalResults());
                            assert response.body() != null;
                            if ( response.body().getTotalResults()>0) {

                                articlesData.setValue(response.body());
                            } else {

                                articlesData.setValue(null);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<QAResponse> call
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

package com.wwp.QA.ProductionLine;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.wwp.QA.Utils.PostJSON;
import com.wwp.QA.Utils.RetrofitService;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductionlineRepository {


    private String errMsg = ""; // added by me to keep the response err message and show it into activity Toast

    private static com.wwp.QA.ProductionLine.ProductionlineRepository productionlineRepository;

    public static ProductionlineRepository getInstance(){
        if (productionlineRepository == null){
            productionlineRepository = new ProductionlineRepository();
        }
        return productionlineRepository;
    }

    private ProductionlineApi productionlineApi;


    // constructor
    public ProductionlineRepository(){

        productionlineApi = RetrofitService.createService(ProductionlineApi.class);
    }

    // metoda ce returneaza un obiect MutableLiveData ce contine response.body()
    public MutableLiveData<ProductionlineResponse> getProductionlineArticles(String webaddress, PostJSON postJSON){

        MutableLiveData<ProductionlineResponse> articlesData = new MutableLiveData<>();

        // {"action":"GETLINES","authkey":["authkey"],"dataset":{"areaname":"","linename":"","areacode":""}}
        Log.e("PLR", "postJSON -> " + new Gson().toJson(postJSON));

        // implements the method from API Interface.
        // Calls may be executed synchronously with execute, or asynchronously with enqueue
        // Must set the object: articlesData that will be returned by this method
        productionlineApi.getProductionlineList(webaddress, postJSON)
                .enqueue(new Callback<ProductionlineResponse>() { // CallBack is an interface class that has to be implemented here with its two methods: onResponse and onFailure

                    @Override
                    public void onResponse(Call<ProductionlineResponse> call // An invocation of a Retrofit method that sends a request to a webserver and returns a response
                            , Response<ProductionlineResponse> response
                    ) {
                        if (response.isSuccessful()){
                            // response.body is like this:
                            // {
                            //   "status": "ok",
                            //   "totalResults": 10,
                            //   "articles": [{article},{article} ...{article}]
                            // }
                            setErrMsg("");
                            Log.e("PLR", "response.body() -> " + new Gson().toJson(response.body()));
                            Log.e("PLR", "response.body().getTotalResults() -> " + response.body().getTotalResults());
                            if ( response.body().getTotalResults()>0) {
                                articlesData.setValue(response.body());
                            } else {
                                articlesData.setValue(null);
                            }
                        } else {
                            Log.e("PLR", "NO SUCCESS -> response.body() -> " + new Gson().toJson(response.body()));
                        }
                    }

                    @Override
                    public void onFailure(Call<ProductionlineResponse> call
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

package com.wwp.QA.ProductionOperator;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.wwp.QA.Utils.PostJSON;
import com.wwp.QA.Utils.RetrofitService;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductionoperatorRepository {


    private String errMsg = ""; // added by me to keep the response err message and show it into activity Toast

    private static com.wwp.QA.ProductionOperator.ProductionoperatorRepository productionoperatorRepository;

    public static com.wwp.QA.ProductionOperator.ProductionoperatorRepository getInstance(){
        if (productionoperatorRepository == null){
            productionoperatorRepository = new ProductionoperatorRepository();
        }
        return productionoperatorRepository;
    }

    private ProductionoperatorApi productionlineApi;


    // constructor
    public ProductionoperatorRepository(){

        productionlineApi = RetrofitService.createService(ProductionoperatorApi.class);
    }

    // metoda ce returneaza un obiect MutableLiveData ce contine response.body()
    public MutableLiveData<ProductionoperatorResponse> getProductionoperatorArticles(String webaddress, PostJSON postJSON){

        MutableLiveData<ProductionoperatorResponse> articlesData = new MutableLiveData<>();

        // {"action":"GETLINES","authkey":["authkey"],"dataset":{"areaname":"","linename":"","areacode":""}}
        Log.e("PLR", "postJSON -> " + new Gson().toJson(postJSON));

        // implements the method from API Interface.
        // Calls may be executed synchronously with execute, or asynchronously with enqueue
        // Must set the object: articlesData that will be returned by this method
        productionlineApi.getProductionoperatorList(webaddress, postJSON)
                .enqueue(new Callback<ProductionoperatorResponse>() { // CallBack is an interface class that has to be implemented here with its two methods: onResponse and onFailure

                    @Override
                    public void onResponse(Call<ProductionoperatorResponse> call // An invocation of a Retrofit method that sends a request to a webserver and returns a response
                            , Response<ProductionoperatorResponse> response
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
                    public void onFailure(Call<ProductionoperatorResponse> call
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

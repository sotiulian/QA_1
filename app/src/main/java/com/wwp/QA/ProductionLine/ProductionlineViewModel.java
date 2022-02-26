package com.wwp.QA.ProductionLine;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.wwp.QA.Utils.PostJSON;

public class ProductionlineViewModel extends ViewModel {

    private MutableLiveData<ProductionlineResponse> mutableLiveData;
    private ProductionlineRepository productionlineRepository;

    public void init(String webaddress, PostJSON postJSON){

        // POST parameter will be like this one:
        // { "action":"TODAYTASK"
        //  ,"dataset":{"operatorid":"000397"}
        //  ,"authkey":[{"authkey":"b8da5853775b6532fdfcbe1ee50832bb"}]
        // }

        if (mutableLiveData != null){
            return;
        }

        // here arrive just when mutableLiveData is null
        productionlineRepository = productionlineRepository.getInstance();

        // set the values that will go into @Body in the API to be POST on URL Call
        mutableLiveData = productionlineRepository.getProductionlineArticles(webaddress, postJSON);

    }

    public LiveData<ProductionlineResponse> getMutableLiveData() {

        return mutableLiveData;
    }

    public ProductionlineRepository getProductionlineRepository() {

        return productionlineRepository;
    }


}

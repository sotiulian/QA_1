package com.wwp.QA.ProductionOperator;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.wwp.QA.Utils.PostJSON;

public class ProductionoperatorViewModel extends ViewModel {

    private MutableLiveData<ProductionoperatorResponse> mutableLiveData;
    private ProductionoperatorRepository productionoperatorRepository;

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
        productionoperatorRepository = ProductionoperatorRepository.getInstance();

        // set the values that will go into @Body in the API to be POST on URL Call
        mutableLiveData = productionoperatorRepository.getProductionoperatorArticles(webaddress, postJSON);

    }

    public LiveData<ProductionoperatorResponse> getMutableLiveData() {

        return mutableLiveData;
    }

    public ProductionoperatorRepository getProductionoperatorRepository() {

        return productionoperatorRepository;
    }
    
    
}

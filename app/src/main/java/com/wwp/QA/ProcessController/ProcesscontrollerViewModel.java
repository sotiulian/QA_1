package com.wwp.QA.ProcessController;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wwp.QA.Utils.PostJSON;

/* https://medium.com/androiddevelopers/viewmodels-and-livedata-patterns-antipatterns-21efaef74a54
   Ideally, ViewModels shouldn’t know anything about Android. This improves testability, leak safety and modularity.
   A general rule of thumb is to make sure there are no android.* imports in your ViewModels (with exceptions like android.arch.*).
   The same applies to presenters.
 */
public class ProcesscontrollerViewModel extends ViewModel {

    private MutableLiveData<ProcesscontrollerResponse> mutableLiveData;
    private ProcesscontrollerRepository processcontrollerRepository;

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
        processcontrollerRepository = processcontrollerRepository.getInstance();

        // set the values that will go into @Body in the API to be POST on URL Call
        mutableLiveData = processcontrollerRepository.getProcesscontrollerArticles(webaddress, postJSON);

    }

    public LiveData<ProcesscontrollerResponse> getMutableLiveData() {

        return mutableLiveData;
    }

    public ProcesscontrollerRepository getProcesscontrollerRepository() {

        return processcontrollerRepository;
    }


}

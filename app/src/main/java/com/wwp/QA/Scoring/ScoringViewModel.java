package com.wwp.QA.Scoring;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wwp.QA.Utils.PostJSON;

public class ScoringViewModel  extends AndroidViewModel {

        private MutableLiveData<ScoringResponse> mutableLiveData;
        private ScoringRepository scoringRepository;

    public ScoringViewModel(@NonNull Application application) {
        super(application);
    }

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
            scoringRepository = ScoringRepository.getInstance();

            // set the values that will go into @Body in the API to be POST on URL Call
            mutableLiveData = scoringRepository.getScoringArticles(webaddress, postJSON);

        }

        public LiveData<ScoringResponse> getMutableLiveData() {

            return mutableLiveData;
        }

        public ScoringRepository getScoringRepository() {

            return scoringRepository;
        }



    }

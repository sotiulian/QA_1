package com.wwp.QA.QACheck;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wwp.QA.ProcessController.ProcesscontrollerArticles;

//
// Based on example of LiveData without Adapter from:
// https://medium.com/@ogieben/basics-of-viewmodel-and-livedata-4550a34c19f3
//
// Example of LiveData with Adapter (onBindViewHolder): https://medium.com/@taman.neupane/basic-example-of-livedata-and-viewmodel-14d5af922d0
// More detailed explanations: https://developer.android.com/topic/libraries/data-binding/architecture

public class QACheckViewModel extends AndroidViewModel {

    MutableLiveData<ProcesscontrollerArticles> qacheck = new MutableLiveData<>();

    public QACheckViewModel(@NonNull Application application) {

        super(application);
    }


    public MutableLiveData<ProcesscontrollerArticles> getQacheck() {

        return qacheck;
    }

}

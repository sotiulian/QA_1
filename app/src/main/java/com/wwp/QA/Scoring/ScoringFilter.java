package com.wwp.QA.Scoring;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ScoringFilter implements Serializable {

    @SerializedName("machineid")
    @Expose
    private String machineid;

    // constructor
    public ScoringFilter(String machineid) {
        this.setMachineid(machineid);
    }


    public String getMachineid() {
        return machineid;
    }

    public void setMachineid(String machineid) {
        this.machineid = machineid;
    }
}

package com.wwp.QA.Machine;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MachineFilter implements Serializable {

    @SerializedName("machineid")
    @Expose
    private String machineid;

    public MachineFilter(String machineid) {
        this.setMachineid(machineid);
    }

    public String getMachineid() {
        return machineid;
    }

    public void setMachineid(String machineid) {
        this.machineid = machineid;
    }
}

package com.wwp.QA.ProductionLine;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ProductionlineFilter implements Serializable {

    @SerializedName("linename")
    @Expose
    private String linename;

    @SerializedName("arename")
    @Expose private String areaname;

    @SerializedName("areacode")
    @Expose
    private String areacode;

    public ProductionlineFilter() {
        linename = "";
        areaname = "";
        areacode = "";
    }

    public String getLinename() {
        return linename;
    }

    public void setLinename(String linename) {
        this.linename = linename;
    }

    public String getAreaname() {
        return areaname;
    }

    public void setAreaname(String areaname) {
        this.areaname = areaname;
    }

    public String getAreacode() {
        return areacode;
    }

    public void setAreacode(String areacode) {
        this.areacode = areacode;
    }
}

package com.wwp.QA.ProductionLine;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ProductionlineArticles implements Serializable {

    @SerializedName("linename") // use @SerializedName annotation to specify the name of the field in the JSON when you do gson.toJson()
    @Expose
    // @Expose to serialize/deserialize is optional and it has two configuration parameters: serialize and deserialize
    private String linename;

    @SerializedName("areaname")
    @Expose
    @NonNull
    private String areaname;

    @SerializedName("areacode")
    @Expose
    @NonNull
    private String areacode;

    // @NonNull fields must be initialised in constructor
    public ProductionlineArticles() {
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

    @NonNull
    public String getAreaname() {
        return areaname;
    }

    public void setAreaname(@NonNull String areaname) {
        this.areaname = areaname;
    }

    @NonNull
    public String getAreacode() {
        return areacode;
    }

    public void setAreacode(@NonNull String areacode) {
        this.areacode = areacode;
    }
}

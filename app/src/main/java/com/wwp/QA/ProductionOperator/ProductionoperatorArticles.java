package com.wwp.QA.ProductionOperator;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ProductionoperatorArticles implements Serializable {

    @SerializedName("cheie")
    @Expose
    @NonNull
    private String cheie;

    @SerializedName("operatorid")
    @Expose
    @NonNull
    private String operatorid;

    @SerializedName("operatorname")
    @Expose
    @NonNull
    private String operatorname;


    public ProductionoperatorArticles() {

        setCheie("");
        setOperatorid("");
        setOperatorname("");
    }

    @NonNull
    public String getCheie() {
        return cheie;
    }

    public void setCheie(@NonNull String cheie) {
        this.cheie = cheie;
    }

    @NonNull
    public String getOperatorid() {
        return operatorid;
    }

    public void setOperatorid(@NonNull String operatorid) {
        this.operatorid = operatorid;
    }

    @NonNull
    public String getOperatorname() {
        return operatorname;
    }

    public void setOperatorname(@NonNull String operatorname) {
        this.operatorname = operatorname;
    }
}

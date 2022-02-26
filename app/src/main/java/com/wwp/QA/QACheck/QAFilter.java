package com.wwp.QA.QACheck;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class QAFilter implements Serializable {

    @SerializedName("cheqa_target")
    @Expose
    private String cheqa_target;

    @SerializedName("qaoperatorname")
    @Expose
    private String qaoperatorname;

    @SerializedName("chepms_areas")
    @Expose
    private String chepms_areas;

    @SerializedName("actualdefectsfoundpieces")
    @Expose
    private Integer actualdefectsfoundpieces;

    @SerializedName("bundleordernumericid")
    @Expose
    private Integer bundleordernumericid;

    public QAFilter() {
        setCheqa_target("");
        setQaoperatorname("");
        setChepms_areas("");
        setActualdefectsfoundpieces(0);
        setBundleordernumericid(0);
    }

    public String getCheqa_target() {
        return cheqa_target;
    }

    public void setCheqa_target(String cheqa_target) {
        this.cheqa_target = cheqa_target;
    }

    public String getQaoperatorname() {
        return qaoperatorname;
    }

    public void setQaoperatorname(String qaoperatorname) {
        this.qaoperatorname = qaoperatorname;
    }

    public String getChepms_areas() {
        return chepms_areas;
    }

    public void setChepms_areas(String chepms_areas) {
        this.chepms_areas = chepms_areas;
    }

    public Integer getActualdefectsfoundpieces() {
        return actualdefectsfoundpieces;
    }

    public void setActualdefectsfoundpieces(Integer actualdefectsfoundpieces) {
        this.actualdefectsfoundpieces = actualdefectsfoundpieces;
    }

    public Integer getBundleordernumericid() {
        return bundleordernumericid;
    }

    public void setBundleordernumericid(Integer bundleordernumericid) {
        this.bundleordernumericid = bundleordernumericid;
    }
}

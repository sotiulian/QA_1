package com.wwp.QA.ProductionLine;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.wwp.QA.ProcessController.ProcesscontrollerArticles;

import java.util.List;

public class ProductionlineResponse {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("totalresults")
    @Expose
    private Integer totalResults;

    @SerializedName("articles") // array of articles within response object
    @Expose
    private List<ProductionlineArticles> articles = null;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }


    public List<ProductionlineArticles> getArticles() {
        return articles;
    }

    public void setArticles(List<ProductionlineArticles> articles) {
        this.articles = articles;
    }

}

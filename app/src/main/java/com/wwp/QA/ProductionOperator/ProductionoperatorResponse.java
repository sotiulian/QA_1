package com.wwp.QA.ProductionOperator;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductionoperatorResponse {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("totalresults")
    @Expose
    private Integer totalResults;

    @SerializedName("articles") // array of articles within response object
    @Expose
    private List<ProductionoperatorArticles> articles = null;


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


    public List<ProductionoperatorArticles> getArticles() {
        return articles;
    }

    public void setArticles(List<ProductionoperatorArticles> articles) {
        this.articles = articles;
    }
}

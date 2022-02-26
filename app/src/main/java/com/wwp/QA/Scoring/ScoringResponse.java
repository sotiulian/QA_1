package com.wwp.QA.Scoring;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/* {
	"status": "OK",
	"totalresults": 2,
	"articles": [{
		"actualpiecesname": "ACTUALPIECESBYOPERATORANOPERATION",
		"actualpieces": null,
		"actualdefectsfoundpieces": null,
		"actualprocent": 0,
		"level": 3,
		"color": 8454016
	}, {
		"actualpiecesname": "ACTUALPIECESBYOPERATOR",
		"actualpieces": null,
		"actualdefectsfoundpieces": null,
		"actualprocent": 0,
		"level": 3,
		"color": 8454016
	}]
  }
* */

public class ScoringResponse {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("totalresults")
    @Expose
    private Integer totalResults;

    @SerializedName("articles") // array of articles within response object
    @Expose
    private List<ScoringArticles> articles = null;


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


    public List<ScoringArticles> getArticles() {
        return articles;
    }

    public void setArticles(List<ScoringArticles> articles) {
        this.articles = articles;
    }



}

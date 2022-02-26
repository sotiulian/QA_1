package com.wwp.QA.Machine;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

// response JSON object is the web response returned from retrofit: "https://newsapi.org/v2/" with API "top-headlines"
// response example:
// {
//   "status": "ok",
//   "totalResults": 10,
//   "articles": [{article},{article} ...{article}]
// }
public class MachineResponse {

        @SerializedName("status")
        @Expose
        private String status;

        @SerializedName("totalresults")
        @Expose
        private Integer totalResults;

        @SerializedName("articles") // array of articles within response object
        @Expose
        private List<MachineArticles> articles = null;


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


        public List<MachineArticles> getArticles() {
            return articles;
        }

        public void setArticles(List<MachineArticles> articles) {
            this.articles = articles;
        }
    }
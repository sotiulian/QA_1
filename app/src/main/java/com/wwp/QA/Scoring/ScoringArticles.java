package com.wwp.QA.Scoring;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/*
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
* */

public class ScoringArticles implements Serializable {

    @SerializedName("actualpiecesname")
    @Expose
    private String actualpiecesname;

    @SerializedName("actualpieces")
    @Expose
    private int actualpieces;

    @SerializedName("actualdefectsfoundpieces")
    @Expose
    private int actualdefectsfoundpieces;

    @SerializedName("actualprocent")
    @Expose
    private int actualprocent;

    @SerializedName("level")
    @Expose
    private int level;

    @SerializedName("color")
    @Expose
    private int color;

    public ScoringArticles() {
        this.setActualpiecesname("");
        this.setActualpieces(0);
        this.setActualdefectsfoundpieces(0);
        this.setActualprocent(0);
        this.setLevel(0);
        this.setColor(0);
    }


    public String getActualpiecesname() {
        return actualpiecesname;
    }

    public void setActualpiecesname(String actualpiecesname) {
        this.actualpiecesname = actualpiecesname;
    }

    public int getActualpieces() {
        return actualpieces;
    }

    public void setActualpieces(int actualpieces) {
        this.actualpieces = actualpieces;
    }

    public int getActualdefectsfoundpieces() {
        return actualdefectsfoundpieces;
    }

    public void setActualdefectsfoundpieces(int actualdefectsfoundpieces) {
        this.actualdefectsfoundpieces = actualdefectsfoundpieces;
    }

    public int getActualprocent() {
        return actualprocent;
    }

    public void setActualprocent(int actualprocent) {
        this.actualprocent = actualprocent;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}

package com.wwp.QA.ProcessController;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

// articles is a JSON array extracted from the response JSON object
// here is an article object extracted from articles array:
/*
{
    "cheqa_target": "GLBA1KK470",
	"chepms_areas": null,
	"areaname": null,
	"chepms_shifts": "_5XG0SCF3E",
	"shiftname": "ЦЕХ 2 ПОТОК 4 СМЕНА 1",
	"chepms_operators": "_5XA0LJ3P8",
	"pmsoperatorname": "ПЕШКОВА А.В.",
	"chestylesname": "_5Z60YL9YQ",
	"stylename": "70935 МУЖ.МО",
	"timestampcreate": "2021-12-19T11:37:41",
	"qadailychecks": 3,
	"qapiecespercheck": 10,
	"qaacceptablelevelofdefectspercheck": 15,
	"onstdminutes": null,
	"offstdminutes": null,
	"shifttimeminutes": null,
	"timeratio": 0.00,
	"targetchecks": 0,
	"targetpieces": 0,
	"qaacceptablelevelofdefectspersession": null,
	"whichx": 2,
	"cheqa_actual": null,
	"cheqa_operators": null,
	"timestampcurrentactual": null,
	"chepms_bundle_orders": null,
	"cheoperations": null,
	"actualpieces": null,
	"actualdefectsfoundpieces": null,
	"actualdefectsfoundprocent":null
	"whichxactual":null
	"balance":null
	"qaoperatorname": null,
	"bundleordernumericid": null,
	"operationcode": null,
	"operationname": null,
	"corderid": null,
	"colour": null,
	"size": null
}
*/
        
// implements Serializable will let this class to be transfomed in String to be attached on Intents and putExtra to be passed to other activities
public class ProcesscontrollerArticles implements Serializable {

    // TARGET (qa_target records cannot be null)
    @SerializedName("cheqa_target") // use @SerializedName annotation to specify the name of the field in the JSON when you do gson.toJson()
    @Expose                  // @Expose to serialize/deserialize is optional and it has two configuration parameters: serialize and deserialize
    @NonNull
    private String cheqa_target;

    @SerializedName("areaname")
    @Expose
    @NonNull
    private String areaname;

    @SerializedName("pmsoperatorname")
    @Expose
    @NonNull
    private String pmsoperatorname;

    @SerializedName("chestylesname")
    @Expose
    @NonNull
    private String chestylesname;

    @SerializedName("stylename")
    @Expose
    @NonNull
    private String stylename;

    @SerializedName("timestampcreate")
    @Expose
    @NonNull
    private String timestampcreate;

    @SerializedName("qapiecespercheck")
    @Expose
    @NonNull
    private Integer qapiecespercheck;

    @SerializedName("qaacceptablelevelofdefectspercheck")
    @Expose
    @NonNull
    private Integer qaacceptablelevelofdefectspercheck;

    @SerializedName("qadailychecks")
    @Expose
    @NonNull
    private Integer qadailychecks;

    @SerializedName("whichx")
    @Expose
    @NonNull
    private Integer whichx; // from qadailychecks



    // ACTUAL (can be null until qaoperator check)

    @SerializedName("cheqa_actual")
    @Expose
    private String cheqa_actual;

    @SerializedName("timestampcurrentactual")
    @Expose
    private String timestampcurrentactual;

    @SerializedName("actualpieces")
    @Expose
    private Integer actualpieces;

    @SerializedName("actualdefectsfoundpieces")
    @Expose
    private Integer actualdefectsfoundpieces;

    @SerializedName("actualdefectsfoundprocent")
    @Expose
    private Integer actualdefectsfoundprocent;

    @SerializedName("whichxactual")
    @Expose
    private Integer whichxactual;

    @SerializedName("qaoperatorname")
    @Expose
    private String qaoperatorname;

    @SerializedName("bundleordernumericid")
    @Expose
    private Integer bundleordernumericid; // MAX_VALUE = 2147483647

    @SerializedName("cheoperations")
    @Expose
    private String cheoperations;

    @SerializedName("operationcode")
    @Expose
    private String operationcode;

    @SerializedName("operationname")
    @Expose
    private String operationname;

    @SerializedName("corderid")
    @Expose
    private String corderid;

    @SerializedName("colour")
    @Expose
    private String colour;

    @SerializedName("size")
    @Expose
    private String size;

    @SerializedName("balance")
    @Expose
    private Integer balance; // 1 if actualdefectsfoundprocent<=qaacceptablelevelofdefectspercheck, 0 otherwise


    // @NonNull fields must be initialised in constructor
    public ProcesscontrollerArticles() {

        // target
        timestampcreate = "";
        pmsoperatorname = "";
        chestylesname = "";
        areaname = "";
        stylename = "";
        qapiecespercheck = 0;
        qaacceptablelevelofdefectspercheck = 0;
        qadailychecks = 0;
        whichx = 0;

        // actual
        actualdefectsfoundpieces =0;
        actualdefectsfoundprocent=0;
        whichxactual=0;
        balance= 0;
    }


    @NonNull
    public Integer getQapiecespercheck() {
        return qapiecespercheck;
    }

    public void setQapiecespercheck(@NonNull Integer qapiecespercheck) { this.qapiecespercheck = qapiecespercheck;}

    @NonNull
    public Integer getQaacceptablelevelofdefectspercheck() { return qaacceptablelevelofdefectspercheck;}

    public void setQaacceptablelevelofdefectspercheck(@NonNull Integer qaacceptablelevelofdefectspercheck) {
        this.qaacceptablelevelofdefectspercheck = qaacceptablelevelofdefectspercheck;
    }

    @NonNull
    public Integer getQadailychecks() {
        return qadailychecks;
    }


    public void setQadailychecks(@NonNull Integer qadailychecks) {this.qadailychecks = qadailychecks;}

    @NonNull
    public Integer getWhichx() { return whichx; }

    public void setWhichx(@NonNull Integer whichx) { this.whichx = whichx;}

    public Integer getActualpieces() {return actualpieces;}

    public void setActualpieces(Integer actualpieces) {this.actualpieces = actualpieces; }

    @NonNull
    public Integer getActualdefectsfoundpieces() {return actualdefectsfoundpieces==null ? 0 : actualdefectsfoundpieces;}

    public void setActualdefectsfoundpieces(@NonNull Integer actualdefectsfoundpieces) {this.actualdefectsfoundpieces = actualdefectsfoundpieces;}

    public Integer getActualdefectsfoundprocent() { return actualdefectsfoundprocent; }

    public void setActualdefectsfoundprocent(Integer actualdefectsfoundprocent) {this.actualdefectsfoundprocent = actualdefectsfoundprocent; }

    public Integer getWhichxactual() {return whichxactual; }

    public void setWhichxactual(Integer whichxactual) {this.whichxactual = whichxactual;}

    public String getTimestampcurrentactual() {
        return timestampcurrentactual;
    }

    public void setTimestampcurrentactual(String timestampcurrentactual) {this.timestampcurrentactual = timestampcurrentactual; }

    public String getQaoperatorname() {
        return qaoperatorname;
    }

    public void setQaoperatorname(String qaoperatorname) {
        this.qaoperatorname = qaoperatorname;
    }

    public Integer getBundleordernumericid() {
        return bundleordernumericid;
    }

    public void setBundleordernumericid(Integer bundleordernumericid) {this.bundleordernumericid = bundleordernumericid; }

    public String getCheoperations() {
        return cheoperations;
    }

    public void setCheoperations(String cheoperations) {
        this.cheoperations = cheoperations;
    }

    public String getOperationcode() {
        return operationcode;
    }

    public void setOperationcode(String operationcode) {
        this.operationcode = operationcode;
    }

    public String getOperationname() {
        return operationname;
    }

    public void setOperationname(String operationname) {
        this.operationname = operationname;
    }

    public String getCorderid() {
        return corderid;
    }

    public void setCorderid(String corderid) {
        this.corderid = corderid;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }


    public String getCheqa_actual() {
        return cheqa_actual;
    }

    public void setCheqa_actual(String cheqa_actual) {
        this.cheqa_actual = cheqa_actual;
    }

    public String getCheqa_target() {
        return cheqa_target;
    }

    public void setCheqa_target(String cheqa_target) {
        this.cheqa_target = cheqa_target;
    }

    public String getPmsoperatorname() {
       return pmsoperatorname;
    }

    public void setPmsoperatorname(String pmsoperatorname) { this.pmsoperatorname = pmsoperatorname; }

    @NonNull
    public String getAreaname() { return areaname;}

    public void setAreaname(@NonNull String areaname) { this.areaname = areaname; }

    @NonNull
    public String getChestylesname() {
        return chestylesname;
    }

    public void setChestylesname(@NonNull String chestylesname) { this.chestylesname = chestylesname; }

    public String getStylename() {
        return stylename;
    }

    public void setStylename(String stylename) {
        this.stylename = stylename;
    }

    public String getTimestampcreate() {
        return timestampcreate;
    }

    public void setTimestampcreate(String timestampcreate) { this.timestampcreate = timestampcreate; }

    public Integer getBalance() { return balance; }

    public void setBalance(Integer balance) {this.balance = balance; }
}

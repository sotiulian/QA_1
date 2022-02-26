package com.wwp.QA.Machine;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/*{
	"machineid": "000311",
	"chepms_areas": "_5XG0SBG9I",
	"areaname": "ЦЕХ 2 ПОТОК 4",
	"chepms_operators": "_5Z10P3BKH",
	"pmsoperatorname": "ВОЛКОВА В.В.",
	"bundleordernumericid": 942335,
	"chestylesname": "_5Z60YL9YQ",
	"stylename": "70935 МУЖ.МО",
	"cheoperations": "_64E0JAUVJ",
	"operationcode": "К-Ф-180-Ч-05",
	"operationname": "ЧИСТКА",
	"cheorders": "_65O0HRHWE",
	"corderid": "67666",
	"colour": "ХАКИ 01.012863",
	"size": "96\/170-176"
}*/

public class MachineArticles implements Serializable {

    @SerializedName("machineid")
    @Expose
    private String machineid;

    @SerializedName("chepms_areas")
    @Expose
    private String chepms_areas;

    @SerializedName("areaname")
    @Expose
    private String areaname;

    @SerializedName("chepms_operators")
    @Expose
    private String chepms_operators;

    @SerializedName("pmsoperatorname")
    @Expose
    private String pmsoperatorname;

    @SerializedName("bundleordernumericid")
    @Expose
    private Integer bundleordernumericid; // MAX_VALUE = 2147483647

    @SerializedName("chestylesname")
    @Expose
    private String chestylesname;

    @SerializedName("stylename")
    @Expose
    private String stylename;

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

    @SerializedName("avoidrules")
    @Expose
    private Boolean avoidrules;

    public MachineArticles(String machineid) {
        this.machineid = machineid;
        this.chepms_areas = "";
        this.areaname = "";
        this.chepms_operators = "";
        this.pmsoperatorname = "";
        this.bundleordernumericid = 0;
        this.chestylesname = "";
        this.stylename = "";
        this.cheoperations = "";
        this.operationcode = "";
        this.operationname = "";
        this.corderid = "";
        this.colour = "";
        this.size = "";
        this.avoidrules = false;
    }

    public String getMachineid() {
        return machineid;
    }

    public void setMachineid(String machineid) {
        this.machineid = machineid;
    }

    public String getChepms_areas() { return chepms_areas; }

    public void setChepms_areas(String chepms_areas) { this.chepms_areas = chepms_areas; }

    public String getAreaname() {
        return areaname;
    }

    public void setAreaname(String areaname) {
        this.areaname = areaname;
    }

    public String getChepms_operators() { return chepms_operators; }

    public void setChepms_operators(String chepms_operators) { this.chepms_operators = chepms_operators;}

    public String getPmsoperatorname() {
        return pmsoperatorname;
    }

    public void setPmsoperatorname(String pmsoperatorname) {
        this.pmsoperatorname = pmsoperatorname;
    }

    public Integer getBundleordernumericid() {
        return bundleordernumericid;
    }

    public void setBundleordernumericid(Integer bundleordernumericid) {
        this.bundleordernumericid = bundleordernumericid;
    }

    public String getChestylesname() {
        return chestylesname;
    }

    public void setChestylesname(String chestylesname) {
        this.chestylesname = chestylesname;
    }

    public String getStylename() {
        return stylename;
    }

    public void setStylename(String stylename) {
        this.stylename = stylename;
    }

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

    public Boolean getAvoidrules() { return avoidrules;}

    public void setAvoidrules(Boolean avoidrules) { this.avoidrules = avoidrules;}
}

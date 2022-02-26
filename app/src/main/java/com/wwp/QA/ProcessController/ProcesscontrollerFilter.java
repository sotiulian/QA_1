package com.wwp.QA.ProcessController;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ProcesscontrollerFilter implements Serializable {

    @SerializedName("cheqa_target")
    @Expose
    private String cheqa_target;

    @SerializedName("chepms_operators")
    @Expose
    private String chepms_operators;


    @SerializedName("pmsoperatorname")
    @Expose
    private String pmsoperatorname;

    @SerializedName("areaname")
    @Expose
    private String areaname;

    @SerializedName("chestylesname")
    @Expose
    private String chestylesname;

    @SerializedName("stylename")
    @Expose
    private String stylename;

    @SerializedName("cheqa_actual")
    @Expose
    private String cheqa_actual;


    @SerializedName("cheoperations")
    @Expose
    private String cheoperations;


    @SerializedName("operationcode")
    @Expose
    private String operationcode;

    @SerializedName("qaoperatorname") // the one whos logged in on QA Tablet
    @Expose
    private String qaoperatorname;

    @SerializedName("machineid")
    @Expose
    private String machineid;

    @SerializedName("resolved") // if the QA_Target has QA_Actual
    @Expose
    private String resolved;

    @SerializedName("unresolved") // if the QA_Target has no QA_Actual
    @Expose
    private String unresolved;

    public ProcesscontrollerFilter(String qaoperatorname){
        setCheqa_target("");
        setCheqa_actual("");
        setChepms_operators("");
        setPmsoperatorname("");
        setAreaname("");
        setChestylesname("");
        setStylename("");
        setCheoperations("");
        setOperationcode("");
        setQaoperatorname(qaoperatorname);
        setMachineid("");
        setResolved("");
        setUnresolved("");
    }

    public String getCheqa_target() {
        return cheqa_target;
    }

    public void setCheqa_target(String cheqa_target) {
        this.cheqa_target = cheqa_target;
    }

    public String getChepms_operators() {
        return chepms_operators;
    }

    public void setChepms_operators(String chepms_operators) {
        this.chepms_operators = chepms_operators;
    }

    public String getPmsoperatorname() {
        return pmsoperatorname;
    }

    public void setPmsoperatorname(String pmsoperatorname) {
        this.pmsoperatorname = pmsoperatorname;
    }

    public String getAreaname() {
        return areaname;
    }

    public String getMachineid() {
        return machineid;
    }

    public void setMachineid(String machineid) {
        this.machineid = machineid;
    }

    public void setAreaname(String areaname) {
        this.areaname = areaname;
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

    public String getCheqa_actual() {
        return cheqa_actual;
    }

    public void setCheqa_actual(String cheqa_actual) {
        this.cheqa_actual = cheqa_actual;
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

    public String getQaoperatorname() {
        return qaoperatorname;
    }

    public void setQaoperatorname(String qaoperatorname) {
        this.qaoperatorname = qaoperatorname;
    }

    public String getResolved() {
        return resolved;
    }

    public void setResolved(String resolved) {
        this.resolved = resolved;
    }

    public String getUnresolved() {
        return unresolved;
    }

    public void setUnresolved(String unresolved) {
        this.unresolved = unresolved;
    }
}

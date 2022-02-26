package com.wwp.QA.Utils;

import java.util.Map;

public class PostJSON{

    public String action;              // "LOGIN"
    public Map<String,String> dataset; // "dataset":{"operatorid":"MUSAKA", "salary":"1500"}
    public String[] authkey;           // "authkey":[{"authkey":"232546346356"}]

    public PostJSON(String action, Map<String,String> dataset, String[] authkey) {
        this.setAction(action);
        this.setDataset(dataset); // reference to exterior object that must exists
        this.setAuthkey(authkey); // reference to exterior object that must exists
    }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action;}

    public Map<String,String> getDataset() { return dataset; }
    public void setDataset(Map<String,String> dataset) { this.dataset = dataset; }

    public String[] getAuthkey() { return authkey; }
    public void setAuthkey(String[] authkey) { this.authkey = authkey; }
}
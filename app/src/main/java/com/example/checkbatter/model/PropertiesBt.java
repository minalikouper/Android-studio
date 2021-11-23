package com.example.checkbatter.model;

public class PropertiesBt {
    String labelPr;
    int valuePr;

    public PropertiesBt(String labelPr, int valuePr) {
        this.labelPr = labelPr;
        this.valuePr = valuePr;
    }

    public int getValuePr() {
        return valuePr;
    }

    public String getLabelPr() {
        return labelPr;
    }
}

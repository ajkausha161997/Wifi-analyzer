package com.example.ajay.wifianalyzer;

/**
 * Created by Ajay Kaushal
 */

public class Properties {

    private String property;
    private String value;

    public Properties(String p, String v) {
        property = p;
        value = v;
    }

    public String getProperty() {
        return property;
    }

    public String getValue() {
        return value;
    }
}

package com.example.vlustore.models;

public class Billitems {

    private String _cvUser;
    private String _cvDate;
    private String _cvCount;
    public Billitems(String cvUser, String cvDate, String cvCount){
        _cvUser = cvUser;
        _cvDate = cvDate;
        _cvCount = cvCount;
    }

    public String get_cvUser() {
        return _cvUser;
    }

    public String get_cvDate() {
        return _cvDate;
    }

    public String get_cvCount() {
        return _cvCount;
    }
}

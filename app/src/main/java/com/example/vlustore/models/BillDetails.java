package com.example.vlustore.models;

public class BillDetails {
    public String pname,name,phone,addr;
    public BillDetails(String pname, String name, String phone, String addr){
        this.pname = pname;
        this.name = name;
        this.phone = phone;
        this.addr = addr;
    }
    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

}



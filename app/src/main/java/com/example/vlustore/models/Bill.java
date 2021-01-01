package com.example.vlustore.models;

public class Bill {
    public String pname, description, price;
    public int quality;


    public Bill(){

    }
    public Bill(String pname, String description, String price,int quality) {
        this.pname = pname;
        this.description = description;
        this.price = price;
        this.quality = quality;
    }
    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }


    public String toString(){
        String temp ="\n"+ getPrice() +"$ " ;
        return temp;
    };

}


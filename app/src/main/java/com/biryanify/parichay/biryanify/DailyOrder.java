package com.biryanify.parichay.biryanify;

import java.util.Map;

public class DailyOrder {

    private String name;
    private String phone;
    private String email;
    private String item;
    private String method;
    private String suggestion;
    private String quantity;
    private Map<String , String> address;


    DailyOrder() {
        name = "";
        phone = "";
        email = "";
        item = "";
        suggestion = "";
        quantity = "";
    }


    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setItem(String item) {
        this.item += item;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getItem() {
        return item;
    }

    public String getSuggestion() {
        return suggestion;
    }

    public String getQuantity() {
        return quantity;
    }

    public Map<String, String> getAddress() {
        return address;
    }

    public void setAddress(Map<String, String> address) {
        this.address = address;
    }
}

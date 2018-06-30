package com.biryanify.parichay.biryanify;

public class DailyOrder {

    private String name;
    private String phone;
    private String email;
    private String address;
    private String method;
    private String suggestion;
    private int quantity;

    DailyOrder() {
        name = "";
        phone = "";
        email = "";
        address = "";
        method = "";
        suggestion = "";
        quantity = 0;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address += address;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }

    public void setQuantity(int quantity) {
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

    public String getAddress() {
        return address;
    }

    public String getMethod() {
        return method;
    }

    public String getSuggestion() {
        return suggestion;
    }

    public int getQuantity() {
        return quantity;
    }
}

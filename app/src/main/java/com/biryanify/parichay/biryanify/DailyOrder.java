package com.biryanify.parichay.biryanify;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class DailyOrder implements Parcelable {

    private String name;
    private String phone;
    private String email;
    private String item;
    private String method;
    private String suggestion;
    private String quantity;
    private String time;
    private Map<String , String> address;


    DailyOrder() {
        name = "";
        phone = "";
        email = "";
        item = "";
        suggestion = "_unknown_";
        quantity = "0";
        time= "_unknown_";
        address = new HashMap<String, String>();
        address.put("area", "");
        address.put("flat", "");
    }


    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setItem(String item) {
        this.item = item;
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
    public void setFlat(String val) {
        address.put("flat", val);
    }

    protected DailyOrder(Parcel in) {
        name = in.readString();
        phone = in.readString();
        email = in.readString();
        item = in.readString();
        method = in.readString();
        suggestion = in.readString();
        quantity = in.readString();
        time = in.readString();
        int size = in.readInt();
        address = new HashMap<String, String>();
        for(int i = 0; i < size; i++) {
            String key = in.readString();
            String value = in.readString();
            address.put(key,value);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(phone);
        dest.writeString(email);
        dest.writeString(item);
        dest.writeString(method);
        dest.writeString(suggestion);
        dest.writeString(quantity);
        dest.writeString(time);
        dest.writeInt(address.size());
        for(Map.Entry<String, String > entry : address.entrySet()) {
            dest.writeString(entry.getKey());
            dest.writeString(entry.getValue());
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<DailyOrder> CREATOR = new Parcelable.Creator<DailyOrder>() {
        @Override
        public DailyOrder createFromParcel(Parcel in) {
            return new DailyOrder(in);
        }

        @Override
        public DailyOrder[] newArray(int size) {
            return new DailyOrder[size];
        }
    };

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("phone", phone);
        result.put("email", email);
        result.put("item", item);
        result.put("quantity", quantity);
        result.put("address", address);
        result.put("suggestion", suggestion);
        result.put("time", time);
        return result;
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

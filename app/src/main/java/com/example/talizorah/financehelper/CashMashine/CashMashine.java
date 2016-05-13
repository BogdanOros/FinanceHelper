package com.example.talizorah.financehelper.CashMashine;

import java.util.List;

/**
 * Created by talizorah on 16.30.4.
 */
public class CashMashine {
    private String address;
    private List<String> schedule;

    public List<String> getSchedule() {
        return schedule;
    }

    public void setSchedule(List<String> schedule) {
        this.schedule = schedule;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

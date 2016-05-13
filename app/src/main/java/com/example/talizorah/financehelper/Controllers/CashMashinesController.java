package com.example.talizorah.financehelper.Controllers;

import android.widget.BaseAdapter;

import com.example.talizorah.financehelper.CashMashine.CashMashine;

/**
 * Created by talizorah on 16.30.4.
 */
public class CashMashinesController {
    private BaseAdapter adapter;
    private CashMashinesController(){
    }
    public static CashMashinesController createCashMashineController(){
        return new CashMashinesController();
    }
}

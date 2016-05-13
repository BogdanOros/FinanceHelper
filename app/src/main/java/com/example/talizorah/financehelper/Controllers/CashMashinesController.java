package com.example.talizorah.financehelper.Controllers;

import android.support.v7.widget.RecyclerView;
import android.widget.BaseAdapter;

import com.example.talizorah.financehelper.CashMashine.CashMashine;
import com.example.talizorah.financehelper.CustomList.CashMashineList;

/**
 * Created by talizorah on 16.30.4.
 */
public class CashMashinesController {
    private CashMashineList cashMashineAdapter;
    private RecyclerView recyclerView;
    private CashMashinesController(RecyclerView recyclerView){
        this.recyclerView = recyclerView;
    }
    public static CashMashinesController createCashMashineController(RecyclerView recyclerView){
        return new CashMashinesController(recyclerView);
    }
}

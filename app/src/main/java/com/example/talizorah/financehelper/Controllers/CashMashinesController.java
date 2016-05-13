package com.example.talizorah.financehelper.Controllers;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;

import com.example.talizorah.financehelper.CashMashine.CashMashine;
import com.example.talizorah.financehelper.CustomList.CashMashineList;
import com.example.talizorah.financehelper.DataLoading.ConnectionChecker;
import com.example.talizorah.financehelper.DataLoading.DataLoader;
import com.example.talizorah.financehelper.DataLoading.ICompleted;
import com.example.talizorah.financehelper.R;

import java.util.List;

/**
 * Created by talizorah on 16.30.4.
 */
public class CashMashinesController {
    private String address = "https://api.privatbank.ua/p24api/infrastructure?json&atm&address=&city=%D0%9A%D0%B8%D0%B5%D0%B2";
    private CashMashineList cashMashineAdapter;
    private Activity activity;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private AlertDialog.Builder alert;
    private CashMashinesController(RecyclerView recyclerView, ProgressBar progressBar,  Activity activity){
        this.recyclerView = recyclerView;
        this.activity = activity;
        this.progressBar = progressBar;
    }
    public static CashMashinesController createCashMashineController(RecyclerView recyclerView,
                                                                     ProgressBar progressBar,
                                                                     Activity activity){
        return new CashMashinesController(recyclerView, progressBar, activity);
    }
    private void loadData(){
        DataLoader loader = new DataLoader();
        loader.setTaskFinishOperation(new ICompleted() {
            @Override
            public void onAsynkTaskFinish(Object object) {
                List<CashMashine> cashMashineList = (List<CashMashine>)object;
                cashMashineAdapter = new CashMashineList(activity, cashMashineList);
                recyclerView.setAdapter(cashMashineAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(activity));
                progressBar.setVisibility(View.GONE);
            }
        });
        loader.execute(address);
    }
    public void setData(){
        if(ConnectionChecker.isNetworkAvailable(activity)){
            loadData();
        }
        else{
            noInternetConnectionWarning();
        }
    }
    private void noInternetConnectionWarning(){
        alert = new AlertDialog.Builder(activity);
        alert.setTitle(R.string.allert_tittle);
        alert.setMessage(R.string.allert_message);
        alert.setPositiveButton(R.string.allert_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                setData();
            }
        });
        alert.setNegativeButton(R.string.allert_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                activity.finish();
            }
        });
        alert.show();
    }
}

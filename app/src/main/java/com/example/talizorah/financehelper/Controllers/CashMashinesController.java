package com.example.talizorah.financehelper.Controllers;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.talizorah.financehelper.CashMashine.CashMashine;
import com.example.talizorah.financehelper.CustomList.CashMashineList;
import com.example.talizorah.financehelper.DataLoading.ConnectionChecker;
import com.example.talizorah.financehelper.DataLoading.DataLoader;
import com.example.talizorah.financehelper.DataLoading.ICompleted;
import com.example.talizorah.financehelper.R;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by talizorah on 16.30.4.
 */
public class CashMashinesController {
    private String address = "https://api.privatbank.ua/p24api/infrastructure?json&atm&address=&city=%D0%9A%D0%B8%D0%B5%D0%B2";
    private CashMashineList cashMashineAdapter;
    private String CASH_CONTROLLER = "cash_cont";
    private Activity activity;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private AlertDialog.Builder alert;
    private GeolocationController geolocationController;
    private GoogleApiController apiController;
    private CashMashinesController(RecyclerView recyclerView, ProgressBar progressBar,  Activity activity){
        this.recyclerView = recyclerView;
        this.activity = activity;
        this.progressBar = progressBar;
        this.geolocationController = new GeolocationController(activity);
        this.apiController = new GoogleApiController(activity);
    }
    public static CashMashinesController createCashMashineController(RecyclerView recyclerView,
                                                                     ProgressBar progressBar,
                                                                     Activity activity){
        return new CashMashinesController(recyclerView, progressBar, activity);
    }
    private void loadData(){
        DataLoader loader = new DataLoader();
        loader.setProgressBar(progressBar);
        loader.setTaskFinishOperation(new ICompleted() {
            @Override
            public void onAsynkTaskFinish(Object object) {
                List<CashMashine> cashMashineList = (List<CashMashine>)object;
                if(cashMashineList.size() == 0){
                    noItemsDialog();
                }
                cashMashineAdapter = new CashMashineList(activity, cashMashineList);
                recyclerView.setAdapter(cashMashineAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(activity));

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

    public void setAddress(){
        LinearLayout layout = new LinearLayout(activity);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText cityBox = new EditText(activity);
        cityBox.setHint(R.string.city_name);
        layout.addView(cityBox);

        final EditText addressBox = new EditText(activity);
        addressBox.setHint(R.string.address_name);
        layout.addView(addressBox);

        alert = new AlertDialog.Builder(activity);
        alert.setTitle(R.string.address_tittle);
        alert.setMessage(R.string.address_message);
        alert.setView(layout);

        alert.setPositiveButton(R.string.address_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                createAddress(cityBox.getText().toString(), addressBox.getText().toString());
                setData();
            }
        });
        alert.setNegativeButton(R.string.address_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alert.show();
    }

    private void createAddress(String city, String address){
        try {
            city = URLEncoder.encode(city, "utf-8");
            address = URLEncoder.encode(address, "utf-8");
        }
        catch (IOException e){
            Log.v(CASH_CONTROLLER, "The url address in not valid");
            e.printStackTrace();
        }
        this.address = "https://api.privatbank.ua/p24api/infrastructure?json&atm&address=" + address + "&city=" + city;
    }

    public void setAddressWithGeo(){
        try {
            String [] address = geolocationController.getAddress(apiController.getLatitude(), apiController.getLongitude());
            createAddress(address[0], address[1]);
            setData();
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public void resumeGeo(){
        apiController.resume();
    }
    public void pauseGeo(){
        apiController.pause();
    }

    private void noItemsDialog(){
        alert = new AlertDialog.Builder(activity);
        alert.setTitle(R.string.no_item_tittle);
        alert.setMessage(R.string.no_item_message);
        alert.setPositiveButton(R.string.no_item_manual, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                setAddress();
            }
        });
        alert.setNegativeButton(R.string.no_item_geo, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                setAddressWithGeo();
            }
        });
        alert.setNeutralButton(R.string.no_item_exit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                activity.finish();
            }
        });
        alert.show();
    }

}

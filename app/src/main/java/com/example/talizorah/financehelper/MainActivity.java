package com.example.talizorah.financehelper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.widget.ProgressBar;

import com.example.talizorah.financehelper.Controllers.CashMashinesController;

public class MainActivity extends AppCompatActivity {

    private CashMashinesController controller;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cash_mashine_list_activity);
        findViews();
        controller = CashMashinesController.createCashMashineController(recyclerView, progressBar, this);
        controller.setData();
    }

    private void findViews(){
        recyclerView = (RecyclerView)findViewById(R.id.cash_list);
        progressBar = (ProgressBar)findViewById(R.id.loading_bar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return true;
    }
}

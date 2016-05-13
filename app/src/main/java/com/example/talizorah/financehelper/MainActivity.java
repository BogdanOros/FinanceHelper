package com.example.talizorah.financehelper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.example.talizorah.financehelper.Controllers.CashMashinesController;

public class MainActivity extends AppCompatActivity {

    private CashMashinesController controller;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        controller = CashMashinesController.createCashMashineController(recyclerView);
    }

    private void findViews(){
        recyclerView = (RecyclerView)findViewById(R.id.cash_list);
    }

}

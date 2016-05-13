package com.example.talizorah.financehelper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.talizorah.financehelper.Controllers.CashMashinesController;

public class MainActivity extends AppCompatActivity {

    private CashMashinesController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        controller = CashMashinesController.createCashMashineController();
    }
}

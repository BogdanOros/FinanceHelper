package com.example.talizorah.financehelper.CustomList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import com.example.talizorah.financehelper.CashMashine.CashMashine;
import com.example.talizorah.financehelper.R;

import java.util.List;

/**
 * Created by talizorah on 16.30.4.
 */
public class CashMashineList extends ArrayAdapter<CashMashine>{
    private Activity context = null;
    private List<CashMashine> dataSource = null;
    public CashMashineList(Activity context, List<CashMashine> dataSource){
        super(context, R.layout.cash_mashine_list_item, dataSource);
        this.context = context;
        this.dataSource = dataSource;
    }

    static class ViewHolder{

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View rowView = layoutInflater.inflate(R.layout.cash_mashine_list_item, null, true);
        return rowView;
    }
}

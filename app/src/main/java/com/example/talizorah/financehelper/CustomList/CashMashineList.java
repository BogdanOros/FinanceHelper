package com.example.talizorah.financehelper.CustomList;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.talizorah.financehelper.CashMashine.CashMashine;
import com.example.talizorah.financehelper.R;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by talizorah on 16.30.4.
 */
public class CashMashineList extends RecyclerView.Adapter<CashMashineList.CashViewHolder>{

    private LayoutInflater inflater;
    private List<CashMashine> cashMashines = Collections.emptyList();
    private Calendar calendar;

    public CashMashineList(Context context, List<CashMashine> cashMashines){
        inflater = LayoutInflater.from(context);
        this.cashMashines = cashMashines;
        calendar = Calendar.getInstance();
    }

    @Override
    public CashViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.cash_mashine_list_item, parent, false);
        CashViewHolder holder = new CashViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(CashViewHolder holder, int position) {
        CashMashine currentCash = cashMashines.get(position);
        holder.getAddressTextView().setText(currentCash.getAddress());
        calendar.setTime(new Date());
        holder.getTimeTextView().setText(currentCash.getSchedule().get(calendar.get(Calendar.DAY_OF_WEEK)-1));
    }

    @Override
    public int getItemCount() {
        return cashMashines.size();
    }

    class CashViewHolder extends RecyclerView.ViewHolder{

        private TextView addressTextView;
        private TextView timeTextView;

        public CashViewHolder(View itemView) {
            super(itemView);
            addressTextView = (TextView)itemView.findViewById(R.id.address);
            timeTextView = (TextView)itemView.findViewById(R.id.time);
        }

        public TextView getAddressTextView() {
            return addressTextView;
        }

        public void setAddressTextView(TextView addressTextView) {
            this.addressTextView = addressTextView;
        }

        public TextView getTimeTextView() {
            return timeTextView;
        }

        public void setTimeTextView(TextView timeTextView) {
            this.timeTextView = timeTextView;
        }

    }

}

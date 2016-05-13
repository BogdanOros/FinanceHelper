package com.example.talizorah.financehelper.CustomList;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.example.talizorah.financehelper.CashMashine.CashMashine;
import com.example.talizorah.financehelper.Controllers.GeolocationController;
import com.example.talizorah.financehelper.Controllers.GoogleApiController;
import com.example.talizorah.financehelper.R;
import com.google.android.gms.maps.model.LatLng;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by talizorah on 16.30.4.
 */
public class CashMashineList extends RecyclerView.Adapter<CashMashineList.CashViewHolder>{

    private GeolocationController controller;
    private LayoutInflater inflater;
    private List<CashMashine> cashMashines = Collections.emptyList();
    private Calendar calendar;
    private Context context;
    private int lastPos = -1;

    public CashMashineList(Context context, List<CashMashine> cashMashines){
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.cashMashines = cashMashines;
        calendar = Calendar.getInstance();
    }

    public void animate(CashViewHolder viewHolder, int position) {
        final Animation animation = AnimationUtils.loadAnimation(context,
                (position > lastPos) ? R.anim.up_for_bottom
                        : R.anim.down_from_top);
        viewHolder.itemView.setAnimation(animation);
        lastPos = position;
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
        String resultTime = "Час роботи: " + currentCash.getSchedule().get(calendar.get(Calendar.DAY_OF_WEEK) - 1);
        holder.getTimeTextView().setText(resultTime);
        animate(holder, position);
    }

    @Override
    public int getItemCount() {
        return cashMashines.size();
    }

    class CashViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView addressTextView;
        private TextView timeTextView;

        public CashViewHolder(View itemView) {
            super(itemView);
            addressTextView = (TextView)itemView.findViewById(R.id.address);
            timeTextView = (TextView)itemView.findViewById(R.id.time);
            itemView.setOnClickListener(this);
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

        @Override
        public void onClick(View v) {
            String[] result = addressTextView.getText().toString().split(",");
            Activity tempAct = (Activity)v.getContext();
            LatLng dest = GeolocationController.getLocationFromAddress(tempAct,
                   result[1] + " " + result[2] + " " + result[3]);
            LatLng source = GoogleApiController.getInstance(tempAct).createLatLng();
            if(dest == null) {
                Toast.makeText(tempAct, "Геолокатор не може знайти дану адрессу", Toast.LENGTH_SHORT).show();
            }
            if(dest != null)
                GeolocationController.createMap(source, dest, tempAct);
        }
    }

}

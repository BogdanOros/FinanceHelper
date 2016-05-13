package com.example.talizorah.financehelper.DataLoading;

import com.example.talizorah.financehelper.CashMashine.CashMashine;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by talizorah on 16.13.5.
 */
public class JsonParser {
    public static List<CashMashine> parseCashMachineJson(String json) throws JSONException {
        String address = "fullAddressRu";
        String schedule = "tw";
        List<String> daysList = Arrays.asList( "sun", "mon", "tue", "wed", "thu", "fri", "sat");
        JSONObject parentObject = new JSONObject(json);
        JSONArray cashMachineArray = parentObject.getJSONArray("devices");
        List<CashMashine> machineList = new ArrayList<>();
        for(int i=0; i<cashMachineArray.length(); i++){
            JSONObject currentObject = cashMachineArray.getJSONObject(i);
            CashMashine item = new CashMashine();
            item.setAddress(currentObject.getString(address));
            List<String> currentSchedule = new ArrayList<>();
            JSONObject scheduleJson = currentObject.getJSONObject(schedule);
            for(int j=0; j<daysList.size(); j++)
                currentSchedule.add(scheduleJson.getString(daysList.get(j)));
            item.setSchedule(currentSchedule);
            machineList.add(item);
        }
        return machineList;
    }
}

package com.example.talizorah.financehelper.DataLoading;

import android.os.AsyncTask;
import android.util.Log;

import com.example.talizorah.financehelper.CashMashine.CashMashine;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

/**
 * Created by talizorah on 16.30.4.
 */
public class DataLoader extends AsyncTask<String, Void, List<CashMashine>> {
    private String DATA_LOADER = "DATA_LOADER";
    private ICompleted taskCompleted;
    private List<CashMashine> cashMashines = Collections.emptyList();

    public void setTaskFinishOperation(ICompleted completed){
        taskCompleted = completed;
    }

    @Override
    protected List<CashMashine> doInBackground(String... params) {
        String resultJson =  downloadJson(params[0]);
        if(resultJson != null){
            try {
                return JsonParser.parseCashMachineJson(resultJson);
            }
            catch (JSONException ex){
                Log.v(DATA_LOADER, "The result json not downloaded");
                ex.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<CashMashine> result) {
        taskCompleted.onAsynkTaskFinish(result);
    }

    private String downloadJson(String urlAddress){
        HttpURLConnection httpURLConnection = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(urlAddress);
            httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            InputStream inputStream = httpURLConnection.getInputStream();
            if(inputStream == null){
                return null;
            }
            StringBuffer stringBuffer = new StringBuffer();
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null){
                stringBuffer.append(line);
            }
            if(stringBuffer.length() == 0){
                return null;
            }
            return stringBuffer.toString();
        }
        catch (MalformedURLException ex){
            Log.v(DATA_LOADER, "The url address in not valid");
            ex.printStackTrace();
        }
        catch (IOException ex){
            Log.v(DATA_LOADER, "Internet connection problem");
            ex.printStackTrace();
        }
        finally {
            if(httpURLConnection != null)
                httpURLConnection.disconnect();
            if(reader != null){
                try {
                    reader.close();
                }
                catch (IOException ex){
                    Log.v(DATA_LOADER, "Cant close the reader stream");
                    ex.printStackTrace();
                }
            }
        }
        return null;
    }

}

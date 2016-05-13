package com.example.talizorah.financehelper.DataLoading;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by talizorah on 16.30.4.
 */
public class DataLoader extends AsyncTask<String, Void, String> {
    private String DATA_LOADER = "DATA_LOADER";
    private ICompleted taskCompleted;

    public void setTaskFinishOperation(ICompleted completed){
        taskCompleted = completed;
    }

    @Override
    protected String doInBackground(String... params) {
        return downloadJson(params[0]);
    }

    @Override
    protected void onPostExecute(String s) {
        taskCompleted.onAsynkTaskFinish(s);
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

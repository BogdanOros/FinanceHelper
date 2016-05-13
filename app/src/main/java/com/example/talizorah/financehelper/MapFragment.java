package com.example.talizorah.financehelper;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by talizorah on 16.13.5.
 */
public class MapFragment extends FragmentActivity {
    private GoogleMap myMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        Intent intent = getIntent();
        myPos = (LatLng)intent.getExtras().get("source");
        endPos =(LatLng)intent.getExtras().get("dest");
        myMap = ((SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map)).getMap();
        myMap.setMyLocationEnabled(true);
        myMap.moveCamera(CameraUpdateFactory.newLatLng(myPos));
        myMap.animateCamera(CameraUpdateFactory.zoomTo(12));
        new createMapTask().execute(myPos, endPos);

    }
    private Polyline line;
    private LatLng myPos, endPos;

    public class createMapTask extends AsyncTask<LatLng, Void, String>{
        private String url;
        private String DATA_LOADER = "createMapTask";

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

        public String makeURL(double sourcelat, double sourcelog, double destlat,
                              double destlog) {
            StringBuilder urlString = new StringBuilder();
            urlString.append("http://maps.googleapis.com/maps/api/directions/json");
            urlString.append("?origin=");// from
            urlString.append(Double.toString(sourcelat));
            urlString.append(",");
            urlString.append(Double.toString(sourcelog));
            urlString.append("&destination=");// to
            urlString.append(Double.toString(destlat));
            urlString.append(",");
            urlString.append(Double.toString(destlog));
            urlString.append("&sensor=false&mode=driving&alternatives=true");
            return urlString.toString();
        }

        @Override
        protected String doInBackground(LatLng ... params) {
            String url = makeURL(params[0].latitude, params[0].longitude, params[1].latitude, params[1].longitude);
            String json = downloadJson(url);
            return json;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(result != null){
                drawPath(result);
            }
        }

        public void drawPath(String result) {
            if (line != null) {
                myMap.clear();
            }
            myMap.addMarker(new MarkerOptions().position(myPos).icon(
                    BitmapDescriptorFactory.fromResource(R.drawable.ic_manual_search)));
            myMap.addMarker(new MarkerOptions().position(endPos).icon(
                    BitmapDescriptorFactory.fromResource(R.drawable.ic_manual_search)));
            try {
                final JSONObject json = new JSONObject(result);
                JSONArray routeArray = json.getJSONArray("routes");
                JSONObject routes = routeArray.getJSONObject(0);
                JSONObject overviewPolylines = routes
                        .getJSONObject("overview_polyline");
                String encodedString = overviewPolylines.getString("points");
                List<LatLng> list = decodePoly(encodedString);

                PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
                for (int z = 0; z < list.size(); z++) {
                    LatLng point = list.get(z);
                    options.add(point);
                }
                line = myMap.addPolyline(options);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        private List<LatLng> decodePoly(String encoded) {

            List<LatLng> poly = new ArrayList<LatLng>();
            int index = 0, len = encoded.length();
            int lat = 0, lng = 0;

            while (index < len) {
                int b, shift = 0, result = 0;
                do {
                    b = encoded.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                lat += dlat;

                shift = 0;
                result = 0;
                do {
                    b = encoded.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                lng += dlng;

                LatLng p = new LatLng((((double) lat / 1E5)),
                        (((double) lng / 1E5)));
                poly.add(p);
            }
            return poly;
        }
    }
}

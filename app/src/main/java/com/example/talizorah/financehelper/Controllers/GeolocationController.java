package com.example.talizorah.financehelper.Controllers;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;

import com.example.talizorah.financehelper.MainActivity;
import com.example.talizorah.financehelper.MapFragment;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.model.LatLng;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by talizorah on 16.13.5.
 */
public class GeolocationController {
    private Geocoder geocoder;
    private List<Address> addresses;
    private Activity activity;
    public GeolocationController(Activity activity){
        this.activity = activity;
        Locale locale = new Locale("ru", "RU");
        geocoder = new Geocoder(activity, locale);
    }

    public String[] getAddress(double latitude, double  longitude) throws IOException{
        addresses = geocoder.getFromLocation(latitude, longitude, 1);
        String address = addresses.get(0).getAddressLine(0);
        String city = addresses.get(0).getLocality();
        return new String[]{city, address};
    }

    public static LatLng getLocationFromAddress(Activity activity, String strAddress) {
        Geocoder geocoder = new Geocoder(activity);
        List<Address> address = null;
        LatLng p1 = null;

        try {
            int counter = 1;
            while(address == null || counter != 5){
                counter++;
                address = geocoder.getFromLocationName(strAddress, 5);
            }
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return p1;
    }

    public static void createMap(LatLng source, LatLng dest, Activity activity){
        Intent i = new Intent(activity, MapFragment.class);
        i.putExtra("source", source);
        i.putExtra("dest", dest);
        activity.startActivity(i);
    }

}

package com.example.talizorah.financehelper.Controllers;

import android.annotation.TargetApi;
import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by talizorah on 16.13.5.
 */
public class GeolocationController {
    private Geocoder geocoder;
    private List<Address> addresses;

    public GeolocationController(Activity activity){
        Locale locale = new Locale("ru", "RU");
        geocoder = new Geocoder(activity, locale);
    }

    public String[] getAddress(double latitude, double  longitude) throws IOException{
        addresses = geocoder.getFromLocation(latitude, longitude, 1);
        String address = addresses.get(0).getAddressLine(0);
        String city = addresses.get(0).getLocality();
        return new String[]{city, address};
    }

}

package sumon.com.escort;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LocationTracker implements LocationListener {

    private boolean isGPSEnabled;
    private boolean isNetworkEnabled;
    private boolean isProviderAvailable;
    private Context context;
    private Location location;
    private LocationManager locationManager;
    private JSONArray currentLocationArray;
    private JSONObject currentLocationJsonObj;
    private SimpleDateFormat simpleDateFormat;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 10 meters
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 hour

    LocationTracker(Context context) {
        this.context = context;
        location = null;
        isGPSEnabled = false;
        isNetworkEnabled = false;
        isProviderAvailable = false;
        currentLocationArray = new JSONArray();
        currentLocationJsonObj = new JSONObject();
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss_a", Locale.getDefault());
    }

    public boolean isLocationProviderEnabled(){


        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        Log.i("isGPSEnabled", "=" + isGPSEnabled);
        Log.i("isNetworkEnabled", "=" + isNetworkEnabled);

        if (isGPSEnabled == false && isNetworkEnabled == false) {
           isProviderAvailable = false;
        }else {
            isProviderAvailable = true;
        }

        return isProviderAvailable;
    }

    public Location getGeoPosition() {
        try {
            if(isLocationProviderEnabled()){
                if (isGPSEnabled) {
                    if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null){
                                Log.i("Location from GPS","Lat "+location.getLatitude()+" Lon "+location.getLongitude());
                            }else {
                                Log.i("Location from GPS", "null");
                            }
                        }
                    }else {
                        Log.i("Location Permission", "Access Fine Location Permission not enabled");
                    }
                }

                if (isNetworkEnabled) {
                    if (location == null){
                        if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                            //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                            if (locationManager != null) {
                                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                                if (location != null) {
                                    Log.i("Location from Network","Lat "+location.getLatitude()+" Lon "+location.getLongitude());
                                }else {
                                    Log.i("Location from Network", "null");
                                }
                            }
                        }else {
                            Log.i("Location Permission", "Access Coarse Location Permission not enabled");
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }

    public JSONObject getCurrentLocation(){

        try {

            if (getGeoPosition() != null){
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);

                if (addressList != null && addressList.size() > 0) {
                    Address address = addressList.get(0);

                    String currentAddress = "";
                    for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                        currentAddress = currentAddress + address.getAddressLine(i)+ " " + "\n";
                    }

                    String city = address.getLocality();
                    String state = address.getAdminArea();
                    String country = address.getCountryName();
                    String postalCode = address.getPostalCode();
                    String featureName = address.getFeatureName();

                    long currentTimeMillis = System.currentTimeMillis();
                    Date date = new Date(currentTimeMillis);
                    String time = simpleDateFormat.format(date);

                    JSONObject currentLocationData = new JSONObject();
                    currentLocationData.put("time", time);
                    currentLocationData.put("address", currentAddress);
                    currentLocationData.put("city", city);
                    currentLocationData.put("state", state);
                    currentLocationData.put("postalCode", postalCode);
                    currentLocationData.put("country", country);
                    currentLocationData.put("feature", featureName);

                    currentLocationArray.put(currentLocationData);
                }

                currentLocationJsonObj.put("LOCATION", currentLocationArray);
            }else{
                Log.i("Location", "Current Location is null");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }

        return currentLocationJsonObj;
    }

    public void stopLocationListener() {
        if (locationManager != null) {
            locationManager.removeUpdates(LocationTracker.this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

}
package com.urbanpoint.UrbanPoint.utils;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

/**
 * Created by Anurag Sethi
 * The class contains the methods based on location services
 */
public class LocationUtility implements LocationListener {

    Context contextObj;
    LocationManager locMgrObj;
    private LatLng deviceCurrentLocation;

    public LocationUtility(Context contextObj) {
        this.contextObj = contextObj;
    }

    /**
     * Check Location Service Status
     *
     * @return true if location services enabled on the device
     */
    public boolean checkLocationServiceStatus() {
        locMgrObj = (LocationManager) contextObj.getSystemService(Context.LOCATION_SERVICE);
        if (!locMgrObj.isProviderEnabled(LocationManager.GPS_PROVIDER) && !locMgrObj.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * The method will return latitude and longitude of the given address
     *
     * @param address for which latitude and longitude needs to be set
     * @return LatLng object having latitude and longitude of the given address
     * @throws IOException
     */

    public LatLng getLatitudeLongitude(String address) {
        List<Address> addressList;
        Geocoder geocoderObj = new Geocoder(contextObj);

        try {
            addressList = geocoderObj.getFromLocationName(address, 1);

            if (addressList.size() > 0) {
                Address location = addressList.get(0);

                double latitude = (double) (location.getLatitude());
                double longitude = (double) (location.getLongitude());

                LatLng latlngObj = new LatLng(latitude, longitude);

                return latlngObj;
            } else {
                return null;
            }


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }


    /**
     * The method will get the address in case latitude and longitude are provided
     *
     * @param latLngObj last know location co-ordinates
     * @return the address
     */
    public String getStreetAddress(LatLng latLngObj) {
        List<Address> addressList;
        Geocoder geocoderObj = new Geocoder(contextObj);

        double locationLatitude = latLngObj.latitude;
        double locationLongitude = latLngObj.longitude;

        try {
            addressList = geocoderObj.getFromLocation(locationLatitude, locationLongitude, 1);

            if (addressList != null) {
                Address fetchedAddress = addressList.get(0);
                StringBuilder strAddress = new StringBuilder();
                for (int i = 0; i < fetchedAddress.getMaxAddressLineIndex(); i++) {
                    strAddress.append(fetchedAddress.getAddressLine(i)).append("\n");
                }

                return strAddress.toString();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }


    /**
     * The method will find the best available current latitude and longitude
     *
     * @param locObj last know location co-ordinates
     * @return LatLng object having the latest current location co-ordinates
     */

    public LatLng getCurrentLocation(Location locObj) {

        String bestProvider = LocationManager.NETWORK_PROVIDER;

        locMgrObj = (LocationManager) contextObj.getSystemService(contextObj.LOCATION_SERVICE);

        if (locObj == null) {

            if (locMgrObj.isProviderEnabled(bestProvider)) {
                locObj = locMgrObj.getLastKnownLocation(bestProvider);

                if (locObj != null) {
                    LatLng latlongObj = new LatLng(locObj.getLatitude(), locObj.getLongitude());
                    return latlongObj;
                } else {
                    locMgrObj.requestLocationUpdates(bestProvider, 0, 0, this);
                }

            }//if
            else {
                contextObj.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        } else {
            LatLng latlongObj = new LatLng(locObj.getLatitude(), locObj.getLongitude());
            return latlongObj;
        }
        return null;
    }


    /**
     * Called when the location has changed.
     * <p/>
     * <p> There are no restrictions on the use of the supplied Location object.
     *
     * @param location The new location, as a Location object.
     */
    @Override
    public void onLocationChanged(Location location) {

        deviceCurrentLocation = getCurrentLocation(location);
        //un-register location update
        locMgrObj.removeUpdates(this);
    }

    /**
     * Called when the provider status changes. This method is called when
     * a provider is unable to fetch a location or if the provider has recently
     * become available after a period of unavailability.
     *
     * @param provider the name of the location provider associated with this
     *                 update.
     * @param status   {@link android.location.LocationProvider#OUT_OF_SERVICE} if the
     *                 provider is out of service, and this is not expected to change in the
     *                 near future; {@link android.location.LocationProvider#TEMPORARILY_UNAVAILABLE} if
     *                 the provider is temporarily unavailable but is expected to be available
     *                 shortly; and {@link android.location.LocationProvider#AVAILABLE} if the
     *                 provider is currently available.
     * @param extras   an optional Bundle which will contain provider specific
     *                 status variables.
     *                 <p/>
     *                 <p> A number of common key/value pairs for the extras Bundle are listed
     *                 below. Providers that use any of the keys on this list must
     *                 provide the corresponding value as described below.
     *                 <p/>
     *                 <ul>
     *                 <li> satellites - the number of satellites used to derive the fix
     */
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    /**
     * Called when the provider is enabled by the user.
     *
     * @param provider the name of the location provider associated with this
     *                 update.
     */
    @Override
    public void onProviderEnabled(String provider) {

    }

    /**
     * Called when the provider is disabled by the user. If requestLocationUpdates
     * is called on an already disabled provider, this method is called
     * immediately.
     *
     * @param provider the name of the location provider associated with this
     *                 update.
     */
    @Override
    public void onProviderDisabled(String provider) {

    }

    public LatLng getDeviceCurrentLocation() {
        return deviceCurrentLocation;
    }

    public void setDeviceCurrentLocation(LatLng deviceCurrentLocation) {
        this.deviceCurrentLocation = deviceCurrentLocation;
    }
}

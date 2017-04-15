package pharmaproject.ahmed.example.packagecom.pharmaproject_employee;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import pharmaproject.ahmed.example.packagecom.pharmaproject_employee.database.Information;
import pharmaproject.ahmed.example.packagecom.pharmaproject_employee.helper.Utils;

public class SendLatLong extends Service {


    String Email, Parent;
    int time;
    LocationListener locationListener;
    LocationManager locationManager;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Email = intent.getStringExtra("EmailAdress");
        Parent = intent.getStringExtra("parentName");
        time = intent.getIntExtra("time", 5);
        Log.i("****","hi");

        latlongSend();

        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // use it to set value of offline if he destroy the service
        Information.getDatabase().child("Supervisor").child(Utils.parentName).child(Utils.EmailAdress.replace(".", "*")).child("online")
                .setValue(false);
        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
        }
    }


    public void latlongSend() {

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(final Location location) {
                Log.i("****","lat/lng: (" + location.getLatitude() + "," + location.getLongitude() + ")");
                Information.getDatabase().child("Supervisor").child(Parent).child(/*Email.replace(".", "*")*/Utils.EmailAdress).child("lastLocation")
                        .setValue("lat/lng: (" + location.getLatitude() + "," + location.getLongitude() + ")");

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {


                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);


            }
        };


        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        //noinspection MissingPermission
        //(java.util.concurrent.TimeUnit.MINUTES.toMillis(Utils.timeTracking))
        locationManager.requestLocationUpdates(locationManager.NETWORK_PROVIDER, (java.util.concurrent.TimeUnit.MINUTES.toMillis(time)), 0, locationListener);
    }


}

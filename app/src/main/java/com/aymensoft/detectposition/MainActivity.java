package com.aymensoft.detectposition;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button btnDetect;
    TextView tvPositions;

    String userPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userPosition="";

        btnDetect=findViewById(R.id.btn_detect);
        tvPositions=findViewById(R.id.tv_positions);

        btnDetect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gpsPermission();
            }
        });

    }

    //request gps permission
    private void gpsPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)// NOPMD
                    != PackageManager.PERMISSION_GRANTED// NOPMD
                    || ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                                android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            }else {
                getLatLng();
            }
        }else {
            getLatLng();
        }
    }

    private void getLatLng(){
        GPSTracker tracker = new GPSTracker(this);
        if (!tracker.canGetLocation()) {// NOPMD
            //tracker.showSettingsAlert();
            Toast.makeText(this, "activate gps", Toast.LENGTH_SHORT).show();
        } else {
            String newPosition = "provider:"+tracker.getProvider()
                    +" accuracy:"+tracker.getAccuracy()
                    +" lat:"+tracker.getLatitude()
                    +" long:"+tracker.getLongitude();
            userPosition = userPosition+"\n"+newPosition;
            tvPositions.setText(userPosition);
        }
    }

}

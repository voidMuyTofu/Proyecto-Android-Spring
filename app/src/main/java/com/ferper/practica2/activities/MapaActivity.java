package com.ferper.practica2.activities;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.view.View;

import com.ferper.practica2.R;
import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationServices;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;

public class MapaActivity extends Activity {
    MapView mapaView;
    private LocationServices servicioUbicacion;
    private View btUbicacion;
    private MapboxMap mapa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapboxAccountManager.start(this,"pk.eyJ1Ijoidm9pZHRvZnUiLCJhIjoiY2pwdTY1ZnQzMDc3MzQzcWkxcnQ2bnRybiJ9.Y0x2KYVBXbYGdbEyXQfSFQ");

        setContentView(R.layout.activity_mapa);
        mapaView = findViewById(R.id.mapaView);
        mapaView.onCreate(savedInstanceState);
        ubicarUsuario();
    }
    private void ubicarUsuario() {
        servicioUbicacion = LocationServices.getLocationServices(this);
        btUbicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mapa != null){
                    Location lastLocation = servicioUbicacion.getLastLocation();
                    if(lastLocation != null){
                        mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastLocation),16));
                    }
                    mapa.setMyLocationEnabled(true);
                }
            }
        });
}

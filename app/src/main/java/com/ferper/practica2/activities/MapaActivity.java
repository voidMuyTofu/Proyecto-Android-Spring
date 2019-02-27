package com.ferper.practica2.activities;

import android.app.Activity;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.Toast;

import com.ferper.practica2.R;
import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationServices;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.services.Constants;
import com.mapbox.services.commons.ServicesException;
import com.mapbox.services.commons.geojson.LineString;
import com.mapbox.services.commons.models.Position;
import com.mapbox.services.directions.v5.DirectionsCriteria;
import com.mapbox.services.directions.v5.MapboxDirections;
import com.mapbox.services.directions.v5.models.DirectionsResponse;
import com.mapbox.services.directions.v5.models.DirectionsRoute;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapaActivity extends Activity implements View.OnClickListener, MapboxMap.OnMarkerClickListener, MapboxMap.OnMapClickListener {
    MapView mapaView;
    private LocationServices servicioUbicacion;
    private FloatingActionButton btUbicacion;
    private FloatingActionButton btRuta;
    private DirectionsRoute ruta;
    private Location locationMarker;
    private Position positionUser;

    private MapboxMap mapa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapboxAccountManager.start(this,"pk.eyJ1Ijoidm9pZHRvZnUiLCJhIjoiY2pwdTY1ZnQzMDc3MzQzcWkxcnQ2bnRybiJ9.Y0x2KYVBXbYGdbEyXQfSFQ");

        ruta = new DirectionsRoute();
        setContentView(R.layout.activity_mapa);
        mapaView = findViewById(R.id.mapaView);
        mapaView.onCreate(savedInstanceState);
        btUbicacion = findViewById(R.id.btUbicacion);
        btUbicacion.setOnClickListener(this);
        btRuta = findViewById(R.id.btRuta);
        btRuta.setOnClickListener(this);
        btRuta.setVisibility(View.INVISIBLE);

        mapaView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                mapa = mapboxMap;
                ubicarUsuario();
                addMarkers(mapa);
                addListeners();
            }
        });
    }
    private void addListeners(){
        mapa.setOnMapClickListener(this);
        mapa.setOnMarkerClickListener(this);
    }
    private void ubicarUsuario() {
        servicioUbicacion = LocationServices.getLocationServices(this);
        if(mapa != null){
            Location lastLocation = servicioUbicacion.getLastLocation();
            if(lastLocation != null){
                mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastLocation),12));
            }
            mapa.setMyLocationEnabled(true);
        }
    }

    private void obtenerRuta(Location markerLocation, Location userLocation)throws ServicesException {
        Position posicionMarker = Position.fromCoordinates(markerLocation.getLongitude(),
                markerLocation.getLatitude());
        Position posicionUser = Position.fromCoordinates(userLocation.getLongitude(),
                userLocation.getLatitude());

        //Obtener direccion entre los dos puntos
        MapboxDirections directions = new MapboxDirections.Builder()
                .setOrigin(posicionUser)
                .setDestination(posicionMarker)
                .setProfile(DirectionsCriteria.PROFILE_WALKING)
                .setAccessToken(MapboxAccountManager.getInstance().getAccessToken())
                .build();
        directions.enqueueCall(new Callback<DirectionsResponse>() {
            @Override
            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                ruta = response.body().getRoutes().get(0);
                Toast.makeText(MapaActivity.this, "Distancia: " + ruta.getDistance(), Toast.LENGTH_LONG).show();

                pintarRuta(ruta);
            }

            @Override
            public void onFailure(Call<DirectionsResponse> call, Throwable t) {

            }
        });
    }

    private void pintarRuta(DirectionsRoute ruta){
        LineString lineString = LineString.fromPolyline(ruta.getGeometry(), Constants.OSRM_PRECISION_V5);
        List<Position> coordenadas = lineString.getCoordinates();
        LatLng[] puntos = new LatLng[coordenadas.size()];
        for (int i = 0; i<coordenadas.size(); i++){
            puntos[i] = new LatLng(coordenadas.get(i).getLatitude(),coordenadas.get(i).getLongitude());
        }

        mapa.addPolyline(new PolylineOptions()
        .add(puntos)
        .color(Color.parseColor("#009688"))
        .width(5));

        if(!mapa.isMyLocationEnabled())
            mapa.setMyLocationEnabled(true);
    }

    public void addMarkers(MapboxMap mapboxMap){
        mapboxMap.addMarker(new MarkerOptions()
            .setTitle(getString(R.string.nigra)).position(new LatLng(40.413700, -3.696685)).snippet(getString(R.string.descripcion_nigra)));
        mapboxMap.addMarker(new MarkerOptions()
                .setTitle(getString(R.string.urban)).position(new LatLng(40.420593, -3.701488)).snippet(getString(R.string.descripcion_urban)));
        mapboxMap.addMarker(new MarkerOptions()
                .setTitle(getString(R.string.size)).position(new LatLng(40.421142, -3.701201)).snippet(getString(R.string.descripcion_size)));
        mapboxMap.addMarker(new MarkerOptions()
                .setTitle(getString(R.string.footlocker)).position(new LatLng(40.417657, -3.704346)).snippet(getString(R.string.descripcion_footlocker)));
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        locationMarker = new Location("click");
        locationMarker.setLatitude(marker.getPosition().getLatitude());
        locationMarker.setLongitude(marker.getPosition().getLongitude());
        btRuta.setVisibility(View.VISIBLE);
        return false;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btRuta:
                try {
                    obtenerRuta(locationMarker, mapa.getMyLocation());
                } catch (ServicesException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btUbicacion:
                ubicarUsuario();
                break;
        }

    }

    @Override
    public void onMapClick(@NonNull LatLng point) {
        btRuta.setVisibility(View.INVISIBLE);
    }
}

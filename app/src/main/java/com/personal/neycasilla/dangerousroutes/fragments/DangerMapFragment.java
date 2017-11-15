package com.personal.neycasilla.dangerousroutes.fragments;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.personal.neycasilla.dangerousroutes.activities.LocationDetailActivity;
import com.personal.neycasilla.dangerousroutes.model.DangerZone;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DangerMapFragment extends MapFragment implements OnMapReadyCallback,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMarkerClickListener, LocationListener {

    public static final String MARKER_TO_USE = "marker";
    private int REQUEST_CODE = 100;
    public static final String DANGERZONE = "danger";
    private LocationManager locationManager;
    private Location currentLocation;
    private GoogleMap privateGoogleMap;
    private static final LatLng RD = new LatLng(18.735693, -70.16265099999998);

    private List<DangerZone> dangerZones;

    private DatabaseReference databaseReference;

    public static List<Marker> markers = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        databaseReference = FirebaseDatabase.getInstance().getReference("DangerZones");
        locationManager = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);
        location();
        getMapAsync(this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE) {
            if (Manifest.permission.ACCESS_FINE_LOCATION.equals(permissions[0])
                    && grantResults[0] != PackageManager.PERMISSION_GRANTED
                    && Manifest.permission.ACCESS_COARSE_LOCATION.equals(permissions[1])
                    && grantResults[1] != PackageManager.PERMISSION_GRANTED) {
                location();
            }
        }

    }

    private void location() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                && (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)) {

            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_CODE);
        }


        currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        cameraUpdate(googleMap);
        mapChanges(googleMap);
        googleMap.setOnMapClickListener(this);
        googleMap.setOnMarkerClickListener(this);
        privateGoogleMap = googleMap;
    }

    public void mapChanges(final GoogleMap googleMap){
        databaseReference = FirebaseDatabase.getInstance().getReference("DangerZones");
        databaseReference.limitToLast(100).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                dangerZones = new ArrayList<>();
                DangerZone dangerZone = dataSnapshot.getValue(DangerZone.class);
                dangerZones.add(dangerZone);
                loadAllMarkers(googleMap);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                dangerZones = new ArrayList<>();
                DangerZone dangerZone = dataSnapshot.getValue(DangerZone.class);
                dangerZones.add(dangerZone);
                loadAllMarkers(googleMap);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loadAllMarkers(GoogleMap googleMap) {
        for (DangerZone dangerZone : dangerZones) {
            Marker marker = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(dangerZone.getLatitude()
                            , dangerZone.getLongitud())));
            marker.setTag(dangerZone);
            markers.add(marker);
        }
    }

    private void cameraUpdate(GoogleMap googleMap) {
        CameraUpdate cameraUpdate;
        if (currentLocation != null) {
            LatLng currentLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentLatLng,7);
            googleMap.moveCamera(cameraUpdate);
        } else {
            Toast.makeText(getActivity(), "No se encuentra pudo encontrar la ubicacion", Toast.LENGTH_SHORT).show();
            cameraUpdate = CameraUpdateFactory.newLatLngZoom(RD, 5);
            googleMap.moveCamera(cameraUpdate);
        }
    }

    @Override
    public void getMapAsync(OnMapReadyCallback onMapReadyCallback) {
        super.getMapAsync(onMapReadyCallback);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if(latLng.latitude>=17.5 && latLng.latitude<=25){
            if(latLng.longitude<=-65 && latLng.longitude>=-72){
                Intent intent = new Intent(getActivity(), LocationDetailActivity.class);
                intent.putExtra(DANGERZONE, latLng);
                startActivity(intent);
            }
        }

    }


    @Override
    public boolean onMarkerClick(Marker marker) {

        Intent intent = new Intent(getActivity(), LocationDetailActivity.class);
        intent.putExtra(MARKER_TO_USE, ((DangerZone) marker.getTag()));
        startActivity(intent);
        return false;
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}

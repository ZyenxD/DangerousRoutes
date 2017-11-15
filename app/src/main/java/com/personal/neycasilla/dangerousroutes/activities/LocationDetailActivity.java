package com.personal.neycasilla.dangerousroutes.activities;

import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.model.LatLng;
import com.personal.neycasilla.dangerousroutes.R;
import com.personal.neycasilla.dangerousroutes.fragments.DangerMapFragment;
import com.personal.neycasilla.dangerousroutes.fragments.LocationDetailFragment;
import com.personal.neycasilla.dangerousroutes.fragments.RegisterDangerZoneFragment;
import com.personal.neycasilla.dangerousroutes.model.DangerZone;

public class LocationDetailActivity extends AppCompatActivity implements
        LocationDetailFragment.dangerZoneData, RegisterDangerZoneFragment.registerADangerZone {

    private DangerZone dangerZone;
    private LatLng latLng;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_detail);
        Bundle bundle = getIntent().getExtras();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        if(bundle.get(DangerMapFragment.MARKER_TO_USE) != null){
            LocationDetailFragment detailFragment = new LocationDetailFragment();
            detailFragment.setDangerZoneData(this);
            dangerZone = (DangerZone)bundle.get(DangerMapFragment.MARKER_TO_USE);
            transaction.replace(R.id.fragemnt_location_continer, detailFragment).commit();

        }else {
            RegisterDangerZoneFragment registerFragment = new RegisterDangerZoneFragment();
            latLng = (LatLng)bundle.get(DangerMapFragment.DANGERZONE);
            registerFragment.setRegisterADangerZone(this);
            transaction.replace(R.id.fragemnt_location_continer, registerFragment).commit();
        }
    }


    @Override
    public DangerZone getZone() {
        return dangerZone;
    }

    @Override
    public LatLng positionOfDanger() {
        return latLng;
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
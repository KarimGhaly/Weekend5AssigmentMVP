package com.example.admin.weekend5assigmentmvp.view.main_activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.admin.weekend5assigmentmvp.R;
import com.example.admin.weekend5assigmentmvp.model.GooglePlaces.GoogleResponse;
import com.example.admin.weekend5assigmentmvp.model.GooglePlaces.Result;
import com.example.admin.weekend5assigmentmvp.view.details_activity.DetailsActivity;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class MainActivity extends AppCompatActivity implements Adapter.RecyclerViewListner, MainActivityContract.View {
    public static final String TAG = "MainActivityTAG";
    Adapter rvAdapter;
    MainActivityPresenter presenter;
    GoogleMap mGoogleMap;
    MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter = new MainActivityPresenter();
        presenter.attach(this);
        CheckPermission();
        setMap();
        setAutoComplete();

    }


    private void CheckPermission()
    {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {


            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {


            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        205);
            }
        } else {
                   getLocation();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 205: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    getLocation();

                } else {

                    Toast.makeText(this, "Need this location", Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            205);
                }

            }

        }

    }

    public void getLocation()
    {
       FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    111);
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                Log.d(TAG, "onSuccess: "+location);
                presenter.GetNearbyPlaces(location);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Failed to Get your location", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: " + e.toString());
            }
        });
    }

    public void setMap()
    {
        mapView = findViewById(R.id.mapView);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
        }
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                MapsInitializer.initialize(MainActivity.this);
                mGoogleMap = googleMap;
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            }

        });

    }

    public void setAutoComplete(){
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                presenter.getPlaceByName(place.getName().toString());
            }

            @Override
            public void onError(Status status) {
                Toast.makeText(MainActivity.this, "Error to Get Place Name", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onError: "+status.toString());
            }
        });
    }

    @Override
    public void showError(String s) {
        Toast.makeText(this, "Error Occurred", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "showError: "+s.toString());
    }

    @Override
    public void showToast(String MSG) {
        Toast.makeText(this, MSG, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void ShowRecyclerView(GoogleResponse googleResponse) {
        RecyclerView RVList = (RecyclerView) findViewById(R.id.RVList);
        rvAdapter = new Adapter(MainActivity.this, googleResponse, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        RVList.setAdapter(rvAdapter);
        RVList.setLayoutManager(layoutManager);
        RVList.setItemAnimator(itemAnimator);

    }

    @Override
    public void updateMap(GoogleResponse response) {
        double MinS = response.getResults().get(0).getGeometry().getLocation().getLat();
        double MinW = response.getResults().get(0).getGeometry().getLocation().getLng();
        double MaxN = MinS;
        double MaxE = MinW;

        for(Result r: response.getResults())
        {
            double lat = r.getGeometry().getLocation().getLat();
            double lng = r.getGeometry().getLocation().getLng();
            mGoogleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(lat,lng))
                    .title(r.getName()));
            if(MinS>lat)
            {
                MinS = lat;
            }
            if(MinW>lng)
            {
                MinW = lng;
            }
            if(MaxN<lat)
            {
                MaxN = lat;
            }
            if(MaxE<lng)
            {
                MaxE = lng;
            }
        }
        LatLngBounds bounds = new LatLngBounds(new LatLng(MinS, MinW), new LatLng(MaxN, MaxE));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds,0));
    }

    @Override
    public void updateRecyclerView(GoogleResponse googleResponse) {
        rvAdapter.UpdateRVLIST(googleResponse);
    }

    @Override
    public void OpenDetailsAct(String placeID) {
        Intent intent = new Intent(MainActivity.this,DetailsActivity.class);
        intent.putExtra("PLACEID",placeID);
        startActivity(intent);
    }

    @Override
    public void itemClicked(String placeID) {
        presenter.openDetailsActivity(placeID);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       presenter.getPlaceByCategory(item.getTitle().toString().toLowerCase());
        return true;
    }
}

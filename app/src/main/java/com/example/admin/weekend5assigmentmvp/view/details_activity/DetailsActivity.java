package com.example.admin.weekend5assigmentmvp.view.details_activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.weekend5assigmentmvp.R;
import com.example.admin.weekend5assigmentmvp.model.PlaceDetails.PlaceResponse;

public class DetailsActivity extends AppCompatActivity implements DetailsActivityContract.View {
    public static final String TAG = "DetailActivityTAG";
    PlaceResponse placeResponse;
    ViewPager viewPager;
    TextView txtName;
    TextView txtAddress;
    TextView txtPhone;
    TextView txtWebsite;
    TextView txtOpenNow;
    TextView txtPriceLevel;

    DetailsActivityPresenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        presenter = new DetailsActivityPresenter();
        presenter.attach(this);
        SetBingers();
        Intent intent = getIntent();
        Log.d(TAG, "onCreate: "+intent.getStringExtra("PLACEID"));
        presenter.getPlace(intent.getStringExtra("PLACEID"));

    }
    public void SetBingers()
    {
        txtName = findViewById(R.id.tv_name);
        txtAddress = findViewById(R.id.tv_formatted_address);
        txtOpenNow = findViewById(R.id.tv_open_now);
        txtPhone = findViewById(R.id.tv_formatted_phone_number);
        txtWebsite = findViewById(R.id.tv_website);
        txtPriceLevel = findViewById(R.id.tv_price_level);
        viewPager = findViewById(R.id.viewPager1ager1);

    }
    @Override
    public void showError(String s) {
        Toast.makeText(this, "Error Occurred", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "showError: "+s);
    }

    @Override
    public void showToast(String MSG) {
        Toast.makeText(this, MSG, Toast.LENGTH_SHORT).show();
    }



    @Override
    public void updateUI() {
        txtName.setText(placeResponse.getResult().getName());
        txtAddress.setText("Address: "+placeResponse.getResult().getFormattedAddress());
        txtPhone.setText("Phone: "+placeResponse.getResult().getFormattedPhoneNumber());
        txtWebsite.setText("Website: "+ placeResponse.getResult().getWebsite());
        txtPriceLevel.setText("Price Level: "+placeResponse.getResult().getPriceLevel());
        String Open="";
        if(placeResponse.getResult().getOpeningHours().getOpenNow())
        {
            Open = "Open Now";
            txtOpenNow.setTextColor(Color.GREEN);
        }
        else
        {
            Open = "Closed";
            txtOpenNow.setTextColor(Color.RED);
        }
        txtOpenNow.setText(Open);
        VPAdapter vpAdapter = new VPAdapter(placeResponse,this);
        viewPager.setAdapter(vpAdapter);
    }

    @Override
    public void openGoogleMaps(String URI) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(URI));
        startActivity(intent);
    }

    @Override
    public void SetPlaceResponse(PlaceResponse placeResponse) {
        this.placeResponse = placeResponse;
    }

    public void OpenGoogleMaps(View view) {
        presenter.openGoogleMaps(placeResponse);
    }
}

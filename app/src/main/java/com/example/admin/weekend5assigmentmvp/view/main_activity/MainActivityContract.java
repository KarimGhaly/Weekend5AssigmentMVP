package com.example.admin.weekend5assigmentmvp.view.main_activity;

import android.location.Location;

import com.example.admin.weekend5assigmentmvp.BasePresenter;
import com.example.admin.weekend5assigmentmvp.BaseView;
import com.example.admin.weekend5assigmentmvp.model.GooglePlaces.GoogleResponse;

/**
 * Created by Admin on 10/1/2017.
 */

public interface MainActivityContract {

    interface View extends BaseView{
        void ShowRecyclerView(GoogleResponse googleResponse);
        void updateMap(GoogleResponse response);
        void updateRecyclerView(GoogleResponse googleResponse);
        void OpenDetailsAct(String placeID);

    }
    interface  Presenter extends BasePresenter<View>{
        void openDetailsActivity(String placeID);

    }
}

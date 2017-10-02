package com.example.admin.weekend5assigmentmvp.view.details_activity;

import com.example.admin.weekend5assigmentmvp.BasePresenter;
import com.example.admin.weekend5assigmentmvp.BaseView;
import com.example.admin.weekend5assigmentmvp.model.PlaceDetails.PlaceResponse;

/**
 * Created by Admin on 10/1/2017.
 */

public interface DetailsActivityContract {

    interface View extends BaseView {
        void updateUI();
        void openGoogleMaps(String URI);
        void SetPlaceResponse(PlaceResponse placeResponse);
    }

    interface Presenter extends BasePresenter<View> {
        void getPlace(String placeID);
        void openGoogleMaps(PlaceResponse placeResponse);
    }
}

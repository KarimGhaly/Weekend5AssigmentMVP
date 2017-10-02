package com.example.admin.weekend5assigmentmvp.view.details_activity;

import android.util.Log;

import com.example.admin.weekend5assigmentmvp.DataSource.RetrofitHelper;
import com.example.admin.weekend5assigmentmvp.model.PlaceDetails.PlaceResponse;
import com.example.admin.weekend5assigmentmvp.view.main_activity.MainActivityContract;
import com.example.admin.weekend5assigmentmvp.view.main_activity.MainActivityPresenter;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Admin on 10/1/2017.
 */

public class DetailsActivityPresenter implements DetailsActivityContract.Presenter {
    DetailsActivityContract.View view;
    @Override
    public void attach(DetailsActivityContract.View view) {
        this.view = view;
    }

    @Override
    public void detach() {
        this.view = null;
    }

    @Override
    public void getPlace(String placeID) {
        Observable<PlaceResponse> ObservableResult = RetrofitHelper.getPlaceDetails(placeID);
        Observer<PlaceResponse> ObserverResult = new Observer<PlaceResponse>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull PlaceResponse placeResponse) {
                view.SetPlaceResponse(placeResponse);
               view.updateUI();
            }

            @Override
            public void onError(@NonNull Throwable e) {
               view.showError(e.toString());
            }

            @Override
            public void onComplete() {

            }
        };
        ObservableResult.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(ObserverResult);
    }

    @Override
    public void openGoogleMaps(PlaceResponse placeResponse) {
        String URI = "http://maps.google.com/maps?q=loc:" + placeResponse.getResult().getGeometry().getLocation().getLat() + "," + placeResponse.getResult().getGeometry().getLocation().getLng() + " (" + placeResponse.getResult().getName() + ")";
        view.openGoogleMaps(URI);
    }


}

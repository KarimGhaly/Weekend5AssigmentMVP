package com.example.admin.weekend5assigmentmvp.view.main_activity;
import android.location.Location;
import android.util.Log;
import com.example.admin.weekend5assigmentmvp.DataSource.RetrofitHelper;
import com.example.admin.weekend5assigmentmvp.model.GooglePlaces.GoogleResponse;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Admin on 10/1/2017.
 */

public class MainActivityPresenter implements MainActivityContract.Presenter {
    public static final String TAG = "PresenterTAG";
    MainActivityContract.View view;
    private String latLong;

    @Override
    public void attach(MainActivityContract.View view)
    {
        this.view = view;
    }

    @Override
    public void detach()
    {
        this.view = null;

    }

    public void GetNearbyPlaces(Location currentLocation)
    {
        Log.d(TAG, "GetNearbyPlaces: "+currentLocation);
        if (currentLocation != null) {
            latLong = currentLocation.getLatitude() + "," + currentLocation.getLongitude();
            Observable<GoogleResponse> ResultObservable = RetrofitHelper.getnearbyPlaces(latLong);
            Observer<GoogleResponse> ResultObserver = new Observer<GoogleResponse>() {
                @Override
                public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                }

                @Override
                public void onNext(@io.reactivex.annotations.NonNull GoogleResponse googleResponse) {
                    Log.d(TAG, "onNext: ");
                    view.ShowRecyclerView(googleResponse);
                    view.updateMap(googleResponse);
                }

                @Override
                public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                    view.showToast("Failed to Fetch Data");

                    Log.d(TAG, "onFailure: RestAPI" + e.toString());
                }

                @Override
                public void onComplete() {

                }
            };
            ResultObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(ResultObserver);
        }
    }
    public void getPlaceByName(String placeName)
    {
        Observable<GoogleResponse> ObservableResult = RetrofitHelper.getPlaceByName(placeName);
        Observer<GoogleResponse> ObserverResult = new Observer<GoogleResponse>() {
            @Override
            public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

            }

            @Override
            public void onNext(@io.reactivex.annotations.NonNull GoogleResponse googleResponse) {
                view.updateRecyclerView(googleResponse);
                view.updateMap(googleResponse);
            }

            @Override
            public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                view.showToast("Failed to Fetch Data");
                Log.d(TAG, "onError: " + e.toString());
            }

            @Override
            public void onComplete() {

            }
        };
        ObservableResult.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(ObserverResult);
    }
    public void getPlaceByCategory(String Category)
    {
        Observable<GoogleResponse> ObservableResult = RetrofitHelper.getnearbyPlacesByType(latLong,Category);
        Observer<GoogleResponse> ObserverResult = new Observer<GoogleResponse>() {
            @Override
            public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

            }

            @Override
            public void onNext(@io.reactivex.annotations.NonNull GoogleResponse googleResponse) {
                view.updateRecyclerView(googleResponse);
                view.updateMap(googleResponse);
            }

            @Override
            public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                view.showToast("Error Occured");
                Log.d(TAG, "onFailure: " + e.toString());
            }

            @Override
            public void onComplete() {

            }
        };
        ObservableResult.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(ObserverResult);
    }
    @Override
    public void openDetailsActivity(String placeID)
    {
        view.OpenDetailsAct(placeID);
    }
}

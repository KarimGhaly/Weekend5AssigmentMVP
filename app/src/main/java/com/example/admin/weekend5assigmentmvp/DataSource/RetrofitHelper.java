package com.example.admin.weekend5assigmentmvp.DataSource;

import com.example.admin.weekend5assigmentmvp.model.GooglePlaces.GoogleResponse;
import com.example.admin.weekend5assigmentmvp.model.PlaceDetails.PlaceResponse;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Admin on 10/1/2017.
 */

public class RetrofitHelper {
    public static final String Base_URL = "https://maps.googleapis.com/maps/api/place/";
    public static final String APIKey = "AIzaSyCcPmL9pb7thWDvrdMYLfznW5yOiKhJ2mM";
    public static final int Raduis = 10000;

    public static Retrofit create(){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Base_URL).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).addConverterFactory(GsonConverterFactory.create()).build();
        return retrofit;

    }

    public static io.reactivex.Observable<GoogleResponse> getnearbyPlaces (String LatLong)
    {
        Retrofit retrofit = create();
        APIService apiService = retrofit.create(APIService.class);
        return apiService.getnearbyPlaces(LatLong,Raduis,APIKey);
    }

    public static io.reactivex.Observable<GoogleResponse> getnearbyPlacesByType (String LatLong, String Category)
    {
        Retrofit retrofit = create();
        APIService apiService = retrofit.create(APIService.class);
        return apiService.getnearbyPlacesByType(LatLong,Raduis,Category,APIKey);
    }
    public static io.reactivex.Observable<GoogleResponse> getPlaceByName(String placeName)
    {
        Retrofit retrofit = create();
        APIService apiService = retrofit.create(APIService.class);
        return apiService.getPlacebyName(placeName,APIKey);
    }
    public static io.reactivex.Observable<PlaceResponse> getPlaceDetails(String placeID)
    {
        Retrofit retrofit = create();
        APIService apiService = retrofit.create(APIService.class);
        return apiService.getPlaceDetails(placeID,APIKey);
    }

    interface APIService{
        @GET("nearbysearch/json")
        io.reactivex.Observable<GoogleResponse> getnearbyPlaces(@Query("location") String Location, @Query("radius") int Radius, @Query("key") String ApiKey);

        @GET("nearbysearch/json")
        io.reactivex.Observable<GoogleResponse> getnearbyPlacesByType(@Query("location") String Location, @Query("radius") int Radius, @Query("type") String Type, @Query("key") String ApiKey);

        @GET ("textsearch/json")
        io.reactivex.Observable<GoogleResponse> getPlacebyName(@Query("query") String Query , @Query("key") String Key);

        @GET("details/json")
        io.reactivex.Observable<PlaceResponse> getPlaceDetails(@Query("placeid") String placeID, @Query("key") String Key);
    }
}

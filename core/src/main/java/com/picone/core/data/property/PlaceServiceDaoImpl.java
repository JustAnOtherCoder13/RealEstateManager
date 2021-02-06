package com.picone.core.data.property;

import androidx.annotation.NonNull;

import com.picone.core.data.service.RetrofitClient;
import com.picone.core.domain.entity.PropertyLocation;
import com.picone.core.domain.entity.pojo.nearBySearch.NearBySearch;
import com.picone.core.domain.entity.pojo.propertyLocation.PropertyLocationPojo;

import javax.inject.Inject;

import io.reactivex.Observable;

import static com.picone.core.utils.ConstantParameters.RADIUS;

public class PlaceServiceDaoImpl {

    @Inject
    protected RetrofitClient retrofitClient;

    public PlaceServiceDaoImpl(RetrofitClient retrofitClient) {
        this.retrofitClient = retrofitClient;
    }

    public Observable<PropertyLocationPojo> getPropertyLocationForAddress(String address, String googleKey) {
        return retrofitClient.googlePlaceService().getLocationForAddress(address, googleKey);
    }

    public Observable<NearBySearch> getNearBySearchForPropertyLocation(@NonNull PropertyLocation propertyLocation, String type, String googleKey) {
        return retrofitClient.googlePlaceService().getNearBySearch(propertyLocation.getLatitude() + "," + propertyLocation.getLongitude(), RADIUS, type, googleKey);
    }
}

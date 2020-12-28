package com.picone.core.data.property;

import com.picone.core.data.service.RetrofitClient;
import com.picone.core.domain.entity.pojo.propertyLocation.PropertyLocationPojo;

import javax.inject.Inject;

import io.reactivex.Observable;

public class PlaceServiceDaoImpl {

    @Inject
    RetrofitClient retrofitClient;

    public PlaceServiceDaoImpl(RetrofitClient retrofitClient) {
        this.retrofitClient = retrofitClient;
    }

    public Observable<PropertyLocationPojo> getPropertyLocationForAddress(String address, String key){
        return retrofitClient.googlePlaceService().getLocationForAddress(address, key);
    }
}

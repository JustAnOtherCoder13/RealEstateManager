package com.picone.core.data.service;

import com.picone.core.domain.entity.pojo.propertyLocation.PropertyLocationPojo;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GooglePlaceService {

    @GET("api/geocode/json?sensor=true")
    Observable<PropertyLocationPojo> getLocationForAddress(
            @Query("address") String address,
            @Query("key") String key
    );

}

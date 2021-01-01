package com.picone.core.data.service;

import com.picone.core.domain.entity.pojo.nearBySearch.NearBySearch;
import com.picone.core.domain.entity.pojo.propertyLocation.PropertyLocationPojo;
import com.picone.core.domain.entity.pojo.staticMap.StaticMapPojo;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GooglePlaceService {

    @GET("api/geocode/json?sensor=true")
    Observable<PropertyLocationPojo> getLocationForAddress(
            @Query("address") String address,
            @Query("key") String googleKey
    );

    @GET("api/staticmap/json?sensor=true")
    Observable<StaticMapPojo> getStaticMapForLatLng(
            @Query("center") String latLng,
            @Query("zoom") int zoom,
            @Query("size") String size,
            @Query("key") String googleKey
    );

    @GET("api/place/nearbysearch/json?sensor=true&type=school&fields=formatted_address,name,geometry")
    Observable<NearBySearch> getNearbySchool(
            @Query("location") String location,
            @Query("radius") String radius,
            @Query("key") String key
    );

}

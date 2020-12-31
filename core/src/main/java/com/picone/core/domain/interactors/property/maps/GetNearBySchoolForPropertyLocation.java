package com.picone.core.domain.interactors.property.maps;

import android.util.Log;

import androidx.annotation.NonNull;

import com.picone.core.data.property.PropertyRepository;
import com.picone.core.domain.entity.PropertyLocation;
import com.picone.core.domain.entity.pojo.nearBySearch.NearBySearch;
import com.picone.core.domain.interactors.property.PropertyBaseInteractor;

import io.reactivex.Observable;

public class GetNearBySchoolForPropertyLocation extends PropertyBaseInteractor {

    public GetNearBySchoolForPropertyLocation(PropertyRepository propertyDataSource) {
        super(propertyDataSource);
    }
    public Observable<NearBySearch> getNearBySchoolForPropertyLocation(@NonNull PropertyLocation propertyLocation, String googleKey) {
        return propertyDataSource.getNearBySchoolForPropertyLocation(propertyLocation, googleKey)
                .map(nearBySearch ->{
                    Log.i("TAG", "getNearBySchoolForPropertyLocation: "+nearBySearch);
                    return nearBySearch;
                });}
    }

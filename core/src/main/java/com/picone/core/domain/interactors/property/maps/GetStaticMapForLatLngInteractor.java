package com.picone.core.domain.interactors.property.maps;

import com.google.android.gms.maps.model.LatLng;
import com.picone.core.data.property.PropertyRepository;
import com.picone.core.domain.entity.pojo.staticMap.StaticMapPojo;
import com.picone.core.domain.interactors.property.PropertyBaseInteractor;

import io.reactivex.Observable;

public class GetStaticMapForLatLngInteractor extends PropertyBaseInteractor {

    public GetStaticMapForLatLngInteractor(PropertyRepository propertyDataSource) {
        super(propertyDataSource);
    }

    public Observable<StaticMapPojo> getStaticMapForLatLng(LatLng latLng, String googleKey) {
        return propertyDataSource.getStaticMapForLatLng(latLng, googleKey);
    }
}

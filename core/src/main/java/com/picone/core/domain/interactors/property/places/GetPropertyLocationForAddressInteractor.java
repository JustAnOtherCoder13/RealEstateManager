package com.picone.core.domain.interactors.property.places;

import com.google.android.gms.maps.model.LatLng;
import com.picone.core.data.property.PropertyRepository;
import com.picone.core.domain.entity.pojo.propertyLocation.PropertyLocationPojo;
import com.picone.core.domain.interactors.property.PropertyBaseInteractor;

import io.reactivex.Observable;

public class GetPropertyLocationForAddressInteractor  extends PropertyBaseInteractor {

    public GetPropertyLocationForAddressInteractor(PropertyRepository propertyDataSource) {
        super(propertyDataSource);
    }

    public Observable<LatLng> getPropertyLocationForAddress(String address, String googleKey){
        return propertyDataSource.getPropertyLocationForAddress(address, googleKey)
                .map(this::propertyLocationPojoToLatLgn);
    }
    private LatLng propertyLocationPojoToLatLgn(PropertyLocationPojo propertyLocationPojo){
        return new LatLng(
                propertyLocationPojo.getResults().get(0).getGeometry().getLocation().getLat(),
                propertyLocationPojo.getResults().get(0).getGeometry().getLocation().getLng()
        );
    }
}

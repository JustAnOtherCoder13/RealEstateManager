package com.picone.core.domain.interactors.property.maps;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.picone.core.data.property.PropertyRepository;
import com.picone.core.domain.entity.Property;
import com.picone.core.domain.entity.PropertyLocation;
import com.picone.core.domain.entity.pojo.propertyLocation.AddressComponent;
import com.picone.core.domain.entity.pojo.propertyLocation.PropertyLocationPojo;
import com.picone.core.domain.interactors.property.PropertyBaseInteractor;

import io.reactivex.Observable;

public class GetPropertyLocationForAddressInteractor extends PropertyBaseInteractor {

    public GetPropertyLocationForAddressInteractor(PropertyRepository propertyDataSource) {
        super(propertyDataSource);
    }

    public Observable<PropertyLocation> getPropertyLocationForAddress(Property property, String googleKey) {
        return propertyDataSource.getPropertyLocationForAddress(property.getAddress(), googleKey)
                .map(propertyLocationPojo -> propertyLocationPojoToPropertyLocationModel(propertyLocationPojo, property));
    }

    private PropertyLocation propertyLocationPojoToPropertyLocationModel(PropertyLocationPojo propertyLocationPojo, Property property) {
        if (propertyLocationPojo.getResults() != null
                && !propertyLocationPojo.getResults().isEmpty())
            return new PropertyLocation(
                        property.getId(),
                        propertyLocationPojo.getResults().get(0).getGeometry().getLocation().getLat(),
                        propertyLocationPojo.getResults().get(0).getGeometry().getLocation().getLng(),
                        propertyLocationPojo.getResults().get(0).getAddressComponents().get(3).getShortName(),
                        property.getId()
                );
            else return new PropertyLocation();
    }
}

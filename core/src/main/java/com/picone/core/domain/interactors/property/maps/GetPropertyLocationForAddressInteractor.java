package com.picone.core.domain.interactors.property.maps;

import com.picone.core.data.property.PropertyRepository;
import com.picone.core.domain.entity.Property;
import com.picone.core.domain.entity.PropertyInformation;
import com.picone.core.domain.entity.PropertyLocation;
import com.picone.core.domain.entity.pojo.propertyLocation.PropertyLocationPojo;
import com.picone.core.domain.interactors.property.PropertyBaseInteractor;

import io.reactivex.Observable;

public class GetPropertyLocationForAddressInteractor extends PropertyBaseInteractor {

    public GetPropertyLocationForAddressInteractor(PropertyRepository propertyDataSource) {
        super(propertyDataSource);
    }

    public Observable<PropertyLocation> getPropertyLocationForAddress(Property property, String googleKey) {
        return propertyDataSource.getPropertyLocationForAddress(property.propertyLocation.getAddress(), googleKey)
                .map(propertyLocationPojo -> propertyLocationPojoToPropertyLocationModel(propertyLocationPojo, property));
    }

    private PropertyLocation propertyLocationPojoToPropertyLocationModel(PropertyLocationPojo propertyLocationPojo, Property property) {
        PropertyLocation propertyLocation = new PropertyLocation();
        if (propertyLocationPojo.getPropertyResults() != null
                && !propertyLocationPojo.getPropertyResults().isEmpty())
            propertyLocation = new PropertyLocation(
                    property.propertyInformation.getId(),
                    propertyLocationPojo.getPropertyResults().get(0).getPropertyGeometry().getPropertyLocation().getLat(),
                    propertyLocationPojo.getPropertyResults().get(0).getPropertyGeometry().getPropertyLocation().getLng(),
                    propertyLocationPojo.getPropertyResults().get(0).getFormattedAddress(),
                    propertyLocationPojo.getPropertyResults().get(0).getAddressComponents().get(2).getShortName(),
                    property.propertyInformation.getId()
            );
        property.propertyLocation=propertyLocation;
        return propertyLocation;
    }
}

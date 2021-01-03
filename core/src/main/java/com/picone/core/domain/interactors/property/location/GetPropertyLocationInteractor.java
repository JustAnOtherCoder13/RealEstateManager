package com.picone.core.domain.interactors.property.location;

import com.picone.core.data.property.PropertyRepository;
import com.picone.core.domain.entity.PropertyLocation;
import com.picone.core.domain.interactors.property.PropertyBaseInteractor;

import io.reactivex.Observable;

public class GetPropertyLocationInteractor extends PropertyBaseInteractor {

    public GetPropertyLocationInteractor(PropertyRepository propertyDataSource) {
        super(propertyDataSource);
    }

    public Observable<PropertyLocation> getPropertyLocationForPropertyId(int propertyId) {
        return propertyDataSource.getPropertyLocationForPropertyId(propertyId);
    }
}

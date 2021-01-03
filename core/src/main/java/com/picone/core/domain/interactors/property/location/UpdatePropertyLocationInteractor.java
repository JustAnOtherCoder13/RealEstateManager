package com.picone.core.domain.interactors.property.location;

import com.picone.core.data.property.PropertyRepository;
import com.picone.core.domain.entity.PropertyLocation;
import com.picone.core.domain.interactors.property.PropertyBaseInteractor;

import io.reactivex.Completable;

public class UpdatePropertyLocationInteractor extends PropertyBaseInteractor {

    public UpdatePropertyLocationInteractor(PropertyRepository propertyDataSource) {
        super(propertyDataSource);
    }

    public Completable updatePropertyLocation(PropertyLocation propertyLocation){
        return propertyDataSource.addPropertyLocation(propertyLocation);
    }
}

package com.picone.core.domain.interactors.property.location;

import com.picone.core.data.property.PropertyRepository;
import com.picone.core.domain.entity.PropertyLocation;

import io.reactivex.Completable;

public class AddPropertyLocationInteractor extends BasePropertyLocationInteractor {

    public AddPropertyLocationInteractor(PropertyRepository propertyDataSource) {
        super(propertyDataSource);
    }

    public Completable addPropertyLocation(PropertyLocation propertyLocation) {
        return propertyDataSource.addPropertyLocation(propertyLocation);
    }
    }

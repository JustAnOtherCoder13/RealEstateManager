package com.picone.core.domain.interactors.property.location;

import com.picone.core.data.property.PropertyRepository;

import javax.inject.Inject;

abstract public class BasePropertyLocationInteractor {

    @Inject
    PropertyRepository propertyDataSource;

    public BasePropertyLocationInteractor(PropertyRepository propertyDataSource) {
        this.propertyDataSource = propertyDataSource;
    }
}

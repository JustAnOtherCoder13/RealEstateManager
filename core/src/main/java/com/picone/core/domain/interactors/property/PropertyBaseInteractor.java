package com.picone.core.domain.interactors.property;

import com.picone.core.data.property.PropertyRepository;

import javax.inject.Inject;

public abstract class PropertyBaseInteractor {

    @Inject
    protected PropertyRepository propertyDataSource;

    public PropertyBaseInteractor(PropertyRepository propertyDataSource) {
        this.propertyDataSource = propertyDataSource;
    }
}

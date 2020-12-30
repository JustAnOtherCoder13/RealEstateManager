package com.picone.core.domain.interactors.property;

import com.picone.core.data.property.PropertyRepository;
import com.picone.core.domain.entity.Property;

import io.reactivex.Completable;

public class UpdatePropertyInteractor extends PropertyBaseInteractor {

    public UpdatePropertyInteractor(PropertyRepository propertyDataSource) {
        super(propertyDataSource);
    }

    public Completable updateProperty(Property property) {
        return propertyDataSource.updateProperty(property);
    }
}

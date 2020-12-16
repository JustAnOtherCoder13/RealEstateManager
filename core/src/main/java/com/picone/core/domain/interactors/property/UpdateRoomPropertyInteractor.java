package com.picone.core.domain.interactors.property;

import com.picone.core.data.property.PropertyRepository;
import com.picone.core.domain.entity.Property;

import io.reactivex.Completable;

public class UpdateRoomPropertyInteractor extends PropertyBaseInteractor {

    public UpdateRoomPropertyInteractor(PropertyRepository propertyDataSource) {
        super(propertyDataSource);
    }

    public Completable updateRoomProperty(Property property) {
        return propertyDataSource.updateRoomProperty(property);
    }
}

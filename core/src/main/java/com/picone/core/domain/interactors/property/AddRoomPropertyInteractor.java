package com.picone.core.domain.interactors.property;

import com.picone.core.data.property.PropertyRepository;
import com.picone.core.domain.entity.Property;

import io.reactivex.Completable;

public class AddRoomPropertyInteractor extends PropertyBaseInteractor {

    public AddRoomPropertyInteractor(PropertyRepository propertyDataSource) {
        super(propertyDataSource);
    }

    public Completable addRoomProperty(Property property){
        return propertyDataSource.addRoomProperty(property);
    }
}

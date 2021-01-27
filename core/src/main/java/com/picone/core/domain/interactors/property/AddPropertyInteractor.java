package com.picone.core.domain.interactors.property;

import com.picone.core.data.property.PropertyRepository;
import com.picone.core.domain.entity.PropertyInformation;

import io.reactivex.Completable;

public class AddPropertyInteractor extends PropertyBaseInteractor {

    public AddPropertyInteractor(PropertyRepository propertyDataSource) {
        super(propertyDataSource);
    }

    public Completable addRoomProperty(PropertyInformation propertyInformation){
        return propertyDataSource.addProperty(propertyInformation);
    }
}

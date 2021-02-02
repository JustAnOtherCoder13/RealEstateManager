package com.picone.core.domain.interactors.property.photo;

import com.picone.core.data.property.PropertyRepository;
import com.picone.core.domain.entity.PropertyMedia;
import com.picone.core.domain.interactors.property.PropertyBaseInteractor;

import io.reactivex.Completable;

public class AddPropertyPhotoInteractor extends PropertyBaseInteractor {

    public AddPropertyPhotoInteractor(PropertyRepository propertyDataSource) {
        super(propertyDataSource);
    }

    public Completable addRoomPropertyPhoto(PropertyMedia propertyMedia){
        return propertyDataSource.addPropertyPhoto(propertyMedia);
    }
}

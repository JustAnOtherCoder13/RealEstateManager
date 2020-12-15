package com.picone.core.domain.interactors.property.photo;

import com.picone.core.data.property.PropertyRepository;
import com.picone.core.domain.entity.PropertyPhoto;
import com.picone.core.domain.interactors.property.PropertyBaseInteractor;

import io.reactivex.Completable;

public class AddRoomPropertyPhotoInteractor extends PropertyBaseInteractor {

    public AddRoomPropertyPhotoInteractor(PropertyRepository propertyDataSource) {
        super(propertyDataSource);
    }

    public Completable addRoomPropertyPhoto(PropertyPhoto propertyPhoto){
        return propertyDataSource.addRoomPropertyPhoto(propertyPhoto);
    }
}

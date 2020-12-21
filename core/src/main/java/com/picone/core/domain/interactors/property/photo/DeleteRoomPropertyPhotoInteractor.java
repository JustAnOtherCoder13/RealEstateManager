package com.picone.core.domain.interactors.property.photo;

import com.picone.core.data.property.PropertyRepository;
import com.picone.core.domain.entity.PropertyPhoto;
import com.picone.core.domain.interactors.property.PropertyBaseInteractor;

import io.reactivex.Completable;

public class DeleteRoomPropertyPhotoInteractor extends PropertyBaseInteractor {

    public DeleteRoomPropertyPhotoInteractor(PropertyRepository propertyDataSource) {
        super(propertyDataSource);
    }

    public Completable deleteRoomPropertyPhoto(PropertyPhoto propertyPhoto) {
        return propertyDataSource.deleteRoomPropertyPhoto(propertyPhoto);
    }
}
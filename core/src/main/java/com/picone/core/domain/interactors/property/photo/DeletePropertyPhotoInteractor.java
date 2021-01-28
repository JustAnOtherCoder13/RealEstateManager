package com.picone.core.domain.interactors.property.photo;

import com.picone.core.data.property.PropertyRepository;
import com.picone.core.domain.entity.PropertyPhoto;
import com.picone.core.domain.interactors.property.PropertyBaseInteractor;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

public class DeletePropertyPhotoInteractor extends PropertyBaseInteractor {

    public DeletePropertyPhotoInteractor(PropertyRepository propertyDataSource) {
        super(propertyDataSource);
    }

    public Completable deleteRoomPropertyPhoto(PropertyPhoto propertyPhoto) {
        return propertyDataSource.deletePropertyPhoto(propertyPhoto);
    }


    public Completable deleteSelectedPhotoForProperty(List<PropertyPhoto> photoToDelete) {
        return Observable.fromIterable(photoToDelete)
                .flatMapCompletable(propertyPhoto -> propertyDataSource.deletePropertyPhoto(propertyPhoto));
    }

}

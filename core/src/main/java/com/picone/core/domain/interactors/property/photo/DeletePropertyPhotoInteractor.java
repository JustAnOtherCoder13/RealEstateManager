package com.picone.core.domain.interactors.property.photo;

import com.picone.core.data.property.PropertyRepository;
import com.picone.core.domain.entity.PropertyMedia;
import com.picone.core.domain.interactors.property.PropertyBaseInteractor;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

public class DeletePropertyPhotoInteractor extends PropertyBaseInteractor {

    public DeletePropertyPhotoInteractor(PropertyRepository propertyDataSource) {
        super(propertyDataSource);
    }

    public Completable deleteRoomPropertyPhoto(PropertyMedia propertyMedia) {
        return propertyDataSource.deletePropertyPhoto(propertyMedia);
    }


    public Completable deleteSelectedPhotoForProperty(List<PropertyMedia> photoToDelete) {
        return Observable.fromIterable(photoToDelete)
                .flatMapCompletable(propertyPhoto -> propertyDataSource.deletePropertyPhoto(propertyPhoto));
    }

}

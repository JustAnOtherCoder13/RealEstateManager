package com.picone.core.domain.interactors.property.media;

import com.picone.core.data.property.PropertyRepository;
import com.picone.core.domain.entity.PropertyMedia;
import com.picone.core.domain.interactors.property.PropertyBaseInteractor;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

public class DeletePropertyMediaInteractor extends PropertyBaseInteractor {

    public DeletePropertyMediaInteractor(PropertyRepository propertyDataSource) {
        super(propertyDataSource);
    }

    public Completable deletePropertyMedia(PropertyMedia propertyMedia) {
        return propertyDataSource.deletePropertyMedia(propertyMedia);
    }

    public Completable deleteSelectedMediaForProperty(List<PropertyMedia> photoToDelete) {
        return Observable.fromIterable(photoToDelete)
                .flatMapCompletable(propertyPhoto -> propertyDataSource.deletePropertyMedia(propertyPhoto));
    }

}

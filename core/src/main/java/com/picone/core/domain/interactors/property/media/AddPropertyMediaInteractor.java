package com.picone.core.domain.interactors.property.media;

import com.picone.core.data.property.PropertyRepository;
import com.picone.core.domain.entity.PropertyMedia;
import com.picone.core.domain.interactors.property.PropertyBaseInteractor;

import io.reactivex.Completable;

public class AddPropertyMediaInteractor extends PropertyBaseInteractor {

    public AddPropertyMediaInteractor(PropertyRepository propertyDataSource) {
        super(propertyDataSource);
    }

    public Completable addPropertyMedia(PropertyMedia propertyMedia){
        return propertyDataSource.addPropertyMedia(propertyMedia);
    }
}

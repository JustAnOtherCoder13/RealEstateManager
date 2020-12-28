package com.picone.core.domain.interactors.property.photo;

import com.picone.core.data.property.PropertyRepository;
import com.picone.core.domain.entity.PropertyPhoto;
import com.picone.core.domain.interactors.property.PropertyBaseInteractor;

import java.util.List;

import io.reactivex.Observable;

public class GetAllPropertyPhotosForPropertyIdInteractor extends PropertyBaseInteractor {

    public GetAllPropertyPhotosForPropertyIdInteractor(PropertyRepository propertyDataSource) {
        super(propertyDataSource);
    }

    public Observable<List<PropertyPhoto>> getAllPhotosForPropertyId(int propertyId){
        return propertyDataSource.getAllPhotosForPropertyId(propertyId);
    }
}

package com.picone.core.domain.interactors.property;

import com.picone.core.data.property.PropertyRepository;
import com.picone.core.domain.entity.PropertyPhoto;

import java.util.List;

import io.reactivex.Observable;

public class GetAllRoomPropertyPhotosForPropertyIdInteractor extends PropertyBaseInteractor {

    public GetAllRoomPropertyPhotosForPropertyIdInteractor(PropertyRepository propertyDataSource) {
        super(propertyDataSource);
    }

    public Observable<List<PropertyPhoto>> getAllPhotosForPropertyId(int propertyId){
        return propertyDataSource.getAllRoomPhotosForPropertyId(propertyId);
    }
}

package com.picone.core.domain.interactors.property;

import com.picone.core.data.property.PropertyRepository;
import com.picone.core.domain.entity.PointOfInterest;

import java.util.List;

import io.reactivex.Observable;

public class GetAllPointOfInterestForPropertyIdInteractor extends PropertyBaseInteractor {

    public GetAllPointOfInterestForPropertyIdInteractor(PropertyRepository propertyDataSource) {
        super(propertyDataSource);
    }

    public Observable<List<PointOfInterest>> getAllPointOfInterestForPropertyId(int propertyId){
        return propertyDataSource.getAllPointOfInterestForPropertyId(propertyId);
    }
}

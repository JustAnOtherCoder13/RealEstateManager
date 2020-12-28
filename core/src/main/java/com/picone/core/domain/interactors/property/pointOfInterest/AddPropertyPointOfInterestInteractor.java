package com.picone.core.domain.interactors.property.pointOfInterest;

import com.picone.core.data.property.PropertyRepository;
import com.picone.core.domain.entity.PointOfInterest;
import com.picone.core.domain.interactors.property.PropertyBaseInteractor;

import io.reactivex.Completable;

public class AddPropertyPointOfInterestInteractor extends PropertyBaseInteractor {

    public AddPropertyPointOfInterestInteractor(PropertyRepository propertyDataSource) {
        super(propertyDataSource);
    }

    public Completable addRoomPropertyPointOfInterest(PointOfInterest pointOfInterest) {
        return propertyDataSource.addPropertyPointOfInterest(pointOfInterest);
    }

}

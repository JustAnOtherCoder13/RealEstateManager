package com.picone.core.domain.interactors.property.pointOfInterest;

import com.picone.core.data.property.PropertyRepository;
import com.picone.core.domain.entity.PointOfInterest;
import com.picone.core.domain.interactors.property.PropertyBaseInteractor;

import io.reactivex.Completable;

public class AddRoomPropertyPointOfInterestInteractor extends PropertyBaseInteractor {

    public AddRoomPropertyPointOfInterestInteractor(PropertyRepository propertyDataSource) {
        super(propertyDataSource);
    }

    public Completable addRoomPropertyPointOfInterest(PointOfInterest pointOfInterest) {
        return propertyDataSource.addRoomPropertyPointOfInterest(pointOfInterest);
    }

}

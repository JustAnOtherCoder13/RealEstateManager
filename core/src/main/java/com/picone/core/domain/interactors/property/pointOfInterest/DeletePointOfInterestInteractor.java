package com.picone.core.domain.interactors.property.pointOfInterest;

import com.picone.core.data.property.PropertyRepository;
import com.picone.core.domain.entity.PointOfInterest;
import com.picone.core.domain.interactors.property.PropertyBaseInteractor;

import io.reactivex.Completable;

public class DeletePointOfInterestInteractor extends PropertyBaseInteractor {

    public DeletePointOfInterestInteractor(PropertyRepository propertyDataSource) {
        super(propertyDataSource);
    }

    public Completable deletePropertyPointOfInterest(PointOfInterest pointOfInterest) {
        return propertyDataSource.deletePropertyPointOfInterest(pointOfInterest);
    }
}

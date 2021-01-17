package com.picone.core.domain.interactors.property.pointOfInterest;

import com.picone.core.data.property.PropertyRepository;
import com.picone.core.domain.entity.PointOfInterest;
import com.picone.core.domain.interactors.property.PropertyBaseInteractor;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;

public class GetAllPointOfInterestsForAllPropertiesInteractor extends PropertyBaseInteractor {

    public GetAllPointOfInterestsForAllPropertiesInteractor(PropertyRepository propertyDataSource) {
        super(propertyDataSource);
    }

    public Observable<List<PointOfInterest>> getAllPointsOfInterestForAllProperties() {
        return propertyDataSource.getAllPointsOfInterestForAllProperties();
    }
}

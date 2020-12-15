package com.picone.core.data.property;

import com.picone.core.domain.entity.PointOfInterest;
import com.picone.core.domain.entity.Property;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

public class PropertyRepository {

    @Inject
    PropertyDaoImpl propertyDao;

    public PropertyRepository(PropertyDaoImpl propertyDao) {
        this.propertyDao = propertyDao;
    }

    public Observable<List<Property>> getAllProperties() {
        return propertyDao.getAllProperties();
    }

    public Observable<List<PointOfInterest>> getAllPointOfInterestForPropertyId(int propertyId) {
        return propertyDao.getAllPointOfInterestForPropertyId(propertyId);
    }

}

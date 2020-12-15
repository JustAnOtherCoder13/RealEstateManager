package com.picone.core.data.property;

import com.picone.core.domain.entity.PointOfInterest;
import com.picone.core.domain.entity.Property;
import com.picone.core.domain.entity.PropertyPhoto;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

public class PropertyRepository {

    @Inject
    PropertyDaoImpl propertyDao;

    public PropertyRepository(PropertyDaoImpl propertyDao) {
        this.propertyDao = propertyDao;
    }

    public Observable<List<Property>> getAllRoomProperties() {
        return propertyDao.getAllRoomProperties();
    }

    public Observable<List<PointOfInterest>> getAllRoomPointOfInterestForPropertyId(int propertyId) {
        return propertyDao.getAllRoomPointOfInterestForPropertyId(propertyId);
    }

    public Observable<List<PropertyPhoto>> getAllRoomPhotosForPropertyId(int propertyId){
        return propertyDao.getAllRoomPhotosForPropertyId(propertyId);
    }

}

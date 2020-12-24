package com.picone.core.data.property;

import com.picone.core.domain.entity.PointOfInterest;
import com.picone.core.domain.entity.Property;
import com.picone.core.domain.entity.PropertyLocation;
import com.picone.core.domain.entity.PropertyPhoto;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
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

    public Observable<PropertyLocation> getPropertyLocationForPropertyId(int propertyId){
        return propertyDao.getPropertyLocationForPropertyId(propertyId);
    }

    public Completable addPropertyLocation(PropertyLocation propertyLocation) {
        return propertyDao.addPropertyLocation(propertyLocation);
    }

        public Completable addRoomProperty(Property property){
        return propertyDao.addRoomProperty(property);
    }

    public Completable addRoomPropertyPointOfInterest(PointOfInterest pointOfInterest) {
        return propertyDao.addRoomPropertyPointOfInterest(pointOfInterest);
    }

    public Completable addRoomPropertyPhoto(PropertyPhoto propertyPhoto){
        return propertyDao.addRoomPropertyPhoto(propertyPhoto);
    }

    public Completable deleteRoomPropertyPhoto(PropertyPhoto propertyPhoto) {
        return propertyDao.deleteRoomPropertyPhoto(propertyPhoto);
    }

    public Completable updateRoomProperty(Property property) {
        return propertyDao.updateRoomProperty(property);
    }
}

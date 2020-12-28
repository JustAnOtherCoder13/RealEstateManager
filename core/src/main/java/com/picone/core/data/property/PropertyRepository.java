package com.picone.core.data.property;

import com.picone.core.domain.entity.PointOfInterest;
import com.picone.core.domain.entity.Property;
import com.picone.core.domain.entity.PropertyLocation;
import com.picone.core.domain.entity.pojo.propertyLocation.PropertyLocationPojo;
import com.picone.core.domain.entity.PropertyPhoto;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;

public class PropertyRepository {

    @Inject
    protected PropertyDaoImpl propertyDao;
    @Inject
    protected PlaceServiceDaoImpl placeServiceDao;

    public PropertyRepository(PropertyDaoImpl propertyDao,PlaceServiceDaoImpl placeServiceDao) {
        this.propertyDao = propertyDao;
        this.placeServiceDao = placeServiceDao;
    }

    public Observable<List<Property>> getAllProperties() {
        return propertyDao.getAllProperties();
    }

    public Observable<List<PointOfInterest>> getAllPointOfInterestForPropertyId(int propertyId) {
        return propertyDao.getAllPointOfInterestForPropertyId(propertyId);
    }

    public Observable<List<PropertyPhoto>> getAllPhotosForPropertyId(int propertyId){
        return propertyDao.getAllPhotosForPropertyId(propertyId);
    }

    public Observable<PropertyLocation> getPropertyLocationForPropertyId(int propertyId){
        return propertyDao.getPropertyLocationForPropertyId(propertyId);
    }

    public Completable addPropertyLocation(PropertyLocation propertyLocation) {
        return propertyDao.addPropertyLocation(propertyLocation);
    }

        public Completable addProperty(Property property){
        return propertyDao.addProperty(property);
    }

    public Completable addPropertyPointOfInterest(PointOfInterest pointOfInterest) {
        return propertyDao.addPropertyPointOfInterest(pointOfInterest);
    }

    public Completable addPropertyPhoto(PropertyPhoto propertyPhoto){
        return propertyDao.addPropertyPhoto(propertyPhoto);
    }

    public Completable deletePropertyPhoto(PropertyPhoto propertyPhoto) {
        return propertyDao.deletePropertyPhoto(propertyPhoto);
    }

    public Completable updateProperty(Property property) {
        return propertyDao.updateProperty(property);
    }

    //----------------------place

    public Observable<PropertyLocationPojo> getPropertyLocationForAddress(String address, String googleKey){
        return placeServiceDao.getPropertyLocationForAddress(address, googleKey);
    }
}

package com.picone.core.data.property;

import androidx.annotation.NonNull;

import com.picone.core.domain.entity.PointOfInterest;
import com.picone.core.domain.entity.Property;
import com.picone.core.domain.entity.PropertyInformation;
import com.picone.core.domain.entity.PropertyLocation;
import com.picone.core.domain.entity.PropertyPhoto;
import com.picone.core.domain.entity.pojo.nearBySearch.NearBySearch;
import com.picone.core.domain.entity.pojo.propertyLocation.PropertyLocationPojo;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;

public class PropertyRepository {

    @Inject
    protected PropertyDaoImpl propertyDao;
    @Inject
    protected PlaceServiceDaoImpl placeServiceDao;

    public PropertyRepository(PropertyDaoImpl propertyDao, PlaceServiceDaoImpl placeServiceDao) {
        this.propertyDao = propertyDao;
        this.placeServiceDao = placeServiceDao;
    }

    public Observable<List<PropertyInformation>> getAllProperties() {
        return propertyDao.getAllProperties();
    }

    public Observable<Property> getPropertyAndAllValues(int propertyId) {
        return propertyDao.getPropertyAndAllValues(propertyId);
    }

    public Observable<List<PointOfInterest>> getAllPointOfInterestForPropertyId(int propertyId) {
        return propertyDao.getAllPointOfInterestForPropertyId(propertyId);
    }

    public Observable<List<PropertyPhoto>> getAllPhotosForPropertyId(int propertyId) {
        return propertyDao.getAllPhotosForPropertyId(propertyId);
    }

    public Observable<List<PropertyPhoto>> getAllPhotosForAllProperties() {
        return propertyDao.getAllPhotosForAllProperties();
    }

    public Observable<List<String>> getAllRegionsForAllProperties(){
        return propertyDao.getAllRegionsForAllProperties();
    }

    public Observable<PropertyLocation> getPropertyLocationForPropertyId(int propertyId) {
        return propertyDao.getPropertyLocationForPropertyId(propertyId);
    }

    public Observable<List<PointOfInterest>> getAllPointsOfInterestForAllProperties() {
        return propertyDao.getAllPointsOfInterestForAllProperties();
    }

    public Completable addPropertyLocation(PropertyLocation propertyLocation) {
        return propertyDao.addPropertyLocation(propertyLocation);
    }

    public Completable addProperty(PropertyInformation propertyInformation) {
        return propertyDao.addProperty(propertyInformation);
    }

    public Completable addPropertyPointOfInterest(PointOfInterest pointOfInterest) {
        return propertyDao.addPropertyPointOfInterest(pointOfInterest);
    }

    public Completable addPropertyPhoto(PropertyPhoto propertyPhoto) {
        return propertyDao.addPropertyPhoto(propertyPhoto);
    }

    public Completable deletePropertyPhoto(PropertyPhoto propertyPhoto) {
        return propertyDao.deletePropertyPhoto(propertyPhoto);
    }

    public Completable deleteAllPhotoForProperty(int propertyId){
        return propertyDao.deleteAllPhotoForProperty(propertyId);
    }

    public Completable updateProperty(PropertyInformation propertyInformation) {
        return propertyDao.updateProperty(propertyInformation);
    }

    public Completable updatePropertyLocation(PropertyLocation propertyLocation) {
        return propertyDao.updatePropertyLocation(propertyLocation);
    }

    public Completable deletePropertyPointOfInterest(PointOfInterest pointOfInterest) {
        return propertyDao.deletePropertyPointOfInterest(pointOfInterest);
    }

    public Observable<PropertyLocationPojo> getPropertyLocationForAddress(String address, String googleKey) {
        return placeServiceDao.getPropertyLocationForAddress(address, googleKey);
    }

    public Observable<NearBySearch> getNearBySearchForPropertyLocation(@NonNull PropertyLocation propertyLocation, String type, String googleKey) {
        return placeServiceDao.getNearBySearchForPropertyLocation(propertyLocation, type, googleKey);
    }
}

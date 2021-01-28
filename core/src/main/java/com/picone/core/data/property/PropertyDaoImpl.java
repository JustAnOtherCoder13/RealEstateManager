package com.picone.core.data.property;

import com.picone.core.data.RealEstateManagerRoomDatabase;
import com.picone.core.domain.entity.PointOfInterest;
import com.picone.core.domain.entity.Property;
import com.picone.core.domain.entity.PropertyInformation;
import com.picone.core.domain.entity.PropertyLocation;
import com.picone.core.domain.entity.PropertyPhoto;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;

public class PropertyDaoImpl {

    @Inject
    protected RealEstateManagerRoomDatabase mRoomDatabase;
    private PropertyRoomDao mPropertyRoomDao;

    public PropertyDaoImpl(RealEstateManagerRoomDatabase mRoomDatabase) {
        this.mRoomDatabase = mRoomDatabase;
        mPropertyRoomDao = mRoomDatabase.propertyRoomDao();
    }

    public Observable<List<Property>> getAllProperties() {
        return mPropertyRoomDao.getAllProperties();
    }

    public Observable<Property> getPropertyAndAllValues(int propertyId) {
        return mPropertyRoomDao.getPropertyAndAllValues(propertyId);
    }


    public Observable<List<PointOfInterest>> getAllPointOfInterestForPropertyId(int propertyId) {
        return mPropertyRoomDao.getAllPointOfInterestForPropertyId(propertyId);
    }

    public Observable<List<PropertyPhoto>> getAllPhotosForPropertyId(int propertyId) {
        return mPropertyRoomDao.getAllPhotosForPropertyId(propertyId);
    }

    public Observable<List<PropertyPhoto>> getAllPhotosForAllProperties() {
        return mPropertyRoomDao.getAllPhotosForAllProperties();
    }

    public Observable<List<String>> getAllRegionsForAllProperties() {
        return mPropertyRoomDao.getAllRegionsForAllProperties();
    }

    public Observable<PropertyLocation> getPropertyLocationForPropertyId(int propertyId) {
        return mPropertyRoomDao.getPropertyLocationForPropertyId(propertyId);
    }

    public Observable<List<PointOfInterest>> getAllPointsOfInterestForAllProperties() {
        return mPropertyRoomDao.getAllPointsOfInterestForAllProperties();
    }

    public Completable addProperty(PropertyInformation propertyInformation) {
        return mPropertyRoomDao.addProperty(propertyInformation);
    }

    public Completable addPropertyPointOfInterest(PointOfInterest pointOfInterest) {
        return mPropertyRoomDao.addPropertyPointOfInterest(pointOfInterest);
    }

    public Completable addPropertyPhoto(PropertyPhoto propertyPhoto) {
        return mPropertyRoomDao.addPropertyPhoto(propertyPhoto);
    }

    public Completable addPropertyLocation(PropertyLocation propertyLocation) {
        return mPropertyRoomDao.addPropertyLocation(propertyLocation);
    }

    public Completable deletePropertyPhoto(PropertyPhoto propertyPhoto) {
        return mPropertyRoomDao.deletePropertyPhoto(propertyPhoto);
    }

    public Completable deleteAllPhotoForProperty(int propertyId) {
        return mPropertyRoomDao.deleteAllPhotoForProperty(propertyId);
    }


    public Completable updateProperty(PropertyInformation propertyInformation) {
        return mPropertyRoomDao.updateProperty(propertyInformation);
    }

    public Completable updatePropertyLocation(PropertyLocation propertyLocation) {
        return mPropertyRoomDao.updatePropertyLocation(propertyLocation);
    }

    public Completable deletePropertyPointOfInterest(PointOfInterest pointOfInterest) {
        return mPropertyRoomDao.deletePropertyPointOfInterest(pointOfInterest);
    }


}

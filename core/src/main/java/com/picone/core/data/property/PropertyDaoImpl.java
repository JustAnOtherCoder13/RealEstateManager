package com.picone.core.data.property;

import androidx.annotation.NonNull;

import com.picone.core.data.RealEstateManagerRoomDatabase;
import com.picone.core.domain.entity.PointOfInterest;
import com.picone.core.domain.entity.Property;
import com.picone.core.domain.entity.PropertyInformation;
import com.picone.core.domain.entity.PropertyLocation;
import com.picone.core.domain.entity.PropertyMedia;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;

public class PropertyDaoImpl implements PropertyRoomDao {

    @Inject
    protected RealEstateManagerRoomDatabase mRoomDatabase;
    private PropertyRoomDao mPropertyRoomDao;

    public PropertyDaoImpl(@NonNull RealEstateManagerRoomDatabase mRoomDatabase) {
        this.mRoomDatabase = mRoomDatabase;
        mPropertyRoomDao = mRoomDatabase.propertyRoomDao();
    }


    @Override
    public Observable<List<Property>> getAllProperties() {
        return mPropertyRoomDao.getAllProperties();
    }
    @Override
    public Completable addProperty(PropertyInformation propertyInformation) {
        return mPropertyRoomDao.addProperty(propertyInformation);
    }
    @Override
    public Completable updateProperty(PropertyInformation propertyInformation) {
        return mPropertyRoomDao.updateProperty(propertyInformation);
    }


    @Override
    public Observable<List<PointOfInterest>> getAllPointOfInterestForPropertyId(int propertyId) {
        return mPropertyRoomDao.getAllPointOfInterestForPropertyId(propertyId);
    }
    @Override
    public Completable addPropertyPointOfInterest(PointOfInterest pointOfInterest) {
        return mPropertyRoomDao.addPropertyPointOfInterest(pointOfInterest);
    }
    @Override
    public Completable deletePropertyPointOfInterest(PointOfInterest pointOfInterest) {
        return mPropertyRoomDao.deletePropertyPointOfInterest(pointOfInterest);
    }


    @Override
    public Observable<PropertyLocation> getPropertyLocationForPropertyId(int propertyId) {
        return mPropertyRoomDao.getPropertyLocationForPropertyId(propertyId);
    }
    @Override
    public Completable addPropertyLocation(PropertyLocation propertyLocation) {
        return mPropertyRoomDao.addPropertyLocation(propertyLocation);
    }
    @Override
    public Completable updatePropertyLocation(PropertyLocation propertyLocation) {
        return mPropertyRoomDao.updatePropertyLocation(propertyLocation);
    }
    @Override
    public Observable<List<String>> getAllRegionsForAllProperties() {
        return mPropertyRoomDao.getAllRegionsForAllProperties();
    }


    @Override
    public Completable addPropertyMedia(PropertyMedia propertyMedia) {
        return mPropertyRoomDao.addPropertyMedia(propertyMedia);
    }
    @Override
    public Completable deletePropertyMedia(PropertyMedia propertyMedia) {
        return mPropertyRoomDao.deletePropertyMedia(propertyMedia);
    }
}

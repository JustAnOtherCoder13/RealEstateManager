package com.picone.core.data.property;

import com.picone.core.data.RealEstateManagerRoomDatabase;
import com.picone.core.domain.entity.PointOfInterest;
import com.picone.core.domain.entity.Property;
import com.picone.core.domain.entity.PropertyPhoto;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

public class PropertyDaoImpl implements PropertyDao {

    @Inject
    protected RealEstateManagerRoomDatabase mRoomDatabase;
    private PropertyRoomDao mPropertyRoomDao;

    public PropertyDaoImpl(RealEstateManagerRoomDatabase mRoomDatabase) {
        this.mRoomDatabase = mRoomDatabase;
        mPropertyRoomDao = mRoomDatabase.propertyRoomDao();
    }

    public Observable<List<Property>> getAllRoomProperties(){
        return mPropertyRoomDao.getAllRoomProperties();
    }

    public Observable<List<PointOfInterest>> getAllRoomPointOfInterestForPropertyId(int propertyId){
        return mPropertyRoomDao.getAllRoomPointOfInterestForPropertyId(propertyId);
    }

    public Observable<List<PropertyPhoto>> getAllRoomPhotosForPropertyId(int propertyId){
        return mPropertyRoomDao.getAllRoomPhotosForPropertyId(propertyId);
    }
}

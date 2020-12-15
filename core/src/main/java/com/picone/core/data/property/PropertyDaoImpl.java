package com.picone.core.data.property;

import com.picone.core.data.RealEstateManagerRoomDatabase;
import com.picone.core.domain.entity.PointOfInterest;
import com.picone.core.domain.entity.Property;

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

    public Observable<List<Property>> getAllProperties(){
        return mPropertyRoomDao.getAllProperties();
    }

    public Observable<List<PointOfInterest>> getAllPointOfInterestForPropertyId(int propertyId){
        return mPropertyRoomDao.getAllPointOfInterestForPropertyId(propertyId);
    }
}

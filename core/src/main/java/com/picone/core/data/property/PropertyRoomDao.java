package com.picone.core.data.property;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.picone.core.domain.entity.PointOfInterest;
import com.picone.core.domain.entity.Property;
import com.picone.core.domain.entity.PropertyLocation;
import com.picone.core.domain.entity.PropertyPhoto;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

@Dao
public interface PropertyRoomDao {

    @Query("SELECT*FROM property_table")
    Observable<List<Property>> getAllProperties();

    @Query("SELECT*FROM property_point_of_interest_table WHERE property_point_of_interest_table.propertyId = :propertyId")
    Observable<List<PointOfInterest>> getAllPointOfInterestForPropertyId(int propertyId);

    @Query("SELECT*FROM property_photo_table WHERE property_photo_table.propertyId = :propertyId")
    Observable<List<PropertyPhoto>> getAllPhotosForPropertyId(int propertyId);

    @Query("SELECT*FROM property_location_table WHERE property_location_table.propertyId = :propertyId")
    Observable<PropertyLocation> getPropertyLocationForPropertyId(int propertyId);

    @Insert
    Completable addProperty(Property property);

    @Insert
    Completable addPropertyPointOfInterest(PointOfInterest pointOfInterest);

    @Insert
    Completable addPropertyPhoto(PropertyPhoto propertyPhoto);

    @Insert
    Completable addPropertyLocation(PropertyLocation propertyLocation);

    @Delete
    Completable deletePropertyPhoto(PropertyPhoto propertyPhoto);

    @Update
    Completable updateProperty(Property property);

    @Update
    Completable updatePropertyLocation(PropertyLocation propertyLocation);

    @Delete
    Completable deletePropertyPointOfInterest(PointOfInterest pointOfInterest);

}

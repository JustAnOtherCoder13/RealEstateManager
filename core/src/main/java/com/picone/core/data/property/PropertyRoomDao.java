package com.picone.core.data.property;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.picone.core.domain.entity.PointOfInterest;
import com.picone.core.domain.entity.Property;
import com.picone.core.domain.entity.PropertyInformation;
import com.picone.core.domain.entity.PropertyLocation;
import com.picone.core.domain.entity.PropertyPhoto;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

@Dao
public interface PropertyRoomDao {

    @Transaction
    @Query("SELECT*FROM property_information_table")
    Observable<List<Property>> getAllProperties();

    @Insert
    Completable addProperty(PropertyInformation propertyInformation);

    @Update
    Completable updateProperty(PropertyInformation propertyInformation);

    @Query("SELECT*FROM property_point_of_interest_table WHERE property_point_of_interest_table.propertyId = :propertyId")
    Observable<List<PointOfInterest>> getAllPointOfInterestForPropertyId(int propertyId);

    @Insert
    Completable addPropertyPointOfInterest(PointOfInterest pointOfInterest);

    @Delete
    Completable deletePropertyPointOfInterest(PointOfInterest pointOfInterest);

    @Query("SELECT*FROM property_location_table WHERE property_location_table.propertyId = :propertyId")
    Observable<PropertyLocation> getPropertyLocationForPropertyId(int propertyId);

    @Insert
    Completable addPropertyLocation(PropertyLocation propertyLocation);

    @Update
    Completable updatePropertyLocation(PropertyLocation propertyLocation);

    @Query("SELECT region FROM property_location_table")
    Observable<List<String>> getAllRegionsForAllProperties();


    @Insert
    Completable addPropertyPhoto(PropertyPhoto propertyPhoto);

    @Delete
    Completable deletePropertyPhoto(PropertyPhoto propertyPhoto);
}

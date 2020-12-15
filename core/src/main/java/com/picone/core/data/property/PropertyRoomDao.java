package com.picone.core.data.property;

import androidx.room.Dao;
import androidx.room.Query;

import com.picone.core.domain.entity.PointOfInterest;
import com.picone.core.domain.entity.Property;
import com.picone.core.domain.entity.PropertyPhoto;

import java.util.List;

import io.reactivex.Observable;

@Dao
public interface PropertyRoomDao {

    @Query("SELECT*FROM property_table")
    Observable<List<Property>> getAllRoomProperties();

    @Query("SELECT*FROM property_point_of_interest_table WHERE property_point_of_interest_table.propertyId = :propertyId")
    Observable<List<PointOfInterest>> getAllRoomPointOfInterestForPropertyId(int propertyId);

    @Query("SELECT*FROM property_photo_table WHERE property_photo_table.propertyId = :propertyId")
    Observable<List<PropertyPhoto>> getAllRoomPhotosForPropertyId(int propertyId);

}

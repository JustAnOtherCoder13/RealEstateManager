package com.picone.core.domain.entity;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class PropertyFactory {

    public PropertyFactory() {
    }

    @Embedded
    public Property property;

    @Relation(parentColumn = "id",entityColumn = "propertyId",entity = PointOfInterest.class)
    public List<PointOfInterest> pointOfInterests;

    @Relation(parentColumn = "id", entityColumn = "propertyId", entity = PropertyPhoto.class)
    public List<PropertyPhoto> photos;

    @Relation(parentColumn = "id",entityColumn = "propertyId",entity = PropertyLocation.class)
    public PropertyLocation propertyLocation;
}

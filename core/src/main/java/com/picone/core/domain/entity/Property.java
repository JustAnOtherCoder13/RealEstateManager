package com.picone.core.domain.entity;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class Property {

    public Property() {
    }

    @Embedded
    public PropertyInformation propertyInformation;

    @Relation(parentColumn = "id",entityColumn = "propertyId",entity = PointOfInterest.class)
    public List<PointOfInterest> pointOfInterests;

    @Relation(parentColumn = "id", entityColumn = "propertyId", entity = PropertyMedia.class)
    public List<PropertyMedia> medias;

    @Relation(parentColumn = "id",entityColumn = "propertyId",entity = PropertyLocation.class)
    public PropertyLocation propertyLocation;
}

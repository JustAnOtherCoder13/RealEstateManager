package com.picone.core.domain.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "property_point_of_interest", foreignKeys = @ForeignKey(entity = Property.class,
        parentColumns = "id",
        childColumns = "propertyId"))
public class PointOfInterest {

    @PrimaryKey
    private int id;
    private int propertyId;

    public PointOfInterest(int id, int propertyId) {
        this.id = id;
        this.propertyId = propertyId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(int propertyId) {
        this.propertyId = propertyId;
    }
}

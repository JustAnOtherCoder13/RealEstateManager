package com.picone.core.domain.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static com.picone.core.utils.ConstantParameters.pointOfInterestTable;

@Entity(tableName = pointOfInterestTable, foreignKeys = @ForeignKey(entity = Property.class,
        parentColumns = "id",
        childColumns = "propertyId"))
public class PointOfInterest {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(index = true)
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

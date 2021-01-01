package com.picone.core.domain.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
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
    private String name;
    private double latitude;
    private double longitude;
    private String type;
    private String icon;

    public PointOfInterest(int id, int propertyId, String name, double latitude, double longitude, String type, String icon) {
        this.id = id;
        this.propertyId = propertyId;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
        this.icon = icon;
    }

    @Ignore
    public PointOfInterest() {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}

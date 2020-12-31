package com.picone.core.domain.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import static com.picone.core.utils.ConstantParameters.propertyLocationTable;

@Entity(tableName = propertyLocationTable, foreignKeys = @ForeignKey(entity = Property.class,
        parentColumns = "id",
        childColumns = "propertyId"))
public class PropertyLocation {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private double latitude;
    private double longitude;
    private String region;
    @ColumnInfo(index = true)
    private int propertyId;

    public PropertyLocation(int id, double latitude, double longitude, String region, int propertyId) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.region = region;
        this.propertyId = propertyId;
    }

    @Ignore
    public PropertyLocation() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(int propertyId) {
        this.propertyId = propertyId;
    }

    public String getRegion() { return region; }

    public void setRegion(String region) { this.region = region; }
}

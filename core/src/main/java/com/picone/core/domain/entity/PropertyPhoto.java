package com.picone.core.domain.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "property_photo", foreignKeys = @ForeignKey(entity = Property.class,
        parentColumns = "id",
        childColumns = "propertyId"))
public class PropertyPhoto {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String photos;
    private String description;
    private int propertyId;

    public PropertyPhoto(int id, String photos, String description, int propertyId) {
        this.id = id;
        this.photos = photos;
        this.description = description;
        this.propertyId = propertyId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(int propertyId) {
        this.propertyId = propertyId;
    }
}

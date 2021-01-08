package com.picone.core.domain.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import static com.picone.core.utils.ConstantParameters.propertyPhotoTable;

@Entity(tableName = propertyPhotoTable, foreignKeys = @ForeignKey(entity = Property.class,
        parentColumns = "id",
        childColumns = "propertyId"))
public class PropertyPhoto {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String photoPath;
    private String description;
    @ColumnInfo(index = true)
    private int propertyId;

    public PropertyPhoto(int id, String photoPath, String description, int propertyId) {
        this.id = id;
        this.photoPath = photoPath;
        this.description = description;
        this.propertyId = propertyId;
    }

    @Ignore
    public PropertyPhoto() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
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

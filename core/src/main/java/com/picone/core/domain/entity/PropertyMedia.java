package com.picone.core.domain.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import static com.picone.core.utils.ConstantParameters.propertyPhotoTable;

@Entity(tableName = propertyPhotoTable, foreignKeys = @ForeignKey(entity = PropertyInformation.class,
        parentColumns = "id",
        childColumns = "propertyId"))
public class PropertyMedia {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String mediaPath;
    private String description;
    @ColumnInfo(index = true)
    private int propertyId;

    public PropertyMedia(int id, String mediaPath, String description, int propertyId) {
        this.id = id;
        this.mediaPath = mediaPath;
        this.description = description;
        this.propertyId = propertyId;
    }

    @Ignore
    public PropertyMedia() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMediaPath() {
        return mediaPath;
    }

    public void setMediaPath(String mediaPath) {
        this.mediaPath = mediaPath;
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

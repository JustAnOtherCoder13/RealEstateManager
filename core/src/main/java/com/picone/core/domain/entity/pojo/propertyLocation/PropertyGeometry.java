package com.picone.core.domain.entity.pojo.propertyLocation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PropertyGeometry {

    @SerializedName("location")
    @Expose
    private PropertyLocation propertyLocation;
    @SerializedName("location_type")
    @Expose
    private String locationType;
    @SerializedName("viewport")
    @Expose
    private PropertyViewport propertyViewport;

    public PropertyLocation getPropertyLocation() {
        return propertyLocation;
    }

    public void setPropertyLocation(PropertyLocation propertyLocation) {
        this.propertyLocation = propertyLocation;
    }

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public PropertyViewport getPropertyViewport() {
        return propertyViewport;
    }

    public void setPropertyViewport(PropertyViewport propertyViewport) {
        this.propertyViewport = propertyViewport;
    }

}
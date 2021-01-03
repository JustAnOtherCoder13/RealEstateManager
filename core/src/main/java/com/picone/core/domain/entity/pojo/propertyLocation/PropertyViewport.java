package com.picone.core.domain.entity.pojo.propertyLocation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PropertyViewport {

    @SerializedName("northeast")
    @Expose
    private PropertyNortheast propertyNortheast;
    @SerializedName("southwest")
    @Expose
    private PropertySouthwest propertySouthwest;

    public PropertyNortheast getPropertyNortheast() {
        return propertyNortheast;
    }

    public void setPropertyNortheast(PropertyNortheast propertyNortheast) {
        this.propertyNortheast = propertyNortheast;
    }

    public PropertySouthwest getPropertySouthwest() {
        return propertySouthwest;
    }

    public void setPropertySouthwest(PropertySouthwest propertySouthwest) {
        this.propertySouthwest = propertySouthwest;
    }

}
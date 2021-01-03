package com.picone.core.domain.entity.pojo.nearBySearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NearBySearchGeometry {

    @SerializedName("location")
    @Expose
    private NearBySearchLocation nearBySearchLocation;
    @SerializedName("viewport")
    @Expose
    private NearBySearchViewport nearBySearchViewport;

    public NearBySearchLocation getNearBySearchLocation() {
        return nearBySearchLocation;
    }

    public void setNearBySearchLocation(NearBySearchLocation nearBySearchLocation) {
        this.nearBySearchLocation = nearBySearchLocation;
    }

    public NearBySearchViewport getNearBySearchViewport() {
        return nearBySearchViewport;
    }

    public void setNearBySearchViewport(NearBySearchViewport nearBySearchViewport) {
        this.nearBySearchViewport = nearBySearchViewport;
    }

}
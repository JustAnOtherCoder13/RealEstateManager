package com.picone.core.domain.entity.pojo.nearBySearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NearBySearchViewport {

    @SerializedName("northeast")
    @Expose
    private NearBySearchNortheast nearBySearchNortheast;
    @SerializedName("southwest")
    @Expose
    private NearBySearchSouthwest nearBySearchSouthwest;

    public NearBySearchNortheast getNearBySearchNortheast() {
        return nearBySearchNortheast;
    }

    public void setNearBySearchNortheast(NearBySearchNortheast nearBySearchNortheast) {
        this.nearBySearchNortheast = nearBySearchNortheast;
    }

    public NearBySearchSouthwest getNearBySearchSouthwest() {
        return nearBySearchSouthwest;
    }

    public void setNearBySearchSouthwest(NearBySearchSouthwest nearBySearchSouthwest) {
        this.nearBySearchSouthwest = nearBySearchSouthwest;
    }

}

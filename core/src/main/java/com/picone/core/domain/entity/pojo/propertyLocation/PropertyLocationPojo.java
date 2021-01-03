package com.picone.core.domain.entity.pojo.propertyLocation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PropertyLocationPojo {

    @SerializedName("results")
    @Expose
    private List<PropertyResult> propertyResults = null;
    @SerializedName("status")
    @Expose
    private String status;

    public List<PropertyResult> getPropertyResults() {
        return propertyResults;
    }

    public void setPropertyResults(List<PropertyResult> propertyResults) {
        this.propertyResults = propertyResults;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
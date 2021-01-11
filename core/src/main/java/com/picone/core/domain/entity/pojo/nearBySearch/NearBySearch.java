package com.picone.core.domain.entity.pojo.nearBySearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NearBySearch {

    @SerializedName("html_attributions")
    @Expose
    private List<Object> htmlAttributions = null;
    @SerializedName("results")
    @Expose
    private List<NearBySearchResult> nearBySearchResults = null;
    @SerializedName("status")
    @Expose
    private String status;

    public List<Object> getHtmlAttributions() {
        return htmlAttributions;
    }

    public void setHtmlAttributions(List<Object> htmlAttributions) {
        this.htmlAttributions = htmlAttributions;
    }

    public List<NearBySearchResult> getNearBySearchResults() {
        return nearBySearchResults;
    }

    public void setNearBySearchResults(List<NearBySearchResult> nearBySearchResults) {
        this.nearBySearchResults = nearBySearchResults;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
package com.openclassrooms.realestatemanager.presentation.utils;

import com.picone.core.domain.entity.PointOfInterest;
import com.picone.core.domain.entity.Property;

import java.util.ArrayList;
import java.util.List;

import static com.openclassrooms.realestatemanager.presentation.utils.Utils.formatStringToDate;

public class FilterHelper {

    private List<String> mRequestPointsOfInterests;
    private List<String> mRequestPropertyType;
    private List<Property> mPropertiesTempValue;
    private List<Property> mFilteredProperties;
    private boolean mIsAnyFilterSelected;

    public List<Property> getFilteredProperties() {
        return mFilteredProperties;
    }

    public boolean getIsAnyFilterSelected() {
        return mIsAnyFilterSelected;
    }

    public void setRequestPointsOfInterests(List<String> mRequestPointsOfInterests) {
        this.mRequestPointsOfInterests = mRequestPointsOfInterests;
    }

    public void setRequestPropertyType(List<String> mRequestPropertyType) {
        this.mRequestPropertyType = mRequestPropertyType;
    }

    public FilterHelper() {
    }

    //------------------------------------------FILTER--------------------------------------------------------

    public void initFilterValue(List<Property> allProperties) {
        mIsAnyFilterSelected = false;
        mFilteredProperties = new ArrayList<>(allProperties);
    }

    public void filterByRegion(String selectedRegion) {
        initFilter();
        // check property that don't match request
        for (Property property : mFilteredProperties)
            if (!property.propertyLocation.getRegion().equalsIgnoreCase(selectedRegion))
                mPropertiesTempValue.add(property);

        applyFilter();
    }


    public void filterByNumberOfMedias(String selectedNumberOfPhoto) {
        initFilter();
        // check property that don't match request
        for (Property property : mFilteredProperties) {
            if (property.medias.size() < Integer.parseInt(selectedNumberOfPhoto))
                mPropertiesTempValue.add(property);
        }
        applyFilter();
    }

    public void filterByPointOfInterest() {
        initFilter();
        filterForPointOfInterestType();
    }

    public void filterByPropertyType() {
        mIsAnyFilterSelected = true;
        filterForType();
    }

    public void filterByOnMarketFrom(String requestDate) {
        initFilter();
        for (Property property : mFilteredProperties) {
            //check if property don't match request
            if (formatStringToDate(property.propertyInformation.getEnterOnMarket())
                    .before(formatStringToDate(requestDate))) {
                mPropertiesTempValue.add(property);
            }
        }
        applyFilter();
    }

    public void filterByPrice(float selectedMin, float selectedMax) {
        initFilter();
        for (Property property : mFilteredProperties) {
            filterForRangeSlider(selectedMin
                    , selectedMax
                    , property.propertyInformation.getPrice(), property);
        }
        applyFilter();
    }

    public void filterBySurface(float selectedMin, float selectedMax) {
        initFilter();
        for (Property property : mFilteredProperties) {
            filterForRangeSlider(selectedMin
                    , selectedMax
                    , property.propertyInformation.getPropertyArea(), property);
        }
        applyFilter();
    }

    public void filterByRoom(float selectedMin, float selectedMax) {
        initFilter();
        for (Property property : mFilteredProperties) {
            filterForRangeSlider(selectedMin
                    , selectedMax
                    , property.propertyInformation.getNumberOfRooms(), property);
        }
        applyFilter();
    }


    //--------------------------------------FILTER HELPERS--------------------------------------------------------

    private void filterForType() {
        mPropertiesTempValue = new ArrayList<>();
        for (String requestPropertyType : mRequestPropertyType) {
            //check if property match request
            for (Property property : mFilteredProperties) {
                if (property.propertyInformation.getPropertyType().equalsIgnoreCase(requestPropertyType) && !mPropertiesTempValue.contains(property)) {
                    mPropertiesTempValue.add(property);
                }
            }

        }
        //apply filter
        mFilteredProperties.clear();
        mFilteredProperties.addAll(mPropertiesTempValue);
    }


    private void filterForRangeSlider(float selectedStartValue, float selectedEndValue, float valueToCompare, Property property) {
        if (Float.compare(selectedStartValue, valueToCompare) == 1
                || Float.compare(selectedEndValue, valueToCompare) == -1) {
            mPropertiesTempValue.add(property);
        }
    }

    private void filterForPointOfInterestType() {
        for (int type = 0; type < mRequestPointsOfInterests.size(); type++) {
            // if no value in tempValue and on 1st request type
            if (mPropertiesTempValue.isEmpty() && type == 0) filterForFirstType(type);
                // if no value in temp value and not first type, means that no property match first type, so break to return empty list
            else if (mPropertiesTempValue.isEmpty() && type > 1) break;
                //else apply filter for following type
            else filterIfNotFirstType(type);
        }
        //apply final filtered value
        mFilteredProperties.clear();
        mFilteredProperties.addAll(mPropertiesTempValue);

    }

    private void filterForFirstType(int type) {
        //for first Point of interest type, add to value if match
        mPropertiesTempValue = new ArrayList<>();
        for (Property property : mFilteredProperties)
            for (PointOfInterest pointOfInterest : property.pointOfInterests) {
                if (pointOfInterest.getType().contains(mRequestPointsOfInterests.get(type))
                        && !mPropertiesTempValue.contains(property)) {
                    mPropertiesTempValue.add(property);
                }
            }
    }

    private void filterIfNotFirstType(int type) {
        for (Property property : mFilteredProperties) {
            boolean isFilteredPropertyHasRequestPointOfInterest = false;
            //for each property in filtered list check if at least one point of interest match request type
            for (PointOfInterest pointOfInterest : property.pointOfInterests) {
                if (pointOfInterest.getType().contains(mRequestPointsOfInterests.get(type))
                        && mPropertiesTempValue.contains(property)) {
                    //temp value got properties that match past point of interest
                    //if match actual type and is in list, means that match all request type
                    isFilteredPropertyHasRequestPointOfInterest = true;
                    break;
                }
            }
            //else that means that property match first type but not actual type, so remove from value
            if (!isFilteredPropertyHasRequestPointOfInterest)
                mPropertiesTempValue.remove(property);
        }
    }

    private void initFilter() {
        mPropertiesTempValue = new ArrayList<>();
        mIsAnyFilterSelected = true;
    }

    private void applyFilter() {
        if (!mPropertiesTempValue.isEmpty())
            mFilteredProperties.removeAll(mPropertiesTempValue);
    }
}

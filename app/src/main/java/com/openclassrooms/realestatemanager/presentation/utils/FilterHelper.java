package com.openclassrooms.realestatemanager.presentation.utils;

import androidx.annotation.NonNull;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.BottomSheetFilterLayoutBinding;
import com.openclassrooms.realestatemanager.databinding.BottomSheetPropertyTypeLayoutBinding;
import com.openclassrooms.realestatemanager.databinding.CustomBottomSheetPointOfInterestLayoutBinding;
import com.openclassrooms.realestatemanager.presentation.utils.customView.CustomBottomSheetRangeSlider;
import com.picone.core.domain.entity.PointOfInterest;
import com.picone.core.domain.entity.Property;

import java.util.ArrayList;
import java.util.List;

import static com.openclassrooms.realestatemanager.presentation.utils.Utils.formatStringToDate;
import static com.picone.core.utils.ConstantParameters.MAX_PRICE;
import static com.picone.core.utils.ConstantParameters.MAX_ROOM;
import static com.picone.core.utils.ConstantParameters.MAX_SURFACE;
import static com.picone.core.utils.ConstantParameters.MIN_PRICE;
import static com.picone.core.utils.ConstantParameters.MIN_ROOM;
import static com.picone.core.utils.ConstantParameters.MIN_SURFACE;

public class FilterHelper {

    private BottomSheetFilterLayoutBinding mBottomSheetFilterLayout;
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

    public FilterHelper(BottomSheetFilterLayoutBinding mBottomSheetFilterLayout) {
        this.mBottomSheetFilterLayout = mBottomSheetFilterLayout;
        initPhotoSpinner();
        initRangeSliderValues();
    }

    private void initPhotoSpinner() {
        List<String> numberOfPhotos = new ArrayList<>();
        for (int i = 1; i <= 10; i++) numberOfPhotos.add(String.valueOf(i));
        mBottomSheetFilterLayout
                .filterPropertyNumberOfPhotoSpinner.setSpinnerAdapter(numberOfPhotos);
    }

    private void initRangeSliderValues() {
        mBottomSheetFilterLayout.filterPropertyLocationPriceRangeSlider
                .setRangeSliderValue(
                        //Min price selectable
                        MIN_PRICE
                        //Max price selectable
                        , MAX_PRICE
                        //step
                        , (float) (MAX_PRICE - MIN_PRICE) / 10000);

        mBottomSheetFilterLayout.filterPropertyLocationSurfaceRangerSlider
                .setRangeSliderValue(
                        //Min area selectable
                        MIN_SURFACE,
                        //Max area selectable
                        MAX_SURFACE,
                        //step
                        (float) (MAX_SURFACE - MIN_SURFACE) / 10);

        mBottomSheetFilterLayout.filterPropertyLocationRoomRangerSlider
                .setRangeSliderValue(
                        //Min room
                        MIN_ROOM,
                        //Max room
                        MAX_ROOM,
                        //step
                        1);
    }

    //------------------------------------------FILTER--------------------------------------------------------

    public void filterProperties(List<Property> allProperties) {
        requestPointOfInterest();
        requestPropertyType();
        mIsAnyFilterSelected = false;
        filterForLocation(allProperties);
    }

    private void filterForLocation(List<Property> allProperties) {
        mFilteredProperties = new ArrayList<>();
        if (!mBottomSheetFilterLayout.filterPropertyLocationSpinner.getText().trim().isEmpty()) {
            mIsAnyFilterSelected = true;
            for (Property property : allProperties) {
                if (property.propertyLocation.getRegion().equalsIgnoreCase(mBottomSheetFilterLayout.filterPropertyLocationSpinner.getText()))
                    mFilteredProperties.add(property);
            }
        }
        //as only known regions is in spinner, if mFilteredPropertyInformation is empty mean that no region selected
        if (mFilteredProperties.isEmpty())
            mFilteredProperties.addAll(allProperties);
        filterForNumberOfPhoto();
    }

    private void filterForNumberOfPhoto() {
        mPropertiesTempValue = new ArrayList<>();
        if (!mBottomSheetFilterLayout.filterPropertyNumberOfPhotoSpinner.getText().trim().isEmpty()) {
           mIsAnyFilterSelected =true;
            // check property that don't match request
            for (Property property : mFilteredProperties) {
                if (property.photos.size() < Integer.parseInt(mBottomSheetFilterLayout.filterPropertyNumberOfPhotoSpinner.getText()))
                    mPropertiesTempValue.add(property);
            }
        }
        //apply filter
        if (!mPropertiesTempValue.isEmpty())
            mFilteredProperties.removeAll(mPropertiesTempValue);
        filterForPointOfInterest();
    }

    private void filterForPointOfInterest() {
        mPropertiesTempValue = new ArrayList<>();
        if (mBottomSheetFilterLayout.bottomSheetPointOfInterestInclude.schoolCheckBox.mCheckBox.isChecked()
                || mBottomSheetFilterLayout.bottomSheetPointOfInterestInclude.restaurantCheckBox.mCheckBox.isChecked()
                || mBottomSheetFilterLayout.bottomSheetPointOfInterestInclude.supermarketCheckBox.mCheckBox.isChecked()) {
            mIsAnyFilterSelected =true;
            filterForPointOfInterestType();
        }
        filterForPropertyType();
    }

    private void filterForPropertyType() {
        if (mBottomSheetFilterLayout.bottomSheetPropertyTypeLayoutInclude.houseCheckBox.mCheckBox.isChecked()
                || mBottomSheetFilterLayout.bottomSheetPropertyTypeLayoutInclude.penthouseCheckBox.mCheckBox.isChecked()
                || mBottomSheetFilterLayout.bottomSheetPropertyTypeLayoutInclude.flatCheckBox.mCheckBox.isChecked()
                || mBottomSheetFilterLayout.bottomSheetPropertyTypeLayoutInclude.duplexCheckBox.mCheckBox.isChecked()){
            mIsAnyFilterSelected =true;
            filterForType();
        }
        filterForOnMarketFrom();
    }

    private void filterForOnMarketFrom() {
        mPropertiesTempValue = new ArrayList<>();
        if (!mBottomSheetFilterLayout.bottomSheetOnMarketFrom.getDate().equalsIgnoreCase(mBottomSheetFilterLayout.getRoot().getResources().getString(R.string.dd_mm_yyyy))){
            mIsAnyFilterSelected =true;
            for (Property property : mFilteredProperties) {
                //check if property don't match request
                if (formatStringToDate(property.propertyInformation.getEnterOnMarket())
                        .before(formatStringToDate(mBottomSheetFilterLayout.bottomSheetOnMarketFrom.getDate()))) {
                    mPropertiesTempValue.add(property);
                }
            }
        }
        //apply filter
        if (!mPropertiesTempValue.isEmpty())
            mFilteredProperties.removeAll(mPropertiesTempValue);
        filterForRangeSlider();
    }

    private void filterForRangeSlider() {
        mPropertiesTempValue = new ArrayList<>();
        if (mBottomSheetFilterLayout.filterPropertyLocationPriceRangeSlider.getStartValue() == MIN_PRICE
                && mBottomSheetFilterLayout.filterPropertyLocationPriceRangeSlider.getEndValue() == MAX_PRICE
                && mBottomSheetFilterLayout.filterPropertyLocationSurfaceRangerSlider.getStartValue() == MIN_SURFACE
                && mBottomSheetFilterLayout.filterPropertyLocationSurfaceRangerSlider.getEndValue() == MAX_SURFACE
                && mBottomSheetFilterLayout.filterPropertyLocationRoomRangerSlider.getStartValue() == MIN_ROOM
                && mBottomSheetFilterLayout.filterPropertyLocationRoomRangerSlider.getEndValue() == MAX_ROOM)
            return;

        mIsAnyFilterSelected =true;
        for (Property property : mFilteredProperties) {
            filterForRangeSlider(mBottomSheetFilterLayout.filterPropertyLocationPriceRangeSlider, property.propertyInformation.getPrice(), property);
            filterForRangeSlider(mBottomSheetFilterLayout.filterPropertyLocationSurfaceRangerSlider, property.propertyInformation.getPropertyArea(), property);
            filterForRangeSlider(mBottomSheetFilterLayout.filterPropertyLocationRoomRangerSlider, property.propertyInformation.getNumberOfRooms(), property);
        }
        if (!mPropertiesTempValue.isEmpty())
            mFilteredProperties.removeAll(mPropertiesTempValue);
    }

    //--------------------------------------LIST HELPERS--------------------------------------------------------

    private void requestPointOfInterest() {
        String schoolStr = mBottomSheetFilterLayout.getRoot().getResources().getString(R.string.school);
        String restaurantStr = mBottomSheetFilterLayout.getRoot().getResources().getString(R.string.restaurant);
        String supermarketStr = mBottomSheetFilterLayout.getRoot().getResources().getString(R.string.supermarket);
        mRequestPointsOfInterests = new ArrayList<>();

        CustomBottomSheetPointOfInterestLayoutBinding pointOfInterestBinding =
                mBottomSheetFilterLayout.bottomSheetPointOfInterestInclude;

        if (pointOfInterestBinding.schoolCheckBox.mCheckBox.isChecked() && !mRequestPointsOfInterests.contains(schoolStr))
            mRequestPointsOfInterests.add(schoolStr);
        if (pointOfInterestBinding.restaurantCheckBox.mCheckBox.isChecked() && !mRequestPointsOfInterests.contains(restaurantStr))
            mRequestPointsOfInterests.add(restaurantStr);
        if (pointOfInterestBinding.supermarketCheckBox.mCheckBox.isChecked() && !mRequestPointsOfInterests.contains(supermarketStr))
            mRequestPointsOfInterests.add(supermarketStr);
    }

    private void requestPropertyType() {
        mRequestPropertyType = new ArrayList<>();
        String houseStr = mBottomSheetFilterLayout.getRoot().getResources().getString(R.string.house);
        String flatStr = mBottomSheetFilterLayout.getRoot().getResources().getString(R.string.flat);
        String duplexStr = mBottomSheetFilterLayout.getRoot().getResources().getString(R.string.duplex);
        String penthouseStr = mBottomSheetFilterLayout.getRoot().getResources().getString(R.string.penthouse);

        BottomSheetPropertyTypeLayoutBinding propertyTypeBinding = mBottomSheetFilterLayout.bottomSheetPropertyTypeLayoutInclude;
        if (propertyTypeBinding.houseCheckBox.mCheckBox.isChecked() && !mRequestPropertyType.contains(houseStr))
            mRequestPropertyType.add(houseStr);
        if (propertyTypeBinding.flatCheckBox.mCheckBox.isChecked() && !mRequestPropertyType.contains(flatStr))
            mRequestPropertyType.add(flatStr);
        if (propertyTypeBinding.duplexCheckBox.mCheckBox.isChecked() && !mRequestPropertyType.contains(duplexStr))
            mRequestPropertyType.add(duplexStr);
        if (propertyTypeBinding.penthouseCheckBox.mCheckBox.isChecked() && !mRequestPropertyType.contains(penthouseStr))
            mRequestPropertyType.add(penthouseStr);
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


    private void filterForRangeSlider(@NonNull CustomBottomSheetRangeSlider rangeSlider, float valueToCompare, Property property) {
        if (valueToCompare < rangeSlider.getStartValue()
                || valueToCompare > rangeSlider.getEndValue())
            mPropertiesTempValue.add(property);
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

    public void resetBottomSheetValues() {
        initRangeSliderValues();
        mBottomSheetFilterLayout.bottomSheetPointOfInterestInclude.supermarketCheckBox.mCheckBox.setChecked(false);
        mBottomSheetFilterLayout.bottomSheetPointOfInterestInclude.schoolCheckBox.mCheckBox.setChecked(false);
        mBottomSheetFilterLayout.bottomSheetPointOfInterestInclude.restaurantCheckBox.mCheckBox.setChecked(false);
        mBottomSheetFilterLayout.bottomSheetPropertyTypeLayoutInclude.duplexCheckBox.mCheckBox.setChecked(false);
        mBottomSheetFilterLayout.bottomSheetPropertyTypeLayoutInclude.houseCheckBox.mCheckBox.setChecked(false);
        mBottomSheetFilterLayout.bottomSheetPropertyTypeLayoutInclude.penthouseCheckBox.mCheckBox.setChecked(false);
        mBottomSheetFilterLayout.bottomSheetPropertyTypeLayoutInclude.flatCheckBox.mCheckBox.setChecked(false);
        mBottomSheetFilterLayout.bottomSheetOnMarketFrom.resetDate();
        mBottomSheetFilterLayout.filterPropertyLocationSpinner.resetText();
        mBottomSheetFilterLayout.filterPropertyNumberOfPhotoSpinner.resetText();
    }
}

package com.openclassrooms.realestatemanager.presentation.utils;

import android.content.Context;

import androidx.annotation.NonNull;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding;
import com.openclassrooms.realestatemanager.databinding.BottomSheetFilterLayoutBinding;
import com.openclassrooms.realestatemanager.databinding.BottomSheetPropertyTypeLayoutBinding;
import com.openclassrooms.realestatemanager.databinding.CustomBottomSheetPointOfInterestLayoutBinding;
import com.openclassrooms.realestatemanager.presentation.ui.main.MainActivity;
import com.openclassrooms.realestatemanager.presentation.utils.customView.CustomBottomSheetRangeSlider;
import com.picone.core.domain.entity.PointOfInterest;
import com.picone.core.domain.entity.Property;
import com.picone.core.domain.entity.PropertyPhoto;

import java.util.ArrayList;
import java.util.List;

import static com.openclassrooms.realestatemanager.presentation.utils.Utils.formatStringToDate;

public class FilterHelper {

    private BottomSheetFilterLayoutBinding bottomSheetFilterLayout;

    private List<String> requestPointsOfInterests;
    private List<String> requestPropertyType;
    private List<PropertyPhoto> allPropertiesPhotos = new ArrayList<>();
    private List<PointOfInterest> allPointsOfInterest = new ArrayList<>();
    private List<Property> filteredValues;
    private List<Property> filteredProperty;


    public List<Property> getFilteredProperty() {
        return filteredProperty;
    }

    public FilterHelper(BottomSheetFilterLayoutBinding bottomSheetFilterLayout) {
        this.bottomSheetFilterLayout = bottomSheetFilterLayout;
        initPhotoSpinner();
        initRangeSliderValues();
    }

    private void initPhotoSpinner() {
        List<String> numberOfPhotos = new ArrayList<>();
        for (int i = 1; i <= 10; i++) numberOfPhotos.add(String.valueOf(i));
        bottomSheetFilterLayout
                .filterPropertyNumberOfPhotoSpinner.setSpinnerAdapter(numberOfPhotos);
    }

    private void initRangeSliderValues() {
        bottomSheetFilterLayout.filterPropertyLocationPriceRangeSlider
                .setRangeSliderValue(
                        //Min price selectable
                        100000
                        //Max price selectable
                        , 1000000
                        //step
                        , (float) (1000000 - 100000) / 10000);

        bottomSheetFilterLayout.filterPropertyLocationSurfaceRangerSlider
                .setRangeSliderValue(
                        //Min area selectable
                        100,
                        //Max area selectable
                        1000,
                        //step
                        (float) (1000 - 100) / 10);

        bottomSheetFilterLayout.filterPropertyLocationRoomRangerSlider
                .setRangeSliderValue(
                        //Min room
                        5,
                        //Max room
                        20,
                        //step
                        1);
    }

    //------------------------------------------FILTER--------------------------------------------------------

    public void filterProperties(List<Property> allProperties) {
        requestPointOfInterest();
        requestPropertyType();
        filterForLocation(allProperties);
        filteredValues = new ArrayList<>();
    }

    private void filterForLocation(List<Property> allProperties) {
        filteredProperty = new ArrayList<>();
        if (!bottomSheetFilterLayout.filterPropertyLocationSpinner.getText().trim().isEmpty()) {
            for (Property property : allProperties) {
                if (property.getRegion().equalsIgnoreCase(bottomSheetFilterLayout.filterPropertyLocationSpinner.getText()))
                    filteredProperty.add(property);
            }
        }
        if (filteredProperty.isEmpty()) filteredProperty.addAll(allProperties);
        filterForNumberOfPhoto();
    }

    private void filterForNumberOfPhoto() {
        filteredValues = new ArrayList<>();
        if (!bottomSheetFilterLayout.filterPropertyNumberOfPhotoSpinner.getText().trim().isEmpty())
            for (Property property : filteredProperty) {
                // count photo corresponding to property
                int numberOfPhotosForProperty = 0;
                for (PropertyPhoto propertyPhoto : allPropertiesPhotos) {
                    if (property.getId() == propertyPhoto.getPropertyId()) {
                        numberOfPhotosForProperty++;
                    }
                }
                //add them to a list if don't match filter
                if (numberOfPhotosForProperty < Integer.parseInt(bottomSheetFilterLayout.filterPropertyNumberOfPhotoSpinner.getText()))
                    filteredValues.add(property);
            }
        //apply filter
        if (!filteredValues.isEmpty()) filteredProperty.removeAll(filteredValues);
        filterForPointOfInterest();
    }

    private void filterForPointOfInterest() {
        filteredValues = new ArrayList<>();
        if (bottomSheetFilterLayout.bottomSheetPointOfInterestInclude.schoolCheckBox.checkBox.isChecked()
                || bottomSheetFilterLayout.bottomSheetPointOfInterestInclude.restaurantCheckBox.checkBox.isChecked()
                || bottomSheetFilterLayout.bottomSheetPointOfInterestInclude.supermarketCheckBox.checkBox.isChecked()) {
            filterForPointOfInterestType();
        }
        filterForPropertyType();
    }

    private void filterForPropertyType() {
        if (bottomSheetFilterLayout.bottomSheetPropertyTypeLayoutInclude.houseCheckBox.checkBox.isChecked()
                || bottomSheetFilterLayout.bottomSheetPropertyTypeLayoutInclude.penthouseCheckBox.checkBox.isChecked()
                || bottomSheetFilterLayout.bottomSheetPropertyTypeLayoutInclude.flatCheckBox.checkBox.isChecked()
                || bottomSheetFilterLayout.bottomSheetPropertyTypeLayoutInclude.duplexCheckBox.checkBox.isChecked())
            filterForType();

        filterForOnMarketFrom();
    }

    private void filterForOnMarketFrom() {
        filteredValues = new ArrayList<>();
        if (!bottomSheetFilterLayout.bottomSheetOnMarketFrom.getDate().equalsIgnoreCase(bottomSheetFilterLayout.getRoot().getResources().getString(R.string.dd_mm_yyyy)))
            for (Property property : filteredProperty) {
                //check if property don't match request
                if (formatStringToDate(property.getEnterOnMarket())
                        .before(formatStringToDate(bottomSheetFilterLayout.bottomSheetOnMarketFrom.getDate()))) {
                    filteredValues.add(property);
                }
            }
        //apply filter
        if (!filteredValues.isEmpty()) filteredProperty.removeAll(filteredValues);
        filterForRangeSlider();
    }

    private void filterForRangeSlider() {
        filteredValues = new ArrayList<>();
        for (Property property : filteredProperty) {
            filterForRangeSlider(bottomSheetFilterLayout.filterPropertyLocationPriceRangeSlider, property.getPrice(), property);
            filterForRangeSlider(bottomSheetFilterLayout.filterPropertyLocationSurfaceRangerSlider, property.getPropertyArea(), property);
            filterForRangeSlider(bottomSheetFilterLayout.filterPropertyLocationRoomRangerSlider, property.getNumberOfRooms(), property);
        }
        if (!filteredValues.isEmpty()) filteredProperty.removeAll(filteredValues);
    }

    //--------------------------------------LIST HELPERS--------------------------------------------------------

    public void updateAllPropertyPhotos(@NonNull List<PropertyPhoto> photosForProperty) {
        this.allPropertiesPhotos = photosForProperty;
    }

    public void updateAllPropertyPointOfInterest(@NonNull List<PointOfInterest> pointOfInterests) {
        this.allPointsOfInterest = pointOfInterests;
    }

    private void requestPointOfInterest() {
        String schoolStr = bottomSheetFilterLayout.getRoot().getResources().getString(R.string.school);
        String restaurantStr = bottomSheetFilterLayout.getRoot().getResources().getString(R.string.restaurant);
        String supermarketStr = bottomSheetFilterLayout.getRoot().getResources().getString(R.string.supermarket);
        requestPointsOfInterests = new ArrayList<>();

        CustomBottomSheetPointOfInterestLayoutBinding pointOfInterestBinding =
                bottomSheetFilterLayout.bottomSheetPointOfInterestInclude;

        if (pointOfInterestBinding.schoolCheckBox.checkBox.isChecked() && !requestPointsOfInterests.contains(schoolStr))
            requestPointsOfInterests.add(schoolStr);
        if (pointOfInterestBinding.restaurantCheckBox.checkBox.isChecked() && !requestPointsOfInterests.contains(restaurantStr))
            requestPointsOfInterests.add(restaurantStr);
        if (pointOfInterestBinding.supermarketCheckBox.checkBox.isChecked() && !requestPointsOfInterests.contains(supermarketStr))
            requestPointsOfInterests.add(supermarketStr);
    }

    private void requestPropertyType() {
        requestPropertyType = new ArrayList<>();
        String houseStr = bottomSheetFilterLayout.getRoot().getResources().getString(R.string.house);
        String flatStr = bottomSheetFilterLayout.getRoot().getResources().getString(R.string.flat);
        String duplexStr = bottomSheetFilterLayout.getRoot().getResources().getString(R.string.duplex);
        String penthouseStr = bottomSheetFilterLayout.getRoot().getResources().getString(R.string.penthouse);

        BottomSheetPropertyTypeLayoutBinding propertyTypeBinding = bottomSheetFilterLayout.bottomSheetPropertyTypeLayoutInclude;
        if (propertyTypeBinding.houseCheckBox.checkBox.isChecked() && !requestPropertyType.contains(houseStr))
            requestPropertyType.add(houseStr);
        if (propertyTypeBinding.flatCheckBox.checkBox.isChecked() && !requestPropertyType.contains(flatStr))
            requestPropertyType.add(flatStr);
        if (propertyTypeBinding.duplexCheckBox.checkBox.isChecked() && !requestPropertyType.contains(duplexStr))
            requestPropertyType.add(duplexStr);
        if (propertyTypeBinding.penthouseCheckBox.checkBox.isChecked() && !requestPropertyType.contains(penthouseStr))
            requestPropertyType.add(penthouseStr);
    }

    //--------------------------------------FILTER HELPERS--------------------------------------------------------

    private void filterForType() {
        filteredValues = new ArrayList<>();
        for (String requestPropertyType : requestPropertyType) {
            //check if property match request
            for (Property property : filteredProperty) {
                if (property.getPropertyType().equalsIgnoreCase(requestPropertyType) && !filteredValues.contains(property)) {
                    filteredValues.add(property);
                }
            }

        }
        //apply filter
        filteredProperty.clear();
        filteredProperty.addAll(filteredValues);
    }


    private void filterForRangeSlider(@NonNull CustomBottomSheetRangeSlider rangeSlider, float valueToCompare, Property property) {
        if (valueToCompare < rangeSlider.getStartValue()
                || valueToCompare > rangeSlider.getEndValue())
            filteredValues.add(property);
    }

    private void filterForPointOfInterestType() {
        for (int type = 0; type < requestPointsOfInterests.size(); type++) {
            if (filteredValues.isEmpty() && type == 0) filterForFirstType(type);
            else if (filteredValues.isEmpty() && type > 1) break;
            else filterIfNotFirstType(type);
        }
        filteredProperty.clear();
        filteredProperty.addAll(filteredValues);

    }

    private void filterForFirstType(int type) {
        for (Property property : filteredProperty)
            for (PointOfInterest pointOfInterest : allPointsOfInterest) {
                if (pointOfInterest.getType().contains(requestPointsOfInterests.get(type))
                        && property.getId() == pointOfInterest.getPropertyId()
                        && !filteredValues.contains(property)) {
                    filteredValues.add(property);
                }
            }
    }

    private void filterIfNotFirstType(int type) {
        for (Property property : filteredProperty) {
            boolean isFilteredPropertyHasRequestPointOfInterest = false;
            //for each property check if at least one point of interest match request type
            for (PointOfInterest pointOfInterest : allPointsOfInterest) {
                if (pointOfInterest.getType().contains(requestPointsOfInterests.get(type))
                        && property.getId() == pointOfInterest.getPropertyId())
                    //filtered values have values as it's not first type to apply filter
                    //if property is already known and match request it's ok
                    if (filteredValues.contains(property)) {
                        isFilteredPropertyHasRequestPointOfInterest = true;
                        break;
                    }
            }
            //else that means that property match first type but not this type, so remove from value
            if (!isFilteredPropertyHasRequestPointOfInterest)
                filteredValues.remove(property);
        }
    }

    public void resetFilter() {
        initRangeSliderValues();
        bottomSheetFilterLayout.bottomSheetPointOfInterestInclude.supermarketCheckBox.checkBox.setChecked(false);
        bottomSheetFilterLayout.bottomSheetPointOfInterestInclude.schoolCheckBox.checkBox.setChecked(false);
        bottomSheetFilterLayout.bottomSheetPointOfInterestInclude.restaurantCheckBox.checkBox.setChecked(false);
        bottomSheetFilterLayout.bottomSheetPropertyTypeLayoutInclude.duplexCheckBox.checkBox.setChecked(false);
        bottomSheetFilterLayout.bottomSheetPropertyTypeLayoutInclude.houseCheckBox.checkBox.setChecked(false);
        bottomSheetFilterLayout.bottomSheetPropertyTypeLayoutInclude.penthouseCheckBox.checkBox.setChecked(false);
        bottomSheetFilterLayout.bottomSheetPropertyTypeLayoutInclude.flatCheckBox.checkBox.setChecked(false);
        bottomSheetFilterLayout.bottomSheetOnMarketFrom.resetDate();
        bottomSheetFilterLayout.filterPropertyLocationSpinner.resetText();
        bottomSheetFilterLayout.filterPropertyNumberOfPhotoSpinner.resetText();
    }
}

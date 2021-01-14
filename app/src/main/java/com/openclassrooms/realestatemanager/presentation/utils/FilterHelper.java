package com.openclassrooms.realestatemanager.presentation.utils;

import android.util.Log;
import android.widget.CheckBox;

import androidx.annotation.NonNull;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding;
import com.openclassrooms.realestatemanager.databinding.BottomSheetPropertyTypeLayoutBinding;
import com.openclassrooms.realestatemanager.databinding.CustomBottomSheetPointOfInterestLayoutBinding;
import com.openclassrooms.realestatemanager.databinding.CustomBottomSheetRangeSliderBinding;
import com.openclassrooms.realestatemanager.presentation.utils.customView.CustomBottomSheetRangeSlider;
import com.picone.core.domain.entity.PointOfInterest;
import com.picone.core.domain.entity.Property;
import com.picone.core.domain.entity.PropertyPhoto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.openclassrooms.realestatemanager.presentation.utils.Utils.formatStringToDate;

public class FilterHelper {

    private ActivityMainBinding mainBinding;

    private List<String> requestPointOfInterest;
    private List<String> requestPropertyType;
    private List<PropertyPhoto> allPropertiesPhotos = new ArrayList<>();
    private List<PointOfInterest> allPointsOfInterest = new ArrayList<>();
    private List<Property> filteredValues;
    private int iterator = 0;
    private List<Property> filteredProperty;


    public FilterHelper(ActivityMainBinding mainBinding) {
        this.mainBinding = mainBinding;
        List<String> numberOfPhotos = new ArrayList<>();
        for (int i = 1; i <= 10; i++) numberOfPhotos.add(String.valueOf(i));
        mainBinding.bottomSheetLayout
                .filterPropertyNumberOfPhotoSpinner.setSpinnerAdapter(numberOfPhotos);

    }

    public void updateAllPropertyPhotos(@NonNull List<PropertyPhoto> photosForProperty) {
        if (allPropertiesPhotos.isEmpty()) allPropertiesPhotos.addAll(photosForProperty);
        else if (!allPropertiesPhotos.containsAll(photosForProperty))
            allPropertiesPhotos.addAll(photosForProperty);
    }

    public void updateAllPropertyPointOfInterest(@NonNull List<PointOfInterest> pointOfInterests) {
        if (!pointOfInterests.isEmpty())
            if (allPointsOfInterest.isEmpty()) allPointsOfInterest.addAll(pointOfInterests);
            else if (!allPointsOfInterest.containsAll(pointOfInterests))
                allPointsOfInterest.addAll(pointOfInterests);
    }

    public void requestPointOfInterest() {
        String schoolStr = mainBinding.getRoot().getResources().getString(R.string.school);
        String restaurantStr = mainBinding.getRoot().getResources().getString(R.string.restaurant);
        String supermarketStr = mainBinding.getRoot().getResources().getString(R.string.supermarket);
        requestPointOfInterest = new ArrayList<>();

        CustomBottomSheetPointOfInterestLayoutBinding pointOfInterestBinding =
                mainBinding.bottomSheetLayout.bottomSheetPointOfInterestInclude;

        if (pointOfInterestBinding.schoolCheckBox.checkBox.isChecked() && !requestPointOfInterest.contains(schoolStr))
            requestPointOfInterest.add(schoolStr);
        else if (!pointOfInterestBinding.schoolCheckBox.checkBox.isChecked())
            requestPointOfInterest.remove(schoolStr);
        if (pointOfInterestBinding.restaurantCheckBox.checkBox.isChecked() && !requestPointOfInterest.contains(restaurantStr))
            requestPointOfInterest.add(restaurantStr);
        else if (!pointOfInterestBinding.restaurantCheckBox.checkBox.isChecked())
            requestPointOfInterest.remove(restaurantStr);
        if (pointOfInterestBinding.supermarketCheckBox.checkBox.isChecked() && !requestPointOfInterest.contains(supermarketStr))
            requestPointOfInterest.add(supermarketStr);
        else if (!pointOfInterestBinding.supermarketCheckBox.checkBox.isChecked())
            requestPointOfInterest.remove(supermarketStr);
    }

    public void requestPropertyType() {
        requestPropertyType = new ArrayList<>();
        String houseStr = mainBinding.getRoot().getResources().getString(R.string.house);
        String flatStr = mainBinding.getRoot().getResources().getString(R.string.flat);
        String duplexStr = mainBinding.getRoot().getResources().getString(R.string.duplex);
        String penthouseStr = mainBinding.getRoot().getResources().getString(R.string.penthouse);

        BottomSheetPropertyTypeLayoutBinding propertyTypeBinding = mainBinding.bottomSheetLayout.bottomSheetPropertyTypeLayoutInclude;
        if (propertyTypeBinding.houseCheckBox.checkBox.isChecked() && !requestPropertyType.contains(houseStr))
            requestPropertyType.add(houseStr);
        else if (!propertyTypeBinding.houseCheckBox.checkBox.isChecked())
            requestPropertyType.remove(houseStr);
        if (propertyTypeBinding.flatCheckBox.checkBox.isChecked() && !requestPropertyType.contains(flatStr))
            requestPropertyType.add(flatStr);
        else if (!propertyTypeBinding.flatCheckBox.checkBox.isChecked())
            requestPropertyType.remove(flatStr);
        if (propertyTypeBinding.duplexCheckBox.checkBox.isChecked() && !requestPropertyType.contains(duplexStr))
            requestPropertyType.add(duplexStr);
        else if (!propertyTypeBinding.duplexCheckBox.checkBox.isChecked())
            requestPropertyType.remove(duplexStr);
        if (propertyTypeBinding.penthouseCheckBox.checkBox.isChecked() && !requestPropertyType.contains(penthouseStr))
            requestPropertyType.add(penthouseStr);
        else if (!propertyTypeBinding.penthouseCheckBox.checkBox.isChecked())
            requestPropertyType.remove(penthouseStr);

    }

    public void initRangeSliderValues(@NonNull List<Property> allProperties) {

        List<Float> allPrices = new ArrayList<>();
        List<Float> allSurfaces = new ArrayList<>();
        List<Float> allRooms = new ArrayList<>();

        for (Property property : allProperties) {
            allPrices.add((float) property.getPrice());
            allSurfaces.add((float) property.getPropertyArea());
            allRooms.add((float) property.getNumberOfRooms());
        }
        mainBinding.bottomSheetLayout.filterPropertyLocationPriceRangeSlider.setRangeSliderTouchListener();
        mainBinding.bottomSheetLayout.filterPropertyLocationPriceRangeSlider
                .setRangeSliderValue(
                        Collections.min(allPrices)
                        , Collections.max(allPrices)
                        , Math.round((Collections.max(allPrices) - Collections.min(allPrices)) / 10000));

        mainBinding.bottomSheetLayout.filterPropertyLocationSurfaceRangerSlider.setRangeSliderTouchListener();
        mainBinding.bottomSheetLayout.filterPropertyLocationSurfaceRangerSlider
                .setRangeSliderValue(
                        Collections.min(allSurfaces),
                        Collections.max(allSurfaces),
                        Math.round((Collections.max(allSurfaces) - Collections.min(allSurfaces)) / 10));

        mainBinding.bottomSheetLayout.filterPropertyLocationRoomRangerSlider.setRangeSliderTouchListener();
        mainBinding.bottomSheetLayout.filterPropertyLocationRoomRangerSlider
                .setRangeSliderValue(
                        Collections.min(allRooms),
                        Collections.max(allRooms),
                        1);
    }

    public void filterProperties(List<Property> allProperties) {
        requestPointOfInterest();
        requestPropertyType();
        filterForLocation(allProperties);
        filteredValues = new ArrayList<>();
        iterator = 0;
        isAtLeastOnePropertyMatchRequestType = true;
    }


    private void filterForLocation(List<Property> allProperties) {
        filteredProperty = new ArrayList<>();
        if (!mainBinding.bottomSheetLayout.filterPropertyLocationSpinner.getText().trim().isEmpty()) {
            for (Property property : allProperties) {
                if (property.getRegion().equalsIgnoreCase(mainBinding.bottomSheetLayout.filterPropertyLocationSpinner.getText()))
                    filteredProperty.add(property);
            }
        }
        if (filteredProperty.isEmpty()) filteredProperty.addAll(allProperties);
        filterForNumberOfPhoto();
    }


    private void filterForNumberOfPhoto() {
        filteredValues = new ArrayList<>();
        if (!mainBinding.bottomSheetLayout.filterPropertyNumberOfPhotoSpinner.getText().trim().isEmpty())
            for (Property property : filteredProperty) {
                int i = 0;
                for (PropertyPhoto propertyPhoto : allPropertiesPhotos) {
                    if (property.getId() == propertyPhoto.getPropertyId()) {
                        i++;
                    }
                }
                if (i < Integer.parseInt(mainBinding.bottomSheetLayout.filterPropertyNumberOfPhotoSpinner.getText()))
                    filteredValues.add(property);
            }
        if (!filteredValues.isEmpty()) filteredProperty.removeAll(filteredValues);
        filterForPointOfInterest();
    }


    private void filterForPointOfInterest() {
        filteredValues = new ArrayList<>();
        if (mainBinding.bottomSheetLayout.bottomSheetPointOfInterestInclude.schoolCheckBox.checkBox.isChecked()) {
            filterForPointOfInterestType(mainBinding.getRoot().getResources().getString(R.string.school));
        }
        if (mainBinding.bottomSheetLayout.bottomSheetPointOfInterestInclude.restaurantCheckBox.checkBox.isChecked()) {
            filterForPointOfInterestType(mainBinding.getRoot().getResources().getString(R.string.restaurant));
        }
        if (mainBinding.bottomSheetLayout.bottomSheetPointOfInterestInclude.supermarketCheckBox.checkBox.isChecked()) {
            filterForPointOfInterestType(mainBinding.getRoot().getResources().getString(R.string.supermarket));
        }
        filterForPropertyType();
        iterator = 0;
    }

    private boolean isAtLeastOnePropertyMatchRequestType = true;

    private void filterForType() {
        if (isAtLeastOnePropertyMatchRequestType)
            for (String requestPointOfInterest : requestPropertyType) {
                isAtLeastOnePropertyMatchRequestType = false;
                for (Property property : filteredProperty)
                    if (property.getPropertyType().equalsIgnoreCase(requestPointOfInterest) && !filteredValues.contains(property)) {
                        isAtLeastOnePropertyMatchRequestType = true;
                        filteredValues.add(property);
                    }
                if (!isAtLeastOnePropertyMatchRequestType) {
                    filteredValues.clear();
                    break;
                }
            }
        if (!filteredValues.isEmpty()) {
            List<Property> propertiesToDelete = new ArrayList<>();
            for (Property property : filteredProperty) {
                boolean bol = false;
                for (Property property1 : filteredValues) {
                    if (property1.getId() == property.getId()) {
                        bol = true;
                        break;
                    }
                }
                if (!bol)
                    propertiesToDelete.add(property);
            }
            filteredProperty.removeAll(propertiesToDelete);
        } else filteredProperty.clear();
    }

    private void filterForPropertyType() {
        filteredValues = new ArrayList<>();
        if (mainBinding.bottomSheetLayout.bottomSheetPropertyTypeLayoutInclude.houseCheckBox.checkBox.isChecked()
                || mainBinding.bottomSheetLayout.bottomSheetPropertyTypeLayoutInclude.penthouseCheckBox.checkBox.isChecked()
                || mainBinding.bottomSheetLayout.bottomSheetPropertyTypeLayoutInclude.flatCheckBox.checkBox.isChecked()
                || mainBinding.bottomSheetLayout.bottomSheetPropertyTypeLayoutInclude.duplexCheckBox.checkBox.isChecked())
            filterForType();
        filterForOnMarketFrom();
    }

    private void filterForOnMarketFrom() {
        filteredValues = new ArrayList<>();
        if (!mainBinding.bottomSheetLayout.bottomSheetOnMarketFrom.getDate().equalsIgnoreCase(mainBinding.getRoot().getResources().getString(R.string.dd_mm_yyyy))) {
            for (Property property : filteredProperty) {
                if (formatStringToDate(property.getEnterOnMarket())
                        .before(formatStringToDate(mainBinding.bottomSheetLayout.bottomSheetOnMarketFrom.getDate()))) {
                    filteredValues.add(property);
                }
            }
            if (!filteredValues.isEmpty()) filteredProperty.removeAll(filteredValues);
        }
        filteredValues = new ArrayList<>();
        for (Property property : filteredProperty) {
            filterForRangeSlider(mainBinding.bottomSheetLayout.filterPropertyLocationPriceRangeSlider,property.getPrice(),property );
            filterForRangeSlider(mainBinding.bottomSheetLayout.filterPropertyLocationSurfaceRangerSlider,property.getPropertyArea(),property);
            filterForRangeSlider(mainBinding.bottomSheetLayout.filterPropertyLocationRoomRangerSlider,property.getNumberOfRooms(),property);
        }
        if (!filteredValues.isEmpty()) filteredProperty.removeAll(filteredValues);
    }

    private void filterForRangeSlider(@NonNull CustomBottomSheetRangeSlider rangeSlider, float valueToCompare, Property property) {
        if (valueToCompare < rangeSlider.getStartValue()
                || valueToCompare > rangeSlider.getEndValue())
            filteredValues.add(property);
    }

    private void filterForPointOfInterestType(CharSequence pointOfInterestType) {
        iterator++;
        if (filteredValues.isEmpty() && iterator == 1) {
            for (PointOfInterest pointOfInterest : allPointsOfInterest) {
                if (pointOfInterest.getType().contains(pointOfInterestType))
                    for (Property property : filteredProperty)
                        if (property.getId() == pointOfInterest.getPropertyId())
                            if (filteredValues.isEmpty() || !filteredValues.contains(property))
                                filteredValues.add(property);
            }
        } else {
            for (Property property : filteredProperty) {
                boolean isFilteredPropertyHasRequestPointOfInterest = false;
                for (PointOfInterest pointOfInterest : allPointsOfInterest) {
                    if (pointOfInterest.getType().contains(pointOfInterestType)
                            && property.getId() == pointOfInterest.getPropertyId()) {
                        if (filteredValues.contains(property))
                            isFilteredPropertyHasRequestPointOfInterest = true;
                        break;
                    }
                }
                if (!isFilteredPropertyHasRequestPointOfInterest) filteredValues.remove(property);
            }
        }

        filteredProperty.clear();
        filteredProperty.addAll(filteredValues);
    }


}

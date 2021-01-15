package com.openclassrooms.realestatemanager.presentation.utils;

import android.util.Log;

import androidx.annotation.NonNull;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding;
import com.openclassrooms.realestatemanager.databinding.BottomSheetFilterLayoutBinding;
import com.openclassrooms.realestatemanager.databinding.BottomSheetPropertyTypeLayoutBinding;
import com.openclassrooms.realestatemanager.databinding.CustomBottomSheetPointOfInterestLayoutBinding;
import com.openclassrooms.realestatemanager.presentation.utils.customView.CustomBottomSheetRangeSlider;
import com.picone.core.domain.entity.PointOfInterest;
import com.picone.core.domain.entity.Property;
import com.picone.core.domain.entity.PropertyPhoto;

import java.util.ArrayList;
import java.util.List;

import static com.openclassrooms.realestatemanager.presentation.utils.Utils.formatStringToDate;

public class FilterHelper {

    private ActivityMainBinding mainBinding;

    private List<String> requestPointsOfInterests;
    private List<String> requestPropertyType;
    private List<PropertyPhoto> allPropertiesPhotos = new ArrayList<>();
    private List<PointOfInterest> allPointsOfInterest = new ArrayList<>();
    private List<Property> filteredValues;
    private List<Property> filteredProperty;
    private boolean isAtLeastOnePropertyMatchRequestType = true;



    public List<Property> getFilteredProperty() {
        return filteredProperty;
    }

    public FilterHelper(ActivityMainBinding mainBinding) {
        this.mainBinding = mainBinding;
        List<String> numberOfPhotos = new ArrayList<>();
        for (int i = 1; i <= 10; i++) numberOfPhotos.add(String.valueOf(i));
        mainBinding.bottomSheetLayout
                .filterPropertyNumberOfPhotoSpinner.setSpinnerAdapter(numberOfPhotos);
        initRangeSliderValues();
    }

    private void initRangeSliderValues() {
        mainBinding.bottomSheetLayout.filterPropertyLocationPriceRangeSlider.setRangeSliderTouchListener();
        mainBinding.bottomSheetLayout.filterPropertyLocationPriceRangeSlider
                .setRangeSliderValue(
                        100000
                        , 1000000
                        , (float) (1000000 - 100000) / 10000);

        mainBinding.bottomSheetLayout.filterPropertyLocationSurfaceRangerSlider.setRangeSliderTouchListener();
        mainBinding.bottomSheetLayout.filterPropertyLocationSurfaceRangerSlider
                .setRangeSliderValue(
                        100,
                        1000,
                        (float) (1000 - 100) / 10);

        mainBinding.bottomSheetLayout.filterPropertyLocationRoomRangerSlider.setRangeSliderTouchListener();
        mainBinding.bottomSheetLayout.filterPropertyLocationRoomRangerSlider
                .setRangeSliderValue(
                        5,
                        20,
                        1);
    }

    //------------------------------------------FILTER--------------------------------------------------------

    public void filterProperties(List<Property> allProperties) {
        requestPointOfInterest();
        requestPropertyType();
        filterForLocation(allProperties);
        filteredValues = new ArrayList<>();
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
        Log.i("TAG", "filterForLocation: " + filteredProperty);
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
        Log.i("TAG", "filterForLocation: photo " + filteredProperty);
        if (!filteredValues.isEmpty()) filteredProperty.removeAll(filteredValues);
        filterForPointOfInterest();
    }

    private void filterForPointOfInterest() {
        filteredValues = new ArrayList<>();
        if (mainBinding.bottomSheetLayout.bottomSheetPointOfInterestInclude.schoolCheckBox.checkBox.isChecked()
                || mainBinding.bottomSheetLayout.bottomSheetPointOfInterestInclude.restaurantCheckBox.checkBox.isChecked()
                || mainBinding.bottomSheetLayout.bottomSheetPointOfInterestInclude.supermarketCheckBox.checkBox.isChecked()) {
            filterForPointOfInterestType();
        }
        Log.i("TAG", "filterForLocation: poi " + filteredProperty);
        filterForPropertyType();
    }

    private void filterForPropertyType() {
        filteredValues = new ArrayList<>();
        if (mainBinding.bottomSheetLayout.bottomSheetPropertyTypeLayoutInclude.houseCheckBox.checkBox.isChecked()
                || mainBinding.bottomSheetLayout.bottomSheetPropertyTypeLayoutInclude.penthouseCheckBox.checkBox.isChecked()
                || mainBinding.bottomSheetLayout.bottomSheetPropertyTypeLayoutInclude.flatCheckBox.checkBox.isChecked()
                || mainBinding.bottomSheetLayout.bottomSheetPropertyTypeLayoutInclude.duplexCheckBox.checkBox.isChecked())
            filterForType();

        Log.i("TAG", "filterForLocation: type " + filteredProperty);
        filterForOnMarketFrom();
    }

    private void filterForOnMarketFrom() {
        filteredValues = new ArrayList<>();
        if (!mainBinding.bottomSheetLayout.bottomSheetOnMarketFrom.getDate().equalsIgnoreCase(mainBinding.getRoot().getResources().getString(R.string.dd_mm_yyyy)))
            for (Property property : filteredProperty) {
                if (formatStringToDate(property.getEnterOnMarket())
                        .before(formatStringToDate(mainBinding.bottomSheetLayout.bottomSheetOnMarketFrom.getDate()))) {
                    filteredValues.add(property);
                }
            }
        Log.i("TAG", "filterForLocation:onMarket " + filteredProperty);
        if (!filteredValues.isEmpty()) filteredProperty.removeAll(filteredValues);
        filterForRangeSlider();
    }

    private void filterForRangeSlider() {
        filteredValues = new ArrayList<>();
        for (Property property : filteredProperty) {
            filterForRangeSlider(mainBinding.bottomSheetLayout.filterPropertyLocationPriceRangeSlider, property.getPrice(), property);
            filterForRangeSlider(mainBinding.bottomSheetLayout.filterPropertyLocationSurfaceRangerSlider, property.getPropertyArea(), property);
            filterForRangeSlider(mainBinding.bottomSheetLayout.filterPropertyLocationRoomRangerSlider, property.getNumberOfRooms(), property);
        }
        Log.i("TAG", "filterForLocation:rangeSlider " + filteredProperty);
        if (!filteredValues.isEmpty()) filteredProperty.removeAll(filteredValues);
    }

    //--------------------------------------LIST HELPERS--------------------------------------------------------

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
    private void requestPointOfInterest() {
        String schoolStr = mainBinding.getRoot().getResources().getString(R.string.school);
        String restaurantStr = mainBinding.getRoot().getResources().getString(R.string.restaurant);
        String supermarketStr = mainBinding.getRoot().getResources().getString(R.string.supermarket);
        requestPointsOfInterests = new ArrayList<>();

        CustomBottomSheetPointOfInterestLayoutBinding pointOfInterestBinding =
                mainBinding.bottomSheetLayout.bottomSheetPointOfInterestInclude;

        if (pointOfInterestBinding.schoolCheckBox.checkBox.isChecked() && !requestPointsOfInterests.contains(schoolStr))
            requestPointsOfInterests.add(schoolStr);
        else if (!pointOfInterestBinding.schoolCheckBox.checkBox.isChecked())
            requestPointsOfInterests.remove(schoolStr);
        if (pointOfInterestBinding.restaurantCheckBox.checkBox.isChecked() && !requestPointsOfInterests.contains(restaurantStr))
            requestPointsOfInterests.add(restaurantStr);
        else if (!pointOfInterestBinding.restaurantCheckBox.checkBox.isChecked())
            requestPointsOfInterests.remove(restaurantStr);
        if (pointOfInterestBinding.supermarketCheckBox.checkBox.isChecked() && !requestPointsOfInterests.contains(supermarketStr))
            requestPointsOfInterests.add(supermarketStr);
        else if (!pointOfInterestBinding.supermarketCheckBox.checkBox.isChecked())
            requestPointsOfInterests.remove(supermarketStr);
    }

    private void requestPropertyType() {
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

    //--------------------------------------FILTER HELPERS--------------------------------------------------------

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


    private void filterForRangeSlider(@NonNull CustomBottomSheetRangeSlider rangeSlider, float valueToCompare, Property property) {
        if (valueToCompare < rangeSlider.getStartValue()
                || valueToCompare > rangeSlider.getEndValue())
            filteredValues.add(property);
    }

    private void filterForPointOfInterestType() {
        for (int i = 0; i < requestPointsOfInterests.size(); i++) {
            if (filteredValues.isEmpty() && i == 0) {
                for (PointOfInterest pointOfInterest : allPointsOfInterest) {
                    if (pointOfInterest.getType().contains(requestPointsOfInterests.get(i)))
                        for (Property property : filteredProperty)
                            if (property.getId() == pointOfInterest.getPropertyId())
                                if (!filteredValues.contains(property)) {
                                    filteredValues.add(property);
                                }
                }
            } else if (filteredValues.isEmpty() && i > 1) {
                break;
            } else {
                for (Property property : filteredProperty) {
                    boolean isFilteredPropertyHasRequestPointOfInterest = false;
                    for (PointOfInterest pointOfInterest : allPointsOfInterest) {
                        if (pointOfInterest.getType().contains(requestPointsOfInterests.get(i))
                                && property.getId() == pointOfInterest.getPropertyId())
                            if (filteredValues.contains(property)) {
                                isFilteredPropertyHasRequestPointOfInterest = true;
                                break;
                            }
                    }
                    if (!isFilteredPropertyHasRequestPointOfInterest)
                        filteredValues.remove(property);
                }
            }
            filteredProperty.clear();
            filteredProperty.addAll(filteredValues);
        }
    }

    public void resetFilter() {
        BottomSheetFilterLayoutBinding bottomSheet = mainBinding.bottomSheetLayout;
        initRangeSliderValues();
        bottomSheet.bottomSheetPointOfInterestInclude.supermarketCheckBox.checkBox.setChecked(false);
        bottomSheet.bottomSheetPointOfInterestInclude.schoolCheckBox.checkBox.setChecked(false);
        bottomSheet.bottomSheetPointOfInterestInclude.restaurantCheckBox.checkBox.setChecked(false);
        bottomSheet.bottomSheetPropertyTypeLayoutInclude.duplexCheckBox.checkBox.setChecked(false);
        bottomSheet.bottomSheetPropertyTypeLayoutInclude.houseCheckBox.checkBox.setChecked(false);
        bottomSheet.bottomSheetPropertyTypeLayoutInclude.penthouseCheckBox.checkBox.setChecked(false);
        bottomSheet.bottomSheetPropertyTypeLayoutInclude.flatCheckBox.checkBox.setChecked(false);
        bottomSheet.bottomSheetOnMarketFrom.resetDate();
        bottomSheet.filterPropertyLocationSpinner.resetText();
        bottomSheet.filterPropertyNumberOfPhotoSpinner.resetText();
    }
}

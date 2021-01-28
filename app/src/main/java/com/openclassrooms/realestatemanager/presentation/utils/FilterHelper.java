package com.openclassrooms.realestatemanager.presentation.utils;

import androidx.annotation.NonNull;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.BottomSheetFilterLayoutBinding;
import com.openclassrooms.realestatemanager.databinding.BottomSheetPropertyTypeLayoutBinding;
import com.openclassrooms.realestatemanager.databinding.CustomBottomSheetPointOfInterestLayoutBinding;
import com.openclassrooms.realestatemanager.presentation.utils.customView.CustomBottomSheetRangeSlider;
import com.picone.core.domain.entity.PointOfInterest;
import com.picone.core.domain.entity.Property;
import com.picone.core.domain.entity.PropertyInformation;
import com.picone.core.domain.entity.PropertyPhoto;

import java.util.ArrayList;
import java.util.List;

import static com.openclassrooms.realestatemanager.presentation.utils.Utils.formatStringToDate;

public class FilterHelper {

    private BottomSheetFilterLayoutBinding bottomSheetFilterLayout;

    private List<String> requestPointsOfInterests;
    private List<String> requestPropertyType;
    private List<Property> propertiesTempValue;
    private List<Property> filteredPropertyInformation;


    public List<Property> getFilteredPropertyInformation() {
        return filteredPropertyInformation;
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
    }

    private void filterForLocation(List<Property> allProperties) {
        filteredPropertyInformation = new ArrayList<>();
        if (!bottomSheetFilterLayout.filterPropertyLocationSpinner.getText().trim().isEmpty()) {
            for (Property property : allProperties) {
                if (property.propertyLocation.getRegion().equalsIgnoreCase(bottomSheetFilterLayout.filterPropertyLocationSpinner.getText()))
                    filteredPropertyInformation.add(property);
            }
        }
        if (filteredPropertyInformation.isEmpty()) filteredPropertyInformation.addAll(allProperties);
        filterForNumberOfPhoto();
    }

    private void filterForNumberOfPhoto() {
        propertiesTempValue = new ArrayList<>();
        if (!bottomSheetFilterLayout.filterPropertyNumberOfPhotoSpinner.getText().trim().isEmpty())
            // check property that don't match request
            for (Property property : filteredPropertyInformation) {
                if (property.photos.size() < Integer.parseInt(bottomSheetFilterLayout.filterPropertyNumberOfPhotoSpinner.getText()))
                    propertiesTempValue.add(property);
            }
        //apply filter
        if (!propertiesTempValue.isEmpty()) filteredPropertyInformation.removeAll(propertiesTempValue);
        filterForPointOfInterest();
    }

    private void filterForPointOfInterest() {
        propertiesTempValue = new ArrayList<>();
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
        propertiesTempValue = new ArrayList<>();
        if (!bottomSheetFilterLayout.bottomSheetOnMarketFrom.getDate().equalsIgnoreCase(bottomSheetFilterLayout.getRoot().getResources().getString(R.string.dd_mm_yyyy)))
            for (Property property : filteredPropertyInformation) {
                //check if property don't match request
                if (formatStringToDate(property.propertyInformation.getEnterOnMarket())
                        .before(formatStringToDate(bottomSheetFilterLayout.bottomSheetOnMarketFrom.getDate()))) {
                    propertiesTempValue.add(property);
                }
            }
        //apply filter
        if (!propertiesTempValue.isEmpty()) filteredPropertyInformation.removeAll(propertiesTempValue);
        filterForRangeSlider();
    }

    private void filterForRangeSlider() {
        propertiesTempValue = new ArrayList<>();
        for (Property property : filteredPropertyInformation) {
            filterForRangeSlider(bottomSheetFilterLayout.filterPropertyLocationPriceRangeSlider, property.propertyInformation.getPrice(), property);
            filterForRangeSlider(bottomSheetFilterLayout.filterPropertyLocationSurfaceRangerSlider, property.propertyInformation.getPropertyArea(), property);
            filterForRangeSlider(bottomSheetFilterLayout.filterPropertyLocationRoomRangerSlider, property.propertyInformation.getNumberOfRooms(), property);
        }
        if (!propertiesTempValue.isEmpty()) filteredPropertyInformation.removeAll(propertiesTempValue);
    }

    //--------------------------------------LIST HELPERS--------------------------------------------------------

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
        propertiesTempValue = new ArrayList<>();
        for (String requestPropertyType : requestPropertyType) {
            //check if property match request
            for (Property property : filteredPropertyInformation) {
                if (property.propertyInformation.getPropertyType().equalsIgnoreCase(requestPropertyType) && !propertiesTempValue.contains(property)) {
                    propertiesTempValue.add(property);
                }
            }

        }
        //apply filter
        filteredPropertyInformation.clear();
        filteredPropertyInformation.addAll(propertiesTempValue);
    }


    private void filterForRangeSlider(@NonNull CustomBottomSheetRangeSlider rangeSlider, float valueToCompare, Property property) {
        if (valueToCompare < rangeSlider.getStartValue()
                || valueToCompare > rangeSlider.getEndValue())
            propertiesTempValue.add(property);
    }

    private void filterForPointOfInterestType() {
        for (int type = 0; type < requestPointsOfInterests.size(); type++) {
            // if no value in tempValue and on 1st request type
            if (propertiesTempValue.isEmpty() && type == 0) filterForFirstType(type);
            // if no value in temp value and not first type, means that no property match first type, so break to return empty list
            else if (propertiesTempValue.isEmpty() && type > 1) break;
            //else apply filter for following type
            else filterIfNotFirstType(type);
        }
        //apply final filtered value
        filteredPropertyInformation.clear();
        filteredPropertyInformation.addAll(propertiesTempValue);

    }

    private void filterForFirstType(int type) {
        //for first Point of interest type, add to value if match
        for (Property property : filteredPropertyInformation)
            for (PointOfInterest pointOfInterest : property.pointOfInterests) {
                if (pointOfInterest.getType().contains(requestPointsOfInterests.get(type))
                        && !propertiesTempValue.contains(property)) {
                    propertiesTempValue.add(property);
                }
            }
    }

    private void filterIfNotFirstType(int type) {
        for (Property property : filteredPropertyInformation) {
            boolean isFilteredPropertyHasRequestPointOfInterest = false;
            //for each property in filtered list check if at least one point of interest match request type
            for (PointOfInterest pointOfInterest : property.pointOfInterests) {
                if (pointOfInterest.getType().contains(requestPointsOfInterests.get(type))
                && propertiesTempValue.contains(property)) {
                    //temp value got properties that match past point of interest
                    //if match actual type and is in list, means that match all request type
                        isFilteredPropertyHasRequestPointOfInterest = true;
                        break;
                    }
            }
            //else that means that property match first type but not actual type, so remove from value
            if (!isFilteredPropertyHasRequestPointOfInterest)
                propertiesTempValue.remove(property);
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

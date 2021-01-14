package com.openclassrooms.realestatemanager.presentation.utils;

import android.util.Log;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding;
import com.openclassrooms.realestatemanager.databinding.BottomSheetPropertyTypeLayoutBinding;
import com.openclassrooms.realestatemanager.databinding.CustomBottomSheetPointOfInterestLayoutBinding;
import com.picone.core.domain.entity.PointOfInterest;
import com.picone.core.domain.entity.Property;
import com.picone.core.domain.entity.PropertyPhoto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class FilterHelper {

    private ActivityMainBinding mainBinding;

    private List<String> requestPointOfInterest;
    private List<String> requestPropertyType;
    private List<PropertyPhoto> allPropertiesPhotos = new ArrayList<>();
    private List<PointOfInterest> allPointsOfInterest = new ArrayList<>();


    public FilterHelper(ActivityMainBinding mainBinding) {
        this.mainBinding = mainBinding;
        List<String> numberOfPhotos = new ArrayList<>();
        for (int i = 1; i <= 9; i++) numberOfPhotos.add(String.valueOf(i));
        numberOfPhotos.add("10+");
        mainBinding.bottomSheetLayout
                .filterPropertyNumberOfPhotoSpinner.setSpinnerAdapter(numberOfPhotos);

    }

    public void updateAllPropertyPhotos(List<PropertyPhoto> photosForProperty) {
        if (allPropertiesPhotos.isEmpty()) allPropertiesPhotos.addAll(photosForProperty);
        else if (!allPropertiesPhotos.containsAll(photosForProperty))
            allPropertiesPhotos.addAll(photosForProperty);
    }

    public void updateAllPropertyPointOfInterest(List<PointOfInterest> pointOfInterests) {
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

    public void initRangeSliderValues(List<Property> allProperties) {

        List<Float> allPrices = new ArrayList<>();
        List<Float> allSurfaces = new ArrayList<>();
        List<Float> allRooms = new ArrayList<>();

        for (Property property : allProperties) {
            allPrices.add((float) property.getPrice());
            allSurfaces.add((float) property.getPropertyArea());
            allRooms.add((float) property.getNumberOfRooms());
        }
        mainBinding.bottomSheetLayout.filterPropertyLocationPriceRangerSlider.setRangeSliderTouchListener();
        mainBinding.bottomSheetLayout.filterPropertyLocationPriceRangerSlider
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

        if (requestPropertyType != null && !requestPropertyType.isEmpty())
            Log.i("TAG", "filterProperties: know type" + requestPropertyType);
        if (!mainBinding.bottomSheetLayout.bottomSheetOnMarketFrom.getDate().equalsIgnoreCase(mainBinding.getRoot().getResources().getString(R.string.dd_mm_yyyy)))
            Log.i("TAG", "filterProperties: know on market from" + mainBinding.bottomSheetLayout.bottomSheetOnMarketFrom.getDate());

    }

    private void filterForLocation(List<Property> allProperties) {
        List<Property> filteredProperty = new ArrayList<>();
        if (!mainBinding.bottomSheetLayout.filterPropertyLocationSpinner.getText().trim().isEmpty()) {
            for (Property property : allProperties) {
                if (property.getRegion().equalsIgnoreCase(mainBinding.bottomSheetLayout.filterPropertyLocationSpinner.getText()))
                    filteredProperty.add(property);
            }
        }
        if (filteredProperty.isEmpty()) filteredProperty.addAll(allProperties);
        filterForNumberOfPhoto(filteredProperty);
    }

    private List<Property> filteredValues;

    private void filterForNumberOfPhoto(List<Property> filteredProperty) {
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
        filterForPointOfInterest(filteredProperty);

        Log.i("TAG", "filterProperties: know number of photo " + filteredProperty.size());

    }

    private void filterForSchool(List<Property> filteredProperty){
        for (String requestPointOfInterest: requestPointOfInterest){
            if (requestPointOfInterest.equalsIgnoreCase(mainBinding.getRoot().getResources().getString(R.string.school)))
                Log.d("TAG", "filterForSchool: ");
        }
    }

    //TODO filter for each categorie one to one
    private void filterForPointOfInterest(List<Property> filteredProperty) {
        filteredValues = new ArrayList<>();
        if (requestPointOfInterest != null && !requestPointOfInterest.isEmpty())
            for (Property property : filteredProperty) {
                List<PointOfInterest> pointOfInterestsForProperty = new ArrayList<>();
                for (PointOfInterest pointOfInterest : allPointsOfInterest) {
                    if (property.getId() == pointOfInterest.getPropertyId()) {
                        boolean isAlreadyKnown = false;
                        for (PointOfInterest pointOfInterest1 : pointOfInterestsForProperty) {
                            if (pointOfInterest1.getName().equalsIgnoreCase(pointOfInterest.getName())) {
                                isAlreadyKnown = true;
                            }
                        }
                        if (!isAlreadyKnown)
                            pointOfInterestsForProperty.add(pointOfInterest);

                    }
                }
                //poi ok
                int i = 0;
                List<String> knowRequestPointOfInterestForProperty = new ArrayList<>();
                for (String requestPointOfInterest : requestPointOfInterest) {
                    for (PointOfInterest pointOfInterestForProperty : pointOfInterestsForProperty) {
                        if (pointOfInterestForProperty.getType().equalsIgnoreCase(requestPointOfInterest)) {
                            boolean isPointOfInterestTypeAlreadyKnown = false;
                            if (knowRequestPointOfInterestForProperty.isEmpty())
                                knowRequestPointOfInterestForProperty.add(pointOfInterestForProperty.getType());
                            else for (String kPOI : knowRequestPointOfInterestForProperty) {
                                Log.i("TAG", "filterForPointOfInterest: "+kPOI+" "+pointOfInterestForProperty.getType());
                                if (pointOfInterestForProperty.getType().equalsIgnoreCase(kPOI))
                                    isPointOfInterestTypeAlreadyKnown = true;
                            }
                            if (!isPointOfInterestTypeAlreadyKnown)
                                knowRequestPointOfInterestForProperty.add(pointOfInterestForProperty.getType());

                        }
                    }
                    Log.e("TAG", "filterForPointOfInterest: " + knowRequestPointOfInterestForProperty.size()+" "+property.getId());
                }
            }


    }


}

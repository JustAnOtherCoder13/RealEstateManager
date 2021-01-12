package com.openclassrooms.realestatemanager.presentation.utils;

import android.content.Context;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding;
import com.openclassrooms.realestatemanager.databinding.BottomSheetPropertyTypeLayoutBinding;
import com.openclassrooms.realestatemanager.databinding.CustomBottomSheetPointOfInterestLayoutBinding;

import java.util.ArrayList;
import java.util.List;

public class FilterHelper {

    private ActivityMainBinding mainBinding;

    private List<String> requestPointOfInterest;
    private List<String> requestPropertyType;

    public FilterHelper(ActivityMainBinding mainBinding) {
        this.mainBinding = mainBinding;
    }

    public List<String> requestPointOfInterest() {
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

        return requestPointOfInterest;
    }

    public List<String> requestPropertyType() {
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

        return requestPropertyType;
    }
}

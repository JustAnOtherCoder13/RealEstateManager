package com.picone.core.utils;

import androidx.annotation.NonNull;

import com.picone.core.domain.entity.Property;
import com.picone.core.domain.entity.RealEstateAgent;

import java.util.Arrays;
import java.util.List;

public class ConstantParameters {

    public static final String realEstateAgentTable = "real_estate_agent_table";
    public static final String propertyTable = "property_table";
    public static final String propertyPhotoTable = "property_photo_table";
    public static final String propertyLocationTable = "property_location_table";
    public static final String pointOfInterestTable = "property_point_of_interest_table";

    public static String MAPS_KEY;
    public static final int REQUEST_CODE = 13700;
    public static final int MAPS_CAMERA_ZOOM = 9;
    public static final String RADIUS = "400";


    @NonNull
    public static Property PROPERTY_TO_ADD(@NonNull RealEstateAgent agent) {
      Property property = new Property();
      property.setRealEstateAgentId(agent.getId());
      return property;
    }

    public static String ADD_PHOTO = "AddPhoto";

    public static final List<String> POINT_OF_INTEREST_TYPE = Arrays.asList(
            "school",
            "supermarket",
            "restaurant"
    );




}

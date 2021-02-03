package com.picone.core.utils;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.picone.core.domain.entity.Property;
import com.picone.core.domain.entity.PropertyInformation;
import com.picone.core.domain.entity.RealEstateAgent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ConstantParameters {

    //------------------------------- ENTITY TABLE NAME ----------------------
    public static final String realEstateAgentTable = "real_estate_agent_table";
    public static final String propertyInformationTable = "property_information_table";
    public static final String propertyPhotoTable = "property_photo_table";
    public static final String propertyLocationTable = "property_location_table";
    public static final String pointOfInterestTable = "property_point_of_interest_table";

    //------------------------------- REQUEST CODE ----------------------
    public static final int LOCATION_PERMISSION_CODE = 13700;
    public static final int MAPS_CAMERA_LARGE_ZOOM = 9;
    public static final int MAPS_CAMERA_NEAR_ZOOM = 15;
    public static final int CAMERA_PERMISSION_CODE = 101;
    public static final int CAMERA_PHOTO_INTENT_REQUEST_CODE = 102;
    public static final int CAMERA_VIDEO_INTENT_REQUEST_CODE = 106;
    public static final int READ_PERMISSION_CODE = 103;
    public static final int WRITE_PERMISSION_CODE = 104;
    public static final int GALLERY_REQUEST_CODE = 105;

    //------------------------------- CONTENT PROVIDER ----------------------
    private static final String AUTHORITY = "com.openclassrooms.realestatemanager.provider";
    private static final String TABLE_NAME = PropertyInformation.class.getSimpleName();
    public static final Uri URI_ITEM = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);

    public static final int NOTIFICATION_ID = 7;
    public static final String NOTIFICATION_TAG = "REAL_ESTATE_MANAGER";
    public static final String RADIUS = "400";
    public static String STATIC_MAP_SIZE = "140";
    public static String BASE_STATIC_MAP_URI = "https://maps.googleapis.com/maps/api/staticmap?";
    public static String MAPS_KEY;
    public static String ADD_PHOTO = "AddPhoto";


    @NonNull
    public static String getTodayDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
        return dateFormat.format(new Date());
    }

    @NonNull
    public static Property PROPERTY_TO_ADD(@NonNull RealEstateAgent agent) {
        Property property = new Property();
        property.propertyInformation = new PropertyInformation();
        property.propertyInformation.setRealEstateAgentId(agent.getId());
        return property;
    }

    public static final List<String> POINT_OF_INTEREST_TYPE = Arrays.asList(
            "school",
            "supermarket",
            "restaurant"
    );
}

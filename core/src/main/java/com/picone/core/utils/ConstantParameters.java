package com.picone.core.utils;

import androidx.annotation.NonNull;

import com.picone.core.domain.entity.Property;
import com.picone.core.domain.entity.PropertyFactory;
import com.picone.core.domain.entity.RealEstateAgent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ConstantParameters {

    public static final String realEstateAgentTable = "real_estate_agent_table";
    public static final String propertyTable = "property_table";
    public static final String propertyPhotoTable = "property_photo_table";
    public static final String propertyLocationTable = "property_location_table";
    public static final String pointOfInterestTable = "property_point_of_interest_table";

    public static String MAPS_KEY;
    public static final int LOCATION_PERMISSION_CODE = 13700;
    public static final int MAPS_CAMERA_LARGE_ZOOM = 9;
    public static final int MAPS_CAMERA_NEAR_ZOOM = 15;
    public static final int CAMERA_PERMISSION_CODE = 101;
    public static final int CAMERA_PHOTO_INTENT_REQUEST_CODE = 102;
    public static final int CAMERA_VIDEO_INTENT_REQUEST_CODE = 106;
    public static final int READ_PERMISSION_CODE = 103;
    public static final int WRITE_PERMISSION_CODE = 104;
    public static final int GALLERY_REQUEST_CODE = 105;
    public static final String FILE_PROVIDER_AUTH = "com.openclassrooms.android.fileprovider";




    public static String STATIC_MAP_SIZE = "140";
    public static String BASE_STATIC_MAP_URI = "https://maps.googleapis.com/maps/api/staticmap?";

    public static final String RADIUS = "400";

    @NonNull
    public static String getTodayDate(){
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
        return dateFormat.format(new Date());
    }

    @NonNull
    public static PropertyFactory PROPERTY_TO_ADD(@NonNull RealEstateAgent agent) {
      PropertyFactory property = new PropertyFactory();
      property.property = new Property();
      property.property.setRealEstateAgentId(agent.getId());
      return property;
    }

    public static String ADD_PHOTO = "AddPhoto";

    public static final List<String> POINT_OF_INTEREST_TYPE = Arrays.asList(
            "school",
            "supermarket",
            "restaurant"
    );




}

package com.picone.core.data;

import com.picone.core.domain.entity.PointOfInterest;
import com.picone.core.domain.entity.Property;
import com.picone.core.domain.entity.PropertyLocation;
import com.picone.core.domain.entity.PropertyPhoto;
import com.picone.core.domain.entity.RealEstateAgent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Generator {

    private static List<RealEstateAgent> AGENTS = Arrays.asList(
            new RealEstateAgent(1, "Manager1 address", "Frank", "Manager1Avatar"),
            new RealEstateAgent(2, "Manager2 address", "Luc", "Manager2Avatar"),
            new RealEstateAgent(3, "Manager3 address", "Robert","Manager3Avatar"),
            new RealEstateAgent(4, "Manager4 address", "Andy","Manager4Avatar")
            );

    public static List<RealEstateAgent> generateAgents() {
        return new ArrayList<>(AGENTS);
    }

    private static List<PropertyPhoto> PHOTOS = Arrays.asList(
            new PropertyPhoto(1, "Photo1", "Living Room", 1),
            new PropertyPhoto(2, "Photo2", "Bedroom", 1),
            new PropertyPhoto(3, "Photo3", "Kitchen",1),
            new PropertyPhoto(4, "Photo4", "Front face",2)
    );

    public static List<PropertyPhoto> generatePhotos() {
        return new ArrayList<>(PHOTOS);
    }

    private static List<PointOfInterest> POINT_OF_INTEREST = Arrays.asList(
            new PointOfInterest(1, 1),
            new PointOfInterest(2, 2),
            new PointOfInterest(3, 2),
            new PointOfInterest(4, 2)
    );

    public static List<PointOfInterest> generatePointOfInterests() {
        return new ArrayList<>(POINT_OF_INTEREST);
    }

    private static List<Property> PROPERTY = Arrays.asList(
            new Property(1, 1, "Property1 address", "cotage",500,15,560000),
            new Property(2, 3, "Property2 address", "penthouse",200,7,450000)
    );

    public static List<Property> generateProperties() {
        return new ArrayList<>(PROPERTY);
    }

}

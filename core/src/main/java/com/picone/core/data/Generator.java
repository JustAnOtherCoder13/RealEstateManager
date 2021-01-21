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

    public static RealEstateAgent generateAgents() {
        return new RealEstateAgent(1, "Manager1 address", "Frank", "Manager1Avatar");
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
            new PointOfInterest(1, 1,"ecole elementaire",0.0,0.0,"school","icon"),
            new PointOfInterest(2, 2,"restaurant le chateau",0.0,0.0,"restaurant","icon"),
            new PointOfInterest(3, 2,"hotel mercure",0.0,0.0,"hostel","icon"),
            new PointOfInterest(4, 2,"SuperU",0.0,0.0,"super market","icon")
    );

    public static List<PointOfInterest> generatePointOfInterests() {
        return new ArrayList<>(POINT_OF_INTEREST);
    }

    private static List<Property> PROPERTIES = Arrays.asList(
            new Property(1, 1, "Avenue Henry Barbusse, Les Tilleuls, 13250 Saint Chamas","Bouches-du-rhône", "cotage",500,15,560000,"First house description",6,3,false,"30/06/2020",""),
            new Property(2, 1, "Property2 address","Bouches-du-rhône", "penthouse",200,7,450000,"Second house description",3,1,false,"15/10/2020","")
    );

    public static List<Property> generateProperties() {
        return new ArrayList<>(PROPERTIES);
    }

    private static List<PropertyLocation> PROPERTIES_LOCATION = Arrays.asList(
            new PropertyLocation(1,43.543732,5.036901,"Bouches-du-rhône",1),
            new PropertyLocation(2,43.560711,5.072869,"Bouches-du-rhône",2)
    );

    public static List<PropertyLocation> generatePropertyLocation(){return new ArrayList<>(PROPERTIES_LOCATION);}

}

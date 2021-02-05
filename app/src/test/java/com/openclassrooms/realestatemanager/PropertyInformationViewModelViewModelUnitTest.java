package com.openclassrooms.realestatemanager;

import com.picone.core.domain.entity.Property;

import org.junit.Test;

import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class PropertyInformationViewModelViewModelUnitTest extends BaseViewModelUnitTest {

    @Test
    public void testNotNull(){
        assertNotNull(propertyViewModel);
        assertTrue(propertyViewModel.getAllProperties.hasObservers());
        assertNotNull(propertyViewModel.getAllProperties.getValue());
    }

    //-------------------------------------PROPERTIES----------------------------------
    @Test
    public void getAllPropertiesShouldReturnGeneratedProperties(){
        assertEquals(2, Objects.requireNonNull(propertyViewModel.getAllProperties.getValue()).size());
        assertEquals(allProperties.get(0).propertyLocation.getAddress(),propertyViewModel.getAllProperties.getValue().get(0).propertyLocation.getAddress());
    }

    @Test
    public void addPropertyShouldUpdatePropertiesList(){
        propertyViewModel.addProperty(propertyToAdd);
        assertEquals(3, Objects.requireNonNull(propertyViewModel.getAllProperties.getValue()).size());
        assertTrue(propertyViewModel.getAllProperties.getValue().contains(propertyToAdd));
        assertEquals(propertyToAdd.propertyLocation.getAddress(),propertyViewModel.getAllProperties.getValue().get(2).propertyLocation.getAddress());
    }

    @Test
    public void updatePropertyShouldUpdatePropertyValue(){
        Property property = Objects.requireNonNull(propertyViewModel.getAllProperties.getValue()).get(0);
        property.propertyInformation.setDescription("test description");
        propertyViewModel.updatePropertyInformation(property);
        assertEquals(Objects.requireNonNull(propertyViewModel.getAllProperties.getValue()).get(0).propertyInformation.getDescription(), "test description");
    }

    //-------------------------------------MEDIA----------------------------------

    @Test
    public void addMediaShouldUpdateMediaList(){
        propertyViewModel.addPropertyMedia(photoToAdd);
        assertEquals(4, Objects.requireNonNull(propertyViewModel.getAllProperties.getValue()).get(0).medias.size());
        assertTrue(propertyViewModel.getAllProperties.getValue().get(0).medias.contains(photoToAdd));
    }

    @Test
    public void deleteMediaShouldUpdateMediaList(){
        propertyViewModel.deleteSelectedMediaForProperty(mediasToDelete);
        propertyViewModel.setAllProperties();
        assertEquals(2, Objects.requireNonNull(propertyViewModel.getAllProperties.getValue()).get(0).medias.size());
        assertFalse(propertyViewModel.getAllProperties.getValue().get(0).medias.contains(photoToDelete));
    }

    //-------------------------------------POINT OF INTEREST----------------------------------

    @Test
    public void getPointOfInterestForPropertyShouldReturnPropertyRelatedPoint(){
        assertEquals(1, Objects.requireNonNull(propertyViewModel.getAllProperties.getValue()).get(0).pointOfInterests.size());
    }

    @Test
    public void addPointOfInterestShouldUpdatePointList(){
        propertyViewModel.addPropertyPointOfInterest(pointOfInterestsToAdd);
        assertEquals(2, Objects.requireNonNull(propertyViewModel.getAllProperties.getValue()).get(0).pointOfInterests.size());
        assertTrue(propertyViewModel.getAllProperties.getValue().get(0).pointOfInterests.contains(pointOfInterestToAdd));
    }

    @Test
    public void UpdatePointOfInterestsShouldUpdateOriginalPointOfInterests(){
        propertyViewModel.deletePointsOfInterestForProperty(allProperties.get(0));
        propertyViewModel.updatePointOfInterest(updatedPointOfInterests);
        assertEquals(1, Objects.requireNonNull(propertyViewModel.getAllProperties.getValue()).get(0).pointOfInterests.size());
        assertTrue(propertyViewModel.getAllProperties.getValue().get(0).pointOfInterests.containsAll(updatedPointOfInterests));
    }

    //-------------------------------------LOCATION----------------------------------

    @Test
    public void addPropertyLocationShouldAddNewPropertyLocation(){
        propertyViewModel.addProperty(propertyToAdd);
        propertyViewModel.addPropertyLocationForProperty(propertyLocationToAdd);
        assertEquals(3, Objects.requireNonNull(propertyViewModel.getAllProperties.getValue()).size());
        assertEquals(Objects.requireNonNull(propertyViewModel.getAllProperties.getValue()).get(2).propertyLocation.getLatitude(), propertyLocationToAdd.getLatitude(), 0.0);
    }

    @Test
    public void UpdatePropertyLocationShouldUpdateOriginalPropertyLocationValue(){
        propertyViewModel.addProperty(propertyToAdd);
        propertyViewModel.addPropertyLocationForProperty(propertyLocationToAdd);
        propertyViewModel.updatePropertyLocation(updatedPropertyLocation);
        assertTrue(Objects.requireNonNull(Objects.requireNonNull(propertyViewModel.getAllProperties.getValue()).get(2).propertyLocation.getRegion().equalsIgnoreCase(updatedPropertyLocation.getRegion())));
    }
}
package com.openclassrooms.realestatemanager;

import com.picone.core.data.Generator;

import org.junit.Test;

import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class PropertyViewModelUnitTest extends BaseUnitTest {

    @Test
    public void testNotNull(){
        assertNotNull(propertyViewModel);
        assertTrue(propertyViewModel.getAllProperties.hasObservers());
        assertTrue(propertyViewModel.getAllPointOfInterestForProperty.hasObservers());
        assertTrue(propertyViewModel.getAllPropertyPhotosForProperty.hasObservers());
        assertNotNull(propertyViewModel.getAllProperties.getValue());
    }

    @Test
    public void getAllPropertiesShouldReturnGeneratedProperties(){
        assertEquals(2, Objects.requireNonNull(propertyViewModel.getAllProperties.getValue()).size());
        assertEquals(Generator.generateProperties().get(0).getAddress(),propertyViewModel.getAllProperties.getValue().get(0).getAddress());
    }

    @Test
    public void addPropertyShouldUpdatePropertiesList(){
        propertyViewModel.addProperty(propertyToAdd);
        assertEquals(3, Objects.requireNonNull(propertyViewModel.getAllProperties.getValue()).size());
        assertTrue(propertyViewModel.getAllProperties.getValue().contains(propertyToAdd));
        assertEquals(propertyToAdd.getAddress(),propertyViewModel.getAllProperties.getValue().get(2).getAddress());
    }

    @Test
    public void updatePropertyShouldUpdatePropertyValue(){
        firstPropertyToUpdate.setAddress("my updated address");
        propertyViewModel.updateProperty(firstPropertyToUpdate);
        assertEquals(Objects.requireNonNull(propertyViewModel.getAllProperties.getValue()).get(0).getAddress(),firstPropertyToUpdate.getAddress());
    }

    @Test
    public void getAllPhotoForPropertyIdShouldReturnPropertyRelatedPhotos(){
        assertEquals(3, Objects.requireNonNull(propertyViewModel.getAllPropertyPhotosForProperty.getValue()).size());
        assertEquals(propertyViewModel.getAllPropertyPhotosForProperty.getValue().get(0).getPropertyId()
        ,propertyViewModel.getAllPropertyPhotosForProperty.getValue().get(1).getPropertyId());
        assertEquals(propertyViewModel.getAllPropertyPhotosForProperty.getValue().get(1).getPropertyId()
                ,propertyViewModel.getAllPropertyPhotosForProperty.getValue().get(2).getPropertyId());
    }

    @Test
    public void addPhotoShouldUpdatePhotoList(){
        propertyViewModel.addPropertyPhoto(photoToAdd);
        assertEquals(4, Objects.requireNonNull(propertyViewModel.getAllPropertyPhotosForProperty.getValue()).size());
        assertTrue(propertyViewModel.getAllPropertyPhotosForProperty.getValue().contains(photoToAdd));
    }

    @Test
    public void deletePhotoShouldUpdatePhotoList(){
        propertyViewModel.deletePropertyPhoto(photoToDelete);
        assertEquals(2, Objects.requireNonNull(propertyViewModel.getAllPropertyPhotosForProperty.getValue()).size());
        assertFalse(propertyViewModel.getAllPropertyPhotosForProperty.getValue().contains(photoToDelete));
    }

    @Test
    public void getPointOfInterestForPropertyShouldReturnPropertyRelatedPoint(){
        assertEquals(1, Objects.requireNonNull(propertyViewModel.getAllPointOfInterestForProperty.getValue()).size());
    }

    @Test
    public void addPointOfInterestShouldUpdatePointList(){
        propertyViewModel.addPropertyPointOfInterest(pointOfInterestsToAdd);
        assertEquals(2, Objects.requireNonNull(propertyViewModel.getAllPointOfInterestForProperty.getValue()).size());
        assertTrue(propertyViewModel.getAllPointOfInterestForProperty.getValue().contains(pointOfInterestToAdd));
    }

    @Test
    public void getPositionShouldReturnPropertyPosition(){
        propertyViewModel.setPropertyLocationForProperty(Generator.generateProperties().get(0));
        assertEquals(Generator.generatePropertyLocation().get(0).getLatitude(),
                Objects.requireNonNull(propertyViewModel.getPropertyLocationForProperty.getValue()).getLatitude(), 0.0);
    }

    @Test
    public void addPropertyPositionShouldAddNewPropertyLocation(){
        propertyViewModel.addPropertyLocationForProperty(propertyLocationToAdd);
        propertyViewModel.setPropertyLocationForProperty(propertyToAdd);
        assertEquals(Objects.requireNonNull(propertyViewModel.getPropertyLocationForProperty.getValue()).getLatitude(), propertyLocationToAdd.getLatitude(), 0.0);
    }

    @Test
    public void UpdatePropertyLocationShouldUpdateOriginalPropertyLocationValue(){
        propertyViewModel.addPropertyLocationForProperty(propertyLocationToAdd);
        propertyViewModel.updatePropertyLocation(updatedPropertyLocation);
        propertyViewModel.setPropertyLocationForProperty(propertyToAdd);
        assertTrue(Objects.requireNonNull(propertyViewModel.getPropertyLocationForProperty.getValue()).getRegion().equalsIgnoreCase(updatedPropertyLocation.getRegion()));
    }

    @Test
    public void UpdatePointOfInterestsShouldUpdateOriginalPointOfInterests(){
        propertyViewModel.updatePointOfInterest(updatedPointOfInterests);
        assertEquals(1, Objects.requireNonNull(propertyViewModel.getAllPointOfInterestForProperty.getValue()).size());
        assertTrue(propertyViewModel.getAllPointOfInterestForProperty.getValue().contains(newPointOfInterest));
    }

}
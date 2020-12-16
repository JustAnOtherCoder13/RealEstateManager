package com.openclassrooms.realestatemanager;

import com.picone.core.data.Generator;

import org.junit.Test;

import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class PropertyViewModelUnitTest extends BaseTest{

    @Test
    public void testNotNull(){
        assertNotNull(propertyViewModel);
        assertTrue(propertyViewModel.getAllRoomProperties.hasObservers());
        assertTrue(propertyViewModel.getAllRoomPointOfInterestForProperty.hasObservers());
        assertTrue(propertyViewModel.getAllRoomPropertyPhotosForProperty.hasObservers());
        assertNotNull(propertyViewModel.getAllRoomProperties.getValue());
    }

    @Test
    public void getAllPropertiesShouldReturnGeneratedProperties(){
        assertEquals(2, Objects.requireNonNull(propertyViewModel.getAllRoomProperties.getValue()).size());
        assertEquals(Generator.generateProperties().get(0).getAddress(),propertyViewModel.getAllRoomProperties.getValue().get(0).getAddress());
    }

    @Test
    public void addPropertyShouldUpdatePropertiesList(){
        propertyViewModel.addRoomProperty(propertyToAdd);
        assertEquals(3, Objects.requireNonNull(propertyViewModel.getAllRoomProperties.getValue()).size());
        assertTrue(propertyViewModel.getAllRoomProperties.getValue().contains(propertyToAdd));
        assertEquals(propertyToAdd.getAddress(),propertyViewModel.getAllRoomProperties.getValue().get(2).getAddress());
    }

    @Test
    public void updatePropertyShouldUpdatePropertyValue(){
        firstPropertyToUpdate.setAddress("my updated address");
        propertyViewModel.updateRoomProperty(firstPropertyToUpdate);
        assertEquals(Objects.requireNonNull(propertyViewModel.getAllRoomProperties.getValue()).get(0).getAddress(),firstPropertyToUpdate.getAddress());
    }

    @Test
    public void getAllPhotoForPropertyIdShouldReturnPropertyRelatedPhotos(){
        assertEquals(3, Objects.requireNonNull(propertyViewModel.getAllRoomPropertyPhotosForProperty.getValue()).size());
        assertEquals(propertyViewModel.getAllRoomPropertyPhotosForProperty.getValue().get(0).getPropertyId()
        ,propertyViewModel.getAllRoomPropertyPhotosForProperty.getValue().get(1).getPropertyId());
        assertEquals(propertyViewModel.getAllRoomPropertyPhotosForProperty.getValue().get(1).getPropertyId()
                ,propertyViewModel.getAllRoomPropertyPhotosForProperty.getValue().get(2).getPropertyId());
    }

    @Test
    public void addPhotoShouldUpdatePhotoList(){
        propertyViewModel.addRoomPropertyPhoto(photoToAdd);
        assertEquals(4, Objects.requireNonNull(propertyViewModel.getAllRoomPropertyPhotosForProperty.getValue()).size());
        assertTrue(propertyViewModel.getAllRoomPropertyPhotosForProperty.getValue().contains(photoToAdd));
    }

    @Test
    public void deletePhotoShouldUpdatePhotoList(){
        propertyViewModel.deleteRoomPropertyPhoto(photoToDelete);
        assertEquals(2, Objects.requireNonNull(propertyViewModel.getAllRoomPropertyPhotosForProperty.getValue()).size());
        assertFalse(propertyViewModel.getAllRoomPropertyPhotosForProperty.getValue().contains(photoToDelete));
    }

    @Test
    public void getPointOfInterestForPropertyShouldReturnPropertyRelatedPoint(){
        assertEquals(1, Objects.requireNonNull(propertyViewModel.getAllRoomPointOfInterestForProperty.getValue()).size());
    }

    @Test
    public void addPointOfInterestShouldUpdatePointList(){
        propertyViewModel.addRoomPropertyPointOfInterest(pointOfInterestToAdd);
        assertEquals(2, Objects.requireNonNull(propertyViewModel.getAllRoomPointOfInterestForProperty.getValue()).size());
        assertTrue(propertyViewModel.getAllRoomPointOfInterestForProperty.getValue().contains(pointOfInterestToAdd));
    }

}
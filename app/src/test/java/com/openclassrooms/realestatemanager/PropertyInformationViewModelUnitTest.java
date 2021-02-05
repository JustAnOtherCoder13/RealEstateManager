package com.openclassrooms.realestatemanager;

import org.junit.Test;

import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class PropertyInformationViewModelUnitTest extends BaseUnitTest {

    @Test
    public void testNotNull(){
        assertNotNull(propertyViewModel);
        assertTrue(propertyViewModel.getAllProperties.hasObservers());
        assertNotNull(propertyViewModel.getAllProperties.getValue());
    }

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

    /*@Test
    public void updatePropertyShouldUpdatePropertyValue(){
        firstPropertyInformationToUpdate.setAddress("my updated address");
        propertyViewModel.updatePropertyInformation(firstPropertyInformationToUpdate);
        assertEquals(Objects.requireNonNull(propertyViewModel.getAllProperties.getValue()).get(0).getAddress(), firstPropertyInformationToUpdate.getAddress());
    }*/


    @Test
    public void addPhotoShouldUpdatePhotoList(){
        propertyViewModel.addPropertyMedia(photoToAdd);
        assertEquals(4, Objects.requireNonNull(propertyViewModel.getAllProperties.getValue()).get(0).medias.size());
        assertTrue(propertyViewModel.getAllProperties.getValue().get(0).medias.contains(photoToAdd));
    }

    @Test
    public void deletePhotoShouldUpdatePhotoList(){
        propertyViewModel.deletePropertyPhoto(photoToDelete);
        assertEquals(2, Objects.requireNonNull(propertyViewModel.getAllProperties.getValue()).get(0).medias.size());
        assertFalse(propertyViewModel.getAllProperties.getValue().get(0).medias.contains(photoToDelete));
    }

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

   /* @Test
    public void getPositionShouldReturnPropertyPosition(){
        propertyViewModel.setPropertyLocationForProperty(Generator.generatePropertiesInformation().get(0));
        assertEquals(Generator.generatePropertyLocation().get(0).getLatitude(),
                Objects.requireNonNull(propertyViewModel.getPropertyLocationForProperty.getValue()).getLatitude(), 0.0);
    }*/

    @Test
    public void addPropertyPositionShouldAddNewPropertyLocation(){
        propertyViewModel.addPropertyLocationForProperty(propertyLocationToAdd);
        //propertyViewModel.setPropertyLocationForProperty(propertyInformationToAdd);
        assertEquals(Objects.requireNonNull(propertyViewModel.getAllProperties.getValue()).get(0).propertyLocation.getLatitude(), propertyLocationToAdd.getLatitude(), 0.0);
    }

    @Test
    public void UpdatePropertyLocationShouldUpdateOriginalPropertyLocationValue(){
        propertyViewModel.addPropertyLocationForProperty(propertyLocationToAdd);
        propertyViewModel.updatePropertyLocation(updatedPropertyLocation);
       // propertyViewModel.setPropertyLocationForProperty(propertyInformationToAdd);
        assertTrue(Objects.requireNonNull(Objects.requireNonNull(propertyViewModel.getAllProperties.getValue()).get(2).propertyLocation.getRegion().equalsIgnoreCase(updatedPropertyLocation.getRegion())));
    }

    @Test
    public void UpdatePointOfInterestsShouldUpdateOriginalPointOfInterests(){
        propertyViewModel.updatePointOfInterest(updatedPointOfInterests);
        assertEquals(1, Objects.requireNonNull(propertyViewModel.getAllProperties.getValue()).get(0).pointOfInterests.size());
        assertTrue(propertyViewModel.getAllProperties.getValue().get(0).pointOfInterests.contains(newPointOfInterest));
    }

}
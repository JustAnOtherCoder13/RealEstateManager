package com.openclassrooms.realestatemanager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.List;

import static com.openclassrooms.realestatemanager.presentation.utils.Utils.formatStringToDate;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class FilterUnitTest extends BaseViewModelUnitTest {

    @Test
    public void filterByRegion(){
        filterHelper.filterByRegion("Var");
        assertEquals(filterHelper.getFilteredProperties().size(),1);
        assertTrue(filterHelper.getFilteredProperties().get(0).propertyLocation.getRegion().equalsIgnoreCase("Var"));
    }

    @Test
    public void filterByNumberOfMedias(){
        filterHelper.filterByNumberOfMedias("3");
        assertEquals(filterHelper.getFilteredProperties().size(),1);
        assertTrue(filterHelper.getFilteredProperties().get(0).medias.size()>=3);
    }

    @Test
    public void filterByPointOfInterest(){
        List<String> requestedPointOfInterests = new ArrayList<>();
        requestedPointOfInterests.add("restaurant");
        requestedPointOfInterests.add("super market");
        filterHelper.setRequestPointsOfInterests(requestedPointOfInterests);
        filterHelper.filterByPointOfInterest();
        assertEquals(filterHelper.getFilteredProperties().size(),1);
        assertTrue(filterHelper.getFilteredProperties().get(0).pointOfInterests.get(0).getType().equalsIgnoreCase("restaurant"));
    }

    @Test
    public void filterByPropertyType(){
        List<String> requestType = new ArrayList<>();
        requestType.add("cottage");
        filterHelper.setRequestPropertyType(requestType);
        filterHelper.filterByPropertyType();
        assertEquals(filterHelper.getFilteredProperties().size(),1);
        assertTrue(filterHelper.getFilteredProperties().get(0).propertyInformation.getPropertyType().equalsIgnoreCase("cottage"));
    }

    @Test
    public void filterByOnMarketFrom(){
        filterHelper.filterByOnMarketFrom("14/10/2020");
        assertEquals(filterHelper.getFilteredProperties().size(),1);
        assertTrue(formatStringToDate(filterHelper.getFilteredProperties().get(0).propertyInformation.getEnterOnMarket())
        .compareTo(formatStringToDate("14/10/2020"))>0);
    }

    @Test
    public void filterByPrice(){
        filterHelper.filterByPrice(460000,570000);
        assertEquals(filterHelper.getFilteredProperties().size(),1);
        assertTrue(filterHelper.getFilteredProperties().get(0).propertyInformation.getPrice()>460000
        && filterHelper.getFilteredProperties().get(0).propertyInformation.getPrice()<570000);
    }
    @Test
    public void filterBySurface(){
        filterHelper.filterBySurface(190,300);
        assertEquals(filterHelper.getFilteredProperties().size(),1);
        assertEquals(filterHelper.getFilteredProperties().get(0), allProperties.get(1));
        assertTrue(filterHelper.getFilteredProperties().get(0).propertyInformation.getPropertyArea()>190
        && filterHelper.getFilteredProperties().get(0).propertyInformation.getPropertyArea()<300);
    }

    @Test
    public void filterByRoom(){
        filterHelper.filterByRoom(8,20);
        assertEquals(filterHelper.getFilteredProperties().size(),1);
        assertEquals(filterHelper.getFilteredProperties().get(0), allProperties.get(0));
        assertTrue(filterHelper.getFilteredProperties().get(0).propertyInformation.getNumberOfRooms()>8
                && filterHelper.getFilteredProperties().get(0).propertyInformation.getNumberOfRooms()<20);
    }

}

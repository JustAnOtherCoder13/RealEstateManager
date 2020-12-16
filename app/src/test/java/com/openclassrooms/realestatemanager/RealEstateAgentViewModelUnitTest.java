package com.openclassrooms.realestatemanager;

import com.picone.core.data.Generator;

import org.junit.Test;

import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class RealEstateAgentViewModelUnitTest extends BaseUnitTest {

    @Test
    public void testNotNull() {
        assertNotNull(realEstateAgentViewModel);
        assertTrue(realEstateAgentViewModel.getAllRoomAgents.hasObservers());
        assertNotNull(realEstateAgentViewModel.getAllRoomAgents.getValue());
    }

    @Test
    public void getAllAgentsShouldReturnGeneratedAgents(){
        assertEquals(4, Objects.requireNonNull(realEstateAgentViewModel.getAllRoomAgents.getValue()).size());
        assertEquals(Generator.generateAgents().get(0).getAddress(),realEstateAgentViewModel.getAllRoomAgents.getValue().get(0).getAddress());
    }
}

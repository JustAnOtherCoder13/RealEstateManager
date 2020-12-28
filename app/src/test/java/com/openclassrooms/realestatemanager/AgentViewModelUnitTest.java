package com.openclassrooms.realestatemanager;

import com.picone.core.data.Generator;

import org.junit.Test;

import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class AgentViewModelUnitTest extends BaseUnitTest {

    @Test
    public void testNotNull() {
        assertNotNull(agentViewModel);
        assertTrue(agentViewModel.getAgent.hasObservers());
        assertNotNull(agentViewModel.getAgent.getValue());
    }

    @Test
    public void getAllAgentShouldReturnGeneratedAgent(){
        assertEquals(Generator.generateAgents().getAddress(), Objects.requireNonNull(agentViewModel.getAgent.getValue()).getAddress());
    }
}
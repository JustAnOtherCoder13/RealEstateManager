package com.openclassrooms.realestatemanager;

import com.picone.core.data.Generator;

import org.junit.Test;

import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class AgentViewModelViewModelUnitTest extends BaseViewModelUnitTest {

    @Test
    public void testNotNull() {
        assertNotNull(agentViewModel);
        assertTrue(agentViewModel.getAgent.hasObservers());
        assertNotNull(agentViewModel.getAgent.getValue());
    }

    @Test
    public void getAllAgentShouldReturnGeneratedAgent() {
        agentViewModel.setAgent();
        assertEquals(Generator.generateAgents().getName(), Objects.requireNonNull(agentViewModel.getAgent.getValue()).getName());
    }
}

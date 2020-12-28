package com.picone.core.domain.interactors.agent;

import com.picone.core.data.realEstateAgent.RealEstateAgentRepository;

import javax.inject.Inject;

public abstract class AgentBaseInteractor {

    @Inject
    protected RealEstateAgentRepository realEstateManagerDataSource;

    public AgentBaseInteractor(RealEstateAgentRepository realEstateManagerDataSource) {
        this.realEstateManagerDataSource = realEstateManagerDataSource;
    }
}

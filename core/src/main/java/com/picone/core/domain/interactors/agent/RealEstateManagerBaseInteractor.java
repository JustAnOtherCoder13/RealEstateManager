package com.picone.core.domain.interactors.agent;

import com.picone.core.data.realEstateManager.RealEstateAgentRepository;

import javax.inject.Inject;

public abstract class RealEstateManagerBaseInteractor {

    @Inject
    protected RealEstateAgentRepository realEstateManagerDataSource;

    public RealEstateManagerBaseInteractor(RealEstateAgentRepository realEstateManagerDataSource) {
        this.realEstateManagerDataSource = realEstateManagerDataSource;
    }
}

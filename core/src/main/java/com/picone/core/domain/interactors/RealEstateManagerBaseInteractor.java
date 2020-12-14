package com.picone.core.domain.interactors;

import com.picone.core.data.realEstateManager.RealEstateManagerRepository;

import javax.inject.Inject;

public abstract class RealEstateManagerBaseInteractor {

    @Inject
    RealEstateManagerRepository realEstateManagerDataSource;

    public RealEstateManagerBaseInteractor(RealEstateManagerRepository realEstateManagerDataSource) {
        this.realEstateManagerDataSource = realEstateManagerDataSource;
    }
}

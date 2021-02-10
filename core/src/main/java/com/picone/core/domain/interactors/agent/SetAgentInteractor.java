package com.picone.core.domain.interactors.agent;

import com.picone.core.data.realEstateAgent.RealEstateAgentRepository;
import com.picone.core.domain.entity.RealEstateAgent;

import io.reactivex.Completable;

public class SetAgentInteractor extends AgentBaseInteractor {

    public SetAgentInteractor(RealEstateAgentRepository realEstateManagerDataSource) {
        super(realEstateManagerDataSource);
    }

    public Completable setAgent(RealEstateAgent agent) {
        return realEstateManagerDataSource.setAgent(agent);
    }
}

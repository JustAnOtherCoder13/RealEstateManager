package com.picone.core.domain.interactors.agent;

import com.picone.core.data.realEstateAgent.RealEstateAgentRepository;
import com.picone.core.domain.entity.RealEstateAgent;

import io.reactivex.Observable;

public class GetAgentInteractor extends AgentBaseInteractor {

    public GetAgentInteractor(RealEstateAgentRepository realEstateManagerDataSource) {
        super(realEstateManagerDataSource);
    }

    public Observable<RealEstateAgent> getAgent(){
        return realEstateManagerDataSource.getAgent();
    }
}

package com.picone.core.domain.interactors.agent;

import com.picone.core.data.realEstateAgent.RealEstateAgentRepository;
import com.picone.core.domain.entity.RealEstateAgent;

import java.util.List;

import io.reactivex.Observable;

public class GetAllRoomAgentInteractor extends RealEstateManagerBaseInteractor {


    public GetAllRoomAgentInteractor(RealEstateAgentRepository realEstateManagerDataSource) {
        super(realEstateManagerDataSource);
    }

    public Observable<List<RealEstateAgent>> getAllAgents(){
        return
        realEstateManagerDataSource.getAllAgents();
    }
}

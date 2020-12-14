package com.picone.core.domain.interactors;

import com.picone.core.data.realEstateManager.RealEstateManagerRepository;
import com.picone.core.domain.entity.RealEstateAgent;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

public class GetAllManagerInteractor extends RealEstateManagerBaseInteractor{


    public GetAllManagerInteractor(RealEstateManagerRepository realEstateManagerDataSource) {
        super(realEstateManagerDataSource);
    }

    public Observable<List<RealEstateAgent>> getAllAgents(){
        return
        realEstateManagerDataSource.getAllAgents();
    }
}

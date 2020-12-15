package com.picone.core.data.realEstateManager;

import com.picone.core.domain.entity.RealEstateAgent;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

public class RealEstateAgentRepository {

    @Inject
    RealEstateManagerDaoImpl realEstateManagerDao;

    public RealEstateAgentRepository(RealEstateManagerDaoImpl realEstateManagerDao) {
        this.realEstateManagerDao = realEstateManagerDao;
    }

    public Observable<List<RealEstateAgent>> getAllAgents(){
        return realEstateManagerDao.getAllAgents();
    }
}

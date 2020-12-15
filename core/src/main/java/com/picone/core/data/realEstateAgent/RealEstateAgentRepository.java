package com.picone.core.data.realEstateAgent;

import com.picone.core.domain.entity.RealEstateAgent;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

public class RealEstateAgentRepository {

    @Inject
    RealEstateAgentDaoImpl realEstateManagerDao;

    public RealEstateAgentRepository(RealEstateAgentDaoImpl realEstateManagerDao) {
        this.realEstateManagerDao = realEstateManagerDao;
    }

    public Observable<List<RealEstateAgent>> getAllAgents(){
        return realEstateManagerDao.getAllAgents();
    }
}

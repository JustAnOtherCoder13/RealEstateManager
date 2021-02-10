package com.picone.core.data.realEstateAgent;

import com.picone.core.domain.entity.RealEstateAgent;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;

public class RealEstateAgentRepository {

    @Inject
    RealEstateAgentDaoImpl realEstateManagerDao;

    public RealEstateAgentRepository(RealEstateAgentDaoImpl realEstateManagerDao) {
        this.realEstateManagerDao = realEstateManagerDao;
    }

    public Observable<RealEstateAgent> getAgent() {
        return realEstateManagerDao.getAgent();
    }

    public Completable setAgent(RealEstateAgent agent) {
        return realEstateManagerDao.setAgent(agent);
    }
}

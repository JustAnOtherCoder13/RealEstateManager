package com.picone.core.data.realEstateManager;

import com.picone.core.domain.entity.RealEstateAgent;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

public class RealEstateManagerRepository {

    @Inject
    RealEstateManagerDaoImpl realEstateManagerDao;

    public RealEstateManagerRepository(RealEstateManagerDaoImpl realEstateManagerDao) {
        this.realEstateManagerDao = realEstateManagerDao;
    }

    public Observable<List<RealEstateAgent>> getAllAgents(){
        return realEstateManagerDao.getAllAgents();
    }
}

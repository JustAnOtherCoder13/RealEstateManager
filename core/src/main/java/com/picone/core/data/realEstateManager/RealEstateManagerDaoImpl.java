package com.picone.core.data.realEstateManager;

import android.annotation.SuppressLint;
import android.util.Log;

import com.picone.core.data.RealEstateManagerDatabase;
import com.picone.core.domain.entity.RealEstateAgent;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;


public class RealEstateManagerDaoImpl implements RealEstateManagerDao {

    @Inject
    protected RealEstateManagerDatabase roomDatabase;
    private RealEstateManagerRoomDao realEstateManagerRoomDao;

    public RealEstateManagerDaoImpl(RealEstateManagerDatabase roomDatabase) {
        this.roomDatabase = roomDatabase;
        this.realEstateManagerRoomDao = roomDatabase.realEstateManagerRoomDao();
    }

    @SuppressLint("CheckResult")
    public Observable<List<RealEstateAgent>> getAllAgents(){
        return realEstateManagerRoomDao.getAllAgents();
    }

}

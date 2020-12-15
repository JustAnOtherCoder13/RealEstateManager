package com.picone.core.data.realEstateAgent;

import android.annotation.SuppressLint;

import com.picone.core.data.RealEstateManagerRoomDatabase;
import com.picone.core.domain.entity.RealEstateAgent;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;


public class RealEstateAgentDaoImpl implements RealEstateAgentDao {

    @Inject
    protected RealEstateManagerRoomDatabase mRoomDatabase;
    private RealEstateAgentRoomDao mRealEstateAgentRoomDao;

    public RealEstateAgentDaoImpl(RealEstateManagerRoomDatabase mRoomDatabase) {
        this.mRoomDatabase = mRoomDatabase;
        this.mRealEstateAgentRoomDao = mRoomDatabase.realEstateManagerRoomDao();
    }

    @SuppressLint("CheckResult")
    public Observable<List<RealEstateAgent>> getAllAgents(){
        return mRealEstateAgentRoomDao.getAllAgents();
    }

}

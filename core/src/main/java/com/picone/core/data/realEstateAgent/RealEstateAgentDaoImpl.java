package com.picone.core.data.realEstateAgent;

import androidx.annotation.NonNull;

import com.picone.core.data.RealEstateManagerRoomDatabase;
import com.picone.core.domain.entity.RealEstateAgent;

import javax.inject.Inject;

import io.reactivex.Observable;


public class RealEstateAgentDaoImpl {

    @Inject
    protected RealEstateManagerRoomDatabase mRoomDatabase;
    private RealEstateAgentRoomDao mRealEstateAgentRoomDao;

    public RealEstateAgentDaoImpl(@NonNull RealEstateManagerRoomDatabase mRoomDatabase) {
        this.mRoomDatabase = mRoomDatabase;
        this.mRealEstateAgentRoomDao = mRoomDatabase.realEstateManagerRoomDao();
    }

    public Observable<RealEstateAgent> getAgent() {
        return mRealEstateAgentRoomDao.getAgent();
    }

}

package com.picone.core.data.realEstateManager;

import android.annotation.SuppressLint;

import com.picone.core.data.RealEstateManagerRoomDatabase;
import com.picone.core.domain.entity.RealEstateAgent;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;


public class RealEstateManagerDaoImpl implements RealEstateManagerDao {

    @Inject
    protected RealEstateManagerRoomDatabase mRoomDatabase;
    private RealEstateManagerRoomDao mRealEstateManagerRoomDao;

    public RealEstateManagerDaoImpl(RealEstateManagerRoomDatabase mRoomDatabase) {
        this.mRoomDatabase = mRoomDatabase;
        this.mRealEstateManagerRoomDao = mRoomDatabase.realEstateManagerRoomDao();
    }

    @SuppressLint("CheckResult")
    public Observable<List<RealEstateAgent>> getAllAgents(){
        return mRealEstateManagerRoomDao.getAllAgents();
    }

}

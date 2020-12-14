package com.picone.core.di;


import android.content.Context;

import androidx.room.PrimaryKey;

import com.picone.core.data.RealEstateManagerDatabase;
import com.picone.core.data.realEstateManager.RealEstateManagerDaoImpl;
import com.picone.core.data.realEstateManager.RealEstateManagerRepository;
import com.picone.core.domain.interactors.GetAllManagerInteractor;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityComponent;
import dagger.hilt.android.qualifiers.ApplicationContext;

@InstallIn(ActivityComponent.class)
@Module
public final class coreModule {

    //-----------------------------------DATA SOURCE------------------------------------------------

    @Singleton
    @Provides
    static RealEstateManagerDatabase provideRealEstateManagerDatabase(@ApplicationContext Context context){
        return RealEstateManagerDatabase.getInstance(context);
    }

    //--------------------------------------DAO-----------------------------------------------------

    @Provides
    static RealEstateManagerDaoImpl provideRealEstateManagerDao(@ApplicationContext Context context){
        return new RealEstateManagerDaoImpl(provideRealEstateManagerDatabase(context));
    }

    //--------------------------------------REPOSITORY-----------------------------------------------------

    @Provides
    static RealEstateManagerRepository provideRealEstateManagerDataSource(@ApplicationContext Context context){
        return new RealEstateManagerRepository(provideRealEstateManagerDao(context));
    }

    //--------------------------------------REAL ESTATE MANAGER INTERACTORS-----------------------------------------------------

    @Provides
    static GetAllManagerInteractor provideGetAllManagers(@ApplicationContext Context context){
        return new GetAllManagerInteractor(provideRealEstateManagerDataSource(context));
    }
}

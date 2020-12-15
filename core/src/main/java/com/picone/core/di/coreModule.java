package com.picone.core.di;


import android.content.Context;

import com.picone.core.data.RealEstateManagerRoomDatabase;
import com.picone.core.data.property.PropertyDaoImpl;
import com.picone.core.data.property.PropertyRepository;
import com.picone.core.data.realEstateManager.RealEstateAgentRepository;
import com.picone.core.data.realEstateManager.RealEstateManagerDaoImpl;
import com.picone.core.domain.interactors.agent.GetAllRoomAgentInteractor;
import com.picone.core.domain.interactors.property.GetAllRoomPropertiesInteractor;

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
    static RealEstateManagerRoomDatabase provideRealEstateManagerRoomDatabase(@ApplicationContext Context context){
        return RealEstateManagerRoomDatabase.getInstance(context);
    }

    //--------------------------------------DAO-----------------------------------------------------

    @Provides
    static RealEstateManagerDaoImpl provideRealEstateManagerDao(@ApplicationContext Context context){
        return new RealEstateManagerDaoImpl(provideRealEstateManagerRoomDatabase(context));
    }

    @Provides
    static PropertyDaoImpl providePropertyDao(@ApplicationContext Context context){
        return new PropertyDaoImpl(provideRealEstateManagerRoomDatabase(context));
    }

    //--------------------------------------REPOSITORY-----------------------------------------------------

    @Provides
    static RealEstateAgentRepository provideRealEstateAgentDataSource(@ApplicationContext Context context){
        return new RealEstateAgentRepository(provideRealEstateManagerDao(context));
    }

    @Provides
    static PropertyRepository providePropertyDataSource(@ApplicationContext Context context){
        return new PropertyRepository(providePropertyDao(context));
    }

    //--------------------------------------REAL ESTATE AGENT INTERACTORS-----------------------------------------------------

    @Provides
    static GetAllRoomAgentInteractor provideGetAllAgents(@ApplicationContext Context context){
        return new GetAllRoomAgentInteractor(provideRealEstateAgentDataSource(context));
    }

    //--------------------------------------PROPERTY INTERACTORS-----------------------------------------------------

    @Provides
    static GetAllRoomPropertiesInteractor provideGetAllProperties(@ApplicationContext Context context){
        return new GetAllRoomPropertiesInteractor(providePropertyDataSource(context));
    }
}

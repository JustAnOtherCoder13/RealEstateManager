package com.picone.core.di;


import android.content.Context;

import com.picone.core.data.RealEstateManagerRoomDatabase;
import com.picone.core.data.property.PropertyDaoImpl;
import com.picone.core.data.property.PropertyRepository;
import com.picone.core.data.realEstateAgent.RealEstateAgentDaoImpl;
import com.picone.core.data.realEstateAgent.RealEstateAgentRepository;
import com.picone.core.domain.interactors.agent.GetAllRoomAgentInteractor;
import com.picone.core.domain.interactors.property.GetAllRoomPointOfInterestForPropertyIdInteractor;
import com.picone.core.domain.interactors.property.GetAllRoomPropertiesInteractor;
import com.picone.core.domain.interactors.property.GetAllRoomPropertyPhotosForPropertyIdInteractor;

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
    static RealEstateAgentDaoImpl provideRealEstateManagerDao(@ApplicationContext Context context){
        return new RealEstateAgentDaoImpl(provideRealEstateManagerRoomDatabase(context));
    }

    @Provides
    static PropertyDaoImpl providePropertyDao(@ApplicationContext Context context){
        return new PropertyDaoImpl(provideRealEstateManagerRoomDatabase(context));
    }

    //--------------------------------------REPOSITORY-----------------------------------------------------

    @Singleton
    @Provides
    static RealEstateAgentRepository provideRealEstateAgentDataSource(@ApplicationContext Context context){
        return new RealEstateAgentRepository(provideRealEstateManagerDao(context));
    }

    @Singleton
    @Provides
    static PropertyRepository providePropertyDataSource(@ApplicationContext Context context){
        return new PropertyRepository(providePropertyDao(context));
    }

    //--------------------------------------REAL ESTATE AGENT INTERACTORS-----------------------------------------------------

    @Provides
    static GetAllRoomAgentInteractor provideGetAllRoomAgents(@ApplicationContext Context context){
        return new GetAllRoomAgentInteractor(provideRealEstateAgentDataSource(context));
    }

    //--------------------------------------PROPERTY INTERACTORS-----------------------------------------------------

    @Provides
    static GetAllRoomPropertiesInteractor provideGetAllRoomProperties(@ApplicationContext Context context){
        return new GetAllRoomPropertiesInteractor(providePropertyDataSource(context));
    }

    @Provides
    static GetAllRoomPointOfInterestForPropertyIdInteractor provideGetAllRoomPointOfInterestForPropertyId(@ApplicationContext Context context){
        return new GetAllRoomPointOfInterestForPropertyIdInteractor(providePropertyDataSource(context));
    }

    @Provides
    static GetAllRoomPropertyPhotosForPropertyIdInteractor provideGetAllPropertyPhotosForPropertyId(@ApplicationContext Context context){
        return new GetAllRoomPropertyPhotosForPropertyIdInteractor(providePropertyDataSource(context));
    }
}

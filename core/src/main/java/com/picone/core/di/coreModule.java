package com.picone.core.di;


import android.content.Context;

import androidx.room.PrimaryKey;

import com.picone.core.data.RealEstateManagerRoomDatabase;
import com.picone.core.data.property.PropertyDaoImpl;
import com.picone.core.data.property.PropertyRepository;
import com.picone.core.data.realEstateAgent.RealEstateAgentDaoImpl;
import com.picone.core.data.realEstateAgent.RealEstateAgentRepository;
import com.picone.core.domain.interactors.agent.GetAllRoomAgentInteractor;
import com.picone.core.domain.interactors.property.AddRoomPropertyInteractor;
import com.picone.core.domain.interactors.property.GetAllRoomPropertiesInteractor;
import com.picone.core.domain.interactors.property.UpdateRoomPropertyInteractor;
import com.picone.core.domain.interactors.property.location.AddPropertyLocationInteractor;
import com.picone.core.domain.interactors.property.location.GetPropertyLocationInteractor;
import com.picone.core.domain.interactors.property.photo.AddRoomPropertyPhotoInteractor;
import com.picone.core.domain.interactors.property.photo.DeleteRoomPropertyPhotoInteractor;
import com.picone.core.domain.interactors.property.photo.GetAllRoomPropertyPhotosForPropertyIdInteractor;
import com.picone.core.domain.interactors.property.pointOfInterest.AddRoomPropertyPointOfInterestInteractor;
import com.picone.core.domain.interactors.property.pointOfInterest.GetAllRoomPointOfInterestForPropertyIdInteractor;

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

    @Provides
    static AddRoomPropertyInteractor provideAddRoomProperty(@ApplicationContext Context context){
        return new AddRoomPropertyInteractor(providePropertyDataSource(context));
    }

    @Provides
    static AddRoomPropertyPointOfInterestInteractor provideAddRoomPropertyPointOfInterest(@ApplicationContext Context context){
        return new AddRoomPropertyPointOfInterestInteractor(providePropertyDataSource(context));
    }

    @Provides
    static AddRoomPropertyPhotoInteractor provideAddRoomPropertyPhoto(@ApplicationContext Context context){
        return  new AddRoomPropertyPhotoInteractor(providePropertyDataSource(context));
    }

    @Provides
    static DeleteRoomPropertyPhotoInteractor provideDeleteRoomPropertyPhoto(@ApplicationContext Context context){
        return new DeleteRoomPropertyPhotoInteractor(providePropertyDataSource(context));
    }

    @Provides
    static UpdateRoomPropertyInteractor provideUpdateRoomProperty(@ApplicationContext Context context){
        return new UpdateRoomPropertyInteractor(providePropertyDataSource(context));
    }

    @Provides
    static GetPropertyLocationInteractor provideGetPropertyLocation(@ApplicationContext Context context){
        return new GetPropertyLocationInteractor(providePropertyDataSource(context));
    }

    @Provides
    static AddPropertyLocationInteractor provideAddPropertyLocation(@ApplicationContext Context context){
        return new AddPropertyLocationInteractor(providePropertyDataSource(context));
    }
}

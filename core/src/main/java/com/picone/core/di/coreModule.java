package com.picone.core.di;


import android.content.Context;

import com.picone.core.data.RealEstateManagerRoomDatabase;
import com.picone.core.data.property.PlaceServiceDaoImpl;
import com.picone.core.data.property.PropertyDaoImpl;
import com.picone.core.data.property.PropertyRepository;
import com.picone.core.data.realEstateAgent.RealEstateAgentDaoImpl;
import com.picone.core.data.realEstateAgent.RealEstateAgentRepository;
import com.picone.core.data.service.RetrofitClient;
import com.picone.core.domain.interactors.agent.GetAgentInteractor;
import com.picone.core.domain.interactors.property.AddPropertyInteractor;
import com.picone.core.domain.interactors.property.GetAllPropertiesInteractor;
import com.picone.core.domain.interactors.property.UpdatePropertyInteractor;
import com.picone.core.domain.interactors.property.location.AddPropertyLocationInteractor;
import com.picone.core.domain.interactors.property.location.GetPropertyLocationInteractor;
import com.picone.core.domain.interactors.property.photo.AddPropertyPhotoInteractor;
import com.picone.core.domain.interactors.property.photo.DeletePropertyPhotoInteractor;
import com.picone.core.domain.interactors.property.photo.GetAllPropertyPhotosForPropertyIdInteractor;
import com.picone.core.domain.interactors.property.places.GetPropertyLocationForAddressInteractor;
import com.picone.core.domain.interactors.property.pointOfInterest.AddPropertyPointOfInterestInteractor;
import com.picone.core.domain.interactors.property.pointOfInterest.GetAllPointOfInterestForPropertyIdInteractor;

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

    @Singleton
    @Provides
    static RetrofitClient provideRetrofitClient(){return new RetrofitClient();}

    //--------------------------------------DAO-----------------------------------------------------

    @Provides
    static RealEstateAgentDaoImpl provideRealEstateManagerDao(@ApplicationContext Context context){
        return new RealEstateAgentDaoImpl(provideRealEstateManagerRoomDatabase(context));
    }

    @Provides
    static PropertyDaoImpl providePropertyDao(@ApplicationContext Context context){
        return new PropertyDaoImpl(provideRealEstateManagerRoomDatabase(context));
    }

    @Provides
    static PlaceServiceDaoImpl providePlaceService(){
        return new PlaceServiceDaoImpl(provideRetrofitClient());
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
        return new PropertyRepository(providePropertyDao(context),providePlaceService());
    }

    //--------------------------------------REAL ESTATE AGENT INTERACTORS-----------------------------------------------------

    @Provides
    static GetAgentInteractor provideGetAllRoomAgents(@ApplicationContext Context context){
        return new GetAgentInteractor(provideRealEstateAgentDataSource(context));
    }

    //--------------------------------------PROPERTY INTERACTORS-----------------------------------------------------

    @Provides
    static GetAllPropertiesInteractor provideGetAllRoomProperties(@ApplicationContext Context context){
        return new GetAllPropertiesInteractor(providePropertyDataSource(context));
    }

    @Provides
    static GetAllPointOfInterestForPropertyIdInteractor provideGetAllRoomPointOfInterestForPropertyId(@ApplicationContext Context context){
        return new GetAllPointOfInterestForPropertyIdInteractor(providePropertyDataSource(context));
    }

    @Provides
    static GetAllPropertyPhotosForPropertyIdInteractor provideGetAllPropertyPhotosForPropertyId(@ApplicationContext Context context){
        return new GetAllPropertyPhotosForPropertyIdInteractor(providePropertyDataSource(context));
    }

    @Provides
    static AddPropertyInteractor provideAddRoomProperty(@ApplicationContext Context context){
        return new AddPropertyInteractor(providePropertyDataSource(context));
    }

    @Provides
    static AddPropertyPointOfInterestInteractor provideAddRoomPropertyPointOfInterest(@ApplicationContext Context context){
        return new AddPropertyPointOfInterestInteractor(providePropertyDataSource(context));
    }

    @Provides
    static AddPropertyPhotoInteractor provideAddRoomPropertyPhoto(@ApplicationContext Context context){
        return  new AddPropertyPhotoInteractor(providePropertyDataSource(context));
    }

    @Provides
    static DeletePropertyPhotoInteractor provideDeleteRoomPropertyPhoto(@ApplicationContext Context context){
        return new DeletePropertyPhotoInteractor(providePropertyDataSource(context));
    }

    @Provides
    static UpdatePropertyInteractor provideUpdateRoomProperty(@ApplicationContext Context context){
        return new UpdatePropertyInteractor(providePropertyDataSource(context));
    }

    @Provides
    static GetPropertyLocationInteractor provideGetPropertyLocation(@ApplicationContext Context context){
        return new GetPropertyLocationInteractor(providePropertyDataSource(context));
    }

    @Provides
    static AddPropertyLocationInteractor provideAddPropertyLocation(@ApplicationContext Context context){
        return new AddPropertyLocationInteractor(providePropertyDataSource(context));
    }

    //place interactor

    @Provides
    static GetPropertyLocationForAddressInteractor provideGetPropertyLocationForAddress(@ApplicationContext Context context){
        return new GetPropertyLocationForAddressInteractor(providePropertyDataSource(context));
    }
}

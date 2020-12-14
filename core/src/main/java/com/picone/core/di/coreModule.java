package com.picone.core.di;


import android.content.Context;

import androidx.room.PrimaryKey;

import com.picone.core.data.RealEstateManagerDatabase;

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
}

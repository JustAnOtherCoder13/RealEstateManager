package com.openclassrooms.realestatemanager.presentation.di;

import com.openclassrooms.realestatemanager.presentation.ui.main.BaseFragment;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityComponent;

@InstallIn(ActivityComponent.class)
@Module
public class PresentationModule {

    @Provides
    static BaseFragment provideBaseFragment() { return new BaseFragment() {}; }
}

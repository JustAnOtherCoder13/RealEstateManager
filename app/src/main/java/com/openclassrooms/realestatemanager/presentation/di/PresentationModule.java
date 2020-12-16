package com.openclassrooms.realestatemanager.presentation.di;

import com.openclassrooms.realestatemanager.presentation.ui.main.BaseFragment;
import com.picone.core.utils.SchedulerProvider;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityComponent;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InstallIn(ActivityComponent.class)
@Module
public class PresentationModule {

    @Provides
    static BaseFragment provideBaseFragment() { return new BaseFragment() {}; }

    @Provides
    static SchedulerProvider provideSchedulerProvider(){return new SchedulerProvider(Schedulers.io(), AndroidSchedulers.mainThread());}
}

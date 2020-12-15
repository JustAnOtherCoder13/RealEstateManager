package com.openclassrooms.realestatemanager.presentation.viewModels;

import androidx.lifecycle.ViewModel;

import com.picone.core.domain.interactors.agent.GetAllRoomAgentInteractor;
import com.picone.core.domain.interactors.property.GetAllPointOfInterestForPropertyIdInteractor;
import com.picone.core.domain.interactors.property.GetAllRoomPropertiesInteractor;
import com.picone.core.utils.SchedulerProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public abstract class BaseViewModel extends ViewModel {

    @Inject
    protected SchedulerProvider schedulerProvider;

    protected CompositeDisposable compositeDisposable = new CompositeDisposable();


    //------------------------REAL ESTATE AGENT INTERACTORS----------------------------

    protected GetAllRoomAgentInteractor getAllRoomAgentInteractor;

    //------------------------PROPERTY INTERACTORS----------------------------

    protected GetAllRoomPropertiesInteractor getAllRoomPropertiesInteractor;
    protected GetAllPointOfInterestForPropertyIdInteractor getAllPointOfInterestForPropertyIdInteractor;


    @Override
    protected void onCleared() {
        compositeDisposable.clear();
        super.onCleared();
    }
}

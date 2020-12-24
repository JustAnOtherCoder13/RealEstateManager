package com.openclassrooms.realestatemanager.presentation.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.presentation.utils.ErrorHandler;
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
import com.picone.core.utils.SchedulerProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public abstract class BaseViewModel extends ViewModel {

    @Inject
    protected SchedulerProvider schedulerProvider;


    protected CompositeDisposable compositeDisposable = new CompositeDisposable();
    protected MutableLiveData<ErrorHandler> errorState = new MutableLiveData<>(ErrorHandler.NO_ERROR);

    //------------------------REAL ESTATE AGENT INTERACTORS----------------------------

    protected GetAllRoomAgentInteractor getAllRoomAgentInteractor;

    //------------------------PROPERTY INTERACTORS----------------------------

    protected GetAllRoomPropertiesInteractor getAllRoomPropertiesInteractor;
    protected GetAllRoomPointOfInterestForPropertyIdInteractor getAllRoomPointOfInterestForPropertyIdInteractor;
    protected GetAllRoomPropertyPhotosForPropertyIdInteractor getAllRoomPropertyPhotosForPropertyIdInteractor;
    protected AddRoomPropertyInteractor addRoomPropertyInteractor;
    protected AddRoomPropertyPointOfInterestInteractor addRoomPropertyPointOfInterestInteractor;
    protected AddRoomPropertyPhotoInteractor addRoomPropertyPhotoInteractor;
    protected DeleteRoomPropertyPhotoInteractor deleteRoomPropertyPhotoInteractor;
    protected UpdateRoomPropertyInteractor updateRoomPropertyInteractor;
    protected GetPropertyLocationInteractor getPropertyLocationInteractor;
    protected AddPropertyLocationInteractor addPropertyLocationInteractor;

    @Override
    protected void onCleared() {
        compositeDisposable.clear();
        super.onCleared();
    }

    protected void checkException() {
        errorState.postValue(ErrorHandler.ON_ERROR);
    }
}

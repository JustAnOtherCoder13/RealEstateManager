package com.openclassrooms.realestatemanager.presentation.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.presentation.utils.ErrorHandler;
import com.picone.core.domain.interactors.agent.GetAgentInteractor;
import com.picone.core.domain.interactors.property.AddPropertyInteractor;
import com.picone.core.domain.interactors.property.GetAllPropertiesInteractor;
import com.picone.core.domain.interactors.property.UpdatePropertyInteractor;
import com.picone.core.domain.interactors.property.location.AddPropertyLocationInteractor;
import com.picone.core.domain.interactors.property.location.GetPropertyLocationInteractor;
import com.picone.core.domain.interactors.property.photo.AddPropertyPhotoInteractor;
import com.picone.core.domain.interactors.property.photo.DeletePropertyPhotoInteractor;
import com.picone.core.domain.interactors.property.photo.GetAllPropertyPhotosForPropertyIdInteractor;
import com.picone.core.domain.interactors.property.pointOfInterest.AddPropertyPointOfInterestInteractor;
import com.picone.core.domain.interactors.property.pointOfInterest.GetAllPointOfInterestForPropertyIdInteractor;
import com.picone.core.utils.SchedulerProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public abstract class BaseViewModel extends ViewModel {

    @Inject
    protected SchedulerProvider schedulerProvider;


    protected CompositeDisposable compositeDisposable = new CompositeDisposable();
    protected MutableLiveData<ErrorHandler> errorState = new MutableLiveData<>(ErrorHandler.NO_ERROR);

    //------------------------REAL ESTATE AGENT INTERACTORS----------------------------

    protected GetAgentInteractor getAgentInteractor;

    //------------------------PROPERTY INTERACTORS----------------------------

    protected GetAllPropertiesInteractor getAllPropertiesInteractor;
    protected GetAllPointOfInterestForPropertyIdInteractor getAllPointOfInterestForPropertyIdInteractor;
    protected GetAllPropertyPhotosForPropertyIdInteractor getAllPropertyPhotosForPropertyIdInteractor;
    protected AddPropertyInteractor addPropertyInteractor;
    protected AddPropertyPointOfInterestInteractor addPropertyPointOfInterestInteractor;
    protected AddPropertyPhotoInteractor addPropertyPhotoInteractor;
    protected DeletePropertyPhotoInteractor deletePropertyPhotoInteractor;
    protected UpdatePropertyInteractor updatePropertyInteractor;
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

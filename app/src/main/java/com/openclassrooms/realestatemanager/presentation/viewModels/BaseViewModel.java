package com.openclassrooms.realestatemanager.presentation.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.presentation.utils.ErrorHandler;
import com.picone.core.domain.interactors.agent.GetAgentInteractor;
import com.picone.core.domain.interactors.property.AddPropertyInteractor;
import com.picone.core.domain.interactors.property.GetAllPropertiesInteractor;
import com.picone.core.domain.interactors.property.UpdatePropertyInteractor;
import com.picone.core.domain.interactors.property.location.AddPropertyLocationInteractor;
import com.picone.core.domain.interactors.property.location.GetAllRegionsForAllPropertiesInteractor;
import com.picone.core.domain.interactors.property.location.UpdatePropertyLocationInteractor;
import com.picone.core.domain.interactors.property.maps.GetNearBySearchForPropertyLocationInteractor;
import com.picone.core.domain.interactors.property.maps.GetPropertyLocationForAddressInteractor;
import com.picone.core.domain.interactors.property.media.AddPropertyMediaInteractor;
import com.picone.core.domain.interactors.property.media.DeletePropertyMediaInteractor;
import com.picone.core.domain.interactors.property.pointOfInterest.AddPropertyPointOfInterestInteractor;
import com.picone.core.domain.interactors.property.pointOfInterest.DeletePointOfInterestInteractor;
import com.picone.core.domain.interactors.property.pointOfInterest.GetAllPointOfInterestForPropertyIdInteractor;
import com.picone.core.utils.SchedulerProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public abstract class BaseViewModel extends ViewModel {

    @Inject
    protected SchedulerProvider schedulerProvider;
    protected CompositeDisposable compositeDisposable = new CompositeDisposable();
    protected MutableLiveData<ErrorHandler> errorState = new MutableLiveData<>(ErrorHandler.NO_ERROR);

    public LiveData<ErrorHandler> getErrorState = errorState;
    //------------------------REAL ESTATE AGENT INTERACTORS----------------------------

    protected GetAgentInteractor getAgentInteractor;

    //------------------------PROPERTY INTERACTORS----------------------------

    protected GetAllPropertiesInteractor getAllPropertiesInteractor;
    protected GetAllPointOfInterestForPropertyIdInteractor getAllPointOfInterestForPropertyIdInteractor;
    protected GetAllRegionsForAllPropertiesInteractor getAllRegionsForAllPropertiesInteractor;
    protected AddPropertyInteractor addPropertyInteractor;
    protected AddPropertyPointOfInterestInteractor addPropertyPointOfInterestInteractor;
    protected AddPropertyMediaInteractor addPropertyMediaInteractor;
    protected DeletePropertyMediaInteractor deletePropertyMediaInteractor;
    protected UpdatePropertyInteractor updatePropertyInteractor;
    protected UpdatePropertyLocationInteractor updatePropertyLocationInteractor;
    protected DeletePointOfInterestInteractor deletePointOfInterestInteractor;
    protected AddPropertyLocationInteractor addPropertyLocationInteractor;

    //------------------------PLACE INTERACTORS----------------------------

    protected GetNearBySearchForPropertyLocationInteractor getNearBySearchForPropertyLocationInteractor;
    protected GetPropertyLocationForAddressInteractor getPropertyLocationForAddressInteractor;

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }

    protected void checkException() {
        errorState.postValue(ErrorHandler.ON_ERROR);
    }

    public enum CompletionState {
        START_STATE,
        ADD_PROPERTY_COMPLETE,
        ADD_LOCATION_COMPLETE,
        UPDATE_PROPERTY_COMPLETE,
        ADD_POINT_OF_INTEREST_COMPLETE
    }
}

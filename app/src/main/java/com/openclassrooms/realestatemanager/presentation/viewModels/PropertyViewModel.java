package com.openclassrooms.realestatemanager.presentation.viewModels;

import android.util.Log;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.picone.core.domain.entity.PointOfInterest;
import com.picone.core.domain.entity.Property;
import com.picone.core.domain.interactors.property.GetAllPointOfInterestForPropertyIdInteractor;
import com.picone.core.domain.interactors.property.GetAllRoomPropertiesInteractor;
import com.picone.core.utils.SchedulerProvider;

import java.util.List;

public class PropertyViewModel extends BaseViewModel {

    private MutableLiveData<List<Property>> allPropertiesMutableLD = new MutableLiveData<>();
    private MutableLiveData<List<PointOfInterest>> allPointOfInterestMutableLD = new MutableLiveData<>();


    public LiveData<List<Property>> getAllProperties = allPropertiesMutableLD;
    public LiveData<List<PointOfInterest>> getAllPointOfInterest = allPointOfInterestMutableLD;


    @ViewModelInject
    public PropertyViewModel(GetAllRoomPropertiesInteractor getAllRoomPropertiesInteractor,
    GetAllPointOfInterestForPropertyIdInteractor getAllPointOfInterestForPropertyIdInteractor
    , SchedulerProvider schedulerProvider) {
        this.getAllRoomPropertiesInteractor = getAllRoomPropertiesInteractor;
        this.getAllPointOfInterestForPropertyIdInteractor = getAllPointOfInterestForPropertyIdInteractor;
        this.schedulerProvider = schedulerProvider;
    }

    public void setAllProperties(){
        compositeDisposable.add(
        getAllRoomPropertiesInteractor.getAllProperties()
                .subscribeOn(schedulerProvider.getIo())
                .observeOn(schedulerProvider.getUi())
                .subscribe(properties -> Log.i("TAG", "setAllProperties: "+properties)));
    }

    public void setAllPointOfInterestForProperty(){
        compositeDisposable.add(
                getAllPointOfInterestForPropertyIdInteractor.getAllPointOfInterestForPropertyId(1)
                .observeOn(schedulerProvider.getIo())
                .subscribeOn(schedulerProvider.getUi())
                .subscribe(pointOfInterests -> Log.i("TAG", "setAllPointOfInterestForProperty: "+pointOfInterests)));

    }
}

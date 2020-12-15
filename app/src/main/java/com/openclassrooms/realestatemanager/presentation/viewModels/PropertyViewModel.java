package com.openclassrooms.realestatemanager.presentation.viewModels;

import android.util.Log;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.picone.core.domain.entity.Property;
import com.picone.core.domain.interactors.property.GetAllRoomPropertiesInteractor;
import com.picone.core.utils.SchedulerProvider;

import java.util.List;

public class PropertyViewModel extends BaseViewModel {

    private MutableLiveData<List<Property>> allPropertiesMutableLD = new MutableLiveData<>();

    public LiveData<List<Property>> getAllProperties = allPropertiesMutableLD;

    @ViewModelInject
    public PropertyViewModel(GetAllRoomPropertiesInteractor getAllRoomPropertiesInteractor
    , SchedulerProvider schedulerProvider) {
        this.getAllRoomPropertiesInteractor = getAllRoomPropertiesInteractor;
        this.schedulerProvider = schedulerProvider;
    }

    public void setAllProperties(){
        compositeDisposable.add(
        getAllRoomPropertiesInteractor.getAllProperties()
                .subscribeOn(schedulerProvider.getIo())
                .observeOn(schedulerProvider.getUi())
                .subscribe(properties -> Log.i("TAG", "setAllProperties: "+properties)));
    }
}

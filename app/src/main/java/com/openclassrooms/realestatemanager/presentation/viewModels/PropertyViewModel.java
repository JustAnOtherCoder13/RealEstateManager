package com.openclassrooms.realestatemanager.presentation.viewModels;

import android.util.Log;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.picone.core.domain.entity.PointOfInterest;
import com.picone.core.domain.entity.Property;
import com.picone.core.domain.entity.PropertyPhoto;
import com.picone.core.domain.interactors.property.GetAllRoomPointOfInterestForPropertyIdInteractor;
import com.picone.core.domain.interactors.property.GetAllRoomPropertiesInteractor;
import com.picone.core.domain.interactors.property.GetAllRoomPropertyPhotosForPropertyIdInteractor;
import com.picone.core.utils.SchedulerProvider;

import java.util.List;

public class PropertyViewModel extends BaseViewModel {

    private MutableLiveData<List<Property>> allRoomPropertiesMutableLD = new MutableLiveData<>();
    private MutableLiveData<List<PointOfInterest>> allRoomPointOfInterestMutableLD = new MutableLiveData<>();
    private MutableLiveData<List<PropertyPhoto>> allRoomPhotosMutableLD = new MutableLiveData<>();

    public LiveData<List<Property>> getAllRoomProperties = allRoomPropertiesMutableLD;
    public LiveData<List<PointOfInterest>> getAllRoomPointOfInterest = allRoomPointOfInterestMutableLD;


    @ViewModelInject
    public PropertyViewModel(GetAllRoomPropertiesInteractor getAllRoomPropertiesInteractor
            , GetAllRoomPointOfInterestForPropertyIdInteractor getAllRoomPointOfInterestForPropertyIdInteractor
            , GetAllRoomPropertyPhotosForPropertyIdInteractor getAllRoomPropertyPhotosForPropertyIdInteractor
            , SchedulerProvider schedulerProvider) {
        this.getAllRoomPropertiesInteractor = getAllRoomPropertiesInteractor;
        this.getAllRoomPointOfInterestForPropertyIdInteractor = getAllRoomPointOfInterestForPropertyIdInteractor;
        this.getAllRoomPropertyPhotosForPropertyIdInteractor = getAllRoomPropertyPhotosForPropertyIdInteractor;
        this.schedulerProvider = schedulerProvider;
    }

    public void setAllRoomProperties() {
        compositeDisposable.add(
                getAllRoomPropertiesInteractor.getAllProperties()
                        .subscribeOn(schedulerProvider.getIo())
                        .observeOn(schedulerProvider.getUi())
                        .subscribe(properties -> Log.i("TAG", "setAllProperties: " + properties)));
    }

    public void setAllRoomPointOfInterestForProperty(Property property) {
        compositeDisposable.add(
                getAllRoomPointOfInterestForPropertyIdInteractor.getAllPointOfInterestForPropertyId(property.getId())
                        .observeOn(schedulerProvider.getIo())
                        .subscribeOn(schedulerProvider.getUi())
                        .subscribe(pointOfInterests -> Log.i("TAG", "setAllPointOfInterestForProperty: " + pointOfInterests)));

    }

    public void setAllRoomPhotosForProperty(Property property) {
        compositeDisposable.add(
                getAllRoomPropertyPhotosForPropertyIdInteractor.getAllPhotosForPropertyId(property.getId())
                .subscribeOn(schedulerProvider.getIo())
                .observeOn(schedulerProvider.getUi())
                .subscribe(propertyPhotos -> Log.i("TAG", "setAllRoomPhotosForProperty: "+propertyPhotos)));


    }
}

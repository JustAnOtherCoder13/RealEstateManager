package com.openclassrooms.realestatemanager.presentation.viewModels;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.picone.core.domain.entity.PointOfInterest;
import com.picone.core.domain.entity.Property;
import com.picone.core.domain.entity.PropertyLocation;
import com.picone.core.domain.entity.PropertyPhoto;
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

import java.util.List;

public class PropertyViewModel extends BaseViewModel {

    private MutableLiveData<List<Property>> allRoomPropertiesMutableLD = new MutableLiveData<>();
    private MutableLiveData<List<PointOfInterest>> allRoomPointOfInterestForPropertyMutableLD = new MutableLiveData<>();
    private MutableLiveData<List<PropertyPhoto>> allRoomPhotosForPropertyMutableLD = new MutableLiveData<>();
    private MutableLiveData<Property> selectedPropertyMutableLD = new MutableLiveData<>();
    private MutableLiveData<List<PropertyPhoto>> photosToDeleteMutableLD = new MutableLiveData<>();
    private MutableLiveData<PropertyLocation> propertyLocationForPropertyMutableLd = new MutableLiveData<>();


    public LiveData<List<Property>> getAllRoomProperties = allRoomPropertiesMutableLD;
    public LiveData<List<PointOfInterest>> getAllRoomPointOfInterestForProperty = allRoomPointOfInterestForPropertyMutableLD;
    public LiveData<List<PropertyPhoto>> getAllRoomPropertyPhotosForProperty = allRoomPhotosForPropertyMutableLD;
    public LiveData<Property> getSelectedProperty = selectedPropertyMutableLD;
    public LiveData<List<PropertyPhoto>> getPhotosToDelete = photosToDeleteMutableLD;
    public LiveData<PropertyLocation> getPropertyLocationForProperty = propertyLocationForPropertyMutableLd;

    @ViewModelInject
    public PropertyViewModel(GetAllRoomPropertiesInteractor getAllRoomPropertiesInteractor
            , GetAllRoomPointOfInterestForPropertyIdInteractor getAllRoomPointOfInterestForPropertyIdInteractor
            , GetAllRoomPropertyPhotosForPropertyIdInteractor getAllRoomPropertyPhotosForPropertyIdInteractor
            , AddRoomPropertyInteractor addRoomPropertyInteractor
            , AddRoomPropertyPointOfInterestInteractor addRoomPropertyPointOfInterestInteractor
            , AddRoomPropertyPhotoInteractor addRoomPropertyPhotoInteractor
            , DeleteRoomPropertyPhotoInteractor deleteRoomPropertyPhotoInteractor
            , UpdateRoomPropertyInteractor updateRoomPropertyInteractor
            , GetPropertyLocationInteractor getPropertyLocationInteractor
            , AddPropertyLocationInteractor addPropertyLocationInteractor
            , SchedulerProvider schedulerProvider) {
        this.getAllRoomPropertiesInteractor = getAllRoomPropertiesInteractor;
        this.getAllRoomPointOfInterestForPropertyIdInteractor = getAllRoomPointOfInterestForPropertyIdInteractor;
        this.getAllRoomPropertyPhotosForPropertyIdInteractor = getAllRoomPropertyPhotosForPropertyIdInteractor;
        this.addRoomPropertyInteractor = addRoomPropertyInteractor;
        this.addRoomPropertyPointOfInterestInteractor = addRoomPropertyPointOfInterestInteractor;
        this.addRoomPropertyPhotoInteractor = addRoomPropertyPhotoInteractor;
        this.deleteRoomPropertyPhotoInteractor = deleteRoomPropertyPhotoInteractor;
        this.updateRoomPropertyInteractor = updateRoomPropertyInteractor;
        this.getPropertyLocationInteractor = getPropertyLocationInteractor;
        this.addPropertyLocationInteractor = addPropertyLocationInteractor;
        this.schedulerProvider = schedulerProvider;
    }

    public void setAllRoomProperties() {
        compositeDisposable.add(
                getAllRoomPropertiesInteractor.getAllProperties()
                        .subscribeOn(schedulerProvider.getIo())
                        .observeOn(schedulerProvider.getUi())
                        .subscribe(properties -> allRoomPropertiesMutableLD.postValue(properties)));
    }

    public void setAllRoomPointOfInterestForProperty(Property property) {
        compositeDisposable.add(
                getAllRoomPointOfInterestForPropertyIdInteractor.getAllPointOfInterestForPropertyId(property.getId())
                        .observeOn(schedulerProvider.getIo())
                        .subscribeOn(schedulerProvider.getUi())
                        .subscribe(pointOfInterests -> allRoomPointOfInterestForPropertyMutableLD.postValue(pointOfInterests)));
    }

    public void setAllRoomPhotosForProperty(Property property) {
        compositeDisposable.add(
                getAllRoomPropertyPhotosForPropertyIdInteractor.getAllPhotosForPropertyId(property.getId())
                        .subscribeOn(schedulerProvider.getIo())
                        .observeOn(schedulerProvider.getUi())
                        .subscribe(propertyPhotos -> allRoomPhotosForPropertyMutableLD.setValue(propertyPhotos)));
    }

    public void setPropertyLocationForProperty(Property property){
        compositeDisposable.add(
                getPropertyLocationInteractor.getPropertyLocationForPropertyId(property.getId())
                .subscribeOn(schedulerProvider.getIo())
                .observeOn(schedulerProvider.getUi())
                .subscribe(propertyLocation -> propertyLocationForPropertyMutableLd.setValue(propertyLocation) ));
    }

    public void addRoomProperty(Property property) {
        compositeDisposable.add(
                addRoomPropertyInteractor.addRoomProperty(property)
                        .subscribeOn(schedulerProvider.getIo())
                        .observeOn(schedulerProvider.getUi())
                        .andThen(getAllRoomPropertiesInteractor.getAllProperties())
                        .subscribe(properties -> allRoomPropertiesMutableLD.postValue(properties), throwable -> checkException()));
    }

    public void addRoomPropertyPointOfInterest(PointOfInterest pointOfInterest) {
        compositeDisposable.add(
                addRoomPropertyPointOfInterestInteractor.addRoomPropertyPointOfInterest(pointOfInterest)
                        .subscribeOn(schedulerProvider.getIo())
                        .observeOn(schedulerProvider.getUi())
                        .andThen(getAllRoomPointOfInterestForPropertyIdInteractor.getAllPointOfInterestForPropertyId(pointOfInterest.getPropertyId()))
                        .subscribe(pointOfInterests -> allRoomPointOfInterestForPropertyMutableLD.postValue(pointOfInterests), throwable -> checkException()));
    }

    public void addRoomPropertyPhoto(PropertyPhoto propertyPhoto) {
        compositeDisposable.add(
                addRoomPropertyPhotoInteractor.addRoomPropertyPhoto(propertyPhoto)
                        .subscribeOn(schedulerProvider.getIo())
                        .observeOn(schedulerProvider.getUi())
                        .andThen(getAllRoomPropertyPhotosForPropertyIdInteractor.getAllPhotosForPropertyId(propertyPhoto.getPropertyId()))
                        .subscribe(propertyPhotos -> allRoomPhotosForPropertyMutableLD.postValue(propertyPhotos), throwable -> checkException()));
    }

    public void deleteRoomPropertyPhoto(PropertyPhoto propertyPhoto) {
        final int propertyId = propertyPhoto.getPropertyId();
        compositeDisposable.add(
                deleteRoomPropertyPhotoInteractor.deleteRoomPropertyPhoto(propertyPhoto)
                        .subscribeOn(schedulerProvider.getIo())
                        .observeOn(schedulerProvider.getUi())
                        .andThen(getAllRoomPropertyPhotosForPropertyIdInteractor.getAllPhotosForPropertyId(propertyId))
                        .subscribe(propertyPhotos -> allRoomPhotosForPropertyMutableLD.postValue(propertyPhotos)));
    }

    public void updateRoomProperty(Property property) {
        compositeDisposable.add(
                updateRoomPropertyInteractor.updateRoomProperty(property)
                        .subscribeOn(schedulerProvider.getIo())
                        .observeOn(schedulerProvider.getUi())
                        .andThen(getAllRoomPropertiesInteractor.getAllProperties())
                        .subscribe(properties -> allRoomPropertiesMutableLD.postValue(properties), throwable -> checkException()));
    }

    public void setSelectedProperty(Property property) {
        selectedPropertyMutableLD.setValue(property);
    }

    public void setPhotosToDelete(List<PropertyPhoto> photosToDelete) {
        photosToDeleteMutableLD.setValue(photosToDelete);
    }
}

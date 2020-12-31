package com.openclassrooms.realestatemanager.presentation.viewModels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.maps.model.LatLng;
import com.picone.core.domain.entity.PointOfInterest;
import com.picone.core.domain.entity.Property;
import com.picone.core.domain.entity.PropertyLocation;
import com.picone.core.domain.entity.PropertyPhoto;
import com.picone.core.domain.interactors.property.AddPropertyInteractor;
import com.picone.core.domain.interactors.property.GetAllPropertiesInteractor;
import com.picone.core.domain.interactors.property.UpdatePropertyInteractor;
import com.picone.core.domain.interactors.property.location.AddPropertyLocationInteractor;
import com.picone.core.domain.interactors.property.location.GetPropertyLocationInteractor;
import com.picone.core.domain.interactors.property.maps.GetNearBySchoolForPropertyLocation;
import com.picone.core.domain.interactors.property.maps.GetPropertyLocationForAddressInteractor;
import com.picone.core.domain.interactors.property.maps.GetStaticMapForLatLngInteractor;
import com.picone.core.domain.interactors.property.photo.AddPropertyPhotoInteractor;
import com.picone.core.domain.interactors.property.photo.DeletePropertyPhotoInteractor;
import com.picone.core.domain.interactors.property.photo.GetAllPropertyPhotosForPropertyIdInteractor;
import com.picone.core.domain.interactors.property.pointOfInterest.AddPropertyPointOfInterestInteractor;
import com.picone.core.domain.interactors.property.pointOfInterest.GetAllPointOfInterestForPropertyIdInteractor;
import com.picone.core.utils.SchedulerProvider;

import java.util.List;

import static com.picone.core.utils.ConstantParameters.MAPS_KEY;

public class PropertyViewModel extends BaseViewModel {

    private MutableLiveData<CompletionState> completionStateMutableLD = new MutableLiveData<>(CompletionState.START_STATE);
    private MutableLiveData<List<Property>> allPropertiesMutableLD = new MutableLiveData<>();
    private MutableLiveData<List<PointOfInterest>> allPointOfInterestForPropertyMutableLD = new MutableLiveData<>();
    private MutableLiveData<List<PropertyPhoto>> allPhotosForPropertyMutableLD = new MutableLiveData<>();
    private MutableLiveData<Property> selectedPropertyMutableLD = new MutableLiveData<>(new Property());
    private MutableLiveData<List<PropertyPhoto>> photosToDeleteMutableLD = new MutableLiveData<>();
    private MutableLiveData<PropertyLocation> propertyLocationForPropertyMutableLd = new MutableLiveData<>();
    private MutableLiveData<PropertyLocation> locationForAddressMutableLD = new MutableLiveData<>();

    public LiveData<CompletionState> getCompletionState = completionStateMutableLD;
    public LiveData<List<Property>> getAllProperties = allPropertiesMutableLD;
    public LiveData<List<PointOfInterest>> getAllPointOfInterestForProperty = allPointOfInterestForPropertyMutableLD;
    public LiveData<List<PropertyPhoto>> getAllPropertyPhotosForProperty = allPhotosForPropertyMutableLD;
    public LiveData<Property> getSelectedProperty = selectedPropertyMutableLD;
    public LiveData<List<PropertyPhoto>> getPhotosToDelete = photosToDeleteMutableLD;
    public LiveData<PropertyLocation> getPropertyLocationForProperty = propertyLocationForPropertyMutableLd;
    public LiveData<PropertyLocation> getLocationForAddress = locationForAddressMutableLD;

    @ViewModelInject
    public PropertyViewModel(GetAllPropertiesInteractor getAllPropertiesInteractor
            , GetAllPointOfInterestForPropertyIdInteractor getAllPointOfInterestForPropertyIdInteractor
            , GetAllPropertyPhotosForPropertyIdInteractor getAllPropertyPhotosForPropertyIdInteractor
            , AddPropertyInteractor addPropertyInteractor
            , AddPropertyPointOfInterestInteractor addPropertyPointOfInterestInteractor
            , AddPropertyPhotoInteractor addPropertyPhotoInteractor
            , DeletePropertyPhotoInteractor deletePropertyPhotoInteractor
            , UpdatePropertyInteractor updatePropertyInteractor
            , GetPropertyLocationInteractor getPropertyLocationInteractor
            , AddPropertyLocationInteractor addPropertyLocationInteractor
            , GetPropertyLocationForAddressInteractor getPropertyLocationForAddressInteractor
            , GetStaticMapForLatLngInteractor getStaticMapForLatLngInteractor
            , GetNearBySchoolForPropertyLocation getNearBySchoolForPropertyLocation
            , SchedulerProvider schedulerProvider) {
        this.getAllPropertiesInteractor = getAllPropertiesInteractor;
        this.getAllPointOfInterestForPropertyIdInteractor = getAllPointOfInterestForPropertyIdInteractor;
        this.getAllPropertyPhotosForPropertyIdInteractor = getAllPropertyPhotosForPropertyIdInteractor;
        this.addPropertyInteractor = addPropertyInteractor;
        this.addPropertyPointOfInterestInteractor = addPropertyPointOfInterestInteractor;
        this.addPropertyPhotoInteractor = addPropertyPhotoInteractor;
        this.deletePropertyPhotoInteractor = deletePropertyPhotoInteractor;
        this.updatePropertyInteractor = updatePropertyInteractor;
        this.getPropertyLocationInteractor = getPropertyLocationInteractor;
        this.addPropertyLocationInteractor = addPropertyLocationInteractor;
        this.getPropertyLocationForAddressInteractor = getPropertyLocationForAddressInteractor;
        this.getStaticMapForLatLngInteractor = getStaticMapForLatLngInteractor;
        this.getNearBySchoolForPropertyLocation = getNearBySchoolForPropertyLocation;
        this.schedulerProvider = schedulerProvider;
    }

    //___________________________________SETTERS______________________________________

    public void setSelectedProperty(Property property) {
        selectedPropertyMutableLD.setValue(property);
    }

    public void setPhotosToDelete(List<PropertyPhoto> photosToDelete) {
        photosToDeleteMutableLD.setValue(photosToDelete);
    }

    public void resetCompletionState() {
        completionStateMutableLD.setValue(CompletionState.START_STATE);
    }

    public void setAllProperties() {
        compositeDisposable.add(
                getAllPropertiesInteractor.getAllProperties()
                        .subscribeOn(schedulerProvider.getIo())
                        .observeOn(schedulerProvider.getUi())
                        .subscribe(properties -> allPropertiesMutableLD.postValue(properties)));
    }

    public void setAllPointOfInterestForProperty(@NonNull Property property) {
        compositeDisposable.add(
                getAllPointOfInterestForPropertyIdInteractor.getAllPointOfInterestForPropertyId(property.getId())
                        .observeOn(schedulerProvider.getIo())
                        .subscribeOn(schedulerProvider.getUi())
                        .subscribe(pointOfInterests -> allPointOfInterestForPropertyMutableLD.postValue(pointOfInterests)));
    }

    public void setAllPhotosForProperty(@NonNull Property property) {
        compositeDisposable.add(
                getAllPropertyPhotosForPropertyIdInteractor.getAllPhotosForPropertyId(property.getId())
                        .subscribeOn(schedulerProvider.getIo())
                        .observeOn(schedulerProvider.getUi())
                        .subscribe(propertyPhotos -> allPhotosForPropertyMutableLD.setValue(propertyPhotos)));
    }

    public void setPropertyLocationForProperty(@NonNull Property property) {
        compositeDisposable.add(
                getPropertyLocationInteractor.getPropertyLocationForPropertyId(property.getId())
                        .subscribeOn(schedulerProvider.getIo())
                        .observeOn(schedulerProvider.getUi())
                        .subscribe(propertyLocation -> propertyLocationForPropertyMutableLd.setValue(propertyLocation)));
    }

    //___________________________________PROPERTY__________________________________

    public void addProperty(Property property) {
        compositeDisposable.add(
                addPropertyInteractor.addRoomProperty(property)
                        .subscribeOn(schedulerProvider.getIo())
                        .observeOn(schedulerProvider.getUi())
                        .doOnComplete(() -> completionStateMutableLD.setValue(CompletionState.ADD_PROPERTY_COMPLETE))
                        .andThen(getAllPropertiesInteractor.getAllProperties())
                        .subscribe(properties -> allPropertiesMutableLD.postValue(properties), throwable -> checkException()));
    }

    public void updateProperty(Property property) {
        compositeDisposable.add(
                updatePropertyInteractor.updateProperty(property)
                        .subscribeOn(schedulerProvider.getIo())
                        .observeOn(schedulerProvider.getUi())
                        .doOnComplete(() -> completionStateMutableLD.setValue(CompletionState.UPDATE_PROPERTY_COMPLETE))
                        .andThen(getAllPropertiesInteractor.getAllProperties())
                        .subscribe(properties -> allPropertiesMutableLD.postValue(properties), throwable -> checkException()));
    }

    //___________________________________PROPERTY LOCATION__________________________________


    public void addPropertyLocationForProperty(PropertyLocation propertyLocation) {
        compositeDisposable.add(
                addPropertyLocationInteractor.addPropertyLocation(propertyLocation)
                        .subscribeOn(schedulerProvider.getIo())
                        .observeOn(schedulerProvider.getUi())
                        .doOnComplete(() -> completionStateMutableLD.setValue(CompletionState.ADD_LOCATION_COMPLETE))
                        .subscribe(() -> setSelectedProperty(new Property()), throwable -> checkException()));
    }

    //___________________________________PROPERTY POINT OF INTEREST__________________________________

    public void addPropertyPointOfInterest(PointOfInterest pointOfInterest) {
        compositeDisposable.add(
                addPropertyPointOfInterestInteractor.addRoomPropertyPointOfInterest(pointOfInterest)
                        .subscribeOn(schedulerProvider.getIo())
                        .observeOn(schedulerProvider.getUi())
                        .andThen(getAllPointOfInterestForPropertyIdInteractor.getAllPointOfInterestForPropertyId(pointOfInterest.getPropertyId()))
                        .subscribe(pointOfInterests -> allPointOfInterestForPropertyMutableLD.postValue(pointOfInterests), throwable -> checkException()));
    }

    //___________________________________PROPERTY PHOTO__________________________________

    public void addPropertyPhoto(PropertyPhoto propertyPhoto) {
        compositeDisposable.add(
                addPropertyPhotoInteractor.addRoomPropertyPhoto(propertyPhoto)
                        .subscribeOn(schedulerProvider.getIo())
                        .observeOn(schedulerProvider.getUi())
                        .andThen(getAllPropertyPhotosForPropertyIdInteractor.getAllPhotosForPropertyId(propertyPhoto.getPropertyId()))
                        .subscribe(propertyPhotos -> allPhotosForPropertyMutableLD.postValue(propertyPhotos), throwable -> checkException()));
    }

    public void deletePropertyPhoto(@NonNull PropertyPhoto propertyPhoto) {
        final int propertyId = propertyPhoto.getPropertyId();
        compositeDisposable.add(
                deletePropertyPhotoInteractor.deleteRoomPropertyPhoto(propertyPhoto)
                        .subscribeOn(schedulerProvider.getIo())
                        .observeOn(schedulerProvider.getUi())
                        .andThen(getAllPropertyPhotosForPropertyIdInteractor.getAllPhotosForPropertyId(propertyId))
                        .subscribe(propertyPhotos -> allPhotosForPropertyMutableLD.postValue(propertyPhotos)));
    }

    //___________________________________MAPS__________________________________

    public void setPropertyLocationForPropertyAddress(@NonNull Property property) {
        if (property.getAddress() != null)
            compositeDisposable.add(
                    getPropertyLocationForAddressInteractor.getPropertyLocationForAddress(property, MAPS_KEY)
                            .subscribeOn(schedulerProvider.getIo())
                            .observeOn(schedulerProvider.getUi())
                            .subscribe(propertyLocation -> locationForAddressMutableLD.setValue(propertyLocation)));
        else locationForAddressMutableLD.setValue(new PropertyLocation());
    }

    public void setStaticMapForLatLng(LatLng latLng) {
        compositeDisposable.add(
                getStaticMapForLatLngInteractor.getStaticMapForLatLng(latLng, MAPS_KEY)
                        .subscribeOn(schedulerProvider.getIo())
                        .observeOn(schedulerProvider.getUi())
                        .subscribe()
        );
    }

    public void setNearBySearchForPropertyLocation(int propertyId) {
        compositeDisposable.add(
                getPropertyLocationInteractor.getPropertyLocationForPropertyId(propertyId)
                        .subscribeOn(schedulerProvider.getIo())
                        .observeOn(schedulerProvider.getUi())
                        .flatMap(propertyLocation ->
                                getNearBySchoolForPropertyLocation.getNearBySchoolForPropertyLocation(propertyLocation, MAPS_KEY))
                        .subscribe(nearBySearch -> {
                            Log.i("TAG", "setNearBySearchForPropertyLocation: "+nearBySearch.getNearBySearchResults());
                        },throwable -> checkException())
        );
    }
}

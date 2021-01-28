package com.openclassrooms.realestatemanager.presentation.viewModels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.picone.core.domain.entity.PointOfInterest;
import com.picone.core.domain.entity.Property;
import com.picone.core.domain.entity.PropertyInformation;
import com.picone.core.domain.entity.PropertyLocation;
import com.picone.core.domain.entity.PropertyPhoto;
import com.picone.core.domain.interactors.property.AddPropertyInteractor;
import com.picone.core.domain.interactors.property.GetAllPropertiesInteractor;
import com.picone.core.domain.interactors.property.UpdatePropertyInteractor;
import com.picone.core.domain.interactors.property.location.AddPropertyLocationInteractor;
import com.picone.core.domain.interactors.property.location.GetAllRegionsForAllPropertiesInteractor;
import com.picone.core.domain.interactors.property.location.GetPropertyLocationInteractor;
import com.picone.core.domain.interactors.property.location.UpdatePropertyLocationInteractor;
import com.picone.core.domain.interactors.property.maps.GetNearBySearchForPropertyLocationInteractor;
import com.picone.core.domain.interactors.property.maps.GetPropertyLocationForAddressInteractor;
import com.picone.core.domain.interactors.property.photo.AddPropertyPhotoInteractor;
import com.picone.core.domain.interactors.property.photo.DeletePropertyPhotoInteractor;
import com.picone.core.domain.interactors.property.photo.GetAllPhotosForAllPropertiesInteractor;
import com.picone.core.domain.interactors.property.photo.GetAllPropertyPhotosForPropertyIdInteractor;
import com.picone.core.domain.interactors.property.pointOfInterest.AddPropertyPointOfInterestInteractor;
import com.picone.core.domain.interactors.property.pointOfInterest.DeletePointOfInterestInteractor;
import com.picone.core.domain.interactors.property.pointOfInterest.GetAllPointOfInterestForPropertyIdInteractor;
import com.picone.core.domain.interactors.property.pointOfInterest.GetAllPointOfInterestsForAllPropertiesInteractor;
import com.picone.core.utils.SchedulerProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import io.reactivex.Observable;

import static com.openclassrooms.realestatemanager.presentation.viewModels.BaseViewModel.CompletionState.ADD_POINT_OF_INTEREST_COMPLETE;
import static com.picone.core.utils.ConstantParameters.MAPS_KEY;

public class PropertyViewModel extends BaseViewModel {

    private MutableLiveData<List<PointOfInterest>> mapsPointOfInterestForPropertyMutableLD = new MutableLiveData<>();
    private MutableLiveData<CompletionState> completionStateMutableLD = new MutableLiveData<>(CompletionState.START_STATE);
    private MutableLiveData<List<Property>> allPropertiesMutableLD = new MutableLiveData<>();
    private MutableLiveData<List<Property>> allPropertiesMutableLD_ = new MutableLiveData<>();

    private MutableLiveData<List<PointOfInterest>> allPointOfInterestForPropertyMutableLD = new MutableLiveData<>(new ArrayList<>());
    private MutableLiveData<List<PointOfInterest>> allPointsOfInterestForAllPropertiesMutableLD = new MutableLiveData<>();
    private MutableLiveData<List<PropertyPhoto>> allPhotosForPropertyMutableLD = new MutableLiveData<>();
    private MutableLiveData<List<PropertyPhoto>> allPhotosForAllPropertiesMutableLD = new MutableLiveData<>();
    private MutableLiveData<PropertyInformation> selectedPropertyMutableLD = new MutableLiveData<>(new PropertyInformation());
    private MutableLiveData<Property> selectedPropertyMutableLD_ = new MutableLiveData<>(new Property());

    private MutableLiveData<List<PropertyPhoto>> photosToDeleteMutableLD = new MutableLiveData<>();
    private MutableLiveData<PropertyLocation> propertyLocationForPropertyMutableLd = new MutableLiveData<>();
    private MutableLiveData<PropertyLocation> locationForAddressMutableLD = new MutableLiveData<>();
    private MutableLiveData<Boolean> isDataLoadingMutableLD = new MutableLiveData<>();
    private MutableLiveData<List<String>> knownRegionsMutableLD = new MutableLiveData<>(new ArrayList<>());

    public LiveData<List<String>> getKnownRegions = knownRegionsMutableLD;
    public LiveData<List<PointOfInterest>> getMapsPointOfInterest = mapsPointOfInterestForPropertyMutableLD;
    public LiveData<CompletionState> getCompletionState = completionStateMutableLD;
    public LiveData<List<Property>> getAllProperties = allPropertiesMutableLD;
    public LiveData<List<Property>> getAllProperties_ = allPropertiesMutableLD_;

    public LiveData<List<PointOfInterest>> getAllPointOfInterestForAllProperties = allPointsOfInterestForAllPropertiesMutableLD;
    public LiveData<List<PointOfInterest>> getAllPointOfInterestForProperty = allPointOfInterestForPropertyMutableLD;
    public LiveData<List<PropertyPhoto>> getAllPropertyPhotosForProperty = allPhotosForPropertyMutableLD;
    public LiveData<List<PropertyPhoto>> getAllPhotosForAllProperties = allPhotosForAllPropertiesMutableLD;
    public LiveData<PropertyInformation> getSelectedProperty = selectedPropertyMutableLD;
    public LiveData<Property> getSelectedProperty_ = selectedPropertyMutableLD_;

    public LiveData<List<PropertyPhoto>> getPhotosToDelete = photosToDeleteMutableLD;
    public LiveData<PropertyLocation> getPropertyLocationForProperty = propertyLocationForPropertyMutableLd;
    public LiveData<PropertyLocation> getLocationForAddress = locationForAddressMutableLD;
    public LiveData<Boolean> isDataLoading = isDataLoadingMutableLD;


    @ViewModelInject
    public PropertyViewModel(GetAllPropertiesInteractor getAllPropertiesInteractor
            , GetAllPointOfInterestForPropertyIdInteractor getAllPointOfInterestForPropertyIdInteractor
            , GetAllPointOfInterestsForAllPropertiesInteractor getAllPointOfInterestsForAllPropertiesInteractor
            , GetAllPropertyPhotosForPropertyIdInteractor getAllPropertyPhotosForPropertyIdInteractor
            , GetAllPhotosForAllPropertiesInteractor getAllPhotosForAllPropertiesInteractor
            , GetAllRegionsForAllPropertiesInteractor getAllRegionsForAllPropertiesInteractor
            , AddPropertyInteractor addPropertyInteractor
            , AddPropertyPointOfInterestInteractor addPropertyPointOfInterestInteractor
            , AddPropertyPhotoInteractor addPropertyPhotoInteractor
            , DeletePropertyPhotoInteractor deletePropertyPhotoInteractor
            , UpdatePropertyInteractor updatePropertyInteractor
            , GetPropertyLocationInteractor getPropertyLocationInteractor
            , AddPropertyLocationInteractor addPropertyLocationInteractor
            , GetPropertyLocationForAddressInteractor getPropertyLocationForAddressInteractor
            , GetNearBySearchForPropertyLocationInteractor getNearBySearchForPropertyLocationInteractor
            , UpdatePropertyLocationInteractor updatePropertyLocationInteractor
            , DeletePointOfInterestInteractor deletePointOfInterestInteractor
            , SchedulerProvider schedulerProvider) {
        this.getAllPropertiesInteractor = getAllPropertiesInteractor;
        this.getAllPointOfInterestForPropertyIdInteractor = getAllPointOfInterestForPropertyIdInteractor;
        this.getAllPointOfInterestsForAllPropertiesInteractor = getAllPointOfInterestsForAllPropertiesInteractor;
        this.getAllPropertyPhotosForPropertyIdInteractor = getAllPropertyPhotosForPropertyIdInteractor;
        this.getAllPhotosForAllPropertiesInteractor = getAllPhotosForAllPropertiesInteractor;
        this.getAllRegionsForAllPropertiesInteractor = getAllRegionsForAllPropertiesInteractor;
        this.addPropertyInteractor = addPropertyInteractor;
        this.addPropertyPointOfInterestInteractor = addPropertyPointOfInterestInteractor;
        this.addPropertyPhotoInteractor = addPropertyPhotoInteractor;
        this.deletePropertyPhotoInteractor = deletePropertyPhotoInteractor;
        this.updatePropertyInteractor = updatePropertyInteractor;
        this.getPropertyLocationInteractor = getPropertyLocationInteractor;
        this.addPropertyLocationInteractor = addPropertyLocationInteractor;
        this.getPropertyLocationForAddressInteractor = getPropertyLocationForAddressInteractor;
        this.getNearBySearchForPropertyLocationInteractor = getNearBySearchForPropertyLocationInteractor;
        this.updatePropertyLocationInteractor = updatePropertyLocationInteractor;
        this.deletePointOfInterestInteractor = deletePointOfInterestInteractor;
        this.schedulerProvider = schedulerProvider;
    }

    //___________________________________SETTERS______________________________________

    private MutableLiveData<Locale> localeMutableLD = new MutableLiveData<>();
    public LiveData<Locale> getLocale = localeMutableLD;


    public void setAllPropertiesAndAllValues() {
        compositeDisposable.add(
                getAllPropertiesInteractor.getAllProperties()
                        .subscribeOn(schedulerProvider.getIo())
                        .observeOn(schedulerProvider.getUi())
                        .subscribe(properties ->
                                allPropertiesMutableLD_.setValue(properties))
        );

    }

    public void setLocale(Locale locale) {
        localeMutableLD.setValue(locale);
    }

    public void setFilteredProperty(List<Property> filteredProperties) {
        allPropertiesMutableLD.setValue(filteredProperties);
    }

    public void setSelectedProperty(PropertyInformation propertyInformation) {
        selectedPropertyMutableLD.setValue(propertyInformation);
    }

    public void setSelectedProperty_(Property property_) {
        selectedPropertyMutableLD_.setValue(property_);
    }

    public void setPhotosToDelete(List<PropertyPhoto> photosToDelete) {
        photosToDeleteMutableLD.postValue(photosToDelete);
    }

    public void resetCompletionState() {
        completionStateMutableLD.setValue(CompletionState.START_STATE);
    }

    public void setAllProperties() {
        compositeDisposable.add(
                getAllPropertiesInteractor.getAllProperties()
                        .subscribeOn(schedulerProvider.getIo())
                        .observeOn(schedulerProvider.getUi())
                        .subscribe(properties -> {
                            allPropertiesMutableLD.setValue(properties);
                        }));
    }


    public void setAllPointOfInterestForProperty(@NonNull Property property) {
        compositeDisposable.add(
                getAllPointOfInterestForPropertyIdInteractor.getAllPointOfInterestForPropertyId(property.propertyInformation.getId())
                        .subscribeOn(schedulerProvider.getIo())
                        .observeOn(schedulerProvider.getUi())
                        .subscribe(pointOfInterests -> allPointOfInterestForPropertyMutableLD.setValue(pointOfInterests)));
    }

    public void setAllPointOfInterestForAllProperties() {
        compositeDisposable.add(
                getAllPointOfInterestsForAllPropertiesInteractor.getAllPointsOfInterestForAllProperties()
                        .subscribeOn(schedulerProvider.getIo())
                        .observeOn(schedulerProvider.getUi())
                        .subscribe(pointOfInterests ->
                                allPointsOfInterestForAllPropertiesMutableLD.setValue(pointOfInterests))
        );
    }

    public void setAllPhotosForProperty(@NonNull PropertyInformation propertyInformation) {
        compositeDisposable.add(
                getAllPropertyPhotosForPropertyIdInteractor.getAllPhotosForPropertyId(propertyInformation.getId())
                        .subscribeOn(schedulerProvider.getIo())
                        .observeOn(schedulerProvider.getUi())
                        .subscribe(propertyPhotos -> allPhotosForPropertyMutableLD.setValue(propertyPhotos)));
    }

    public void setAllPhotoForAllProperties() {
        compositeDisposable.add(
                getAllPhotosForAllPropertiesInteractor.getAllPhotosForAllProperties()
                        .subscribeOn(schedulerProvider.getIo())
                        .observeOn(schedulerProvider.getUi())
                        .subscribe(propertyPhotos ->
                                allPhotosForAllPropertiesMutableLD.setValue(propertyPhotos))
        );
    }

    public void setPropertyLocationForProperty(@NonNull PropertyInformation propertyInformation) {
        compositeDisposable.add(
                getPropertyLocationInteractor.getPropertyLocationForPropertyId(propertyInformation.getId())
                        .subscribeOn(schedulerProvider.getIo())
                        .observeOn(schedulerProvider.getUi())
                        .subscribe(propertyLocation -> propertyLocationForPropertyMutableLd.setValue(propertyLocation)));
    }

    public void setAllRegionForAllProperties() {
        compositeDisposable.add(
                getAllRegionsForAllPropertiesInteractor.getAllRegionsForAllProperties()
                        .subscribeOn(schedulerProvider.getIo())
                        .observeOn(schedulerProvider.getUi())
                        .subscribe(allRegions ->
                                knownRegionsMutableLD.setValue(allRegions))
        );
    }
    //___________________________________PROPERTY__________________________________

    public void addProperty(Property property) {
        compositeDisposable.add(
                addPropertyInteractor.addProperty(property.propertyInformation)
                        .subscribeOn(schedulerProvider.getIo())
                        .observeOn(schedulerProvider.getUi())
                        .doOnComplete(() -> completionStateMutableLD.postValue(CompletionState.ADD_PROPERTY_COMPLETE))
                        .andThen(getAllPropertiesInteractor.getAllProperties())
                        .subscribe(properties -> allPropertiesMutableLD.postValue(properties)
                                , throwable -> checkException()));
    }

    public void updatePropertyInformation(Property property) {
        compositeDisposable.add(
                updatePropertyInteractor.updateProperty(property.propertyInformation)
                        .subscribeOn(schedulerProvider.getIo())
                        .observeOn(schedulerProvider.getUi())
                        .doOnComplete(() -> completionStateMutableLD.setValue(CompletionState.UPDATE_PROPERTY_COMPLETE))
                        .andThen(getAllPropertiesInteractor.getAllProperties())
                        .subscribe(properties -> {
                            Log.d("TAG", "updatePropertyInformation: " + properties);
                            allPropertiesMutableLD.postValue(properties);
                        }, throwable -> checkException()));
    }

    //___________________________________PROPERTY LOCATION__________________________________


    public void addPropertyLocationForProperty(PropertyLocation propertyLocation) {
        compositeDisposable.add(
                addPropertyLocationInteractor.addPropertyLocation(propertyLocation)
                        .subscribeOn(schedulerProvider.getIo())
                        .observeOn(schedulerProvider.getUi())
                        .doOnComplete(() -> completionStateMutableLD.setValue(CompletionState.ADD_LOCATION_COMPLETE))
                        .subscribe(() -> setSelectedProperty(new PropertyInformation()), throwable -> checkException()));
    }

    public void updatePropertyLocation(@NonNull PropertyLocation propertyLocation) {
        compositeDisposable.add(
                updatePropertyLocationInteractor.updatePropertyLocation(propertyLocation)
                        .subscribeOn(schedulerProvider.getIo())
                        .observeOn(schedulerProvider.getUi())
                        .andThen(getAllPropertiesInteractor.getAllProperties())
                        .subscribe(properties -> selectedPropertyMutableLD_.postValue(new Property()), throwable -> Log.e("TAG", "updatePropertyLocation: " + throwable)));

    }
    //___________________________________PROPERTY POINT OF INTEREST__________________________________

    public void addPropertyPointOfInterest(@NonNull List<PointOfInterest> pointOfInterests) {
        compositeDisposable.add(
                Observable.fromIterable(pointOfInterests)
                        .subscribeOn(schedulerProvider.getIo())
                        .flatMapCompletable(pointOfInterest -> addPropertyPointOfInterestInteractor.addPropertyPointOfInterest(pointOfInterest))
                        .doOnComplete(()->completionStateMutableLD.postValue(ADD_POINT_OF_INTEREST_COMPLETE))
                        .observeOn(schedulerProvider.getUi())
                        .subscribe(() -> {
                        }, throwable -> checkException()));

    }

    public void updatePointOfInterest(@NonNull List<PointOfInterest> mapsPointOfInterest) {
        compositeDisposable.add(
                Observable.fromIterable(Objects.requireNonNull(allPointOfInterestForPropertyMutableLD.getValue()))
                        .subscribeOn(schedulerProvider.getIo())
                        .flatMapCompletable(pointOfInterest ->
                                deletePointOfInterestInteractor.deletePropertyPointOfInterest(pointOfInterest))
                        .andThen(Observable.fromIterable(mapsPointOfInterest))
                        .flatMapCompletable(pointOfInterest ->
                                addPropertyPointOfInterestInteractor.addPropertyPointOfInterest(pointOfInterest))
                        .doOnComplete(()->completionStateMutableLD.postValue(ADD_POINT_OF_INTEREST_COMPLETE))
                        .observeOn(schedulerProvider.getUi())
                        .subscribe(() -> {
                        }, throwable -> checkException())
        );
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

    public void deleteSelectedPhotosForProperty(@NonNull List<PropertyPhoto> propertyPhotos) {
        compositeDisposable.add(
                deletePropertyPhotoInteractor.deleteSelectedPhotoForProperty(propertyPhotos)
                        .subscribeOn(schedulerProvider.getIo())
                        .observeOn(schedulerProvider.getUi())
                        .subscribe(() -> {
                        }, throwable -> checkException())
        );
    }

    //___________________________________MAPS__________________________________

    public void setPropertyLocationForPropertyAddress(@NonNull Property property) {
        Log.i("TAG", "setPropertyLocationForPropertyAddress: " + property.propertyLocation);
        if (property.propertyLocation!=null)
        compositeDisposable.add(
                getPropertyLocationForAddressInteractor.getPropertyLocationForAddress(property, MAPS_KEY)
                        .subscribeOn(schedulerProvider.getIo())
                        .observeOn(schedulerProvider.getUi())
                        .subscribe(propertyLocation ->
                                        locationForAddressMutableLD.setValue(propertyLocation)
                                , throwable -> checkException()));
        else locationForAddressMutableLD.setValue(new PropertyLocation());
    }

    public void setNearBySearchForPropertyLocation(PropertyLocation propertyLocation) {
        compositeDisposable.add(
                getNearBySearchForPropertyLocationInteractor.getNearBySearchForPropertyLocation(propertyLocation, MAPS_KEY)
                        .subscribeOn(schedulerProvider.getIo())
                        .observeOn(schedulerProvider.getUi())
                        .subscribe(pointOfInterests ->
                                        mapsPointOfInterestForPropertyMutableLD.setValue(pointOfInterests)
                                , throwable -> checkException()));
    }

}

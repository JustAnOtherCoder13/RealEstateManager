package com.openclassrooms.realestatemanager.presentation.viewModels;

import androidx.annotation.NonNull;
import androidx.hilt.Assisted;
import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.picone.core.domain.entity.PointOfInterest;
import com.picone.core.domain.entity.Property;
import com.picone.core.domain.entity.PropertyLocation;
import com.picone.core.domain.entity.PropertyMedia;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observable;

import static com.openclassrooms.realestatemanager.presentation.viewModels.BaseViewModel.CompletionState.ADD_POINT_OF_INTEREST_COMPLETE;
import static com.openclassrooms.realestatemanager.presentation.viewModels.BaseViewModel.CompletionState.DELETE_POINT_OF_INTEREST_COMPLETE;
import static com.picone.core.utils.ConstantParameters.MAPS_KEY;

public class PropertyViewModel extends BaseViewModel {

    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    //needed to save viewModel state with hilt injection
    private final SavedStateHandle savedStateHandle;

    private MutableLiveData<List<PointOfInterest>> mapsPointOfInterestForPropertyMutableLD = new MutableLiveData<>();
    private MutableLiveData<CompletionState> completionStateMutableLD = new MutableLiveData<>(CompletionState.START_STATE);
    private MutableLiveData<List<Property>> allPropertiesMutableLD = new MutableLiveData<>();
    private MutableLiveData<List<PointOfInterest>> storedPointOfInterestForPropertyMutableLD = new MutableLiveData<>(new ArrayList<>());
    private MutableLiveData<Property> selectedPropertyMutableLD = new MutableLiveData<>(new Property());
    private MutableLiveData<List<PropertyMedia>> photosToDeleteMutableLD = new MutableLiveData<>();
    private MutableLiveData<PropertyLocation> locationForAddressMutableLD = new MutableLiveData<>();
    private MutableLiveData<Boolean> isDataLoadingMutableLD = new MutableLiveData<>();
    private MutableLiveData<List<String>> knownRegionsMutableLD = new MutableLiveData<>(new ArrayList<>());
    private MutableLiveData<Locale> localeMutableLD = new MutableLiveData<>();
    private MutableLiveData<List<PropertyMedia>> mediasToAdMutableLD = new MutableLiveData<>(new ArrayList<>());
    private MutableLiveData<Property> newPropertyToPersistMutableLD = new MutableLiveData<>();


    public LiveData<Property> getNewProperty = newPropertyToPersistMutableLD;
    public LiveData<Locale> getLocale = localeMutableLD;
    public LiveData<List<String>> getKnownRegions = knownRegionsMutableLD;
    public LiveData<List<PointOfInterest>> getMapsPointOfInterest = mapsPointOfInterestForPropertyMutableLD;
    public LiveData<CompletionState> getCompletionState = completionStateMutableLD;
    public LiveData<List<Property>> getAllProperties = allPropertiesMutableLD;
    public LiveData<Property> getSelectedProperty = selectedPropertyMutableLD;
    public LiveData<List<PropertyMedia>> getPhotosToDelete = photosToDeleteMutableLD;
    public LiveData<PropertyLocation> getLocationForAddress = locationForAddressMutableLD;
    public LiveData<Boolean> isDataLoading = isDataLoadingMutableLD;
    public LiveData<List<PropertyMedia>> getMediasToAdd = mediasToAdMutableLD;

    @ViewModelInject
    public PropertyViewModel(GetAllPropertiesInteractor getAllPropertiesInteractor
            , GetAllPointOfInterestForPropertyIdInteractor getAllPointOfInterestForPropertyIdInteractor
            , GetAllRegionsForAllPropertiesInteractor getAllRegionsForAllPropertiesInteractor
            , AddPropertyInteractor addPropertyInteractor
            , AddPropertyPointOfInterestInteractor addPropertyPointOfInterestInteractor
            , AddPropertyMediaInteractor addPropertyMediaInteractor
            , DeletePropertyMediaInteractor deletePropertyMediaInteractor
            , UpdatePropertyInteractor updatePropertyInteractor
            , AddPropertyLocationInteractor addPropertyLocationInteractor
            , GetPropertyLocationForAddressInteractor getPropertyLocationForAddressInteractor
            , GetNearBySearchForPropertyLocationInteractor getNearBySearchForPropertyLocationInteractor
            , UpdatePropertyLocationInteractor updatePropertyLocationInteractor
            , DeletePointOfInterestInteractor deletePointOfInterestInteractor
            , @Assisted SavedStateHandle savedStateHandle
            , SchedulerProvider schedulerProvider) {
        this.getAllPropertiesInteractor = getAllPropertiesInteractor;
        this.getAllPointOfInterestForPropertyIdInteractor = getAllPointOfInterestForPropertyIdInteractor;
        this.getAllRegionsForAllPropertiesInteractor = getAllRegionsForAllPropertiesInteractor;
        this.addPropertyInteractor = addPropertyInteractor;
        this.addPropertyPointOfInterestInteractor = addPropertyPointOfInterestInteractor;
        this.addPropertyMediaInteractor = addPropertyMediaInteractor;
        this.deletePropertyMediaInteractor = deletePropertyMediaInteractor;
        this.updatePropertyInteractor = updatePropertyInteractor;
        this.addPropertyLocationInteractor = addPropertyLocationInteractor;
        this.getPropertyLocationForAddressInteractor = getPropertyLocationForAddressInteractor;
        this.getNearBySearchForPropertyLocationInteractor = getNearBySearchForPropertyLocationInteractor;
        this.updatePropertyLocationInteractor = updatePropertyLocationInteractor;
        this.deletePointOfInterestInteractor = deletePointOfInterestInteractor;
        this.schedulerProvider = schedulerProvider;
        this.savedStateHandle = savedStateHandle;
    }

    //___________________________________SETTERS______________________________________

    public void setLocale(Locale locale) {
        localeMutableLD.setValue(locale);
    }

    public void setNewProperty(Property newProperty){
        newPropertyToPersistMutableLD.setValue(newProperty);
    }

    public void setFilteredProperty(List<Property> filteredProperties) {
        allPropertiesMutableLD.setValue(filteredProperties);
    }

    public void setSelectedProperty(Property property) {
        selectedPropertyMutableLD.setValue(property);
    }

    public void setMediasToDelete(List<PropertyMedia> mediasToDelete) {
        photosToDeleteMutableLD.postValue(mediasToDelete);
    }

    public void setMediaToAdd(List<PropertyMedia > mediasToAdd){
        mediasToAdMutableLD.setValue(mediasToAdd);
    }

    public void resetCompletionState() {
        completionStateMutableLD.setValue(CompletionState.START_STATE);
    }

    public void setAllProperties() {
        compositeDisposable.add(
                getAllPropertiesInteractor.getAllProperties()
                        .subscribeOn(schedulerProvider.getIo())
                        .observeOn(schedulerProvider.getUi())
                        .subscribe(properties ->
                                allPropertiesMutableLD.setValue(properties)));
    }

    public void setAllPointOfInterestForProperty(@NonNull Property property) {
        compositeDisposable.add(
                getAllPointOfInterestForPropertyIdInteractor.getAllPointOfInterestForPropertyId(property.propertyInformation.getId())
                        .subscribeOn(schedulerProvider.getIo())
                        .observeOn(schedulerProvider.getUi())
                        .subscribe(pointOfInterests -> storedPointOfInterestForPropertyMutableLD.setValue(pointOfInterests)));
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

    public void addProperty(@NonNull Property property) {
        compositeDisposable.add(
                addPropertyInteractor.addProperty(property.propertyInformation)
                        .subscribeOn(schedulerProvider.getIo())
                        .observeOn(schedulerProvider.getUi())
                        .doOnSubscribe(__ -> isDataLoadingMutableLD.postValue(true))
                        .doAfterTerminate(() -> completionStateMutableLD.postValue(CompletionState.ADD_PROPERTY_COMPLETE))
                        .subscribe(() -> {
                        }, throwable -> checkException()));
    }

    public void updatePropertyInformation(@NonNull Property property) {
        compositeDisposable.add(
                updatePropertyInteractor.updateProperty(property.propertyInformation)
                        .subscribeOn(schedulerProvider.getIo())
                        .observeOn(schedulerProvider.getUi())
                        .doOnSubscribe(__ -> isDataLoadingMutableLD.postValue(true))
                        .doOnComplete(() -> completionStateMutableLD.setValue(CompletionState.UPDATE_PROPERTY_COMPLETE))
                        .subscribe(() -> {
                        }, throwable -> checkException()));
    }

    //___________________________________PROPERTY LOCATION__________________________________

    public void addPropertyLocationForProperty(PropertyLocation propertyLocation) {
        compositeDisposable.add(
                addPropertyLocationInteractor.addPropertyLocation(propertyLocation)
                        .subscribeOn(schedulerProvider.getIo())
                        .observeOn(schedulerProvider.getUi())
                        .subscribe(() -> {
                        }, throwable -> checkException()));
    }

    public void updatePropertyLocation(@NonNull PropertyLocation propertyLocation) {
        compositeDisposable.add(
                updatePropertyLocationInteractor.updatePropertyLocation(propertyLocation)
                        .subscribeOn(schedulerProvider.getIo())
                        .observeOn(schedulerProvider.getUi())
                        .andThen(Observable.just(new Property()))
                        .subscribe(newProperty -> selectedPropertyMutableLD.postValue(newProperty),
                                throwable -> checkException()));

    }
    //___________________________________PROPERTY POINT OF INTEREST__________________________________

    public void addPropertyPointOfInterest(@NonNull List<PointOfInterest> pointOfInterests) {
        compositeDisposable.add(
                Observable.fromIterable(pointOfInterests)
                        .subscribeOn(schedulerProvider.getIo())
                        .flatMapCompletable(pointOfInterest -> addPropertyPointOfInterestInteractor.addPropertyPointOfInterest(pointOfInterest))
                        .doOnComplete(() -> completionStateMutableLD.postValue(ADD_POINT_OF_INTEREST_COMPLETE))
                        .doOnTerminate(() -> isDataLoadingMutableLD.postValue(false))
                        .observeOn(schedulerProvider.getUi())
                        .subscribe(() -> {
                        }, throwable -> checkException()));

    }

    public void deletePointsOfInterestForProperty(@NonNull Property property) {
        compositeDisposable.add(
                Observable.fromIterable(property.pointOfInterests)
                        .subscribeOn(schedulerProvider.getIo())
                        .flatMapCompletable(pointOfInterest -> deletePointOfInterestInteractor.deletePropertyPointOfInterest(pointOfInterest))
                        .observeOn(schedulerProvider.getUi())
                        .doOnComplete(() -> completionStateMutableLD.postValue(DELETE_POINT_OF_INTEREST_COMPLETE))
                        .subscribe(()-> {
                        }, throwable -> checkException())
        );
    }

    public void updatePointOfInterest(@NonNull List<PointOfInterest> mapsPointOfInterest) {
        compositeDisposable.add(
                Observable.fromIterable(mapsPointOfInterest)
                        .subscribeOn(schedulerProvider.getIo())
                        .flatMapCompletable(pointOfInterest ->
                                addPropertyPointOfInterestInteractor.addPropertyPointOfInterest(pointOfInterest))
                        .observeOn(schedulerProvider.getUi())
                        .doOnComplete(() -> completionStateMutableLD.postValue(ADD_POINT_OF_INTEREST_COMPLETE))
                        .doOnTerminate(() -> isDataLoadingMutableLD.postValue(false))
                        .subscribe(() -> {
                        }, throwable -> checkException())
        );
    }


    //___________________________________PROPERTY PHOTO__________________________________

    public void addPropertyMedia(PropertyMedia propertyMedia) {
        compositeDisposable.add(
                addPropertyMediaInteractor.addPropertyMedia(propertyMedia)
                        .subscribeOn(schedulerProvider.getIo())
                        .observeOn(schedulerProvider.getUi())
                        .subscribe(() -> {
                        }, throwable -> checkException())
        );
    }

    public void deleteSelectedMediaForProperty(@NonNull List<PropertyMedia> propertyMedia) {
        compositeDisposable.add(
                deletePropertyMediaInteractor.deleteSelectedMediaForProperty(propertyMedia)
                        .subscribeOn(schedulerProvider.getIo())
                        .observeOn(schedulerProvider.getUi())
                        .subscribe(() -> {
                        }, throwable -> checkException())
        );
    }

    //___________________________________MAPS__________________________________

    public void setPropertyLocationForPropertyAddress(@NonNull Property property) {
        if (property.propertyLocation != null)
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

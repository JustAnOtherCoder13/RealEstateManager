package com.openclassrooms.realestatemanager.presentation.viewModels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.picone.core.domain.entity.PointOfInterest;
import com.picone.core.domain.entity.Property;
import com.picone.core.domain.entity.PropertyFactory;
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
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

import static com.picone.core.utils.ConstantParameters.MAPS_KEY;

public class PropertyViewModel extends BaseViewModel {

    private MutableLiveData<List<PointOfInterest>> mapsPointOfInterestForPropertyMutableLD = new MutableLiveData<>();
    private MutableLiveData<CompletionState> completionStateMutableLD = new MutableLiveData<>(CompletionState.START_STATE);
    private MutableLiveData<List<Property>> allPropertiesMutableLD = new MutableLiveData<>();
    private MutableLiveData<List<PropertyFactory>> allPropertiesMutableLD_ = new MutableLiveData<>();

    private MutableLiveData<List<PointOfInterest>> allPointOfInterestForPropertyMutableLD = new MutableLiveData<>(new ArrayList<>());
    private MutableLiveData<List<PointOfInterest>> allPointsOfInterestForAllPropertiesMutableLD = new MutableLiveData<>();
    private MutableLiveData<List<PropertyPhoto>> allPhotosForPropertyMutableLD = new MutableLiveData<>();
    private MutableLiveData<List<PropertyPhoto>> allPhotosForAllPropertiesMutableLD = new MutableLiveData<>();
    private MutableLiveData<Property> selectedPropertyMutableLD = new MutableLiveData<>(new Property());
    private MutableLiveData<PropertyFactory> selectedPropertyMutableLD_ = new MutableLiveData<>(new PropertyFactory());

    private MutableLiveData<List<PropertyPhoto>> photosToDeleteMutableLD = new MutableLiveData<>();
    private MutableLiveData<PropertyLocation> propertyLocationForPropertyMutableLd = new MutableLiveData<>();
    private MutableLiveData<PropertyLocation> locationForAddressMutableLD = new MutableLiveData<>();
    private MutableLiveData<Boolean> isDataLoadingMutableLD = new MutableLiveData<>();
    private MutableLiveData<List<String>> knownRegionsMutableLD = new MutableLiveData<>(new ArrayList<>());

    public LiveData<List<String>> getKnownRegions = knownRegionsMutableLD;
    public LiveData<List<PointOfInterest>> getMapsPointOfInterest = mapsPointOfInterestForPropertyMutableLD;
    public LiveData<CompletionState> getCompletionState = completionStateMutableLD;
    public LiveData<List<Property>> getAllProperties = allPropertiesMutableLD;
    public LiveData<List<PropertyFactory>> getAllProperties_ = allPropertiesMutableLD_;

    public LiveData<List<PointOfInterest>> getAllPointOfInterestForAllProperties = allPointsOfInterestForAllPropertiesMutableLD;
    public LiveData<List<PointOfInterest>> getAllPointOfInterestForProperty = allPointOfInterestForPropertyMutableLD;
    public LiveData<List<PropertyPhoto>> getAllPropertyPhotosForProperty = allPhotosForPropertyMutableLD;
    public LiveData<List<PropertyPhoto>> getAllPhotosForAllProperties = allPhotosForAllPropertiesMutableLD;
    public LiveData<Property> getSelectedProperty = selectedPropertyMutableLD;
    public LiveData<PropertyFactory> getSelectedProperty_ = selectedPropertyMutableLD_;

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


    private MutableLiveData<PropertyFactory> propertyAndAllValuesMutableLD = new MutableLiveData<>();
    private MutableLiveData<List<PropertyFactory>> allPropertiesAndAllValues = new MutableLiveData<>();
    public LiveData<PropertyFactory> getPropertyAndAllValues = propertyAndAllValuesMutableLD;
    public LiveData<List<PropertyFactory>> getAllPropertiesAndAllValues = allPropertiesAndAllValues;

    public void setPropertyAndAllValues(Property property) {
        compositeDisposable.add(
                getAllPropertiesInteractor.getPropertyAndAllValues(property.getId())
                        .subscribeOn(schedulerProvider.getIo())
                        .observeOn(schedulerProvider.getUi())
                        .subscribe(propertyAndAllValues -> propertyAndAllValuesMutableLD.setValue(propertyAndAllValues))
        );
    }

    public void setAllPropertiesAndAllValues() {
        compositeDisposable.add(
                getAllPropertiesInteractor.getAllProperties_()
                        .subscribeOn(schedulerProvider.getIo())
                        .observeOn(schedulerProvider.getUi())
                        .subscribe(propertyFactories ->
                                allPropertiesMutableLD_.setValue(propertyFactories))
        );

    }

    public void setLocale(Locale locale) {
        localeMutableLD.setValue(locale);
    }

    public void setFilteredProperty(List<Property> filteredProperties) {
        allPropertiesMutableLD.setValue(filteredProperties);
    }

    public void setSelectedProperty(Property property) {
        selectedPropertyMutableLD.setValue(property);
    }

    public void setSelectedProperty_(PropertyFactory property_){
        selectedPropertyMutableLD_.setValue(property_);
    }
    public void setSelectedProperty_(Property property) {
        compositeDisposable.add(
                getAllPropertiesInteractor.getPropertyAndAllValues(property.getId())
                        .subscribeOn(schedulerProvider.getIo())
                        .observeOn(schedulerProvider.getUi())
                        .flatMap(new Function<PropertyFactory, ObservableSource<PropertyFactory>>() {
                                       @Override
                                       public ObservableSource<PropertyFactory> apply(PropertyFactory propertyFactory) throws Exception {
                                           Log.i("TAG", "apply: "+propertyFactory.property.getAddress());
                                           return Observable.just(propertyFactory);
                                       }
                                   })
                .subscribe(propertyFactory ->selectedPropertyMutableLD_.setValue(propertyFactory)) );
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
                            for (Property property : properties)
                                addPhotoAndPointOfInterestForProperty(property);
                        }));
    }


    public void setAllPointOfInterestForProperty(@NonNull Property property) {
        if (property.getAddress() != null)
            compositeDisposable.add(
                    getAllPointOfInterestForPropertyIdInteractor.getAllPointOfInterestForPropertyId(property.getId())
                            .subscribeOn(schedulerProvider.getIo())
                            .observeOn(schedulerProvider.getUi())
                            .subscribe(pointOfInterests -> allPointOfInterestForPropertyMutableLD.setValue(pointOfInterests)));
        else allPointOfInterestForPropertyMutableLD.setValue(new ArrayList<>());
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

    public void setAllPhotosForProperty(@NonNull Property property) {
        compositeDisposable.add(
                getAllPropertyPhotosForPropertyIdInteractor.getAllPhotosForPropertyId(property.getId())
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

    public void setPropertyLocationForProperty(@NonNull Property property) {
        compositeDisposable.add(
                getPropertyLocationInteractor.getPropertyLocationForPropertyId(property.getId())
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
                addPropertyInteractor.addRoomProperty(property)
                        .subscribeOn(schedulerProvider.getIo())
                        .observeOn(schedulerProvider.getUi())
                        .doOnComplete(() -> completionStateMutableLD.postValue(CompletionState.ADD_PROPERTY_COMPLETE))
                        .andThen(getAllPropertiesInteractor.getAllProperties())
                        .subscribe(properties -> allPropertiesMutableLD.postValue(properties)
                                , throwable -> checkException()));
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

    public void updatePropertyLocation(@NonNull PropertyLocation propertyLocation) {
        compositeDisposable.add(
                updatePropertyLocationInteractor.updatePropertyLocation(propertyLocation)
                        .subscribeOn(schedulerProvider.getIo())
                        .observeOn(schedulerProvider.getUi())
                        .andThen(getAllPropertiesInteractor.getAllProperties())
                        .subscribe(properties -> selectedPropertyMutableLD.postValue(new Property()), throwable -> Log.e("TAG", "updatePropertyLocation: " + throwable)));

    }
    //___________________________________PROPERTY POINT OF INTEREST__________________________________

    public void addPropertyPointOfInterest(@NonNull List<PointOfInterest> pointOfInterests) {
        if (!pointOfInterests.isEmpty()) {
            for (int i = 0; i < pointOfInterests.size(); i++) {
                compositeDisposable.add(
                        addPropertyPointOfInterestInteractor.addPropertyPointOfInterest(pointOfInterests.get(i))
                                .subscribeOn(schedulerProvider.getIo())
                                .observeOn(schedulerProvider.getUi())
                                .subscribe(() -> {
                                }, throwable -> checkException()));

                if (pointOfInterests.size() - 1 == i) {
                    isDataLoadingMutableLD.setValue(false);
                    completionStateMutableLD.setValue(CompletionState.ADD_POINT_OF_INTEREST_COMPLETE);
                    pointOfInterests.clear();
                    break;
                }
            }
        } else completionStateMutableLD.setValue(CompletionState.ADD_POINT_OF_INTEREST_COMPLETE);

    }

    public void updatePointOfInterest(@NonNull List<PointOfInterest> pointOfInterests) {
        for (int i = 0; i < Objects.requireNonNull(allPointOfInterestForPropertyMutableLD.getValue()).size(); i++) {
            compositeDisposable.add(
                    deletePointOfInterestInteractor.deletePropertyPointOfInterest(allPointOfInterestForPropertyMutableLD.getValue().get(i))
                            .subscribeOn(schedulerProvider.getIo())
                            .observeOn(schedulerProvider.getUi())
                            .subscribe(() -> {
                            }, throwable -> checkException())
            );
            if (i == allPointOfInterestForPropertyMutableLD.getValue().size() - 1) {
                allPointOfInterestForPropertyMutableLD.getValue().clear();
                addPropertyPointOfInterest(pointOfInterests);
            }
        }
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

    public void resetPhotoForProperty(@NonNull Property property) {
        compositeDisposable.add(
                deletePropertyPhotoInteractor.deleteAllPhotoForProperty(property.getId())
                        .subscribeOn(schedulerProvider.getIo())
                        .observeOn(schedulerProvider.getUi())
                        .subscribe(() -> {
                        }, throwable -> checkException())
        );
    }

    //___________________________________MAPS__________________________________

    public void setPropertyLocationForPropertyAddress(@NonNull Property property) {
        if (property.getAddress() != null)
            compositeDisposable.add(
                    getPropertyLocationForAddressInteractor.getPropertyLocationForAddress(property, MAPS_KEY)
                            .subscribeOn(schedulerProvider.getIo())
                            .observeOn(schedulerProvider.getUi())
                            .subscribe(propertyLocation -> locationForAddressMutableLD.setValue(propertyLocation), throwable -> Log.e("TAG", "setPropertyLocationForPropertyAddress: " + throwable)));
    }

    public void setNearBySearchForPropertyLocation(PropertyLocation propertyLocation) {
        compositeDisposable.add(
                getNearBySearchForPropertyLocationInteractor.getNearBySearchForPropertyLocation(propertyLocation, MAPS_KEY)
                        .subscribeOn(schedulerProvider.getIo())
                        .observeOn(schedulerProvider.getUi())
                        .subscribe(pointOfInterests -> mapsPointOfInterestForPropertyMutableLD.setValue(pointOfInterests)));
    }


    private void addPhotoAndPointOfInterestForProperty(@NonNull Property property) {
        compositeDisposable.add(
                getAllPropertyPhotosForPropertyIdInteractor.getAllPhotosForPropertyId(property.getId())
                        .subscribeOn(schedulerProvider.getIo())
                        .observeOn(schedulerProvider.getUi())
                        .flatMap(propertyPhotos -> {
                            property.setPropertyPhotos(propertyPhotos);
                            return getAllPointOfInterestForPropertyIdInteractor.getAllPointOfInterestForPropertyId(property.getId());
                        })
                        .subscribe(property::setPointOfInterests)
        );
    }
}

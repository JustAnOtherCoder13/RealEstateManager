package com.picone.core.domain.interactors.property;

import androidx.annotation.NonNull;

import com.picone.core.data.property.PropertyRepository;
import com.picone.core.domain.entity.Property;
import com.picone.core.domain.entity.PropertyInformation;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

public class GetAllPropertiesInteractor extends PropertyBaseInteractor {
    private List<Property> allProperties;

    public GetAllPropertiesInteractor(PropertyRepository propertyDataSource) {
        super(propertyDataSource);
    }

    public Observable<List<Property>> getAllProperties_() {
        return propertyDataSource.getAllProperties()
                .switchMap(this::propertyToPropertyFactory);
    }

    private Observable<List<Property>> propertyToPropertyFactory(List<PropertyInformation> properties) {
        allProperties = new ArrayList<>();
        return Observable.fromIterable(properties)
                .flatMap(this::addPropertyToList);
    }

    private Observable<List<Property>> addPropertyToList(@NonNull PropertyInformation propertyInformation) {
        return propertyDataSource.getPropertyAndAllValues(propertyInformation.getId())
                .flatMap(propertyFactory -> {
                    if (propertyFactory!=null&& allProperties!=null)
                    if (!allProperties.contains(propertyFactory))
                        allProperties.add(propertyFactory);
                    return Observable.create(emitter -> emitter.onNext(allProperties));
                });
    }

    public Observable<Property> getPropertyAndAllValues(int propertyId) {
        return propertyDataSource.getPropertyAndAllValues(propertyId);
    }

    public Observable<List<PropertyInformation>> getAllProperties() {
        return propertyDataSource.getAllProperties();
    }
}

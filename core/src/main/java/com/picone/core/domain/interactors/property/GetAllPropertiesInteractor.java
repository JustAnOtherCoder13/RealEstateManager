package com.picone.core.domain.interactors.property;

import com.picone.core.data.property.PropertyRepository;
import com.picone.core.domain.entity.Property;
import com.picone.core.domain.entity.PropertyFactory;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

public class GetAllPropertiesInteractor extends PropertyBaseInteractor {
    private List<PropertyFactory> allProperties;

    public GetAllPropertiesInteractor(PropertyRepository propertyDataSource) {
        super(propertyDataSource);
    }

    public Observable<List<PropertyFactory>> getAllProperties_() {
        return propertyDataSource.getAllProperties()
                .switchMap(this::propertyToPropertyFactory);
    }

    private Observable<List<PropertyFactory>> propertyToPropertyFactory(List<Property> properties) {
        allProperties = new ArrayList<>();
        return Observable.fromIterable(properties)
                .flatMap(this::addPropertyToList);
    }

    private Observable<List<PropertyFactory>> addPropertyToList(Property property) {
        return propertyDataSource.getPropertyAndAllValues(property.getId())
                .flatMap(propertyFactory -> {
                    if (propertyFactory!=null&& allProperties!=null)
                    if (!allProperties.contains(propertyFactory))
                        allProperties.add(propertyFactory);
                    return Observable.create(emitter -> emitter.onNext(allProperties));
                });
    }

    public Observable<PropertyFactory> getPropertyAndAllValues(int propertyId) {
        return propertyDataSource.getPropertyAndAllValues(propertyId);
    }

    public Observable<List<Property>> getAllProperties() {
        return propertyDataSource.getAllProperties();
    }
}

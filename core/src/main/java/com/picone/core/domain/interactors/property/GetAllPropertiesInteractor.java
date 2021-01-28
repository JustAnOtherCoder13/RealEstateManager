package com.picone.core.domain.interactors.property;

import com.picone.core.data.property.PropertyRepository;
import com.picone.core.domain.entity.Property;

import java.util.List;

import io.reactivex.Observable;

public class GetAllPropertiesInteractor extends PropertyBaseInteractor {

    public GetAllPropertiesInteractor(PropertyRepository propertyDataSource) {
        super(propertyDataSource);
    }

    public Observable<List<Property>> getAllProperties() {
        return propertyDataSource.getAllProperties();
    }

//todo interactor
    public Observable<Property> getProperty(int propertyId) {
        return propertyDataSource.getPropertyAndAllValues(propertyId);
    }
}

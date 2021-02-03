package com.picone.core.domain.interactors.property.location;

import androidx.annotation.NonNull;

import com.picone.core.data.property.PropertyRepository;
import com.picone.core.domain.interactors.property.PropertyBaseInteractor;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

public class GetAllRegionsForAllPropertiesInteractor extends PropertyBaseInteractor {

    public GetAllRegionsForAllPropertiesInteractor(PropertyRepository propertyDataSource) {
        super(propertyDataSource);
    }

    public Observable<List<String>> getAllRegionsForAllProperties() {
        return propertyDataSource.getAllRegionsForAllProperties()
                .map(this::removeUnnecessaryValues);
    }

    public List<String> removeUnnecessaryValues(@NonNull List<String> allRegions) {
        List<String> regionToPass = new ArrayList<>();
        for (String knownRegion : allRegions)
            if (!regionToPass.contains(knownRegion))
                regionToPass.add(knownRegion);

        return regionToPass;
    }
}

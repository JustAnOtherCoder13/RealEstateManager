package com.picone.core.domain.interactors.property.maps;

import androidx.annotation.NonNull;

import com.picone.core.data.property.PropertyRepository;
import com.picone.core.domain.entity.PointOfInterest;
import com.picone.core.domain.entity.PropertyLocation;
import com.picone.core.domain.entity.pojo.nearBySearch.NearBySearch;
import com.picone.core.domain.entity.pojo.nearBySearch.NearBySearchResult;
import com.picone.core.domain.interactors.property.PropertyBaseInteractor;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

import static com.picone.core.utils.ConstantParameters.POINT_OF_INTEREST_TYPE;

public class GetNearBySchoolForPropertyLocationInteractor extends PropertyBaseInteractor {
    List<PointOfInterest> pointOfInterests = new ArrayList<>();

    public GetNearBySchoolForPropertyLocationInteractor(PropertyRepository propertyDataSource) {
        super(propertyDataSource);
    }

    public Observable<List<PointOfInterest>> getNearBySchoolForPropertyLocation(@NonNull PropertyLocation propertyLocation, String googleKey) {

        return propertyDataSource.getNearBySchoolForPropertyLocation(propertyLocation, POINT_OF_INTEREST_TYPE.get(0), googleKey)
                .flatMap(nearBySearch -> {
                    nearBySearchToPointOfInterest(nearBySearch, propertyLocation.getPropertyId());
                    return propertyDataSource.getNearBySchoolForPropertyLocation(propertyLocation, POINT_OF_INTEREST_TYPE.get(1), googleKey);
                })
                .flatMap(nearBySearch -> {
                    nearBySearchToPointOfInterest(nearBySearch, propertyLocation.getPropertyId());
                    return propertyDataSource.getNearBySchoolForPropertyLocation(propertyLocation, POINT_OF_INTEREST_TYPE.get(2), googleKey);
                })
                .map(nearBySearch -> nearBySearchToPointOfInterest(nearBySearch, propertyLocation.getPropertyId()));
    }

    private List<PointOfInterest> nearBySearchToPointOfInterest(NearBySearch nearBySearch, int propertyId) {

        PointOfInterest pointOfInterest;

        for (NearBySearchResult nearBySearchResult : nearBySearch.getNearBySearchResults()) {
            pointOfInterest = new PointOfInterest();
            pointOfInterest.setPropertyId(propertyId);
            pointOfInterest.setName(nearBySearchResult.getName());
            pointOfInterest.setLatitude(nearBySearchResult.getNearBySearchGeometry().getNearBySearchLocation().getLat());
            pointOfInterest.setLongitude(nearBySearchResult.getNearBySearchGeometry().getNearBySearchLocation().getLng());
            pointOfInterest.setType(nearBySearchResult.getTypes().get(0));
            pointOfInterest.setIcon(nearBySearchResult.getIcon());

            if (pointOfInterests.isEmpty()) pointOfInterests.add(pointOfInterest);
            else if (!pointOfInterests.contains(pointOfInterest))
                pointOfInterests.add(pointOfInterest);
        }
        return pointOfInterests;
    }

}


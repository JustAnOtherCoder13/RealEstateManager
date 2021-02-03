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

public class GetNearBySearchForPropertyLocationInteractor extends PropertyBaseInteractor {

    private List<PointOfInterest> mPointOfInterests = new ArrayList<>();

    public GetNearBySearchForPropertyLocationInteractor(PropertyRepository propertyDataSource) {
        super(propertyDataSource);
    }

    public Observable<List<PointOfInterest>> getNearBySearchForPropertyLocation(@NonNull PropertyLocation propertyLocation, String googleKey) {
        mPointOfInterests = new ArrayList<>();
        return propertyDataSource.getNearBySearchForPropertyLocation(propertyLocation, POINT_OF_INTEREST_TYPE.get(0), googleKey)
                .switchMap(nearBySearch -> {
                    nearBySearchToPointOfInterest(nearBySearch, propertyLocation.getPropertyId());
                    return propertyDataSource.getNearBySearchForPropertyLocation(propertyLocation, POINT_OF_INTEREST_TYPE.get(1), googleKey);
                })
                .switchMap(nearBySearch -> {
                    nearBySearchToPointOfInterest(nearBySearch, propertyLocation.getPropertyId());
                    return propertyDataSource.getNearBySearchForPropertyLocation(propertyLocation, POINT_OF_INTEREST_TYPE.get(2), googleKey);
                })
                .map(nearBySearch -> nearBySearchToPointOfInterest(nearBySearch, propertyLocation.getPropertyId()));
    }

    private List<PointOfInterest> nearBySearchToPointOfInterest(@NonNull NearBySearch nearBySearch, int propertyId) {
        PointOfInterest pointOfInterest;
        if (!nearBySearch.getNearBySearchResults().isEmpty())
            for (NearBySearchResult nearBySearchResult : nearBySearch.getNearBySearchResults()) {
                pointOfInterest = createPointOfInterest(propertyId, nearBySearchResult);
                mPointOfInterests.add(pointOfInterest);
            }
        return mPointOfInterests;
    }


    @NonNull
    private PointOfInterest createPointOfInterest(int propertyId, @NonNull NearBySearchResult nearBySearchResult) {
        PointOfInterest pointOfInterest;
        pointOfInterest = new PointOfInterest();
        pointOfInterest.setPropertyId(propertyId);
        pointOfInterest.setName(nearBySearchResult.getName());
        pointOfInterest.setLatitude(nearBySearchResult.getNearBySearchGeometry().getNearBySearchLocation().getLat());
        pointOfInterest.setLongitude(nearBySearchResult.getNearBySearchGeometry().getNearBySearchLocation().getLng());
        pointOfInterest.setType(nearBySearchResult.getTypes().get(0));
        pointOfInterest.setIcon(nearBySearchResult.getIcon());
        return pointOfInterest;
    }
}


package com.picone.core.domain.interactors.property.photo;

import com.picone.core.data.property.PropertyRepository;
import com.picone.core.domain.entity.PropertyPhoto;
import com.picone.core.domain.interactors.property.PropertyBaseInteractor;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

public class GetAllPhotosForAllPropertiesInteractor extends PropertyBaseInteractor {

    public GetAllPhotosForAllPropertiesInteractor(PropertyRepository propertyDataSource) {
        super(propertyDataSource);
    }

    public Observable<List<PropertyPhoto>> getAllPhotosForAllProperties(){
        return propertyDataSource.getAllPhotosForAllProperties();
    }

    public Observable<List<PropertyPhoto>> getFirstPhotoForAllProperties(){
        return propertyDataSource.getAllPhotosForAllProperties()
                .map(this::getOnlyFirstPhotoForEachProperty);
    }

    private List<PropertyPhoto> getOnlyFirstPhotoForEachProperty(List<PropertyPhoto> allPropertiesPhoto){
        List<PropertyPhoto> photosToReturn = new ArrayList<>();
        for (int photo = 0; photo<allPropertiesPhoto.size();photo++){
            //if first photo
            if (photo==0)photosToReturn.add(allPropertiesPhoto.get(photo));
            else{
                boolean isThisPropertyAlreadyHavePhotoInList=false;
                //else check if photo to return already have photo for the same property
                // if case pass to following photo
                for (PropertyPhoto photoToReturn : photosToReturn)
                    if (photoToReturn.getPropertyId()==allPropertiesPhoto.get(photo).getPropertyId()){
                        isThisPropertyAlreadyHavePhotoInList=true;
                        break;
                    }
                // if doesn't know add to list
                if (!isThisPropertyAlreadyHavePhotoInList)photosToReturn.add(allPropertiesPhoto.get(photo));
            }

        }
        return photosToReturn;
    }
}

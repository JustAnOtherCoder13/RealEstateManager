package com.openclassrooms.realestatemanager;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import com.openclassrooms.realestatemanager.presentation.viewModels.AgentViewModel;
import com.openclassrooms.realestatemanager.presentation.viewModels.PropertyViewModel;
import com.picone.core.data.Generator;
import com.picone.core.data.property.PropertyRepository;
import com.picone.core.data.realEstateAgent.RealEstateAgentRepository;
import com.picone.core.domain.entity.PointOfInterest;
import com.picone.core.domain.entity.Property;
import com.picone.core.domain.entity.PropertyLocation;
import com.picone.core.domain.entity.PropertyPhoto;
import com.picone.core.domain.entity.RealEstateAgent;
import com.picone.core.domain.interactors.agent.GetAgentInteractor;
import com.picone.core.domain.interactors.property.AddPropertyInteractor;
import com.picone.core.domain.interactors.property.GetAllPropertiesInteractor;
import com.picone.core.domain.interactors.property.UpdatePropertyInteractor;
import com.picone.core.domain.interactors.property.location.AddPropertyLocationInteractor;
import com.picone.core.domain.interactors.property.location.GetPropertyLocationInteractor;
import com.picone.core.domain.interactors.property.maps.GetNearBySearchForPropertyLocationInteractor;
import com.picone.core.domain.interactors.property.maps.GetPropertyLocationForAddressInteractor;
import com.picone.core.domain.interactors.property.maps.GetStaticMapForLatLngInteractor;
import com.picone.core.domain.interactors.property.photo.AddPropertyPhotoInteractor;
import com.picone.core.domain.interactors.property.photo.DeletePropertyPhotoInteractor;
import com.picone.core.domain.interactors.property.photo.GetAllPropertyPhotosForPropertyIdInteractor;
import com.picone.core.domain.interactors.property.pointOfInterest.AddPropertyPointOfInterestInteractor;
import com.picone.core.domain.interactors.property.pointOfInterest.GetAllPointOfInterestForPropertyIdInteractor;
import com.picone.core.utils.SchedulerProvider;

import org.junit.Before;
import org.junit.Rule;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import static org.mockito.Mockito.when;

public abstract class BaseUnitTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    final int propertyId = Generator.generateProperties().get(0).getId();

    Property propertyToAdd = new Property(3,2,"property3Adress","zone","House",120,6,350000,"description",2,1,false,"0","0");
    Property firstPropertyToUpdate = Generator.generateProperties().get(0);
    List<PropertyPhoto> photoForPropertyId = new ArrayList<>();
    PropertyPhoto photoToAdd = new PropertyPhoto(5,"newPhoto","newDescription",1);
    PropertyPhoto photoToDelete = Generator.generatePhotos().get(1);
    List<PointOfInterest> pointOfInterestForPropertyId = new ArrayList<>();
    PointOfInterest pointOfInterestToAdd = new PointOfInterest(5,1,"school",0.0,0.0,"school","icon");
    List<PointOfInterest> pointOfInterestsToAdd = new ArrayList<>();

    PropertyLocation propertyLocationToAdd = new PropertyLocation(3,42.543732,5.036950,"region",propertyToAdd.getId());

    SchedulerProvider schedulerProvider = new SchedulerProvider(Schedulers.trampoline(), Schedulers.trampoline());

    //mock PropertyViewModel
    PropertyViewModel propertyViewModel;
    @Mock
    PropertyRepository propertyRepository;
    @InjectMocks
    GetAllPropertiesInteractor getAllPropertiesInteractor;
    @InjectMocks
    AddPropertyInteractor addPropertyInteractor;
    @InjectMocks
    UpdatePropertyInteractor updatePropertyInteractor;
    @InjectMocks
    AddPropertyPointOfInterestInteractor addPropertyPointOfInterestInteractor;
    @InjectMocks
    GetAllPointOfInterestForPropertyIdInteractor getAllPointOfInterestForPropertyIdInteractor;
    @InjectMocks
    AddPropertyPhotoInteractor addPropertyPhotoInteractor;
    @InjectMocks
    DeletePropertyPhotoInteractor deletePropertyPhotoInteractor;
    @InjectMocks
    GetAllPropertyPhotosForPropertyIdInteractor getAllPropertyPhotosForPropertyIdInteractor;
    @InjectMocks
    GetPropertyLocationInteractor getPropertyLocationInteractor;
    @InjectMocks
    AddPropertyLocationInteractor addPropertyLocationInteractor;
    @InjectMocks
    GetNearBySearchForPropertyLocationInteractor getNearBySearchForPropertyLocationInteractor;
    @InjectMocks
    GetPropertyLocationForAddressInteractor getPropertyLocationForAddressInteractor;
    @InjectMocks
    GetStaticMapForLatLngInteractor getStaticMapForLatLngInteractor;

    //mock realEstateAgentViewModel
    AgentViewModel agentViewModel;
    @Mock
    RealEstateAgentRepository realEstateAgentRepository;
    @InjectMocks
    GetAgentInteractor getAgentInteractor;

    //mock observer
    @Mock
    Observer<List<Property>> propertyObserver;
    @Mock
    Observer<List<PointOfInterest>> pointOfInterestObserver;
    @Mock
    Observer<List<PropertyPhoto>> photoObserver;
    @Mock
    Observer<RealEstateAgent> agentObserver;
    @Mock
    Observer<PropertyLocation> locationObserver;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);

        for (int i = 0; i<3;i++)
        photoForPropertyId.add(Generator.generatePhotos().get(i));

        pointOfInterestForPropertyId.add(Generator.generatePointOfInterests().get(0));
        pointOfInterestsToAdd.add(pointOfInterestToAdd);

        //initViewModels
        propertyViewModel = new PropertyViewModel(getAllPropertiesInteractor, getAllPointOfInterestForPropertyIdInteractor, getAllPropertyPhotosForPropertyIdInteractor, addPropertyInteractor, addPropertyPointOfInterestInteractor, addPropertyPhotoInteractor, deletePropertyPhotoInteractor, updatePropertyInteractor,getPropertyLocationInteractor,addPropertyLocationInteractor,getPropertyLocationForAddressInteractor,getStaticMapForLatLngInteractor, getNearBySearchForPropertyLocationInteractor,schedulerProvider);
        agentViewModel = new AgentViewModel(getAgentInteractor,schedulerProvider);

        //initObserver
        propertyViewModel.getAllProperties.observeForever(propertyObserver);
        propertyViewModel.getAllPointOfInterestForProperty.observeForever(pointOfInterestObserver);
        propertyViewModel.getAllPropertyPhotosForProperty.observeForever(photoObserver);
        propertyViewModel.getPropertyLocationForProperty.observeForever(locationObserver);

        agentViewModel.getAgent.observeForever(agentObserver);

        //stub return
        when(propertyRepository.getAllProperties())
                .thenReturn(Observable.create(emitter -> emitter.onNext(Generator.generateProperties())));
        when(propertyRepository.addProperty(propertyToAdd))
                .thenReturn(Completable.create
                        (emitter -> Objects.requireNonNull(propertyViewModel.getAllProperties.getValue()).add(propertyToAdd)));
        when(propertyRepository.updateProperty(firstPropertyToUpdate))
                .thenReturn(Completable.create(CompletableEmitter::onComplete));

        when(propertyRepository.getAllPhotosForPropertyId(propertyId))
                .thenReturn(Observable.create(emitter -> emitter.onNext(photoForPropertyId) ));
        when(propertyRepository.deletePropertyPhoto(photoToDelete))
                .thenReturn(Completable.create(emitter -> Objects.requireNonNull(propertyViewModel.getAllPropertyPhotosForProperty.getValue()).remove(photoToDelete)));
        when(propertyRepository.addPropertyPhoto(photoToAdd))
                .thenReturn(Completable.create(emitter -> Objects.requireNonNull(propertyViewModel.getAllPropertyPhotosForProperty.getValue()).add(photoToAdd)));

        when(propertyRepository.getAllPointOfInterestForPropertyId(propertyId))
                .thenReturn(Observable.create(emitter -> emitter.onNext(pointOfInterestForPropertyId)));
        when(propertyRepository.addPropertyPointOfInterest(pointOfInterestToAdd))
                .thenReturn(Completable.create(emitter -> Objects.requireNonNull(propertyViewModel.getAllPointOfInterestForProperty.getValue()).add(pointOfInterestToAdd)));

        when(propertyRepository.getPropertyLocationForPropertyId(propertyId))
                .thenReturn(Observable.create(emitter -> emitter.onNext(Generator.generatePropertyLocation().get(0))));
        when(propertyRepository.getPropertyLocationForPropertyId(propertyToAdd.getId()))
                .thenReturn(Observable.create(emitter -> emitter.onNext(propertyLocationToAdd)));
        when(propertyRepository.addPropertyLocation(propertyLocationToAdd))
                .thenReturn(Completable.create(CompletableEmitter::onComplete));

        when(realEstateAgentRepository.getAgent())
                .thenReturn(Observable.create(emitter -> emitter.onNext(Generator.generateAgents())));
        //set initial values
        propertyViewModel.setAllProperties();
        propertyViewModel.setAllPhotosForProperty(Generator.generateProperties().get(0));
        propertyViewModel.setAllPointOfInterestForProperty(Generator.generateProperties().get(0));

        agentViewModel.setAgent();
    }

}

package com.openclassrooms.realestatemanager;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import com.openclassrooms.realestatemanager.presentation.viewModels.PropertyViewModel;
import com.openclassrooms.realestatemanager.presentation.viewModels.RealEstateAgentViewModel;
import com.picone.core.data.Generator;
import com.picone.core.data.property.PropertyRepository;
import com.picone.core.data.realEstateAgent.RealEstateAgentRepository;
import com.picone.core.domain.entity.PointOfInterest;
import com.picone.core.domain.entity.Property;
import com.picone.core.domain.entity.PropertyPhoto;
import com.picone.core.domain.entity.RealEstateAgent;
import com.picone.core.domain.interactors.agent.GetAllRoomAgentInteractor;
import com.picone.core.domain.interactors.property.AddRoomPropertyInteractor;
import com.picone.core.domain.interactors.property.GetAllRoomPropertiesInteractor;
import com.picone.core.domain.interactors.property.UpdateRoomPropertyInteractor;
import com.picone.core.domain.interactors.property.photo.AddRoomPropertyPhotoInteractor;
import com.picone.core.domain.interactors.property.photo.DeleteRoomPropertyPhotoInteractor;
import com.picone.core.domain.interactors.property.photo.GetAllRoomPropertyPhotosForPropertyIdInteractor;
import com.picone.core.domain.interactors.property.pointOfInterest.AddRoomPropertyPointOfInterestInteractor;
import com.picone.core.domain.interactors.property.pointOfInterest.GetAllRoomPointOfInterestForPropertyIdInteractor;
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

    Property propertyToAdd = new Property(3,2,"property3Adress","House",120,6,350000,"description",2,1,false,"0","0");
    Property firstPropertyToUpdate = Generator.generateProperties().get(0);
    List<PropertyPhoto> photoForPropertyId = new ArrayList<>();
    PropertyPhoto photoToAdd = new PropertyPhoto(5,"newPhoto","newDescription",1);
    PropertyPhoto photoToDelete = Generator.generatePhotos().get(1);
    List<PointOfInterest> pointOfInterestForPropertyId = new ArrayList<>();
    PointOfInterest pointOfInterestToAdd = new PointOfInterest(5,1);

    SchedulerProvider schedulerProvider = new SchedulerProvider(Schedulers.trampoline(), Schedulers.trampoline());

    //mock PropertyViewModel
    PropertyViewModel propertyViewModel;
    @Mock
    PropertyRepository propertyRepository;
    @InjectMocks
    GetAllRoomPropertiesInteractor getAllRoomPropertiesInteractor;
    @InjectMocks
    AddRoomPropertyInteractor addRoomPropertyInteractor;
    @InjectMocks
    UpdateRoomPropertyInteractor updateRoomPropertyInteractor;
    @InjectMocks
    AddRoomPropertyPointOfInterestInteractor addRoomPropertyPointOfInterestInteractor;
    @InjectMocks
    GetAllRoomPointOfInterestForPropertyIdInteractor getAllRoomPointOfInterestForPropertyIdInteractor;
    @InjectMocks
    AddRoomPropertyPhotoInteractor addRoomPropertyPhotoInteractor;
    @InjectMocks
    DeleteRoomPropertyPhotoInteractor deleteRoomPropertyPhotoInteractor;
    @InjectMocks
    GetAllRoomPropertyPhotosForPropertyIdInteractor getAllRoomPropertyPhotosForPropertyIdInteractor;

    //mock realEstateAgentViewModel
    RealEstateAgentViewModel realEstateAgentViewModel;
    @Mock
    RealEstateAgentRepository realEstateAgentRepository;
    @InjectMocks
    GetAllRoomAgentInteractor getAllRoomAgentInteractor;

    //mock observer
    @Mock
    Observer<List<Property>> propertyObserver;
    @Mock
    Observer<List<PointOfInterest>> pointOfInterestObserver;
    @Mock
    Observer<List<PropertyPhoto>> photoObserver;
    @Mock
    Observer<List<RealEstateAgent>> agentObserver;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);

        for (int i = 0; i<3;i++)
        photoForPropertyId.add(Generator.generatePhotos().get(i));

        pointOfInterestForPropertyId.add(Generator.generatePointOfInterests().get(0));

        //initViewModels
        propertyViewModel = new PropertyViewModel(getAllRoomPropertiesInteractor,getAllRoomPointOfInterestForPropertyIdInteractor,getAllRoomPropertyPhotosForPropertyIdInteractor,addRoomPropertyInteractor,addRoomPropertyPointOfInterestInteractor,addRoomPropertyPhotoInteractor,deleteRoomPropertyPhotoInteractor,updateRoomPropertyInteractor,schedulerProvider);
        realEstateAgentViewModel = new RealEstateAgentViewModel(getAllRoomAgentInteractor,schedulerProvider);

        //initObserver
        propertyViewModel.getAllRoomProperties.observeForever(propertyObserver);
        propertyViewModel.getAllRoomPointOfInterestForProperty.observeForever(pointOfInterestObserver);
        propertyViewModel.getAllRoomPropertyPhotosForProperty.observeForever(photoObserver);

        realEstateAgentViewModel.getAllRoomAgents.observeForever(agentObserver);

        //stub return
        when(propertyRepository.getAllRoomProperties())
                .thenReturn(Observable.create(emitter -> emitter.onNext(Generator.generateProperties())));
        when(propertyRepository.addRoomProperty(propertyToAdd))
                .thenReturn(Completable.create
                        (emitter -> Objects.requireNonNull(propertyViewModel.getAllRoomProperties.getValue()).add(propertyToAdd)));
        when(propertyRepository.updateRoomProperty(firstPropertyToUpdate))
                .thenReturn(Completable.create(CompletableEmitter::onComplete));

        when(propertyRepository.getAllRoomPhotosForPropertyId(propertyId))
                .thenReturn(Observable.create(emitter -> emitter.onNext(photoForPropertyId) ));
        when(propertyRepository.deleteRoomPropertyPhoto(photoToDelete))
                .thenReturn(Completable.create(emitter -> Objects.requireNonNull(propertyViewModel.getAllRoomPropertyPhotosForProperty.getValue()).remove(photoToDelete)));
        when(propertyRepository.addRoomPropertyPhoto(photoToAdd))
                .thenReturn(Completable.create(emitter -> Objects.requireNonNull(propertyViewModel.getAllRoomPropertyPhotosForProperty.getValue()).add(photoToAdd)));

        when(propertyRepository.getAllRoomPointOfInterestForPropertyId(propertyId))
                .thenReturn(Observable.create(emitter -> emitter.onNext(pointOfInterestForPropertyId)));
        when(propertyRepository.addRoomPropertyPointOfInterest(pointOfInterestToAdd))
                .thenReturn(Completable.create(emitter -> Objects.requireNonNull(propertyViewModel.getAllRoomPointOfInterestForProperty.getValue()).add(pointOfInterestToAdd)));

        when(realEstateAgentRepository.getAllAgents())
                .thenReturn(Observable.create(emitter -> emitter.onNext(Generator.generateAgents())));
        //set initial values
        propertyViewModel.setAllRoomProperties();
        propertyViewModel.setAllRoomPhotosForProperty(Generator.generateProperties().get(0));
        propertyViewModel.setAllRoomPointOfInterestForProperty(Generator.generateProperties().get(0));

        realEstateAgentViewModel.setRoomAgentValue();
    }

}

package com.openclassrooms.realestatemanager;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateHandle;

import com.openclassrooms.realestatemanager.presentation.utils.FilterHelper;
import com.openclassrooms.realestatemanager.presentation.viewModels.AgentViewModel;
import com.openclassrooms.realestatemanager.presentation.viewModels.PropertyViewModel;
import com.picone.core.data.Generator;
import com.picone.core.data.property.PropertyRepository;
import com.picone.core.data.realEstateAgent.RealEstateAgentRepository;
import com.picone.core.domain.entity.PointOfInterest;
import com.picone.core.domain.entity.Property;
import com.picone.core.domain.entity.PropertyInformation;
import com.picone.core.domain.entity.PropertyLocation;
import com.picone.core.domain.entity.PropertyMedia;
import com.picone.core.domain.entity.RealEstateAgent;
import com.picone.core.domain.interactors.agent.GetAgentInteractor;
import com.picone.core.domain.interactors.agent.SetAgentInteractor;
import com.picone.core.domain.interactors.property.AddPropertyInteractor;
import com.picone.core.domain.interactors.property.GetAllPropertiesInteractor;
import com.picone.core.domain.interactors.property.UpdatePropertyInteractor;
import com.picone.core.domain.interactors.property.location.AddPropertyLocationInteractor;
import com.picone.core.domain.interactors.property.location.GetAllRegionsForAllPropertiesInteractor;
import com.picone.core.domain.interactors.property.location.UpdatePropertyLocationInteractor;
import com.picone.core.domain.interactors.property.maps.GetNearBySearchForPropertyLocationInteractor;
import com.picone.core.domain.interactors.property.maps.GetPropertyLocationForAddressInteractor;
import com.picone.core.domain.interactors.property.media.AddPropertyMediaInteractor;
import com.picone.core.domain.interactors.property.media.DeletePropertyMediaInteractor;
import com.picone.core.domain.interactors.property.pointOfInterest.AddPropertyPointOfInterestInteractor;
import com.picone.core.domain.interactors.property.pointOfInterest.DeletePointOfInterestInteractor;
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

public abstract class BaseViewModelUnitTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    final int propertyId = Generator.generatePropertiesInformation().get(0).getId();
    FilterHelper filterHelper;
    PropertyInformation propertyInformationToAdd = new PropertyInformation(3, 2, "House", 120, 6, 350000, "description", 2, 1, false, "0", "0");
    Property propertyToAdd = new Property();
    List<PropertyMedia> photoForPropertyId = new ArrayList<>();
    PropertyMedia photoToAdd = new PropertyMedia(5, "newPhoto", "newDescription", 1);
    PropertyMedia photoToDelete = Generator.generatePhotos().get(1);
    List<PointOfInterest> pointOfInterestForPropertyId = new ArrayList<>();
    PointOfInterest pointOfInterestToAdd = new PointOfInterest(5, 1, "school", 0.0, 0.0, "school", "icon");
    PointOfInterest newPointOfInterest = new PointOfInterest(1, 1, "restaurant", 0.0, 0.0, "restaurant", "icon");
    List<PointOfInterest> pointOfInterestsToAdd = new ArrayList<>();
    List<PointOfInterest> updatedPointOfInterests = new ArrayList<>();
    List<PropertyMedia> mediasToDelete = new ArrayList<>();
    List<Property> allProperties = new ArrayList<>();

    PropertyLocation propertyLocationToAdd = new PropertyLocation(3, 42.543732, 5.036950, "property3Adress", "region", propertyInformationToAdd.getId());
    PropertyLocation updatedPropertyLocation = propertyLocationToAdd;
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
    AddPropertyMediaInteractor addPropertyMediaInteractor;
    @InjectMocks
    DeletePropertyMediaInteractor deletePropertyMediaInteractor;
    @InjectMocks
    AddPropertyLocationInteractor addPropertyLocationInteractor;
    @InjectMocks
    GetNearBySearchForPropertyLocationInteractor getNearBySearchForPropertyLocationInteractor;
    @InjectMocks
    GetPropertyLocationForAddressInteractor getPropertyLocationForAddressInteractor;
    @InjectMocks
    UpdatePropertyLocationInteractor updatePropertyLocationInteractor;
    @InjectMocks
    DeletePointOfInterestInteractor deletePointOfInterestInteractor;
    @InjectMocks
    GetAllRegionsForAllPropertiesInteractor getAllRegionsForAllPropertiesInteractor;
    SavedStateHandle savedStateHandle;

    //mock realEstateAgentViewModel
    AgentViewModel agentViewModel;
    @Mock
    RealEstateAgentRepository realEstateAgentRepository;
    @InjectMocks
    GetAgentInteractor getAgentInteractor;
    @InjectMocks
    SetAgentInteractor setAgentInteractor;

    //mock observer
    @Mock
    Observer<List<Property>> propertyObserver;
    @Mock
    Observer<RealEstateAgent> agentObserver;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        for (int i = 0; i < 3; i++)
            photoForPropertyId.add(Generator.generatePhotos().get(i));

        initAllProperties();

        filterHelper = new FilterHelper();
        filterHelper.initFilterValue(allProperties);

        propertyToAdd.propertyInformation = propertyInformationToAdd;
        propertyToAdd.propertyLocation = propertyLocationToAdd;
        propertyToAdd.pointOfInterests = pointOfInterestsToAdd;

        pointOfInterestForPropertyId.add(Generator.generatePointOfInterests().get(0));
        pointOfInterestsToAdd.add(pointOfInterestToAdd);
        updatedPointOfInterests.add(newPointOfInterest);

        updatedPropertyLocation.setRegion("new region");

        mediasToDelete.add(photoToDelete);
        //initViewModels
        propertyViewModel = new PropertyViewModel(getAllPropertiesInteractor, getAllPointOfInterestForPropertyIdInteractor, getAllRegionsForAllPropertiesInteractor, addPropertyInteractor, addPropertyPointOfInterestInteractor, addPropertyMediaInteractor, deletePropertyMediaInteractor, updatePropertyInteractor, addPropertyLocationInteractor, getPropertyLocationForAddressInteractor, getNearBySearchForPropertyLocationInteractor, updatePropertyLocationInteractor, deletePointOfInterestInteractor,savedStateHandle, schedulerProvider);
        agentViewModel = new AgentViewModel(getAgentInteractor, setAgentInteractor, schedulerProvider);

        //initObserver
        propertyViewModel.getAllProperties.observeForever(propertyObserver);

        agentViewModel.getAgent.observeForever(agentObserver);

        //stub return

        //property
        when(propertyRepository.getAllProperties())
                .thenReturn(Observable.create(emitter -> emitter.onNext(allProperties)));
        when(propertyRepository.addProperty(propertyInformationToAdd))
                .thenReturn(Completable.create
                        (emitter -> Objects.requireNonNull(propertyViewModel.getAllProperties.getValue()).add(propertyToAdd)));
        when(propertyRepository.updateProperty(allProperties.get(0).propertyInformation))
                .thenReturn(Completable.create(CompletableEmitter::onComplete));

        //point of interest
        when(propertyRepository.getAllPointOfInterestForPropertyId(propertyId))
                .thenReturn(Observable.create(emitter -> emitter.onNext(pointOfInterestForPropertyId)));
        when(propertyRepository.addPropertyPointOfInterest(pointOfInterestToAdd))
                .thenReturn(Completable.create(emitter -> Objects.requireNonNull(propertyViewModel.getAllProperties.getValue()).get(0).pointOfInterests.addAll(pointOfInterestsToAdd)));
        when(propertyRepository.deletePropertyPointOfInterest(Generator.generatePointOfInterests().get(0)))
                .thenReturn(Completable.create(emitter -> Objects.requireNonNull(propertyViewModel.getAllProperties.getValue()).get(0).pointOfInterests.remove(Generator.generatePointOfInterests().get(0))));
        when(propertyRepository.addPropertyPointOfInterest(updatedPointOfInterests.get(0)))
                .thenReturn(Completable.create(emitter -> Objects.requireNonNull(propertyViewModel.getAllProperties.getValue()).get(0).pointOfInterests.add(updatedPointOfInterests.get(0))));
        when(propertyRepository.deletePropertyPointOfInterest(pointOfInterestToAdd))
                .thenReturn(Completable.create(CompletableEmitter::onComplete));


        //media
        when(propertyRepository.deletePropertyMedia(photoToDelete))
                .thenReturn(Completable.create(emitter -> Objects.requireNonNull(propertyViewModel.getAllProperties.getValue()).get(0).medias.remove(photoToDelete)));
        when(propertyRepository.addPropertyMedia(photoToAdd))
                .thenReturn(Completable.create(emitter -> Objects.requireNonNull(propertyViewModel.getAllProperties.getValue()).get(0).medias.add(photoToAdd)));


        //location
        when(propertyRepository.getPropertyLocationForPropertyId(propertyId))
                .thenReturn(Observable.create(emitter -> emitter.onNext(Generator.generatePropertyLocation().get(0))));
        when(propertyRepository.getPropertyLocationForPropertyId(propertyInformationToAdd.getId()))
                .thenReturn(Observable.create(emitter -> emitter.onNext(propertyLocationToAdd)));
        when(propertyRepository.addPropertyLocation(propertyLocationToAdd))
                .thenReturn(Completable.create(CompletableEmitter::onComplete));
        when(propertyRepository.updatePropertyLocation(updatedPropertyLocation))
                .thenReturn(Completable.create(CompletableEmitter::onComplete));

        when(realEstateAgentRepository.getAgent())
                .thenReturn(Observable.create(emitter -> emitter.onNext(Generator.generateAgents())));
        //set initial values
        propertyViewModel.setAllProperties();
        propertyViewModel.setAllPointOfInterestForProperty(allProperties.get(0));

        agentViewModel.setAgent();
    }

    private void initAllProperties() {
        for (int i = 0; i < Generator.generatePropertiesInformation().size(); i++) {
            Property property = new Property();
            property.propertyInformation = Generator.generatePropertiesInformation().get(i);
            property.propertyLocation = Generator.generatePropertyLocation().get(i);
            property.medias = new ArrayList<>();
            property.pointOfInterests = new ArrayList<>();
            for (PropertyMedia propertyMedia : Generator.generatePhotos())
                if (propertyMedia.getPropertyId() == property.propertyInformation.getId())
                    property.medias.add(propertyMedia);
            for (PointOfInterest pointOfInterest : Generator.generatePointOfInterests())
                if (pointOfInterest.getPropertyId() == property.propertyInformation.getId())
                    property.pointOfInterests.add(pointOfInterest);

            allProperties.add(property);
        }
    }

}

package com.openclassrooms.realestatemanager.presentation.viewModels;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.picone.core.domain.entity.RealEstateAgent;
import com.picone.core.domain.interactors.agent.GetAgentInteractor;
import com.picone.core.utils.SchedulerProvider;

public class AgentViewModel extends BaseViewModel {

    private MutableLiveData<RealEstateAgent> AgentMutableLD = new MutableLiveData<>();

    public LiveData <RealEstateAgent> getAgent = AgentMutableLD;

    @ViewModelInject
    public AgentViewModel(GetAgentInteractor getAgentInteractor
    , SchedulerProvider schedulerProvider){
        this.getAgentInteractor = getAgentInteractor;
        this.schedulerProvider =schedulerProvider;
    }

    public void setAgent(){
        compositeDisposable.add(
        getAgentInteractor.getAgent()
                .subscribeOn(schedulerProvider.getIo())
                .observeOn(schedulerProvider.getUi())
                .subscribe(realEstateAgents -> AgentMutableLD.postValue(realEstateAgents)));
    }
}

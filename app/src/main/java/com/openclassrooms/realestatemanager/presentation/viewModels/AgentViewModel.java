package com.openclassrooms.realestatemanager.presentation.viewModels;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.picone.core.domain.entity.RealEstateAgent;
import com.picone.core.domain.interactors.agent.GetAgentInteractor;
import com.picone.core.domain.interactors.agent.SetAgentInteractor;
import com.picone.core.utils.SchedulerProvider;

public class AgentViewModel extends BaseViewModel {

    private MutableLiveData<RealEstateAgent> agentMutableLD = new MutableLiveData<>();

    public LiveData<RealEstateAgent> getAgent = agentMutableLD;

    @ViewModelInject
    public AgentViewModel(GetAgentInteractor getAgentInteractor, SetAgentInteractor setAgentInteractor
            , SchedulerProvider schedulerProvider) {
        this.getAgentInteractor = getAgentInteractor;
        this.setAgentInteractor = setAgentInteractor;
        this.schedulerProvider = schedulerProvider;
    }

    public void setAgent() {
        compositeDisposable.add(
                getAgentInteractor.getAgent()
                        .subscribeOn(schedulerProvider.getIo())
                        .observeOn(schedulerProvider.getUi())
                        .subscribe(realEstateAgents -> agentMutableLD.postValue(realEstateAgents)));
    }

    public void initAgent(RealEstateAgent agent) {
        compositeDisposable.add(
                setAgentInteractor.setAgent(agent)
                        .subscribeOn(schedulerProvider.getIo())
                        .observeOn(schedulerProvider.getUi())
                        .andThen(getAgentInteractor.getAgent())
                        .subscribe(realEstateAgent->
                                agentMutableLD.postValue(realEstateAgent),
                                throwable -> checkException()));
    }
}

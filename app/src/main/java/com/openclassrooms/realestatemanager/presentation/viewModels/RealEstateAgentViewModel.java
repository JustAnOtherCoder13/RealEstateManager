package com.openclassrooms.realestatemanager.presentation.viewModels;

import android.util.Log;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.picone.core.domain.entity.RealEstateAgent;
import com.picone.core.domain.interactors.agent.GetAllRoomAgentInteractor;
import com.picone.core.utils.SchedulerProvider;

import java.util.List;

public class RealEstateAgentViewModel extends BaseViewModel {
    private MutableLiveData<List<RealEstateAgent>> allAgentsMutableLD = new MutableLiveData<>();

    public LiveData<List<RealEstateAgent>> getAllAgents = allAgentsMutableLD;

    @ViewModelInject
    public RealEstateAgentViewModel(GetAllRoomAgentInteractor getAllRoomAgentInteractor
    , SchedulerProvider schedulerProvider){
        this.getAllRoomAgentInteractor = getAllRoomAgentInteractor;
        this.schedulerProvider =schedulerProvider;
    }

    public void setAgentValue (){
        compositeDisposable.add(
        getAllRoomAgentInteractor.getAllAgents()
                .subscribeOn(schedulerProvider.getIo())
                .observeOn(schedulerProvider.getUi())
                .subscribe(realEstateAgents -> Log.i("TAG", "setAgentValue: "+realEstateAgents)));
    }
}

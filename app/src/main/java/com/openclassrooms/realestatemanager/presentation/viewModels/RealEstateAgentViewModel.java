package com.openclassrooms.realestatemanager.presentation.viewModels;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.picone.core.domain.entity.RealEstateAgent;
import com.picone.core.domain.interactors.GetAllManagerInteractor;

import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RealEstateAgentViewModel extends BaseViewModel {
    private MutableLiveData<List<RealEstateAgent>> allAgentsMutableLD = new MutableLiveData<>();

    public LiveData<List<RealEstateAgent>> getAllAgents = allAgentsMutableLD;

    @ViewModelInject
    public RealEstateAgentViewModel(GetAllManagerInteractor getAllManagerInteractor){
        this.getAllManagerInteractor = getAllManagerInteractor;
    }

    public void setAgentValue (){
        getAllManagerInteractor.getAllAgents()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }
}

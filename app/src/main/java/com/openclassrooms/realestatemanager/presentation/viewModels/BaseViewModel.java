package com.openclassrooms.realestatemanager.presentation.viewModels;

import androidx.lifecycle.ViewModel;

import com.picone.core.domain.interactors.GetAllManagerInteractor;

public abstract class BaseViewModel extends ViewModel {

    //------------------------REAL ESTATE MANAGER INTERACTORS----------------------------

    protected GetAllManagerInteractor getAllManagerInteractor;
}

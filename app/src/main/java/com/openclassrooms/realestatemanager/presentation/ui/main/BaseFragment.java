package com.openclassrooms.realestatemanager.presentation.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.presentation.viewModels.PropertyViewModel;
import com.openclassrooms.realestatemanager.presentation.viewModels.RealEstateAgentViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public abstract class BaseFragment extends Fragment {

    protected RealEstateAgentViewModel mRealEstateAgentViewModel;
    protected PropertyViewModel mPropertyViewModel;
    protected NavController mNavController;

    private MainActivity mainActivity;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        mRealEstateAgentViewModel = new ViewModelProvider(requireActivity()).get(RealEstateAgentViewModel.class);
        mPropertyViewModel = new  ViewModelProvider(requireActivity()).get(PropertyViewModel.class);
    }
    protected void setAppBarVisibility(boolean isVisible) {
        mainActivity.setMenuVisibility(isVisible);
    }

}

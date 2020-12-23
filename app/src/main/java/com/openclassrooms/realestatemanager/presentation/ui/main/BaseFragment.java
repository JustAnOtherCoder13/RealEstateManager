package com.openclassrooms.realestatemanager.presentation.ui.main;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;

import com.openclassrooms.realestatemanager.presentation.viewModels.PropertyViewModel;
import com.openclassrooms.realestatemanager.presentation.viewModels.RealEstateAgentViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public abstract class BaseFragment extends Fragment {

    protected RealEstateAgentViewModel mRealEstateAgentViewModel;
    protected PropertyViewModel mPropertyViewModel;
    protected NavController mNavController;

    protected MainActivity mainActivity;
    protected ImageButton mUpdateButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        assert mainActivity != null;
        mUpdateButton = mainActivity.mUpdateButton;
        mRealEstateAgentViewModel = new ViewModelProvider(requireActivity()).get(RealEstateAgentViewModel.class);
        mPropertyViewModel = new ViewModelProvider(requireActivity()).get(PropertyViewModel.class);
    }

    protected void setAppBarVisibility(boolean isVisible) {
        mainActivity.setMenuVisibility(isVisible);
    }

}

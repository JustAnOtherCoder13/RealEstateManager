package com.openclassrooms.realestatemanager.presentation.ui.main;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;

import com.openclassrooms.realestatemanager.presentation.viewModels.AgentViewModel;
import com.openclassrooms.realestatemanager.presentation.viewModels.PropertyViewModel;
import com.picone.core.domain.entity.Property;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public abstract class BaseFragment extends Fragment {

    protected AgentViewModel mAgentViewModel;
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
        mAgentViewModel = new ViewModelProvider(requireActivity()).get(AgentViewModel.class);
        mPropertyViewModel = new ViewModelProvider(requireActivity()).get(PropertyViewModel.class);
    }

    protected void setAppBarVisibility(boolean isVisible) {
        mainActivity.setMenuVisibility(isVisible);
    }
    protected void setUpdateButtonIcon(boolean isForUpdate){
        mainActivity.initUpdateButton(isForUpdate);
    }

    protected Property getPropertyForId(String propertyId) {
        Property propertyToReturn = new Property();
        for (Property property : Objects.requireNonNull(mPropertyViewModel.getAllProperties.getValue())) {
            if (String.valueOf(property.getId()).equalsIgnoreCase(propertyId))
                propertyToReturn = property;
        }
        return propertyToReturn;
    }

}

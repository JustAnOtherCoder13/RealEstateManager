package com.openclassrooms.realestatemanager.presentation.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.openclassrooms.realestatemanager.databinding.FragmentAddPropertyBinding;
import com.openclassrooms.realestatemanager.presentation.ui.main.BaseFragment;

public class AddPropertyFragment extends BaseFragment {

    private FragmentAddPropertyBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentAddPropertyBinding.inflate(getLayoutInflater());
        mRealEstateAgentViewModel.setAgentValue();
        return mBinding.getRoot();
    }
}
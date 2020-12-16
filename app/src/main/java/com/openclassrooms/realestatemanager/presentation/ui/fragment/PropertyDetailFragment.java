package com.openclassrooms.realestatemanager.presentation.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentPropertyDetailBinding;
import com.openclassrooms.realestatemanager.presentation.ui.main.BaseFragment;
import com.picone.core.data.Generator;

import java.util.Objects;


public class PropertyDetailFragment extends BaseFragment {

    private FragmentPropertyDetailBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentPropertyDetailBinding.inflate(inflater,container,false);
        setAppBarVisibility(false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView addressTextView = mBinding.fragmentDetailLocationCustomView.findViewById(R.id.information_custom_view_value);
        addressTextView.setText(Objects.requireNonNull(mPropertyViewModel.getSelectedProperty.getValue()).getAddress());
    }
}
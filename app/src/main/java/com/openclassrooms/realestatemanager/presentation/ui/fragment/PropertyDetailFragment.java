package com.openclassrooms.realestatemanager.presentation.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentPropertyDetailBinding;
import com.openclassrooms.realestatemanager.presentation.ui.main.BaseFragment;
import com.picone.core.domain.entity.Property;

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
        int valueId = R.id.detail_information_custom_view_value;
        Property property = Objects.requireNonNull(mPropertyViewModel.getSelectedProperty.getValue());

        TextView addressTextView = mBinding.fragmentDetailInformationLayout.fragmentDetailLocationCustomView.findViewById(valueId);
        TextView surfaceTextView = mBinding.fragmentDetailInformationLayout.fragmentDetailAreaCustomView.findViewById(valueId);
        TextView numberOfRoomsTextView = mBinding.fragmentDetailInformationLayout.fragmentDetailNumbersOfRoomsCustomView.findViewById(valueId);

        addressTextView.setText(property.getAddress());
        surfaceTextView.setText(String.valueOf(property.getPropertyArea()).concat(" ").concat("sq m"));
        numberOfRoomsTextView.setText(String.valueOf(property.getNumberOfRooms()));
    }
}
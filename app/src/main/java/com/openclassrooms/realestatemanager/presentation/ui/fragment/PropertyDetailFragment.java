package com.openclassrooms.realestatemanager.presentation.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentPropertyDetailBinding;
import com.openclassrooms.realestatemanager.databinding.FragmentPropertyDetailInformationBinding;
import com.openclassrooms.realestatemanager.presentation.ui.fragment.adapter.PhotoRecyclerViewAdapter;
import com.openclassrooms.realestatemanager.presentation.ui.main.BaseFragment;
import com.openclassrooms.realestatemanager.presentation.utils.customView.DetailInformationCustomView;
import com.picone.core.domain.entity.Property;

import java.util.ArrayList;
import java.util.Objects;


public class PropertyDetailFragment extends BaseFragment {

    private FragmentPropertyDetailBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentPropertyDetailBinding.inflate(inflater, container, false);
        mNavController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        setAppBarVisibility(false);
        initRecyclerView();
        setUpdateButtonIcon(true);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPropertyViewModel.getSelectedProperty.observe(getViewLifecycleOwner(),property ->
                initValue(mBinding.fragmentDetailInformationLayout,
                Objects.requireNonNull(property),
                mBinding.fragmentDetailDescriptionLayout.customViewDetailMediaDescriptionText,
                mBinding.fragmentDetailInformationLayout.fragmentDetailLocationCustomView.findViewById(R.id.detail_information_custom_view_value)));
        }

    private void initRecyclerView() {
        PhotoRecyclerViewAdapter adapter = new PhotoRecyclerViewAdapter(new ArrayList<>());
        mBinding.fragmentDetailMediaLayout.detailCustomViewRecyclerView.setAdapter(adapter);
        mPropertyViewModel.getAllPropertyPhotosForProperty.observe(getViewLifecycleOwner(), adapter::updatePhotos);
    }

    private void initValue(@NonNull FragmentPropertyDetailInformationBinding detailInformationLayout, @NonNull Property property, TextView descriptionTextView, TextView addressTextView) {
        setTextForCustomView(detailInformationLayout.fragmentDetailAreaCustomView, String.valueOf(property.getPropertyArea()).concat(" ").concat("sq m"));
        setTextForCustomView(detailInformationLayout.fragmentDetailLocationCustomView, property.getAddress());
        setTextForCustomView(detailInformationLayout.fragmentDetailNumbersOfRoomsCustomView, String.valueOf(property.getNumberOfRooms()));
        setTextForCustomView(detailInformationLayout.fragmentDetailNumbersOfBedroomsCustomView, String.valueOf(property.getNumberOfBedrooms()));
        setTextForCustomView(detailInformationLayout.fragmentDetailNumbersOfBathroomsCustomView, String.valueOf(property.getNumberOfBathrooms()));
    }

    private void setTextForCustomView(@NonNull DetailInformationCustomView customView, String text) {
        TextView textView = customView.findViewById(R.id.detail_information_custom_view_value);
        textView.setText(text);
    }
}
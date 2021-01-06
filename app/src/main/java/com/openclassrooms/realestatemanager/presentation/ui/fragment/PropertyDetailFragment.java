package com.openclassrooms.realestatemanager.presentation.ui.fragment;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentPropertyDetailBinding;
import com.openclassrooms.realestatemanager.databinding.FragmentPropertyDetailInformationBinding;
import com.openclassrooms.realestatemanager.presentation.ui.fragment.adapter.PhotoRecyclerViewAdapter;
import com.openclassrooms.realestatemanager.presentation.ui.main.BaseFragment;
import com.openclassrooms.realestatemanager.presentation.utils.customView.DetailInformationCustomView;
import com.picone.core.domain.entity.Property;
import com.picone.core.domain.entity.PropertyLocation;

import java.util.ArrayList;
import java.util.Objects;

import static com.picone.core.utils.ConstantParameters.BASE_STATIC_MAP_URI;
import static com.picone.core.utils.ConstantParameters.MAPS_CAMERA_NEAR_ZOOM;
import static com.picone.core.utils.ConstantParameters.MAPS_KEY;
import static com.picone.core.utils.ConstantParameters.STATIC_MAP_SIZE;


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
        mPropertyViewModel.getSelectedProperty.observe(getViewLifecycleOwner(), property -> {
            mPropertyViewModel.setPropertyLocationForProperty(property);
            initValue(mBinding.fragmentDetailInformationLayout,
                    Objects.requireNonNull(property),
                    mBinding.fragmentDetailDescriptionLayout.detailMediaDescriptionLayoutText);
        });
    }

    private void initRecyclerView() {
        PhotoRecyclerViewAdapter adapter = new PhotoRecyclerViewAdapter(new ArrayList<>());
        mBinding.fragmentDetailMediaLayout.detailCustomViewRecyclerView.setAdapter(adapter);
        mPropertyViewModel.getAllPropertyPhotosForProperty.observe(getViewLifecycleOwner(), adapter::updatePhotos);
    }

    private void initValue(@NonNull FragmentPropertyDetailInformationBinding detailInformationLayout, @NonNull Property property, @NonNull TextView descriptionTextView) {
        setTextForCustomView(detailInformationLayout.fragmentDetailAreaCustomView, String.valueOf(property.getPropertyArea()).concat(" ").concat("sq m"));
        setTextForCustomView(detailInformationLayout.fragmentDetailLocationCustomView, property.getAddress());
        setTextForCustomView(detailInformationLayout.fragmentDetailNumbersOfRoomsCustomView, String.valueOf(property.getNumberOfRooms()));
        setTextForCustomView(detailInformationLayout.fragmentDetailNumbersOfBedroomsCustomView, String.valueOf(property.getNumberOfBedrooms()));
        setTextForCustomView(detailInformationLayout.fragmentDetailNumbersOfBathroomsCustomView, String.valueOf(property.getNumberOfBathrooms()));
        descriptionTextView.setText(property.getPropertyType());
        mPropertyViewModel.getPropertyLocationForProperty.observe(getViewLifecycleOwner(), this::setStaticMap);
    }


    private void setTextForCustomView(@NonNull DetailInformationCustomView customView, String text) {
        TextView textView = customView.findViewById(R.id.detail_information_custom_view_value);
        textView.setText(text);
    }

    private void setStaticMap(@NonNull PropertyLocation propertyLocation) {

        String propertyLocationStr = String.valueOf(propertyLocation.getLatitude())
                .concat(",").concat(String.valueOf(propertyLocation.getLongitude()));

        String center = "center=".concat(propertyLocationStr)
                .concat("&zoom=").concat(String.valueOf(MAPS_CAMERA_NEAR_ZOOM));

        String size = "&size=".concat(STATIC_MAP_SIZE).concat("x").concat(STATIC_MAP_SIZE);

        String markerStyle = "&markers=color:0xFF4081%7C".concat(propertyLocationStr);

        String uriToPass = BASE_STATIC_MAP_URI.concat(center)
                .concat(size)
                .concat(markerStyle)
                .concat("&key=")
                .concat(MAPS_KEY);

        Glide.with(mBinding.fragmentDetailInformationLayout.fragmentDetailMapsView)
                .load(Uri.parse(uriToPass))
                .centerCrop()
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        mBinding.fragmentDetailInformationLayout.fragmentDetailMapsView.setImageDrawable(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) { }
                });
    }
}
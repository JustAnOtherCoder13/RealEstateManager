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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentPropertyDetailBinding;
import com.openclassrooms.realestatemanager.databinding.FragmentPropertyDetailInformationBinding;
import com.openclassrooms.realestatemanager.presentation.ui.fragment.adapter.PhotoRecyclerViewAdapter;
import com.openclassrooms.realestatemanager.presentation.ui.main.BaseFragment;
import com.openclassrooms.realestatemanager.presentation.utils.RecyclerViewItemClickListener;
import com.openclassrooms.realestatemanager.presentation.utils.customView.CustomMediaFullScreenDialog;
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
    private PhotoRecyclerViewAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentPropertyDetailBinding.inflate(inflater, container, false);
        setAppBarVisibility(false);
        initRecyclerView();
        setUpdateButtonIcon(true);
        return mBinding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPropertyViewModel.getSelectedProperty.observe(getViewLifecycleOwner(), property -> {
            if (property.propertyInformation !=null){
             mBinding.fragmentDetailSoldTextView.setVisibility(property.propertyInformation.isSold()?View.VISIBLE:View.GONE);
                setUpdateButtonCustomViewVisibility(!property.propertyInformation.isSold());
                if (property.propertyInformation.getSoldFrom()!=null)
                    mBinding.fragmentDetailSoldTextView.setText(getString(R.string.is_sold_from).concat(property.propertyInformation.getSoldFrom()));
                adapter.updatePhotos(property.photos);
                initClickOnMedia(property);
                initValue(mBinding.fragmentDetailInformationLayout,
                        Objects.requireNonNull(property),
                        mBinding.fragmentDetailDescriptionLayout.detailMediaDescriptionLayoutText);
            }
        });
    }

    private void initRecyclerView() {
        adapter = new PhotoRecyclerViewAdapter(new ArrayList<>());
        adapter.isPhotoHaveBeenDeleted(false);
        mBinding.fragmentDetailMediaLayout.detailCustomViewRecyclerView.setAdapter(adapter);
    }

    private void initValue(@NonNull FragmentPropertyDetailInformationBinding detailInformationLayout, @NonNull Property property, @NonNull TextView descriptionTextView) {
        setTextForCustomView(detailInformationLayout.fragmentDetailAreaCustomView, String.valueOf(property.propertyInformation.getPropertyArea()).concat(" ").concat("sq m"));
        setTextForCustomView(detailInformationLayout.fragmentDetailLocationCustomView, property.propertyLocation.getAddress());
        setTextForCustomView(detailInformationLayout.fragmentDetailNumbersOfRoomsCustomView, String.valueOf(property.propertyInformation.getNumberOfRooms()));
        setTextForCustomView(detailInformationLayout.fragmentDetailNumbersOfBedroomsCustomView, String.valueOf(property.propertyInformation.getNumberOfBedrooms()));
        setTextForCustomView(detailInformationLayout.fragmentDetailNumbersOfBathroomsCustomView, String.valueOf(property.propertyInformation.getNumberOfBathrooms()));
        descriptionTextView.setText(property.propertyInformation.getDescription());
        setStaticMap(property.propertyLocation);
    }

    private void initClickOnMedia(Property property) {
        RecyclerViewItemClickListener.addTo(mBinding.fragmentDetailMediaLayout.detailCustomViewRecyclerView, R.layout.fragment_property_detail)
                .setOnItemClickListener((recyclerView, position, v) -> {
                    CustomMediaFullScreenDialog fullScreenMediaDialog = new CustomMediaFullScreenDialog(requireContext(), property.photos.get(position).getPhotoPath());
                    fullScreenMediaDialog.show();
                });
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
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });
    }
}
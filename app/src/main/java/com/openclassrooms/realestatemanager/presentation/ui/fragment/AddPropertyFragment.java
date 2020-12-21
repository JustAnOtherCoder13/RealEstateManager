package com.openclassrooms.realestatemanager.presentation.ui.fragment;

import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentAddPropertyBinding;
import com.openclassrooms.realestatemanager.databinding.FragmentAddPropertyInformationLayoutBinding;
import com.openclassrooms.realestatemanager.presentation.ui.fragment.adapter.PhotoRecyclerViewAdapter;
import com.openclassrooms.realestatemanager.presentation.ui.main.BaseFragment;
import com.openclassrooms.realestatemanager.presentation.utils.RecyclerViewItemClickUtil;
import com.openclassrooms.realestatemanager.presentation.utils.customView.AddPropertyInformationCustomView;
import com.openclassrooms.realestatemanager.presentation.utils.customView.DetailInformationCustomView;
import com.picone.core.domain.entity.Property;
import com.picone.core.domain.entity.PropertyPhoto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AddPropertyFragment extends BaseFragment {

    private FragmentAddPropertyBinding mBinding;
    private List<PropertyPhoto> propertyPhotos = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentAddPropertyBinding.inflate(getLayoutInflater());
        setAppBarVisibility(false);
        initRecyclerView();
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentAddPropertyInformationLayoutBinding addPropertyInformationCustomView = mBinding.addPropertyInformationLayout;
        Property property = mPropertyViewModel.getSelectedProperty.getValue();
        configureOnClickRecyclerView();

        mBinding.addPropertySoldLayout.addPropertySoldCheckbox.setOnClickListener(v -> {
            if (mBinding.addPropertySoldLayout.addPropertySoldCheckbox.isChecked())
                mBinding.addPropertySoldLayout.addPropertySoldEditText.setVisibility(View.VISIBLE);
            else mBinding.addPropertySoldLayout.addPropertySoldEditText.setVisibility(View.GONE);
        });
        if (property != null) {
            setTextForCustomView(addPropertyInformationCustomView.addPropertyInformationPrice, String.valueOf(property.getPrice()));
            setTextForCustomView(addPropertyInformationCustomView.addPropertyInformationArea, String.valueOf(property.getPropertyArea()));
            setTextForCustomView(addPropertyInformationCustomView.addPropertyInformationNumberOfBathrooms, String.valueOf(property.getNumberOfBathrooms()));
            setTextForCustomView(addPropertyInformationCustomView.addPropertyInformationNumberOfBedrooms, String.valueOf(property.getNumberOfBedrooms()));
            setTextForCustomView(addPropertyInformationCustomView.addPropertyInformationNumberOfRooms, String.valueOf(property.getNumberOfRooms()));
            EditText editText = addPropertyInformationCustomView.addPropertyInformationAddress.findViewById(R.id.add_property_information_custom_view_value);
            editText.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
            setTextForCustomView(addPropertyInformationCustomView.addPropertyInformationAddress, property.getAddress());
            propertyPhotos.addAll(Objects.requireNonNull(mPropertyViewModel.getAllRoomPropertyPhotosForProperty.getValue()));
        }
    }

    private void initRecyclerView() {
        PropertyPhoto propertyPhoto = new PropertyPhoto(0, "AddPhoto", "add photo", 0);
        propertyPhotos.add(propertyPhoto);
        PhotoRecyclerViewAdapter adapter = new PhotoRecyclerViewAdapter(propertyPhotos);
        mBinding.addPropertyMediaLayout.detailCustomViewRecyclerView.setAdapter(adapter);
    }

    private void setTextForCustomView(AddPropertyInformationCustomView customView, String text) {
        EditText editText = customView.findViewById(R.id.add_property_information_custom_view_value);
        editText.setText(text);
    }

    public void configureOnClickRecyclerView() {
        RecyclerViewItemClickUtil.addTo(mBinding.addPropertyMediaLayout.detailCustomViewRecyclerView, R.layout.fragment_add_property)
                .setOnItemClickListener((recyclerView, position, v) -> {
                    if (position==0) Toast.makeText(requireContext(),"add",Toast.LENGTH_SHORT).show();

                });
    }

}
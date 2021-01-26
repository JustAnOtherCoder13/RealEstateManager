package com.openclassrooms.realestatemanager.presentation.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentPropertyListBinding;
import com.openclassrooms.realestatemanager.presentation.ui.fragment.adapter.PropertyRecyclerViewAdapter;
import com.openclassrooms.realestatemanager.presentation.ui.main.BaseFragment;
import com.openclassrooms.realestatemanager.presentation.utils.RecyclerViewItemClickListener;
import com.picone.core.domain.entity.Property;
import com.picone.core.domain.entity.PropertyFactory;

import java.util.ArrayList;
import java.util.List;


public class PropertyListFragment extends BaseFragment {

    private FragmentPropertyListBinding mBinding;
    private PropertyRecyclerViewAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentPropertyListBinding.inflate(inflater, container, false);
        mNavController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        setAppBarVisibility(true);
        initRecyclerView();
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        configureOnClickRecyclerView();
        //mPropertyViewModel.setSelectedProperty_(new PropertyFactory());
        mPropertyViewModel.getSelectedProperty_.observe(getViewLifecycleOwner(), property -> {
            if (property.property != null && property.property.getAddress()!=null) {
                Log.i("TAG", "onViewCreated: "+property.property.getAddress());
                //mPropertyViewModel.setAllPointOfInterestForProperty(property);
                mNavController.navigate(R.id.action_propertyListFragment_to_propertyDetailFragment);
            }
        });
    }

    private void initRecyclerView() {
        mPropertyViewModel.setAllPropertiesAndAllValues();
        PropertyRecyclerViewAdapter adapter = new PropertyRecyclerViewAdapter(new ArrayList<>(),requireContext());
        RecyclerView.LayoutManager linearLayout = new LinearLayoutManager(getContext());
        setCurrencySwitch(adapter);
        mBinding.fragmentPropertyListRecyclerview.setLayoutManager(linearLayout);
        mBinding.fragmentPropertyListRecyclerview.setAdapter(adapter);
        mPropertyViewModel.getAllProperties_.observe(getViewLifecycleOwner(), adapter::updateProperties);
    }

    public void configureOnClickRecyclerView() {
        RecyclerViewItemClickListener.addTo(mBinding.fragmentPropertyListRecyclerview, R.layout.fragment_property_list)
                .setOnItemClickListener((recyclerView, position, v) -> {
                    List<PropertyFactory> allProperties = mPropertyViewModel.getAllProperties_.getValue();
                    assert allProperties != null;
                    PropertyFactory property = allProperties.get(position);
                    mPropertyViewModel.setSelectedProperty_(property);
                });
    }


}

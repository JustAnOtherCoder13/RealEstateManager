package com.openclassrooms.realestatemanager.presentation.ui.fragment;

import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class PropertyListFragment extends BaseFragment {

    private FragmentPropertyListBinding mBinding;
    private PropertyRecyclerViewAdapter mAdapter;

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
        mPropertyViewModel.getSelectedProperty.observe(getViewLifecycleOwner(), property -> {
            mAdapter.updateSelectedProperty(property);
            if (property.propertyInformation != null && property.propertyLocation.getAddress() != null && getResources().getBoolean(R.bool.phone_device)&& Objects.requireNonNull(mNavController.getCurrentDestination()).getId()==R.id.propertyListFragment)
                mNavController.navigate(R.id.action_propertyListFragment_to_propertyDetailFragment);
            else if (property.propertyInformation != null && property.propertyLocation.getAddress() != null && !getResources().getBoolean(R.bool.phone_device))
                mNavController.navigate(R.id.propertyDetailFragment);
        });
    }

    private void initRecyclerView() {
         mAdapter = new PropertyRecyclerViewAdapter(new ArrayList<>(), requireContext());
        RecyclerView.LayoutManager linearLayout = new LinearLayoutManager(getContext());
        setCurrencySwitch(mAdapter);
        mBinding.fragmentPropertyListRecyclerview.setLayoutManager(linearLayout);
        mBinding.fragmentPropertyListRecyclerview.setAdapter(mAdapter);
        mPropertyViewModel.getAllProperties.observe(getViewLifecycleOwner(), mAdapter::updateProperties);
    }

    public void configureOnClickRecyclerView() {
        RecyclerViewItemClickListener.addTo(mBinding.fragmentPropertyListRecyclerview, R.layout.fragment_property_list)
                .setOnItemClickListener((recyclerView, position, v) -> {
                    List<Property> allProperties = mPropertyViewModel.getAllProperties.getValue();
                    assert allProperties != null;
                    Property property = allProperties.get(position);
                    mPropertyViewModel.setSelectedProperty(property);
                });
    }
}

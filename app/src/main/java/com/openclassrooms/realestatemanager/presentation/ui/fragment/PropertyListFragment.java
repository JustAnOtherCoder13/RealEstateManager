package com.openclassrooms.realestatemanager.presentation.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentPropertyListBinding;
import com.openclassrooms.realestatemanager.presentation.ui.fragment.adapter.PropertyRecyclerViewAdapter;
import com.openclassrooms.realestatemanager.presentation.ui.main.BaseFragment;
import com.openclassrooms.realestatemanager.presentation.utils.RecyclerViewItemClickUtil;
import com.picone.core.domain.entity.Property;

import java.util.ArrayList;
import java.util.List;


public class PropertyListFragment extends BaseFragment {

    private FragmentPropertyListBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentPropertyListBinding.inflate(inflater, container, false);
        mNavController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        setAppBarVisibility(true);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView();
        configureOnClickRecyclerView();
        mPropertyViewModel.getSelectedProperty.observe(getViewLifecycleOwner(),property -> {
            if (property!=null) mNavController.navigate(R.id.propertyDetailFragment);

        });
    }

    private void initRecyclerView() {
        PropertyRecyclerViewAdapter adapter = new PropertyRecyclerViewAdapter(new ArrayList<>());
        RecyclerView.LayoutManager linearLayout = new LinearLayoutManager(getContext());
        mBinding.fragmentPropertyListRecyclerview.setLayoutManager(linearLayout);
        mBinding.fragmentPropertyListRecyclerview.setAdapter(adapter);
        mPropertyViewModel.getAllRoomProperties.observe(getViewLifecycleOwner(), adapter::updateProperties);
    }

    public void configureOnClickRecyclerView() {
        RecyclerViewItemClickUtil.addTo(mBinding.fragmentPropertyListRecyclerview, R.layout.fragment_property_list)
                .setOnItemClickListener((recyclerView, position, v) -> {
                    List<Property> allProperties = mPropertyViewModel.getAllRoomProperties.getValue();
                    assert allProperties != null;
                    Property property = allProperties.get(position);
                    mPropertyViewModel.setSelectedProperty(property);
                });
    }
}

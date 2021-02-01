package com.openclassrooms.realestatemanager.presentation.utils.customView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.presentation.ui.main.MainActivity;
import com.openclassrooms.realestatemanager.presentation.viewModels.PropertyViewModel;
import com.picone.core.domain.entity.Property;

import java.util.Objects;

public class TopAppBarCustomView extends ConstraintLayout {

    private PropertyViewModel propertyViewModel;
    private BottomSheetBehavior<ConstraintLayout> bottomSheetBehavior;
    public ImageButton resetFilterButton;
    public ImageButton addPropertyButton;
    public SwitchMaterial currencySwitch;

    public TopAppBarCustomView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        propertyViewModel = new ViewModelProvider((MainActivity) context).get(PropertyViewModel.class);
        initView(context);
    }

    private void initView(Context context) {
        inflate(getContext(), R.layout.custom_view_top_nav_bar, this);
        addPropertyButton = findViewById(R.id.top_bar_add_property);
        ImageButton filterButton = findViewById(R.id.top_bar_filter_icon);
        resetFilterButton = findViewById(R.id.top_reset_filter_property);
        currencySwitch = findViewById(R.id.currency_switch);

        filterButton.setOnClickListener(v -> {
            bottomSheetBehavior.setState(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED ?
                    BottomSheetBehavior.STATE_EXPANDED : BottomSheetBehavior.STATE_COLLAPSED);
            propertyViewModel.setAllProperties();
        });

        addPropertyButton.setOnClickListener(v -> initClickForAdd((MainActivity)context));
    }

    private void initClickForAdd(MainActivity context) {
        resetPropertyValues();
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        NavController navController = Navigation.findNavController(context, R.id.nav_host_fragment);
        switch (Objects.requireNonNull(navController.getCurrentDestination()).getId()) {
            case R.id.propertyListFragment:
                navController.navigate
                        (R.id.action_propertyListFragment_to_addPropertyFragment);
                break;

            case R.id.mapsFragment:
                navController.navigate
                        (R.id.action_mapsFragment_to_addPropertyFragment);
                break;

            default:
                navController.navigate
                        (R.id.addPropertyFragment);
        }
    }

    public void setBottomSheetBehavior(BottomSheetBehavior<ConstraintLayout> bottomSheetBehavior) {
        this.bottomSheetBehavior = bottomSheetBehavior;
    }

    private void resetPropertyValues() {
        propertyViewModel.setSelectedProperty(new Property());
    }
}

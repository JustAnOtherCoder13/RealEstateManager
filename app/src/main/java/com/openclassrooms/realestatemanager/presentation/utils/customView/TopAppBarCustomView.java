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

    private PropertyViewModel mPropertyViewModel;
    private BottomSheetBehavior<ConstraintLayout> mBottomSheetBehavior;
    public ImageButton mResetFilterButton, mAddPropertyButton;
    public SwitchMaterial mCurrencySwitch;

    public TopAppBarCustomView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPropertyViewModel = new ViewModelProvider((MainActivity) context).get(PropertyViewModel.class);
        initView(context);
    }

    private void initView(Context context) {
        inflate(getContext(), R.layout.custom_view_top_nav_bar, this);
        mAddPropertyButton = findViewById(R.id.top_bar_add_property);
        ImageButton filterButton = findViewById(R.id.top_bar_filter_icon);
        mResetFilterButton = findViewById(R.id.top_reset_filter_property);
        mCurrencySwitch = findViewById(R.id.currency_switch);

        filterButton.setOnClickListener(v -> {
            mBottomSheetBehavior.setState(mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED ?
                    BottomSheetBehavior.STATE_EXPANDED : BottomSheetBehavior.STATE_COLLAPSED);
            mPropertyViewModel.setAllProperties();
        });

        mAddPropertyButton.setOnClickListener(v -> initAddButtonClick((MainActivity) context));
    }

    private void initAddButtonClick(MainActivity context) {
        resetPropertyValues();
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
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
        this.mBottomSheetBehavior = bottomSheetBehavior;
    }

    private void resetPropertyValues() {
        mPropertyViewModel.setSelectedProperty(new Property());
    }
}

package com.openclassrooms.realestatemanager.presentation.ui.main;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding;
import com.openclassrooms.realestatemanager.presentation.viewModels.AgentViewModel;
import com.openclassrooms.realestatemanager.presentation.viewModels.PropertyViewModel;
import com.picone.core.domain.entity.Property;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;
import dagger.hilt.android.scopes.ActivityScoped;

@ActivityScoped
@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mBinding;
    private PropertyViewModel mPropertyViewModel;
    private AgentViewModel mAgentViewModel;
    private NavController mNavController;
    protected ImageButton mUpdateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        mUpdateButton = mBinding.updateButtonCustomView.findViewById(R.id.custom_view_update_image_button);
        mPropertyViewModel = new ViewModelProvider(this).get(PropertyViewModel.class);
        mAgentViewModel = new ViewModelProvider(this).get(AgentViewModel.class);
        mNavController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(mBinding.bottomNavBar, mNavController);
        initTopAppBar();
        mAgentViewModel.setAgent();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (Objects.requireNonNull(mNavController.getCurrentDestination()).getId() != R.id.propertyDetailFragment)
            mPropertyViewModel.setSelectedProperty(new Property());
    }

    protected void setMenuVisibility(@NonNull Boolean isVisible) {
        mBinding.topAppBar.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        mBinding.bottomNavBar.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        mBinding.updateButtonCustomView.setVisibility(isVisible ? View.GONE : View.VISIBLE);
    }

    protected void initUpdateButton(boolean isForUpdate) {
        mUpdateButton.setImageResource(isForUpdate ?
                R.drawable.ic_custom_view_update_24
                : R.drawable.ic_custom_view_save_24);

        if (isForUpdate) mUpdateButton.setOnClickListener
                (v -> mNavController.navigate
                        (R.id.action_propertyDetailFragment_to_addPropertyFragment));
    }

    private void initTopAppBar() {
        ImageButton addPropertyBtn = mBinding.topAppBar.findViewById(R.id.top_bar_add_property);
        addPropertyBtn.setOnClickListener(v -> {
            switch (Objects.requireNonNull(mNavController.getCurrentDestination()).getId()) {
                case R.id.propertyListFragment:
                    mNavController.navigate
                            (R.id.action_propertyListFragment_to_addPropertyFragment);
                    break;

                case R.id.mapsFragment:
                    mNavController.navigate
                            (R.id.action_mapsFragment_to_addPropertyFragment);
                    break;

                default:
                    mNavController.navigate
                            (R.id.addPropertyFragment);
            }
        });
    }
}

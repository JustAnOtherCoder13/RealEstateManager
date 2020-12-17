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
import com.openclassrooms.realestatemanager.presentation.viewModels.PropertyViewModel;

import dagger.hilt.android.AndroidEntryPoint;
import dagger.hilt.android.scopes.ActivityScoped;

@ActivityScoped
@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mBinding;
    private PropertyViewModel mPropertyViewModel;
    private NavController mNavController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        mPropertyViewModel = new  ViewModelProvider(this).get(PropertyViewModel.class);
        mNavController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(mBinding.bottomNavBar,mNavController);
        initTopAppBar();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mPropertyViewModel.setSelectedProperty(null);
    }

    protected void setMenuVisibility(@NonNull Boolean isVisible) {
        if (isVisible) {
            mBinding.bottomNavBar.setVisibility(View.VISIBLE);
            mBinding.topAppBar.setVisibility(View.VISIBLE);
        } else {
            mBinding.bottomNavBar.setVisibility(View.GONE);
            mBinding.topAppBar.setVisibility(View.GONE);
        }
    }

    private void initTopAppBar(){
        ImageButton addPropertyBtn = mBinding.topAppBar.findViewById(R.id.top_bar_add_property);
        addPropertyBtn.setOnClickListener(v -> mNavController.navigate(R.id.addPropertyFragment));
    }
}

package com.openclassrooms.realestatemanager.presentation.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding;
import com.openclassrooms.realestatemanager.presentation.ui.fragment.PropertyDetailFragment;
import com.openclassrooms.realestatemanager.presentation.utils.Utils;
import com.openclassrooms.realestatemanager.presentation.viewModels.PropertyViewModel;

import dagger.hilt.android.AndroidEntryPoint;
import dagger.hilt.android.scopes.ActivityScoped;

@ActivityScoped
@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mBinding;
    private PropertyViewModel mPropertyViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        mPropertyViewModel = new  ViewModelProvider(this).get(PropertyViewModel.class);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(mBinding.bottomNavBar,navController);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mPropertyViewModel.setSelectedProperty(null);
    }

    protected void setMenuVisibility(@NonNull Boolean isVisible) {
        if (isVisible) {
            mBinding.bottomNavBar.setVisibility(View.VISIBLE);
        } else {
            mBinding.bottomNavBar.setVisibility(View.GONE);
        }
    }
}

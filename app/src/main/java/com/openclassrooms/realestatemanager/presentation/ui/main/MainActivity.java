package com.openclassrooms.realestatemanager.presentation.ui.main;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.airbnb.lottie.LottieAnimationView;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding;
import com.openclassrooms.realestatemanager.presentation.viewModels.AgentViewModel;
import com.openclassrooms.realestatemanager.presentation.viewModels.PropertyViewModel;
import com.picone.core.domain.entity.Property;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;
import dagger.hilt.android.scopes.ActivityScoped;

import static com.openclassrooms.realestatemanager.presentation.utils.Utils.isGpsAvailable;

@ActivityScoped
@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mBinding;
    private PropertyViewModel mPropertyViewModel;
    private AgentViewModel mAgentViewModel;
    private NavController mNavController;
    protected ImageButton mUpdateButton;
    protected LottieAnimationView mLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        initValues();
        initLoader();
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY))
            Toast.makeText(this,"camera available",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (Objects.requireNonNull(mNavController.getCurrentDestination()).getId() != R.id.propertyDetailFragment)
            mPropertyViewModel.setSelectedProperty(new Property());
    }

    private void initValues() {
        mUpdateButton = mBinding.updateButtonCustomView.findViewById(R.id.custom_view_update_image_button);
        mPropertyViewModel = new ViewModelProvider(this).get(PropertyViewModel.class);
        mAgentViewModel = new ViewModelProvider(this).get(AgentViewModel.class);
        mNavController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(mBinding.bottomNavBar, mNavController);
        mAgentViewModel.setAgent();
        if (!isGpsAvailable(this))Toast.makeText(this,R.string.gps_warning_message,Toast.LENGTH_LONG).show();

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

    protected void hideSoftKeyboard(@NonNull View view){
        InputMethodManager imm =(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    private void initLoader(){
        mLoader = mBinding.loader.animationView;
        mLoader.setAnimation(R.raw.loader);
        mLoader.setVisibility(View.GONE);
    }



    protected void playLoader(boolean isVisible){
        mLoader.setVisibility(isVisible? View.VISIBLE : View.GONE);
        if (isVisible)mLoader.playAnimation();
        else mLoader.pauseAnimation();
    }
}

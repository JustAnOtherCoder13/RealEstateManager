package com.openclassrooms.realestatemanager.presentation.ui.main;

import android.Manifest;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding;
import com.openclassrooms.realestatemanager.presentation.ui.fragment.adapter.PropertyRecyclerViewAdapter;
import com.openclassrooms.realestatemanager.presentation.utils.ErrorHandler;
import com.openclassrooms.realestatemanager.presentation.utils.FilterHelper;
import com.openclassrooms.realestatemanager.presentation.viewModels.AgentViewModel;
import com.openclassrooms.realestatemanager.presentation.viewModels.PropertyViewModel;
import com.picone.core.domain.entity.Property;

import java.util.Locale;
import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;
import dagger.hilt.android.scopes.ActivityScoped;

import static com.openclassrooms.realestatemanager.presentation.utils.Utils.isGpsAvailable;
import static com.picone.core.utils.ConstantParameters.CAMERA_PERMISSION_CODE;
import static com.picone.core.utils.ConstantParameters.LOCATION_PERMISSION_CODE;
import static com.picone.core.utils.ConstantParameters.READ_PERMISSION_CODE;
import static com.picone.core.utils.ConstantParameters.WRITE_PERMISSION_CODE;

@ActivityScoped
@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mBinding;
    private PropertyViewModel mPropertyViewModel;
    private NavController mNavController;
    private FilterHelper mFilterHelper;
    private BottomSheetBehavior<ConstraintLayout> mBottomSheetBehavior;

    protected ImageButton mUpdateButton;
    protected ImageButton mAddButton;
    protected LottieAnimationView mLoader;
    protected boolean mIsCameraPermissionGranted, mIsLocationPermissionGranted, mIsReadPermissionGranted, mIsWritePermissionGranted, mIsPhone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        mAddButton = mBinding.topAppBar.mAddPropertyButton;
        mUpdateButton = mBinding.updateButtonCustomView.mUpdateButton;
        setContentView(mBinding.getRoot());
        initLoader();
        checkCameraDevice();
        checkGpsEnable();
        askLocationPermission();
        mIsPhone = getResources().getBoolean(R.bool.phone_device);
        initValues();
    }

    @Override
    public void onBackPressed() {
        if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        //reset selected property if not on add or detail fragment
        if (Objects.requireNonNull(mNavController.getCurrentDestination()).getId() != R.id.addPropertyFragment)
            mPropertyViewModel.setSelectedProperty(new Property());
        //set back press nav
        if (mNavController.getCurrentDestination() != null && mIsPhone)
            setPhoneBackNavigation();
        else if (mNavController.getCurrentDestination() != null && !mIsPhone)
            setTabBackNavigation();
        else super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mBottomSheetBehavior = BottomSheetBehavior.from(mBinding.bottomSheetLayout.bottomSheet);
        mBottomSheetBehavior.setDraggable(false);
        mBinding.topAppBar.setBottomSheetBehavior(mBottomSheetBehavior);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_CODE:
                mIsLocationPermissionGranted = checkResult(grantResults);
                if (mIsLocationPermissionGranted) askCameraPermission();
                break;
            case CAMERA_PERMISSION_CODE:
                mIsCameraPermissionGranted = checkResult(grantResults);
                if (mIsCameraPermissionGranted) askReadPermission();
                break;
            case READ_PERMISSION_CODE:
                mIsReadPermissionGranted = checkResult(grantResults);
                if (mIsReadPermissionGranted) askWritePermission();
                break;
            case WRITE_PERMISSION_CODE:
                mIsWritePermissionGranted = checkResult(grantResults);
                break;
        }
    }

    private void initValues() {
        mPropertyViewModel = new ViewModelProvider(this).get(PropertyViewModel.class);
        AgentViewModel agentViewModel = new ViewModelProvider(this).get(AgentViewModel.class);
        mFilterHelper = new FilterHelper(mBinding.bottomSheetLayout);
        mNavController = Navigation.findNavController(this, R.id.nav_host_fragment);
        agentViewModel.setAgent();
        mPropertyViewModel.setAllProperties();
        mPropertyViewModel.getErrorState.observe(this,errorHandler -> {
            if (errorHandler.equals(ErrorHandler.ON_ERROR)) {
                Toast.makeText(this, ErrorHandler.ON_ERROR.label, Toast.LENGTH_LONG).show();
                playLoader(false);
            }
        });
        initView();
        initBottomSheetLocationFilter();
        setBottomSheetButtonClickListener();
    }

    @SuppressWarnings("ConstantConditions")//can't be null on phone
    private void initView() {
        if (getResources().getBoolean(R.bool.phone_device)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            NavigationUI.setupWithNavController(mBinding.bottomNavBar, mNavController);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    //-------------------------- COMPONENT --------------------------------

    protected void setTopAppBarCurrencySwitch(@NonNull PropertyRecyclerViewAdapter adapter) {
        mBinding.topAppBar.mCurrencySwitch.setOnClickListener(v -> {
            assert mBinding.topAppBar.mCurrencySwitch != null;
            mPropertyViewModel.setLocale(mBinding.topAppBar.mCurrencySwitch.isChecked() ?
                    Locale.FRANCE
                    : Locale.US);
        });
        mPropertyViewModel.getLocale.observe(this, adapter::updateLocale);
    }

    private void setBottomSheetButtonClickListener() {

        mBinding.bottomSheetLayout.bottomSheetCloseButton.setOnClickListener(v -> {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            mBinding.topAppBar.mResetFilterButton.setVisibility(View.GONE);
        });

        mBinding.bottomSheetLayout.bottomSheetOkButton.setOnClickListener(v -> {
            mFilterHelper.filterProperties(mPropertyViewModel.getAllProperties.getValue());
            mPropertyViewModel.setFilteredProperty(mFilterHelper.getFilteredPropertyInformation());

            mBinding.topAppBar.mResetFilterButton.setVisibility(View.VISIBLE);
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            mFilterHelper.resetFilter();
            mBinding.topAppBar.mResetFilterButton.setOnClickListener(v1 ->
                    mBinding.topAppBar.mResetFilterButton.setVisibility(View.GONE));
        });
    }

    private void initBottomSheetLocationFilter() {
        mPropertyViewModel.setAllRegionForAllProperties();
        mPropertyViewModel.getKnownRegions.observe(this, regions ->
                mBinding.bottomSheetLayout.filterPropertyLocationSpinner.setSpinnerAdapter(regions));
    }

    //-------------------------- NAVIGATION --------------------------------

    private void setTabBackNavigation() {
        switch (Objects.requireNonNull(mNavController.getCurrentDestination()).getId()) {
            case R.id.addPropertyFragment:
                mNavController.navigate(Objects.requireNonNull
                        (mPropertyViewModel.getSelectedProperty.getValue()).propertyInformation!=null ?
                        R.id.action_addPropertyFragment_to_propertyDetailFragment
                        :R.id.action_addPropertyFragment_to_mapsFragment);
                break;
            case R.id.propertyDetailFragment:
                mNavController.navigate(R.id.action_propertyDetailFragment_to_mapsFragment);
                break;
            case R.id.mapsFragment:
                this.finish();
        }
    }

    @SuppressWarnings("ConstantConditions")//already checked
    private void setPhoneBackNavigation() {
        switch (mNavController.getCurrentDestination().getId()) {
            case R.id.addPropertyFragment:
                mNavController.navigate(mPropertyViewModel.getSelectedProperty.getValue().propertyLocation!=null?
                        R.id.action_addPropertyFragment_to_propertyDetailFragment
                        :R.id.action_addPropertyFragment_to_propertyListFragment);
                break;
            case R.id.propertyDetailFragment:
                mNavController.navigate(R.id.action_propertyDetailFragment_to_propertyListFragment);
                break;
            case R.id.propertyListFragment:
                mNavController.navigate(R.id.action_propertyListFragment_to_mapsFragment);
                break;
            case R.id.mapsFragment:
                this.finish();
                break;
        }
    }

    //-------------------------- METHOD EXPOSED TO BASE FRAGMENT --------------------------------

    @SuppressWarnings("ConstantConditions")//can't be null for phone
    protected void setMenuVisibility(@NonNull Boolean isVisible) {
        if (mIsPhone)
            mBinding.bottomNavBar.setVisibility(isVisible ? View.VISIBLE : View.GONE);

        mBinding.topAppBar.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        mBinding.updateButtonCustomView.setVisibility(isVisible ? View.GONE : View.VISIBLE);
    }

    protected void setUpdateButtonCustomViewVisibility(boolean isVisible){
        mBinding.updateButtonCustomView.setVisibility(isVisible?View.VISIBLE:View.GONE);
    }

    protected void initUpdateButton(boolean isForUpdate) {
        mUpdateButton.setImageResource(isForUpdate ?
                R.drawable.ic_custom_view_update_24
                : R.drawable.ic_custom_view_save_24);

        if (isForUpdate) mUpdateButton.setOnClickListener
                (v -> mNavController.navigate
                        (R.id.action_propertyDetailFragment_to_addPropertyFragment));

    }

    protected void setSaveButtonClickListener(View.OnClickListener clickListener) {
        mUpdateButton.setEnabled(true);
        mUpdateButton.setOnClickListener(clickListener);
    }

    protected void hideSoftKeyboard(@NonNull View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    protected void playLoader(boolean isVisible) {
        mLoader.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        if (isVisible) mLoader.playAnimation();
        else mLoader.pauseAnimation();
    }

    //-------------------------- PERMISSIONS --------------------------------

    private boolean checkResult(@NonNull int[] grantResults) {
        return grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
    }

    private void askLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);

        else {
            mIsLocationPermissionGranted = true;
            askCameraPermission();
        }
    }

    private void askCameraPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        } else {
            mIsCameraPermissionGranted = true;
            askReadPermission();
        }
    }

    private void askReadPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_PERMISSION_CODE);

        else {
            mIsReadPermissionGranted = true;
            askWritePermission();
        }
    }

    private void askWritePermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_PERMISSION_CODE);

        else { mIsWritePermissionGranted = true; }
    }

    //-----------------------------HELPERS------------------------------------

    private void initLoader() {
        mLoader = mBinding.loader.animationView;
        mLoader.setAnimation(R.raw.loader);
        mLoader.setVisibility(View.GONE);
    }

    private void checkCameraDevice() {
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY))
            Toast.makeText(this, R.string.no_camera_warning, Toast.LENGTH_LONG).show();
    }

    private void checkGpsEnable() {
        if (!isGpsAvailable(this))
            Toast.makeText(this, R.string.gps_warning_message, Toast.LENGTH_LONG).show();
    }
}

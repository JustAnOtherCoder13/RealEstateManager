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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding;
import com.openclassrooms.realestatemanager.presentation.ui.fragment.adapter.PropertyRecyclerViewAdapter;
import com.openclassrooms.realestatemanager.presentation.utils.FilterHelper;
import com.openclassrooms.realestatemanager.presentation.utils.RecyclerViewItemClickListener;
import com.openclassrooms.realestatemanager.presentation.viewModels.AgentViewModel;
import com.openclassrooms.realestatemanager.presentation.viewModels.PropertyViewModel;
import com.picone.core.domain.entity.Property;

import java.util.ArrayList;
import java.util.List;
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
    private AgentViewModel mAgentViewModel;
    private NavController mNavController;
    protected ImageButton mUpdateButton;
    protected ImageButton mAddButton;
    protected LottieAnimationView mLoader;
    protected boolean isCameraPermissionGranted, isLocationPermissionGranted, isReadPermissionGranted, isWritePermissionGranted, isPhone;
    private FilterHelper filterHelper;
    private BottomSheetBehavior<ConstraintLayout> mBottomSheetBehavior;
    private PropertyRecyclerViewAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        mAddButton = mBinding.topAppBar.addPropertyButton;
        mUpdateButton = mBinding.updateButtonCustomView.updateButton;
        setContentView(mBinding.getRoot());
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
        if (mNavController.getCurrentDestination() != null && isPhone) setPhoneBackNavigation();
        else if (mNavController.getCurrentDestination() != null && !isPhone) setTabBackNavigation();
        else super.onBackPressed();
    }

    private void setTabBackNavigation() {
        switch (Objects.requireNonNull(mNavController.getCurrentDestination()).getId()) {
            case R.id.addPropertyFragment:
                mNavController.navigate(R.id.propertyDetailFragment);
                break;
            case R.id.propertyDetailFragment:
                mNavController.navigate(R.id.mapsFragment);
                break;
            case R.id.mapsFragment:
                this.finish();
        }
    }


    @SuppressWarnings("ConstantConditions")//already checked
    private void setPhoneBackNavigation() {
        switch (mNavController.getCurrentDestination().getId()) {
            case R.id.addPropertyFragment:
                mNavController.navigate(mPropertyViewModel.getSelectedProperty.getValue().getAddress()!=null?
                        R.id.propertyDetailFragment
                        :R.id.propertyListFragment);
                break;
            case R.id.propertyDetailFragment:
                mNavController.navigate(R.id.propertyListFragment);
                break;
            case R.id.propertyListFragment:
                mNavController.navigate(R.id.mapsFragment);
                break;
            case R.id.mapsFragment:
                this.finish();
                break;
        }
    }

    @Override
    protected void onResume() {
        initLoader();
        askLocationPermission();
        isPhone = getResources().getBoolean(R.bool.phone_device);
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY))
            Toast.makeText(this, R.string.no_camera_warning, Toast.LENGTH_LONG).show();
        super.onResume();
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
                isLocationPermissionGranted = checkResult(grantResults);
                if (isLocationPermissionGranted) askCameraPermission();
                break;
            case CAMERA_PERMISSION_CODE:
                isCameraPermissionGranted = checkResult(grantResults);
                if (isCameraPermissionGranted) askReadPermission();
                break;
            case READ_PERMISSION_CODE:
                isReadPermissionGranted = checkResult(grantResults);
                if (isReadPermissionGranted) askWritePermission();
                break;
            case WRITE_PERMISSION_CODE:
                isWritePermissionGranted = checkResult(grantResults);
                break;
        }
    }

    @SuppressWarnings("ConstantConditions")//can't be null on phone
    private void initPhoneOrTablet() {
        if (getResources().getBoolean(R.bool.phone_device)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            NavigationUI.setupWithNavController(mBinding.bottomNavBar, mNavController);

        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            initRecyclerView();
            configureOnClickRecyclerView();
            mBinding.currencySwitch.setOnClickListener(v -> {
                adapter.updateLocale(mBinding.currencySwitch.isChecked() ?
                        Locale.FRANCE
                        : Locale.US);
            });
        }
    }

    protected void setTopAppBarCurrencySwitch(@NonNull PropertyRecyclerViewAdapter adapter) {
        mBinding.topAppBar.currencySwitch.setOnClickListener(v -> {
            assert mBinding.currencySwitch != null;
            mPropertyViewModel.setLocale(mBinding.currencySwitch.isChecked() ?
                    Locale.FRANCE
                    : Locale.US);
        });
        mPropertyViewModel.getLocale.observe(this, adapter::updateLocale);
    }

    private boolean checkResult(@NonNull int[] grantResults) {
        return grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
    }

    private void askLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);

        else {
            isLocationPermissionGranted = true;
            askCameraPermission();
        }
    }

    private void askCameraPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        } else {
            isCameraPermissionGranted = true;
            askReadPermission();
        }
    }

    private void askReadPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_PERMISSION_CODE);

        else {
            isReadPermissionGranted = true;
            askWritePermission();
        }
    }

    private void askWritePermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_PERMISSION_CODE);

        else {
            isWritePermissionGranted = true;
        }
    }


    private void initValues() {
        mPropertyViewModel = new ViewModelProvider(this).get(PropertyViewModel.class);
        mAgentViewModel = new ViewModelProvider(this).get(AgentViewModel.class);
        mNavController = Navigation.findNavController(this, R.id.nav_host_fragment);
        initPhoneOrTablet();
        mAgentViewModel.setAgent();
        mPropertyViewModel.setAllProperties();
        filterHelper = new FilterHelper(mBinding.bottomSheetLayout);
        setBottomSheetButtonClickListener();
        initBottomSheetLocationFilter();

        if (!isGpsAvailable(this))
            Toast.makeText(this, R.string.gps_warning_message, Toast.LENGTH_LONG).show();

    }

    @SuppressWarnings("ConstantConditions")//already checked
    private void initRecyclerView() {
        adapter = new PropertyRecyclerViewAdapter(new ArrayList<>(), this);
        RecyclerView.LayoutManager linearLayout = new LinearLayoutManager(this);
        mBinding.fragmentPropertyListRecyclerview.setLayoutManager(linearLayout);
        mBinding.fragmentPropertyListRecyclerview.setAdapter(adapter);
        mPropertyViewModel.getAllProperties.observe(this, properties -> {
            adapter.updateProperties(properties);
        });

        mPropertyViewModel.getSelectedProperty.observe(this, property -> {
            if (property.getAddress() != null) {
                adapter.updateSelectedProperty(property);
                mPropertyViewModel.setAllPointOfInterestForProperty(property);
                if (isPhone)
                    mNavController.navigate(R.id.action_mapsFragment_to_propertyDetailFragment);
                else {
                    mNavController.navigate(R.id.propertyDetailFragment);
                }
            } else adapter.updateSelectedProperty(new Property());
        });
    }

    @SuppressWarnings("ConstantConditions")//already checked
    public void configureOnClickRecyclerView() {
        RecyclerViewItemClickListener.addTo(mBinding.fragmentPropertyListRecyclerview, R.layout.fragment_property_list)
                .setOnItemClickListener((recyclerView, position, v) -> {
                    List<Property> allProperties = mPropertyViewModel.getAllProperties.getValue();
                    assert allProperties != null;
                    Property property = allProperties.get(position);
                    mPropertyViewModel.setSelectedProperty(property);
                });
    }

    private void setBottomSheetButtonClickListener() {

        mBinding.bottomSheetLayout.bottomSheetCloseButton.setOnClickListener(v -> {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            mBinding.topAppBar.resetFilterButton.setVisibility(View.GONE);
        });

        mBinding.bottomSheetLayout.bottomSheetOkButton.setOnClickListener(v -> {
            filterHelper.filterProperties(mPropertyViewModel.getAllProperties.getValue());
            mPropertyViewModel.setFilteredProperty(filterHelper.getFilteredProperty());

            mBinding.topAppBar.resetFilterButton.setVisibility(View.VISIBLE);
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            filterHelper.resetFilter();
            mBinding.topAppBar.resetFilterButton.setOnClickListener(v1 -> {
                mPropertyViewModel.setAllProperties();
                mBinding.topAppBar.resetFilterButton.setVisibility(View.GONE);
            });
        });
    }

    private void initBottomSheetLocationFilter() {
        mPropertyViewModel.setAllPointOfInterestForAllProperties();
        mPropertyViewModel.setAllPhotoForAllProperties();
        mPropertyViewModel.setAllRegionForAllProperties();

        mPropertyViewModel.getAllPhotosForAllProperties.observe(this,
                filterHelper::updateAllPropertyPhotos);

        mPropertyViewModel.getAllPointOfInterestForAllProperties.observe(this,
                filterHelper::updateAllPropertyPointOfInterest);

        mPropertyViewModel.getKnownRegions.observe(this, regions ->
                mBinding.bottomSheetLayout.filterPropertyLocationSpinner.setSpinnerAdapter(regions));
    }

    @SuppressWarnings("ConstantConditions")//can't be null for phone
    protected void setMenuVisibility(@NonNull Boolean isVisible) {
        if (isPhone)
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
        mUpdateButton.setOnClickListener(clickListener);
    }

    protected void hideSoftKeyboard(@NonNull View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    private void initLoader() {
        mLoader = mBinding.loader.animationView;
        mLoader.setAnimation(R.raw.loader);
        mLoader.setVisibility(View.GONE);
    }


    protected void playLoader(boolean isVisible) {
        mLoader.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        if (isVisible) mLoader.playAnimation();
        else mLoader.pauseAnimation();
    }
}

package com.openclassrooms.realestatemanager.presentation.ui.main;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding;
import com.openclassrooms.realestatemanager.databinding.BottomSheetPropertyTypeLayoutBinding;
import com.openclassrooms.realestatemanager.databinding.CustomBottomSheetPointOfInterestLayoutBinding;
import com.openclassrooms.realestatemanager.presentation.ui.fragment.adapter.PropertyRecyclerViewAdapter;
import com.openclassrooms.realestatemanager.presentation.utils.ErrorHandler;
import com.openclassrooms.realestatemanager.presentation.utils.FilterHelper;
import com.openclassrooms.realestatemanager.presentation.viewModels.AgentViewModel;
import com.openclassrooms.realestatemanager.presentation.viewModels.PropertyViewModel;
import com.picone.core.domain.entity.Property;
import com.picone.core.domain.entity.RealEstateAgent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;
import dagger.hilt.android.scopes.ActivityScoped;

import static com.openclassrooms.realestatemanager.presentation.utils.Utils.isGpsAvailable;
import static com.picone.core.utils.ConstantParameters.CAMERA_PERMISSION_CODE;
import static com.picone.core.utils.ConstantParameters.LOCATION_PERMISSION_CODE;
import static com.picone.core.utils.ConstantParameters.MAX_PRICE;
import static com.picone.core.utils.ConstantParameters.MAX_ROOM;
import static com.picone.core.utils.ConstantParameters.MAX_SURFACE;
import static com.picone.core.utils.ConstantParameters.MIN_PRICE;
import static com.picone.core.utils.ConstantParameters.MIN_ROOM;
import static com.picone.core.utils.ConstantParameters.MIN_SURFACE;
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
    private ImageButton mUpdateButton;

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

        if (Objects.requireNonNull(mNavController.getCurrentDestination()).getId() == R.id.addPropertyFragment) {
            mPropertyViewModel.setAllProperties();
        }
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
        mFilterHelper = new FilterHelper();
        initPhotoSpinner();
        initRangeSliderValues();
        mNavController = Navigation.findNavController(this, R.id.nav_host_fragment);
        agentViewModel.setAgent();
        mPropertyViewModel.setAllProperties();
        mPropertyViewModel.getAllProperties.observe(this, properties -> {
            if (mPropertyViewModel.getSelectedProperty.getValue() != null && mPropertyViewModel.getSelectedProperty.getValue().propertyInformation != null)
                mPropertyViewModel.setSelectedProperty(getPropertyForId(String.valueOf(mPropertyViewModel.getSelectedProperty.getValue().propertyInformation.getId())));
        });
        mPropertyViewModel.getErrorState.observe(this, errorHandler -> {
            if (errorHandler.equals(ErrorHandler.ON_ERROR)) {
                Toast.makeText(this, ErrorHandler.ON_ERROR.label, Toast.LENGTH_LONG).show();
                playLoader(false);
            }
        });
        if (getResources().getBoolean(R.bool.phone_device)) {
            assert mBinding.bottomNavBar != null;
            NavigationUI.setupWithNavController(mBinding.bottomNavBar, mNavController);
        }

        agentViewModel.getAgent.observe(this,agent -> {
            if (agent.getName().trim().isEmpty()) initDialogForNewAgent(agentViewModel, agent);
        });
        initBottomSheet();
    }



    private void initPhotoSpinner() {
        List<String> numberOfPhotos = new ArrayList<>();
        for (int i = 1; i <= 10; i++) numberOfPhotos.add(String.valueOf(i));
        mBinding.bottomSheetLayout
                .filterPropertyNumberOfPhotoSpinner.setSpinnerAdapter(numberOfPhotos);
    }

    private void initRangeSliderValues() {
        mBinding.bottomSheetLayout
                .filterPropertyLocationPriceRangeSlider
                .setRangeSliderValue(
                        //Min price selectable
                        MIN_PRICE
                        //Max price selectable
                        , MAX_PRICE
                        //step
                        , (float) (MAX_PRICE - MIN_PRICE) / 10000);

        mBinding.bottomSheetLayout
                .filterPropertyLocationSurfaceRangerSlider
                .setRangeSliderValue(
                        //Min area selectable
                        MIN_SURFACE,
                        //Max area selectable
                        MAX_SURFACE,
                        //step
                        (float) (MAX_SURFACE - MIN_SURFACE) / 10);

        mBinding.bottomSheetLayout
                .filterPropertyLocationRoomRangerSlider
                .setRangeSliderValue(
                        //Min room
                        MIN_ROOM,
                        //Max room
                        MAX_ROOM,
                        //step
                        1);
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

    private void initBottomSheet() {
        initBottomSheetLocationFilter();
        initCloseButton();
        mBinding.bottomSheetLayout.bottomSheetOkButton.setOnClickListener(v -> {
            initFilter();
            filterByRegion();
            filterByNumberOfMedia();
            filterByPointOfInterests();
            filterByPropertyType();
            filterByOnMarketFrom();
            filterByPrice();
            filterBySurface();
            filterByRooms();
            resetBottomSheetValues();
            if (initWarningMessage()) return;
            initEndFilterValue();
        });
    }

    private void initCloseButton() {
        mBinding.bottomSheetLayout.bottomSheetCloseButton.setOnClickListener(v -> {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            mBinding.topAppBar.mResetFilterButton.setVisibility(View.GONE);
        });
    }

    private void initEndFilterValue() {
        mPropertyViewModel.setFilteredProperty(mFilterHelper.getFilteredProperties());
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        mBinding.topAppBar.mResetFilterButton.setVisibility(View.VISIBLE);
        mBinding.topAppBar.mResetFilterButton.setOnClickListener(v1 -> {
            mPropertyViewModel.setAllProperties();
            mBinding.topAppBar.mResetFilterButton.setVisibility(View.GONE);
        });
    }

    private boolean initWarningMessage() {
        if (mFilterHelper.getFilteredProperties().isEmpty()) {
            Toast.makeText(this, R.string.no_match_found, Toast.LENGTH_SHORT).show();
            return true;
        } else if (!mFilterHelper.getIsAnyFilterSelected()) {
            Toast.makeText(this, R.string.no_filter_selected, Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    private void filterByRooms() {
        if (Float.compare(mBinding.bottomSheetLayout.filterPropertyLocationRoomRangerSlider.getStartValue(), MIN_ROOM) == 1
                || Float.compare(mBinding.bottomSheetLayout.filterPropertyLocationRoomRangerSlider.getEndValue(), MAX_ROOM) == -1) {
            //filter for room
            mFilterHelper.filterByRoom(mBinding.bottomSheetLayout.filterPropertyLocationRoomRangerSlider.getStartValue(),
                    mBinding.bottomSheetLayout.filterPropertyLocationRoomRangerSlider.getEndValue());
        }
    }

    private void filterBySurface() {
        if (Float.compare(mBinding.bottomSheetLayout.filterPropertyLocationSurfaceRangerSlider.getStartValue(), MIN_SURFACE) == 1
                || Float.compare(mBinding.bottomSheetLayout.filterPropertyLocationSurfaceRangerSlider.getEndValue(), MAX_SURFACE) == -1) {
            mFilterHelper.filterBySurface(mBinding.bottomSheetLayout.filterPropertyLocationSurfaceRangerSlider.getStartValue(),
                    mBinding.bottomSheetLayout.filterPropertyLocationSurfaceRangerSlider.getEndValue());
        }
    }

    private void filterByPrice() {
        if (Float.compare(mBinding.bottomSheetLayout.filterPropertyLocationPriceRangeSlider.getStartValue(), MIN_PRICE) == 1
                || Float.compare(mBinding.bottomSheetLayout.filterPropertyLocationPriceRangeSlider.getEndValue(), MAX_PRICE) == -1) {
            mFilterHelper.filterByPrice(mBinding.bottomSheetLayout.filterPropertyLocationPriceRangeSlider.getStartValue(),
                    mBinding.bottomSheetLayout.filterPropertyLocationPriceRangeSlider.getEndValue());
        }
    }

    private void filterByOnMarketFrom() {
        if (!mBinding.bottomSheetLayout.bottomSheetOnMarketFrom.getDate().equalsIgnoreCase(getResources().getString(R.string.dd_mm_yyyy)))
            mFilterHelper.filterByOnMarketFrom(mBinding.bottomSheetLayout.bottomSheetOnMarketFrom.getDate());
    }

    private void filterByPropertyType() {
        if (mBinding.bottomSheetLayout.bottomSheetPropertyTypeLayoutInclude.houseCheckBox.mCheckBox.isChecked()
                || mBinding.bottomSheetLayout.bottomSheetPropertyTypeLayoutInclude.penthouseCheckBox.mCheckBox.isChecked()
                || mBinding.bottomSheetLayout.bottomSheetPropertyTypeLayoutInclude.flatCheckBox.mCheckBox.isChecked()
                || mBinding.bottomSheetLayout.bottomSheetPropertyTypeLayoutInclude.duplexCheckBox.mCheckBox.isChecked())
            mFilterHelper.filterByPropertyType();
    }

    private void filterByPointOfInterests() {
        if (mBinding.bottomSheetLayout.bottomSheetPointOfInterestInclude.schoolCheckBox.mCheckBox.isChecked()
                || mBinding.bottomSheetLayout.bottomSheetPointOfInterestInclude.restaurantCheckBox.mCheckBox.isChecked()
                || mBinding.bottomSheetLayout.bottomSheetPointOfInterestInclude.supermarketCheckBox.mCheckBox.isChecked())
            mFilterHelper.filterByPointOfInterest();
    }

    private void filterByNumberOfMedia() {
        if (!mBinding.bottomSheetLayout.filterPropertyNumberOfPhotoSpinner.getText().trim().isEmpty())
            mFilterHelper.filterByNumberOfMedias(mBinding.bottomSheetLayout.filterPropertyNumberOfPhotoSpinner.getText());
    }

    private void filterByRegion() {
        if (!mBinding.bottomSheetLayout.filterPropertyLocationSpinner.getText().trim().isEmpty())
            mFilterHelper.filterByRegion(mBinding.bottomSheetLayout.filterPropertyLocationSpinner.getText());
    }

    private void initFilter() {
        mFilterHelper.setRequestPointsOfInterests(requestPointOfInterest());
        mFilterHelper.setRequestPropertyType(requestPropertyType());
        mFilterHelper.initFilterValue(mPropertyViewModel.getAllProperties.getValue());
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
                        (mPropertyViewModel.getSelectedProperty.getValue()).propertyInformation != null ?
                        R.id.action_addPropertyFragment_to_propertyDetailFragment
                        : R.id.action_addPropertyFragment_to_mapsFragment);
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
                mNavController.navigate(mPropertyViewModel.getSelectedProperty.getValue().propertyLocation != null ?
                        R.id.action_addPropertyFragment_to_propertyDetailFragment
                        : R.id.action_addPropertyFragment_to_propertyListFragment);
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

    protected void setUpdateButtonCustomViewVisibility(boolean isVisible) {
        mBinding.updateButtonCustomView.setVisibility(isVisible ? View.VISIBLE : View.GONE);
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

    protected Property getPropertyForId(String propertyId) {
        Property propertyToReturn = new Property();
        for (Property property : Objects.requireNonNull(mPropertyViewModel.getAllProperties.getValue())) {
            if (String.valueOf(property.propertyInformation.getId()).equalsIgnoreCase(propertyId))
                propertyToReturn = property;
        }
        return propertyToReturn;
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
        else {
            mIsWritePermissionGranted = true;
        }
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

    private void resetBottomSheetValues() {
        initRangeSliderValues();
        mBinding.bottomSheetLayout.bottomSheetPointOfInterestInclude.supermarketCheckBox.mCheckBox.setChecked(false);
        mBinding.bottomSheetLayout.bottomSheetPointOfInterestInclude.schoolCheckBox.mCheckBox.setChecked(false);
        mBinding.bottomSheetLayout.bottomSheetPointOfInterestInclude.restaurantCheckBox.mCheckBox.setChecked(false);
        mBinding.bottomSheetLayout.bottomSheetPropertyTypeLayoutInclude.duplexCheckBox.mCheckBox.setChecked(false);
        mBinding.bottomSheetLayout.bottomSheetPropertyTypeLayoutInclude.houseCheckBox.mCheckBox.setChecked(false);
        mBinding.bottomSheetLayout.bottomSheetPropertyTypeLayoutInclude.penthouseCheckBox.mCheckBox.setChecked(false);
        mBinding.bottomSheetLayout.bottomSheetPropertyTypeLayoutInclude.flatCheckBox.mCheckBox.setChecked(false);
        mBinding.bottomSheetLayout.bottomSheetOnMarketFrom.resetDate();
        mBinding.bottomSheetLayout.filterPropertyLocationSpinner.resetText();
        mBinding.bottomSheetLayout.filterPropertyNumberOfPhotoSpinner.resetText();
    }

    private void initDialogForNewAgent(AgentViewModel agentViewModel, RealEstateAgent agent) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.custom_dialog_shape, null));
        builder.setMessage(R.string.welcome_message);
        final EditText input = new EditText(MainActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        builder.setView(input);
        builder.setPositiveButton(R.string.ok, (dialog, which) -> {
            agent.setTimestamp(Calendar.getInstance().getTimeInMillis());
            agent.setName(input.getText().toString());
            agentViewModel.initAgent(agent);
        });
        builder.show();
    }

    //--------------------------------------LIST HELPERS--------------------------------------------------------

    @NonNull
    private List<String> requestPointOfInterest() {
        String schoolStr = getResources().getString(R.string.school);
        String restaurantStr = getResources().getString(R.string.restaurant);
        String supermarketStr = getResources().getString(R.string.supermarket);
        List<String> requestPointsOfInterests = new ArrayList<>();

        CustomBottomSheetPointOfInterestLayoutBinding pointOfInterestBinding =
                mBinding.bottomSheetLayout.bottomSheetPointOfInterestInclude;

        if (pointOfInterestBinding.schoolCheckBox.mCheckBox.isChecked() && !requestPointsOfInterests.contains(schoolStr))
            requestPointsOfInterests.add(schoolStr);
        if (pointOfInterestBinding.restaurantCheckBox.mCheckBox.isChecked() && !requestPointsOfInterests.contains(restaurantStr))
            requestPointsOfInterests.add(restaurantStr);
        if (pointOfInterestBinding.supermarketCheckBox.mCheckBox.isChecked() && !requestPointsOfInterests.contains(supermarketStr))
            requestPointsOfInterests.add(supermarketStr);

        return requestPointsOfInterests;
    }

    @NonNull
    private List<String> requestPropertyType() {
        List<String> requestPropertyType = new ArrayList<>();
        String houseStr = getResources().getString(R.string.house);
        String flatStr = getResources().getString(R.string.flat);
        String duplexStr = getResources().getString(R.string.duplex);
        String penthouseStr = getResources().getString(R.string.penthouse);

        BottomSheetPropertyTypeLayoutBinding propertyTypeBinding =
                mBinding.bottomSheetLayout.bottomSheetPropertyTypeLayoutInclude;
        if (propertyTypeBinding.houseCheckBox.mCheckBox.isChecked() && !requestPropertyType.contains(houseStr))
            requestPropertyType.add(houseStr);
        if (propertyTypeBinding.flatCheckBox.mCheckBox.isChecked() && !requestPropertyType.contains(flatStr))
            requestPropertyType.add(flatStr);
        if (propertyTypeBinding.duplexCheckBox.mCheckBox.isChecked() && !requestPropertyType.contains(duplexStr))
            requestPropertyType.add(duplexStr);
        if (propertyTypeBinding.penthouseCheckBox.mCheckBox.isChecked() && !requestPropertyType.contains(penthouseStr))
            requestPropertyType.add(penthouseStr);
        return requestPropertyType;
    }
}
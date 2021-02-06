package com.openclassrooms.realestatemanager.presentation.ui.main;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;

import com.openclassrooms.realestatemanager.presentation.ui.fragment.adapter.PropertyRecyclerViewAdapter;
import com.openclassrooms.realestatemanager.presentation.viewModels.AgentViewModel;
import com.openclassrooms.realestatemanager.presentation.viewModels.PropertyViewModel;
import com.picone.core.domain.entity.Property;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

import static com.picone.core.utils.ConstantParameters.CAMERA_PERMISSION_CODE;
import static com.picone.core.utils.ConstantParameters.LOCATION_PERMISSION_CODE;
import static com.picone.core.utils.ConstantParameters.READ_PERMISSION_CODE;
import static com.picone.core.utils.ConstantParameters.WRITE_PERMISSION_CODE;

@AndroidEntryPoint
public abstract class BaseFragment extends Fragment {

    protected AgentViewModel mAgentViewModel;
    protected PropertyViewModel mPropertyViewModel;
    protected NavController mNavController;

    private MainActivity mMainActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainActivity = (MainActivity) getActivity();
        assert mMainActivity != null;
        mAgentViewModel = new ViewModelProvider(requireActivity()).get(AgentViewModel.class);
        mPropertyViewModel = new ViewModelProvider(requireActivity()).get(PropertyViewModel.class);
    }

    protected void setCurrencySwitch(PropertyRecyclerViewAdapter adapter){
        mMainActivity.setTopAppBarCurrencySwitch(adapter);
    }

    protected void setAppBarVisibility(boolean isVisible) {
        mMainActivity.setMenuVisibility(isVisible);
    }

    protected void setUpdateButtonIcon(boolean isForUpdate) {
        mMainActivity.initUpdateButton(isForUpdate);
    }

    protected void setUpdateButtonCustomViewVisibility(boolean isVisible){
        mMainActivity.setUpdateButtonCustomViewVisibility(isVisible);
    }

    protected void setSaveButtonOnClickListener(View.OnClickListener clickListener){
        mMainActivity.setSaveButtonClickListener(clickListener);
    }

    protected void hideSoftKeyboard(View view) {
        mMainActivity.hideSoftKeyboard(view);
    }

    protected void playLoader(boolean isVisible) {
        mMainActivity.playLoader(isVisible);
    }

    protected Property getPropertyForId(String propertyId) { return mMainActivity.getPropertyForId(propertyId); }

    protected boolean isPermissionGrantedForRequestCode(int requestCode) {
        switch (requestCode) {
            case LOCATION_PERMISSION_CODE:
                return mMainActivity.mIsLocationPermissionGranted;
            case CAMERA_PERMISSION_CODE:
                return mMainActivity.mIsCameraPermissionGranted;
            case READ_PERMISSION_CODE:
                return mMainActivity.mIsReadPermissionGranted;
            case WRITE_PERMISSION_CODE:
                return mMainActivity.mIsWritePermissionGranted;
            default:
                return false;
        }
    }
}

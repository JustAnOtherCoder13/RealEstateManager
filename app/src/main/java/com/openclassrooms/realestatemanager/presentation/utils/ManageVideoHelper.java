package com.openclassrooms.realestatemanager.presentation.utils;

import android.content.Intent;
import android.provider.MediaStore;

import com.openclassrooms.realestatemanager.presentation.ui.fragment.AddPropertyFragment;

import static com.picone.core.utils.ConstantParameters.CAMERA_VIDEO_INTENT_REQUEST_CODE;

public class ManageVideoHelper {

    private AddPropertyFragment addPropertyFragment;

    public ManageVideoHelper(AddPropertyFragment addPropertyFragment) {
        this.addPropertyFragment = addPropertyFragment;
    }

    public void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(addPropertyFragment.requireActivity().getPackageManager()) != null) {
            addPropertyFragment.startActivityForResult(takeVideoIntent, CAMERA_VIDEO_INTENT_REQUEST_CODE);
        }
    }
}

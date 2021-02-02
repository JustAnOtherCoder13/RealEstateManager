package com.openclassrooms.realestatemanager.presentation.utils.customView;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.CustomDialogTakePictureBinding;

import java.util.Objects;

import static com.picone.core.utils.ConstantParameters.CAMERA_PHOTO_INTENT_REQUEST_CODE;
import static com.picone.core.utils.ConstantParameters.CAMERA_VIDEO_INTENT_REQUEST_CODE;
import static com.picone.core.utils.ConstantParameters.GALLERY_REQUEST_CODE;

public class CustomMediaDialog extends Dialog implements android.view.View.OnClickListener {

    private CheckedTextView mGallery, mPhoto, mVideo;
    private Button mOkButton;
    private int mIntentRequestCode;
    private Context mContext;

    public CustomMediaDialog(@NonNull Context mContext) {
        super(mContext);
        this.mContext = mContext;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomDialogTakePictureBinding mBinding = CustomDialogTakePictureBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        Objects.requireNonNull(this.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        mGallery = mBinding.customDialogGalleryButton;
        mPhoto = mBinding.customDialogPhotoButton;
        mVideo = mBinding.customDialogVideoButton;
        mOkButton = mBinding.customDialogGoButton;
        mGallery.setOnClickListener(this);
        mPhoto.setOnClickListener(this);
        mVideo.setOnClickListener(this);
        mBinding.customDialogBackButton.setOnClickListener(this);
        mOkButton.setEnabled(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.custom_dialog_gallery_button:
                manageCheckedMedia(mGallery, mPhoto, mVideo);
                mIntentRequestCode = GALLERY_REQUEST_CODE;
                break;
            case R.id.custom_dialog_photo_button:
                manageCheckedMedia(mPhoto, mGallery, mVideo);
                mIntentRequestCode = CAMERA_PHOTO_INTENT_REQUEST_CODE;
                break;
            case R.id.custom_dialog_video_button:
                manageCheckedMedia(mVideo, mGallery, mPhoto);
                mIntentRequestCode = CAMERA_VIDEO_INTENT_REQUEST_CODE;
                break;
            case R.id.custom_dialog_back_button:
                dismiss();
                break;
        }
    }

    public void okButtonSetOnClickListener(View.OnClickListener onClickListener) {
        mOkButton.setOnClickListener(onClickListener);
    }

    public int getIntentRequestCode() {
        return mIntentRequestCode;
    }

    //--------------------------- HELPER -----------------------------

    private void setIconStyle(@NonNull CheckedTextView icon) {
        icon.setBackground(icon.isChecked() ?
                ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.custom_round_primary, null)
                : null);
        icon.setTextColor(icon.isChecked() ?
                mContext.getResources().getColor(R.color.white)
                : mContext.getResources().getColor(R.color.black));
    }

    private void manageCheckedMedia(@NonNull CheckedTextView clickedIcon, @NonNull CheckedTextView secondIcon, CheckedTextView thirdIcon) {
        clickedIcon.setChecked(!clickedIcon.isChecked());
        if (secondIcon.isChecked() && clickedIcon.isChecked())
            secondIcon.setChecked(false);
        if (thirdIcon.isChecked() && clickedIcon.isChecked())
            thirdIcon.setChecked(false);
        setIconStyle(clickedIcon);
        setIconStyle(secondIcon);
        setIconStyle(thirdIcon);
        mOkButton.setEnabled(clickedIcon.isChecked() || secondIcon.isChecked() || thirdIcon.isChecked());
    }
}

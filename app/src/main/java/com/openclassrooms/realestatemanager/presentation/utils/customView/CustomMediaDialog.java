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

    private CheckedTextView gallery;
    private CheckedTextView photo;
    private CheckedTextView video;
    private Button okButton;
    private int intentRequestCode;
    private Context context;

    public CustomMediaDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomDialogTakePictureBinding mBinding = CustomDialogTakePictureBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        Objects.requireNonNull(this.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        gallery = mBinding.customDialogGalleryButton;
        photo = mBinding.customDialogPhotoButton;
        video = mBinding.customDialogVideoButton;
        okButton = mBinding.customDialogGoButton;
        gallery.setOnClickListener(this);
        photo.setOnClickListener(this);
        video.setOnClickListener(this);
        mBinding.customDialogBackButton.setOnClickListener(this);
        okButton.setEnabled(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.custom_dialog_gallery_button:
                manageMediaChecked(gallery, photo, video);
                intentRequestCode = GALLERY_REQUEST_CODE;
                break;
            case R.id.custom_dialog_photo_button:
                manageMediaChecked(photo, gallery, video);
                intentRequestCode = CAMERA_PHOTO_INTENT_REQUEST_CODE;
                break;
            case R.id.custom_dialog_video_button:
                manageMediaChecked(video, gallery, photo);
                intentRequestCode = CAMERA_VIDEO_INTENT_REQUEST_CODE;
                break;
            case R.id.custom_dialog_back_button:
                dismiss();
                break;
        }
    }

    public void okButtonSetOnClickListener(View.OnClickListener onClickListener) {
        okButton.setOnClickListener(onClickListener);
    }

    public int getIntentRequestCode() {
        return intentRequestCode;
    }

    //--------------------------- HELPER -----------------------------

    private void setIconStyle(@NonNull CheckedTextView icon) {
        icon.setBackground(icon.isChecked() ?
                ResourcesCompat.getDrawable(context.getResources(), R.drawable.custom_round_primary, null)
                : null);
        icon.setTextColor(icon.isChecked() ?
                context.getResources().getColor(R.color.white)
                : context.getResources().getColor(R.color.black));
    }

    private void manageMediaChecked(@NonNull CheckedTextView clickedIcon, @NonNull CheckedTextView secondIcon, CheckedTextView thirdIcon) {
        clickedIcon.setChecked(!clickedIcon.isChecked());
        if (secondIcon.isChecked() && clickedIcon.isChecked())
            secondIcon.setChecked(false);
        if (thirdIcon.isChecked() && clickedIcon.isChecked())
            thirdIcon.setChecked(false);
        setIconStyle(clickedIcon);
        setIconStyle(secondIcon);
        setIconStyle(thirdIcon);
        okButton.setEnabled(clickedIcon.isChecked() || secondIcon.isChecked() || thirdIcon.isChecked());
    }
}

package com.openclassrooms.realestatemanager.presentation.utils.customView;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.CustomDialogSetPhotoTitleBinding;
import com.openclassrooms.realestatemanager.presentation.utils.PathUtil;

import java.util.Objects;

public class CustomMediaSetTitleDialog extends Dialog implements android.view.View.OnClickListener {

    private Context mContext;
    private ImageView mPhoto;
    private VideoView mVideo;
    private EditText mDescription;
    private Button mAccept;
    private String mVideoPath;


    public CustomMediaSetTitleDialog(@NonNull Context mContext) {
        super(mContext);
        this.mContext = mContext;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomDialogSetPhotoTitleBinding mBinding = CustomDialogSetPhotoTitleBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        mPhoto = mBinding.setPhotoTitleImageView;
        mVideo = mBinding.setPhotoTitleVideoView;
        mDescription = mBinding.setPhotoDescriptionEditText;
        mAccept = mBinding.setPhotoOkButton;
        mBinding.setPhotoBackButton.setOnClickListener(this);
        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.set_photo_back_button) dismiss();
    }

    public void setAcceptOnClickListener(View.OnClickListener onClickListener) {
        mAccept.setOnClickListener(onClickListener);
    }

    public void setPhoto(String photoPath) {
        isPhoto(true);
        Glide.with(mContext)
                .load(photoPath)
                .centerCrop()
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        mPhoto.setImageDrawable(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });
    }

    public void setVideo(Uri videoPath) {
        isPhoto(false);
        mVideo.setVideoURI(videoPath);
        mVideoPath = PathUtil.getPath(mContext, videoPath);
        mVideo.start();
    }

    public String getVideoPath() {
        return mVideoPath;
    }

    public String getText() {
        if (mDescription.getText() != null) return mDescription.getText().toString();
        else return "";
    }

    public void resetEditText() {
        mDescription.setText("");
    }

    private void isPhoto(boolean isPhoto) {
        mPhoto.setVisibility(isPhoto ? View.VISIBLE : View.GONE);
        mVideo.setVisibility(isPhoto ? View.GONE : View.VISIBLE);
    }

}

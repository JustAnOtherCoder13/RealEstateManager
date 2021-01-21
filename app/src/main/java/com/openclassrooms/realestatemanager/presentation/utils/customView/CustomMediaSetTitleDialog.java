package com.openclassrooms.realestatemanager.presentation.utils.customView;

import android.app.Dialog;
import android.content.Context;
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

public class CustomMediaSetTitleDialog extends Dialog implements android.view.View.OnClickListener {

    private Context context;
    private ImageView photo;
    private VideoView video;
    private EditText description;
    private Button accept;
    private String mVideoPath;


    public CustomMediaSetTitleDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomDialogSetPhotoTitleBinding mBinding = CustomDialogSetPhotoTitleBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        photo = mBinding.setPhotoTitleImageView;
        video = mBinding.setPhotoTitleVideoView;
        description = mBinding.setPhotoDescriptionEditText;
        accept = mBinding.setPhotoOkButton;
        mBinding.setPhotoBackButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.set_photo_back_button) dismiss();
    }

    public void setAcceptOnClickListener(View.OnClickListener onClickListener) {
        accept.setOnClickListener(onClickListener);
    }

    public void setPhoto(String photoPath) {
        isPhoto(true);
        Glide.with(context)
                .load(photoPath)
                .centerCrop()
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        photo.setImageDrawable(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });
    }

    public void setVideo(Uri videoPath) {
        isPhoto(false);
        video.setVideoURI(videoPath);
        mVideoPath = PathUtil.getPath(context, videoPath);
        video.start();
    }

    public String getVideoPath() {
        return mVideoPath;
    }


    public String getText() {
        if (description.getText() != null) return description.getText().toString();
        else return " ";
    }
    public void resetEditText(){
        description.setText(" ");
    }

    private void isPhoto(boolean isPhoto) {
        photo.setVisibility(isPhoto ? View.VISIBLE : View.GONE);
        video.setVisibility(isPhoto ? View.GONE : View.VISIBLE);
    }
}

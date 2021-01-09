package com.openclassrooms.realestatemanager.presentation.utils.customView;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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

import static com.openclassrooms.realestatemanager.presentation.utils.ResizePictureForView.setPic;

public class CustomSetTitleDialog extends Dialog implements android.view.View.OnClickListener {

    private Context context;
    private CustomDialogSetPhotoTitleBinding mBinding;
    private ImageView photo;
    private VideoView video;
    private EditText description;
    private Button accept;
    private Button cancel;


    public CustomSetTitleDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = CustomDialogSetPhotoTitleBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        photo = mBinding.setPhotoTitleImageView;
        video = mBinding.setPhotoTitleVideoView;
        description = mBinding.setPhotoDescriptionEditText;
        accept = mBinding.setPhotoAcceptButton;
        cancel = mBinding.setPhotoCancelButton;
        cancel.setOnClickListener(this);
        video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.i("TAG", "onCompletion: work well"+mp);
            }
        });
        video.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.i("TAG", "onError: "+extra);
                return false;
            }
        });
    }

    public void setAcceptOnClickListener(View.OnClickListener onClickListener) {
        accept.setOnClickListener(onClickListener);
    }

    public void setPhoto(String photoPath) {
        isPhoto(true);
        Glide.with(context)
                .load(setPic(photo, photoPath))
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

    public void setVideo(Uri videoPath){
        isPhoto(false);
        video.setVideoURI(videoPath);
        video.start();

    }

    public void isPhoto(boolean isPhoto){
        photo.setVisibility(isPhoto?View.VISIBLE:View.GONE);
        video.setVisibility(isPhoto?View.GONE:View.VISIBLE);
    }

    public String getText() {
        if (description.getText() != null)
            return description.getText().toString();
        else return " ";
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.set_photo_cancel_button) dismiss();
    }
}

package com.openclassrooms.realestatemanager.presentation.utils.customView;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.CustomDialogFullScreenMediaBinding;
import com.openclassrooms.realestatemanager.presentation.utils.PathUtil;

import java.util.Objects;

public class CustomFullScreenMediaDialog extends Dialog implements android.view.View.OnClickListener {

    private Context context;
    private ImageView photo;
    private VideoView video;
    private String mediaPath;
    private CheckedTextView play;
    private CheckedTextView pause;
    private int stopPosition = 0;

    public CustomFullScreenMediaDialog(@NonNull Context context, String mediaPath) {
        super(context);
        this.context = context;
        this.mediaPath = mediaPath;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomDialogFullScreenMediaBinding mBinding = CustomDialogFullScreenMediaBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        Objects.requireNonNull(this.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        photo = mBinding.fullScreenImage;
        video = mBinding.fullScreenVideo;
        play = mBinding.customMediaControler.customMediaControllerPlayButton;
        pause = mBinding.customMediaControler.customMediaControllerPauseButton;
        mBinding.customMediaControler.customMediaControllerBackButton.setOnClickListener(this);
        play.setOnClickListener(this);
        pause.setOnClickListener(this);

        switchPhotoOrVideoVisibility(PathUtil.isImageFileFromPath(mediaPath));
        if (PathUtil.isImageFileFromPath(mediaPath))
            Glide.with(photo)
                    .load(mediaPath)
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

        else video.setVideoPath(mediaPath);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.custom_media_controller_play_button:
                managePlayButton();
                if (stopPosition > 0) {
                    video.setVideoPath(mediaPath);
                    video.seekTo(stopPosition);
                    stopPosition = 0;
                }
                video.start();
                break;

            case R.id.custom_media_controller_pause_button:
                managePlayButton();
                stopPosition = video.getCurrentPosition();
                video.suspend();
                break;

            case R.id.custom_media_controller_back_button:
                dismiss();
                break;
        }
    }

    //------------------------ HELPER -----------------------------

    private void switchPhotoOrVideoVisibility(boolean isPhoto) {
        photo.setVisibility(isPhoto ? View.VISIBLE : View.GONE);
        play.setVisibility(isPhoto ? View.GONE : View.VISIBLE);
        pause.setVisibility(isPhoto ? View.GONE : View.VISIBLE);
        video.setVisibility(isPhoto ? View.GONE : View.VISIBLE);
    }

    private void managePlayButton() {
        play.setChecked(!video.isPlaying());
        pause.setChecked(video.isPlaying());
        setIconStyle(play);
        setIconStyle(pause);
    }

    private void setIconStyle(@NonNull CheckedTextView icon) {
        icon.setBackground(icon.isChecked() ?
                ResourcesCompat.getDrawable(context.getResources(), R.drawable.custom_round_white, null)
                : null
        );
        icon.setTextColor(icon.isChecked() ?
                context.getResources().getColor(R.color.black)
                : context.getResources().getColor(R.color.white));
    }
}
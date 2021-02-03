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
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.CustomDialogFullScreenMediaBinding;
import com.openclassrooms.realestatemanager.presentation.utils.PathUtil;

import java.util.Objects;

public class CustomMediaFullScreenDialog extends Dialog implements android.view.View.OnClickListener {

    private Context mContext;
    private ImageView mPhoto;
    private VideoView mVideo;
    private String mMediaPath;
    private CheckedTextView mPlay, mPause;
    private int mStopPosition = 0;

    public CustomMediaFullScreenDialog(@NonNull Context mContext, String mMediaPath) {
        super(mContext);
        this.mContext = mContext;
        this.mMediaPath = mMediaPath;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomDialogFullScreenMediaBinding mBinding = CustomDialogFullScreenMediaBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        Objects.requireNonNull(this.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        mPhoto = mBinding.fullScreenImage;
        mVideo = mBinding.fullScreenVideo;
        mPlay = mBinding.customMediaControler.customMediaControllerPlayButton;
        mPause = mBinding.customMediaControler.customMediaControllerPauseButton;
        mBinding.customMediaControler.customMediaControllerBackButton.setOnClickListener(this);
        mPlay.setOnClickListener(this);
        mPause.setOnClickListener(this);

        initMedia();
    }

    private void initMedia() {
        switchPhotoOrVideoVisibility(PathUtil.isImageFileFromPath(mMediaPath));
        if (PathUtil.isImageFileFromPath(mMediaPath))
            Glide.with(mPhoto)
                    .load(mMediaPath)
                    .apply(new RequestOptions().override(800))
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

        else mVideo.setVideoPath(mMediaPath);
    }

    @Override
    public void onClick(View v) {
        //init media controls
        switch (v.getId()) {
            case R.id.custom_media_controller_play_button:
                managePlayButton();
                if (mStopPosition > 0) {
                    mVideo.setVideoPath(mMediaPath);
                    mVideo.seekTo(mStopPosition);
                    mStopPosition = 0;
                }
                mVideo.start();
                break;

            case R.id.custom_media_controller_pause_button:
                managePlayButton();
                mStopPosition = mVideo.getCurrentPosition();
                mVideo.suspend();
                break;

            case R.id.custom_media_controller_back_button:
                dismiss();
                break;
        }
    }

    //------------------------ HELPER -----------------------------

    private void switchPhotoOrVideoVisibility(boolean isPhoto) {
        mPhoto.setVisibility(isPhoto ? View.VISIBLE : View.GONE);
        mPlay.setVisibility(isPhoto ? View.GONE : View.VISIBLE);
        mPause.setVisibility(isPhoto ? View.GONE : View.VISIBLE);
        mVideo.setVisibility(isPhoto ? View.GONE : View.VISIBLE);
    }

    private void managePlayButton() {
        mPlay.setChecked(!mVideo.isPlaying());
        mPause.setChecked(mVideo.isPlaying());
        setIconStyle(mPlay);
        setIconStyle(mPause);
    }

    private void setIconStyle(@NonNull CheckedTextView icon) {
        icon.setBackground(icon.isChecked() ?
                ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.custom_round_white, null)
                : null
        );
        icon.setTextColor(icon.isChecked() ?
                mContext.getResources().getColor(R.color.black)
                : mContext.getResources().getColor(R.color.white));
    }
}
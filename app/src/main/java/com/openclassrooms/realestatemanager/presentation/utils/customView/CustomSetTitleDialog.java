package com.openclassrooms.realestatemanager.presentation.utils.customView;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

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
        description = mBinding.setPhotoDescriptionEditText;
        accept = mBinding.setPhotoAcceptButton;
        cancel = mBinding.setPhotoCancelButton;
        cancel.setOnClickListener(this);
    }

    public void setAcceptOnClickListener(View.OnClickListener onClickListener) {
        accept.setOnClickListener(onClickListener);
    }

    public void setPhoto(String photoPath) {
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

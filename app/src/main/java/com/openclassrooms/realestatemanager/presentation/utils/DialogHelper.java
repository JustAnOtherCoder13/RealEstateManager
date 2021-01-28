package com.openclassrooms.realestatemanager.presentation.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.Toast;

import androidx.core.content.res.ResourcesCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.presentation.utils.customView.CustomMediaSetTitleDialog;

import java.util.Objects;

public class DialogHelper {

    public static void initAlertNoNetworkAvailable(Context context) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
        builder.setBackground(ResourcesCompat.getDrawable(context.getResources(), R.drawable.custom_dialog_shape, null));
        builder.setMessage(R.string.no_internet_message);
        builder.setPositiveButton(R.string.ok, (dialog, which) -> {
        });
        builder.show();
    }

   /* public static void initSetTitleCustomDialog(boolean isPhoto, Intent data, Context context, View.OnClickListener onClickListener) {
        CustomMediaSetTitleDialog setTitleDialog = new CustomMediaSetTitleDialog(context);
        Objects.requireNonNull(setTitleDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        if (isPhoto) setTitleDialog.setPhoto(Objects.requireNonNull(data.getData()).getPath());
        else setTitleDialog.setVideo(data.getData());

        setTitleDialog.show();

        setTitleDialog.setAcceptOnClickListener(onClickListener);
    }*/
}

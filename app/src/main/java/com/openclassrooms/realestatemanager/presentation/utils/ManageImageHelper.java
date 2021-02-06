package com.openclassrooms.realestatemanager.presentation.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import com.airbnb.lottie.LottieAnimationView;
import com.picone.core.domain.entity.PropertyMedia;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.picone.core.utils.ConstantParameters.ADD_PHOTO;

public class ManageImageHelper {

    private String mCurrentPhotoPath;
    private Context requireContext;

    public ManageImageHelper(Context requireContext) {
        this.requireContext = requireContext;
    }

    public String getCurrentPhotoPath() {
        return mCurrentPhotoPath;
    }

    public void setCurrentPhotoPath(String photoPath) {
        this.mCurrentPhotoPath = photoPath;
    }

    public void saveImageInGallery() {
        File f = new File(mCurrentPhotoPath);
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        requireContext.getApplicationContext().sendBroadcast(mediaScanIntent);
    }

    public File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.FRANCE).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = requireContext.getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".png",
                storageDir
        );
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public Intent getTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {
            Toast.makeText(requireContext, ex.toString(), Toast.LENGTH_SHORT).show();
        }
        if (photoFile != null) {
            Uri photoURI = FileProvider.getUriForFile(requireContext.getApplicationContext(),
                    "com.openclassrooms.android.fileprovider",
                    photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        }
        return takePictureIntent;
    }

    @NonNull
    public List<PropertyMedia> propertyMediasWithAddButton(List<PropertyMedia> medias) {
        List<PropertyMedia> photos = new ArrayList<>();
        PropertyMedia propertyMedia = new PropertyMedia(0, ADD_PHOTO, "", 0);
        photos.add(propertyMedia);
        photos.addAll(medias);
        return photos;
    }

    //to use in both adapter
    public static void playLoader(boolean isVisible, LottieAnimationView animationView) {
        if (isVisible) {
            animationView.setVisibility(View.VISIBLE);
            animationView.playAnimation();
        } else {
            animationView.pauseAnimation();
            animationView.setVisibility(View.GONE);
        }
    }
}

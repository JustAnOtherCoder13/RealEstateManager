package com.openclassrooms.realestatemanager.presentation.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.MimeTypeMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentActivity;

import com.openclassrooms.realestatemanager.presentation.ui.fragment.AddPropertyFragment;
import com.picone.core.domain.entity.PropertyPhoto;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.picone.core.utils.ConstantParameters.ADD_PHOTO;
import static com.picone.core.utils.ConstantParameters.CAMERA_INTENT_REQUEST_CODE;
import static com.picone.core.utils.ConstantParameters.FILE_PROVIDER_AUTH;

public class ManageImageHelper {

    private  String mCurrentPhotoPath;
    private AddPropertyFragment requireContext;

    public ManageImageHelper(AddPropertyFragment requireContext) {
        this.requireContext = requireContext;
    }

    public String getCurrentPhotoPath() {
        return mCurrentPhotoPath;
    }

    public void setCurrentPhotoPath(String photoPath){
        this.mCurrentPhotoPath = photoPath;
    }

    public void saveImageInGallery() {
        File f = new File(mCurrentPhotoPath);
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        requireContext.requireActivity().sendBroadcast(mediaScanIntent);
    }


    public File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.FRANCE).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = requireContext.requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public void dispatchTakePictureIntent() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {
            Log.e("TAG", "dispatchTakePictureIntent: ", ex);
        }
        if (photoFile != null) {
            Uri photoURI = FileProvider.getUriForFile(requireContext.requireActivity(),
                    "com.openclassrooms.android.fileprovider",
                    photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            requireContext.startActivityForResult(takePictureIntent, CAMERA_INTENT_REQUEST_CODE);
        }
    }

    @NonNull
    public List<PropertyPhoto> propertyPhotosWithAddButton(List<PropertyPhoto> propertyPhotos) {
        List<PropertyPhoto> photos = new ArrayList<>();
        PropertyPhoto propertyPhoto = new PropertyPhoto(0, ADD_PHOTO, "", 0);
        photos.add(propertyPhoto);
        photos.addAll(propertyPhotos);
        return photos;
    }
}

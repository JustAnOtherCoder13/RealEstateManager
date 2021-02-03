package com.picone.core.data.property;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.picone.core.data.RealEstateManagerRoomDatabase;

public class PropertyContentProvider extends ContentProvider {

    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        if (getContext() != null) {
            RealEstateManagerRoomDatabase realEstateManagerRoomDatabase = RealEstateManagerRoomDatabase.getInstance(getContext().getApplicationContext());
            PropertyDaoImpl propertyDao = new PropertyDaoImpl(realEstateManagerRoomDatabase);

            int propertyId = (int) ContentUris.parseId(uri);
            Cursor cursor = null;

            if (selection == null || selection.trim().isEmpty())
                throw new IllegalArgumentException("You have to enter value for argument selection. This value have to be \"property\", \"properties\" or \"location\".");

            switch (selection){
                case "property":
                    cursor = propertyDao.getPropertiesWithCursor(propertyId);
                    break;
                case "properties":
                    cursor = propertyDao.getAllPropertiesInformationWithCursor();
                    break;
                case "location":
                    cursor = propertyDao.getLocationWithCursor(propertyId);
                    break;
            }

            if (cursor != null)
                cursor.setNotificationUri(getContext().getContentResolver(), uri);

            return cursor;
        }
        throw new IllegalArgumentException("Failed to query row for uri " + uri);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}

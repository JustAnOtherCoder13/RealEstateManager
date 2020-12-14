package com.picone.core.data;

import android.content.ContentValues;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.OnConflictStrategy;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.picone.core.data.realEstateManager.RealEstateManagerRoomDao;
import com.picone.core.domain.entity.Property;
import com.picone.core.domain.entity.RealEstateAgent;

import static com.picone.core.data.Generator.generateManagers;
import static com.picone.core.data.Generator.generatePhotos;
import static com.picone.core.data.Generator.generatePointOfInterests;
import static com.picone.core.data.Generator.generateProperties;
import static com.picone.core.utils.ConstantParameters.pointOfInterestTable;
import static com.picone.core.utils.ConstantParameters.propertyPhotoTable;
import static com.picone.core.utils.ConstantParameters.propertyTable;
import static com.picone.core.utils.ConstantParameters.realEstateAgentTable;

@androidx.room.Database(entities = {Property.class, RealEstateAgent.class}, version = 2, exportSchema = false)
public abstract class RealEstateManagerDatabase extends RoomDatabase {

    public abstract RealEstateManagerRoomDao realEstateManagerRoomDao();

    public static RealEstateManagerDatabase getInstance(Context context) {
        return Room.databaseBuilder(context.getApplicationContext(),
                RealEstateManagerDatabase.class, "RealEstateManager.db")
                .fallbackToDestructiveMigration()
                .addCallback(prepopulateDatabase())
                .build();
    }

    private static Callback prepopulateDatabase() {
        return new Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
                createManagers(db);
                createProperties(db);
                createPhotos( db);
                createPointOfInterest(db);
            }
        };
    }

    private static void createManagers(SupportSQLiteDatabase db) {
        for (int i =0; i<4; i++){
            ContentValues contentValues = new ContentValues();
            contentValues.put("id", generateManagers().get(i).getId());
            contentValues.put("address", generateManagers().get(i).getAddress());
            contentValues.put("name", generateManagers().get(i).getName());
            contentValues.put("avatar", generateManagers().get(i).getAvatar());

            db.insert(realEstateAgentTable, OnConflictStrategy.IGNORE, contentValues);
        }
    }

    private static void createProperties( SupportSQLiteDatabase db) {
        for (int i =0; i<2; i++) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("id", generateProperties().get(i).getId());
            contentValues.put("realEstateManagerId", generateProperties().get(i).getRealEstateManagerId());
            contentValues.put("address", generateProperties().get(i).getAddress());
            contentValues.put("propertyType", generateProperties().get(i).getPropertyType());
            contentValues.put("propertyArea", generateProperties().get(i).getPropertyArea());
            contentValues.put("numberOfRooms", generateProperties().get(i).getNumberOfRooms());
            contentValues.put("price", generateProperties().get(i).getPrice());

            db.insert(propertyTable, OnConflictStrategy.IGNORE, contentValues);
        }
    }

    private static void createPhotos( SupportSQLiteDatabase db) {
        for (int i =0; i<4; i++) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("id", generatePhotos().get(i).getId());
            contentValues.put("photo", generatePhotos().get(i).getPhoto());
            contentValues.put("description", generatePhotos().get(i).getDescription());
            contentValues.put("propertyId", generatePhotos().get(i).getPropertyId());

            db.insert(propertyPhotoTable, OnConflictStrategy.IGNORE, contentValues);
        }
    }

    private static void createPointOfInterest(SupportSQLiteDatabase db) {
        for (int i =0; i<4; i++) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("id", generatePointOfInterests().get(i).getId());
            contentValues.put("propertyId", generatePointOfInterests().get(i).getPropertyId());

            db.insert(pointOfInterestTable, OnConflictStrategy.IGNORE, contentValues);
        }
    }

}

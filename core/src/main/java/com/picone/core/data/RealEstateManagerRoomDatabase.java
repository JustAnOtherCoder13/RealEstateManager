package com.picone.core.data;

import android.content.ContentValues;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.OnConflictStrategy;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.picone.core.data.property.PropertyRoomDao;
import com.picone.core.data.realEstateAgent.RealEstateAgentRoomDao;
import com.picone.core.domain.entity.PointOfInterest;
import com.picone.core.domain.entity.PropertyInformation;
import com.picone.core.domain.entity.PropertyLocation;
import com.picone.core.domain.entity.PropertyMedia;
import com.picone.core.domain.entity.RealEstateAgent;

import static com.picone.core.data.Generator.generateAgents;
import static com.picone.core.data.Generator.generatePhotos;
import static com.picone.core.data.Generator.generatePointOfInterests;
import static com.picone.core.data.Generator.generatePropertiesInformation;
import static com.picone.core.data.Generator.generatePropertyLocation;
import static com.picone.core.utils.ConstantParameters.pointOfInterestTable;
import static com.picone.core.utils.ConstantParameters.propertyInformationTable;
import static com.picone.core.utils.ConstantParameters.propertyLocationTable;
import static com.picone.core.utils.ConstantParameters.propertyPhotoTable;
import static com.picone.core.utils.ConstantParameters.realEstateAgentTable;

@androidx.room.Database(entities = {PropertyInformation.class, RealEstateAgent.class, PointOfInterest.class, PropertyMedia.class, PropertyLocation.class}, version = 1, exportSchema = false)
public abstract class RealEstateManagerRoomDatabase extends RoomDatabase {

    //todo content provider
    public abstract RealEstateAgentRoomDao realEstateManagerRoomDao();

    public abstract PropertyRoomDao propertyRoomDao();

    @NonNull
    public static RealEstateManagerRoomDatabase getInstance(@NonNull Context context) {
        return Room.databaseBuilder(context.getApplicationContext(),
                RealEstateManagerRoomDatabase.class, "RealEstateManager.db")
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
                createPhotos(db);
                createPointOfInterest(db);
                createPropertiesLocation(db);
            }
        };
    }

    private static void createManagers(@NonNull SupportSQLiteDatabase db) {

        ContentValues contentValues = new ContentValues();
        contentValues.put("id", generateAgents().getId());
        contentValues.put("address", generateAgents().getAddress());
        contentValues.put("name", generateAgents().getName());
        contentValues.put("avatar", generateAgents().getAvatar());

        db.insert(realEstateAgentTable, OnConflictStrategy.IGNORE, contentValues);

    }

    private static void createProperties(SupportSQLiteDatabase db) {
        for (int i = 0; i < 2; i++) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("id", generatePropertiesInformation().get(i).getId());
            contentValues.put("realEstateAgentId", generatePropertiesInformation().get(i).getRealEstateAgentId());
            contentValues.put("propertyType", generatePropertiesInformation().get(i).getPropertyType());
            contentValues.put("propertyArea", generatePropertiesInformation().get(i).getPropertyArea());
            contentValues.put("numberOfRooms", generatePropertiesInformation().get(i).getNumberOfRooms());
            contentValues.put("price", generatePropertiesInformation().get(i).getPrice());
            contentValues.put("description", generatePropertiesInformation().get(i).getDescription());
            contentValues.put("numberOfBedrooms", generatePropertiesInformation().get(i).getNumberOfBedrooms());
            contentValues.put("numberOfBathrooms", generatePropertiesInformation().get(i).getNumberOfBathrooms());
            contentValues.put("isSold", generatePropertiesInformation().get(i).isSold());
            contentValues.put("enterOnMarket", generatePropertiesInformation().get(i).getEnterOnMarket());
            contentValues.put("soldFrom", generatePropertiesInformation().get(i).getSoldFrom());

            db.insert(propertyInformationTable, OnConflictStrategy.IGNORE, contentValues);
        }
    }

    private static void createPhotos(SupportSQLiteDatabase db) {
        for (int i = 0; i < 4; i++) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("id", generatePhotos().get(i).getId());
            contentValues.put("photoPath", generatePhotos().get(i).getPhotoPath());
            contentValues.put("description", generatePhotos().get(i).getDescription());
            contentValues.put("propertyId", generatePhotos().get(i).getPropertyId());

            db.insert(propertyPhotoTable, OnConflictStrategy.IGNORE, contentValues);
        }
    }

    private static void createPointOfInterest(SupportSQLiteDatabase db) {
        for (int i = 0; i < 4; i++) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("id", generatePointOfInterests().get(i).getId());
            contentValues.put("propertyId", generatePointOfInterests().get(i).getPropertyId());
            contentValues.put("name", generatePointOfInterests().get(i).getName());
            contentValues.put("latitude", generatePointOfInterests().get(i).getLatitude());
            contentValues.put("longitude", generatePointOfInterests().get(i).getLongitude());
            contentValues.put("type", generatePointOfInterests().get(i).getType());
            contentValues.put("icon", generatePointOfInterests().get(i).getIcon());

            db.insert(pointOfInterestTable, OnConflictStrategy.IGNORE, contentValues);
        }
    }

    private static void createPropertiesLocation(SupportSQLiteDatabase db) {
        for (int i = 0; i < 2; i++) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("id", generatePropertyLocation().get(i).getId());
            contentValues.put("latitude", generatePropertyLocation().get(i).getLatitude());
            contentValues.put("longitude", generatePropertyLocation().get(i).getLongitude());
            contentValues.put("address", generatePropertyLocation().get(i).getAddress());
            contentValues.put("region", generatePropertyLocation().get(i).getRegion());
            contentValues.put("propertyId", generatePropertyLocation().get(i).getPropertyId());

            db.insert(propertyLocationTable, OnConflictStrategy.IGNORE, contentValues);

        }
    }

}

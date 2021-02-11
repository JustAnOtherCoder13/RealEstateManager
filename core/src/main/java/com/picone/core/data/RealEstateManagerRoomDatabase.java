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

import static com.picone.core.utils.ConstantParameters.realEstateAgentTable;

@androidx.room.Database(entities = {PropertyInformation.class, RealEstateAgent.class, PointOfInterest.class, PropertyMedia.class, PropertyLocation.class}, version = 1, exportSchema = false)
public abstract class RealEstateManagerRoomDatabase extends RoomDatabase {

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
            }
        };
    }

    private static void createManagers(@NonNull SupportSQLiteDatabase db) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", 1L);
        db.insert(realEstateAgentTable, OnConflictStrategy.IGNORE, contentValues);
    }
}

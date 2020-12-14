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

@androidx.room.Database(entities = {Property.class, RealEstateAgent.class}, version = 1, exportSchema = false)
public abstract class RealEstateManagerDatabase extends RoomDatabase {

    public abstract RealEstateManagerRoomDao realEstateManagerRoomDao();

    public static RealEstateManagerDatabase getInstance(Context context){
        return Room.databaseBuilder(context.getApplicationContext(),
                RealEstateManagerDatabase.class, "RealEstateManager.db")
                .addCallback(prepopulateDatabase())
                .build();
    }

    private static Callback prepopulateDatabase() {
        return new Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);

               createManager(0,db);
               createManager(1,db);
               createManager(2,db);
               createManager(3,db);
            }
        };
    }

    private static void createManager(int index, SupportSQLiteDatabase db) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", generateManagers().get(index).getId());
        contentValues.put("address", generateManagers().get(index).getAddress());
        contentValues.put("name", generateManagers().get(index).getName());
        contentValues.put("avatar", generateManagers().get(index).getAvatar());

        db.insert("real_estate_agent", OnConflictStrategy.IGNORE, contentValues);
    }

}

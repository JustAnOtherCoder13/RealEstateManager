package com.picone.core.data;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.picone.core.domain.entity.Property;
import com.picone.core.domain.entity.RealEstateAgent;

@androidx.room.Database(entities = {Property.class, RealEstateAgent.class}, version = 1, exportSchema = false)
public abstract class RealEstateManagerDatabase extends RoomDatabase {

    public static RealEstateManagerDatabase getInstance(Context context){
        return Room.databaseBuilder(context.getApplicationContext(),
                RealEstateManagerDatabase.class, "RealEstateManager.db")
                .build();
    }

}

package com.picone.core.data;

import com.picone.core.domain.entity.Property;
import com.picone.core.domain.entity.RealEstateAgent;

@androidx.room.Database(entities = {Property.class, RealEstateAgent.class}, version = 1, exportSchema = false)
public abstract class realEstateDatabase {


}

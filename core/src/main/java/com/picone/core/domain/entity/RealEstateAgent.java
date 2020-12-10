package com.picone.core.domain.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "real_estate_agent")
public class RealEstateAgent {

    @PrimaryKey
    private final long id;

    public RealEstateAgent(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}

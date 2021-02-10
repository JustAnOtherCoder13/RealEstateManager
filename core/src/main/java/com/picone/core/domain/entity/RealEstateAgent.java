package com.picone.core.domain.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import static com.picone.core.utils.ConstantParameters.realEstateAgentTable;

@Entity(tableName = realEstateAgentTable)
public class RealEstateAgent {

    @PrimaryKey
    @ColumnInfo(defaultValue = "1")
    private long id;
    @ColumnInfo(defaultValue = "1")
    private long timestamp;
    @ColumnInfo(defaultValue = " ")
    private String name;


    public RealEstateAgent(long id, long timestamp, String name) {
        this.id = id;
        this.timestamp = timestamp;
        this.name = name;
    }

    @Ignore
    public RealEstateAgent() {
    }

    public long getId() {
        return id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

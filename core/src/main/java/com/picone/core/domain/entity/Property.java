package com.picone.core.domain.entity;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "property")
public class Property {

    @PrimaryKey
    private final long id;

    public Property(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}

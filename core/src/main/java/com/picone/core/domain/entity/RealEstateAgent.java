package com.picone.core.domain.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import static com.picone.core.utils.ConstantParameters.realEstateAgentTable;

@Entity(tableName = realEstateAgentTable)
public class RealEstateAgent {

    @PrimaryKey
    private final long id;

    private String address;
    private String name;
    private String avatar;

    public RealEstateAgent(long id, String address, String name, String avatar) {
        this.id = id;
        this.address = address;
        this.name = name;
        this.avatar = avatar;
    }

    public long getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}

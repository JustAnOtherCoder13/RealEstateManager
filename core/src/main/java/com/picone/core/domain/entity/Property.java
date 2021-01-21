package com.picone.core.domain.entity;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

import static com.picone.core.utils.ConstantParameters.propertyTable;

@Entity(tableName = propertyTable, foreignKeys = @ForeignKey(entity = RealEstateAgent.class,
        parentColumns = "id",
        childColumns = "realEstateAgentId"))
public class Property {

    @PrimaryKey(autoGenerate = true)
    private int id;

    // ESSENTIALS INFORMATION
    @ColumnInfo(index = true)
    private int realEstateAgentId;
    private String address;
    private String region;
    private String propertyType;
    private int propertyArea;
    private int numberOfRooms;
    private int price;

    //DESCRIPTION
    private String description;

    //ADDITIONAL INFORMATION
    private int numberOfBedrooms;
    private int numberOfBathrooms;
    private boolean isSold;
    private String enterOnMarket;
    private String soldFrom;

    @Ignore
    List<PropertyPhoto> propertyPhotos = new ArrayList<>();

    @Ignore
    public Property() {
    }

    public Property(int id, int realEstateAgentId, String address, String region, String propertyType, int propertyArea, int numberOfRooms, int price, String description, int numberOfBedrooms, int numberOfBathrooms, boolean isSold, String enterOnMarket, String soldFrom) {
        this.id = id;
        this.realEstateAgentId = realEstateAgentId;
        this.address = address;
        this.region = region;
        this.propertyType = propertyType;
        this.propertyArea = propertyArea;
        this.numberOfRooms = numberOfRooms;
        this.price = price;
        this.description = description;
        this.numberOfBedrooms = numberOfBedrooms;
        this.numberOfBathrooms = numberOfBathrooms;
        this.isSold = isSold;
        this.enterOnMarket = enterOnMarket;
        this.soldFrom = soldFrom;
    }

    public List<PropertyPhoto> getPropertyPhotos() {
        return propertyPhotos;
    }

    public void setPropertyPhotos(List<PropertyPhoto> propertyPhotos) {
        this.propertyPhotos = propertyPhotos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRealEstateAgentId() {
        return realEstateAgentId;
    }

    public void setRealEstateAgentId(int realEstateAgentId) {
        this.realEstateAgentId = realEstateAgentId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public int getPropertyArea() {
        return propertyArea;
    }

    public void setPropertyArea(int propertyArea) {
        this.propertyArea = propertyArea;
    }

    public int getNumberOfRooms() {
        return numberOfRooms;
    }

    public void setNumberOfRooms(int numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNumberOfBedrooms() {
        return numberOfBedrooms;
    }

    public void setNumberOfBedrooms(int numberOfBedrooms) {
        this.numberOfBedrooms = numberOfBedrooms;
    }

    public int getNumberOfBathrooms() {
        return numberOfBathrooms;
    }

    public void setNumberOfBathrooms(int numberOfBathrooms) {
        this.numberOfBathrooms = numberOfBathrooms;
    }

    public boolean isSold() {
        return isSold;
    }

    public void setSold(boolean sold) {
        isSold = sold;
    }

    public String getEnterOnMarket() {
        return enterOnMarket;
    }

    public void setEnterOnMarket(String enterOnMarket) {
        this.enterOnMarket = enterOnMarket;
    }

    public String getSoldFrom() {
        return soldFrom;
    }

    public void setSoldFrom(String soldFrom) {
        this.soldFrom = soldFrom;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}

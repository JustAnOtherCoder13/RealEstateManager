package com.picone.core.data.realEstateManager;

import androidx.room.Dao;
import androidx.room.Query;

import com.picone.core.domain.entity.RealEstateAgent;

import java.util.List;

import io.reactivex.Observable;

@Dao
public interface RealEstateManagerRoomDao {


    @Query("SELECT*FROM real_estate_agent_table")
    Observable<List<RealEstateAgent>> getAllAgents();
}

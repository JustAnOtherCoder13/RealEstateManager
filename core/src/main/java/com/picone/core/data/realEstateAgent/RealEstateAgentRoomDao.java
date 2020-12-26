package com.picone.core.data.realEstateAgent;

import androidx.room.Dao;
import androidx.room.Query;

import com.picone.core.domain.entity.RealEstateAgent;

import io.reactivex.Observable;

@Dao
public interface RealEstateAgentRoomDao {

    @Query("SELECT*FROM real_estate_agent_table")
    Observable<RealEstateAgent> getAgent();
}

package com.parking.parking_lot.parking.repository

import com.parking.parking_lot.parking.Parking
import org.springframework.data.jpa.repository.JpaRepository

interface ParkingRepository: JpaRepository<Parking,Long> {


    fun findByIdIn(ids: List<Long>): List<Parking>  // 검색된 ID로 MySQL에서 조회

}
package com.parking.parking_lot.operationHours

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param


interface OperatingHoursRepository : JpaRepository<OperatingHours,Long> {


    /**
     * 특정 주차장의 ID를 기반으로 운영 시간을 조회하는 메서드.
     *
     * @param parkingId 조회할 주차장의 ID
     * @return 해당 주차장의 운영 시간  [OperatingHours] List 객체
     */
    @Query("select o " +
            "from OperatingHours o " +
            "where o.parking.id in :parkingIds")
    fun findByParkingIds(@Param("parkingIds") parkingId:List<Long>): List<OperatingHours>
}
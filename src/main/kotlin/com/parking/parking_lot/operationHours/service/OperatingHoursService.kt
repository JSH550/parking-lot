package com.parking.parking_lot.operationHours.service

import com.parking.parking_lot.operationHours.dto.OperationHoursDto


interface OperatingHoursService {


    /**
     * 주어진 주차장 ID List 기반으로 운영 시간을 조회합니다.
     *
     * @param parkingIds 조회할 주차장 ID 목록
     * @return 운영 시간 정보를 담은 [OperationHoursDto] 리스트
     */
    fun getByParkingIds(parkingIds:List<Long>):List<OperationHoursDto>


    /**
     * 주어진 주차장 ID로 운영 시간을 조회합니다.
     *
     * @param parkingId 조회할 주차장 ID
     * @return 운영 시간 정보를 담은 [OperationHoursDto] 리스트
     */
    fun getByParkingId(parkingId:Long):List<OperationHoursDto>

}
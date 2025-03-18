package com.parking.parking_lot.operationHours.service

import com.parking.parking_lot.operationHours.OperatingHoursRepository
import com.parking.parking_lot.operationHours.dto.OperationHoursDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OperatingHoursServiceImpl(

    private val operatingHoursRepository: OperatingHoursRepository

) : OperatingHoursService {


    @Transactional(readOnly = true)
    override fun getByParkingIds(parkingIds: List<Long>): List<OperationHoursDto> {

        //파라미터로 받은 ID List로 Operating Hours 찾고 DTO로 변환해서 반환
        return operatingHoursRepository
            .findByParkingIds(parkingIds)
            .map { OperationHoursDto.fromOperating(it) }//DTO 변환

    }
    @Transactional(readOnly = true)
    override fun getByParkingId(parkingId: Long): List<OperationHoursDto> {

        return getByParkingIds(listOf(parkingId)).ifEmpty { emptyList() }

    }


}
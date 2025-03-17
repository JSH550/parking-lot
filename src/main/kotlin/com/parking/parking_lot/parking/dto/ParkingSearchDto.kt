package com.parking.parking_lot.parking.dto

import com.parking.parking_lot.operationHours.dto.OperationHoursDto
import com.parking.parking_lot.parking.ParkingDocument


/**
 * Parking 검색 결과를 변환하여 사용자에게 전달하는 DTO
 *
 * - Elasticsearch에서 검색된 주차장 정보를 사용자 친화적인 형식으로 변환, API 응답시 사용
 */
data class ParkingSearchDto(
    val id: Long,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val address: String,
    val feePerHour: Int,
    val operatingHours: List<OperationHoursDto>
) {
    companion object {
        /**
         * Elasticsearch Document 를 DTO로 변환하는 메서드
         */
        fun fromParkingDocument(parkingDocument: ParkingDocument): ParkingSearchDto {
            return ParkingSearchDto(
                id = parkingDocument.id,
                name = parkingDocument.name,
                latitude = parkingDocument.location.lat,
                longitude = parkingDocument.location.lon,
                address = parkingDocument.address,
                feePerHour = parkingDocument.feePerHour,
                operatingHours = parkingDocument.operatingHours,

                )

        }


    }
}
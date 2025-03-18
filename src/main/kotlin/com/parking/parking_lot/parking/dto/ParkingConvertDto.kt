package com.parking.parking_lot.parking.dto

import com.parking.parking_lot.operationHours.dto.OperationHoursDto
import com.parking.parking_lot.parking.Ownership

/**
 * MySQL -> Elasticsearch Document 변환 시 사용될 DTO
 *
 * - MySQL에서 주차장 데이터를 가져와서 Elasticsearch 문서로 변환할 때 사용
 */

data class ParkingConvertDto(
    val id: Long,
    val name: String? = "이름",
    val address: String? = "주소",
    val latitude: Double,
    val longitude: Double,
    val feePerHour: Int? = 100,
    val ownership: Ownership?,
    val operatingHours: List<OperationHoursDto> // ✅ Reader에서 운영시간도 포

)
package com.parking.parking_lot.operationHours.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.parking.parking_lot.operationHours.OperatingHours


data class OperationHoursDto @JsonCreator constructor(
    @JsonProperty("dataType") val dataType: String,
    @JsonProperty("openTime") val openTime: String,
    @JsonProperty("closeTime") val closeTime: String
) {

    companion object {
        fun fromOperating(operatingHours: OperatingHours): OperationHoursDto {
            return OperationHoursDto(

                dataType = operatingHours.dateType.toString(),
                openTime = operatingHours.openTime,
                closeTime = operatingHours.closeTime,
            )
        }

    }
}
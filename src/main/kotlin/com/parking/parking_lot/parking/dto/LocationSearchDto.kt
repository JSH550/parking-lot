package com.parking.parking_lot.parking.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.validation.constraints.*

data class LocationSearchDto(




    @field:Pattern(regexp = """^\d+(km|m)$""", message = "거리 형식이 올바르지 않습니다. 예: '5km' 또는 '500m'")
    val distance:String,

    @field:Min(value = -90, message = "위도는 -90 이상이어야 합니다.")
    @field:Max(value = 90, message = "위도는 90 이하이어야 합니다.")
    val latitude: Double,

    @field:Min(value = -180, message = "경도는 -180 이상이어야 합니다.")
    @field:Max(value = 180, message = "경도는 180 이하이어야 합니다.")
    val longitude: Double


){

    /**
     *
     */
    @AssertTrue(message = "거리는 최대 50km 까지 설정할 수 있습니다.")
    @JsonIgnore
    fun isValidDistance(): Boolean{
        val regex = """^(\d+)(km|m)$""".toRegex()//숫자 + 단위로 분리
        val matchResult = regex.matchEntire(distance) ?:  return false //파라미터를 숫자와 단위로 분리

        val (value,unit)= matchResult.destructured//단위와 숫자를 각자 파라미터로 분리
        val numericValue = value.toInt();//숫자부 Int 로 변환

        return when (unit){
            "km" -> numericValue in 1..50 //1~50 km 허용
            "m" -> numericValue in 1..50000// 1~50000 허용
            else ->false//나머지 허용하지않음
        }

    }
}
